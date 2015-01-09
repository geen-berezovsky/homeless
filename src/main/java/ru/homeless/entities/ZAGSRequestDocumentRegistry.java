package ru.homeless.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by maxim on 28.06.14.
 */
@Entity
@Table(name = "ZAGSRequestDocumentRegistry")
public class ZAGSRequestDocumentRegistry implements Serializable {

    private Integer id;

    private Integer client;

    @Lob
    private String forWhom;

    @Lob
    private String name;

    @Lob
    private String whereWasBorn;

    @Lob
    private String address;

    @Lob
    private String mother;

    @Lob
    private String father;

    private Date date;

    private Integer performerId;

    public ZAGSRequestDocumentRegistry(Integer client, String forWhom, String name, String whereWasBorn, String address, String mother, String father, Date date, Integer performerId) {
        this.client = client;
        this.forWhom = forWhom;
        this.name = name;
        this.whereWasBorn = whereWasBorn;
        this.address = address;
        this.mother = mother;
        this.father = father;
        this.date = date;
        this.performerId = performerId;
    }


    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public ZAGSRequestDocumentRegistry() {

    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public String getForWhom() {
        return forWhom;
    }

    public void setForWhom(String forWhom) {
        this.forWhom = forWhom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWhereWasBorn() {
        return whereWasBorn;
    }

    public void setWhereWasBorn(String whereWasBorn) {
        this.whereWasBorn = whereWasBorn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public Integer getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Integer performerId) {
        this.performerId = performerId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
