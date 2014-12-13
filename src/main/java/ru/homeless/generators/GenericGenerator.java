package ru.homeless.generators;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.homeless.entities.Client;
import ru.homeless.parsers.CustomValuesHttpRequestParser;
import ru.homeless.services.IContractService;
import ru.homeless.shared.IDocumentMapping;
import ru.homeless.util.Util;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class GenericGenerator {

    @Autowired
    private CustomValuesHttpRequestParser hrp; //We need to inject all used classes where we need to inject our services

    @Autowired
    private IContractService contractService;

    public static final Logger log = Logger.getLogger(GenericGenerator.class);

    private Map<String, String> defaultValuesMap = null;

    private void putDefaultValuesInMap(Client client, String documentNumber, Date issueDate)  {
        if (defaultValuesMap == null) {
            defaultValuesMap = new HashMap<String, String>();
        }
        defaultValuesMap.put("[t:client:name]", client.getSurname()+" "+client.getFirstname()+" "+client.getMiddlename());
        defaultValuesMap.put("[t:num]", documentNumber);
        defaultValuesMap.put("[t:client:birth]", Util.convertDate(client.getDate()));
        defaultValuesMap.put("clientWhereWasBorn", client.getWhereWasBorn());
        defaultValuesMap.put("clientId", String.valueOf(client.getId()));
        defaultValuesMap.put("[t:today]", Util.convertDate(issueDate)); defaultValuesMap.put("[t:date]", Util.convertDate(issueDate)); //synonym

        defaultValuesMap.put("[t:signatory1]", IDocumentMapping.SIGN_PART_1);
        defaultValuesMap.put("[t:signatory2]", IDocumentMapping.SIGN_PART_2);

    }

    private Date parseIssueDate(HttpServletRequest request) {
        Date issueDate = null;
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        String sDate = request.getParameter("issueDate");
        if (sDate!=null && !sDate.trim().equals(""))
            try {
                issueDate = df.parse(sDate);
            } catch (ParseException e) {
                log.warn("Date "+sDate+" does not match the template dd.MM.yyyy, using the current date");
                issueDate = new Date();
                e.printStackTrace();
            }
        return issueDate;
    }


    public WordprocessingMLPackage generate(HttpServletRequest request) throws UnsupportedEncodingException {
        if (hrp == null ) {
            hrp = new CustomValuesHttpRequestParser();
        }


        Client client = contractService.getInstanceById(Client.class, Integer.parseInt(request.getParameter("clientId")));
        if (client == null) {
            log.error("Client is null (does not parsed properly)");
            return null;
        }
        log.info("Working with client "+client.getId());


        //Make global preparations
        putDefaultValuesInMap(client, "000000000000", parseIssueDate(request));

		switch (Integer.parseInt(request.getParameter("requestType"))) {
		
			//STANDARD DOCUMENTS
			case IDocumentMapping.DOCUMENT_SOCIAL_HELP: {
				return hrp.generateSocialHelpDocument(request, client, defaultValuesMap);
			}
			case IDocumentMapping.DOCUMENT_FREE_TRAVEL: {
				return hrp.generateFreeTravelDocument(request, client, defaultValuesMap);
			}
            case IDocumentMapping.DOCUMENT_SANITATION: {
                return hrp.generateSanitationDocument(request, client, defaultValuesMap);
            }
            case IDocumentMapping.DOCUMENT_DISPENSARY: {
                return hrp.generateDispensaryDocument(request, client, defaultValuesMap);
            }
            case IDocumentMapping.DOCUMENT_REGISTRATION: {
                return hrp.generateRegistrationDocument(request, client, defaultValuesMap);
            }
            case IDocumentMapping.DOCUMENT_TRANSIT: {
                return hrp.generateTransitDocument(request, client, defaultValuesMap);
            }
            case IDocumentMapping.DOCUMENT_ZAGS_QUERY: {
                return hrp.generateZAGSQueryDocument(request, client, defaultValuesMap);
            }
            case IDocumentMapping.DOCUMENT_CUSTOM: {
                return hrp.generateCustomDocument(request, client, defaultValuesMap);
            }
            
            //STANDARD CONTRACTS
            case IDocumentMapping.DOCUMENT_DEFAULT_CONTRACT: {
                return hrp.generateDefaultContract(request, client, defaultValuesMap);
            }
            case IDocumentMapping.DOCUMENT_SHELTER_CONTRACT: {
                return hrp.generateShelterContract(request, client, defaultValuesMap);
            }

            default: {
	   			return null;
		   }
		}
	}

}
