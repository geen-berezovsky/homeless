package ru.homeless.mappings;

import java.util.Map;

import javax.servlet.ServletContext;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.homeless.entities.Client;
import ru.homeless.entities.CustomDocumentRegistry;
import ru.homeless.entities.ZAGSRequestDocumentRegistry;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.services.IGenericService;
import ru.homeless.shared.IDocumentMapping;

/**
 * Created by maxim on 30.11.14.
 */
@Component
public class ZagsQueryMappingImpl implements ICustomMappingWordDocument {

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

        ZAGSRequestDocumentRegistry zagsRequestDocumentRegistry = genericService.getInstanceById(ZAGSRequestDocumentRegistry.class, docId);

        map.put("[m]", zagsRequestDocumentRegistry.getMother());
        map.put("[f]", zagsRequestDocumentRegistry.getFather());
        map.put("[tosend]", zagsRequestDocumentRegistry.getForWhom());
        map.put("[address]", zagsRequestDocumentRegistry.getAddress());

        map.put("[t:client:name]", zagsRequestDocumentRegistry.getName());
        map.put("[t:city]", zagsRequestDocumentRegistry.getWhereWasBorn());

        return new DocTypeProcessor(IDocumentMapping.DOCUMENT_ZAGS_QUERY_TEMPLATE_PATH).replaceParametersInDocument(map, null,0);
    }

    @Override
    public WordprocessingMLPackage getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {
        return null;
    }
}
