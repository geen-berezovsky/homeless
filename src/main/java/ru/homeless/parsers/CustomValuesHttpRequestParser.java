/*
 * 
 */
package ru.homeless.parsers;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.homeless.entities.Client;
import ru.homeless.entities.Worker;
import ru.homeless.mappings.*;
import ru.homeless.services.IContractService;
import ru.homeless.shared.IDocumentMapping;
import ru.homeless.util.Util;

@Component
public class CustomValuesHttpRequestParser implements IDocumentMapping {
	
	public static final Logger log = Logger.getLogger(CustomValuesHttpRequestParser.class);

    //Class should be annotated as @Component and child dependencies should be injected with @Autowires annotation
    //If you need use Services for woriking with database in child classes (re-inject service)

    @Autowired
    private DefaultContractMappingImpl defaultContractMappingImpl;

    @Autowired
    private ShelterContractMappingImpl shelterContractMapping;

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
        return new RegistrationMappingImpl().getDocument(map);
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
    	map.put("[m]", parseCustomParams(request, "m", "ИМЯ МАТЕРИ!!!"));
    	map.put("[f]", parseCustomParams(request, "f", "ИМЯ ОТЦА!!!"));
    	map.put("[to:send]", parseCustomParams(request, "toSend", "АДРЕС!!!"));
    	map.put("[address]", parseCustomParams(request, "address", "АДРЕС!!!"));
        return new ZagsQueryMappingImpl().getDocument(map);
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
    	map.put("[input:num]", parseCustomParams(request, "inputNum", "Входящий номер!!!"));
    	map.put("[enter:address]", parseCustomParams(request, "address", "Адрес!!!"));
    	map.put("[doc:cap]", parseCustomParams(request, "docCap", "Название!!!"));
    	map.put("[enter:intro]", parseCustomParams(request, "enterIntro", "Вводная часть!!!"));
    	map.put("[enter:base]", parseCustomParams(request, "enterBase", "Основная часть!!!"));
    	map.put("[enter:fin]", parseCustomParams(request, "enterFin", "Заключительная часть!!!"));
    	map.put("[director:signature]", IDocumentMapping.SIGN_PART_1 + " " + IDocumentMapping.SIGN_PART_2);
    	map.put("[worker:info]", Util.getWorkersBio(null)); //WORKER ID OR OBJECT!!!
        return new CustomMappingImpl().getDocument(map);
    }


    public WordprocessingMLPackage generateDefaultContract(HttpServletRequest request, Client client, Map<String, String> map) {
        return defaultContractMappingImpl.getDocument(map, client, parseContractId(request), parseWorkerId(request), parseContext(request));
    }

    @Override
    public WordprocessingMLPackage generateShelterContract(HttpServletRequest request, Client client, Map<String, String> map) {
        return shelterContractMapping.getDocument(map, client, parseContractId(request), parseWorkerId(request), parseContext(request));
    }


}
