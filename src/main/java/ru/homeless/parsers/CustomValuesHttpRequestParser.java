/*
 * 
 */
package ru.homeless.parsers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.homeless.entities.Client;
import ru.homeless.mappings.CustomMappingImpl;
import ru.homeless.mappings.DefaultContractMappingImpl;
import ru.homeless.mappings.DispensaryMappingImpl;
import ru.homeless.mappings.FreeTravelMappingImpl;
import ru.homeless.mappings.OneTimeServicesReportMappingImpl;
import ru.homeless.mappings.OutOfShelterReportMappingImpl;
import ru.homeless.mappings.OuterReportMappingImpl;
import ru.homeless.mappings.OverVacReportMappingImpl;
import ru.homeless.mappings.RegistrationMappingImpl;
import ru.homeless.mappings.ResultWorkReportMappingImpl;
import ru.homeless.mappings.SanitationMappingImpl;
import ru.homeless.mappings.ShelterContractMappingImpl;
import ru.homeless.mappings.SocialHelpMappingImpl;
import ru.homeless.mappings.TransitMappingImpl;
import ru.homeless.mappings.ZagsQueryMappingImpl;
import ru.homeless.shared.IDocumentMapping;
import ru.homeless.util.Util;

@Component
public class CustomValuesHttpRequestParser implements IDocumentMapping {
	
	public static final Logger log = Logger.getLogger(CustomValuesHttpRequestParser.class);

    //Class should be annotated as @Component and child dependencies should be injected with @Autowires annotation
    //If you need use Services for woriking with database in child classes (re-inject service)


    @Autowired
    private CustomMappingImpl customMappingImpl;

    @Autowired
    private DefaultContractMappingImpl defaultContractMappingImpl;

    @Autowired
    private ShelterContractMappingImpl shelterContractMapping;

    @Autowired
    private ResultWorkReportMappingImpl resultWorkReportMappingImpl;
    
    @Autowired
    private OutOfShelterReportMappingImpl outOfShelterReportMappimgImpl;
    
    @Autowired
    private OneTimeServicesReportMappingImpl oneTimeServicesReportMappingImpl;
    
    @Autowired
    private OverVacReportMappingImpl overVacReportMappingImpl;
    
    @Autowired
    private OuterReportMappingImpl outerReportMappingImpl;

    @Autowired
    private OuterReportMappingImpl oldSchoolReportMappingImpl;

    @Autowired
    private ZagsQueryMappingImpl zagsQueryMappingImpl;

    @Autowired
    private RegistrationMappingImpl registrationMappingImpl;
    
    //HERE WE PARSE CUSTOM VALUES LIKE travelCity
    
    
    /**
     * Parses the travel city.
     *
     * @param request the request
     * @return the string
     */
    private String parseTravelCity(HttpServletRequest request) {
        String travelCity = request.getParameter("travelCity");
        if (travelCity == null || travelCity.equals("")) {
            return "ГОРОД НЕ УКАЗАН!";
        } else {
            return travelCity;
        }
    }
    
    
    private String parseCustomParams(HttpServletRequest request, String param, String err) {
        String paramValue = request.getParameter(param);
        if (paramValue == null || paramValue.equals("")) {
            return err;
        } else {
            return paramValue;
        }
    }

