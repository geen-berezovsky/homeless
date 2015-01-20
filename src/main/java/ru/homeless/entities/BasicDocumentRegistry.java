package ru.homeless.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by maxim on 28.06.14.
 */
@Entity
@Table(name = "BasicDocumentRegistry")
public class BasicDocumentRegistry implements Serializable {

    private Integer id;

    private BasicDocumentRegistryType type;

    private Integer client;

    private Integer documentId;

    private Date dateFrom;

    private Date dateTill;

    private Integer performerId;

    private Date date;

    public BasicDocumentRegistry(Integer client, BasicDocumentRegistryType type, Integer documentId, Date dateFrom, Date dateTill, Integer performerId,
                                 Date date) {
        this.client = client;
        this.type = type;
        this.documentId = documentId;
        this.dateFrom = dateFrom;
        this.dateTill = dateTill;
        this.performerId = performerId;
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public BasicDocumentRegistry() {

    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
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

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTill() {
        return dateTill;
    }

    public void setDateTill(Date dateTill) {
        this.dateTill = dateTill;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = BasicDocumentRegistryType.class)
    @JoinColumn(name = "type")
    public BasicDocumentRegistryType getType() {
        return type;
    }

    public void setType(BasicDocumentRegistryType type) {
        this.type = type;
    }


}
