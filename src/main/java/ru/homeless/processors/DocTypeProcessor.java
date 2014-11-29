package ru.homeless.processors;

import java.io.*;
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

    XWPFDocument document = null;

	private ServletContext context;

	public DocTypeProcessor(Map<String, String> parameters, String pathToTemplate) {
		this.pathToTemplate = pathToTemplate;
		this.parameters = parameters;
	}

    public XWPFDocument replaceParametersInDocument() {
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = new FileInputStream(new File(pathToTemplate));
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(),e);
        }
        try {
            for (Map.Entry e : parameters.entrySet()) {
                log.info(e.getKey()+"="+e.getValue());
            }
            if (new File(pathToTemplate).exists()) {
                document = WordDocumentReplaceProcessor.searchInParagraphs(new XWPFDocument(resourceAsStream), parameters);
            } else {
                log.error("Document template "+pathToTemplate+" does not exist or not accessible");
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
        return document;
	}
	

}
