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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class ContractControl implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer servcontract;
	private ContractPoints contractpoints;
	private Date endDate;
	
	@Lob
	private String comments;
	
	public ContractControl() {
		
	}
	
	public ContractControl(Integer servcontract, ContractPoints contractpoints) {
		setServcontract(servcontract);
		setContractpoints(contractpoints);
		setEndDate(null);
		setComments("");
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Lob
	public String getComments() {
		return comments;
	}
	@Lob
	public void setComments(String comments) {
		this.comments = comments;
	}

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = ContractPoints.class)
	@JoinColumn(name="contractpoints")
	public ContractPoints getContractpoints() {
		return contractpoints;
	}

	public void setContractpoints(ContractPoints contractpoints) {
		this.contractpoints = contractpoints;
	}

	public Integer getServcontract() {
		return servcontract;
	}

	public void setServcontract(Integer servcontract) {
		this.servcontract = servcontract;
	}
}
