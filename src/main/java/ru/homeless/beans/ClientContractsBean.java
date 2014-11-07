package ru.homeless.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import org.primefaces.model.StreamedContent;
import ru.homeless.configuration.Configuration;
import ru.homeless.converters.ContractPointsTypeConverter;
import ru.homeless.converters.ContractResultTypeConverter;
import ru.homeless.converters.ShelterResultConverter;
import ru.homeless.entities.*;
import ru.homeless.primefaces.model.ContractPointsDataModel;
import ru.homeless.services.GenericService;
import ru.homeless.services.WorkerService;
import ru.homeless.util.Util;

@ManagedBean(name = "clientcontracts")
@ViewScoped
public class ClientContractsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ClientContractsBean.class);
	private int cid = 0;
	private List<ServContract> contractsList = null;
	private ServContract selectedContract;
	private Document selectedDocument;
	private Worker worker;
	private Document workerDocument;
	private List<ContractResult> contractResultTypes;
    private List<ShelterResult> shelterResults;

	private List<ContractControl> contractItems;
	private List<ContractPoints> contractPointsItems;
	private ContractPointsDataModel contractPointsDataModel;
	private ContractPoints[] selectedNewContractPoints;
	private ContractControl selectedContractControl;
	private TimeZone timeZone;
	private String workerSelfData;
	private String workerPassportData;
	private String warrantData;

	@ManagedProperty(value = "#{GenericService}")
	private GenericService genericService;
	
	@ManagedProperty(value = "#{WorkerService}")
	private WorkerService workerService;

	public ClientContractsBean() {
		timeZone = TimeZone.getDefault();
	}

    public void downloadContract(int id) {
        RequestContext rc = RequestContext.getCurrentInstance();

        selectedContract = getGenericService().getInstanceById(ServContract.class, id);

        StringBuilder sb = new StringBuilder();
        sb.append("window.location.href = \"");
        sb.append(Configuration.reportEngineUrl);
        sb.append("/getGeneratedContract?");
        sb.append("requestType=100");
        sb.append("&clientId="+this.cid);
        sb.append("&contractId="+selectedContract.getId());
        sb.append("&workerId="+selectedContract.getWorker().getId());
        sb.append("\"");

        log.info("Executing "+sb.toString());

        rc.execute(sb.toString());

    }

	public String formatDate(Date q) {
		if (q != null && !q.equals("")) {
			return Util.formatDate(q);
		} else {
			return "";
		}
	}

	public void reload() {
		HttpSession session = Util.getSession();
		String cids = session.getAttribute("cid").toString();
		worker = (Worker) session.getAttribute("worker");
		
		setWorkerSelfData(worker.getRules().getCaption()+" "+worker.getSurname()+" "+worker.getFirstname()+" "+worker.getMiddlename());
		workerDocument = getWorkerDocument();
		if (workerDocument != null && workerDocument.getDoctype()!=null && workerDocument.getDocPrefix()!=null && workerDocument.getDocNum()!=null && workerDocument.getDate()!=null && workerDocument.getWhereAndWhom()!=null) {
			setWorkerPassportData(workerDocument.getDoctype().getCaption()+" "+workerDocument.getDocPrefix()+" "+workerDocument.getDocNum()+" выдан "+formatDate(workerDocument.getDate())+" "+workerDocument.getWhereAndWhom());
		} else {
			setWorkerPassportData("НЕДОСТАТОЧНО ДАННЫХ");
		}
		if (!worker.getWarrantNum().trim().equals("") && worker.getWarrantDate()!=null) {
			setWarrantData("№"+worker.getWarrantNum()+" от "+formatDate(worker.getWarrantDate()));
		} else {
			setWarrantData("НЕДОСТАТОЧНО ДАННЫХ");
		}
		
		if (cids != null && !cids.trim().equals("")) {
			this.cid = Integer.parseInt(cids);
		}

		//update service plan items for actual contract
		if (selectedContract!=null && selectedContract.getContractcontrols() != null) {
			contractItems = new ArrayList<ContractControl>(selectedContract.getContractcontrols());
		}

	}

	public void deleteContract() {
		getGenericService().deleteInstance(getGenericService().getInstanceById(ServContract.class, selectedContract.getId()));
		reload();
	}

	public void editContract() {
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update("edit_contract"); // next form should be updated immediatelly and manually!
		reload();
	}

	public void showAddContractDialog() {
		RequestContext rc = RequestContext.getCurrentInstance();
		if (isClientHasNoOpenedContracts()) {
			rc.execute("selectDocumentWv.show()");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Невозможно добавить новый договор, пока не закрыт существующий!",
					"Пожалуйста, закройте существующий открытый договор с этим клиентом сначала.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	private boolean isClientHasNoOpenedContracts() {
		log.info("Testing available contract results (possible encoding issues): ");
		for (ContractResult c : getGenericService().getInstances(ContractResult.class)) {
			if (c.getCaption().startsWith("Выполнен ")) {
				log.info("\t" + c.getCaption());
			}
		}
		List<ServContract> contracts = getGenericService().getInstancesByClientId(ServContract.class, cid);
		for (ServContract s : contracts) {
			String result = s.getResult().getCaption();
			log.info("Testing contract results for client " + cid + ": ");
			log.info("\t" + result);
			if (!result.startsWith("Выполнен ")) {
				log.info("\t" + "Found, at least, one uncompleted contract");
				return false; // there is at least one unclosed contract
			}
		}
		log.info("No uncompleted contracts found");
		return true; // all contracts are completed partially or fully
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public List<ServContract> getContractsList() {
		return getGenericService().getInstancesByClientId(ServContract.class, cid);
	}

	public void setContractsList(List<ServContract> contractsList) {
		this.contractsList = contractsList;
	}

	public ServContract getSelectedContract() {
		return selectedContract;
	}

	public void setSelectedContract(ServContract selectedContract) {
		this.selectedContract = selectedContract;
	}

	public Document getSelectedDocument() {
		return selectedDocument;
	}

	public void setSelectedDocument(Document selectedDocument) {
		this.selectedDocument = selectedDocument;
	}

	public void onRowSelect(SelectEvent event) {
		selectedDocument = (Document) event.getObject();
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public GenericService getGenericService() {
		return genericService;
	}

	public void setGenericService(GenericService contractControlService) {
		this.genericService = contractControlService;
	}

	public ContractControl getSelectedContractControl() {
		return selectedContractControl;
	}

	public void setSelectedContractControl(ContractControl selectedContractControl) {
		this.selectedContractControl = selectedContractControl;
	}

	public void updateSelectedContract() {
		getGenericService().updateInstance(selectedContract);
		reload();
	}

	public List<ContractResult> getContractResultTypes() {
		return contractResultTypes;
	}

	public void setContractResultTypes(List<ContractResult> contractResultTypes) {
		this.contractResultTypes = contractResultTypes;
	}

	@PostConstruct
	// special for converter and selection data model!
	public void init() {
		contractResultTypes = new ArrayList<ContractResult>();
		contractResultTypes.addAll(getGenericService().getInstances(ContractResult.class));
		ContractResultTypeConverter.contractResultTypesDB = new ArrayList<ContractResult>();
		ContractResultTypeConverter.contractResultTypesDB.addAll(contractResultTypes);

		contractPointsItems = new ArrayList<ContractPoints>();
		contractPointsItems.addAll(getGenericService().getInstances(ContractPoints.class));
		ContractPointsTypeConverter.contractPointsTypesDB = new ArrayList<ContractPoints>();
		ContractPointsTypeConverter.contractPointsTypesDB.addAll(contractPointsItems);

        shelterResults = new ArrayList<ShelterResult>();
        shelterResults.addAll(getGenericService().getInstances(ShelterResult.class));
        ShelterResultConverter.shelterResultList = new ArrayList<ShelterResult>();
        ShelterResultConverter.shelterResultList.addAll(shelterResults);


		
		contractPointsDataModel = new ContractPointsDataModel(contractPointsItems);
		
	}

	public void editServicePlanItem() {
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.execute("contractItemsListWv.unselectAllRows()");
		rc.update("add_seriveplanitem"); // next form should be updated
											// immediatelly and manually!
		reload();
	}

	public void resetTableSelection() {
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.execute("contractItemsListWv.unselectAllRows()");
		rc.execute("addServiceItemWv.show()");
	}

	public List<ContractPoints> getContractPointsItems() {
		return contractPointsItems;
	}

	public void setContractPointsItems(List<ContractPoints> contractPointsItems) {
		this.contractPointsItems = contractPointsItems;
	}

	public void initContractControl() {
		if (selectedContractControl == null) {
			selectedContractControl = new ContractControl();
		}
	}

	public void addServicePlanItem() {
		
		//According current table model (otherwise next code should be refactored)
		if (selectedContractControl.getServcontract() == null || selectedContractControl.getServcontract() == 0) {
			//This is new record
			selectedContractControl.setServcontract(selectedContract.getId());
			//getGenericService().addInstance(selectedContractControl);
			selectedContract.getContractcontrols().add(selectedContractControl);
		} else {
			getGenericService().updateInstance(selectedContractControl);	
		}
		
		getGenericService().updateInstance(selectedContract);
		reload();
	}
	
	public void deleteServicePlanItem() {
		selectedContract.getContractcontrols().remove(selectedContractControl);
		getGenericService().updateInstance(selectedContract);
		reload();
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public WorkerService getWorkerService() {
		return workerService;
	}

	public void setWorkerService(WorkerService workerService) {
		this.workerService = workerService;
	}

	public Document getWorkerDocument() {
		return getWorkerService().getWorkerDocumentById(worker.getId());
	}

	public void setWorkerDocument(Document workerDocument) {
		this.workerDocument = workerDocument;
	}
	
	public void resetSelectedContract() {
		selectedContract = new ServContract();
	}

	public ContractPointsDataModel getContractPointsDataModel() {
		return contractPointsDataModel;
	}

	public void setContractPointsDataModel(ContractPointsDataModel contractPointsDataModel) {
		this.contractPointsDataModel = contractPointsDataModel;
	}

	public void validateStartDateFormat(FacesContext ctx, UIComponent component, Object value) {
		log.info("Val start called");
		if (value==null || value.toString().trim().equals("")) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Пожалуйста введите дату начала действия контракта", "Формат даты: ДД.ММ.ГГГГ");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			throw new ValidatorException(msg);
		}
		Date result = Util.validateDateFormat(ctx, component, value);
		if (result != null) {
			selectedContract.setStartDate(result);
		}
	}

	public void validateStopDateFormat(FacesContext ctx, UIComponent component, Object value) {
		log.info("Val stop called");
		if (value==null || value.toString().trim().equals("")) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Пожалуйста введите дату окончания действия контракта", "Формат даты: ДД.ММ.ГГГГ");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			throw new ValidatorException(msg);
		}
		Date result = Util.validateDateFormat(ctx, component, value);
		if (result != null) {
			selectedContract.setStartDate(result);
		}
	}

	public void validateWorkerPasportDataOnly(FacesContext ctx, UIComponent component, Object value) {
		String str = value.toString();
		if (str.length() < 20) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Некорректны паспортные данные работника", "Пожалуйста, сначала добавьте собственные паспортные данные через Настройки->Мои Настройки!");
			throw new ValidatorException(msg);
		}
	}

	public void validateWorkerWarrantDataOnly(FacesContext ctx, UIComponent component, Object value) {
		String str = value.toString();
		if (str.contains("ЗАПОЛНИТЬ") || str.contains("НЕДОСТАТОЧНО")) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Некорректный номер доверенности работника", "Пожалуйста, сначала добавьте информацию о доверенности и ее дате в свой профиль через Настройки->Мои Настройки!");
			throw new ValidatorException(msg);
		}
	}

	public void addNewContract() {
		
		if (selectedNewContractPoints.length ==0) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Не выбран ни один пункт сервисного плана", "Пожалуйста, выберите хотя бы один пункт сервисного плана!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			//for each contract point we need add different ContractControl objects related to this contract
			selectedContract.setContractcontrols(new HashSet<ContractControl>());
			for (ContractPoints c : getSelectedNewContractPoints()) {
				selectedContract.getContractcontrols().add(new ContractControl(selectedContract.getId(), c));
			}
			//then add new ServContract
			selectedContract.setClient(cid);
			selectedContract.setCommentResult("");
			selectedContract.setDocumentId(selectedDocument.getId());
			selectedContract.setResult(getGenericService().getInstanceById(ContractResult.class, 1));
			selectedContract.setWorker(worker);
			
			getGenericService().addInstance(selectedContract);
			
			RequestContext rc = RequestContext.getCurrentInstance();
			rc.update("conlistId");
			rc.execute("addContractWv.hide()");
			reload();

		}
	}

	public String getWorkerSelfData() {
		return workerSelfData;
	}

	public void setWorkerSelfData(String workerSelfData) {
		this.workerSelfData = workerSelfData;
	}

	public String getWorkerPassportData() {
		return workerPassportData;
	}

	public void setWorkerPassportData(String workerPassportData) {
		this.workerPassportData = workerPassportData;
	}

	public String getWarrantData() {
		return warrantData;
	}

	public void setWarrantData(String warrantData) {
		this.warrantData = warrantData;
	}

	public ContractPoints[] getSelectedNewContractPoints() {
		return selectedNewContractPoints;
	}

	public void setSelectedNewContractPoints(ContractPoints[] selectedNewContractPoints) {
		this.selectedNewContractPoints = selectedNewContractPoints;
	}

	public List<ContractControl> getContractItems() {
		return contractItems;
	}

	public void setContractItems(List<ContractControl> contractItems) {
		this.contractItems = contractItems;
	}
}

