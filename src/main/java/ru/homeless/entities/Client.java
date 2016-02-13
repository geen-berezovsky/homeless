package ru.homeless.entities;

import ru.homeless.util.Util;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "Client")
public class Client implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;

	private String surname;
	private String middlename;
	private String firstname;
	private String profession;
	private int martialStatus;
	private int isStudent;
	private int liveInFlat;
	private int dependents;
	private boolean gender;
	private Set<ChronicDisease> diseases;
	private NightStay nightStay;
	private Education education;
	private FamilyCommunication fcom;
	private Date homelessdate;
	private Set<Breadwinner> breadwinners;
	private Set<Reasonofhomeless> reasonofhomeless;

	private Set<RecievedService> recievedservices;
	
	private String uniqDisease;
	private String uniqBreadwinner;
	private String uniqReason;
	private String photoName;
	private String photoCheckSum;

    private Date deathDate;
    private String deathReason;
    private String deathCity;

    private SubRegion lastLiving;
    private SubRegion lastRegistration;

    private Boolean hasNotice;

	public Client() {

	}

	@Lob
	private String contacts;

	@Lob
	private String memo;

	private Date regDate;
	private String whereWasBorn;

	@Lob
	private Blob avatar;

	@Temporal(TemporalType.DATE)
	private Date date;

	public Client(String surname, String firstname, String middlename, boolean gender, Blob avatar, Date date, String photoName, String photoChecksum) {
		setSurname(surname);
		setFirstname(firstname);
		setMiddlename(middlename);
		setGender(gender);
		setDate(date);
		setAvatar(avatar);
		setMartialStatus(0);
		setDependents(0);
		setIsStudent(0);
		setLiveInFlat(0);
		setPhotoName(photoName);
		setPhotoCheckSum(photoChecksum);
		diseases = new HashSet<ChronicDisease>();
		breadwinners = new HashSet<Breadwinner>();
		reasonofhomeless = new HashSet<Reasonofhomeless>();

	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Basic
	@Column(columnDefinition = "BIT")
	public boolean isGender() {
		return gender;
	}

    public void setGender(boolean gender) {
		this.gender = gender;
	}

	@Lob
	public Blob getAvatar() {
		return avatar;
	}

	@Lob
	public void setAvatar(Blob avatar2) {
		this.avatar = avatar2;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToMany(cascade = CascadeType.ALL ,targetEntity = ChronicDisease.class)
	@JoinTable(name = "link_chronicdisease_client", 
		joinColumns = @JoinColumn(name = "clients_id", nullable = false, updatable = false), 
		inverseJoinColumns = @JoinColumn(name = "diseases_id", nullable = false, updatable = false) 
	)
	public Set<ChronicDisease> getDiseases() {
		return diseases;
	}

	public void setDiseases(Set<ChronicDisease> diseases) {
		this.diseases = diseases;
	}

	@ManyToOne(targetEntity = NightStay.class)
	@JoinColumn(name = "nightStay")
	public NightStay getNightstay() {
		return nightStay;
	}

	public void setNightstay(NightStay nightStay) {
		this.nightStay = nightStay;
	}

	@ManyToOne(targetEntity = Education.class)
	@JoinColumn(name = "education")
	public Education getEducation() {
		return education;
	}

	public void setEducation(Education education) {
		this.education = education;
	}

	public Date getHomelessdate() {
		return homelessdate;
	}

	public void setHomelessdate(Date homelessdate) {
		this.homelessdate = homelessdate;
	}

	@ManyToMany(cascade = CascadeType.ALL ,targetEntity = Breadwinner.class)
	@JoinTable(name = "link_breadwinner_client", 
		joinColumns = @JoinColumn(name = "clients_id", nullable = false, updatable = false), 
		inverseJoinColumns = @JoinColumn(name = "breadwinners_id", nullable = false, updatable = false) 
	)
	public Set<Breadwinner> getBreadwinners() {
		return breadwinners;
	}

	public void setBreadwinners(Set<Breadwinner> breadwinners) {
		this.breadwinners = breadwinners;
	}

	@ManyToMany(cascade = CascadeType.ALL ,targetEntity = Reasonofhomeless.class)
	@JoinTable(name = "link_reasonofhomeless_client", 
		joinColumns = @JoinColumn(name = "clients_id", nullable = false, updatable = false), 
		inverseJoinColumns = @JoinColumn(name = "reasonofhomeless_id", nullable = false, updatable = false) 
	)
	public Set<Reasonofhomeless> getReasonofhomeless() {
		return reasonofhomeless;
	}

	public void setReasonofhomeless(Set<Reasonofhomeless> reasonofhomeless) {
		this.reasonofhomeless = reasonofhomeless;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public int getMartialStatus() {
		return martialStatus;
	}

	public void setMartialStatus(int martialStatus) {
		this.martialStatus = martialStatus;
	}

	public int getDependents() {
		return dependents;
	}

	public void setDependents(int dependents) {
		this.dependents = dependents;
	}

	@ManyToOne(targetEntity = FamilyCommunication.class)
	@JoinColumn(name = "familycommunication")
	public FamilyCommunication getFcom() {
		return fcom;
	}

	public void setFcom(FamilyCommunication fcom) {
		this.fcom = fcom;
	}

	public int getIsStudent() {
		return isStudent;
	}

	public void setIsStudent(int isStudent) {
		this.isStudent = isStudent;
	}

	public int getLiveInFlat() {
		return liveInFlat;
	}

	public void setLiveInFlat(int liveInFlat) {
		this.liveInFlat = liveInFlat;
	}

	public String getUniqDisease() {
		return uniqDisease;
	}

	public void setUniqDisease(String uniqDisease) {
		this.uniqDisease = uniqDisease;
	}

	public String getUniqBreadwinner() {
		return uniqBreadwinner;
	}

	public void setUniqBreadwinner(String uniqBreadwinner) {
		this.uniqBreadwinner = uniqBreadwinner;
	}

	public String getUniqReason() {
		return uniqReason;
	}

	public void setUniqReason(String uniqReason) {
		this.uniqReason = uniqReason;
	}

	@Lob
	public String getContacts() {
		return contacts;
	}

	@Lob
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	@Lob
	public String getMemo() {
		return memo;
	}

	@Lob
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getWhereWasBorn() {
		return whereWasBorn;
	}

	public void setWhereWasBorn(String whereWasBorn) {
		this.whereWasBorn = whereWasBorn;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public String getPhotoCheckSum() {
		return photoCheckSum;
	}

	public void setPhotoCheckSum(String photoCheckSum) {
		this.photoCheckSum = photoCheckSum;
	}

	@OneToMany(targetEntity = RecievedService.class, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "client")
	public Set<RecievedService> getRecievedservices() {
		return recievedservices;
	}

	public void setRecievedservices(Set<RecievedService> recievedservices) {
		this.recievedservices = recievedservices;
	}
	
	@Transient
    public String getShortFIO(){
    	return getSurname() + " " + getFirstname().substring(0, 1) + ". " + getMiddlename().substring(0, 1) + ".";
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public String getDeathCity() {
        return deathCity;
    }

    public void setDeathCity(String deathCity) {
        this.deathCity = deathCity;
    }

    @ManyToOne(targetEntity = SubRegion.class)
    @JoinColumn(name = "lastLiving")
    public SubRegion getLastLiving() {
        return lastLiving;
    }

    public void setLastLiving(SubRegion lastLiving) {
        this.lastLiving = lastLiving;
    }

    @ManyToOne(targetEntity = SubRegion.class)
    @JoinColumn(name = "lastRegistration")
    public SubRegion getLastRegistration() {
        return lastRegistration;
    }

    public void setLastRegistration(SubRegion lastRegistration) {
        this.lastRegistration = lastRegistration;
    }

    @Basic
    @Column(columnDefinition = "BIT")
    public Boolean getHasNotice() {
        return hasNotice;
    }

    public void setHasNotice(Boolean hasNotice) {
        this.hasNotice = hasNotice;
    }

    public String toString() {
        return "["+getId()+"] "+getSurname()+" "+getFirstname()+" "+getMiddlename()+" ["+ Util.formatDate(getDate())+"]";
    }

}
