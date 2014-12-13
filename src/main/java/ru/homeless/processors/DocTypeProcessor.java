package ru.homeless.processors;

import java.io.File;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.docx4j.jaxb.XPathBinderAssociationIsPartialException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

public class DocTypeProcessor {
	protected Map<String, String> parameters;
	protected String pathToTemplate = null;
    public static final Logger log = Logger.getLogger(DocTypeProcessor.class);

    WordprocessingMLPackage document = null;

	private ServletContext context;

	public DocTypeProcessor(Map<String, String> parameters, String pathToTemplate) {
		this.pathToTemplate = pathToTemplate;
		this.parameters = parameters;
	}

    public WordprocessingMLPackage replaceParametersInDocument(byte[] photo, int avatarLocation) {
    	
    	try {
			document = WordprocessingMLPackage.load(new File(pathToTemplate));
		} catch (Docx4JException e1) {
			log.error(e1.getMessage(),e1);
		}
    	
        for (Map.Entry e : parameters.entrySet()) {
		    log.info(e.getKey()+"="+e.getValue());
		}
		if (new File(pathToTemplate).exists()) {
		    try {
				document = new WordDocumentReplaceProcessor(document).searchInParagraphs(parameters, photo, avatarLocation);
			} catch (Exception e1) {
				log.error(e1.getMessage(),e1);
			}
		} else {
		    log.error("Document template "+pathToTemplate+" does not exist or not accessible");
		}
        return document;
	}
	

}
