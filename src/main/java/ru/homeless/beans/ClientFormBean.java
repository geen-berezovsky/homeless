package ru.homeless.beans;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ru.homeless.comparators.RecievedServiceSortingComparator;
import ru.homeless.configuration.Configuration;
import ru.homeless.converters.*;
import ru.homeless.entities.*;
import ru.homeless.services.ClientService;
import ru.homeless.util.Util;

@ManagedBean(name = "clientform")
@ViewScoped
/**
 * This is the main (base) form for the Client and it is a mirror for all Client's properties
 * Refactoring is done at 13.02.2016
 */
public class ClientFormBean implements Serializable {

    public static Logger log = Logger.getLogger(ClientFormBean.class);
    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{ClientService}")
    private ClientService clientService;

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


    private boolean homelessDateNotNull = false;

    private Region lastLivingRegion;
    private Region lastRegistrationRegion;

    private List<SubRegion> lastLivingSubRegions;
    private List<SubRegion> lastRegistrationSubRegions;

    private String header;
    private String documentsHeaderInline;
    private String contactsHeaderInline;
    private String commentsHeaderInline;
    private String contractsHeaderInline;

    //UTIL variables
    private int selectedMonth;
    private int selectedYear;
    private String originalPhotoFilePath;
    private StreamedContent clientFormAvatar;
    private StreamedContent clientFormRealPhoto;
    private String formattedDate;

    //Technical variables for auto saving
    private int tabIndex = 0;
    private int prevTabIndex = 0;
    // *********************************
    
    private final String IIID = String.valueOf(new Random(System.currentTimeMillis()).nextInt());
    
    public ClientFormBean()  {
        this.mainPanelVisibility = "display: none;";
        clientBreadwinners = new ArrayList<String>(); //avoid null pointer exception on save form method
        clientReasonsofhomeless = new ArrayList<String>(); // -//-
        clientChronicDisease = new ArrayList<String>(); // -//-
    }

