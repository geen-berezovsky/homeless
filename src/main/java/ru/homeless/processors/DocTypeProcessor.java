package ru.homeless.processors;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.docx4j.jaxb.XPathBinderAssociationIsPartialException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

/*
 * This class load template from disk, insert/replace necessary data and return the complete generated document 
 */
public class DocTypeProcessor {
	protected String pathToTemplate = null;
    public static final Logger log = Logger.getLogger(DocTypeProcessor.class);

    WordprocessingMLPackage wordDocument = null;
    SpreadsheetMLPackage excelDocument = null;

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
		    log.info(e.getKey()+"="+e.getValue());
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
    
    public SpreadsheetMLPackage generateReport(Map<Integer, List<String>> sheetData) {
    	
    	try {
			excelDocument = SpreadsheetMLPackage.load(new File(pathToTemplate)); 
		} catch (Docx4JException e1) {
			log.error(e1.getMessage(),e1);
		}
    	/*
        for (Map.Entry<Integer, List<String>> e : sheetData.entrySet()) {
        	String res = "";
        	for (String s : e.getValue()) {
        		res += s+", ";
        	}
		    log.info(e.getKey()+"="+res);
		}
        */
		if (new File(pathToTemplate).exists()) {
		    try {
		    	//ADD PREPARATED SHEET DATA INTO TEMPLATE
		    	excelDocument = new ReportDocumentProcessor(excelDocument).createSheet(sheetData);
			} catch (Exception e1) {
				log.error(e1.getMessage(),e1);
			}
		} else {
		    log.error("Document template "+pathToTemplate+" does not exist or not accessible");
		}
        return excelDocument;
    	
    }
	

}
