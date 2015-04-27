package ru.homeless.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.SpreadsheetML.JaxbSmlPart;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.docx4j.wml.ObjectFactory;

import ru.homeless.report.entities.ICustomStatisticsReport;
import ru.homeless.report.entities.CustomStatisticsReportEntity;

/*
 * This class inserts sheetData into template in custom fields and returns complete generated report
 */
public class CustomReportDocumentProcessor {
    public static final Logger log = Logger.getLogger(CustomReportDocumentProcessor.class);
    private ObjectFactory factory;
    private XSSFWorkbook document;
    private HashMap<String, String> mappings;

    public CustomReportDocumentProcessor(XSSFWorkbook document) {
    	this.document = document;
    }
    
    public XSSFWorkbook createSheet(List<CustomStatisticsReportEntity> extSheetData) throws Exception {
        Sheet sheet = document.getSheetAt(0);
    	mappings = new HashMap<String, String>();
    	
    	for (CustomStatisticsReportEntity o : extSheetData) {
    		
    		switch (o.getQueryType()) {
    			case ICustomStatisticsReport.QUERY_GENDER_TYPE: { addGenderReportData(sheet, o); break;}

    			case ICustomStatisticsReport.QUERY_MARTIAL_STATUS_TYPE: { addMartialReportData(sheet, o); break;}

                case ICustomStatisticsReport.QUERY_EDUCATION_TYPE: { addEducationReportData(sheet, o); break; }

                case ICustomStatisticsReport.QUERY_CHILDS_TYPE: { addChildsReportData(sheet, o); break; }

                case ICustomStatisticsReport.QUERY_STUDENTS_TYPE: { addStudentsReportData(sheet, o); break; }

                case ICustomStatisticsReport.QUERY_PROFESSION_TYPE: { addProfessionReportData(sheet, o); break; }

                case ICustomStatisticsReport.QUERY_LIVE_IN_FLAT_TYPE: { addLiveInFlatReportData(sheet, o); break; }
    												  
    			default:
    					break;
    		}
    		
    	}
        XSSFFormulaEvaluator.evaluateAllFormulaCells(document);
        return document;
    }

    private Integer getIntValue(CustomStatisticsReportEntity o, String s) {
        Integer value = o.getValueAndQuantity().get(s);
        if (value!=null) {
            return value;
        } else {
            return 0;
        }
    }

    private void addMartialReportData(Sheet sheet, CustomStatisticsReportEntity o) {
        sheet.getRow(34).getCell(1).setCellValue("Неизвестно");
        sheet.getRow(35).getCell(1).setCellValue("Состоит в браке");
        sheet.getRow(36).getCell(1).setCellValue("Не состоит в браке");

        sheet.getRow(34).getCell(2).setCellValue(getIntValue(o, "Неизвестно"));
        sheet.getRow(35).getCell(2).setCellValue(getIntValue(o, "Состоит в браке"));
        sheet.getRow(36).getCell(2).setCellValue(getIntValue(o, "Не состоит в браке"));
	}

	private void addGenderReportData(Sheet sheet, CustomStatisticsReportEntity o) {
        sheet.getRow(4).getCell(5).setCellValue("Мужчины");
        sheet.getRow(5).getCell(5).setCellValue("Женщины");

        sheet.getRow(4).getCell(6).setCellValue(getIntValue(o, "Мужчины"));
        sheet.getRow(5).getCell(6).setCellValue(getIntValue(o, "Женщины"));
	}

    private void addEducationReportData(Sheet sheet, CustomStatisticsReportEntity o) {
        int row_start = 57;
        int col_start = 1;


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

    private void addChildsReportData(Sheet sheet, CustomStatisticsReportEntity o) {
        int row_start = 57;
        int col_start = 5;


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

    private void addStudentsReportData(Sheet sheet, CustomStatisticsReportEntity o) {
        sheet.getRow(82).getCell(5).setCellValue("Сейчас учатся");
        sheet.getRow(83).getCell(5).setCellValue("Сейчас не учатся");
        sheet.getRow(84).getCell(5).setCellValue("Неизвестно");

        sheet.getRow(82).getCell(6).setCellValue(getIntValue(o, "Сейчас учатся"));
        sheet.getRow(83).getCell(6).setCellValue(getIntValue(o, "Сейчас не учатся"));
        sheet.getRow(84).getCell(6).setCellValue(getIntValue(o, "Неизвестно"));

    }

    private void addProfessionReportData(Sheet sheet, CustomStatisticsReportEntity o) {

        sheet.getRow(105).getCell(1).setCellValue("Да");
        sheet.getRow(106).getCell(1).setCellValue("Нет");
        sheet.getRow(107).getCell(1).setCellValue("Неизвестно");

        sheet.getRow(105).getCell(2).setCellValue(getIntValue(o, "Да"));
        sheet.getRow(106).getCell(2).setCellValue(getIntValue(o, "Нет"));
        sheet.getRow(107).getCell(2).setCellValue(getIntValue(o, "Неизвестно"));

    }


    private void addLiveInFlatReportData(Sheet sheet, CustomStatisticsReportEntity o) {

        sheet.getRow(105).getCell(5).setCellValue("Да");
        sheet.getRow(106).getCell(5).setCellValue("Нет");
        sheet.getRow(107).getCell(5).setCellValue("Неизвестно");

        sheet.getRow(105).getCell(6).setCellValue(getIntValue(o, "Да"));
        sheet.getRow(106).getCell(6).setCellValue(getIntValue(o, "Нет"));
        sheet.getRow(107).getCell(6).setCellValue(getIntValue(o, "Неизвестно"));

    }


}
