package ru.homeless.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.homeless.entities.Client;
import ru.homeless.generators.GenericGenerator;
import ru.homeless.services.IGenericService;

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
    String getDefaultMessage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String res = "";
        InputStream is = context.getResource("help.html").openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            res+=line;
        }
        br.close();
        return res;
    }

	/*
	 * Sending the rewrited template
	 */
	@RequestMapping(value = "/getGeneratedWordDocument", method = RequestMethod.GET)
	public @ResponseBody 
	String getGeneratedWordDocument(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        String headerResponse = "attachment; filename*=UTF-8''";
		headerResponse = headerResponse.concat("GeneratedDocument.docx");
		response.addHeader("Content-disposition", headerResponse);

		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		WordprocessingMLPackage document = null;

        if (gg == null) {
            gg = new GenericGenerator();
        }

		document = gg.generateWordDocument(request);

	
		try {
			out.flush();
			document.save(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Docx4JException e) {
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

        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        String headerResponse = "attachment; filename*=UTF-8''";

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        WordprocessingMLPackage document = null;

        if (gg == null) {
            gg = new GenericGenerator();
        }

        document = gg.generateWordDocument(request);
        
        String fileName = String.valueOf(context.getAttribute("resultFileName"));

        log.info("Trying to set the result file name "+fileName);
        headerResponse += fileName;
        response.addHeader("Content-Disposition", headerResponse);

        try {
            out.flush();
    		document.save(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Docx4JException e) {
			e.printStackTrace();
		}
        return "Document sent";
    }



}
