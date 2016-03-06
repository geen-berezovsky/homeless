package ru.homeless.mappings;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletContext;

import com.github.aleksandy.petrovich.Gender;
import org.apache.log4j.Logger;
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


    @Override
    public WordprocessingMLPackage getDocument(Map map, Client client) throws IOException {


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



        return new DocTypeProcessor(IDocumentMapping.DOCUMENT_ZAGS_QUERY_TEMPLATE_PATH).replaceParametersInDocument(map, null,0);
    }

    @Override
    public WordprocessingMLPackage getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {
        return null;
    }
}
