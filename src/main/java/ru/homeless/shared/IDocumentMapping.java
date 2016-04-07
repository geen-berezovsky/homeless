package ru.homeless.shared;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import ru.homeless.configuration.Configuration;
import ru.homeless.entities.Client;

public interface IDocumentMapping {

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


    /*
    Template file: WorkResultReport.xlsx
    Document title: Стандартный отчет по результатам работы
    */
    public final int REPORT_WORK_RESULT = 200; //READY
    public final String REPORT_WORK_RESULT_TEMPLATE_PATH = Configuration.templatesDir+"/"+"WorkResultReport.xlsm";
    
    /*
    Template file: OutOfShelter.xlsx
    Document title: Стандартный отчет по выбывшим
    */
    public final int REPORT_OUT_OF_SHELTER = 202;
    public final String REPORT_OUT_OF_SHELTER_TEMPLATE_PATH = Configuration.templatesDir+"/"+"EvictedReport.xlsm";
    
    /*
    Template file: OneTimeServices.xlsx
    Document title: Стандартный отчет по разовым услугам
    */
    public final int REPORT_ONE_TIME_SERVICES = 204;
    public final String REPORT_ONE_TIME_SERVICES_PATH = Configuration.templatesDir+"/"+"OnceOnlyReport.xlsm";
    
    /*
    Template file: OverVac.xlsx
    Document title: Стандартный отчет на охрану и в поликлинику
    */
    public final int REPORT_OVERVAC = 206;
    public final String REPORT_OVERVAC_PATH = Configuration.templatesDir+"/"+"VacReport.xlsx";
    
    /*
    Template file: OuterReport.xlsx
    Document title: Стандартный отчет по клиентам на сопровождении, не проживающим в приюте
    */
    public final int REPORT_OUTER = 208;
    public final String REPORT_OUTER_PATH = Configuration.templatesDir+"/"+"OuterReport.xlsm";
    
    /*
    Template file: InnerReport.xlsx
    Document title: Стандартный отчет по клиентам на сопровождении, проживающим в приюте
    */
    public final int REPORT_INNER = 210;
    public final String REPORT_INNER_PATH = Configuration.templatesDir+"/"+"InnerReport.xlsm";
    
    /*
    Template file: CustomStatistics.xlsm (OldSchoolReport.xlsm)
    Document title: Статистика как в старой базе
    */
    public final int REPORT_CUSTOM_STATISTICS = 212;
    public final String REPORT_CUSTOM_STATISTICS_PATH = Configuration.templatesDir+"/"+"CustomStatistics.xlsm";

    /*
    Template file: ProvidedServicesByClient.xlsx
    Document title: HS-3, "Отчет << Список клиентов >>"
     */
    public final int REPORT_PROVIDED_SERVICES_BY_CLIENT = 214;
    public final String REPORT_PROVIDED_SERVICES_BY_CLIENT_PATH = Configuration.templatesDir+"/"+"ProvidedServicesByClient.xlsx";


    public WordprocessingMLPackage generateSocialHelpDocument(HttpServletRequest request, Client client, Map<String, String> map);
    public WordprocessingMLPackage generateFreeTravelDocument(HttpServletRequest request, Client client, Map<String, String> map);
    public WordprocessingMLPackage generateSanitationDocument(HttpServletRequest request, Client client, Map<String, String> map);
    public WordprocessingMLPackage generateDispensaryDocument(HttpServletRequest request, Client client, Map<String, String> map);
    public WordprocessingMLPackage generateRegistrationDocument(HttpServletRequest request, Client client, Map<String, String> map);
    public WordprocessingMLPackage generateTransitDocument(HttpServletRequest request, Client client, Map<String, String> map);
    public WordprocessingMLPackage generateZAGSQueryDocument(HttpServletRequest request, Client client, Map<String, String> map) throws IOException;
    public WordprocessingMLPackage generateCustomDocument(HttpServletRequest request, Client client, Map<String, String> map);


    public WordprocessingMLPackage generateDefaultContract(HttpServletRequest request, Client client, Map<String, String> map);
    public WordprocessingMLPackage generateShelterContract(HttpServletRequest request, Client client, Map<String, String> map);

	public XSSFWorkbook generateWorkReportDocument(HttpServletRequest request);
	public XSSFWorkbook generateOutShelterDocument(HttpServletRequest request);
	public XSSFWorkbook generateOneTimeServicesDocument(HttpServletRequest request);
	public XSSFWorkbook generateOverVacDocument(HttpServletRequest request);
	public XSSFWorkbook generateOuterDocument(HttpServletRequest request);
    public XSSFWorkbook generateInnerReport(HttpServletRequest request);
	public XSSFWorkbook generateCustomStatisticsDocument(HttpServletRequest request);
    public XSSFWorkbook generateProvidedServicesByClientDocument(HttpServletRequest request);
    
}
