package ru.homeless.doctypes;

import ru.homeless.shared.IDocumentMapping;

import java.util.HashMap;

public class DocumentSocialHelp extends FormParametersHandler {

	public DocumentSocialHelp(HashMap<String, String> parameters,
                              String pathToTemplate) {
		super(parameters, IDocumentMapping.DOCUMENT_SOCIAL_HELP_TEMPLATE_PATH);
	}

}
