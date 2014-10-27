package ru.homeless.beans;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

import org.primefaces.model.StreamedContent;
import ru.homeless.comparators.RecievedServiceSortingComparator;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.Breadwinner;
import ru.homeless.entities.ChronicDisease;
import ru.homeless.entities.Client;
import ru.homeless.entities.Education;
import ru.homeless.entities.FamilyCommunication;
import ru.homeless.entities.NightStay;
import ru.homeless.entities.Reasonofhomeless;
import ru.homeless.entities.RecievedService;
import ru.homeless.services.GenericService;
import ru.homeless.util.Util;

@ManagedBean(name = "clientform")
@SessionScoped
public class ClientFormBean extends ClientDataBean implements Serializable {

    public static Logger log = Logger.getLogger(ClientFormBean.class);
    private static final long serialVersionUID = 1L;
    private int cid;

    private Client client;
    private List<RecievedService> servicesList;
    private String mainPanelVisibility;

    //chronical disasters 'another' feature
    private boolean anotherChronicalDisasterChecked = false;
    private String anotherChronicsStyle = "";

    //breadwinner 'another' feature
    private boolean anotherBreadwinnerChecked = false;
    private String anotherBreadwinnerStyle = ""; //fake

    //homeless reasons 'another' feature
    private boolean anotherHomelessReasonChecked = false;
    private String anotherHomelessReasonStyle = ""; //fake

    private int hasProfession = 0;
    private List<String> reasonofhomelessTypes = null;

    private List<String> clientChronicDisease = null; //fake
    private List<String> clientReasonsofhomeless = null;
    private List<String> clientBreadwinners = null;

    private List<String> chronicDiseaseTypes = null;

    private List<String> educationTypes;
    private List<String> nightStayTypes;
    private List<String> familyCommunicationTypes;
    private List<String> breadwinnerTypes;

    public ClientFormBean()  {
        this.mainPanelVisibility = "display: none;";
        clientBreadwinners = new ArrayList<String>(); //avoid null pointer exception on save form method
        clientReasonsofhomeless = new ArrayList<String>(); // -//-
        clientChronicDisease = new ArrayList<String>(); // -//-
    }

    public void reloadAll(int id) throws SQLException {
        this.cid = id;
        this.mainPanelVisibility = "display: block;";
        reloadAll();
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.update("m_tabview");
        rc.update("upload_photo_form");

        //RequestContext rc = RequestContext.getCurrentInstance();
        //rc.execute("reload();");

    }

    public void reloadAll() throws SQLException {
        reloadClientData();
        reloadClientReceivedServices();
        //chronic disasters
        if (client != null && client.getUniqDisease() != null && !client.getUniqDisease().trim().equals("")) {
            setAnotherChronicalDisasterChecked(true); //toggle checbox "Another" for chronical disasters
            toggleAnotherChronicsStyle(true); //show input "Another" for chronical disasters
        } else {
            setAnotherChronicalDisasterChecked(false); //toggle checbox "Another" for chronical disasters
            toggleAnotherChronicsStyle(false); //hide input "Another" for chronical disasters
        }

        //breadwinners
        if (client != null && client.getUniqBreadwinner() != null && !client.getUniqBreadwinner().trim().equals("")) {
            setAnotherBreadwinnerChecked(true); //toggle checbox "Another" for breadwinners
            toggleAnotherBreadwinnerStyle(true); //show input "Another" for breadwinners

        } else {
            setAnotherBreadwinnerChecked(false); //toggle checbox "Another" for breadwinners
            toggleAnotherBreadwinnerStyle(false); //hide input "Another" for breadwinners
        }

        //homeless reasons
        if (client != null && client.getUniqReason() != null && !client.getUniqReason().trim().equals("")) {
            setAnotherHomelessReasonChecked(true); //toggle checbox "Another" for homeless reasons
            toggleAnotherHomelessReasonStyle(true); //show input "Another" for homeless reasons
        } else {
            setAnotherHomelessReasonChecked(false); //toggle checbox "Another" for homeless reasons
            toggleAnotherHomelessReasonStyle(false); //hide input "Another" for homeless reasons
        }

        //set actual client id to session for using in another applications
        HttpSession session = Util.getSession();
        session.setAttribute("cid", cid);
        /*
        This is bufix for FireFox because it does not keep value after refreshing from cached page
        We are setting model values only for drop down lists manually (hope their value is small :-) )
         */
        log.info("Successfully loaded data for client id=" + this.cid + ", " + client.getSurname() + " " + client.getFirstname() + " " + client.getMiddlename());
    }

