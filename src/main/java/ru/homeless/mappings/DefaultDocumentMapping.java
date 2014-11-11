package ru.homeless.mappings;

import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.*;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.services.IContractService;
import ru.homeless.shared.IDocumentMapping;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by maxim on 28.10.14.
 * Implementation of document processing
 */
@Component
public class DefaultDocumentMapping implements IDocumentMapping {


    public static final Logger log = Logger.getLogger(DefaultDocumentMapping.class);

    @Autowired
    private IContractService contractService;

    private Map<String, String> map = null;

    private String convertDate(Date date) {
        if (date == null) {
            return null;
        } else {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            return (df.format(date));
        }
    }

    private void putDefaultValuesInMap(Client client, String documentNumber, Date issueDate)  {
        map.put("[t:client:name]", client.getSurname()+" "+client.getFirstname()+" "+client.getMiddlename());
        map.put("[t:num]", documentNumber);
        map.put("[t:client:birth]", convertDate(client.getDate()));
        map.put("clientWhereWasBorn", client.getWhereWasBorn());
        map.put("clientId", String.valueOf(client.getId()));
        map.put("[t:today]", convertDate(issueDate));

        //[t:signatory1]
        //[t:signatory2]

    }

    @Override
    public HWPFDocument documentSocialHelpImpl(int requestType, int clientId, Date issueDate, ServletContext context) {
        Client client = contractService.getInstanceById(Client.class, clientId);
        if (client == null) {
            return null;
        }
        map = new HashMap<String, String>();
        putDefaultValuesInMap(client, "123123123", issueDate);
        return new DocTypeProcessor(map, context, IDocumentMapping.DOCUMENT_SOCIAL_HELP_TEMPLATE_PATH).replaceParametersInDocument();
    }

    @Override
    public HWPFDocument documentFreeTravelImpl(int requestType, int clientId, String travelCity, Date issueDate, ServletContext context)  {
        Client client = contractService.getInstanceById(Client.class, clientId);
        if (client == null) {
            return null;
        }
        map = new HashMap<String, String>();
        putDefaultValuesInMap(client, "456456456", issueDate);
        map.put("[t:city]", travelCity);
        return new DocTypeProcessor(map, context, IDocumentMapping.DOCUMENT_FREE_TRAVEL_TEMPLATE_PATH).replaceParametersInDocument();
    }

    @Override
    public HWPFDocument documentSanitationImpl(int requestType, int clientId, Date issueDate, ServletContext context)  {
        Client client = contractService.getInstanceById(Client.class, clientId);
        if (client == null) {
            return null;
        }
        map = new HashMap<String, String>();
        putDefaultValuesInMap(client, "789789789", issueDate);
        return new DocTypeProcessor(map, context, IDocumentMapping.DOCUMENT_SANITATION_TEMPLATE_PATH).replaceParametersInDocument();
    }

    @Override
    public HWPFDocument documentDefaultContractImpl(int requestType, int clientId, Date issueDate, int contractId, int workerId, ServletContext context) throws UnsupportedEncodingException {
        Client client = contractService.getInstanceById(Client.class, clientId);
        if (client == null) {
            return null;
        }

        //check that this contract is not exist in storage, otherwise take it and send to the requestor
        ServContract servContract = contractService.getInstanceById(ServContract.class, contractId);
        String sourceFile = client.getSurname() + "_" + client.getFirstname() + "_" + client.getId() + "_" + servContract.getId() + ".doc";
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
        resultFilename = URLEncoder.encode(resultFilename,"UTF-8");
        resultFilename = resultFilename.replaceAll("\\*","%20");
        context.setAttribute("resultFileName",resultFilename);
        HWPFDocument finalDocument = null;

        //file can contain the space in its path
        log.info("Destination contract file path is "+contractPath);
        if (new File(contractPath).exists()) {
            //the contract is already generated, return the document from disk
            log.info("The contract for client ID="+client.getId()+" already exist, taking it from the storage");
            try {
                FileInputStream fileInputStream = new FileInputStream(new File(contractPath));
                finalDocument = new HWPFDocument(fileInputStream);
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
            putDefaultValuesInMap(client, "789789789", issueDate);

            //prepare replacements for placeholders
            if (servContract.getDocNum() == null) {
                map.put("[label:import:contract_num]",String.valueOf(servContract.getId()));
            } else {
                map.put("[label:import:contract_num]",servContract.getDocNum());
            }

            Document workerDocument = contractService.getWorkerDocumentForContractByWorkerId(worker.getId());
            Document clientDocument = contractService.getClientDocumentForContractByContractId(contractId);
            String clientData = client.getSurname()+" "+client.getFirstname()+" "+client.getMiddlename()+", "+convertDate(client.getDate())+" г.р.";
            String workerData = worker.getRules().getCaption()+" "+worker.getSurname()+" "+worker.getFirstname()+" "+worker.getMiddlename();

            map.put("[label:import:contract_date]", convertDate(servContract.getStartDate()));
            map.put("[label:import:worker_short_info]", workerData+", действующий на основании доверенности "+worker.getWarrantNum()+" от "+convertDate(worker.getWarrantDate()));
            map.put("[label:import:client_short_info]", clientData);

            map.put("[label:import:client_info]", clientData+". "+clientDocument.getDoctype().getCaption()+" "+clientDocument.getDocPrefix()+" "+clientDocument.getDocNum()+" выдан "+convertDate(clientDocument.getDate()) + " "+clientDocument.getWhereAndWhom());

            map.put("[label:import:worker]", workerData+". "+workerDocument.getDoctype().getCaption()+" "+workerDocument.getDocPrefix()+" "+workerDocument.getDocNum()+" выдан "+convertDate(workerDocument.getDate()) + " "+workerDocument.getWhereAndWhom());
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
            finalDocument = new DocTypeProcessor(map, context, IDocumentMapping.DOCUMENT_DEFAULT_CONTRACT_TEMPLATE_PATH).replaceParametersInDocument();


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
