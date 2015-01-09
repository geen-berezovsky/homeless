package ru.homeless.processors;

import java.util.List;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.PartName;
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

    public CustomReportDocumentProcessor(SpreadsheetMLPackage document) {
    	this.document = document;
    }
    
    public SpreadsheetMLPackage createSheet(List<OldSchoolReportEntity> extSheetData) throws Exception {
    	//HERE WE ONLY INSERT ALREADY PREPARED DATA INTO TEMPLATE
    	WorksheetPart sheet = (WorksheetPart) document.getParts().get(new PartName("/xl/worksheets/sheet1.xml"));
    	SheetData sheetData = sheet.getContents().getSheetData();
    	for (OldSchoolReportEntity o : extSheetData) {
    		
    		switch (o.getQueryType()) {
    			case IOldSchoolReport.QUERY_XXX_TYPE: addGenderReportData(sheetData, o);
    												  break;
    			default:
    					break;
    		}
    		
    	}
    	
    	SaveToZipFile saver = new SaveToZipFile(document);
		saver.save("C:/tmp/out.xlsm");
    	
        return document;
    }
    
    
    private void addGenderReportData(SheetData sheetData, OldSchoolReportEntity o) {
    	setText(sheetData, 5, 6, "Мужчины");
    	setText(sheetData, 6, 6, o.getValueAndQuantity().get("Мужчины").toString());
    	setText(sheetData, 5, 7, "Женщины");
    	setText(sheetData, 6, 7, o.getValueAndQuantity().get("Женщины").toString());
    	
	}

	public void setText(SheetData sheetData, int column, int row, String value) {

        if (sheetData.getRow().size() < row) {
            for (int i = sheetData.getRow().size(); i<= row; i++) {
                sheetData.getRow().add(Context.getsmlObjectFactory().createRow());
            }
        }

        Row nRow = sheetData.getRow().get(row-1);

        if (nRow.getC().size() < column) {
            for (int i = nRow.getC().size(); i <= column; i++) {
                nRow.getC().add(Context.getsmlObjectFactory().createCell());
            }
        }

        sheetData.getRow().get(row-1).getC().get(column-1).setT(STCellType.STR); //set the type TEXT
        sheetData.getRow().get(row-1).getC().get(column-1).setV(value); //set the value

    }


    
    
    
}
