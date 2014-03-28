package ru.homeless.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ru.homeless.dao.WorkerDAO;
import ru.homeless.entities.Document;
import ru.homeless.entities.Worker;
import ru.homeless.util.Util;

@ManagedBean (name = "mysettings")
@ViewScoped
public class MySettings implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(MySettings.class);
	
	private Document document;
	private Worker worker;
	
	private String oldPassword;
	private String newPassword;
	private String newPassword2;
	
	public MySettings() {
		HttpSession session = Util.getSession();
		worker = (Worker) session.getAttribute("worker");
		
		//get active document data from database
		document = new WorkerDAO().getWorkerDocumentById(worker.getId());
		if (document == null) {
			document = new Document();
		}
	}
	
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public Worker getWorker() {
		return worker;
	}
	public void setWorker(Worker worker) {
		this.worker = worker;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getNewPassword2() {
		return newPassword2;
	}
	public void setNewPassword2(String newPassword2) {
		this.newPassword2 = newPassword2;
	}

	
	public void updateWorkerSettings() {
		FacesMessage msg = null;
		//first, check the password
		if (! oldPassword.trim().equals("")) {
			//it is right password?
			if (oldPassword.trim().equals(worker.getPassword())) {
				//found old password, let's check if new password fields are filled
				if (! newPassword.trim().equals("")) {
					//ok, let's check newPassword2
					if (! newPassword2.trim().equals("") && newPassword2.trim().equals(newPassword.trim())) {
						//all passwords are correct
						worker.setPassword(newPassword.trim());
						msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Изменение пароля", "Пароль успешно обновлен");
					} else {
						msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Изменение пароля", "Вы не указали или указали некорректное повторение нового пароля");
					}
				} else {
					//old password exists, but no new password provided
					msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Изменение пароля", "Вы не указали новый пароль");
				}
			} else {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Изменение пароля", "Вы указали некорректный старый пароль");
			}
		}
		if (msg != null) {
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		//then, check passport data
		if (document.getDocPrefix().trim().equals("") || document.getDocNum().trim().equals("") || 
				document.getDate() == null || document.getWhereAndWhom().trim().equals("")) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Паспортные данные", "Пожалуйста, заполните все данные по паспорту!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			//Saving document data
			Map<Boolean, String> res = new WorkerDAO().updateItem(document);
			if (res.get(true)!=null) {
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Паспортные данные обновлены", res.get(true));	
			} else {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Паспортные данные не обновлены!", res.get(false));
				
			}
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
		//then check all private fields
		if (worker.getSurname().trim().equals("") || worker.getFirstname().trim().equals("") || 
				worker.getRules() == null || worker.getWarrantNum().trim().equals("") ||
				worker.getWarrantDate() == null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Изменение данных пользователя", "Вы указали не все данные!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			//Saving document data
			Map<Boolean, String> res = new WorkerDAO().updateItem(worker);
			if (res.get(true)!=null) {
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Данные пользоватля обновлены", res.get(true));	
			} else {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Данны пользователя не обновлены!", res.get(false));
				
			}
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
	}
	
	//Validators
	public void validateTextOnly(FacesContext ctx, UIComponent component, Object value) {
		Util.validateTextOnly(ctx, component, value);
	}

	public void validateWarrantDateFormat(FacesContext ctx, UIComponent component, Object value) {
		try {
			Date date = (Date) value;
			worker.setWarrantDate(date);
		} catch (Exception e) {
			log.info("The date format is abnormal. Trying to re-parse it. "+e.getMessage());
			Date result = Util.validateDateFormat(ctx, component, value);
			if (result != null) {
				worker.setWarrantDate(result);
			}
		}
	}

	public void validateDocumentDateFormat(FacesContext ctx, UIComponent component, Object value) {
		try {
			Date date = (Date) value;
			document.setDate(date);
		} catch (Exception e) {
			log.info("The date format is abnormal. Trying to re-parse it. "+e.getMessage());
			Date result = Util.validateDateFormat(ctx, component, value);
			if (result != null) {
				document.setDate(result);
			}
		}
	}

	public void validateNumOnly(FacesContext ctx, UIComponent component, Object value) {
		Util.validateNumFormat(ctx, component, value);
	}
	
}
