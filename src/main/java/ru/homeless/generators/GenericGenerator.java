package ru.homeless.generators;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.homeless.configuration.Configuration;
import ru.homeless.entities.Client;
import ru.homeless.entities.Worker;
import ru.homeless.parsers.CustomValuesHttpRequestParser;
import ru.homeless.services.IContractService;
import ru.homeless.shared.IDocumentMapping;
import ru.homeless.util.Util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
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
    
    private void putDefaultValuesInMap(Client client, Date issueDate, Worker worker)  {

        if (issueDate == null) {
            issueDate = new Date();
        }

        wordDocumentDefaultValuesMap.put("[t:client:name]", client.getSurname()+" "+client.getFirstname()+" "+client.getMiddlename());
        wordDocumentDefaultValuesMap.put("[t:client:birth]", Util.convertDate(client.getDate()));
        wordDocumentDefaultValuesMap.put("clientWhereWasBorn", client.getWhereWasBorn());
        wordDocumentDefaultValuesMap.put("clientId", String.valueOf(client.getId()));
        wordDocumentDefaultValuesMap.put("[t:today]", Util.convertDate(issueDate));
        wordDocumentDefaultValuesMap.put("[t:date]", Util.convertDate(issueDate)); //synonym

        if (Boolean.parseBoolean(Configuration.useSingleSignature)) {
            worker = contractService.getInstanceById(Worker.class, 1);
        }
        wordDocumentDefaultValuesMap.put("[t:signatory1]", worker.getRules().getCaption()+ " "+Configuration.orgName);
        wordDocumentDefaultValuesMap.put("[t:signatory2]", worker.getSurname()+" "+worker.getFirstname()+" "+worker.getMiddlename());

    }

    private void putDocNumToDefaultMap(HttpServletRequest request) {
        wordDocumentDefaultValuesMap.put("[t:num]", request.getParameter("docNum"));
    }

    public WordprocessingMLPackage generateWordDocument(HttpServletRequest request) throws IOException {
        if (hrp == null ) {
            hrp = new CustomValuesHttpRequestParser();
        }

        Client client = contractService.getInstanceById(Client.class, Integer.parseInt(request.getParameter("clientId")));
        if (client == null) {
            log.error("Client is null (does not parsed properly)");
            return null;
        }
        log.info("Working with client "+client.getId());

        String workerId = "";
        workerId = request.getParameter("workerId");
        if (workerId.equals("")) {
            log.info("Unknown workerId");
            return null;
        }
        Worker w = contractService.getInstanceById(Worker.class, Integer.parseInt(request.getParameter("workerId")));
        if (w == null) {
            log.error("Worker is null (does not parsed properly)");
            return null;
        }
        log.info("Worker is "+w.getId());

        //Make global preparations
        putDefaultValuesInMap(client, Util.parseDate(request, "issueDate", log), w);

		switch (Integer.parseInt(request.getParameter("requestType"))) {
		
			//STANDARD DOCUMENTS
			case IDocumentMapping.DOCUMENT_SOCIAL_HELP: {
                putDocNumToDefaultMap(request);
				return hrp.generateSocialHelpDocument(request, client, wordDocumentDefaultValuesMap);
			}
			case IDocumentMapping.DOCUMENT_FREE_TRAVEL: {
                putDocNumToDefaultMap(request);
				return hrp.generateFreeTravelDocument(request, client, wordDocumentDefaultValuesMap);
			}
            case IDocumentMapping.DOCUMENT_SANITATION: {
                putDocNumToDefaultMap(request);
                return hrp.generateSanitationDocument(request, client, wordDocumentDefaultValuesMap);
            }
            case IDocumentMapping.DOCUMENT_DISPENSARY: {
                putDocNumToDefaultMap(request);
                return hrp.generateDispensaryDocument(request, client, wordDocumentDefaultValuesMap);
            }
            case IDocumentMapping.DOCUMENT_REGISTRATION: {
                putDocNumToDefaultMap(request);
                return hrp.generateRegistrationDocument(request, client, wordDocumentDefaultValuesMap);
            }
            case IDocumentMapping.DOCUMENT_TRANSIT: {
                putDocNumToDefaultMap(request);
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

    public XSSFWorkbook generateExcelDocument(HttpServletRequest request) throws UnsupportedEncodingException {
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
            case IDocumentMapping.REPORT_INNER: {
                return hrp.generateInnerReport(request);
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
			case IDocumentMapping.REPORT_CUSTOM_STATISTICS: {
				return hrp.generateCustomStatisticsDocument(request);
			}

            default: {
	   			return null;
		   }
		}
	}



}
