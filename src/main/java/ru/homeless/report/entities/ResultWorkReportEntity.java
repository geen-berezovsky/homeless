package ru.homeless.report.entities;

public class ResultWorkReportEntity {

	private String workerSurname;
	private String contractPointsCaption;
	private Integer countLivingInShelter;
	private Integer countNonLivingInShelter;
	
	public ResultWorkReportEntity(String workerSurname, String contractPointsCaption, Integer countLivingInShelter, Integer countNonLivingInShelter ) {
		this.workerSurname = workerSurname;
		this.contractPointsCaption = contractPointsCaption;
		this.countLivingInShelter = countLivingInShelter;
		this.countNonLivingInShelter = countNonLivingInShelter;
	}
	
	public String getWorkerSurname() {
		return workerSurname;
	}
	public void setWorkerSurname(String workerSurname) {
		this.workerSurname = workerSurname;
	}
	public String getContractPointsCaption() {
		return contractPointsCaption;
	}
	public void setContractPointsCaption(String contractPointsCaption) {
		this.contractPointsCaption = contractPointsCaption;
	}
	public Integer getCountLivingInShelter() {
		return countLivingInShelter;
	}
	public void setCountLivingInShelter(Integer countLivingInShelter) {
		this.countLivingInShelter = countLivingInShelter;
	}
	public Integer getCountNonLivingInShelter() {
		return countNonLivingInShelter;
	}
	public void setCountNonLivingInShelter(Integer countNonLivingInShelter) {
		this.countNonLivingInShelter = countNonLivingInShelter;
	}

	
}
