package ru.homeless.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.http.HttpHeaders;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.homeless.entities.Client;
import ru.homeless.entities.NightStay;
import ru.homeless.generators.GenericGenerator;
import ru.homeless.processors.WordDocumentReplaceProcessor;
import ru.homeless.services.GenericService;
import ru.homeless.services.IGenericService;
import ru.homeless.shared.IDocumentMapping;

@Controller
public class GetGeneratedDocumentController {

	public static final Logger log = Logger.getLogger(GetGeneratedDocumentController.class);

	@Autowired
	private ServletContext context;

    @Qualifier("GenericService")
    @Autowired
	private IGenericService genericService;

    @Autowired
    private GenericGenerator gg;


    /*
     * Example how to request with the parameter
     */
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
    public @ResponseBody
    String getDefaultMessage() {
        String res = "Документ \"Справка о социальной помощи\": /getGeneratedWordDocument?requestType=2&clientId=[clientId]&issueDate=[issueDate]<br><br>";
        res += "Документ \"Справка о получении социальной помощи (не препятствовать проезду)\": /getGeneratedWordDocument?requestType=4&clientId=[clientId]&travelCity=[travelCity]&issueDate=[issueDate]<br><br>";
        res += "Документ \"Направление на санитарную обработку\": /getGeneratedWordDocument?requestType=6&clientId=[clientId]&issueDate=[issueDate]<br><br>";
        return "Пожалуйста сформируйте GET запрос для формирования документа<br><br>"+res;
    }

	/*
	 * Sending the rewrited template
	 */
	@RequestMapping(value = "/getGeneratedWordDocument", method = RequestMethod.GET)
	public @ResponseBody 
	String getGeneratedWordDocument(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

		response.setContentType("application/vnd.ms-word");
		String headerResponse = "attachment;filename=";
		headerResponse = headerResponse.concat("Document.docx");
		response.addHeader("Content-disposition", headerResponse);

		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		XWPFDocument document = null;

        if (gg == null) {
            gg = new GenericGenerator();
        }

		document = gg.generate(request);

		try {
			out.flush();
			document.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Document sent";
	}

    /*
 * Sending the rewrited template
 */
    @RequestMapping(value = "/getGeneratedContract", method = RequestMethod.GET)
    public @ResponseBody
    String getGeneratedContract(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        response.setContentType("application/vnd.ms-word");
        String headerResponse = "attachment; filename*=UTF-8''";

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        XWPFDocument document = null;

        if (gg == null) {
            gg = new GenericGenerator();
        }

        document = gg.generate(request);
        String fileName = String.valueOf(context.getAttribute("resultFileName"));

        log.info("Trying to set the result file name "+fileName);
        headerResponse += fileName;
        response.addHeader("Content-Disposition", headerResponse);

        try {
            out.flush();
            document.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Document sent";
    }



}