    private int parseContractId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("contractId"));
    }

    private int parseWorkerId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("workerId"));
    }

    private ServletContext parseContext(HttpServletRequest request) {
        return request.getSession().getServletContext();
    }

	public WordprocessingMLPackage generateSocialHelpDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        return new SocialHelpMappingImpl().getDocument(map);
	}

	public WordprocessingMLPackage generateFreeTravelDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        //Adding custom values
        map.put("[t:city]", parseTravelCity(request));
        return new FreeTravelMappingImpl().getDocument(map);
	}

    public WordprocessingMLPackage generateSanitationDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        return new SanitationMappingImpl().getDocument(map);
    }

    @Override
    public WordprocessingMLPackage generateDispensaryDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        return new DispensaryMappingImpl().getDocument(map);
    }

    @Override
    public WordprocessingMLPackage generateRegistrationDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        return registrationMappingImpl.getDocument(map);
    }

    @Override
    public WordprocessingMLPackage generateTransitDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        return new TransitMappingImpl().getDocument(map);
    }

    @Override
    /**
     * Parses the ZAGS Document.
     *
     * @param HttpServletRequest request 
     * variables: 
     * m = mother's name
     * f = father's name
     * toSend = address of the recipient
     * address = address of the sender
     * Client client
     * Map<String, String> map
     * @return XWPFDocument
     */
    public WordprocessingMLPackage generateZAGSQueryDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        map.put("[input:docId]", parseCustomParams(request, "docId", ""));
        return zagsQueryMappingImpl.getDocument(map);
    }

    @Override
    /**
     * Parses some custom Document.
     *
     * @param HttpServletRequest request 
     * variables: 
     * inputNum = input number of the document
     * address = address of the recipient
     * docCap = caption of the document
     * enterIntro = first part of document (greeting)
     * enterBase = main part of document
     * enterFin = final part of document
     * Client client
     * Map<String, String> map
     * @return XWPFDocument
     */
    public WordprocessingMLPackage generateCustomDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        map.put("[input:docId]", parseCustomParams(request, "docId", ""));
        return customMappingImpl.getDocument(map);
    }


    public WordprocessingMLPackage generateDefaultContract(HttpServletRequest request, Client client, Map<String, String> map) {
        return defaultContractMappingImpl.getDocument(map, client, parseContractId(request), parseWorkerId(request), parseContext(request));
    }

    @Override
    public WordprocessingMLPackage generateShelterContract(HttpServletRequest request, Client client, Map<String, String> map) {
        return shelterContractMapping.getDocument(map, client, parseContractId(request), parseWorkerId(request), parseContext(request));
    }

    
    @Override
	public SpreadsheetMLPackage generateWorkReportDocument(HttpServletRequest request) {
    	Map<String, Date> requestParameters = new HashMap<String, Date>();
    	requestParameters.put("from", Util.parseDate(request, "from", log));
    	requestParameters.put("till", Util.parseDate(request, "till", log));
    	return resultWorkReportMappingImpl.getDocument(requestParameters); 
	}


	@Override
	public SpreadsheetMLPackage generateOutShelterDocument(HttpServletRequest request) {
    	Map<String, Date> requestParameters = new HashMap<String, Date>();
    	requestParameters.put("from", Util.parseDate(request, "from", log));
    	requestParameters.put("till", Util.parseDate(request, "till", log));
		return outOfShelterReportMappimgImpl.getDocument(requestParameters);
	}


	@Override
	public SpreadsheetMLPackage generateOneTimeServicesDocument(HttpServletRequest request) {
    	Map<String, Date> requestParameters = new HashMap<String, Date>();
    	requestParameters.put("from", Util.parseDate(request, "from", log));
    	requestParameters.put("till", Util.parseDate(request, "till", log));
		return oneTimeServicesReportMappingImpl.getDocument(requestParameters);
	}


	@Override
	public SpreadsheetMLPackage generateOverVacDocument(HttpServletRequest request) {
		Map<String, Date> requestParameters = new HashMap<String, Date>();
		return overVacReportMappingImpl.getDocument(requestParameters);
	}


	@Override
	public SpreadsheetMLPackage generateOuterDocument(HttpServletRequest request) {
		Map<String, Date> requestParameters = new HashMap<String, Date>();
		return outerReportMappingImpl.getDocument(requestParameters);
	}


	@Override
	public SpreadsheetMLPackage generateOldSchoolDocument(HttpServletRequest request) {
		Map<String, Date> requestParameters = new HashMap<String, Date>();
    	requestParameters.put("from", Util.parseDate(request, "from", log));
    	requestParameters.put("till", Util.parseDate(request, "till", log));
		return oldSchoolReportMappingImpl.getDocument(requestParameters);
	}


}
