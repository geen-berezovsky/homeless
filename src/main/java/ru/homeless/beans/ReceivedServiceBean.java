package ru.homeless.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import ru.homeless.entities.Client;
import ru.homeless.entities.RecievedService;
import ru.homeless.entities.ServicesType;
import ru.homeless.entities.Worker;
import ru.homeless.services.ClientControlService;
import ru.homeless.util.Util;


@ManagedBean (name = "services")
@ViewScoped
public class ReceivedServiceBean implements Serializable {

	private static final Logger log = Logger.getLogger(ReceivedServiceBean.class);
	private static final long serialVersionUID = 1L;
	private Date date;
	private List<String> selectedItems; //we can put only strings there
	private RecievedService selectedService;
	
	@ManagedProperty(value = "#{GenericService}")
	private ClientControlService genericService;

	
	public ReceivedServiceBean() {
		resetForm();
	}
	
	private void resetForm() {
		setDate(Calendar.getInstance().getTime());
		setSelectedItems(new ArrayList<String>());
	}

	public List<ServicesType> getAvailableServices() {
		return getGenericService().getInstances(ServicesType.class);
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
					HttpSession httpsession = Util.getSession();
					Worker w = (Worker) httpsession.getAttribute("worker");
					
					Client c = getGenericService().getInstanceById(Client.class, cb.getClient().getId());
					c.getRecievedservices().add(new RecievedService(w,c,st,date)); //adding new received service
					getGenericService().updateInstance(c);
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
		Client c = selectedService.getClient();
		c.getRecievedservices().remove(selectedService);
		getGenericService().updateInstance(c);
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

	public ClientControlService getGenericService() {
		return genericService;
	}

	public void setGenericService(ClientControlService genericService) {
		this.genericService = genericService;
	}

}
