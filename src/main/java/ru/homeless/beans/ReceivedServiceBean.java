package ru.homeless.beans;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;
import ru.homeless.converters.ServiceConverter;
import ru.homeless.entities.Client;
import ru.homeless.entities.RecievedService;
import ru.homeless.entities.ServicesType;
import ru.homeless.entities.Worker;
import ru.homeless.services.GenericService;
import ru.homeless.util.Util;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ManagedBean(name = "services")
@ViewScoped
public class ReceivedServiceBean implements Serializable {

    private static final Logger log = Logger.getLogger(ReceivedServiceBean.class);
    private static final long serialVersionUID = 1L;
    private RecievedService selectedService;
    private List<ServicesType> availableServices;

    @ManagedProperty(value = "#{GenericService}")
    private GenericService genericService;

    private void toggleCashVisibility(boolean value) {
        if (value)
            this.cashValueVisibility = "display: block;";
        else this.cashValueVisibility = "display: none;";
    }

    private String cashValueVisibility;

    public ReceivedServiceBean() {
        resetForm();
    }

    public void resetForm() {
        selectedService = new RecievedService();
        selectedService.setComment("");
        selectedService.setDate(new Date());
        selectedService.setCash(0);
        this.cashValueVisibility = "display: none;";
        log.info("Reseting form with add/edit received service");

    }

    public List<ServicesType> getAvailableServices() {
        List<ServicesType> list = getGenericService().getInstances(ServicesType.class);

        //Removing all Справки from the list
        List<ServicesType> todelItems = new ArrayList<ServicesType>();
        for (ServicesType st : list) {
            if (st.getDocument()) todelItems.add(st);
        }
        for (ServicesType i : todelItems) {
            list.remove(i);
        }

        //Pick in the converter's data
        ServiceConverter.rcDB = new ArrayList<ServicesType>();
        ServiceConverter.rcDB.addAll(list);
        return list;
    }


    public void itemSelected() {
        if (selectedService.getServiceType().getMoney()) {
            toggleCashVisibility(true);
        } else {
            toggleCashVisibility(false);
        }
    }

    public void updateItem(ClientFormBean cb) {
        FacesMessage msg = null;
        HttpSession session = Util.getSession();
        selectedService.setWorker((Worker) session.getAttribute("worker"));
        selectedService.setClient(cb.getClient());
        log.info("Update Received Service: " + selectedService.getServiceType().getCaption() + ", " + Util.formatDate(selectedService.getDate()) + ", for client " + selectedService.getClient().toString());
        try {
            getGenericService().updateInstance(selectedService);
            resetForm();
            //Because the client is global for application, we need to reload for him/her actual ReceivedService from the database
            cb.reloadClientReceivedServices();
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Данные по оказанной услуге сохранены", "");
        } catch (Exception e) {
            log.error(e);
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Данные по оказанной услуге не сохранены", "");
        }
        if (msg != null) {
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public void handleCloseOperation() {
        FacesMessage msg = null;
        msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Данные по оказанной услуге не сохранены", "Пожалуйста, нажимайте кнопку Обновить для сохранения данных!");
        if (msg != null) {
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        //auto saving IS NOT WORKING CURRENTLY. Please fix when you will have enough time and this is 'must have'
        //RequestContext rc = RequestContext.getCurrentInstance();
        //rc.execute("saveEditServicesForm()");
        //log.info("Dialog Edit Service has been closed unexpectedly. The selected Service is saved automatically.");
    }

    public void prepareForNewService() {
        resetForm();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("addServiceWv.show();");
    }

    public void addItem(ClientFormBean cb) {
        FacesMessage msg = null;
        HttpSession session = Util.getSession();
        selectedService.setWorker((Worker) session.getAttribute("worker"));
        selectedService.setClient(cb.getClient());
        log.info("Adding Received Service: "+selectedService.getServiceType().getCaption()+", "+Util.formatDate(selectedService.getDate())+", for client "+selectedService.getClient().toString());
        try {
            getGenericService().addInstance(selectedService);
            resetForm();
            //Because the client is global for application, we need to reload for him/her actual ReceivedService from the database
            cb.reloadClientReceivedServices();
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Данные по оказанной услуге сохранены", "");
        } catch (Exception e) {
            log.error(e);
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Данные по оказанной услуге не сохранены", "");
        }
        if (msg != null) {
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }


    public void deleteService() {
        Client c = selectedService.getClient();
        c.getRecievedservices().remove(selectedService);
        getGenericService().updateInstance(c);
    }

    /*
        ******************* GETTERS AND SETTERS *******************
     */

    public RecievedService getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(RecievedService selectedService) {
        this.selectedService = selectedService;
    }

    public String formattedDate(Date query) {
        return Util.formatDate(query);
    }

    public GenericService getGenericService() {
        return genericService;
    }

    public void setGenericService(GenericService genericService) {
        this.genericService = genericService;
    }

    public String getCashValueVisibility() {
        return cashValueVisibility;
    }

    public void setCashValueVisibility(String cashValueVisibility) {
        this.cashValueVisibility = cashValueVisibility;
    }


    public void setAvailableServices(List<ServicesType> availableServices) {
        this.availableServices = availableServices;
    }
}
