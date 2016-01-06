package ru.homeless.converters;

import ru.homeless.entities.ContractPoints;
import ru.homeless.entities.ShelterResult;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.io.Serializable;
import java.util.List;

@FacesConverter(value = "shelterResultConverter")
public class ShelterResultConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
    public static List<ShelterResult> shelterResultList;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String submittedValue) {
		try {
			if (submittedValue.trim().equals("")) {
				return null;
			} else {
				int number = Integer.parseInt(submittedValue);

				for (ShelterResult  shelterResult : shelterResultList) {
					if (shelterResult.getId() == number) {
						return shelterResult.getId();
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
			return String.valueOf(value);
		}
	}

}
