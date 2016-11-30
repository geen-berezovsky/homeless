package ru.homeless.processors;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.*;
import ru.homeless.report.entities.CustomStatisticsReportEntity;
import ru.homeless.shared.IDocumentMapping;

/*
 * This class directly inserts sheetData into template and returns complete generated report
 */
public class ReportDocumentProcessor {

    private static final Logger LOG = Logger.getLogger(ReportDocumentProcessor.class);
    private final XSSFWorkbook document;

    public ReportDocumentProcessor(XSSFWorkbook document) {
        this.document = document;
    }

    public XSSFWorkbook createSheet(Map<Integer, List<String>> extSheetData, int type) throws Exception {
        LOG.info("Making the sheet");
        Sheet sheet = document.getSheetAt(0);
        XSSFFormulaEvaluator.evaluateAllFormulaCells(document);
        int row_start = 1;
        int col_start = 0;
        for (Entry<Integer, List<String>> e : extSheetData.entrySet()) {
            if (sheet.getRow(row_start) == null) {
                sheet.createRow(row_start);
            }
            int k = 0;
            for (String s : e.getValue()) {
                sheet.getRow(row_start).createCell(col_start + k);
                if ((k > 2) && (type == IDocumentMapping.REPORT_WORK_RESULT)) {
                    sheet.getRow(row_start).getCell(col_start + k).setCellValue(Integer.parseInt(s));
                } else {
                    sheet.getRow(row_start).getCell(col_start + k).setCellValue(s);
                    /*
                        CUSTOMIZATIONS FOR DIFFERENT REPORTS (COLORS, STYLES FOR EXACT CELLS)
                        //REFACTOR IF NECESSARY
                     */

                    switch (type) {
                        case IDocumentMapping.REPORT_OVERVAC: {
                            XSSFCellStyle style = document.createCellStyle();
                            if (s.contains("Комната")) {
                                style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
                                style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
                                for (int i = 1; i <= 4; i++) {
                                    sheet.getRow(row_start).getCell(col_start + k - i).setCellStyle(style);
                                }
                                for (int i = 1; i <= 6; i++) {
                                    sheet.getRow(row_start).createCell(col_start + k + i);
                                    sheet.getRow(row_start).getCell(col_start + k + i).setCellStyle(style);
                                }
                            } else {
                                style.setBorderBottom(BorderStyle.THIN);
                                style.setBorderTop(BorderStyle.THIN);
                                style.setBorderRight(BorderStyle.THIN);
                                style.setBorderLeft(BorderStyle.THIN);
                                if (k == 1 || k == 10) {
                                    style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
                                } else {
                                    style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                                }

                            }
                            //style7.setFillPattern( XSSFCellStyle.LESS_DOTS);

                            sheet.getRow(row_start).getCell(col_start + k).setCellStyle(style);
                            break;
                        }
                        case IDocumentMapping.REPORT_PROVIDED_SERVICES_BY_CLIENT: {
                            if (s.contains(" В..")) {
                                sheet.getRow(row_start).getCell(col_start + k).setCellValue("Волонтеры");
                            }
                            break;
                        }
                    }


                    /*
                        CUSTOMIZATIONS DONE
                     */
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
                sheet.getRow(row_start).createCell(col_start + 1);
            }

            sheet.getRow(row_start).getCell(col_start).setCellValue(e.getKey());
            sheet.getRow(row_start).getCell(col_start + 1).setCellValue(e.getValue());
            row_start++;
        }

    }

}
