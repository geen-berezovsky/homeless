package ru.homeless.beans;

import java.io.Serializable;
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

import ru.homeless.entities.ContractResult;
import ru.homeless.entities.Document;
import ru.homeless.entities.ServContract;
import ru.homeless.services.GenericService;
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
	
	@ManagedProperty(value = "#{GenericService}")
	private GenericService genericService;
	
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

		if (cids != null && !cids.trim().equals("")) {
			this.cid = Integer.parseInt(cids);
			contractsList = getGenericService().getInstancesByClientId(ServContract.class, cid);
		}
		newSelectedContract(); // set new contact
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update("conlistId");
		for (ServContract s : contractsList) {
			log.info(s.getResult().getCaption());
		}
	}
	
	
	public void deleteContract() {
		getGenericService().deleteInstance(getGenericService().getInstanceById(ServContract.class, selectedContract.getId()));
		reload();
	}

	public void editContract() {
		selectedContract = getGenericService().getInstanceById(ServContract.class, selectedContract.getId());
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update("add_contract");	//force updating the add contract form	
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
		for (ContractResult c : getGenericService().getInstances(ContractResult.class)) {
			if (c.getCaption().startsWith("Выполнен ")) {
				log.info("\t"+c.getCaption());
			}
		}
		List<ServContract> contracts = getGenericService().getInstancesByClientId(ServContract.class, cid);
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

	public void newSelectedContract() {
		selectedContract = new ServContract();
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

	public GenericService getGenericService() {
		return genericService;
	}

	public void setGenericService(GenericService genericService) {
		this.genericService = genericService;
	}  
	

}
