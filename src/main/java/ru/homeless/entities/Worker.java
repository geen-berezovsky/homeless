package ru.homeless.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Worker")
public class Worker implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String surname;
	private String firstname;
	private String middlename;
	private String password;
	private Rules rules;
	private String warrantNum;
	private Date warrantDate;
    private String primefacesskin;

	public Worker() {
		
	}
	
	public Worker(String surname, String firstname, String middlename, String password, Rules rules) {
		setSurname(surname);
		setFirstname(firstname);
		setMiddlename(middlename);
		setPassword(password); 
		setRules(rules);
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@ManyToOne                                   
	@JoinColumn(name="rules")
	public Rules getRules() {
		return rules;
	}
	
	public void setRules(Rules rules) {
		this.rules = rules;
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id; 
	}
	
	@Override
	public String toString() {
		return surname + " " + firstname;
	}

	public String getWarrantNum() {
		return warrantNum;
	}

	public void setWarrantNum(String warrantNum) {
		this.warrantNum = warrantNum;
	}

	public Date getWarrantDate() {
		return warrantDate;
	}

	public void setWarrantDate(Date warrantDate) {
		this.warrantDate = warrantDate;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

    public String getPrimefacesskin() {
        return primefacesskin;
    }

    public void setPrimefacesskin(String primefacesskin) {
        this.primefacesskin = primefacesskin;
    }
}
