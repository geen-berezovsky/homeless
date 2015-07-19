package ru.homeless.processors;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import ru.homeless.report.entities.CustomStatisticsReportEntity;

/*
 * This class load template from disk, insert/replace necessary data and return the complete generated document 
 */
public class DocTypeProcessor {
	protected String pathToTemplate = null;
    public static final Logger log = Logger.getLogger(DocTypeProcessor.class);

    private WordprocessingMLPackage wordDocument = null;
    private XSSFWorkbook excelDocument = null;

	private ServletContext context;

	public DocTypeProcessor(String pathToTemplate) {
		this.pathToTemplate = pathToTemplate;
	}

    public WordprocessingMLPackage replaceParametersInDocument(Map<String, String> parameters, byte[] photo, int avatarLocation) {
    	
    	try {
			wordDocument = WordprocessingMLPackage.load(new File(pathToTemplate));
		} catch (Docx4JException e1) {
			log.error(e1.getMessage(),e1);
		}
    	
        for (Map.Entry<String, String> e : parameters.entrySet()) {
            if (e != null) {
                log.info(e.getKey() + "=" + e.getValue());
            }
		}
		if (new File(pathToTemplate).exists()) {
		    try {
				wordDocument = new WordDocumentReplaceProcessor(wordDocument).searchInParagraphs(parameters, photo, avatarLocation);
			} catch (Exception e1) {
				log.error(e1.getMessage(),e1);
			}
		} else {
		    log.error("Document template "+pathToTemplate+" does not exist or not accessible");
		}
        return wordDocument;
	}
    
    public XSSFWorkbook generateReport(Map<Integer, List<String>> sheetData, int type) {
        if (new File(pathToTemplate).exists()) {
            log.info("Using template " + pathToTemplate);
            try {
                excelDocument = new XSSFWorkbook(OPCPackage.open(pathToTemplate));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidFormatException e) {
                e.printStackTrace();
            }
            for (Map.Entry<Integer, List<String>> e : sheetData.entrySet()) {
                String res = "";
                if (e != null) {
                    for (String s : e.getValue()) {
                        res += s + ", ";
                    }
                }
                log.info(e.getKey() + "=" + res);
            }
            try {
                //INSERT NEW DATA TO THE CREATED COPY OF ORIGINAL TEMPLATE
                excelDocument = new ReportDocumentProcessor(excelDocument).createSheet(sheetData, type);
            } catch (Exception e1) {
                log.error(e1.getMessage(), e1);
            }
        } else {
            log.error("Document template " + pathToTemplate + " does not exist or not accessible");
        }
        return excelDocument;
    }
	

    public XSSFWorkbook generateReport(List<CustomStatisticsReportEntity> sheetData) {
        if (new File(pathToTemplate).exists()) {
            log.info("Using template " + pathToTemplate);
            try {
                excelDocument = new XSSFWorkbook(OPCPackage.open(pathToTemplate));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidFormatException e) {
                e.printStackTrace();
            }
            for (int i=0; i<sheetData.size(); i++) {
                for (Map.Entry<String, Integer> e : sheetData.get(i).getValueAndQuantity().entrySet()) {
                    if (e!=null) {
                        log.info(e.getKey() + "=" + e.getValue());
                    }
                }
            }
            try {
                //INSERT NEW DATA TO THE CREATED COPY OF ORIGINAL TEMPLATE
                excelDocument = new CustomReportDocumentProcessor(excelDocument).createSheet(sheetData);
            } catch (Exception e1) {
                log.error(e1.getMessage(), e1);
            }
        } else {
            log.error("Document template " + pathToTemplate + " does not exist or not accessible");
        }
        return excelDocument;
    }

    
}
