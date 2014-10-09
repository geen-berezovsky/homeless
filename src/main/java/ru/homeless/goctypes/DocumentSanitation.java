package ru.homeless.goctypes;

import java.util.HashMap;

public class DocumentSanitation extends FormParametersHandler {
	
	private static String path = "/WEB-INF/templates/DocumentSanitation.docx";
	
	public DocumentSanitation(HashMap<String, String> defaulParameters,
			String pathToTemplate) {
		super(defaulParameters, path);
	}

	@Override
	public HashMap<String, String> getUniqueParams() {
		return new HashMap<String, String>();
	}

}
