package ru.homeless.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;

import ru.homeless.entities.ShelterHistory;
import ru.homeless.services.ClientService;
import ru.homeless.util.Util;

/**
 * Bean contains reminders logic
 * @author Victorio
 */
@ManagedBean(name = "reminderBean")
@SessionScoped
public class ReminderBean implements Serializable{
	
    @ManagedProperty(value = "#{ClientService}")
	private ClientService clientService;
    
    //TODO: some kind of setting. Move it somewhere
    protected static final int DAY_COUNT_REMINDER = 3;
	
    public static Logger log = Logger.getLogger(ClientFormBean.class);
    private static final long serialVersionUID = 1L;
    
    public ClientService getClientService() {
		return clientService;
	}

	public void setClientService(ClientService clientService) {
		this.clientService = clientService;
	}

	public Collection<ShelterHistory> getClientsReminderForCurUser(){
    	return getClientsFinishingForCurUser();
    }
    
    private Collection<ShelterHistory> getClientsFinishingForCurUser(){
    	return clientService.getShelterEndsBefore(Util.getNDayFromCurrent(DAY_COUNT_REMINDER));
    }
    
    public Collection<ShelterHistory> getEndedShelterAndNotLeaving(){
    	return clientService.getEndedShelterAndNotLeaving();
    }
    
    
    
    public void onRowClck(final SelectEvent event) {
    	ShelterHistory myClientsEntity = (ShelterHistory) event.getObject();
        //RequestContext rc = RequestContext.getCurrentInstance();
        //rc.execute("myClientsWv.hide();");
        //rc.execute("myPrevClientsWv.hide();");

        FacesContext context = FacesContext.getCurrentInstance();
        ClientFormBean clientFormBean = context.getApplication().evaluateExpressionGet(context, "#{clientform}", ClientFormBean.class);
        try {
            clientFormBean.reloadAll(myClientsEntity.getClient().getId());
        } catch (SQLException e) {
            log.error("Error during ClienFormBean reloading for ShelterHistory=" + myClientsEntity.getId(), e);
        }
    }
    
    public boolean isHasSoonShelterEnded(){
    	return !getClientsFinishingForCurUser().isEmpty();
    }
    
    public boolean isHasShelterEndedAndNotLeaving(){
    	return !getEndedShelterAndNotLeaving().isEmpty();
    }
    
    public boolean getNeedToOpenReminder(){
    	return isHasSoonShelterEnded()
    	    || isHasShelterEndedAndNotLeaving();
    }

}
