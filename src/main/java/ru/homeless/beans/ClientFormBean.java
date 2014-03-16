package ru.homeless.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

import ru.homeless.comparators.RecievedServiceSortingComparator;
import ru.homeless.dao.BreadwinnerDAO;
import ru.homeless.dao.ChronicDiseaseDAO;
import ru.homeless.dao.ClientDAO;
import ru.homeless.dao.EductionDAO;
import ru.homeless.dao.FamilyCommunicationDAO;
import ru.homeless.dao.NightStayDAO;
import ru.homeless.dao.ReasonOfHomelessDAO;
import ru.homeless.entities.Breadwinner;
import ru.homeless.entities.ChronicDisease;
import ru.homeless.entities.Client;
import ru.homeless.entities.Education;
import ru.homeless.entities.FamilyCommunication;
import ru.homeless.entities.NightStay;
import ru.homeless.entities.Reasonofhomeless;
import ru.homeless.entities.RecievedService;
import ru.homeless.util.Util;

@ManagedBean(name = "clientform")
@SessionScoped
public class ClientFormBean extends ClientDataBean implements Serializable {

	public static Logger log = Logger.getLogger(ClientFormBean.class);
	private static final long serialVersionUID = 1L;
	private int cid;


	private Client client;
	private List<RecievedService> servicesList;
	private List<String> clientChronicDisease; //fake
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
	private List<String> clientReasonsofhomeless = null;
	private List<String> chronicDiseaseTypes = null;
	private List<String> clientBreadwinners;

	private List<String> educationTypes;
	private List<String> nightStayTypes;
	private List<String> familyCommunicationTypes;
	private List<String> breadwinnerTypes;

	public ClientFormBean() {
		this.mainPanelVisibility = "display: none;"; 
	}

	public void reloadAll(int id) throws SQLException {
		this.cid = id;
		this.mainPanelVisibility = "display: block;";
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.execute("reload();");
		

		reloadAll();
	}

	public void reloadAll() throws SQLException {
		reloadClientData();
		reloadClientReceivedServices();
		//chronic disasters
		if (client !=null && client.getUniqDisease()!=null && !client.getUniqDisease().trim().equals("")) {
			setAnotherChronicalDisasterChecked(true); //toggle checbox "Another" for chronical disasters 
			toggleAnotherChronicsStyle(true); //show input "Another" for chronical disasters
		} else {
			setAnotherChronicalDisasterChecked(false); //toggle checbox "Another" for chronical disasters
			toggleAnotherChronicsStyle(false); //hide input "Another" for chronical disasters
		}

		//breadwinners		
		if (client !=null && client.getUniqBreadwinner()!=null && !client.getUniqBreadwinner().trim().equals("")) {
			setAnotherBreadwinnerChecked(true); //toggle checbox "Another" for breadwinners
			toggleAnotherBreadwinnerStyle(true); //show input "Another" for breadwinners

		} else {
			setAnotherBreadwinnerChecked(false); //toggle checbox "Another" for breadwinners
			toggleAnotherBreadwinnerStyle(false); //hide input "Another" for breadwinners
		}
		
		//homeless reasons		
		if (client !=null && client.getUniqReason()!=null && !client.getUniqReason().trim().equals("")) {
			setAnotherHomelessReasonChecked(true); //toggle checbox "Another" for homeless reasons
			toggleAnotherHomelessReasonStyle(true); //show input "Another" for homeless reasons
		} else {
			setAnotherHomelessReasonChecked(false); //toggle checbox "Another" for homeless reasons
			toggleAnotherHomelessReasonStyle(false); //hide input "Another" for homeless reasons
		}
		
		//set actual client id to session for using in another applications
		HttpSession session = Util.getSession();
		session.setAttribute("cid", cid);

		
		log.info("Successfully loaded data for client id=" + this.cid + ", " + client.getSurname() + " " + client.getFirstname() + " " + client.getMiddlename());
	}

	
	public void reloadClientData() {
		setClient(new ClientDAO().getClientById(getCid()));
		//copy data to externalized class data
		log.info("Client ID = "+client.getId()+" has been selected for usage");
		copyClientToClientData(client);
		//setClientFormAvatar(clientFormAvatar);
		//setAvatar(getClientFormAvatar());
		updateHomelessDate();
	}

