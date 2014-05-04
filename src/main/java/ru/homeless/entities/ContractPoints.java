package ru.homeless.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ContractPoints implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String caption;
	private int audience;

	public ContractPoints() {
		
	}
	
	public boolean equals(Object o) {
		if (o == null) return false;
		if (! (o instanceof ContractPoints)) {
			return false;
		}
		ContractPoints d = (ContractPoints) o;
		if (getId() == d.getId()) {
			return true;
		} else {
			return false;	
		}
	}

	
	public ContractPoints(String caption, int audience) {
		setCaption(caption);
		setAudience(audience);
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
	
	public int getAudience() {
		return audience;
	}

	public void setAudience(int audience) {
		this.audience = audience;
	}
	
	@Override
	public String toString() {
		return caption;
	}


}
