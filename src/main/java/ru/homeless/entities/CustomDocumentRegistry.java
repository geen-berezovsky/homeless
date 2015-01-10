package ru.homeless.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by maxim on 28.06.14.
 */
@Entity
@Table(name = "CustomDocumentRegistry")
public class CustomDocumentRegistry implements Serializable {

    private Integer id;

    private Integer client;

    private String docNum;

    @Lob
    private String type;

    @Lob
    private String preamble;

    @Lob
    private String mainPart;

    @Lob
    private String finalPart;

    @Lob
    private String forWhom;

    @Lob
    private String signature;

    @Lob
    private String performerText;

    private Integer performerId;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;

    public CustomDocumentRegistry(Integer client, String docNum, String type, String preamble, String mainPart, String finalPart, String forWhom, String signature, String performerText, Integer performerId, Date date) {
        this.client = client;
        this.docNum = docNum;
        this.type = type;
        this.preamble = preamble;
        this.mainPart = mainPart;
        this.finalPart = finalPart;
        this.forWhom = forWhom;
        this.signature = signature;
        this.performerText = performerText;
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


    public CustomDocumentRegistry() {

    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public String getDocNum() {
        return docNum;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPreamble() {
        return preamble;
    }

    public void setPreamble(String preamble) {
        this.preamble = preamble;
    }

    public String getMainPart() {
        return mainPart;
    }

    public void setMainPart(String mainPart) {
        this.mainPart = mainPart;
    }

    public String getFinalPart() {
        return finalPart;
    }

    public void setFinalPart(String finalPart) {
        this.finalPart = finalPart;
    }

    public String getForWhom() {
        return forWhom;
    }

    public void setForWhom(String forWhom) {
        this.forWhom = forWhom;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPerformerText() {
        return performerText;
    }

    public void setPerformerText(String performerText) {
        this.performerText = performerText;
    }

    public Integer getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Integer performerId) {
        this.performerId = performerId;
    }


}
