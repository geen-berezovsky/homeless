package ru.homeless.processors;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;

public class WordDocumentReplaceProcessor {
    public static final Logger log = Logger.getLogger(WordDocumentReplaceProcessor.class);
	public static XWPFDocument searchInParagraphs(XWPFDocument document, Map<String, String> replacedMap) {
        List<XWPFParagraph> xwpfParagraphs = document.getParagraphs();
        for (XWPFParagraph xwpfParagraph : xwpfParagraphs) {
            List<XWPFRun> xwpfRuns = xwpfParagraph.getRuns();
            for (XWPFRun xwpfRun : xwpfRuns) {
                String xwpfRunText = xwpfRun.getText(0);
                String xwpfRunTextFiltered = "";
                log.info("String = "+xwpfRunText);
                for (Map.Entry<String, String> entry : replacedMap.entrySet()) {
                    if (xwpfRunText != null) {
                        if (entry.getKey() != null && entry.getValue() != null && xwpfRunText.contains(entry.getKey())) {
                            log.info("Replacing \""+xwpfRunText+"\""+" with entry.key="+entry.getKey()+" and value "+entry.getValue());
                            xwpfRunText = xwpfRunText.replace(entry.getKey(), entry.getValue());
                            log.info("Set " + xwpfRunTextFiltered);
                        }
                    }
                }
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
                            for (Map.Entry<String, String> entry : replacedMap.entrySet()) {
                                if (xwpfRunText != null && xwpfRunText.contains(entry.getKey())) {
                                    log.info("Processing word " + xwpfRunText);
                                    xwpfRunText = xwpfRunText.replace(entry.getKey(), entry.getValue());
                                    log.info("Setting text " + entry.getKey() + " with " + entry.getValue());
                                }
                            }
                            log.info("Set " + xwpfRunText + " to the position ");
                            r.setText(xwpfRunText, 0);
                        }
                    }
                }
            }
        }

        return document;
    }

}
