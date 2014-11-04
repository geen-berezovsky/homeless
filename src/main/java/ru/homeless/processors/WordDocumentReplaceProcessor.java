package ru.homeless.processors;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;

public class WordDocumentReplaceProcessor {
    public static final Logger log = Logger.getLogger(WordDocumentReplaceProcessor.class);
	public static XWPFDocument searchInParagraphs(XWPFDocument document, Map<String, String> replacedMap) {
		 List<XWPFParagraph> xwpfParagraphs = document.getParagraphs();
	        for(XWPFParagraph xwpfParagraph : xwpfParagraphs) {
	            List<XWPFRun> xwpfRuns = xwpfParagraph.getRuns();
	            for(XWPFRun xwpfRun : xwpfRuns) {
	                String xwpfRunText = xwpfRun.getText(xwpfRun.getTextPosition());
	                for(Map.Entry<String, String> entry : replacedMap.entrySet()) {
	                    if (xwpfRunText != null && xwpfRunText.contains(entry.getKey())) {
	                        //if (entry.getValue().contains("\n")) {
                                log.info("Processing word "+xwpfRunText);
	                            String[] paragraphs = entry.getValue().split("\n");
	                            //entry.setValue("");
	                            //createParagraphs(document, xwpfParagraph, paragraphs);
	                        //}
                            log.info("Setting text "+entry.getKey() + " with "+entry.getValue());
	                        xwpfRunText = xwpfRunText.replaceAll(entry.getKey(), entry.getValue());
	                    }
	                }
                    log.info("Set "+xwpfRunText+" to the position ");
	                xwpfRun.setText(xwpfRunText, 0);
	            }
	        }
            List<XWPFTable> xwpfTables = document.getTables();
            for (XWPFTable tbl : xwpfTables) {
                for (XWPFTableRow row : tbl.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            for (XWPFRun r : p.getRuns()) {
                                String xwpfRunText = r.getText(r.getTextPosition());
                                for(Map.Entry<String, String> entry : replacedMap.entrySet()) {
                                    if (xwpfRunText != null && xwpfRunText.contains(entry.getKey())) {
                                        log.info("Processing word "+xwpfRunText);
                                        String[] paragraphs = entry.getValue().split("\n");
                                        xwpfRunText = xwpfRunText.replaceAll(entry.getKey(), entry.getValue());
                                        log.info("Setting text "+entry.getKey() + " with "+entry.getValue());
                                    }
                                }
                                log.info("Set "+xwpfRunText+" to the position ");
                                r.setText(xwpfRunText, 0);
                            }
                        }
                    }
                }
            }
	        return document;
	    }

	 private static void createParagraphs(XWPFDocument document, XWPFParagraph xwpfParagraph, String[] paragraphs) {
	        if(xwpfParagraph!=null){
	            for (int i = 0; i < paragraphs.length; i++) {
	                XmlCursor cursor = xwpfParagraph.getCTP().newCursor();
	                XWPFParagraph newParagraph = document.insertNewParagraph(cursor);
	                newParagraph.setAlignment(xwpfParagraph.getAlignment());
	                newParagraph.getCTP().insertNewR(0).insertNewT(0).setStringValue(paragraphs[i]);
	                newParagraph.setNumID(xwpfParagraph.getNumID());
	            }
	            document.removeBodyElement(document.getPosOfParagraph(xwpfParagraph));
	        }
	    }
}
