package ru.homeless.converters;

import ru.homeless.entities.Region;
import ru.homeless.entities.SubRegion;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.io.Serializable;
import java.util.List;

@FacesConverter(value = "subRegionIDConverter")
public class SubRegionIDConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
    public static List<SubRegion> regionsList;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String submittedValue) {
		try {
			if (submittedValue.trim().equals("")) {
				return null;
			} else {
				int number = Integer.parseInt(submittedValue);
				for (SubRegion region : regionsList) {
					if (region.getId() == number) {
                        return region;
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
            return String.valueOf(((SubRegion) value).getId());
		}
	}

}
