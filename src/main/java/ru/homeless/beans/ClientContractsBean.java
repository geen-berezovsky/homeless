package ru.homeless.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import ru.homeless.dao.ClientContractsDAO;
import ru.homeless.dao.ClientDocumentsDAO;
import ru.homeless.entities.Document;
import ru.homeless.entities.ServContract;
import ru.homeless.util.Util;

@ManagedBean (name = "clientcontracts")
@SessionScoped
public class ClientContractsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ClientContractsBean.class);
	private int cid = 0;
	private List<ServContract> contractsList = null;
	private ServContract selectedContract;
	
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
			contractsList = new ClientContractsDAO().getAllClientContracts(cid);
		}
		newSelectedContract(); // set new contact
	}
	
	
	public void deleteContract() {
		ClientContractsDAO cd = new ClientContractsDAO(); 
		cd.deleteContract(cd.getContractById(selectedContract.getId()));
		reload();
	}

	public void editContract() {
		selectedContract = new ClientContractsDAO().getContractById(selectedContract.getId());
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update("add_contract");	//force updating the add contract form	
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


}
