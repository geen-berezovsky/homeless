package ru.homeless.parsers;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import ru.homeless.shared.IDocumentMapping;

public class HttpRequestParser implements IDocumentMapping {
	
	public static final Logger log = Logger.getLogger(HttpRequestParser.class);

	XWPFDocument replaced_document = null;
	
	@Override
	public XWPFDocument documentType1(int requestType, int cid, String city, Date date1) {

		//full document processing implmentation
		
		return null;
	}

	@Override
	public XWPFDocument documentType2(int requestType, int cid, int wid, Date date1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public XWPFDocument generateSpravka1(HttpServletRequest request) {
		//here we parsing request and running method documentType1 with parsed parameters

//		def + uniq (from db)
		
		replaced_document = documentType1(IDocumentMapping.DOCUMENT_SOCIAL_HELP, 0, "", null);
		return replaced_document;
	}

	public XWPFDocument generateSpravka2(HttpServletRequest request) {
		//here we parsing request and running method documentType2 with parsed parameters
		replaced_document = documentType2(IDocumentMapping.DOCUMENT_FREE_TRAVEL, 0, 0, null);
		return replaced_document;
	}

	
}
