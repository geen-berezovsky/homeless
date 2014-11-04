package ru.homeless.mappings;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.homeless.entities.Client;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.services.IGenericService;
import ru.homeless.shared.IDocumentMapping;

import javax.servlet.ServletContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by maxim on 28.10.14.
 * Implementation of document processing
 */
@Component
public class DefaultDocumentMapping implements IDocumentMapping {


    public static final Logger log = Logger.getLogger(DefaultDocumentMapping.class);

    @Autowired
    private IGenericService genericService;

    private Map<String, String> map = null;

    private void putDefaultValuesInMap(Client client, String documentNumber, Date issueDate)  {
        map.put("[clientName]", client.getSurname()+" "+client.getFirstname()+" "+client.getMiddlename());
        map.put("[documentNumber]", documentNumber);
        map.put("[clientDateOfBirthday]", client.getDate().toString());
        map.put("[clientWhereWasBorn]", client.getWhereWasBorn());
        map.put("[clientId]", String.valueOf(client.getId()));
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        String documentDate = df.format(issueDate);
        map.put("[documentDate]", documentDate);
    }

    @Override
    public XWPFDocument documentSocialHelpImpl(int requestType, int clientId, Date issueDate, ServletContext context) {
        Client client = genericService.getInstanceById(Client.class, clientId);
        if (client == null) {
            return null;
        }
        map = new HashMap<String, String>();
        putDefaultValuesInMap(client, "123123123", issueDate);
        return new DocTypeProcessor(map, context, IDocumentMapping.DOCUMENT_SOCIAL_HELP_TEMPLATE_PATH).replaceParametersInDocument();
    }

    @Override
    public XWPFDocument documentFreeTravelImpl(int requestType, int clientId, String travelCity, Date issueDate, ServletContext context)  {
        Client client = genericService.getInstanceById(Client.class, clientId);
        if (client == null) {
            return null;
        }
        map = new HashMap<String, String>();
        putDefaultValuesInMap(client, "456456456", issueDate);
        map.put("[travelCity]", travelCity);
        return new DocTypeProcessor(map, context, IDocumentMapping.DOCUMENT_FREE_TRAVEL_TEMPLATE_PATH).replaceParametersInDocument();
    }

    @Override
    public XWPFDocument documentSanitationImpl(int requestType, int clientId, Date issueDate, ServletContext context)  {
        Client client = genericService.getInstanceById(Client.class, clientId);
        if (client == null) {
            return null;
        }
        map = new HashMap<String, String>();
        putDefaultValuesInMap(client, "789789789", issueDate);
        return new DocTypeProcessor(map, context, IDocumentMapping.DOCUMENT_SANITATION_TEMPLATE_PATH).replaceParametersInDocument();
    }
}
