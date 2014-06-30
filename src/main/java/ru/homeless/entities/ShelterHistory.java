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
public class ShelterHistory implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int client;
	private Date inShelter;
	private Date outShelter;
	private Date hepotitsVac;
	private Date dipthVac;
	private Date typhVac;
	private Date fluorogr;
	private int roomId;

    public Integer getShelterresult() {
        return shelterresult;
    }

    public void setShelterresult(Integer shelterresult) {
        this.shelterresult = shelterresult;
    }

    private Integer shelterresult;
	
	public ShelterHistory() {
		
	}
	
	public ShelterHistory(int client, Date inShelter, Date outShelter, int roomId, ShelterResult sr) {
		setClient(client);
		setInShelter(inShelter);
		setOutShelter(outShelter);
		setRoomId(roomId);
        setShelterresult(sr.getId());
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getClient() {
		return client;
	}
	public void setClient(int client) {
		this.client = client;
	}
	public Date getInShelter() {
		return inShelter;
	}
	public void setInShelter(Date inShelter) {
		this.inShelter = inShelter;
	}
	public Date getOutShelter() {
		return outShelter;
	}
	public void setOutShelter(Date outShelter) {
		this.outShelter = outShelter;
	}
	public Date getHepotitsVac() {
		return hepotitsVac;
	}
	public void setHepotitsVac(Date hepotitsVac) {
		this.hepotitsVac = hepotitsVac;
	}
	public Date getDipthVac() {
		return dipthVac;
	}
	public void setDipthVac(Date dipthVac) {
		this.dipthVac = dipthVac;
	}
	public Date getTyphVac() {
		return typhVac;
	}
	public void setTyphVac(Date typhVac) {
		this.typhVac = typhVac;
	}
	public Date getFluorogr() {
		return fluorogr;
	}
	public void setFluorogr(Date fluorogr) {
		this.fluorogr = fluorogr;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
/*
	*/

}
