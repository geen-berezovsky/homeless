package ru.homeless.mappings;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletContext;

import com.github.aleksandy.petrovich.Gender;
import org.apache.log4j.Logger;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.Client;
import ru.homeless.entities.CustomDocumentRegistry;
import ru.homeless.entities.ZAGSRequestDocumentRegistry;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.services.IGenericService;
import ru.homeless.shared.IDocumentMapping;
import ru.homeless.util.Util;

/**
 * Created by maxim on 30.11.14.
 */
@Component
public class ZagsQueryMappingImpl implements ICustomMappingWordDocument {

    public static final Logger log = Logger.getLogger(ZagsQueryMappingImpl.class);

    @Qualifier("GenericService")
    @Autowired
    private IGenericService genericService;

    private WordprocessingMLPackage finalDocumentForSaving;

    @Override
    public WordprocessingMLPackage getDocument(Map map, Client client) throws IOException {
        Integer docId = Integer.parseInt(map.get("[input:docId]").toString());

        if (docId == null || docId == 0) {
            return null;
        }


        String sourceFile = "ZagsDocument_"+client.getId()+"_"+docId+".docx";
        File profilesDir = new File(Configuration.profilesDir+System.getProperty("file.separator")+client.getId());
        if (!profilesDir.exists()) {
            profilesDir.mkdirs();
        }
        String zagsDocPath = profilesDir.getAbsolutePath()+System.getProperty("file.separator")+sourceFile;

        if (new File(zagsDocPath).exists()) { //the CustomDocument is already generated, return the document from disk
            log.info("The ZagsDocument id = "+ docId+ " for client ID="+client.getId()+" already exist, taking it from the storage");
            try {
                finalDocumentForSaving = WordprocessingMLPackage.load(new java.io.File(zagsDocPath));
            } catch (Docx4JException e) {
                log.error(e.getMessage(),e);
            }
        } else { //otherwise, generate it
            //Here we need to get all missing data from the database
            ZAGSRequestDocumentRegistry zagsRequestDocumentRegistry = genericService.getInstanceById(ZAGSRequestDocumentRegistry.class, docId);

            map.put("[m]", zagsRequestDocumentRegistry.getMother());
            map.put("[f]", zagsRequestDocumentRegistry.getFather());
            map.put("[tosend]", zagsRequestDocumentRegistry.getForWhom());
            map.put("[address]", zagsRequestDocumentRegistry.getAddress());
            map.put("[to:address]", zagsRequestDocumentRegistry.getRespAddress());

            String name = client.getSurname() + " " + client.getFirstname() + " " + client.getMiddlename();
            map.put("[t:custom:client:name]", name + " " + Util.convertDate(client.getDate())+" г.р.");
            map.put("[t:custom_city]", zagsRequestDocumentRegistry.getWhereWasBorn());

            Gender gender = null;
            if (client.isGender()) {
                gender = Gender.MALE;
            } else {
                gender = Gender.FEMALE;
            }

            map.put("[t:from_rod_padezh]", URLDecoder.decode(Util.getFIORodPadezh(client.getSurname(), client.getFirstname(), client.getMiddlename(), gender), "UTF-8"));
            map.put("[t:initials]", client.getSurname() + " " + client.getFirstname().substring(0,1).toUpperCase() + ". " + client.getMiddlename().substring(0,1).toUpperCase());

            if (client.isGender()) {
                map.put("[t:urozhden]", "уроженец");
            } else {
                map.put("[t:urozhden]", "уроженка");
            }

            finalDocumentForSaving = new DocTypeProcessor(IDocumentMapping.DOCUMENT_ZAGS_QUERY_TEMPLATE_PATH).replaceParametersInDocument(map, null,0);
            try {
                finalDocumentForSaving.save(new File(zagsDocPath));
            } catch (Docx4JException e) {
                log.error(e.getMessage(),e);
            }
        }

        return finalDocumentForSaving;
    }

    @Override
    public WordprocessingMLPackage getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {
        return null;
    }



    public WordprocessingMLPackage getFinalDocumentForSaving() {
        return finalDocumentForSaving;
    }

    public void setFinalDocumentForSaving(WordprocessingMLPackage finalDocumentForSaving) {
        this.finalDocumentForSaving = finalDocumentForSaving;
    }

}
