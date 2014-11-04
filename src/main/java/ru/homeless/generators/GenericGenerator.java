package ru.homeless.generators;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.homeless.parsers.HttpRequestParser;
import ru.homeless.shared.IDocumentMapping;

@Component
public class GenericGenerator {

    @Autowired
    HttpRequestParser hrp;

	public XWPFDocument generate(HttpServletRequest request) {
        if (hrp == null ) {
            hrp = new HttpRequestParser();
        }

		
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
