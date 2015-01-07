package ru.homeless.tests;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.junit.Ignore;
import org.junit.Test;

import org.xlsx4j.jaxb.Context;
import org.xlsx4j.sml.Row;
import org.xlsx4j.sml.STCellType;
import org.xlsx4j.sml.SheetData;
import ru.homeless.processors.ReportDocumentProcessor;

public class BasicReportGenerationTest {

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

	@Ignore
	@Test
	public void testBasicReportGenerationTest()  throws Exception {
    	
		System.out.println(System.getProperties().toString());
		
    	String outputfilepath = System.getProperty("java.io.tmpdir")+"/OUT_CreateSimpleSpreadsheet.xlsx";
		SpreadsheetMLPackage pkg = null;
    	try {
    		pkg = SpreadsheetMLPackage.load(new File("src/main/resources/test-resources/template.xlsx")); 
		} catch (Docx4JException e1) {
			System.out.println(e1.getMessage());
		}
    	
		ReportDocumentProcessor r = new ReportDocumentProcessor(pkg);
    	Map m = new HashMap<Integer, List<String>>();

    	ArrayList a1 = new ArrayList<String>();
    	a1.add("1ABC");
    	a1.add("2EFG");
    	a1.add("3EFG");

    	ArrayList a2 = new ArrayList<String>();
    	a2.add("4ABC");
    	a2.add("5EFG");
    	a2.add("6EFG");

    	
    	m.put(0, a1);
    	m.put(1, a2);

        SpreadsheetMLPackage pkgUpd = r.createSheet(m);

        WorksheetPart sheet = pkgUpd.getWorkbookPart().getWorksheet(0);
        SheetData sheetData = sheet.getContents().getSheetData();

        setText(sheetData, 4, 4, "HELLO");
        setText(sheetData, 5, 5, "FUCKING");
        setText(sheetData, 6, 6, "WORLD");

        setText(sheetData, 3, 20, "БЛЯ123");

    	SaveToZipFile saver = new SaveToZipFile(pkgUpd);
		saver.save(outputfilepath);
    	
    }
	
}
