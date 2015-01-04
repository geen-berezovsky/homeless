package ru.homeless.mappings;

import java.util.Date;
import java.util.Map;

import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;

/**
 * Created by maxim on 30.11.14.
 */
public interface ICustomMappingExcelDocument {
	
    public SpreadsheetMLPackage getDocument(Map<String, Date> requestParameters);

}
