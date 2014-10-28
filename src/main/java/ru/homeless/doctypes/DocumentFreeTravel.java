package ru.homeless.doctypes;

import ru.homeless.shared.IDocumentMapping;

import java.util.HashMap;
import java.util.Map;

public class DocumentFreeTravel extends FormParametersHandler {
	private String city;
	
	public DocumentFreeTravel(Map<String, String> parameters
			) {
		super(parameters, IDocumentMapping.DOCUMENT_FREE_TRAVEL_TEMPLATE_PATH);
	}

}
