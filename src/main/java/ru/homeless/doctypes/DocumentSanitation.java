package ru.homeless.doctypes;

import ru.homeless.shared.IDocumentMapping;

import java.util.HashMap;

public class DocumentSanitation extends FormParametersHandler {
	
	public DocumentSanitation(HashMap<String, String> parameters,
			String pathToTemplate) {
		super(parameters, IDocumentMapping.DOCUMENT_SANITATION_TEMPLATE_PATH);
	}

}
