package ru.homeless.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import ru.homeless.entities.ContractControl;
import ru.homeless.entities.ContractResult;
import ru.homeless.entities.Document;
import ru.homeless.entities.ServContract;
import ru.homeless.entities.Worker;
import ru.homeless.services.ContractControlService;
import ru.homeless.util.Util;

@ManagedBean (name = "clientcontracts")
@SessionScoped
public class ClientContractsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ClientContractsBean.class);
	private int cid = 0;
	private List<ServContract> contractsList = null;
	private ServContract selectedContract;
	private Document selectedDocument;
	private Worker worker;
	private List<String> contractResultTypes;
	//TO BE REFACTORED IN LATEST FUTURE: INCORRECT MODEL SPECIFICATION
	private List<ContractControl> contractItems;
	private ContractControl selectedContractControl;
	
	@ManagedProperty(value = "#{ContractControlService}")
	private ContractControlService contractControlService;
	
	public ClientContractsBean() {
		
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
		selectedContract = getContractControlService().getInstanceById(ServContract.class, selectedContract.getId());
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update("edit_contract");	//force updating the add contract form	
	}

	public void showAddContractDialog() {
		RequestContext rc = RequestContext.getCurrentInstance();
		if (isClientHasNoOpenedContracts()) {
			rc.execute("selectDocumentWv.show()");	
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Невозможно добавить новый договор, пока не закрыт существующий!","Пожалуйста, закройте существующий открытый договор с этим клиентом сначала.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
		
	}
	
	private boolean isClientHasNoOpenedContracts() {
		log.info("Testing available contract results (possible encoding issues): ");
		for (ContractResult c : getContractControlService().getInstances(ContractResult.class)) {
			if (c.getCaption().startsWith("Выполнен ")) {
				log.info("\t"+c.getCaption());
			}
		}
		List<ServContract> contracts = getContractControlService().getInstancesByClientId(ServContract.class, cid);
		for (ServContract s : contracts) {
			String result = s.getResult().getCaption();
			log.info("Testing contract results for client "+cid+": ");
			log.info("\t"+result);
			if (! result.startsWith("Выполнен ")) {
				log.info("\t"+"Found, at least, one uncompleted contract");
				return false; //there is at least one unclosed contract
			}
		}
		log.info("No uncompleted contracts found");
		return true; //all contracts are completed partially or fully
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

	public List<String> getContractResultTypes() {
		List<String> l = new ArrayList<String>();
		for (ContractResult e : getContractControlService().getInstances(ContractResult.class)) {
			l.add(e.getCaption());
		}
		return l;
	}

	public void setContractResultTypes(List<String> contractResultTypes) {
		this.contractResultTypes = contractResultTypes;
	}

	public List<ContractControl> getContractItems() {
		log.info(selectedContract.toString());
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

}
