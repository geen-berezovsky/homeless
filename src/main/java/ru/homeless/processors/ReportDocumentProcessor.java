package ru.homeless.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.docx4j.wml.ObjectFactory;
import org.xlsx4j.jaxb.Context;
import org.xlsx4j.sml.CTRst;
import org.xlsx4j.sml.CTXstringWhitespace;
import org.xlsx4j.sml.Cell;
import org.xlsx4j.sml.Row;
import org.xlsx4j.sml.STCellType;
import org.xlsx4j.sml.SheetData;
import ru.homeless.report.entities.CustomStatisticsReportEntity;
import ru.homeless.report.entities.ResultWorkReportEntity;
import ru.homeless.shared.IDocumentMapping;

/*
 * This class directly inserts sheetData into template and returns complete generated report
 */
public class ReportDocumentProcessor {
    public static final Logger log = Logger.getLogger(ReportDocumentProcessor.class);
    private ObjectFactory factory;
    private XSSFWorkbook document;

    public ReportDocumentProcessor(XSSFWorkbook document) {
    	this.document = document;
    }
    
    public XSSFWorkbook createSheet(Map<Integer, List<String>> extSheetData, int type) throws Exception {
        log.info("Making the sheet");
        Sheet sheet = document.getSheetAt(0);
        XSSFFormulaEvaluator.evaluateAllFormulaCells(document);
        int row_start=1;
        int col_start=0;
        for (Entry<Integer, List<String>> e : extSheetData.entrySet()) {
            if (sheet.getRow(row_start) == null) {
                sheet.createRow(row_start);
            }

            int k = 0;
            for (String s : e.getValue()) {
                sheet.getRow(row_start).createCell(col_start + k);
                Integer value = null;
                if ((k > 1) && (type == IDocumentMapping.REPORT_WORK_RESULT)) {
                    value = Integer.parseInt(s);
                    sheet.getRow(row_start).getCell(col_start + k).setCellValue(value);
                } else {
                    sheet.getRow(row_start).getCell(col_start + k).setCellValue(s);
                }
                k++;
            }
            row_start++;

        }

        return document;
    }

    private void publishMap(Sheet sheet, CustomStatisticsReportEntity o, int row_start, int col_start) {
        for (Map.Entry<String, Integer> e : o.getValueAndQuantity().entrySet()) {
            if (sheet.getRow(row_start) == null) {
                sheet.createRow(row_start);
                sheet.getRow(row_start).createCell(col_start);
                sheet.getRow(row_start).createCell(col_start+1);
            }

            sheet.getRow(row_start).getCell(col_start).setCellValue(e.getKey());
            sheet.getRow(row_start).getCell(col_start+1).setCellValue(e.getValue());
            row_start++;
        }

    }



}
