package ru.homeless.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.homeless.entities.Client;
import ru.homeless.entities.NightStay;
import ru.homeless.generators.GenericGenerator;
import ru.homeless.processors.WordDocumentReplaceProcessor;
import ru.homeless.services.IGenericService;

@Controller
public class GetGeneratedDocumentController {

	public static final Logger log = Logger.getLogger(GetGeneratedDocumentController.class);

	@Autowired
	private ServletContext context;

	@Autowired
	private IGenericService genericService;

	/*
	 * Example how to request with the parameter
	 */
	@RequestMapping(value = "/getClientByID", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String getGeneratedDocument(@RequestParam(value = "id", required = true, defaultValue = "7") String id) {
		String value = ((Client) getGenericService().getInstanceById(Client.class, Integer.parseInt(id))).toString();
		log.info(value);
		return value;
	}

	/*
	 * Example how to request all data from table
	 */
	@RequestMapping(value = "/getallnightstay", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public @ResponseBody
	String getAllNightStay() {
		String str_list = "";
		for (NightStay n : getGenericService().getInstances(NightStay.class)) {
			str_list += n.toString() + "<br>";
		}
		return str_list;
	}

	/*
	 * Sending the rewrited template
	 */
	@RequestMapping(value = "/getGeneratedWordDocument", method = RequestMethod.GET)
	public @ResponseBody 
	String getGeneratedWordDocument(HttpServletRequest request, HttpServletResponse response) {

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
		
		GenericGenerator gg = new GenericGenerator();
		document = gg.generate(request);
		// get document
		
		
		/*
		Map<String, String> map = new HashMap<String, String>();
		map.put("shit2", "говно 2");
		map.put("shit3", "говно 3");
		map.put("shit1", "говно 1");
		InputStream resourceAsStream = context.getResourceAsStream("/WEB-INF/templates/template.docx");
		
		try {
			document = WordDocumentReplaceProcessor.searchInParagraphs(new XWPFDocument(resourceAsStream), map);
		} catch (IOException e) {
			e.printStackTrace();
		}
*/
		try {
			out.flush();
			document.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Document sent";
	}

	public IGenericService getGenericService() {
		return genericService;
	}

	public void setGenericService(IGenericService genericService) {
		this.genericService = genericService;
	}

}
