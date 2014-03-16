package ru.homeless.converters;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ru.homeless.dao.DocTypesDAO;
import ru.homeless.entities.DocType;

@FacesConverter(forClass = ru.homeless.entities.DocType.class, value = "docTypeConverter")
public class DocTypeConverter implements Converter {

	public static List<DocType> docTypesDB;

	public DocTypeConverter() {
		DocTypesDAO nsDAO = new DocTypesDAO();
		docTypesDB = new ArrayList<DocType>();
		for (DocType ns : nsDAO.getAvailbleDocTypes()) {
			docTypesDB.add(ns);
		}
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String submittedValue) {
		if (submittedValue.trim().equals("")) {
			return null;
		} else {
			int number = Integer.parseInt(submittedValue);
			for (DocType d : docTypesDB) {
				if (d.getId() == number) {
					return d;
				}
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals("")) {
			return "";
		} else {
			return String.valueOf(((DocType) value).getId());
		}
	}

}
