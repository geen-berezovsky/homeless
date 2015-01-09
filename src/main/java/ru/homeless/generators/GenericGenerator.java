package ru.homeless.generators;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class GenericGenerator {

    @Autowired
    private CustomValuesHttpRequestParser hrp; //We need to inject all used classes where we need to inject our services

    @Autowired
    private IContractService contractService;

    public static final Logger log = Logger.getLogger(GenericGenerator.class);

    private Map<String, String> wordDocumentDefaultValuesMap = null;

    public GenericGenerator() {
    	wordDocumentDefaultValuesMap = new HashMap<String, String>();
    }
    
    private void putDefaultValuesInMap(Client client, String documentNumber, Date issueDate)  {
        wordDocumentDefaultValuesMap.put("[t:client:name]", client.getSurname()+" "+client.getFirstname()+" "+client.getMiddlename());
        wordDocumentDefaultValuesMap.put("[t:num]", documentNumber);
        wordDocumentDefaultValuesMap.put("[t:client:birth]", Util.convertDate(client.getDate()));
        wordDocumentDefaultValuesMap.put("clientWhereWasBorn", client.getWhereWasBorn());
        wordDocumentDefaultValuesMap.put("clientId", String.valueOf(client.getId()));
        wordDocumentDefaultValuesMap.put("[t:today]", Util.convertDate(issueDate)); wordDocumentDefaultValuesMap.put("[t:date]", Util.convertDate(issueDate)); //synonym

        wordDocumentDefaultValuesMap.put("[t:signatory1]", IDocumentMapping.SIGN_PART_1);
        wordDocumentDefaultValuesMap.put("[t:signatory2]", IDocumentMapping.SIGN_PART_2);

    }

    public WordprocessingMLPackage generateWordDocument(HttpServletRequest request) throws UnsupportedEncodingException {
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
        putDefaultValuesInMap(client, "000000000000", Util.parseDate(request, "issueDate", log));

		switch (Integer.parseInt(request.getParameter("requestType"))) {
		
			//STANDARD DOCUMENTS
			case IDocumentMapping.DOCUMENT_SOCIAL_HELP: {
				return hrp.generateSocialHelpDocument(request, client, wordDocumentDefaultValuesMap);
			}
			case IDocumentMapping.DOCUMENT_FREE_TRAVEL: {
				return hrp.generateFreeTravelDocument(request, client, wordDocumentDefaultValuesMap);
			}
            case IDocumentMapping.DOCUMENT_SANITATION: {
                return hrp.generateSanitationDocument(request, client, wordDocumentDefaultValuesMap);
            }
            case IDocumentMapping.DOCUMENT_DISPENSARY: {
                return hrp.generateDispensaryDocument(request, client, wordDocumentDefaultValuesMap);
            }
            case IDocumentMapping.DOCUMENT_REGISTRATION: {
                return hrp.generateRegistrationDocument(request, client, wordDocumentDefaultValuesMap);
            }
            case IDocumentMapping.DOCUMENT_TRANSIT: {
                return hrp.generateTransitDocument(request, client, wordDocumentDefaultValuesMap);
            }
            case IDocumentMapping.DOCUMENT_ZAGS_QUERY: {
                return hrp.generateZAGSQueryDocument(request, client, wordDocumentDefaultValuesMap);
            }
            case IDocumentMapping.DOCUMENT_CUSTOM: {
                return hrp.generateCustomDocument(request, client, wordDocumentDefaultValuesMap);
            }
            
            //STANDARD CONTRACTS
            case IDocumentMapping.DOCUMENT_DEFAULT_CONTRACT: {
                return hrp.generateDefaultContract(request, client, wordDocumentDefaultValuesMap);
            }
            case IDocumentMapping.DOCUMENT_SHELTER_CONTRACT: {
                return hrp.generateShelterContract(request, client, wordDocumentDefaultValuesMap);
            }
        

            default: {
	   			return null;
		   }
		}
	}

    public SpreadsheetMLPackage generateExcelDocument(HttpServletRequest request) throws UnsupportedEncodingException {
        if (hrp == null ) {
            hrp = new CustomValuesHttpRequestParser();
        }

		switch (Integer.parseInt(request.getParameter("requestType"))) {
		
			case IDocumentMapping.REPORT_WORK_RESULT: {
				return hrp.generateWorkReportDocument(request);
			}
			case IDocumentMapping.REPORT_OUT_OF_SHELTER: {
				return hrp.generateOutShelterDocument(request);
			}
			case IDocumentMapping.REPORT_ONE_TIME_SERVICES: {
				return hrp.generateOneTimeServicesDocument(request);
			}
			case IDocumentMapping.REPORT_OVERVAC: {
				return hrp.generateOverVacDocument(request);
			}
			case IDocumentMapping.REPORT_OUTER: {
				return hrp.generateOuterDocument(request);
			}
			case IDocumentMapping.REPORT_OLD_SCHOOL: {
				return hrp.generateOldSchoolDocument(request);
			}

            default: {
	   			return null;
		   }
		}
	}



}
