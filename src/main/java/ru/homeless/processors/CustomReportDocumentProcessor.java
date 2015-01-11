package ru.homeless.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.SpreadsheetML.JaxbSmlPart;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.docx4j.wml.ObjectFactory;
import org.xlsx4j.jaxb.Context;
import org.xlsx4j.sml.Row;
import org.xlsx4j.sml.STCellType;
import org.xlsx4j.sml.SheetData;

import ru.homeless.report.entities.IOldSchoolReport;
import ru.homeless.report.entities.OldSchoolReportEntity;

/*
 * This class inserts sheetData into template and returns complete generated report
 */
public class CustomReportDocumentProcessor {
    public static final Logger log = Logger.getLogger(CustomReportDocumentProcessor.class);
    private ObjectFactory factory;
    private SpreadsheetMLPackage document;
    private HashMap<String, String> mappings;

    public CustomReportDocumentProcessor(SpreadsheetMLPackage document) {
    	this.document = document;
    }
    
    public SpreadsheetMLPackage createSheet(List<OldSchoolReportEntity> extSheetData) throws Exception {
    	//HERE WE ONLY INSERT ALREADY PREPARED DATA INTO TEMPLATE
    	WorksheetPart sheet = (WorksheetPart) document.getParts().get(new PartName("/xl/worksheets/sheet1.xml"));
    	SheetData sheetData = sheet.getContents().getSheetData();
    	
    	mappings = new HashMap<String, String>();
    	
    	for (OldSchoolReportEntity o : extSheetData) {
    		
    		switch (o.getQueryType()) {
    			case IOldSchoolReport.QUERY_GENDER_TYPE: { addGenderReportData(sheetData, o); break; }

    			case IOldSchoolReport.QUERY_MARTIAL_STATUS_TYPE: { addMartialReportData(sheetData, o); break;}
				  
    												  
    			default:
    					break;
    		}
    		
    	}
    	
    	JaxbSmlPart smlPart = (JaxbSmlPart)document.getParts().get(new PartName("/xl/sharedStrings.xml"));
    	System.out.println("\n\nBEFORE\n\n:" + smlPart.getXML());
		smlPart.variableReplace(mappings);
    	System.out.println("\n\nAFTER\n\n:" + smlPart.getXML());
        return document;
    }
    
    
    private void addMartialReportData(SheetData sheetData, OldSchoolReportEntity o) {

    	mappings.put("[marriage:1]", "Неизвестно");
 	    mappings.put("[marriage:1]", "Состоит в браке");
 	    mappings.put("[marriage:1]", "Не состоит в браке");
 	    
 	    mappings.put("[marriage:1:value]", o.getValueAndQuantity().get("Неизвестно").toString());
 	    mappings.put("[marriage:2:value]", o.getValueAndQuantity().get("Состоит в браке").toString());
 	    mappings.put("[marriage:3:value]", o.getValueAndQuantity().get("Не состоит в браке").toString());
		
	}

	private void addGenderReportData(SheetData sheetData, OldSchoolReportEntity o) {
	
		mappings.put("[gender:1]", "Мужчины");
		mappings.put("[gender:2]", "Женщины");
		mappings.put("[gender:1:value]", o.getValueAndQuantity().get("Мужчины").toString());
		mappings.put("[gender:2:value]", o.getValueAndQuantity().get("Женщины").toString());
		
	}
    
    
}
