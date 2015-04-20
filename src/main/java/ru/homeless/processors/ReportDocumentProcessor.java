package ru.homeless.processors;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
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
    
    public XSSFWorkbook createSheet(Map<Integer, List<String>> extSheetData) throws Exception {
    	//HERE WE ONLY INSERT ALREADY PREPARED DATA INTO TEMPLATE
/*
    	//WorksheetPart sheet = document.getWorkbookPart().getWorksheet(0);
    	WorksheetPart sheet = (WorksheetPart) document.getParts().get(new PartName("/xl/worksheets/sheet1.xml"));
    	//SheetData sheetData = sheet.getJaxbElement().getSheetData();
    	SheetData sheetData = sheet.getContents().getSheetData();
    	
    	
    	for (Entry<Integer, List<String>> e : extSheetData.entrySet()) {
        	Row row = Context.getsmlObjectFactory().createRow();
        	for (String s : e.getValue()) {
        		Cell cell = Context.getsmlObjectFactory().createCell();
            	cell.setV(s);
            	row.getC().add(cell); //add new column (cell)
        	}
        	sheetData.getRow().add(row); //add new row
    	}
    	
        return document;
        */
        return null;
    }
    
    private static Cell createCell(String content) {
		Cell cell = Context.getsmlObjectFactory().createCell();
		CTXstringWhitespace ctx = Context.getsmlObjectFactory().createCTXstringWhitespace();
		ctx.setValue(content);
		CTRst ctrst = new CTRst();
		ctrst.setT(ctx);
		cell.setT(STCellType.INLINE_STR);
		cell.setIs(ctrst); // add ctrst as inline string
		return cell;
	}
    
    
}
