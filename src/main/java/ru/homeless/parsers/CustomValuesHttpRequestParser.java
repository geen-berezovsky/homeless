package ru.homeless.parsers;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.homeless.entities.Client;
import ru.homeless.mappings.*;
import ru.homeless.services.IContractService;
import ru.homeless.shared.IDocumentMapping;

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
    private String parseTravelCity(HttpServletRequest request) {
        String travelCity = request.getParameter("travelCity");
        if (travelCity == null || travelCity.equals("")) {
            return "ГОРОД НЕ УКАЗАН!";
        } else {
            return travelCity;
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

	public XWPFDocument generateSocialHelpDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        return new SocialHelpMappingImpl().getDocument(map);
	}

	public XWPFDocument generateFreeTravelDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        //Adding custom values
        map.put("[t:city]", parseTravelCity(request));
        return new FreeTravelMappingImpl().getDocument(map);
	}

    public XWPFDocument generateSanitationDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        return new SanitationMappingImpl().getDocument(map);
    }

    @Override
    public XWPFDocument generateDispensaryDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        return new DispensaryMappingImpl().getDocument(map);
    }

    @Override
    public XWPFDocument generateRegistrationDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        return new RegistrationMappingImpl().getDocument(map);
    }

    @Override
    public XWPFDocument generateTransitDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        return new TransitMappingImpl().getDocument(map);
    }

    @Override
    public XWPFDocument generateZAGSQueryDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        return new ZagsQueryMappingImpl().getDocument(map);
    }

    @Override
    public XWPFDocument generateCustomDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        return new CustomMappingImpl().getDocument(map);
    }


    public XWPFDocument generateDefaultContract(HttpServletRequest request, Client client, Map<String, String> map) {
        return defaultContractMappingImpl.getDocument(map, client, parseContractId(request), parseWorkerId(request), parseContext(request));
    }

    @Override
    public XWPFDocument generateShelterContract(HttpServletRequest request, Client client, Map<String, String> map) {
        return shelterContractMapping.getDocument(map, client, parseContractId(request), parseWorkerId(request), parseContext(request));
    }


}
