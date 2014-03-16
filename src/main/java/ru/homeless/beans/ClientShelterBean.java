package ru.homeless.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import ru.homeless.dao.ClientShelterDAO;
import ru.homeless.entities.ShelterHistory;
import ru.homeless.util.Util;

@ManagedBean (name = "clientshelter")
@SessionScoped
public class ClientShelterBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ClientShelterBean.class);
	private int cid = 0;
	private List<ShelterHistory> shelterList = null;
	private ShelterHistory selectedShelter;
	
	public ClientShelterBean() {
		
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
			setShelterList(new ClientShelterDAO().getAllClientShelters(cid));
		}
		newSelectedShelter(); // set new shelter
	}
	
	
	public void deleteShelter() {
		ClientShelterDAO cd = new ClientShelterDAO();
		cd.deleteShelter(cd.getShelterById(selectedShelter.getId()));
		reload();
	}

	public void editShelter() {
		selectedShelter = new ClientShelterDAO().getShelterById(selectedShelter.getId());
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update("add_shelter");	//force updating the add shelter form	
	}

	
	public void newSelectedShelter() {
		selectedShelter = new ShelterHistory();
	}

	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}

	public ShelterHistory getSelectedShelter() {
		return selectedShelter;
	}

	public void setSelectedShelter(ShelterHistory selectedShelter) {
		this.selectedShelter = selectedShelter;
	}

	public List<ShelterHistory> getShelterList() {
		return shelterList;
	}

	public void setShelterList(List<ShelterHistory> shelterList) {
		this.shelterList = shelterList;
	}
}
