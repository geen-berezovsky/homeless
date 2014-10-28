package ru.homeless.parsers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import ru.homeless.mappings.DefaultDocumentMapping;
import ru.homeless.shared.IDocumentMapping;

public class HttpRequestParser {
	
	public static final Logger log = Logger.getLogger(HttpRequestParser.class);

	XWPFDocument replaced_document = null;
    IDocumentMapping documentMapping = new DefaultDocumentMapping();

    private int parseClientId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("clientId"));
    }
    private Date parseIssueDate(HttpServletRequest request) {
        Date issueDate = null;
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        String sDate = request.getParameter("issueDate");
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
        if (travelCity.equals("")) {
            return "ГОРОД НЕ УКАЗАН!";
        } else {
            return travelCity;
        }
    }
	
	public XWPFDocument generateSocialHelpDocument(HttpServletRequest request) {
        int clientId=parseClientId(request);
        Date issueDate = parseIssueDate(request);
        replaced_document = documentMapping.documentSocialHelpImpl(IDocumentMapping.DOCUMENT_SOCIAL_HELP,clientId, issueDate);
		return replaced_document;
	}

	public XWPFDocument generateFreeTravelDocument(HttpServletRequest request) {
        int clientId=parseClientId(request);
        Date issueDate = parseIssueDate(request);
        String travelCity= parseTravelCity(request);
        replaced_document = documentMapping.documentFreeTravelImpl(IDocumentMapping.DOCUMENT_FREE_TRAVEL, clientId,travelCity,issueDate);
		return replaced_document;
	}

    public XWPFDocument generateSanitationDocument(HttpServletRequest request) {
        int clientId=parseClientId(request);
        Date issueDate = parseIssueDate(request);
        replaced_document = documentMapping.documentSanitationImpl(IDocumentMapping.DOCUMENT_SANITATION, clientId, issueDate);
        return replaced_document;
    }

}
