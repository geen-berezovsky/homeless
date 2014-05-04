package ru.homeless.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import ru.homeless.converters.ContractPointsTypeConverter;
import ru.homeless.converters.ContractResultTypeConverter;
import ru.homeless.entities.ContractControl;
import ru.homeless.entities.ContractPoints;
import ru.homeless.entities.ContractResult;
import ru.homeless.entities.Document;
import ru.homeless.entities.ServContract;
import ru.homeless.entities.Worker;
import ru.homeless.services.ContractControlService;
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
	private List<ContractResult> contractResultTypes;
	// TO BE REFACTORED IN LATEST FUTURE: INCORRECT MODEL SPECIFICATION
	private List<ContractControl> contractItems;
	private List<ContractPoints> contractPointsItems;
	private ContractControl selectedContractControl;
	private TimeZone timeZone; 

	@ManagedProperty(value = "#{ContractControlService}")
	private ContractControlService contractControlService;

	public ClientContractsBean() {
		timeZone = TimeZone.getDefault();
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

		if (cids != null && !cids.trim().equals("")) {
			this.cid = Integer.parseInt(cids);
			contractsList = getContractControlService().getInstancesByClientId(ServContract.class, cid);
		}
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update("conlistId");
	}

	public void deleteContract() {
		getContractControlService().deleteInstance(getContractControlService().getInstanceById(ServContract.class, selectedContract.getId()));
		reload();
	}

	public void editContract() {
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update("edit_contract"); // next form should be updated immediatelly
									// and manually!
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
		for (ContractResult c : getContractControlService().getInstances(ContractResult.class)) {
			if (c.getCaption().startsWith("Выполнен ")) {
				log.info("\t" + c.getCaption());
			}
		}
		List<ServContract> contracts = getContractControlService().getInstancesByClientId(ServContract.class, cid);
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
		return contractsList;
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

	public List<ContractControl> getContractItems() {
		return getContractControlService().getItemsByServContractId(selectedContract.getId());
	}

	public void setContractItems(List<ContractControl> contractItems) {
		this.contractItems = contractItems;
	}

	public ContractControlService getContractControlService() {
		return contractControlService;
	}

	public void setContractControlService(ContractControlService contractControlService) {
		this.contractControlService = contractControlService;
	}

	public ContractControl getSelectedContractControl() {
		return selectedContractControl;
	}

	public void setSelectedContractControl(ContractControl selectedContractControl) {
		this.selectedContractControl = selectedContractControl;
	}

	public void updateSelectedContract() {
		getContractControlService().updateInstance(selectedContract);
		reload();
	}

	public List<ContractResult> getContractResultTypes() {
		return contractResultTypes;
	}

	public void setContractResultTypes(List<ContractResult> contractResultTypes) {
		this.contractResultTypes = contractResultTypes;
	}

	@PostConstruct
	// special for converter!
	public void init() {
		contractResultTypes = new ArrayList<ContractResult>();
		contractResultTypes.addAll(getContractControlService().getInstances(ContractResult.class));
		ContractResultTypeConverter.contractResultTypesDB = new ArrayList<ContractResult>();
		ContractResultTypeConverter.contractResultTypesDB.addAll(contractResultTypes);

		contractPointsItems = new ArrayList<ContractPoints>();
		contractPointsItems.addAll(getContractControlService().getInstances(ContractPoints.class));
		ContractPointsTypeConverter.contractPointsTypesDB = new ArrayList<ContractPoints>();
		ContractPointsTypeConverter.contractPointsTypesDB.addAll(contractPointsItems);
	}

	public void editServicePlanItem() {
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.execute("contractItemsListWv.unselectAllRows()");
		rc.update("add_seriveplanitem"); // next form should be updated
											// immediatelly and manually!
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
			getContractControlService().addInstance(selectedContractControl);
		} else {
			getContractControlService().updateInstance(selectedContractControl);	
		}
	}
	
	public void deleteServicePlanItem() {
		getContractControlService().deleteInstance(selectedContractControl);
		reload();
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
}
