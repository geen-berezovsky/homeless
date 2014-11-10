package ru.homeless.parsers;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.homeless.mappings.DefaultDocumentMapping;
import ru.homeless.shared.IDocumentMapping;

@Component
public class HttpRequestParser {
	
	public static final Logger log = Logger.getLogger(HttpRequestParser.class);

    HWPFDocument replaced_document = null;

    @Autowired
    IDocumentMapping documentMapping;

    private int parseClientId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("clientId"));
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
	
	public HWPFDocument generateSocialHelpDocument(HttpServletRequest request) {
        if (documentMapping == null) {
            documentMapping = new DefaultDocumentMapping();
        }
        replaced_document = documentMapping.documentSocialHelpImpl(IDocumentMapping.DOCUMENT_SOCIAL_HELP,parseClientId(request), parseIssueDate(request), parseContext(request));
		return replaced_document;
	}

	public HWPFDocument generateFreeTravelDocument(HttpServletRequest request) {
        if (documentMapping == null) {
            documentMapping = new DefaultDocumentMapping();
        }
        replaced_document = documentMapping.documentFreeTravelImpl(IDocumentMapping.DOCUMENT_FREE_TRAVEL, parseClientId(request),parseTravelCity(request),parseIssueDate(request), parseContext(request));
		return replaced_document;
	}

    public HWPFDocument generateSanitationDocument(HttpServletRequest request) {
        if (documentMapping == null) {
            documentMapping = new DefaultDocumentMapping();
        }
        replaced_document = documentMapping.documentSanitationImpl(IDocumentMapping.DOCUMENT_SANITATION, parseClientId(request), parseIssueDate(request), parseContext(request));
        return replaced_document;
    }

    public HWPFDocument generateDefaultContract(HttpServletRequest request) throws UnsupportedEncodingException {
        if (documentMapping == null) {
            documentMapping = new DefaultDocumentMapping();
        }
        replaced_document = documentMapping.documentDefaultContractImpl(IDocumentMapping.DOCUMENT_DEFAULT_CONTRACT, parseClientId(request), parseIssueDate(request), parseContractId(request), parseWorkerId(request), parseContext(request));
        return replaced_document;
    }



}