    public void reloadClientData() {
        setClient(getGenericService().getInstanceById(Client.class, getCid()));
        //copy data to externalized class data
        if (client != null) {
            log.info("Client ID = " + client.getId() + " has been selected for usage");
            copyClientToClientData(client);
            //setClientFormAvatar(clientFormAvatar);
            //setAvatar(getClientFormAvatar());
            updateHomelessDate();
        } else {
            log.info("Oops, but this client is not found in database...");
        }
    }


    public void reloadClientReceivedServices() {
        servicesList = new ArrayList<RecievedService>(client.getRecievedservices());
        Collections.sort(servicesList, new RecievedServiceSortingComparator());
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<String> getEducationTypes() {
        List<String> l = new ArrayList<String>();
        for (Education e : getGenericService().getInstances(Education.class)) {
            l.add(e.getCaption());
        }
        return l;
    }

    public List<String> getNightStayTypes() {
        List<String> l = new ArrayList<String>();
        for (NightStay ns : getGenericService().getInstances(NightStay.class)) {
            l.add(ns.getCaption());
        }
        return l;
    }

    public List<String> getFamilyCommunicationTypes() {
        List<String> l = new ArrayList<String>();
        for (FamilyCommunication f : getGenericService().getInstances(FamilyCommunication.class)) {
            l.add(f.getCaption());
        }
        return l;
    }

	/*
     * Get all Breadwinners from the database
	 */

    public List<String> getBreadwinnerTypes() {
        List<String> l = new ArrayList<String>();
        for (Breadwinner b : getGenericService().getInstances(Breadwinner.class)) {
            if (!b.getCaption().equals("Другие")) {
                l.add(b.getCaption());
            }
        }
        return l;
    }


    /*
     * Just copy the Set<Breadwinner> to List<Breadwinner> for JSF compatibility
     */
    public List<String> getClientBreadwinners() {
        if (client != null) {
            List<String> l = new ArrayList<String>();
            for (Breadwinner b : client.getBreadwinners()) {
                l.add(b.getCaption());
            }
            return l;
        } else {
            return null;
        }
    }


    /*
     * Get all Reasons Of Homeless from the database
     */
    public List<String> getReasonofhomelessTypes() {
        List<String> l = new ArrayList<String>();
        for (Reasonofhomeless r : getGenericService().getInstances(Reasonofhomeless.class)) {
            if (!r.getCaption().equals("Другие")) {
                l.add(r.getCaption());
            }
        }
        return l;
    }

    /*
     * Just copy the Set<Reasonofhomeless> to List<Reasonofhomeless> for JSF
     * compatibility
     */
    public List<String> getClientReasonsofhomeless() {
        if (client != null) {
            List<String> l = new ArrayList<String>();
            for (Reasonofhomeless r : client.getReasonofhomeless()) {
                l.add(r.getCaption());
            }
            return l;
        } else {
            return null;
        }
    }

    /*
     * Get all Chronic Diseases from the database
     */
    public List<String> getChronicDiseaseTypes() {
        List<String> l = new ArrayList<String>();
        for (ChronicDisease cd : getGenericService().getInstances(ChronicDisease.class)) {
            if (!cd.getCaption().equals("Другие")) {
                l.add(cd.getCaption());
            }
        }
        return l;
    }

    /*
     * Just copy the Set<ChronicDisease> to List<ChronicDisease> for JSF
     * compatibility
     */
    public List<String> getClientChronicDisease() {
        if (client != null) {
            List<String> l = new ArrayList<String>();
            for (ChronicDisease cd : client.getDiseases()) {
                l.add(cd.getCaption());
            }
            return l;
        } else {
            return null;
        }
    }

    public void setClientChronicDisease(List<String> s) {
        this.clientChronicDisease = s;
    }

    /*
     * Just fake for compatibility. Not used anywhere.
     */
    public List<RecievedService> getServicesList() {
        return servicesList;
    }

    public void setServicesList(List<RecievedService> servicesList) {
        this.servicesList = servicesList;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    // ****************** Working with dynamic change of "Другие" for Chronical Disasters section *******

    private void toggleAnotherChronicsStyle(boolean x) {
        if (x) {
            anotherChronicsStyle = "display:block;";
        } else {
            anotherChronicsStyle = "display:none;";
            setUniqDisease(null);
        }
    }

    public void selectedChronicsItemsChanged(ValueChangeEvent event) {
        RequestContext rc = RequestContext.getCurrentInstance();
        toggleAnotherChronicsStyle((Boolean) event.getNewValue());
        rc.update("chronical_distasters_form:anotherChronincs");
    }

    /*
     * This method invoked automatically when you have to change the value of exact checkbox
     * Without it ValueChangeListener will not work!!! Do not delete it!
     */
    public void itemSelected(final AjaxBehaviorEvent event) {
        //Stub
    }

    public String getAnotherChronicsStyle() {
        return anotherChronicsStyle;
    }

    public void setAnotherChronicsStyle(String anotherChronicsStyle) {
        this.anotherChronicsStyle = anotherChronicsStyle;
    }

    public boolean isAnotherChronicalDisasterChecked() {
        return anotherChronicalDisasterChecked;
    }

    public void setAnotherChronicalDisasterChecked(boolean anotherChronicalDisasterChecked) {
        this.anotherChronicalDisasterChecked = anotherChronicalDisasterChecked;
    }
    // ****************** Working with dynamic change of "Другие" for Chronical Disasters section *******

    // ****************** Working with dynamic change of "Другие" for Breadwinner section *******

    private void toggleAnotherBreadwinnerStyle(boolean x) {
        if (x) {
            anotherBreadwinnerStyle = "display:block;";
        } else {
            anotherBreadwinnerStyle = "display:none;";
            setUniqBreadwinner(null);
        }
    }

    public void selectedBreadwinnersItemsChanged(ValueChangeEvent event) {
        RequestContext rc = RequestContext.getCurrentInstance();
        toggleAnotherBreadwinnerStyle((Boolean) event.getNewValue());
        rc.update("chronical_distasters_form:anotherBreadwinners");
    }

    public String getAnotherBreadwinnerStyle() {
        return anotherBreadwinnerStyle;
    }

    public void setAnotherBreadwinnerStyle(String anotherBreadwinnerStyle) {
        this.anotherBreadwinnerStyle = anotherBreadwinnerStyle;
    }

    public boolean isAnotherBreadwinnerChecked() {
        return anotherBreadwinnerChecked;
    }

    public void setAnotherBreadwinnerChecked(boolean anotherBreadwinnerChecked) {
        this.anotherBreadwinnerChecked = anotherBreadwinnerChecked;
    }
    // ****************** Working with dynamic change of "Другие" for Breadwinner section *******

    // ****************** Working with dynamic change of "Другие" for Homeless Reasons section *******

    private void toggleAnotherHomelessReasonStyle(boolean x) {
        if (x) {
            anotherHomelessReasonStyle = "display:block;";
        } else {
            anotherHomelessReasonStyle = "display:none;";
            setUniqReason(null);
        }
    }

    public void selectedHomelessReasonItemsChanged(ValueChangeEvent event) {
        RequestContext rc = RequestContext.getCurrentInstance();
        toggleAnotherHomelessReasonStyle((Boolean) event.getNewValue());
        rc.update("homeless_reasons_form:anotherHomelessReasons");
    }


    public boolean isAnotherHomelessReasonChecked() {
        return anotherHomelessReasonChecked;
    }

    public void setAnotherHomelessReasonChecked(boolean anotherHomelessReasonChecked) {
        this.anotherHomelessReasonChecked = anotherHomelessReasonChecked;
    }

    public String getAnotherHomelessReasonStyle() {
        return anotherHomelessReasonStyle;
    }

    public void setAnotherHomelessReasonStyle(String anotherHomelessReasonStyle) {
        this.anotherHomelessReasonStyle = anotherHomelessReasonStyle;
    }
    // ****************** Working with dynamic change of "Другие" for Homeless Reasons section *******

    public void saveClientForm(ClientFormBean cfb) {
        FacesMessage msg = null;

        if (((client.getMemo() != null) && (!client.getMemo().equals(getMemo()))) || ((client.getContacts() != null) && (!client.getContacts().equals(getContacts())))) {
            log.info("Changed data on Comments or on Contacts tab - do not save the whole client settings!");
            client.setMemo(getMemo());
            client.setContacts(getContacts());
        } else {
            //update "homeless till the moment" (don't move it after client data copying!)
            updateHomelessDate(selectedMonth);
            client = copyClientDataToClient(client);
            //Update Surname, FirstName and MiddleName for starting with uppercase letter
/*
            if (client.getSurname().trim().equals("") || client.getMiddlename().trim().equals("") || client.getFirstname().trim().equals("")) {
                FacesMessage msg1 = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "ФИО не может содержать цифры, спецсимволы и пробелы и быть пустым!\nТолько русский или латинский текст, а также тире.", "Пожалуйста, проверьте форму!");
                    FacesContext.getCurrentInstance().addMessage(null, msg1);
                throw new ValidatorException(msg1);
            }
*/


            String fl = client.getSurname().toUpperCase().substring(0, 1);
            String ll = client.getSurname().toLowerCase().substring(1, client.getSurname().length());
            client.setSurname(fl + ll);
            fl = client.getFirstname().toUpperCase().substring(0, 1);
            ll = client.getFirstname().toLowerCase().substring(1, client.getFirstname().length());
            client.setFirstname(fl + ll);
            fl = client.getMiddlename().toUpperCase().substring(0, 1);
            ll = client.getMiddlename().toLowerCase().substring(1, client.getMiddlename().length());
            client.setMiddlename(fl + ll);

            //update homeless reasons, breadwinners, chronical disasters
            Set<Breadwinner> sb = new HashSet<Breadwinner>();
            for (Breadwinner b : getGenericService().getInstances(Breadwinner.class)) {
                for (String cb : clientBreadwinners) {
                    if (b.getCaption().equals(cb)) {
                        sb.add(b);
                    }
                }
            }
            client.setBreadwinners(sb);

            Set<Reasonofhomeless> sr = new HashSet<Reasonofhomeless>();
            for (Reasonofhomeless b : getGenericService().getInstances(Reasonofhomeless.class)) {
                for (String cb : clientReasonsofhomeless) {
                    if (b.getCaption().equals(cb)) {
                        sr.add(b);
                    }
                }
            }
            client.setReasonofhomeless(sr);

            Set<ChronicDisease> chd = new HashSet<ChronicDisease>();
            for (ChronicDisease b : getGenericService().getInstances(ChronicDisease.class)) {
                for (String cb : clientChronicDisease) {
                    if (b.getCaption().equals(cb)) {
                        chd.add(b);
                    }
                }
            }
            client.setDiseases(chd);

        }

        try {
            getGenericService().updateInstance(client);
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Сохранено", "");
            try {
                cfb.reloadAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Клиент не сохранен!", "");
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }


    public int getHasProfession() {
        if (client == null || client.getProfession() == null || client.getProfession().trim().equals("")) {
            return 0; // Нет ответа
        } else {
            if (client.getProfession().equalsIgnoreCase("нет")) {
                return 2; // Нет
            } else {
                return 1; // Да
            }
        }
    }

    public void refreshTabs() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (cid != 0 && client != null) {
            ClientDocumentsBean cdb = context.getApplication().evaluateExpressionGet(context, "#{clientdocuments}", ClientDocumentsBean.class);
            ClientContractsBean ccb = context.getApplication().evaluateExpressionGet(context, "#{clientcontracts}", ClientContractsBean.class);
            ClientShelterBean csb = context.getApplication().evaluateExpressionGet(context, "#{clientshelter}", ClientShelterBean.class);
            try {
                reloadAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            cdb.reload();
            ccb.reload();
            csb.reload();
            log.info("Reloaded all data for the selected client " + cid);
        }
    }

    public void tabChangeListener(TabChangeEvent event) {
        refreshTabs();
    }

    public void setHasProfession(int hasProfession) {
        this.hasProfession = hasProfession;
    }

    public void setReasonofhomelessTypes(List<String> reasonofhomelessTypes) {
        this.reasonofhomelessTypes = reasonofhomelessTypes;
    }

    public void setClientReasonsofhomeless(List<String> clientReasonsofhomeless) {
        this.clientReasonsofhomeless = clientReasonsofhomeless;
    }

    public void setChronicDiseaseTypes(List<String> chronicDiseaseTypes) {
        this.chronicDiseaseTypes = chronicDiseaseTypes;
    }

    public void setClientBreadwinners(List<String> clientBreadwinners) {
        this.clientBreadwinners = clientBreadwinners;
    }

    public void setEducationTypes(List<String> educationTypes) {
        this.educationTypes = educationTypes;
    }

    public void setNightStayTypes(List<String> nightStayTypes) {
        this.nightStayTypes = nightStayTypes;
    }

    public void setFamilyCommunicationTypes(List<String> familyCommunicationTypes) {
        this.familyCommunicationTypes = familyCommunicationTypes;
    }

    public void setBreadwinnerTypes(List<String> breadwinnerTypes) {
        this.breadwinnerTypes = breadwinnerTypes;
    }

    public String getMainPanelVisibility() {
        return mainPanelVisibility;
    }

    public void setMainPanelVisibility(String mainPanelVisibility) {
        this.mainPanelVisibility = mainPanelVisibility;
    }


    @PostConstruct
    public void postConstructExample() {
        /*
        Do nothing now. Just a stub.
         */
    }

    public void openPhotoDlg() {
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("realPhotoWv.show();");
    }


    public void addClient() throws SQLException {

        HttpSession session = Util.getSession();

        log.info("Creating new client and adding it to the database");
        Client client = new Client("", "", "", false, null, null, "", "");

        //Setting new values to the relations
        client.setNightstay(getGenericService().getInstanceByCaption(NightStay.class, "Нет ответа"));
        client.setEducation(getGenericService().getInstanceByCaption(Education.class, "Нет ответа"));
        client.setFcom(getGenericService().getInstanceByCaption(FamilyCommunication.class, "Нет ответа"));

        getGenericService().addInstance(client);
        session.setAttribute("cid", client.getId());
        log.info("Client with ID="+client.getId()+" successfully added to the database and set to the http session");
        copyClientToClientData(client);
        reloadAll(client.getId());

    }

}
