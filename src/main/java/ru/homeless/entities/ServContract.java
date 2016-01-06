package ru.homeless.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class ServContract implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Worker worker;
	private Integer client;
	private Date startDate;
	private Date stopDate;
	private String docNum;
	private Integer documentId;
	private ContractResult result;
	private String commentResult;
	private Set<ContractControl> contractcontrols;
	
	public ServContract() {
		
	}
	
	public ServContract(Worker worker, int client, Date startDate, Date stopDate, String docNum, ContractResult cr) {
		setWorker(worker);
		setClient(client);
		setStartDate(startDate);
		setStopDate(stopDate);
		setDocNum(docNum);
		setResult(cr);
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne (targetEntity = Worker.class)
	@JoinColumn(name="worker")
	public Worker getWorker() {
		return worker;
	}
	public void setWorker(Worker worker) {
		this.worker = worker;
	}
	
	public Integer getClient() {
		return client;
	}
	public void setClient(Integer client) {
		this.client = client;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getStopDate() {
		return stopDate;
	}
	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}
	

	public String getDocNum() {
		return docNum;
	}

	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}
	
	@ManyToOne (targetEntity = ContractResult.class)
	@JoinColumn(name="contractresult")
	public ContractResult getResult() {
		return result;
	}

	public void setResult(ContractResult result) {
		this.result = result;
	}

	public String getCommentResult() {
		return commentResult;
	}

	public void setCommentResult(String commentResult) {
		this.commentResult = commentResult;
	}

	public Integer getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}

	@OneToMany(targetEntity = ContractControl.class, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "servcontract")
	public Set<ContractControl> getContractcontrols() {
		return contractcontrols;
	}

	public void setContractcontrols(Set<ContractControl> contractcontrols) {
		this.contractcontrols = contractcontrols;
	}

}
