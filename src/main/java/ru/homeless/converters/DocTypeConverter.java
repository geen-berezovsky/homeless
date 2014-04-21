package ru.homeless.converters;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ru.homeless.entities.DocType;

@FacesConverter(forClass = ru.homeless.entities.DocType.class, value = "docTypeConverter")
public class DocTypeConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	public static List<DocType> docTypesDB;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String submittedValue) {
		try {
			System.out.println("Got a number: " + submittedValue);
			if (submittedValue.trim().equals("")) {
				return null;
			} else {
				int number = Integer.parseInt(submittedValue);
				for (DocType d : docTypesDB) {
					if (d.getId() == number) {
						System.out.println("Returning a " + d.getCaption());
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
			return String.valueOf(((DocType) value).getId());
		}
	}

}
