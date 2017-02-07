/*
 * 
 */
package ru.homeless.parsers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.homeless.entities.Client;
import ru.homeless.mappings.*;
import ru.homeless.shared.IDocumentMapping;
import ru.homeless.util.Util;

@Component
public class CustomValuesHttpRequestParser implements IDocumentMapping {

    private static final Logger LOG = Logger.getLogger(CustomValuesHttpRequestParser.class);

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
    private InnerReportMappingImpl innerReportMappingImpl;

    @Autowired
    private CustomStatisticsReportMappingImpl customStatisticsReportMappingImpl;

    @Autowired
    private ZagsQueryMappingImpl zagsQueryMappingImpl;

    @Autowired
    private RegistrationMappingImpl registrationMappingImpl;

    @Autowired
    private ConsAboutWorkMappingImpl consAboutWorkMappingImpl;

    @Autowired
    private ProvidedServicesByClientReportMappingImpl providedServicesByClientReportMapping;

    //HERE WE PARSE CUSTOM VALUES LIKE travelCity
    /**
     * Parses the travel city.
     *
     * @param request the request BECAUSE OF ISSUES WITH GETTING RUSSIAN TEXT FROM REQUEST, WE ARE
     * DOING SOME HACK
     * @return the string
     */
    private String parseTravelCity(HttpServletRequest request) {
        String travelCity = null;
        try {
            travelCity = URLDecoder.decode(request.getQueryString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.info(e);
            e.printStackTrace();
        }

        if (travelCity == null || travelCity.equals("")) {
            return "ГОРОД НЕ УКАЗАН!";
        } else {
            travelCity = travelCity.replaceAll("^.*travelCity=", "");
            travelCity = travelCity.replaceAll("&.*$", "");
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

    @Override
    public WordprocessingMLPackage generateSocialHelpDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        return new SocialHelpMappingImpl().getDocument(map, client);
    }

    @Override
    public WordprocessingMLPackage generateFreeTravelDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        //Adding custom values
        map.put("[t:city]", parseTravelCity(request));
        return new FreeTravelMappingImpl().getDocument(map, client);
    }

    @Override
    public WordprocessingMLPackage generateSanitationDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        return new SanitationMappingImpl().getDocument(map, client);
    }

    @Override
    public WordprocessingMLPackage generateDispensaryDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        return new DispensaryMappingImpl().getDocument(map, client);
    }

    @Override
    public WordprocessingMLPackage generateRegistrationDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        map.put("[input:docId]", parseCustomParams(request, "docId", ""));
        return registrationMappingImpl.getDocument(map, client);
    }

    @Override
    public WordprocessingMLPackage generateTransitDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        //Adding custom values
        map.put("[t:city]", parseTravelCity(request));
        return new TransitMappingImpl().getDocument(map, client);
    }

    @Override
    public WordprocessingMLPackage generateConsAboutWorkDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        map.put("[input:docId]", parseCustomParams(request, "docId", ""));
        return consAboutWorkMappingImpl.getDocument(map, client);
        //return new ConsAboutWorkMappingImpl().getDocument(map, client);
    }

    @Override
    /**
     * Parses the ZAGS Document.
     *
     * @param HttpServletRequest request variables: m = mother's name f = father's name toSend =
     * address of the recipient address = address of the sender Client client Map<String, String>
     * map
     * @return XWPFDocument
     */
    public WordprocessingMLPackage generateZAGSQueryDocument(HttpServletRequest request, Client client, Map<String, String> map) throws IOException {
        map.put("[input:docId]", parseCustomParams(request, "docId", ""));
        return zagsQueryMappingImpl.getDocument(map, client);
    }

    @Override
    /**
     * Parses some custom Document.
     *
     * @param HttpServletRequest request variables: inputNum = input number of the document address
     * = address of the recipient docCap = caption of the document enterIntro = first part of
     * document (greeting) enterBase = main part of document enterFin = final part of document
     * Client client Map<String, String> map
     * @return XWPFDocument
     */
    public WordprocessingMLPackage generateCustomDocument(HttpServletRequest request, Client client, Map<String, String> map) {
        map.put("[input:docId]", parseCustomParams(request, "docId", ""));
        return customMappingImpl.getDocument(map, client);
    }

    @Override
    public WordprocessingMLPackage generateDefaultContract(HttpServletRequest request, Client client, Map<String, String> map) {
        return defaultContractMappingImpl.getDocument(map, client, parseContractId(request), parseWorkerId(request), parseContext(request));
    }

    @Override
    public WordprocessingMLPackage generateShelterContract(HttpServletRequest request, Client client, Map<String, String> map) {
        return shelterContractMapping.getDocument(map, client, parseContractId(request), parseWorkerId(request), parseContext(request));
    }

    @Override
    public XSSFWorkbook generateWorkReportDocument(HttpServletRequest request) {
        Map<String, Date> requestParameters = new HashMap<>();
        requestParameters.put("from", Util.parseDate(request, "from", LOG));
        requestParameters.put("till", Util.parseDate(request, "till", LOG));
        return resultWorkReportMappingImpl.getDocument(requestParameters);
    }

    @Override
    public XSSFWorkbook generateOutShelterDocument(HttpServletRequest request) {
        Map<String, Date> requestParameters = new HashMap<>();
        requestParameters.put("from", Util.parseDate(request, "from", LOG));
        requestParameters.put("till", Util.parseDate(request, "till", LOG));
        return outOfShelterReportMappimgImpl.getDocument(requestParameters);
    }

    @Override
    public XSSFWorkbook generateOneTimeServicesDocument(HttpServletRequest request) {
        Map<String, Date> requestParameters = new HashMap<>();
        requestParameters.put("from", Util.parseDate(request, "from", LOG));
        requestParameters.put("till", Util.parseDate(request, "till", LOG));
        return oneTimeServicesReportMappingImpl.getDocument(requestParameters);
    }

    @Override
    public XSSFWorkbook generateOverVacDocument(HttpServletRequest request) {
        Map<String, Date> requestParameters = new HashMap<>();
        return overVacReportMappingImpl.getDocument(requestParameters);
    }

    @Override
    public XSSFWorkbook generateOuterDocument(HttpServletRequest request) {
        Map<String, Date> requestParameters = new HashMap<>();
        return outerReportMappingImpl.getDocument(requestParameters);
    }

    @Override
    public XSSFWorkbook generateInnerReport(HttpServletRequest request) {
        LOG.info("Preparing document");
        Map<String, Date> requestParameters = new HashMap<>();
        return innerReportMappingImpl.getDocument(requestParameters);
    }

    @Override
    public XSSFWorkbook generateCustomStatisticsDocument(HttpServletRequest request) {
        Map<String, Date> requestParameters = new HashMap<>();
        requestParameters.put("from", Util.parseDate(request, "from", LOG));
        requestParameters.put("till", Util.parseDate(request, "till", LOG));
        return customStatisticsReportMappingImpl.getDocument(requestParameters);
    }

    @Override
    public XSSFWorkbook generateProvidedServicesByClientDocument(HttpServletRequest request) {
        Map<String, Date> requestParameters = new HashMap<String, Date>();
        requestParameters.put("from", Util.parseDate(request, "from", LOG));
        requestParameters.put("till", Util.parseDate(request, "till", LOG));
        return providedServicesByClientReportMapping.getDocument(requestParameters);
    }

}
