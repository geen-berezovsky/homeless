package ru.homeless.goctypes;

import java.util.HashMap;

public class DocumentFreeTravel extends FormParametersHandler {
	private String city;
	
	private static String path = "WEB-INF/templates/DocumentFreeTravel.docx";

	public DocumentFreeTravel(HashMap<String, String> defaulParameters,
			String pathToTemplate) {
		super(defaulParameters, path);
	}

	@Override
	public HashMap<String, String> getUniqueParams() {
		// TODO Auto-generated method stub
		return null;
	}

}
