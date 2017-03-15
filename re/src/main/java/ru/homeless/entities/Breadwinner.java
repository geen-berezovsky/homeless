package ru.homeless.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Breadwinner")
public class Breadwinner implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String caption;
	private int audience;
	
	public Breadwinner() {
		
	}
	
	public Breadwinner(String caption, int audience) {
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

}
