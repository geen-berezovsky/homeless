package ru.homeless.tests;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.junit.Ignore;
import org.junit.Test;

import ru.homeless.processors.ReportDocumentProcessor;

public class BasicReportGenerationTest {
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
    	
    	
    	SaveToZipFile saver = new SaveToZipFile(r.createSheet(m));
		saver.save(outputfilepath);
    	
    }
	
}
