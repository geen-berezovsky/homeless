package ru.homeless.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.homeless.generators.GenericGenerator;

@Controller
public class GetReportsController {

	public static final Logger log = Logger.getLogger(GetReportsController.class);
	
	@Autowired
	private ServletContext context;
	
	@Autowired
    private GenericGenerator gg;
	
	//application/vnd.ms-excel.sheet.macroEnabled.12
	
	
	@RequestMapping(value = "/getReport", method = RequestMethod.GET)
	public @ResponseBody 
	String getReport (HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		log.info("Called R");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String headerResponse = "attachment; filename*=UTF-8''";
		headerResponse = headerResponse.concat("NewReport.xlsx");
		response.addHeader("Content-disposition", headerResponse);

		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

        XSSFWorkbook document = null;

        if (gg == null) {
            gg = new GenericGenerator();
        }

        log.info("Called Report\n"+request);
		document = gg.generateExcelDocument(request);

	
		try {
			out.flush();
            document.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "Report sent";
	}
	
	@RequestMapping(value = "/getMacroReport", method = RequestMethod.GET)
	public @ResponseBody 
	String getMacroReport (HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        log.info("Called MR");
		response.setContentType("application/vnd.ms-excel.sheet.macroEnabled.12");
        String headerResponse = "attachment; filename*=UTF-8''";
		headerResponse = headerResponse.concat("NewReport.xlsm");
		response.addHeader("Content-disposition", headerResponse);

		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

        XSSFWorkbook document = null;

        if (gg == null) {
            gg = new GenericGenerator();
        }
        log.info("Called Macro Report\n"+request);
		document = gg.generateExcelDocument(request);
        log.info("Document: "+document);
	
		try {
            document.write(out);
            out.flush();
			out.close();
		} catch (IOException e) {
            e.printStackTrace();
        }

		return "Report sent";
	}
}
