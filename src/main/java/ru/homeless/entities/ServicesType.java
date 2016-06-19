package ru.homeless.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class ServicesType implements Serializable {
	
    private static final long serialVersionUID = 2L;
    private int id;
    private String caption;
    private Boolean money;
    private Boolean document;
    private int sort_order;

    @Basic
    @Column(columnDefinition = "BIT")
    public Boolean getMoney() {
        return money;
    }

    public void setMoney(Boolean money) {
        this.money = money;
    }
	
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

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
    }

    public int getSort_order() {
        return sort_order;
    }

    @Override
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


    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (! (o instanceof ServicesType)) {
            return false;
        }
        ServicesType d = (ServicesType) o;
        return this.getId() == d.getId();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.id;
        return hash;
    }

}
