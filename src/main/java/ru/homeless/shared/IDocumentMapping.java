package ru.homeless.shared;

import java.util.Date;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public interface IDocumentMapping {
	
	public final int DOCUMENT_SOCIAL_HELP = 2;
	
	public final int DOCUMENT_FREE_TRAVEL = 4;
	
	public final int DOCUMENT_SANITATION = 8;
	
	public XWPFDocument documentType1(int requestType, int cid, String city, Date date1);
	public XWPFDocument documentType2(int requestType, int cid, int wid, Date date1);
	
}
