package ru.homeless.converters;

import ru.homeless.entities.Education;
import ru.homeless.entities.RecievedService;
import ru.homeless.entities.ServicesType;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.io.Serializable;
import java.util.List;

@FacesConverter(value = "serviceConverter")
public class ServiceConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	public static List<ServicesType> rcDB;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String submittedValue) {
		try {
			if (submittedValue.trim().equals("")) {
				return null;
			} else {
				int number = Integer.parseInt(submittedValue);
				for (ServicesType d : rcDB) {
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
			return String.valueOf(((ServicesType) value).getId());
		}
	}

}
