package ru.homeless.mappings;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.homeless.entities.Client;
import ru.homeless.entities.CustomDocumentRegistry;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.services.IContractService;
import ru.homeless.services.IGenericService;
import ru.homeless.shared.IDocumentMapping;

/**
 * Created by maxim on 30.11.14.
 */
@Component
public class CustomMappingImpl implements ICustomMappingWordDocument {

    public static final Logger log = Logger.getLogger(CustomMappingImpl.class);

    @Qualifier("GenericService")
    @Autowired
    private IGenericService genericService;

    @Override
    public WordprocessingMLPackage getDocument(Map map) {

        //Here we need to get all missing data from the database

        Integer docId = Integer.parseInt(map.get("[input:docId]").toString());

        if (docId == null || docId == 0) {
            return null;
        }

        CustomDocumentRegistry customDocumentRegistry = genericService.getInstanceById(CustomDocumentRegistry.class, docId);

        map.put("[input:num]", customDocumentRegistry.getDocNum());
        map.put("[enter:address]", customDocumentRegistry.getForWhom());
        map.put("[doc:cap]", customDocumentRegistry.getType());
        map.put("[enter:intro]", customDocumentRegistry.getPreamble());
        map.put("[enter:base]", customDocumentRegistry.getMainPart());
        map.put("[enter:fin]", customDocumentRegistry.getFinalPart());
        map.put("[director:signature]", customDocumentRegistry.getSignature());
        map.put("[worker:info]", customDocumentRegistry.getPerformerText());

        return new DocTypeProcessor(IDocumentMapping.DOCUMENT_CUSTOM_TEMPLATE_PATH).replaceParametersInDocument(map, null, 0);
    }

    @Override
    public WordprocessingMLPackage getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {
        return null;
    }
}
