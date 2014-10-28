package ru.homeless.mappings;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.homeless.doctypes.DocumentFreeTravel;
import ru.homeless.entities.Client;
import ru.homeless.services.IGenericService;
import ru.homeless.shared.IDocumentMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maxim on 28.10.14.
 * Implementation of document processing
 */
@Configurable
public class DefaultDocumentMapping implements IDocumentMapping {


    public static final Logger log = Logger.getLogger(DefaultDocumentMapping.class);

    public IGenericService getGenericService() {
        return genericService;
    }

    public void setGenericService(IGenericService genericService) {
        this.genericService = genericService;
    }

    @Autowired
    private IGenericService genericService;

    @Override
    public XWPFDocument documentSocialHelpImpl(int requestType, int clientId, Date issueDate) {
        //params:
        /*
        documentNumber
        clientName
        clientDateOfBirthday
        documentDate
         */
        Client client = genericService.getInstanceById(Client.class, clientId);
        if (client == null) {
            return null;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("documentNumber", "123123123");
        map.put("clientName", client.getSurname()+" "+client.getFirstname()+" "+client.getMiddlename());
        map.put("clientDateOfBirthday", client.getDate().toString());
        map.put("documentDate", issueDate.toString());

        return new DocumentFreeTravel(map).replaceParametersInDocument();
    }

    @Override
    public XWPFDocument documentFreeTravelImpl(int requestType, int clientId, String travelCity, Date issueDate) {
        return null;
    }

    @Override
    public XWPFDocument documentSanitationImpl(int requestType, int clientId, Date issueDate) {
        return null;
    }
}
