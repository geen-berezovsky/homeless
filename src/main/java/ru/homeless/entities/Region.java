package ru.homeless.entities;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Region implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String caption;
    private String abbreviation;


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
	
    public boolean equals(Object o) {
        if (o == null) return false;
        if (! (o instanceof Region)) {
            return false;
        }
        Region d = (Region) o;
        if (new Integer(getId()).equals(new Integer(d.getId()))) {
            return true;
        } else {
            return false;
        }
    }


    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
