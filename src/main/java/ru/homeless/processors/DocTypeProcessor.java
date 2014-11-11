package ru.homeless.processors;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class DocTypeProcessor {
	protected Map<String, String> parameters;
	protected HWPFDocument replacedDocument = null;
	protected String pathToTemplate = null;
    public static final Logger log = Logger.getLogger(DocTypeProcessor.class);
	protected BigInteger generatedNum = null;

    HWPFDocument document = null;

	private ServletContext context;

	//getting default params
	public DocTypeProcessor(Map<String, String> parameters, ServletContext context, String pathToTemplate) {
		generatedNum = generateDocumentId();
		this.pathToTemplate = pathToTemplate;
		this.parameters = parameters;
        this.context = context;
	}

    public DocTypeProcessor() {
    }

    public HWPFDocument replaceParametersInDocument() {
		InputStream resourceAsStream = context.getResourceAsStream(pathToTemplate);
		try {
            for (Map.Entry e : parameters.entrySet()) {
                log.info(e.getKey()+"="+e.getValue());
            }
			document = WordDocumentReplaceProcessor.searchInParagraphs(new HWPFDocument(resourceAsStream), parameters);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return document;
	}
	
	private BigInteger generateDocumentId() {
		return null;
	}

}
