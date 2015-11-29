package ru.homeless.converters;

import java.io.Serializable;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ru.homeless.entities.ContractResult;

@FacesConverter(value = "contractResultTypeConverter")
public class ContractResultTypeConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	public static List<ContractResult> contractResultTypesDB;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String submittedValue) {
		try {
			if (submittedValue.trim().equals("")) {
				return null;
			} else {
				int number = Integer.parseInt(submittedValue);
				for (ContractResult d : contractResultTypesDB) {
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
			return String.valueOf(((ContractResult) value).getId());
		}
	}

}
