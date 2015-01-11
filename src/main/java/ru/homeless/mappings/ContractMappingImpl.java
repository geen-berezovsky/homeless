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

@Component
public class ContractMappingImpl {

	public static final Logger log = Logger.getLogger(ContractMappingImpl.class);
	public WordprocessingMLPackage finalDocumentForSaving;
	public ServContract servContract; 
	public Worker worker;
	public String contractPath;
	public String contractDate;
	
	public HashMap<String, String> placeholdersAndValues;
	
    @Autowired
    private IContractService contractService;

	
	public void init(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {
		
        log.info("SERVICE="+contractService.toString());
       
        servContract = contractService.getInstanceById(ServContract.class, contractId);
        
        String sourceFile = client.getSurname() + "_" + client.getFirstname() + "_" + client.getId() + "_" + servContract.getId() + ".docx";
        log.info("Contracts dir = "+ Configuration.contractsDir);
        File contractsDir = new File(Configuration.contractsDir);
        if (!contractsDir.exists()) {
            contractsDir.mkdirs();
        }
        //Saving to the worker's profile
        worker = contractService.getInstanceById(Worker.class, workerId);
        
        File workerDir = new File(contractsDir.getAbsolutePath()+System.getProperty("file.separator")+worker.getSurname()+"_"+worker.getFirstname());
        if (!workerDir.exists()) {
            workerDir.mkdirs();
        }
        contractPath = workerDir.getAbsolutePath()+System.getProperty("file.separator")+sourceFile;

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
		
	}

	public void preparePlaceholdersMap(Client client, int contractId) {
		

		String contractNum = "";
		if (servContract.getDocNum() == null) {
			contractNum = String.valueOf(servContract.getId());
        } else {
        	contractNum = servContract.getDocNum();
        }

        Document workerDocument = contractService.getWorkerDocumentForContractByWorkerId(worker.getId());
        List<ContractControl> contractControls = contractService.getContractControlPointsByServContractId(servContract.getId());
        Document clientDocument = contractService.getClientDocumentForContractByContractId(contractId);
		
		contractDate = Util.convertDate(servContract.getStartDate());
		String workerShortInfo = Util.getWorkersBio(worker);
		String clientShortInfo = client.getSurname()+" "+client.getFirstname()+" "+client.getMiddlename()+", "+ Util.convertDate(client.getDate())+" г.р.";
		String clientInfo = clientShortInfo + ". "+clientDocument.getDoctype().getCaption()+" "+clientDocument.getDocPrefix()+" "+clientDocument.getDocNum()
				+" выдан "+Util.convertDate(clientDocument.getDate()) + " "+clientDocument.getWhereAndWhom();
		String workerInfo = workerShortInfo + ". "+workerDocument.getDoctype().getCaption()+" "+workerDocument.getDocPrefix()+" "+workerDocument.getDocNum()
				+" выдан "+Util.convertDate(workerDocument.getDate()) + " "+workerDocument.getWhereAndWhom();
		String orgInfo = IDocumentMapping.ORGANIZATION_INFO;
		String contractControlReplacement = String.valueOf((char)10);
        for (ContractControl cc : contractControls) {
            contractControlReplacement += "- "+cc.getContractpoints().getCaption() + ";"+String.valueOf((char)13);
        }
        
        placeholdersAndValues = new HashMap<String, String>();
        
        placeholdersAndValues.put("[label:import:contract_num]", contractNum);
        placeholdersAndValues.put("[label:import:contract_date]", contractDate);
        placeholdersAndValues.put("[label:import:worker_short_info]", workerShortInfo);
        placeholdersAndValues.put("[label:import:client_short_info]", clientShortInfo);
        placeholdersAndValues.put("[label:import:client_info]", clientInfo);
        placeholdersAndValues.put("[label:import:worker]", workerInfo);
        placeholdersAndValues.put("[label:import:org_info]", orgInfo);
        placeholdersAndValues.put("[label:import:contract_points]", contractControlReplacement);

        log.info("The contract for client ID="+client.getId()+" does not exist, creating, saving it to the storage and return to the requestor");
	}
	
	public byte[] attachPhoto(Client client) {
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
        return blobAsBytes;
	}
	

}
