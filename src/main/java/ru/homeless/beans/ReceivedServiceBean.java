package ru.homeless.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.jboss.logging.Logger;

import ru.homeless.dao.ClientDAO;
import ru.homeless.dao.ReceivedServicesDAO;
import ru.homeless.entities.Client;
import ru.homeless.entities.RecievedService;
import ru.homeless.entities.ServicesType;
import ru.homeless.util.Util;


@ManagedBean (name = "services")
@ViewScoped
public class ReceivedServiceBean implements Serializable {

	private static final Logger log = Logger.getLogger(ReceivedServiceBean.class);
	private static final long serialVersionUID = 1L;
	private Date date;
	private List<String> selectedItems; //we can put only strings there
	private RecievedService selectedService;
	
	public ReceivedServiceBean() {
		resetForm();
	}
	
	private void resetForm() {
		setDate(Calendar.getInstance().getTime());
		setSelectedItems(new ArrayList<String>());
	}

	public List<ServicesType> getAvailableServices() {
		ReceivedServicesDAO rd = new ReceivedServicesDAO();
		return rd.getAvailableServiceTypes();
	}

	public List<String> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<String> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public void addSelectedServices(ClientFormBean cb) {
		for (ServicesType st : getAvailableServices()) {
			for (String s : selectedItems) {
				if (st.getCaption().equals(s)) {
					new ReceivedServicesDAO().addReceivedService(new ClientDAO().getClientById(cb.getClient().getId()), st, date);
				}
			}
		}
		resetForm();
		try {
			cb.reloadAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteService() {
		log.info("Deleting selected received service: "+selectedService.getServiceType().getCaption());
		new ReceivedServicesDAO().deleteReceivedService(selectedService);
	}
	
	
	public RecievedService getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(RecievedService selectedService) {
		this.selectedService = selectedService;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String formattedDate(Date query) {
		return Util.formatDate(query);
	}

}
