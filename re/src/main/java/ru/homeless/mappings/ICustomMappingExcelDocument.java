package ru.homeless.mappings;

import java.util.Date;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;

/**
 * Created by maxim on 30.11.14.
 */
public interface ICustomMappingExcelDocument {
	
    public XSSFWorkbook getDocument(Map<String, Date> requestParameters);

}
