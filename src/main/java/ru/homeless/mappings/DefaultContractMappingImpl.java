package ru.homeless.mappings;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.homeless.configuration.Configuration;
import ru.homeless.entities.Client;
import ru.homeless.entities.ContractControl;
import ru.homeless.entities.Document;
import ru.homeless.entities.ServContract;
import ru.homeless.entities.Worker;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.services.IContractService;
import ru.homeless.shared.IDocumentMapping;
import ru.homeless.util.Util;

/**
 * Created by maxim on 30.11.14.
 */
@Component
public class DefaultContractMappingImpl implements ICustomMappingWordDocument {

    public static final Logger log = Logger.getLogger(DefaultContractMappingImpl.class);

    @Autowired
    private IContractService contractService;

    @Override
    public WordprocessingMLPackage getDocument(Map<String, String> map) {
        //STUB
        return null;
    }

    @Override
    public WordprocessingMLPackage getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {

        //check that this contract is not exist in storage, otherwise take it and send to the requestor
        log.info("SERVICE="+contractService.toString());
        ServContract servContract = contractService.getInstanceById(ServContract.class, contractId);
        String sourceFile = client.getSurname() + "_" + client.getFirstname() + "_" + client.getId() + "_" + servContract.getId() + ".docx";
        log.info("Contracts dir = "+ Configuration.contractsDir);
        File contractsDir = new File(Configuration.contractsDir);
        if (!contractsDir.exists()) {
            contractsDir.mkdirs();
        }
        //Saving to the worker's profile
        Worker worker = contractService.getInstanceById(Worker.class, workerId);
        
        File workerDir = new File(contractsDir.getAbsolutePath()+System.getProperty("file.separator")+worker.getSurname()+"_"+worker.getFirstname());
        if (!workerDir.exists()) {
            workerDir.mkdirs();
        }
        String contractPath = workerDir.getAbsolutePath()+System.getProperty("file.separator")+sourceFile;

        //prepare the result filename of the generated contract
        String resultFilename = sourceFile.replaceAll(" ","*");
        try {
            resultFilename = URLEncoder.encode(resultFilename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        resultFilename = resultFilename.replaceAll("\\*","%20");
        context.setAttribute("resultFileName",resultFilename);

        //file can contain the space in its path
        log.info("Destination contract file path is "+contractPath);
        WordprocessingMLPackage finalDocumentForSaving = null;
		if (new File(contractPath).exists()) {
            //the contract is already generated, return the document from disk
            log.info("The contract for client ID="+client.getId()+" already exist, taking it from the storage");
            try {
                finalDocumentForSaving = WordprocessingMLPackage.load(new java.io.File(contractPath));
			} catch (Docx4JException e) {
				log.error(e.getMessage(),e);
			}
        } else {
            map = new HashMap<String, String>();

            //prepare replacements for placeholders
            if (servContract.getDocNum() == null) {
                map.put("[label:import:contract_num]",String.valueOf(servContract.getId()));
            } else {
                map.put("[label:import:contract_num]",servContract.getDocNum());
            }

            Document workerDocument = contractService.getWorkerDocumentForContractByWorkerId(worker.getId());
            Document clientDocument = contractService.getClientDocumentForContractByContractId(contractId);
            String clientData = client.getSurname()+" "+client.getFirstname()+" "+client.getMiddlename()+", "+ Util.convertDate(client.getDate())+" г.р.";
            String workerData = worker.getRules().getCaption()+" "+worker.getSurname()+" "+worker.getFirstname()+" "+worker.getMiddlename();

            map.put("[label:import:contract_date]", Util.convertDate(servContract.getStartDate()));
            map.put("[label:import:worker_short_info]", Util.getWorkersBio(worker));
            map.put("[label:import:client_short_info]", clientData);

            map.put("[label:import:client_info]", clientData+". "+clientDocument.getDoctype().getCaption()+" "+clientDocument.getDocPrefix()+" "+clientDocument.getDocNum()+" выдан "+Util.convertDate(clientDocument.getDate()) + " "+clientDocument.getWhereAndWhom());

            map.put("[label:import:worker]", workerData+". "+workerDocument.getDoctype().getCaption()+" "+workerDocument.getDocPrefix()+" "+workerDocument.getDocNum()+" выдан "+Util.convertDate(workerDocument.getDate()) + " "+workerDocument.getWhereAndWhom());
            map.put("[label:import:org_info]", IDocumentMapping.ORGANIZATION_INFO);

            List<ContractControl> contractControls = contractService.getContractControlPointsByServContractId(servContract.getId());
            String contractControlReplacement = String.valueOf((char)10);
            for (ContractControl cc : contractControls) {
                contractControlReplacement += "- "+cc.getContractpoints().getCaption() + ";"+String.valueOf((char)13);
            }
            map.put("[label:import:contract_points]", contractControlReplacement);

            log.info("The contract for client ID="+client.getId()+" does not exist, creating, saving it to the storage and return to the requestor");



            //INSERT PHOTO
            byte[] blobAsBytes = null;
            if (client.getAvatar() != null) {
            	Blob blob = client.getAvatar();

            	int blobLength;
				try {
					blobLength = (int) blob.length();
	            	blobAsBytes = blob.getBytes(1, blobLength);

	            	//release the blob and free up memory. (since JDBC 4.0)
	            	blob.free();
				} catch (SQLException e) {
					log.error(e.getMessage(),e);
				}  
            }
            
            //generate the contract
            finalDocumentForSaving = new DocTypeProcessor(IDocumentMapping.DOCUMENT_DEFAULT_CONTRACT_TEMPLATE_PATH).replaceParametersInDocument(map, blobAsBytes, ICustomMappingWordDocument.AVATAR_LOCATION_TOP_RIGHT);

            
            //XWPFParagraph pictureSection = finalDocument.createParagraph();
            //pictureSection.setAlignment(ParagraphAlignment.CENTER);
            //XWPFRun pictureSectionRunOne = pictureSection.createRun();
            
     /*
			try {
				XWPFParagraph paragraphX = finalDocument.createParagraph();
                paragraphX.setAlignment(ParagraphAlignment.RIGHT);
                
                String blipId = paragraphX.getDocument().addPictureData(new FileInputStream(new File("c:/newfile.jpg")), CustomXWPFDocument.PICTURE_TYPE_JPEG);
                finalDocument.createPicture(blipId,finalDocument.getNextPicNameNumber(org.apache.poi.xwpf.usermodel.Document.PICTURE_TYPE_JPEG), 176, 144);
			} catch (IOException | InvalidFormatException e2) {
				log.error(e2.getMessage(), e2);
			}
*/            
                try {
					finalDocumentForSaving.save(new File(contractPath));
				} catch (Docx4JException e) {
					log.error(e.getMessage(),e);
				}

        }
        
        return finalDocumentForSaving;
    }

}
