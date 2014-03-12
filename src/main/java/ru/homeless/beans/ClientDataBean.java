package ru.homeless.beans;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ru.homeless.entities.Breadwinner;
import ru.homeless.entities.ChronicDisease;
import ru.homeless.entities.Client;
import ru.homeless.entities.Education;
import ru.homeless.entities.FamilyCommunication;
import ru.homeless.entities.NightStay;
import ru.homeless.entities.Reasonofhomeless;
import ru.homeless.util.Util;

/*
 * This is class only for externalizing properties of selected client and persisting them to the database after
 * So, it is just a copy of Client pojo for availability of properties in the children class
 */
@ManagedBean (name = "clientdata")
@SessionScoped
public class ClientDataBean implements Serializable {
	public static Logger log = Logger.getLogger(ClientDataBean.class);
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
	private String uniqDisease;
	private String uniqBreadwinner;
	private String uniqReason;
	private String photoName;
	private String photoCheckSum;
	private String contacts;
	private String memo;
	private Date regDate;
	private String whereWasBorn;
	private Blob avatar;
	private Date date;

	//custom
	private StreamedContent clientFormAvatar; //fake
	private String formattedDate;
	protected int selectedMonth;
	private int selectedYear;

	
	public ClientDataBean() {
		
	}

	
	protected void copyClientToClientData(Client c) {
		setId(c.getId());
		setSurname(c.getSurname());
		setMiddlename(c.getMiddlename());
		setFirstname(c.getFirstname());
		setProfession(c.getProfession());
		setMartialStatus(c.getMartialStatus());
		setIsStudent(c.getIsStudent());
		setLiveInFlat(c.getLiveInFlat());
		setDependents(c.getDependents());
		setGender(c.isGender());
		setDiseases(c.getDiseases());
		setNightStay(c.getNightstay());
		setEducation(c.getEducation());
		setFcom(c.getFcom());
		setHomelessdate(c.getHomelessdate());
		setBreadwinners(c.getBreadwinners());
		setReasonofhomeless(c.getReasonofhomeless());
		setUniqDisease(c.getUniqDisease());
		setUniqBreadwinner(c.getUniqBreadwinner());
		setUniqReason(c.getUniqReason());
		setPhotoName(c.getPhotoName());
		setPhotoCheckSum(c.getPhotoCheckSum());
		setContacts(c.getContacts());
		setMemo(c.getMemo());
		setRegDate(c.getRegDate());
		setWhereWasBorn(c.getWhereWasBorn());
		setAvatar(c.getAvatar());
		setDate(c.getDate());
	}
	protected Client copyClientDataToClient(Client c) {
		c.setId(getId());
		c.setSurname(getSurname());
		c.setMiddlename(getMiddlename());
		c.setFirstname(getFirstname());
		c.setProfession(getProfession());
		c.setMartialStatus(getMartialStatus());
		c.setIsStudent(getIsStudent());
		c.setLiveInFlat(getLiveInFlat());
		c.setDependents(getDependents());
		c.setGender(isGender());
		c.setDiseases(getDiseases());
		c.setEducation(getEducation());
		c.setFcom(getFcom());
		c.setNightstay(getNightStay());
		c.setHomelessdate(getHomelessdate());
		c.setBreadwinners(getBreadwinners());
		c.setReasonofhomeless(getReasonofhomeless());
		c.setUniqBreadwinner(getUniqBreadwinner());
		c.setUniqDisease(getUniqDisease());
		c.setUniqReason(getUniqReason());
		c.setPhotoName(getPhotoName());
		c.setPhotoCheckSum(getPhotoCheckSum());
		c.setContacts(getContacts());
		c.setMemo(getMemo());
		c.setRegDate(getRegDate());
		c.setWhereWasBorn(getWhereWasBorn());
		c.setAvatar(getAvatar());
		c.setDate(getDate());
		return c;
	}
	
	public String toString() {
		return "[Surname:\t"+getSurname()+" ]" +
				"[Name:\t"+getFirstname()+" ]" +
				"[Middlename:\t"+getMiddlename()+" ]"
				;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getDependents() {
		return dependents;
	}

	public void setDependents(int dependents) {
		this.dependents = dependents;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public Set<ChronicDisease> getDiseases() {
		return diseases;
	}

	public void setDiseases(Set<ChronicDisease> diseases) {
		this.diseases = diseases;
	}

	public NightStay getNightStay() {
		return nightStay;
	}

	public void setNightStay(NightStay nightStay) {
		this.nightStay = nightStay;
	}

	public Education getEducation() {
		return education;
	}

	public void setEducation(Education education) {
		this.education = education;
	}

	public FamilyCommunication getFcom() {
		return fcom;
	}

	public void setFcom(FamilyCommunication fcom) {
		this.fcom = fcom;
	}

	public Date getHomelessdate() {
		return homelessdate;
	}

	public void setHomelessdate(Date homelessdate) {
		this.homelessdate = homelessdate;
	}

	public Set<Breadwinner> getBreadwinners() {
		return breadwinners;
	}

	public void setBreadwinners(Set<Breadwinner> breadwinners) {
		this.breadwinners = breadwinners;
	}

	public Set<Reasonofhomeless> getReasonofhomeless() {
		return reasonofhomeless;
	}

	public void setReasonofhomeless(Set<Reasonofhomeless> reasonofhomeless) {
		this.reasonofhomeless = reasonofhomeless;
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

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getMemo() {
		return memo;
	}

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

	public Blob getAvatar() {
		return avatar;
	}

	public void setAvatar(Blob avatar) {
		this.avatar = avatar;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public StreamedContent getClientFormAvatar() {
		InputStream imageInByteArray = null;
		if (getAvatar() != null) {
			try {
				imageInByteArray = getAvatar().getBinaryStream();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			
			BufferedImage bi = new BufferedImage(177, 144, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = bi.createGraphics();
			Stroke drawingStroke = new BasicStroke(3);
			Rectangle2D rect = new Rectangle2D.Double(0, 0, 177, 144);
			
			g.setStroke(drawingStroke);
			g.draw(rect);
			g.setPaint(Color.LIGHT_GRAY);
			g.fill(rect);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ImageIO.write(bi, "png", baos);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				baos.flush();
				imageInByteArray = new ByteArrayInputStream(baos.toByteArray());
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new DefaultStreamedContent(imageInByteArray, "image/png");
	}

	
	public void setClientFormAvatar(StreamedContent clientFormAvatar) {
		this.clientFormAvatar = clientFormAvatar;
	}
	
	public String getFormattedDate() {
		if (getDate() != null) {
			return Util.formatDate(getDate());
		} else {
			return "";
		}
	}

	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}

	public int getSelectedMonth() {
		return selectedMonth;
	}

	public void setSelectedMonth(int selectedMonth) {
		this.selectedMonth = selectedMonth;
	}

	public int getSelectedYear() {
		return selectedYear;
	}

	public void setSelectedYear(int selectedYear) {
		this.selectedYear = selectedYear;
	}


}
