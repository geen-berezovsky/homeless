package ru.homeless.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.context.RequestContext;

import ru.homeless.dao.ClientDAO;
import ru.homeless.entities.Client;
import ru.homeless.util.HibernateUtil;
import ru.homeless.util.Util;

@ManagedBean(name = "clientsearch")
@SessionScoped
public class ClientSearchBean implements Serializable {
	public static Logger log = Logger.getLogger(ClientSearchBean.class);
	private static final long serialVersionUID = 1L;
	private int id;
	private String surname;
	private String middlename;
	private String firstname;
	private String date;
	private List<Client> foundList;

	public ClientSearchBean() {
		foundList = new ArrayList<Client>();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void findClients() {
		foundList = new ClientDAO().getClientsByCriteria(id, surname, firstname, middlename, date);
		
		//clear form for next run
		setId(0);
		setSurname("");
		setFirstname("");
		setMiddlename("");
		setDate("");
	}
	
	public void showClientDetails() {
		
	}

	public String formattedDate(Date query) {
		if (query != null && ! query.toString().equalsIgnoreCase("")) {
			return Util.formatDate(query);
		} else {
			return "";
		}
	}
	
	public List<Client> getFoundList() {
		return foundList;
	}

	public void setFoundList(List<Client> foundList) {
		this.foundList = foundList;
	}
	
}
