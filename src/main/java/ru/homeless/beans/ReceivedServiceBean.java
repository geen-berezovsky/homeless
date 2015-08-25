package ru.homeless.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import org.primefaces.context.RequestContext;
import ru.homeless.entities.*;
import ru.homeless.services.GenericService;
import ru.homeless.util.Util;


@ManagedBean(name = "services")
@ViewScoped
public class ReceivedServiceBean implements Serializable {

    private static final Logger log = Logger.getLogger(ReceivedServiceBean.class);
    private static final long serialVersionUID = 1L;
    private Date date;
    private RecievedService selectedService;

    private Integer selectedItemCash;

    private String selectedItem;

    private String selectedItemComment;

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

    private void resetForm() {
        setDate(Calendar.getInstance().getTime());
        setSelectedItemComment("");
        setSelectedItemCash(0);
        this.cashValueVisibility = "display: none;";
        selectedService = null;
    }

    public List<ServicesType> getAvailableServices() {
        List<ServicesType> list = getGenericService().getInstances(ServicesType.class);
        List<ServicesType> todelItems = new ArrayList<ServicesType>();
        for (ServicesType st : list) {
            if (st.getDocument()) todelItems.add(st);
        }
        for (ServicesType i : todelItems) {
            list.remove(i);
        }
        return list;
    }


    public void itemSelected() {
        RequestContext rc = RequestContext.getCurrentInstance();

        for (ServicesType st : getAvailableServices()) {
            if (selectedItem.equals(st.getCaption())) {
                if (st.getMoney()) {
                    toggleCashVisibility(true);
                } else {
                    toggleCashVisibility(false);
                }
            }
        }
        if (selectedService == null) { //only if it is new record
            selectedItemComment = "";
            selectedItemCash = 0;
        }
        rc.update("add_services:additionalServiceValues");
    }

    public void addSelectedServices(ClientFormBean cb) {
            boolean update = false;
            if (selectedService == null ) {
                selectedService = new RecievedService();
            } else {
                update = true;
            }

            for (ServicesType st : getAvailableServices()) {
                if (selectedItem.equals(st.getCaption())) {
                    selectedService.setServiceType(st);
                    break;
                }
            }

            if (selectedService.getServiceType().getMoney()) {
                selectedService.setCash(selectedItemCash);
            }

            selectedService.setComment(selectedItemComment);

            HttpSession session = Util.getSession();
            String cids = session.getAttribute("cid").toString();

            Client c = null;
            if (cids != null && !cids.trim().equals("")) {
                c = getGenericService().getInstanceById(Client.class, Integer.parseInt(cids));
            }

            selectedService.setWorker((Worker) session.getAttribute("worker"));
            selectedService.setClient(c);
            selectedService.setDate(getDate());

            if (! update) {
                getGenericService().addInstance(selectedService);
            } else {
                getGenericService().updateInstance(selectedService);
            }
            resetForm();
            try {
                cb.reloadAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }

    public void updateValue() {
        setDate(selectedService.getDate());
        setSelectedItem(selectedService.getServiceType().getCaption());
        setSelectedItemCash(selectedService.getCash());
        setSelectedItemComment(selectedService.getComment());
        itemSelected(); //toggle cash field if necessary
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getSelectedItemComment() {
        return selectedItemComment;
    }

    public void setSelectedItemComment(String selectedItemComment) {
        this.selectedItemComment = selectedItemComment;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public Integer getSelectedItemCash() {
        return selectedItemCash;
    }

    public void setSelectedItemCash(Integer selectedItemCash) {
        this.selectedItemCash = selectedItemCash;
    }

    public String getCashValueVisibility() {
        return cashValueVisibility;
    }

    public void setCashValueVisibility(String cashValueVisibility) {
        this.cashValueVisibility = cashValueVisibility;
    }


}
