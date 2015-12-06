package ru.homeless.entities;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Region implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String caption;

	public Region() {

	}

	public Region(String caption) {
		setCaption(caption);
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public String toString() {
		return caption;
	}


}
