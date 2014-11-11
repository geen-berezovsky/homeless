package ru.homeless.shared;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.servlet.ServletContext;

public interface IDocumentMapping {

    public static final String ORGANIZATION_INFO = "СПбБОО «Ночлежка»" + "192007, СПб, ул. Боровая, д. 112, Б" + "ИНН 7805104809 КПП 780501001";

    /*
    Template file: DocumentSocialHelp.docx
    Document title: Справка о социальной помощи
     */
	public final int DOCUMENT_SOCIAL_HELP = 2;
    public final String DOCUMENT_SOCIAL_HELP_TEMPLATE_PATH = "WEB-INF/templates/documents/help.doc";

    /*
    Template file: DocumentFreeTravel.docx
    Document title: Справка о получении социальной помощи (не препятствовать проезду)
     */
	public final int DOCUMENT_FREE_TRAVEL = 4;
    public final String DOCUMENT_FREE_TRAVEL_TEMPLATE_PATH = "WEB-INF/templates/documents/travel.doc";
	
    /*
    Template file: DocumentSanitation.docx
    Document title: Направление на санитарную обработку
     */
	public final int DOCUMENT_SANITATION = 6;
    public final String DOCUMENT_SANITATION_TEMPLATE_PATH = "WEB-INF/templates/documents/sanitation.doc";

    /*
    Template file: DocumentDefaultContract.docx
    Document title: Стандартный контракт
    */
    public final int DOCUMENT_DEFAULT_CONTRACT = 100;
    public final String DOCUMENT_DEFAULT_CONTRACT_TEMPLATE_PATH = "WEB-INF/templates/documents/contract_template.doc";


    public HWPFDocument documentSocialHelpImpl(int requestType, int clientId, Date issueDate, ServletContext context);
	public HWPFDocument documentFreeTravelImpl(int requestType, int clientId, String travelCity, Date issueDate, ServletContext context);
    public HWPFDocument documentSanitationImpl(int requestType, int clientId, Date issueDate, ServletContext context);

    public HWPFDocument documentDefaultContractImpl(int requestType, int clientId, Date issueDate, int contractId, int workerId, ServletContext context) throws UnsupportedEncodingException;
	
}
