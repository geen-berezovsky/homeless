package ru.homeless.processors;

import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlCursor;

public class WordDocumentReplaceProcessor {
	public static XWPFDocument searchInParagraphs(XWPFDocument document, Map<String, String> replacedMap) {
		 List<XWPFParagraph> xwpfParagraphs = document.getParagraphs();
	        for(XWPFParagraph xwpfParagraph : xwpfParagraphs) {
	            List<XWPFRun> xwpfRuns = xwpfParagraph.getRuns();
	            for(XWPFRun xwpfRun : xwpfRuns) {
	                String xwpfRunText = xwpfRun.getText(xwpfRun.getTextPosition());
	                for(Map.Entry<String, String> entry : replacedMap.entrySet()) {
	                    if (xwpfRunText != null && xwpfRunText.contains(entry.getKey())) {
	                        if (entry.getValue().contains("\n")) {
	                            String[] paragraphs = entry.getValue().split("\n");
	                            entry.setValue("");
	                            createParagraphs(document, xwpfParagraph, paragraphs);
	                        }
	                        xwpfRunText = xwpfRunText.replaceAll(entry.getKey(), entry.getValue());
	                    }
	                }
	                xwpfRun.setText(xwpfRunText, 0);
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
