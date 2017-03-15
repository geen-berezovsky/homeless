package ru.homeless.report.entities;

public class OuterReportEntity {
	
	private String clientId;
	private String name;
	private String dateOfBith;
	private String startDate;
	private String caption;
	private String endDate;
	private String stopDate;
	private String comments;
	private String memo;
	private String workerSurname;
	
	public OuterReportEntity(String clientId, String name, String dateOfBith, String startDate, String caption, 
			String endDate, String stopDate, String comments, String memo, String workerSurname) {
		
		this.clientId = clientId;
		this.name = name;
		this.dateOfBith = dateOfBith;
		this.startDate = startDate;
		this.caption = caption;
		this.endDate = endDate;
		this.stopDate = stopDate;
		this.comments = comments;
		this.memo = memo;
		this.workerSurname = workerSurname;
		
	}
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDateOfBith() {
		return dateOfBith;
	}
	public void setDateOfBith(String dateOfBith) {
		this.dateOfBith = dateOfBith;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStopDate() {
		return stopDate;
	}
	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getWorkerSurname() {
		return workerSurname;
	}
	public void setWorkerSurname(String workerSurname) {
		this.workerSurname = workerSurname;
	}
}
