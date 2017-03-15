package ru.homeless.mappings;

import java.io.File;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.Client;
import ru.homeless.entities.CustomDocumentRegistry;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.services.IContractService;
import ru.homeless.services.IGenericService;
import ru.homeless.shared.IDocumentMapping;
import ru.homeless.util.Util;

/**
 * Created by maxim on 30.11.14.
 */
@Component
public class CustomMappingImpl implements ICustomMappingWordDocument {

    public static final Logger log = Logger.getLogger(CustomMappingImpl.class);

    @Qualifier("GenericService")
    @Autowired
    private IGenericService genericService;

    private WordprocessingMLPackage finalDocumentForSaving;

    @Override
    public WordprocessingMLPackage getDocument(Map map, Client client) {

        Integer docId = Integer.parseInt(map.get("[input:docId]").toString());

        if (docId == null || docId == 0) {
            return null;
        }

        String sourceFile = "CustomDocument_"+client.getId()+"_"+docId+".docx";
        File profilesDir = new File(Configuration.profilesDir+System.getProperty("file.separator")+client.getId());
        if (!profilesDir.exists()) {
            profilesDir.mkdirs();
        }
        String custDocPath = profilesDir.getAbsolutePath()+System.getProperty("file.separator")+sourceFile;

        if (new File(custDocPath).exists()) { //the CustomDocument is already generated, return the document from disk
            log.info("The CustomDocument id = "+ docId+ " for client ID="+client.getId()+" already exist, taking it from the storage");
            try {
                finalDocumentForSaving = WordprocessingMLPackage.load(new java.io.File(custDocPath));
            } catch (Docx4JException e) {
                log.error(e.getMessage(),e);
            }
        } else { //otherwise, generate it
            //Here we need to get all missing data from the database
            CustomDocumentRegistry customDocumentRegistry = genericService.getInstanceById(CustomDocumentRegistry.class, docId);

            map.put("[input:num]", customDocumentRegistry.getDocNum());
            map.put("[enter:address]", customDocumentRegistry.getForWhom());
            map.put("[doc:cap]", customDocumentRegistry.getType());
            map.put("[enter:intro]", customDocumentRegistry.getPreamble());
            map.put("[enter:base]", customDocumentRegistry.getMainPart());
            map.put("[enter:fin]", customDocumentRegistry.getFinalPart());
            map.put("[director:signature]", customDocumentRegistry.getSignature());
            map.put("[worker:info]", customDocumentRegistry.getPerformerText());

            finalDocumentForSaving = new DocTypeProcessor(IDocumentMapping.DOCUMENT_CUSTOM_TEMPLATE_PATH).replaceParametersInDocument(map, null, 0);
            try {
                finalDocumentForSaving.save(new File(custDocPath));
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
