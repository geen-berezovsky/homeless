package ru.homeless.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ChronicDisease")
public class ChronicDisease implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String caption;
	
	public ChronicDisease() {
		
	}
	
	public ChronicDisease(String caption) {
		setCaption(caption);
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

	@Override
	public String toString() {
		return caption;
	}

}
