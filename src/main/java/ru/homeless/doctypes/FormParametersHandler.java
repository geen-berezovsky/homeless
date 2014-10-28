package ru.homeless.doctypes;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Configurable;
import ru.homeless.processors.WordDocumentReplaceProcessor;

@Configurable
public class FormParametersHandler {
	protected Map<String, String> parameters;
	protected XWPFDocument replacedDocument = null;
	protected String pathToTemplate = null;
    public static final Logger log = Logger.getLogger(FormParametersHandler.class);
	protected BigInteger generatedNum = null;
	
	XWPFDocument document = null;
	@Autowired
	private ServletContext context;
	
	//getting default params
	public FormParametersHandler (Map<String, String> parameters, String pathToTemplate) {
		generatedNum = generateDocumentId();
		this.pathToTemplate = pathToTemplate;
		this.parameters = parameters;
	}
	
	public XWPFDocument replaceParametersInDocument() {
		InputStream resourceAsStream = context.getResourceAsStream(pathToTemplate);
		try {
            for (Map.Entry e : parameters.entrySet()) {
                log.info(e.getKey()+"="+e.getValue());
            }
			document = WordDocumentReplaceProcessor.searchInParagraphs(new XWPFDocument(resourceAsStream), parameters);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return document;
	}
	
	private BigInteger generateDocumentId() {
		return null;
	}

}
