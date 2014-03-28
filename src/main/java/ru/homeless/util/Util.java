package ru.homeless.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Util {

	public static ResourceBundle bundle = ResourceBundle.getBundle("config", FacesContext.getCurrentInstance().getViewRoot().getLocale());

	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	}

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	public static String getContextRealPath() {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		return servletContext.getRealPath("");
	}

	public static String getUserName() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return session.getAttribute("username").toString();
	}

	public static String getUserId() {
		HttpSession session = getSession();
		if (session != null)
			return (String) session.getAttribute("userid");
		else
			return null;
	}

	public static String formatDate(Date query) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		return formatter.format(query);
	}

	public static String getImagesStorageLocation() {
		return bundle.getString("imagesStorageLocation");
	}

	public static String getTempImagesStorageLocation() {
		return bundle.getString("imagesTempStorageLocation");
	}

	public static void validateTextOnly(FacesContext ctx, UIComponent component, Object value) {
		String str = value.toString();
		if (!isTextOnlyValid(str)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"ФИО не может содержать цифры, спецсимволы и пробелы!\nТолько русский или латинский текст, а также тире.", "Пожалуйста, проверьте форму!");
			throw new ValidatorException(msg);
		}
	}

	public static boolean isTextOnlyValid(String str) {
		Pattern pattern = Pattern.compile("[a-zA-Z-]+|[а-яА-Я-]+");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static Date validateDateFormat(FacesContext ctx, UIComponent component, Object value) {
		String str = value.toString();
		if (!isDateValid(str)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Некорректный формат даты!", "Используйте дд.мм.гггг");
			throw new ValidatorException(msg);
		} else {
			try {
				Date d = new SimpleDateFormat("dd.MM.yyyy").parse(str);
				return d;
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public static boolean isDateValid(String str) {
		Pattern pattern = Pattern.compile("\\d{2}.\\d{2}.\\d{4}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			try {
				new SimpleDateFormat("dd.MM.yyyy").parse(str);
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	public static void validateNumFormat(FacesContext ctx, UIComponent component, Object value) {
		String str = value.toString();
		if (!isNumValid(str)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Некорректный формат!", "Допустимы только цифры.");
			throw new ValidatorException(msg);
		}
	}
	
	public static boolean isNumValid(String str) {
		Pattern pattern = Pattern.compile("^[0-9]+$");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

}
