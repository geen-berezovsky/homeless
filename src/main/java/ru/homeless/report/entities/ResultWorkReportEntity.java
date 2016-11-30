package ru.homeless.report.entities;

public class ResultWorkReportEntity {

    private String workerSurname;
    private String contractPointsCaption;
    private Boolean isLivingInShelter;
    private Integer tasksPerformed;

    public ResultWorkReportEntity(String workerSurname, String contractPointsCaption, 
            Boolean isLivingInShelter, Integer totalTasksPerformed) {
        this.workerSurname = workerSurname;
        this.contractPointsCaption = contractPointsCaption;
        this.isLivingInShelter = isLivingInShelter;
        this.tasksPerformed = totalTasksPerformed;
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

    public Boolean isLivingInShelter() {
        return isLivingInShelter;
    }

    public void setLivingInShelter(Boolean isLivingInShelter) {
        this.isLivingInShelter = isLivingInShelter;
    }

    public Integer getTasksPerformed() {
        return tasksPerformed;
    }

    public void setTasksPerformed(Integer tasksPerformed) {
        this.tasksPerformed = tasksPerformed;
    }

}