	public void updateHomelessDate() {
		setSelectedMonth(getHomelessMonth(client.getHomelessdate()));
		setSelectedYear(getHomelessYear(client.getHomelessdate()));
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

	public List<String> getEducationTypes() {
		EductionDAO eDAO = new EductionDAO();
		List<String> l = new ArrayList<String>();
		for (Education e : eDAO.getAllEducationTypes()) {
			l.add(e.getCaption());
		}
		return l;
	}

	public List<String> getNightStayTypes() {
		NightStayDAO nsDAO = new NightStayDAO();
		List<String> l = new ArrayList<String>();
		for (NightStay ns : nsDAO.getAllNightStayTypes()) {
			l.add(ns.getCaption());
		}
		return l;
	}

	public List<String> getFamilyCommunicationTypes() {
		FamilyCommunicationDAO fDAO = new FamilyCommunicationDAO();
		List<String> l = new ArrayList<String>();
		for (FamilyCommunication f : fDAO.getAllFamilyCommunicationTypes()) {
			l.add(f.getCaption());
		}
		return l;
	}

	/*
	 * Get all Breadwinners from the database
	 */
	
	public List<String> getBreadwinnerTypes() {
		BreadwinnerDAO bDAO = new BreadwinnerDAO();
		List<String> l = new ArrayList<String>();
		for (Breadwinner b : bDAO.getAllBreadwinnerTypes()) {
			if (! b.getCaption().equals("Другие")) {
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
		ReasonOfHomelessDAO rDAO = new ReasonOfHomelessDAO();
		List<String> l = new ArrayList<String>();
		for (Reasonofhomeless r : rDAO.getAllReasonofhomelessTypes()) {
			if (! r.getCaption().equals("Другие")) {
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
		ChronicDiseaseDAO rDAO = new ChronicDiseaseDAO();
		List<String> l = new ArrayList<String>();
		for (ChronicDisease cd : rDAO.getAllChronicDiseaseTypes()) {
			if (! cd.getCaption().equals("Другие")) {
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
	
	private void toggleAnotherChronicsStyle (boolean x) {
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
	
	private void toggleAnotherBreadwinnerStyle (boolean x) {
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

	private void toggleAnotherHomelessReasonStyle (boolean x) {
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

		//update "homeless till the moment" (don't move it after client data copying!)
		updateHomelessDate(selectedMonth);

		client = copyClientDataToClient(client);
		
		//Update Surname, FirstName and MiddleName for starting with uppercase letter
		String fl = client.getSurname().toUpperCase().substring(0,1);
		String ll = client.getSurname().toLowerCase().substring(1,client.getSurname().length());
		client.setSurname(fl+ll);
		fl = client.getFirstname().toUpperCase().substring(0,1);
		ll = client.getFirstname().toLowerCase().substring(1,client.getFirstname().length());
		client.setFirstname(fl+ll);
		fl = client.getMiddlename().toUpperCase().substring(0,1);
		ll = client.getMiddlename().toLowerCase().substring(1,client.getMiddlename().length());
		client.setMiddlename(fl+ll);
		

		//update homeless reasons, breadwinners, chronical disasters
		Set<Breadwinner> sb = new HashSet<Breadwinner>();
		for (Breadwinner b : new BreadwinnerDAO().getAllBreadwinnerTypes()) {
			for (String cb : clientBreadwinners) {
				if (b.getCaption().equals(cb)) {
					sb.add(b);
				}
			}
		}
		client.setBreadwinners(sb);

		Set<Reasonofhomeless> sr = new HashSet<Reasonofhomeless>();
		for (Reasonofhomeless b : new ReasonOfHomelessDAO().getAllReasonofhomelessTypes()) {
			for (String cb : clientReasonsofhomeless) {
				if (b.getCaption().equals(cb)) {
					sr.add(b);
				}
			}
		}
		client.setReasonofhomeless(sr);
		
		Set<ChronicDisease> chd = new HashSet<ChronicDisease>();
		for (ChronicDisease b : new ChronicDiseaseDAO().getAllChronicDiseaseTypes()) {
			for (String cb : clientChronicDisease) {
				if (b.getCaption().equals(cb)) {
					chd.add(b);
				}
			}
		}
		client.setDiseases(chd);
		

		
		ClientDAO cd = new ClientDAO();
		Map<Boolean, String> res = cd.updateClientData(client);
		if (res.get(true)!=null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Сохранено", res.get(true));	
		} else {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Клиент не сохранен!", res.get(false));
			
		}
		try {
			cfb.reloadAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public boolean isDateValid(String str) {
		Pattern pattern = Pattern.compile("\\d{2}.\\d{2}.\\d{4}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			try {
				new SimpleDateFormat("dd.MM.yyyy").parse(str);
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
	
	public boolean isYearValid(String str) {
		Pattern pattern = Pattern.compile("\\d{4}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isTextOnlyValid(String str) {
		Pattern pattern = Pattern.compile("[a-zA-Z-]+|[а-яА-Я-]+");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public void validateTextOnly(FacesContext ctx, UIComponent component, Object value) {
		String str = value.toString();
		if (! isTextOnlyValid(str)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ФИО не может содержать цифры, спецсимволы и пробелы!\nТолько русский или латинский текст, а также тире.","Пожалуйста, проверьте форму!");
			throw new ValidatorException(msg);
		}
	}
	
	public void validateHomelessYear(FacesContext ctx, UIComponent component, Object value) {
		String str = value.toString();
		if (! isYearValid(str)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Неправильно указан год в поле <Бездомный с момента>!","Используйте формат гггг");
			throw new ValidatorException(msg);
		} else {
			updateHomelessDate(Integer.parseInt(str));
		}
	}
	
	public void validateBirthDateFormat(FacesContext ctx, UIComponent component, Object value) {
		String str = value.toString();
		if (! isDateValid(str)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Некорректный формат даты рождения!","Используйте дд.мм.гггг");
			throw new ValidatorException(msg);
		} else {
			try {
				Date d = new SimpleDateFormat("dd.MM.yyyy").parse(str);
				setDate(d);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateHomelessDate(int newValue) {
		int month = 0;
		int year = 0;
		Calendar orig = GregorianCalendar.getInstance();
		orig.setTime(client.getHomelessdate());
		if (newValue < 13) { // it is monthId
			month = newValue;
			year = orig.get(Calendar.YEAR);
		} else {
			year = newValue;
			month = orig.get(Calendar.MONTH);
		}
		Calendar c = GregorianCalendar.getInstance();
		c.set(year, month, 1, 0, 0, 0);
		// setting updated value to the client
		setHomelessdate(c.getTime());
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

	public void tabChangeListener(TabChangeEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		ClientDocumentsBean cdb = context.getApplication().evaluateExpressionGet(context, "#{clientdocuments}", ClientDocumentsBean.class);
		ClientContractsBean ccb = context.getApplication().evaluateExpressionGet(context, "#{clientcontracts}", ClientContractsBean.class);
		ClientShelterBean csb = context.getApplication().evaluateExpressionGet(context, "#{clientshelter}", ClientShelterBean.class);
		cdb.reload();
		ccb.reload();
		csb.reload();
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
		log.info("Reload called!");
	}
	
}
