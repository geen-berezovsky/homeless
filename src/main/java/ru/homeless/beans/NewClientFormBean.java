package ru.homeless.beans;

import java.io.Serializable;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import ru.homeless.entities.Client;
import ru.homeless.entities.Education;
import ru.homeless.entities.FamilyCommunication;
import ru.homeless.entities.NightStay;
import ru.homeless.services.GenericService;

@ManagedBean(name = "newclientform")
@ViewScoped
public class NewClientFormBean extends ClientDataBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(NewClientFormBean.class);
	
	@ManagedProperty(value = "#{GenericService}")
	private GenericService genericService;

	public NewClientFormBean() {
		setSelectedMonth(0);
		setSelectedYear(1900);
	}

	public void addBasicClient() throws SQLException {
		log.info("Surname = " + getSurname());
		log.info("Firstname = " + getFirstname());
		log.info("Middlename = " + getMiddlename());
		log.info("Date = " + getFormattedDate());
		log.info("Gender = " + isGender());
		log.info("Month = " + getSelectedMonth());
		log.info("Year = " + getSelectedYear());
		log.info("Where = " + getWhereWasBorn());

		Client c = new Client(getSurname(), getFirstname(), getMiddlename(), isGender(), null, getDate(), "", "");
		updateHomelessDate(getSelectedYear());
		updateHomelessDate(getSelectedMonth());
		c.setHomelessdate(getHomelessdate());
		c.setWhereWasBorn(getWhereWasBorn());
		
		/*
		 * INITIALIZING RELEATED ENTITIES FOR AVOIDING NULL POINTER EXCEPTIONS ON VIEW SIDE
		 */
		
		c.setNightstay(getGenericService().getInstanceByCaption(NightStay.class, "Нет ответа"));
		c.setEducation(getGenericService().getInstanceByCaption(Education.class, "Нет ответа"));
		c.setFcom(getGenericService().getInstanceByCaption(FamilyCommunication.class, "Нет ответа"));

		
		FacesMessage msg = null;
		if (getSurname().trim().equals("") || getFirstname().trim().equals("") || getDate() == null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Ошибка добавления клиента", "Недостаточно данных!");
		} else {
			try {
				getGenericService().updateInstance(c);
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Сохранено", "");
			} catch (Exception e) {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Клиент не сохранен!", "");
			}
			
			FacesContext context = FacesContext.getCurrentInstance();
			ClientFormBean cfb = context.getApplication().evaluateExpressionGet(context, "#{clientform}", ClientFormBean.class);
			cfb.reloadAll(c.getId());
			RequestContext.getCurrentInstance().execute("addClientWv.hide()");
			
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public GenericService getGenericService() {
		return genericService;
	}

	public void setGenericService(GenericService genericService) {
		this.genericService = genericService;
	}

}
