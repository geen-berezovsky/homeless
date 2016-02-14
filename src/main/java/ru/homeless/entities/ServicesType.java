package ru.homeless.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class ServicesType implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String caption;

    @Basic
    @Column(columnDefinition = "BIT")
    public Boolean getMoney() {
        return money;
    }

    public void setMoney(Boolean money) {
        this.money = money;
    }

    private Boolean money;

    private Boolean document;
	
	public ServicesType() {
		
	}
	
	public ServicesType(String caption) {
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
	
	public String toString() {
		return caption;
	}

    @Basic
    @Column(columnDefinition = "BIT")
    public Boolean getDocument() {
        return document;
    }

    public void setDocument(Boolean document) {
        this.document = document;
    }


    public boolean equals(Object o) {
        if (o == null) return false;
        if (! (o instanceof ServicesType)) {
            return false;
        }
        ServicesType d = (ServicesType) o;
        if (new Integer(getId()).equals(new Integer(d.getId()))) {
            return true;
        } else {
            return false;
        }
    }

}
