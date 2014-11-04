package ru.homeless.shared;

import java.text.ParseException;
import java.util.Date;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.servlet.ServletContext;

public interface IDocumentMapping {

    /*
    Template file: DocumentSocialHelp.docx
    Document title: Справка о социальной помощи
     */
	public final int DOCUMENT_SOCIAL_HELP = 2;
    public final String DOCUMENT_SOCIAL_HELP_TEMPLATE_PATH = "WEB-INF/templates/DocumentSocialHelp.docx";

    /*
    Template file: DocumentFreeTravel.docx
    Document title: Справка о получении социальной помощи (не препятствовать проезду)
     */
	public final int DOCUMENT_FREE_TRAVEL = 4;
    public final String DOCUMENT_FREE_TRAVEL_TEMPLATE_PATH = "WEB-INF/templates/DocumentFreeTravel.docx";
	
    /*
    Template file: DocumentSanitation.docx
    Document title: Направление на санитарную обработку
     */
	public final int DOCUMENT_SANITATION = 8;
    public final String DOCUMENT_SANITATION_TEMPLATE_PATH = "WEB-INF/templates/DocumentSanitation.docx";


	public XWPFDocument documentSocialHelpImpl(int requestType, int clientId, Date issueDate, ServletContext context);
	public XWPFDocument documentFreeTravelImpl(int requestType, int clientId, String travelCity, Date issueDate, ServletContext context);
    public XWPFDocument documentSanitationImpl(int requestType, int clientId, Date issueDate, ServletContext context);
	
}
