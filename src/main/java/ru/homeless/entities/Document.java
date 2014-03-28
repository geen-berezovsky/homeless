package ru.homeless.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Document")
public class Document implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
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

	public Document() {
		
	}
	
	public Document(String docPrefix, String docNum, String whereAndWhom, DocType doctype, Date date) {
		setDocPrefix(docPrefix);
		setDocNum(docNum);
		setWhereAndWhom(whereAndWhom);
		setDoctype(doctype);
		setDate(date);
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public int getClient() {
		return client;
	}

	public void setClient(int client) {
		this.client = client;
	}

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = DocType.class)
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

	public int getRegistration() {
		return registration;
	}

	public void setRegistration(int registration) {
		this.registration = registration;
	}

	public int getWorker() {
		return worker;
	}

	public void setWorker(int worker) {
		this.worker = worker;
	}
	
	
}
