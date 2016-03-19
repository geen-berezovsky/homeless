package ru.homeless.report.entities;

public class OverVacReportEntity {

    private int id;
	private String name;
	private String dateOfBirth;
	private String inShelter;
	private String outShelter;
	private String workerSurname;
	private String fluoragr;
    private String hepotitsVac;
    private String dipthVac;
	private String typhVac;
    private String comments;
	
	public OverVacReportEntity(int id, String name, String dateOfBirth, String inShelter, String outShelter, String workerSurname,
                               String fluoragr, String hepotitsVac, String dipthVac, String typhVac, String comments) {
        this.id = id;
        this.name = name;
		this.dateOfBirth = dateOfBirth;
        this.inShelter = inShelter;
		this.outShelter = outShelter;
		this.workerSurname = workerSurname;
		this.fluoragr = fluoragr;
        this.hepotitsVac = hepotitsVac;
        this.dipthVac = dipthVac;
        this.typhVac = typhVac;
        this.comments = comments;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
