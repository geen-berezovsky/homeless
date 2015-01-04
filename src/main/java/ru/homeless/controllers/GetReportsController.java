package ru.homeless.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
	
	@RequestMapping(value = "/getReport", method = RequestMethod.GET)
	public @ResponseBody 
	String getReport (HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		
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
		
		SpreadsheetMLPackage document = null;

        if (gg == null) {
            gg = new GenericGenerator();
        }

		document = gg.generateExcelDocument(request);

	
		try {
			out.flush();
			document.save(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Docx4JException e) {
			e.printStackTrace();
		}
		
		return "Report sent";
	}
}
