package ru.homeless.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.swing.JCheckBox;

import ru.homeless.interfaces.ICheckBoxAction;

@Entity
@Table(name = "Breadwinner")
public class Breadwinner implements ICheckBoxAction, Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String caption;
	//private Set<Client> clients;
	private int audience;
	
	public Breadwinner() {
		
	}
	
	public Breadwinner(String caption, int audience) {
		setCaption(caption);
		setAudience(audience);
		//clients = new HashSet<Client>();
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	/*
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "breadwinners")
	public Set<Client> getClients() {
		return clients;
	}

	public void setClients(Set<Client> clients) {
		this.clients = clients;
	}
	
	public void addClient(Client client) {
		clients.add(client);
	}
	*/

	public int getAudience() {
		return audience;
	}

	public void setAudience(int audience) {
		this.audience = audience;
	}

}
