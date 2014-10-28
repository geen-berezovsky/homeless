package ru.homeless.generators;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import ru.homeless.parsers.HttpRequestParser;
import ru.homeless.shared.IDocumentMapping;

public class GenericGenerator {

	public XWPFDocument generate(HttpServletRequest request) {

		HttpRequestParser hrp = new HttpRequestParser(); 
		
		switch (Integer.parseInt(request.getParameter("requestType"))) {
			case IDocumentMapping.DOCUMENT_SOCIAL_HELP: {
				return hrp.generateSocialHelpDocument(request);
			}
			case IDocumentMapping.DOCUMENT_FREE_TRAVEL: {
				return hrp.generateFreeTravelDocument(request);
			}
            case IDocumentMapping.DOCUMENT_SANITATION: {
                return hrp.generateSanitationDocument(request);
            }
            default: {
	   			return null;
		   }
		}
	}

}
