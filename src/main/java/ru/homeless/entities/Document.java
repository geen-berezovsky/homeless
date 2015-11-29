package ru.homeless.entities;

import org.hibernate.annotations.Cascade;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "Document")
public class Document implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String docPrefix;
	private String docNum;
	private String whereAndWhom;
	private Integer client;
	private Integer worker;
	private DocType doctype;
	private Date date;
	private Integer registration;
	private String address;
	private String city;

    private Date tempRegDateFrom;
    private Date tempRegDateTo;

    public Document() {
		
	}
	
	public Document(String docPrefix, String docNum, String whereAndWhom, DocType doctype, Date date, Date tempRegDateFrom, Date tempRegDateTo) {
		setDocPrefix(docPrefix);
		setDocNum(docNum);
		setWhereAndWhom(whereAndWhom);
		setDoctype(doctype);
		setDate(date);
        setTempRegDateFrom(tempRegDateFrom);
        setTempRegDateTo(tempRegDateTo);
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDocPrefix() {
		return docPrefix;
	}

	public void setDocPrefix(String docPrefix) {
		this.docPrefix = docPrefix;
	}

	public String getDocNum() {
		return docNum;
	}

	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}

	public String getWhereAndWhom() {
		return whereAndWhom;
	}

	public void setWhereAndWhom(String whereAndWhom) {
		this.whereAndWhom = whereAndWhom;
	}

	public Integer getClient() {
		return client;
	}

	public void setClient(Integer client) {
		this.client = client;
	}

	@ManyToOne(targetEntity = DocType.class)
	@JoinColumn(name = "doctype")
	public DocType getDoctype() {
		return doctype;
	}

	public void setDoctype(DocType doctype) {
		this.doctype = doctype;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getRegistration() {
		return registration;
	}

	public void setRegistration(Integer registration) {
		this.registration = registration;
	}

	public Integer getWorker() {
		return worker;
	}

	public void setWorker(Integer worker) {
		this.worker = worker;
	}

    public Date getTempRegDateFrom() {
        return tempRegDateFrom;
    }

    public void setTempRegDateFrom(Date tempRegDateFrom) {
        this.tempRegDateFrom = tempRegDateFrom;
    }

    public Date getTempRegDateTo() {
        return tempRegDateTo;
    }

    public void setTempRegDateTo(Date tempRegDateTo) {
        this.tempRegDateTo = tempRegDateTo;
    }

    public boolean equals(Object o) {
		if (o == null) return false;
		if (! (o instanceof Document)) {
			return false;
		}
		Document d = (Document) o;
        if (new Integer(getId()).equals(new Integer(d.getId()))) {
			return true;
		} else {
			return false;	
		}
	}

	
}
