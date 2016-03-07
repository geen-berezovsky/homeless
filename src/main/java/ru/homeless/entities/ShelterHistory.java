package ru.homeless.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table (name = "ShelterHistory")
public class ShelterHistory implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
	private int id;
	private Client client;
	private Date inShelter;
	private Date outShelter;
	private Date hepotitsVac;
	private Date dipthVac;
	private Date typhVac;
	private Date fluorogr;
	private Integer roomId;
    private Integer shelterresult;
    private ServContract servContract;
    private String comments;
	
	public ShelterHistory() {
		
	}
	
	public ShelterHistory(Client client, Date inShelter, Date outShelter, int roomId, Integer sr) {
		setClient(client);
		setInShelter(inShelter);
		setOutShelter(outShelter);
		setRoomId(roomId);
        setShelterresult(sr);
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

    @ManyToOne(targetEntity = Client.class)
    @JoinColumn(name = "client")
    public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
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

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

    public Integer getShelterresult() {
        return shelterresult;
    }

    public void setShelterresult(Integer shelterresult) {
        this.shelterresult = shelterresult;
    }

    @OneToOne (targetEntity = ServContract.class)
    @JoinColumn(name="servContract")
    public ServContract getServContract() {
        return servContract;
    }

    public void setServContract(ServContract servContract) {
        this.servContract = servContract;
    }
    
    public boolean equals(Object other) {
        if (this == other) return true;
        if ( !(other instanceof ShelterHistory) ) return false;

        final ShelterHistory otherSH = (ShelterHistory) other;

        return new Integer(getId()).equals(otherSH.getId());
    }

    public int hashCode() {
        return 29 * getId();
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
