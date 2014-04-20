package ru.homeless.converters;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ru.homeless.entities.DocType;

@FacesConverter(forClass = ru.homeless.entities.DocType.class, value = "docTypeConverter")
public class DocTypeConverter implements Converter {

	private static final long serialVersionUID = 1L;
	public static List<DocType> docTypesDB;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String submittedValue) {
		try {
		System.out.println("Got a number: "+submittedValue);
		if (submittedValue.trim().equals("")) {
			return null;
		} else {
			int number = Integer.parseInt(submittedValue);
			for (DocType d : docTypesDB) {
				if (d.getId() == number) {
					System.out.println("Returning a "+d.getCaption());
					return d;
				}
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals("")) {
			return "";
		} else {
			System.out.println("Returning "+String.valueOf(((DocType) value).getId()) + " "+String.valueOf(((DocType) value).getCaption()));
			//System.out.println("Total: "+docTypesDB.size());
			//System.out.println("First: "+((DocType) docTypesDB.get(0)).getCaption());
			//System.out.println("Last: "+((DocType) docTypesDB.get(docTypesDB.size()-1)).getCaption());
			return String.valueOf(((DocType) value).getId());
		}
	}

}
