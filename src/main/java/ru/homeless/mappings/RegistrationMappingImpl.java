package ru.homeless.mappings;

import java.util.Map;

import javax.servlet.ServletContext;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.homeless.entities.Client;
import ru.homeless.entities.Document;
import ru.homeless.entities.RegistrationDocumentRegistry;
import ru.homeless.entities.ZAGSRequestDocumentRegistry;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.services.IGenericService;
import ru.homeless.shared.IDocumentMapping;
import ru.homeless.util.Util;

/**
 * Created by maxim on 30.11.14.
 */
@Component
public class RegistrationMappingImpl implements ICustomMappingWordDocument {

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

        RegistrationDocumentRegistry registrationDocumentRegistry = genericService.getInstanceById(RegistrationDocumentRegistry.class, docId);

        map.put("[t:dateFrom]", registrationDocumentRegistry.getDateFrom());
        map.put("[t:dateTill]", registrationDocumentRegistry.getDateTill());

        Document document = genericService.getInstanceById(Document.class, registrationDocumentRegistry.getDocumentId());

        String documentData = document.getDoctype().getCaption()+" "+document.getDocPrefix()+" "+document.getDocNum()+" выдан "+ Util.convertDate(document.getDate()) + " "+document.getWhereAndWhom();

        map.put("[t:client:document]", documentData);
        map.put("[t:reg:date]", "??????????????");

        return new DocTypeProcessor(IDocumentMapping.DOCUMENT_REGISTRATION_TEMPLATE_PATH).replaceParametersInDocument(map, null,0);
    }

    @Override
    public WordprocessingMLPackage getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {
        return null;
    }
}
