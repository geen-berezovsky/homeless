package ru.homeless.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class RecievedService implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private Worker worker;
	private ServicesType serviceType;
	private Date date;
	private Client client;
	
	public RecievedService() {
		
	}
	
	public RecievedService(Worker worker, Client client, ServicesType serviceType, Date date) {
		setWorker(worker);
		setClient(client);
		setServiceType(serviceType);
		setDate(date);
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne (targetEntity = Worker.class)
	@JoinColumn(name = "worker")
	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	@ManyToOne (targetEntity = ServicesType.class)
	@JoinColumn(name = "servicesType")
	public ServicesType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServicesType serviceType) {
		this.serviceType = serviceType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@ManyToOne(targetEntity = Client.class)
	@JoinColumn (name = "client")
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	
	
}
