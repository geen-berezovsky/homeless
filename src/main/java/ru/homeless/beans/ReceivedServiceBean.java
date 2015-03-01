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
import ru.homeless.entities.Client;
import ru.homeless.entities.RecievedService;
import ru.homeless.entities.ServicesType;
import ru.homeless.entities.Worker;
import ru.homeless.services.GenericService;
import ru.homeless.util.Util;


@ManagedBean(name = "services")
@ViewScoped
public class ReceivedServiceBean implements Serializable {

    private static final Logger log = Logger.getLogger(ReceivedServiceBean.class);
    private static final long serialVersionUID = 1L;
    private Date date;
    private List<String> selectedItems; //we can put only strings there
    private RecievedService selectedService;

    private Integer selectedItemCash;

    private boolean selectedItemRemoved;

    private String curService;

    private String selectedItem;

    private String selectedItemComment;

    private List<RecievedService> selectedItemsList;

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
        log.info("Reset date: "+date);
        setDate(Calendar.getInstance().getTime());
        log.info("Reseted date: "+date);
        setSelectedItems(new ArrayList<String>());
        setSelectedItemComment("");
        setSelectedItemCash(0);
        if (selectedItemsList != null) {
            selectedItemsList.clear();
        } else {
            selectedItemsList = new ArrayList<RecievedService>();
        }
        this.cashValueVisibility = "display: none;";
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

    public List<String> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<String> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public void saveMetadataForService() {
        //log.info("Selected item: "+selectedItem);
        if (selectedItemsList == null) {
            //what is it?
            return;
        }
        for (RecievedService rs : selectedItemsList) {
            if (rs.getServiceType().getCaption().equals(selectedItem)) {
                rs.setComment(selectedItemComment);
                rs.setDate(date);
                if (rs.getServiceType().getMoney()) {
                    rs.setCash(selectedItemCash);
                }
            }
        }

    }

    @SuppressWarnings("unchecked")
    public void selectedItemsChanged(ValueChangeEvent event) {
        List<String> oldValue = (List<String>) event.getOldValue();
        List<String> newValue = (List<String>) event.getNewValue();

        if (oldValue == null) {
            oldValue = Collections.emptyList();
        }

        List<ServicesType> actualList = getAvailableServices();
        if (selectedItemsList == null) selectedItemsList = new ArrayList<RecievedService>();

        //for avoiding ConcurrentModificationException
        List<RecievedService> itemsToPostRemove = new ArrayList<RecievedService>();

        if (oldValue.size() > newValue.size()) {
            oldValue = new ArrayList<String>(oldValue);
            oldValue.removeAll(newValue);
            selectedItem = oldValue.iterator().next();
            selectedItemRemoved = true;
            log.info("Item "+selectedItem+" removed");
            for (ServicesType st : actualList) {
                if (st.getCaption().equals(selectedItem)) {
                    //check selectedItemsList and remove unchecked ReceivedService item
                    for (RecievedService rs : selectedItemsList) {
                        if (rs.getServiceType().getId() == st.getId()) {
                            itemsToPostRemove.add(rs);
                        }
                    }
                }
            }
            if (itemsToPostRemove.size()>0) {
                for (RecievedService rs : itemsToPostRemove) {
                    selectedItemsList.remove(rs);
                }
            }
        }
        else {
            newValue = new ArrayList<String>(newValue);
            newValue.removeAll(oldValue);
            selectedItem = newValue.iterator().next();
            selectedItemRemoved = false;
            log.info("Item "+selectedItem+" added");
            for (ServicesType st : actualList) {
                if (st.getCaption().equals(selectedItem)) {
                    HttpSession httpsession = Util.getSession();
                    Worker w = (Worker) httpsession.getAttribute("worker");
                    int cid  = (int) httpsession.getAttribute("cid");
                    Client c = getGenericService().getInstanceById(Client.class, cid);
                    log.info("Actual date: "+date);
                    selectedItemsList.add(new RecievedService(w,c,st,date));
                }
            }
        }
    }

    public void itemSelected(AjaxBehaviorEvent event) {
        System.out.println("Selected item: " + selectedItem);
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

        for (RecievedService rs : selectedItemsList) {
            log.info("RS: "+rs.getServiceType() +", cash = "+rs.getCash()+", COMMENT: "+rs.getComment());
        }

        selectedItemComment = "";
        selectedItemCash = 0;
        rc.update("add_services:additionalServiceValues");


    }

    public void addSelectedServices(ClientFormBean cb) {
        RequestContext rc = RequestContext.getCurrentInstance();

        for (RecievedService rs : selectedItemsList) {

            //set specified date for all selected (it may be changed AFTER creating the array with selected items
            //IF YOU NEED TO SET DATE FOR EACH SEPARATE RECEIVED SERVICE, PLEASE REMOVE NEXT STRING!
            rs.setDate(getDate());

            getGenericService().addInstance(rs);
            getGenericService().updateInstance(rs.getClient());
        }

        resetForm();
        try {
            cb.reloadAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteService() {
        log.info("Deleting selected received service: " + selectedService.getServiceType().getCaption());
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

    public GenericService getGenericService() {
        return genericService;
    }

    public void setGenericService(GenericService genericService) {
        this.genericService = genericService;
    }

    public String getCurService() {
        return curService;
    }

    public void setCurService(String curService) {
        this.curService = curService;
    }

    public boolean isSelectedItemRemoved() {
        return selectedItemRemoved;
    }

    public void setSelectedItemRemoved(boolean selectedItemRemoved) {
        this.selectedItemRemoved = selectedItemRemoved;
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

    public List<RecievedService> getSelectedItemsList() {
        return selectedItemsList;
    }

    public void setSelectedItemsList(List<RecievedService> selectedItemsList) {
        this.selectedItemsList = selectedItemsList;
    }


}
