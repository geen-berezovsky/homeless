package ru.homeless.mappings;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.*;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.services.IContractService;
import ru.homeless.shared.IDocumentMapping;
import ru.homeless.util.Util;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maxim on 30.11.14.
 */
@Component
public class DefaultContractMappingImpl implements ICustomMapping {

    public static final Logger log = Logger.getLogger(DefaultContractMappingImpl.class);

    @Autowired
    private IContractService contractService;

    @Override
    public XWPFDocument getDocument(Map<String, String> map) {
        //STUB
        return null;
    }

    @Override
    public XWPFDocument getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {

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
        XWPFDocument finalDocument = null;

        //file can contain the space in its path
        log.info("Destination contract file path is "+contractPath);
        if (new File(contractPath).exists()) {
            //the contract is already generated, return the document from disk
            log.info("The contract for client ID="+client.getId()+" already exist, taking it from the storage");
            try {
                FileInputStream fileInputStream = new FileInputStream(new File(contractPath));
                finalDocument = new XWPFDocument(fileInputStream);
                fileInputStream.close();

            } catch (FileNotFoundException e) {
                log.error("Cannot find the file "+sourceFile);
                e.printStackTrace();
            } catch (IOException e) {
                log.error("Cannot read the file "+sourceFile);
                e.printStackTrace();
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
            map.put("[label:import:worker_short_info]", workerData+", действующий на основании доверенности "+worker.getWarrantNum()+" от "+Util.convertDate(worker.getWarrantDate()));
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
            InputStream imageInByteArray = null;
            if (client.getAvatar() != null) {
                try {
                    imageInByteArray = client.getAvatar().getBinaryStream();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


            //generate the contract
            finalDocument = new DocTypeProcessor(map, IDocumentMapping.DOCUMENT_DEFAULT_CONTRACT_TEMPLATE_PATH).replaceParametersInDocument();


            // Save the contract to the disk in the proper path
            FileOutputStream fileOut = null;
            try {
                fileOut = new FileOutputStream(contractPath);
                finalDocument.write(fileOut);
                fileOut.close();
            } catch (FileNotFoundException e) {
                log.error("Cannot find the path specified ("+contractPath+")");
                e.printStackTrace();
            } catch (IOException e) {
                log.error("Cannot write to the file "+contractPath+", it may be busy");
                e.printStackTrace();
            }

        }


        return finalDocument;
    }

}
