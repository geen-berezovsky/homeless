package ru.homeless.shared;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import ru.homeless.configuration.Configuration;
import ru.homeless.entities.Client;
import ru.homeless.mappings.FreeTravelMappingImpl;
import ru.homeless.mappings.SanitationMappingImpl;
import ru.homeless.mappings.SocialHelpMappingImpl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public interface IDocumentMapping {

    public static final String ORGANIZATION_INFO = "СПбБОО «Ночлежка»" + "192007, СПб, ул. Боровая, д. 112, Б" + "ИНН 7805104809 КПП 780501001";
    public static final String SIGN_PART_1 = "Директор";
    public static final String SIGN_PART_2 = "Свердлин Г.";

    /*
    Template file: DocumentSocialHelp.docx
    Document title: Справка о социальной помощи
     */
	public final int DOCUMENT_SOCIAL_HELP = 2;
    public final String DOCUMENT_SOCIAL_HELP_TEMPLATE_PATH = Configuration.templatesDir+"/"+"DocumentSocialHelp.docx";

    /*
    Template file: DocumentTravel.docx
    Document title: Справка о получении социальной помощи (не препятствовать проезду)
     */
	public final int DOCUMENT_FREE_TRAVEL = 4;
    public final String DOCUMENT_FREE_TRAVEL_TEMPLATE_PATH = Configuration.templatesDir+"/"+"DocumentTravel.docx";
	
    /*
    Template file: DocumentSanitation.docx
    Document title: Направление на санитарную обработку
     */
	public final int DOCUMENT_SANITATION = 6;
    public final String DOCUMENT_SANITATION_TEMPLATE_PATH = Configuration.templatesDir+"/"+"DocumentSanitation.docx";

    /*
    Template file: DocumentDispensary.docx
    Document title: Направление на диспансеризацию
    */
    public final int DOCUMENT_DISPENSARY = 8;
    public final String DOCUMENT_DISPENSARY_TEMPLATE_PATH = Configuration.templatesDir+"/"+"DocumentDispensary.docx";

    /*
    Template file: DocumentRegistration.docx
    Document title: Справка о регистрации
    */
    public final int DOCUMENT_REGISTRATION = 10;
    public final String DOCUMENT_REGISTRATION_TEMPLATE_PATH = Configuration.templatesDir+"/"+"DocumentRegistration.docx";

    /*
    Template file: DocumentTransit.docx
    Document title: Справка по "Транзиту"
    */
    public final int DOCUMENT_TRANSIT = 12;
    public final String DOCUMENT_TRANSIT_TEMPLATE_PATH = Configuration.templatesDir+"/"+"DocumentTransit.docx";

    /*
    Template file: DocumentZAGSQuery.docx
    Document title: Запрос в отдел ЗАГС
    */
    public final int DOCUMENT_ZAGS_QUERY = 14;
    public final String DOCUMENT_ZAGS_QUERY_TEMPLATE_PATH = Configuration.templatesDir+"/"+"DocumentZAGSQuery.docx";

    /*
    Template file: DocumentCustom.docx
    Document title: Произвольная справка
     */
    public final int DOCUMENT_CUSTOM = 16;
    public final String DOCUMENT_CUSTOM_TEMPLATE_PATH = Configuration.templatesDir+"/"+"DocumentCustom.docx";



    /*
    Template file: ContractDefault.docx
    Document title: Стандартный контракт
    */
    public final int DOCUMENT_DEFAULT_CONTRACT = 100;
    public final String DOCUMENT_DEFAULT_CONTRACT_TEMPLATE_PATH = Configuration.templatesDir+"/"+"ContractDefault.docx";

    /*
    Template file: ContractShelter.docx
    Document title: Контракт с проживающим
    */
    public final int DOCUMENT_SHELTER_CONTRACT = 102;
    public final String DOCUMENT_SHELTER_CONTRACT_TEMPLATE_PATH = Configuration.templatesDir+"/"+"ContractShelter.docx";



    public WordprocessingMLPackage generateSocialHelpDocument(HttpServletRequest request, Client client, Map<String, String> map);
    public WordprocessingMLPackage generateFreeTravelDocument(HttpServletRequest request, Client client, Map<String, String> map);
    public WordprocessingMLPackage generateSanitationDocument(HttpServletRequest request, Client client, Map<String, String> map);
    public WordprocessingMLPackage generateDispensaryDocument(HttpServletRequest request, Client client, Map<String, String> map);
    public WordprocessingMLPackage generateRegistrationDocument(HttpServletRequest request, Client client, Map<String, String> map);
    public WordprocessingMLPackage generateTransitDocument(HttpServletRequest request, Client client, Map<String, String> map);
    public WordprocessingMLPackage generateZAGSQueryDocument(HttpServletRequest request, Client client, Map<String, String> map);
    public WordprocessingMLPackage generateCustomDocument(HttpServletRequest request, Client client, Map<String, String> map);


    public WordprocessingMLPackage generateDefaultContract(HttpServletRequest request, Client client, Map<String, String> map);
    public WordprocessingMLPackage generateShelterContract(HttpServletRequest request, Client client, Map<String, String> map);

}
