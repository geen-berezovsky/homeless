package ru.homeless.report.entities;

public class OutOfShelterReportEntity {
	private String clientId;
	private String clientSurname;
	private String clientDateOfBirth;
	private String roomId;
	private String inShelter;
	private String contractPointCaption;
	private String contractEndDate;
	private String outShelterDate;
	private String comments;
	private String contacts;
	private String workerSurname;
	
	public OutOfShelterReportEntity(String clientId, String clientSurname, String clientDateOfBirth, String roomId, 
			String inShelter, String contractPointCaption, String contractEndDate, String outShelterDate, String comments, 
			String contacts, String workerSurname) {
		
		this.clientId = clientId;
		this.clientSurname = clientSurname;
		this.clientDateOfBirth = clientDateOfBirth;
		this.roomId = roomId;
		this.inShelter = inShelter;
		this.contractPointCaption = contractPointCaption;
		this.contractEndDate = contractEndDate;
		this.outShelterDate = outShelterDate;
		this.comments = comments;
		this.contacts = contacts;
		this.workerSurname = workerSurname;
		
	}
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSurname() {
		return clientSurname;
	}
	public void setClientSurname(String clientSurname) {
		this.clientSurname = clientSurname;
	}
	public String getClientDateOfBirth() {
		return clientDateOfBirth;
	}
	public void setClientDateOfBirth(String clientDateOfBirth) {
		this.clientDateOfBirth = clientDateOfBirth;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getInShelter() {
		return inShelter;
	}
	public void setInShelter(String inShelter) {
		this.inShelter = inShelter;
	}
	public String getContractPointCaption() {
		return contractPointCaption;
	}
	public void setContractPointCaption(String contractPointCaption) {
		this.contractPointCaption = contractPointCaption;
	}
	public String getContractEndDate() {
		return contractEndDate;
	}
	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
	public String getOutShelterDate() {
		return outShelterDate;
	}
	public void setOutShelterDate(String outShelterDate) {
		this.outShelterDate = outShelterDate;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getWorkerSurname() {
		return workerSurname;
	}
	public void setWorkerSurname(String workerSurname) {
		this.workerSurname = workerSurname;
	}

}