    public void reload() throws SQLException {
        //Actual client may be already set using search. But, for compatibility this method is saved
        //Setting actual client id to session for using in another applications
        HttpSession session = Util.getSession();
        if (client != null) {

            //setClient(client); //Setting client for the runtime

            log.info("Opening client with id = " + client.getId() + " (" + client.toString() + ")");
            this.mainPanelVisibility = "display: block;"; //Show main panel when the first client is attached

            File profile = new File(Configuration.profilesDir + "/" + client.getId());
            if (!profile.exists()) {
                profile.mkdirs();
            }




            reloadAll();
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.update("select_document");
            rc.update("m_tabview");
            rc.update("upload_photo_form");
            rc.update("real_photo_form");

            prevTabIndex = 0;
            tabIndex = 0;
        } else {
            log.error("Client is null. Nobody knows how it happens...");
        }
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


    private void toggleHomelessDateKnownStyle(boolean x) {
        homelessDateNotNull = x;
    }

    public void homelessDateIsKnown(ValueChangeEvent event) {
        toggleHomelessDateKnownStyle((Boolean) event.getNewValue());
    }

    public void reloadAll() throws SQLException {
        if (client != null) {
            log.info("Client ID = " + client.getId() + " has been selected for usage");
            Calendar cal = Calendar.getInstance();
            cal.set(1900,0,1,0,0,0);
            Date nullDate = cal.getTime();

            if (client.getHomelessdate() == null) {
                client.setHomelessdate(nullDate);
            }
            updateHomelessDate();

            if (!Util.formatDate(client.getHomelessdate()).equals(Util.formatDate(nullDate)) && !Util.formatDate(nullDate).equals("??.??.????")) {
                toggleHomelessDateKnownStyle(true);
            } else {
                toggleHomelessDateKnownStyle(false);
            }

        } else {
            log.info("Oops, but this client is not found in database...");
            return;
        }

        reloadClientReceivedServices();

        //*** CHECK ON NULL ***
        if (client.getNightstay() == null) {
            client.setNightstay(getClientService().getInstanceByCaption(NightStay.class, "Нет ответа"));
        }
        if (client.getEducation() == null) {
            client.setEducation(getClientService().getInstanceByCaption(Education.class, "Нет ответа"));
        }
        if (client.getFcom() == null) {
            client.setFcom(getClientService().getInstanceByCaption(FamilyCommunication.class, "Нет ответа"));
        }

        //set region according subregion
        if (client.getLastLiving() == null) {
            client.setLastLiving(getClientService().getInstanceById(SubRegion.class, 1));
        }
        if (client.getLastRegistration() == null) {
            client.setLastRegistration(getClientService().getInstanceById(SubRegion.class,1));
        }
        setLastLivingRegion(client.getLastLiving().getRegion());
        setLastRegistrationRegion(client.getLastRegistration().getRegion());
        //*** CHECK ON NULL ***

        //pull subregions list for actual region
        lastLivingSubRegions = new ArrayList<>();
        lastLivingSubRegions.addAll(getClientService().getSubRegionsByRegion(getLastLivingRegion()));

        lastRegistrationSubRegions = new ArrayList<>();
        lastRegistrationSubRegions.addAll(getClientService().getSubRegionsByRegion(getLastRegistrationRegion()));

        //chronic disasters
        if (client != null && client.getUniqDisease() != null && !client.getUniqDisease().trim().equals("")) {
            setAnotherChronicalDisasterChecked(true); //toggle checkbox "Another" for chronical disasters
            toggleAnotherChronicsStyle(true); //show input "Another" for chronical disasters
        } else {
            setAnotherChronicalDisasterChecked(false); //toggle checkbox "Another" for chronical disasters
            toggleAnotherChronicsStyle(false); //hide input "Another" for chronical disasters
        }

        //breadwinners
        if (client != null && client.getUniqBreadwinner() != null && !client.getUniqBreadwinner().trim().equals("")) {
            setAnotherBreadwinnerChecked(true); //toggle checkbox "Another" for breadwinners
            toggleAnotherBreadwinnerStyle(true); //show input "Another" for breadwinners

        } else {
            setAnotherBreadwinnerChecked(false); //toggle checkbox "Another" for breadwinners
            toggleAnotherBreadwinnerStyle(false); //hide input "Another" for breadwinners
        }

        //homeless reasons
        if (client != null && client.getUniqReason() != null && !client.getUniqReason().trim().equals("")) {
            setAnotherHomelessReasonChecked(true); //toggle checkbox "Another" for homeless reasons
            toggleAnotherHomelessReasonStyle(true); //show input "Another" for homeless reasons
        } else {
            setAnotherHomelessReasonChecked(false); //toggle checkbox "Another" for homeless reasons
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

        /*
        This is bufix for FireFox because it does not keep value after refreshing from cached page
        We are setting model values only for drop down lists manually (hope their value is small :-) )
         */
        log.info("Successfully loaded data for client id=" + client.getId() + ", " + client.getSurname() + " " + client.getFirstname() + " " + client.getMiddlename());
    }

    private String getClientActualStatus(Client client) {
        //Client may live in shelter, lived in shelter earlier, not lived in shelter at all or stored under calculation
        if (client == null) {
            return "";
        }
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

    public void reloadClientReceivedServices() {
        servicesList = new ArrayList<RecievedService>(getClientService().getInstancesByClientId(RecievedService.class, client));
        Collections.sort(servicesList, new RecievedServiceSortingComparator());
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

    // ****************** Working with dynamic change of "Другие" for Chronical Disasters section *******

    private void toggleAnotherChronicsStyle(boolean x) {
        if (x) {
            anotherChronicsStyle = "display:block;";
        } else {
            anotherChronicsStyle = "display:none;";
            client.setUniqDisease(null);
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

    // ****************** Working with dynamic change of "Другие" for Chronical Disasters section *******

    // ****************** Working with dynamic change of "Другие" for Breadwinner section *******

    private void toggleAnotherBreadwinnerStyle(boolean x) {
        if (x) {
            anotherBreadwinnerStyle = "display:block;";
        } else {
            anotherBreadwinnerStyle = "display:none;";
            client.setUniqBreadwinner(null);
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
            client.setUniqReason(null);
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
                cfb.reloadAll();
                log.info("Contacts for client "+client.getId()+" are saved successfully");
            } catch (Exception e) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Контакты не сохранены!", "");
                log.error("Contacts for client "+client.getId()+" are not saved!",e);
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
                HttpSession session = Util.getSession();
                String username = session.getAttribute("username").toString();

                log.info("Worker "+username+" is saving new Memo for the client "+client.toString());
                if (client.getMemo().trim().equals("")) {
                    log.warn("ATTENTION! Memo that worker "+username+" has saving is empty!!!");
                }
                getClientService().updateInstance(client);
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Примечания сохранены", "");
                cfb.reloadAll();
                log.info("Comments (Memo) for client "+client.getId()+" are saved successfully");
            } catch (Exception e) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Примечания не сохранены!", "");
                log.error("Comments (Memo) for client "+client.getId()+" are not saved!",e);
            }

            if (msg != null) {
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

        }
    }

    public String checkNameDash(String source) {
        String newNamefromSubNames = "";
        if (source.contains("-")) {
            String[] subNames = source.split("-");
            for (String s : subNames) {
                String upCase = s.toUpperCase();
                newNamefromSubNames+=upCase.substring(0, 1)+s.toLowerCase().substring(1, s.length())+"-";
            }
            if (source.charAt(0) != '-') {
                newNamefromSubNames = newNamefromSubNames.substring(0, newNamefromSubNames.length() - 1);
            }
        } else {
            newNamefromSubNames+=source.toUpperCase().substring(0, 1)+source.toLowerCase().substring(1, source.length());
        }
        //Don't allow empty values
        if (newNamefromSubNames.trim().equals("")) {
            return "--";
        } else {
            return newNamefromSubNames;
        }
    }

    public void saveClientForm() {
        FacesMessage msg;

        //only when client is already selected and not null
        //the client will be null when you just had open this application
        if (client != null) {

            HttpSession session = Util.getSession();
            String username = session.getAttribute("username").toString();
            log.info("Worker "+username+ " is trying to save a client form where client name = "+client.toString());
            if (homelessDateNotNull) {
                updateHomelessDate(selectedMonth, getSelectedYear());
            } else {
                Calendar cal = Calendar.getInstance();
                cal.set(1900,0,1,0,0,0);
                Date nullDate = cal.getTime();
                client.setHomelessdate(nullDate);
            }

                //Update Surname, FirstName and MiddleName for starting with uppercase letter
            client.setSurname(checkNameDash(client.getSurname()));
            client.setFirstname(checkNameDash(client.getFirstname()));
            client.setMiddlename(checkNameDash(client.getMiddlename()));

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
                log.info("Worker "+username+ "is finished saving client with new name = "+client.toString());
                reloadAll();
            } catch (Exception e) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Клиент не сохранен!", "");
                log.error("Cannot save client "+client.toString(),e);
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
        if (client != null) {

            ClientDocumentsBean cdb = context.getApplication().evaluateExpressionGet(context, "#{clientdocuments}", ClientDocumentsBean.class);
            ScanDocumentsBean sdb = context.getApplication().evaluateExpressionGet(context, "#{scandocuments}", ScanDocumentsBean.class);
            ClientContractsBean ccb = context.getApplication().evaluateExpressionGet(context, "#{clientcontracts}", ClientContractsBean.class);
            ClientShelterBean csb = context.getApplication().evaluateExpressionGet(context, "#{clientshelter}", ClientShelterBean.class);
            cdb.reload();
            sdb.reload();
            ccb.reload();
            csb.reload();

            //12.02.2016: Autosaving is TEMPORARY disabled
            /*
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
            */

            log.info("Reloaded all data for the selected client " + client.toString());
        }
    }

    public void tabChangeListener(TabChangeEvent event) {

        //17.02.2016: tabChangeListener is TEMPORARY disabled for investigating an issue with overwriting records
        // For the first opinion it could be related with autosaving

        //workaround for getting the current selected tab
        //it cannot be done with basic way because the tag tabView is not included into <form> tag
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        TabView tabView = (TabView) event.getComponent();
        setTabIndex(Integer.parseInt(params.get(tabView.getClientId(context) + "_tabindex")));

        //But! If user started to change tabs before he type mandatory data, we have to reject this action
        if (client.getFirstname().trim().equals("") ||
                client.getSurname().trim().equals("") ||
                client.getMiddlename().trim().equals("") ||
                client.getDate() == null) {

            if ((tabIndex > 0)) { //for avoiding cycles when we set the active tab and tablchange listener perform it again
                RequestContext rc = RequestContext.getCurrentInstance();
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", ""));
                rc.execute("forceFirstTab();");
            }

        } else {
            //17.02.2016: autosaving is TEMPORARY disabled
            refreshTabs(prevTabIndex);
        }
        prevTabIndex = tabIndex;
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
        log.info("Creating new client and adding it to the database");
        Client _client = new Client("", "", "", true, null, null, "", "");

        //Setting new values to the relations
        _client.setNightstay(getClientService().getInstanceByCaption(NightStay.class, "Нет ответа"));
        _client.setEducation(getClientService().getInstanceByCaption(Education.class, "Нет ответа"));
        _client.setFcom(getClientService().getInstanceByCaption(FamilyCommunication.class, "Нет ответа"));
        _client.setLastLiving(getClientService().getInstanceById(SubRegion.class,1));
        _client.setLastRegistration(getClientService().getInstanceById(SubRegion.class,1));
        _client.setRegDate(new Date());
        _client.setRecievedservices(new HashSet<RecievedService>());
        _client.setBreadwinners(new HashSet<Breadwinner>());
        _client.setDiseases(new HashSet<ChronicDisease>());
        _client.setReasonofhomeless(new HashSet<Reasonofhomeless>());

        getClientService().addInstance(_client);
        log.info("Client with ID=" + _client.getId() + " successfully added to the database and set to the http session");
        setClient(_client);
        reload();
    }

    public void deleteClient() {
        try {

            log.info("Client with ID = "+client.getId() + " will be deleted");
            
            log.info("Cleaning breadwinners, reason of homeless and diseases");
            client.setBreadwinners(Collections.<Breadwinner>emptySet());
            client.setReasonofhomeless(Collections.<Reasonofhomeless>emptySet());
            client.setDiseases(Collections.<ChronicDisease>emptySet());
            getClientService().updateInstance(client);
            log.info("...done!");
            
            log.info("Deleting all client's documents...");
            List<Document> documents = getClientService().getInstancesByClientId(Document.class, client.getId());
            for (Document document : documents) {
                log.info("\tDeleting document id = "+document.getId());
                getClientService().deleteInstance(document);
                log.info("\t... done!");
            }

            for (ShelterHistory shelterHistory : getClientService().getInstancesByClientId(ShelterHistory.class, client)) {
                log.info("\tDeleting shelterHistory id = " + shelterHistory.getId());
                getClientService().deleteInstance(shelterHistory);
                log.info("\t... done!");
            }
            
            for (ServContract servContract : getClientService().getInstancesByClientId(ServContract.class, client.getId())) {
                log.info("\tDeleting servContract id = " + servContract.getId());
                getClientService().deleteInstance(servContract);
                log.info("\t... done!");
            }

            client.setEducation(null);
            client.setNightstay(null);
            client.setFcom(null);

            log.info("Deleting the client itself...");
            getClientService().deleteInstance(client);
            log.info("Client with ID = "+client.getId() + " is deleted");
            client = null;
            HttpSession session = Util.getSession();
            session.removeAttribute("clientform");
            session.removeAttribute("stddoc");
            session.removeAttribute("clientshelter");
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


    /*
        REFACTORED METHODS FROM PREVIOUS BEAN
     */

    public int getHomelessYear(Date query) {
        if (query != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(query);
            return c.get(Calendar.YEAR);
        } else {
            return -1;
        }
    }

    public int getHomelessMonth(Date query) {
        if (query != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(query);
            return c.get(Calendar.MONTH);
        } else {
            return -1;
        }
    }

    /**
     * This method update custom homeless date for the main client's form
     */
    public void updateHomelessDate() {
        if (client!=null) {
            setSelectedMonth(getHomelessMonth(client.getHomelessdate()));
            setSelectedYear(getHomelessYear(client.getHomelessdate()));
        }
    }

    /**
     * Just set actual homeless date to the database according data in the main client's form
     * @param month
     * @param year
     */
    public void updateHomelessDate(int month, int year) {
        if (client == null) {
            return;
        }
        int m = 0;
        int y = 0;

        Calendar orig = GregorianCalendar.getInstance();
        if (client.getHomelessdate()!=null) {
            orig.setTime(client.getHomelessdate());
        } else {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            try {
                orig.setTime(df.parse("01.01.1900 00:00:00"));
            } catch (ParseException e) {
                log.error("Date parse exception",e);
            }
        }

        if (month == 0) {
            //set only year
            y = year;
            m = orig.get(Calendar.MONTH);
        } else if (year == 0) {
            //set only month
            m = month;
            y = orig.get(Calendar.YEAR);
        } else {
            m = orig.get(Calendar.MONTH);
            y = orig.get(Calendar.YEAR);
        }

        Calendar c = GregorianCalendar.getInstance();
        c.set(year, month, 1, 0, 0, 0);
        // setting updated value to the client
        client.setHomelessdate(c.getTime());
    }

    public StreamedContent getClientFormRealPhoto() throws IOException {
        if (client == null || client.getPhotoName() == null) {
            return null;
        }
        File resultFile = new File(getOriginalPhotoFilePath());
        if (!resultFile.exists() || client.getPhotoCheckSum().trim().equals("") || client.getPhotoName().trim().equals("")) {
            return null;
        } else {
            StreamedContent sc = Util.loadResizedPhotoFromDisk(resultFile);
            return sc;
        }
    }

    public StreamedContent getClientFormAvatar() {
        if (client == null) {
            return null;
        }
        InputStream imageInByteArray = null;
        if ( client.getAvatar() != null) {
            try {
                imageInByteArray = client.getAvatar().getBinaryStream();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {

            BufferedImage bi = new BufferedImage(177, 144, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = bi.createGraphics();
            Stroke drawingStroke = new BasicStroke(3);
            Rectangle2D rect = new Rectangle2D.Double(0, 0, 177, 144);

            g.setStroke(drawingStroke);
            g.draw(rect);
            g.setPaint(Color.LIGHT_GRAY);
            g.fill(rect);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(bi, "png", baos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                baos.flush();
                imageInByteArray = new ByteArrayInputStream(baos.toByteArray());
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new DefaultStreamedContent(imageInByteArray, "image/png");
    }


    public String getOriginalPhotoFilePath() {
        if (client.getPhotoName() == null) {
            return null;
        }
        File f = Paths.get(Configuration.photos, client.getPhotoName()).toFile();
        if (!f.exists()) {
            f = Paths.get(Configuration.profilesDir, String.valueOf(client.getId()),
                    client.getPhotoName()).toFile();
        }
        String path = f.getAbsolutePath();
        return f.exists() ? path : String.format("%s не найден в хранилище.", path);
    }

    public String getFormattedDate() {
        if (client != null && client.getDate() != null) {
            return Util.formatDate(client.getDate());
        } else {
            return "";
        }
    }

    //Shared Validators
    public void validateTextOnly(FacesContext ctx, UIComponent component, Object value) {
        Util.validateTextOnly(ctx, component, value);
    }

    public void validateBirthDateFormat(FacesContext ctx, UIComponent component, Object value) {
        Date result = Util.validateDateFormat(ctx, component, value);
        if (result != null && client != null) {
            client.setDate(result);
        }
    }

    public boolean isYearValid(String str) {
        Pattern pattern = Pattern.compile("\\d{4}");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

     /*
        *****************************************************
     */

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

    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
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


    public Client getClient() {
         return client;
     }

    public void setClient(Client client) {
        this.client = client;
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

    public void setClientChronicDisease(List<String> s) {
        this.clientChronicDisease = s;
    }

    public List<RecievedService> getServicesList() {
        return servicesList;
    }

    public void setServicesList(List<RecievedService> servicesList) {
        this.servicesList = servicesList;
    }

    public int getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(int selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public int getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
    }

    public void setOriginalPhotoFilePath(String originalPhotoFilePath) {
        this.originalPhotoFilePath = originalPhotoFilePath;
    }

    public void setClientFormAvatar(StreamedContent clientFormAvatar) {
        this.clientFormAvatar = clientFormAvatar;
    }

    public void setClientFormRealPhoto(StreamedContent clientFormRealPhoto) {
        this.clientFormRealPhoto = clientFormRealPhoto;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }


    public boolean isHomelessDateNotNull() {
        return homelessDateNotNull;
    }

    public void setHomelessDateNotNull(boolean homelessDateNotNull) {
        this.homelessDateNotNull = homelessDateNotNull;
    }



}
