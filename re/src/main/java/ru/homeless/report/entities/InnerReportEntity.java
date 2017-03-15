package ru.homeless.report.entities;

public class InnerReportEntity {

	private String clientId;
	private String name;
    private String dateOfBith;
    private String comments;
    private String room;
    private String startDate; //of living
    private String stopDate; //of living
    private String purposes;
    private String releaseDate;
    private String releaseSteps;
	private String workerSurname;

    public InnerReportEntity() {}


    public InnerReportEntity(String clientId, String name, String dateOfBith, String comments, String room, String startDate, String stopDate, String purposes, String releaseDate, String releaseSteps, String workerSurname) {
        this.clientId = clientId;
        this.name = name;
        this.dateOfBith = dateOfBith;
        this.comments = comments;
        this.room = room;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.purposes = purposes;
        this.releaseDate = releaseDate;
        this.releaseSteps = releaseSteps;
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
	public String getWorkerSurname() {
		return workerSurname;
	}
	public void setWorkerSurname(String workerSurname) {
		this.workerSurname = workerSurname;
	}

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getPurposes() {
        return purposes;
    }

    public void setPurposes(String purposes) {
        this.purposes = purposes;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseSteps() {
        return releaseSteps;
    }

    public void setReleaseSteps(String releaseSteps) {
        this.releaseSteps = releaseSteps;
    }
}
