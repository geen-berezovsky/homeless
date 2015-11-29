package ru.homeless.converters;

import ru.homeless.entities.DocType;
import ru.homeless.entities.FamilyCommunication;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.io.Serializable;
import java.util.List;

@FacesConverter(value = "fcomConverter")
public class FComConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	public static List<FamilyCommunication> fcomDB;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String submittedValue) {
		try {
			if (submittedValue.trim().equals("")) {
				return null;
			} else {
				int number = Integer.parseInt(submittedValue);
				for (FamilyCommunication d : fcomDB) {
					if (d.getId() == number) {
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
			return String.valueOf(((FamilyCommunication) value).getId());
		}
	}

}
