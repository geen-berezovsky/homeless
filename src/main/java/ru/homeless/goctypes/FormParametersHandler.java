package ru.homeless.goctypes;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;

import javax.servlet.ServletContext;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;

import ru.homeless.processors.WordDocumentReplaceProcessor;

public abstract class FormParametersHandler {
	protected HashMap<String, String> parameters;
	protected XWPFDocument replacedDocument = null;
	protected String pathToTemplate = null;
	
	protected BigInteger generatedNum = null;
	
	XWPFDocument document = null;
	@Autowired
	private ServletContext context;
	
	//getting default params
	public FormParametersHandler (HashMap<String, String> defaulParameters, String pathToTemplate) {
		generatedNum = generateDocumentId();
		this.pathToTemplate = pathToTemplate;
		this.parameters = defaulParameters;
		mergeParams();
		replaceParametersInDocument();
	}
	
	public abstract HashMap<String, String> getUniqueParams();
	
	private final void mergeParams() {
		parameters.putAll(getUniqueParams());
	}
	
	private final void replaceParametersInDocument() {
		InputStream resourceAsStream = context.getResourceAsStream(pathToTemplate);
		try {
			document = WordDocumentReplaceProcessor.searchInParagraphs(new XWPFDocument(resourceAsStream), parameters);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private BigInteger generateDocumentId() {
		return null;
	}

}
