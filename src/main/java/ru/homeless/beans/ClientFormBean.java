package ru.homeless.beans;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.type.StringClobType;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

import ru.homeless.comparators.RecievedServiceSortingComparator;
import ru.homeless.configuration.Configuration;
import ru.homeless.converters.*;
import ru.homeless.entities.*;
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
    private List<String> regionTypes;

    private Region lastLivingRegion;
    private Region lastRegistrationRegion;

    private List<SubRegion> lastLivingSubRegions;
    private List<SubRegion> lastRegistrationSubRegions;

    private String header;
    private String documentsHeaderInline;
    private String contactsHeaderInline;
    private String commentsHeaderInline;
    private String contractsHeaderInline;


    //Technical variables for auto saving
    private int tabIndex = 0;
    private int prevTabIndex = 0;
    // *********************************

    public ClientFormBean()  {
        this.mainPanelVisibility = "display: none;";
        clientBreadwinners = new ArrayList<String>(); //avoid null pointer exception on save form method
        clientReasonsofhomeless = new ArrayList<String>(); // -//-
        clientChronicDisease = new ArrayList<String>(); // -//-
    }

    public void reload() throws SQLException {
        //just for search
        if (client!=null) {
            reloadAll(client.getId());
        }
    }

    public void reloadAll(int id) throws SQLException {

        log.info("Opening client with id = "+id);

        this.cid = id;
        this.mainPanelVisibility = "display: block;";

        File profile = new File(Configuration.profilesDir+"/"+cid);
        if (!profile.exists()) {
            profile.mkdirs();
        }

        reloadAll();
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.update("select_document");
        rc.update("m_tabview");
        rc.update("upload_photo_form");

        prevTabIndex = 0;
        tabIndex = 0;

    }

    public void updateDocumentsTabHeader() {
        List<Document> listOfDocumentsForTitle = getClientService().getInstancesByClientId(Document.class, client.getId());
        if (listOfDocumentsForTitle == null || listOfDocumentsForTitle.size() == 0) {
            documentsHeaderInline = "";
        } else {
            documentsHeaderInline = "text-decoration: underline;";
        }
    }

    public void updateContractsTabHeader() {
        List<ServContract> listOfContractsForTitle = getClientService().getInstancesByClientId(ServContract.class, client.getId());
        if (listOfContractsForTitle == null || listOfContractsForTitle.size() == 0) {
            contractsHeaderInline = "";
        } else {
            contractsHeaderInline = "text-decoration: underline;";
        }
    }


    public void reloadAll() throws SQLException {
        reloadClientData();
        reloadClientReceivedServices();

        //set region according subregion
        if (client.getLastLiving()==null) {
            client.setLastLiving(getClientService().getInstanceById(SubRegion.class, 1));
        }
        if (client.getLastRegistration()==null) {
            client.setLastRegistration(getClientService().getInstanceById(SubRegion.class,1));
        }
        setLastLivingRegion(client.getLastLiving().getRegion());
        setLastRegistrationRegion(client.getLastRegistration().getRegion());

        //pull subregions list for actual region
        lastLivingSubRegions = new ArrayList<>();
        lastLivingSubRegions.addAll(getClientService().getSubRegionsByRegion((Region) getLastLivingRegion()));

        lastRegistrationSubRegions = new ArrayList<>();
        lastRegistrationSubRegions.addAll(getClientService().getSubRegionsByRegion((Region) getLastRegistrationRegion()));

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

        RequestContext rc = RequestContext.getCurrentInstance();
        header = client.getSurname() + " " + client.getFirstname() + " " + client.getMiddlename() + getClientActualStatus(client);
        rc.update("header");

        updateDocumentsTabHeader();
        updateContractsTabHeader();

        if (client.getContacts() == null || client.getContacts().trim().replaceAll("\\<.*?>","").equals("")) {
            contactsHeaderInline = "";
        } else {
            contactsHeaderInline = "text-decoration: underline;";
        }

        if (client.getMemo() == null || client.getMemo().trim().replaceAll("\\<.*?>","").equals("")) {
            commentsHeaderInline = "";
        } else {
            commentsHeaderInline = "text-decoration: underline;";
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

    private String getClientActualStatus(Client client) {
        //Client may live in shelter, lived in shelter earlier, not lived in shelter at all or stored under calculation
        String result = " (ID = " + client.getId() ;
        if (client.getDeathDate() != null) {
            result += ",<span class=\"deathNotification\">";
            result += " умер";
            if (!client.isGender()) {
                result += "ла";
            }
            result += " "+Util.formatDate(client.getDeathDate());
            result += "</span>";
        }

        List<ShelterHistory> shelters = getClientService().getInstancesByClientId(ShelterHistory.class, client);
        if (shelters != null) {
            if (shelters.size() == 0) {
                result += ", в приюте ранее не проживал";
                if (!client.isGender()) {
                    result += "а";
                }
                result += checkRegistry();
                return result + ")";
            }

            for (ShelterHistory sh : shelters) {
                if (sh.getOutShelter()!= null && sh.getOutShelter().getTime() >= new Date().getTime()) {
                    result += ", в приюте проживает" + checkRegistry();
                    return result + ")";
                }
            }
            result += ", в приюте ранее проживал";
            if (!client.isGender()) {
                result += "а";
            }
            result += checkRegistry();
            return result + ")";
        } else return result + ", информация по проживанию в приюте и о выданных справках о регистрации в базе данных отсутствует)";
    }

    private String checkRegistry() {
        String result = "";
        if (getClientService().hasBeenRegistered(client.getId())) {
            result += ", состоит на учете";
        }
        return result;
    }

    public void reloadClientData() {
        setClient(getClientService().getInstanceById(Client.class, getCid())); //cannot be deprecated while opening client from search differ than other open methods
        //copy data to externalized class data
        if (client != null) {
            log.info("Client ID = " + client.getId() + " has been selected for usage");
            copyClientToClientData(client);
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

    public List<Education> getEducationTypes() {
        //refresh converter if new data available
        List<Education> list = getClientService().getInstances(Education.class);
        EducationConverter.edDB = new ArrayList<Education>();
        EducationConverter.edDB.addAll(list);
        return list;
    }

    public List<NightStay> getNightStayTypes() {
        //refresh converter if new data available
        List<NightStay> list = getClientService().getInstances(NightStay.class);
        NightStayConverter.nsDB = new ArrayList<NightStay>();
        NightStayConverter.nsDB.addAll(list);
        return  list;
    }

    public List<FamilyCommunication> getFamilyCommunicationTypes() {
        //refresh converter if new data available
        List<FamilyCommunication> list = getClientService().getInstances(FamilyCommunication.class);
        FComConverter.fcomDB = new ArrayList<FamilyCommunication>();
        FComConverter.fcomDB.addAll(list);
        return list;
    }

    public List<Region> getRegionTypes() {
        //refresh converter if new data available
        List<Region> list = getClientService().getInstances(Region.class);
        RegionIDConverter.regionsList = new ArrayList<Region>();
        RegionIDConverter.regionsList.addAll(list);
        return list;
    }


	/*
     * Get all Breadwinners from the database
	 */

    public List<String> getBreadwinnerTypes() {
        List<String> l = new ArrayList<String>();
        for (Breadwinner b : getClientService().getInstances(Breadwinner.class)) {
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
        for (Reasonofhomeless r : getClientService().getInstances(Reasonofhomeless.class)) {
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
        for (ChronicDisease cd : getClientService().getInstances(ChronicDisease.class)) {
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

    public void loadSubRegionsForLastLiving(ValueChangeEvent event) {
        lastLivingSubRegions = new ArrayList<>();
        lastLivingSubRegions.addAll(getClientService().getSubRegionsByRegion((Region) event.getNewValue()));
    }

    public void loadSubRegionsForLastRegistration(ValueChangeEvent event) {
        lastRegistrationSubRegions = new ArrayList<>();
        lastRegistrationSubRegions.addAll(getClientService().getSubRegionsByRegion((Region) event.getNewValue()));
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
    public void saveContactsOnly(ClientFormBean cfb) {
        FacesMessage msg = null;
        if (client !=null) {
            try {
                getClientService().updateInstance(client);
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Контакты сохранены", "");
                try {
                    cfb.reloadAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Контакты не сохранены!", "");
            }

            if (msg != null) {
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

        }
    }


    public void saveMemoOnly(ClientFormBean cfb) {
        FacesMessage msg = null;
        if (client !=null) {
            try {
                getClientService().updateInstance(client);
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Примечания сохранены", "");
                try {
                    cfb.reloadAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Примечания не сохранены!", "");
            }

            if (msg != null) {
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

        }
    }

    public void saveClientForm(ClientFormBean cfb) {
        FacesMessage msg = null;

        //only when client is already selected and not null
        //the client will be null when you just had open this application
        if (client != null) {
                //update "homeless till the moment" (don't move it after client data copying!)
                updateHomelessDate(selectedMonth, getSelectedYear());

            /*
            This is the workaround for keeping records while this code is not refactored
            Current issue is in overwriting existing clients for unknown reasons. As minimum, ClientDataBean should be deprecated.
            TODO: deprecate ClientDataBean нахрен
             */

            if (!client.getSurname().equalsIgnoreCase(getSurname()) && !Util.formatDate(client.getDate()).equals(Util.formatDate(getDate()))) {
                if (!client.getSurname().trim().equals("") && !client.getSurname().trim().equals("")) { //keep new clients
                    //произошла какая-то хрень, валим отсюда и показываем сообщение
                    log.error("OLD NAME: " + client.getFirstname() + " " + client.getSurname() + " " + client.getDate());
                    log.error("NEW NAME: " + getFirstname() + " " + getSurname() + " " + getDate());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "НЕПРЕДВИДЕННАЯ ОШИБКА!", "Защита от перезаписи существующего клиента. Пожалуйста, перезагрузите страницу."));
                    try {
                        cfb.reloadAll();
                       } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
             /*
             ***************************************************************************************************************
             */

                client = copyClientDataToClient(client);
                //Update Surname, FirstName and MiddleName for starting with uppercase letter
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
                for (Breadwinner b : getClientService().getInstances(Breadwinner.class)) {
                    for (String cb : clientBreadwinners) {
                        if (b.getCaption().equals(cb)) {
                            sb.add(b);
                        }
                    }
                }
                client.setBreadwinners(sb);

                Set<Reasonofhomeless> sr = new HashSet<Reasonofhomeless>();
                for (Reasonofhomeless b : getClientService().getInstances(Reasonofhomeless.class)) {
                    for (String cb : clientReasonsofhomeless) {
                        if (b.getCaption().equals(cb)) {
                            sr.add(b);
                        }
                    }
                }
                client.setReasonofhomeless(sr);

                Set<ChronicDisease> chd = new HashSet<ChronicDisease>();
                for (ChronicDisease b : getClientService().getInstances(ChronicDisease.class)) {
                    for (String cb : clientChronicDisease) {
                        if (b.getCaption().equals(cb)) {
                            chd.add(b);
                        }
                    }
                }
                client.setDiseases(chd);

            try {
                getClientService().updateInstance(client);
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Сохранено", "");
                try {
                    cfb.reloadAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Клиент не сохранен!", "");
            }

            if (msg != null) {
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
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

    public void refreshTabs(Integer pti) {

        //When user change the tab, he don't think about saving unsaved data.
        //So, let him think that this is a normal situation and perform auto save operation
        //Saving will run refreshing, so we can skip to do it again, except custom beans reloading

        FacesContext context = FacesContext.getCurrentInstance();
        if (cid != 0 && client != null) {

            ClientDocumentsBean cdb = context.getApplication().evaluateExpressionGet(context, "#{clientdocuments}", ClientDocumentsBean.class);
            ScanDocumentsBean sdb = context.getApplication().evaluateExpressionGet(context, "#{scandocuments}", ScanDocumentsBean.class);
            ClientContractsBean ccb = context.getApplication().evaluateExpressionGet(context, "#{clientcontracts}", ClientContractsBean.class);
            ClientShelterBean csb = context.getApplication().evaluateExpressionGet(context, "#{clientshelter}", ClientShelterBean.class);
            cdb.reload();
            sdb.reload();
            ccb.reload();
            csb.reload();

            //We can't just perform saveClientFormsaveClientForm(this) because we don't have such context. Currently, "this" is not an exact instance
            //which we need to send to the save function
            //Then, use javascript callback to perform the saving
            if (pti == 0) {
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("save0Tab();");
            }
            if (pti == 3) {
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("save3Tab();");
            }
            if (pti == 4) {
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("save4Tab();");
            }

            log.info("Reloaded all data for the selected client " + cid);
        }
    }

    public void tabChangeListener(TabChangeEvent event) {
        //workaround for getting the current selected tab
        //it cannot be done with basic way because the tag tabView is not included into <form> tag
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        TabView tabView = (TabView) event.getComponent();
        setTabIndex(Integer.parseInt(params.get(tabView.getClientId(context) + "_tabindex")));

        //But! If user started to change tabs before he type mandatory data, we have to reject this action
        if (this.getFirstname().trim().equals("") ||
                this.getSurname().trim().equals("") ||
                this.getMiddlename().trim().equals("") ||
                this.getDate() == null) {

            if ((tabIndex > 0)) { //for avoiding cycles when we set the active tab and tablchange listener perform it again
                RequestContext rc = RequestContext.getCurrentInstance();
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", ""));
                rc.execute("forceFirstTab();");
            }

        } else {
            refreshTabs(prevTabIndex);
        }
        prevTabIndex = tabIndex;
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


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
    public String getContactsHeaderInline() {
        return contactsHeaderInline;
    }

    public void setContactsHeaderInline(String contactsHeaderInline) {
        this.contactsHeaderInline = contactsHeaderInline;
    }

    public String getCommentsHeaderInline() {
        return commentsHeaderInline;
    }

    public void setCommentsHeaderInline(String commentsHeaderInline) {
        this.commentsHeaderInline = commentsHeaderInline;
    }

    @PostConstruct
    // special for converter and selection data model!
    public void init() {
        FComConverter.fcomDB = new ArrayList<FamilyCommunication>();
        FComConverter.fcomDB.addAll(getClientService().getInstances(FamilyCommunication.class));

        NightStayConverter.nsDB = new ArrayList<NightStay>();
        NightStayConverter.nsDB.addAll(getClientService().getInstances(NightStay.class));

        EducationConverter.edDB = new ArrayList<Education>();
        EducationConverter.edDB.addAll(getClientService().getInstances(Education.class));

        RegionIDConverter.regionsList = new ArrayList<Region>();
        RegionIDConverter.regionsList.addAll(getClientService().getInstances(Region.class));

        SubRegionIDConverter.regionsList = new ArrayList<>();
        SubRegionIDConverter.regionsList.addAll(getClientService().getInstances(SubRegion.class));
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
        client.setNightstay(getClientService().getInstanceByCaption(NightStay.class, "Нет ответа"));
        client.setEducation(getClientService().getInstanceByCaption(Education.class, "Нет ответа"));
        client.setFcom(getClientService().getInstanceByCaption(FamilyCommunication.class, "Нет ответа"));
        client.setLastLiving(getClientService().getInstanceById(SubRegion.class,1));
        client.setLastRegistration(getClientService().getInstanceById(SubRegion.class,1));
        client.setRegDate(new Date());

        getClientService().addInstance(client);
        session.setAttribute("cid", client.getId());
        log.info("Client with ID="+client.getId()+" successfully added to the database and set to the http session");
        copyClientToClientData(client);
        reloadAll(client.getId());

    }

    public void deleteClient() {
        HttpSession session = Util.getSession();

        session.removeAttribute("clientform");
        session.removeAttribute("stddoc");
        session.removeAttribute("clientshelter");
        session.removeAttribute("cit");

        try {

            log.info("Client with ID = "+client.getId() + " will be deleted");
            log.info("Deleting all client's documents...");

            List<Document> documents = getClientService().getInstancesByClientId(Document.class, client.getId());
            for (Document document : documents) {
                log.info("\tDeleting document id = "+document.getId());
                getClientService().deleteInstance(document);
                log.info("\t... done!");
            }

            for (Breadwinner breadwinner : client.getBreadwinners()) {
                log.info("\tDeleting breadwinner id = "+breadwinner.getId());
                getClientService().deleteInstance(breadwinner);
                log.info("\t... done!");
            }

            for (Reasonofhomeless reasonofhomeless : client.getReasonofhomeless()) {
                log.info("\tDeleting reasonofhomeless id = "+reasonofhomeless.getId());
                getClientService().deleteInstance(reasonofhomeless);
                log.info("\t... done!");
            }

            for (ChronicDisease chronicDisease : client.getDiseases()) {
                log.info("\tDeleting chronicDisease id = " + chronicDisease.getId());
                getClientService().deleteInstance(chronicDisease);
                log.info("\t... done!");
            }

            for (ServContract servContract : getClientService().getInstancesByClientId(ServContract.class, client.getId())) {
                log.info("\tDeleting servContract id = " + servContract.getId());
                getClientService().deleteInstance(servContract);
                log.info("\t... done!");
            }

            for (ShelterHistory shelterHistory : getClientService().getInstancesByClientId(ShelterHistory.class, client)) {
                log.info("\tDeleting shelterHistory id = " + shelterHistory.getId());
                getClientService().deleteInstance(shelterHistory);
                log.info("\t... done!");
            }



            client.setEducation(null);
            client.setNightstay(null);
            client.setFcom(null);

            log.info("Deleting the client itself...");
            getClientService().deleteInstance(client);
            log.info("Client with ID = "+client.getId() + " is deleted");
            client = null;
        } catch (Exception e) {
            log.error("Cannot delete client with ID = "+client.getId(),e);
        }
    }


    public List<String> getAllSubRegions(String query) {
        List<String> sr = new ArrayList<String>();

        for (SubRegion s : getClientService().getInstances(SubRegion.class)) {
            sr.add(s.getCaption());
        }

        return sr;
    }


    public String getDocumentsHeaderInline() {
        return documentsHeaderInline;
    }

    public void setDocumentsHeaderInline(String documentsHeaderInline) {
        this.documentsHeaderInline = documentsHeaderInline;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public int getPrevTabIndex() {
        return prevTabIndex;
    }

    public void setPrevTabIndex(int prevTabIndex) {
        this.prevTabIndex = prevTabIndex;
    }


    public void setRegionTypes(List<String> regionTypes) {
        this.regionTypes = regionTypes;
    }

    public Region getLastLivingRegion() {
        return lastLivingRegion;
    }

    public void setLastLivingRegion(Region lastLivingRegion) {
        this.lastLivingRegion = lastLivingRegion;
    }

    public Region getLastRegistrationRegion() {
        return lastRegistrationRegion;
    }

    public void setLastRegistrationRegion(Region lastRegistrationRegion) {
        this.lastRegistrationRegion = lastRegistrationRegion;
    }

    public List<SubRegion> getLastLivingSubRegions() {
        return lastLivingSubRegions;
    }

    public void setLastLivingSubRegions(List<SubRegion> lastLivingSubRegions) {
        this.lastLivingSubRegions = lastLivingSubRegions;
    }

    public List<SubRegion> getLastRegistrationSubRegions() {
        return lastRegistrationSubRegions;
    }

    public void setLastRegistrationSubRegions(List<SubRegion> lastRegistrationSubRegions) {
        this.lastRegistrationSubRegions = lastRegistrationSubRegions;
    }

    public String getContractsHeaderInline() {
        return contractsHeaderInline;
    }

    public void setContractsHeaderInline(String contractsHeaderInline) {
        this.contractsHeaderInline = contractsHeaderInline;
    }
}
