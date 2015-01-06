package ru.homeless.report.entities;

public class OverVacReportEntity {
	
	private String clientId;
	private String name;
	private String dateOfBirth;
	private String roomId;
	private String inShelter;
	private String outShelter;
	private String workerSurname;
	private String fluoragr;
	private String typhVac;
	private String dipthVac;
	private String hepotitsVac;
	
	public OverVacReportEntity(String clientId, String name, String dateOfBirth, String roomId, String inShelter, String outShelter, String workerSurname, 
			String fluoragr, String typhVac, String dipthVac, String hepotitsVac) {
		
		this.clientId = clientId;
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.roomId = roomId;
		this.inShelter = inShelter;
		this.outShelter = outShelter;
		this.workerSurname = workerSurname;
		this.fluoragr = fluoragr;
		this.typhVac = typhVac;
		this.dipthVac = dipthVac;
		this.hepotitsVac = hepotitsVac;
		
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
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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
	public String getOutShelter() {
		return outShelter;
	}
	public void setOutShelter(String outShelter) {
		this.outShelter = outShelter;
	}
	public String getWorkerSurname() {
		return workerSurname;
	}
	public void setWorkerSurname(String workerSurname) {
		this.workerSurname = workerSurname;
	}
	public String getFluoragr() {
		return fluoragr;
	}
	public void setFluoragr(String fluoragr) {
		this.fluoragr = fluoragr;
	}
	public String getTyphVac() {
		return typhVac;
	}
	public void setTyphVac(String typhVac) {
		this.typhVac = typhVac;
	}
	public String getDipthVac() {
		return dipthVac;
	}
	public void setDipthVac(String dipthVac) {
		this.dipthVac = dipthVac;
	}
	public String getHepotitsVac() {
		return hepotitsVac;
	}
	public void setHepotitsVac(String hepotitsVac) {
		this.hepotitsVac = hepotitsVac;
	}
	
	
}
