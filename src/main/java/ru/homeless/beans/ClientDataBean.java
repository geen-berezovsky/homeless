package ru.homeless.beans;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ru.homeless.configuration.Configuration;
import ru.homeless.entities.*;
import ru.homeless.services.ClientService;
import ru.homeless.services.GenericService;
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
	private Date regDate;
	private String whereWasBorn;
	private Blob avatar;
    private String originalPhotoFilePath;
    private SubRegion lastLiving;
    private SubRegion lastRegistration;
    private Boolean hasNotice;

	private Date date;

    @ManagedProperty(value = "#{ClientService}")
    private ClientService clientService;

    //custom
	private StreamedContent clientFormAvatar; //fake
    private StreamedContent clientFormRealPhoto; //fake
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
		setRegDate(c.getRegDate());
		setWhereWasBorn(c.getWhereWasBorn());
		setAvatar(c.getAvatar());
		setDate(c.getDate());
        setLastLiving(c.getLastLiving());
        setLastRegistration(c.getLastRegistration());
        setHasNotice(c.getHasNotice());
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
		c.setRegDate(getRegDate());
		c.setWhereWasBorn(getWhereWasBorn());
		c.setAvatar(getAvatar());
		c.setDate(getDate());
        c.setLastLiving(getLastLiving());
        c.setLastRegistration(getLastRegistration());
        c.setHasNotice(getHasNotice());
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
        if (nightStay == null) {
            return
                    getClientService().getInstanceByCaption(NightStay.class, "Нет ответа");
        } else {
            return nightStay;
        }

	}

	public void setNightStay(NightStay nightStay) {
		this.nightStay = nightStay;
	}

	public Education getEducation() {
        //set new values for relations if not exist
        if (education == null) {
            return
            getClientService().getInstanceByCaption(Education.class, "Нет ответа");
        } else {
            return education;
        }
	}

	public void setEducation(Education education) {
		this.education = education;
	}

	public FamilyCommunication getFcom() {
        if (fcom == null ) {
            return getClientService().getInstanceByCaption(FamilyCommunication.class, "Нет ответа");
        } else {
            return fcom;
        }
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

	//Shared Validators
	public void validateTextOnly(FacesContext ctx, UIComponent component, Object value) {
		Util.validateTextOnly(ctx, component, value);
	}

	public boolean isYearValid(String str) {
		Pattern pattern = Pattern.compile("\\d{4}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	Deprecated because of social workers sometimes don't know the real homeless age for the client or client can't say it
	This method can be deleted safely
	 */
	public void validateHomelessYear(FacesContext ctx, UIComponent component, Object value) {
		String str = value.toString();
        System.out.println(str);
        if (! isYearValid(str)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Неправильно указан год в поле <Бездомный с момента>!","Используйте формат гггг");
			throw new ValidatorException(msg);
		} else {
			updateHomelessDate(0, Integer.parseInt(str));
		}
	}

	public void updateHomelessDate(int month, int year) {
		int m = 0;
		int y = 0;

		Calendar orig = GregorianCalendar.getInstance();
		if (getHomelessdate()!=null) {
			orig.setTime(getHomelessdate());
		} else {
			DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			try {
				orig.setTime(df.parse("01.01.1900 00:00:00"));
			} catch (ParseException e) {
				log.error("Date parse exception",e);
			}
		}

		if (month == 0) {
			//set only year
			y = year;
			m = orig.get(Calendar.MONTH);
		} else if (year == 0) {
			//set only month
			m = month;
			y = orig.get(Calendar.YEAR);
		} else {
			m = orig.get(Calendar.MONTH);
			y = orig.get(Calendar.YEAR);
		}

		Calendar c = GregorianCalendar.getInstance();
		c.set(year, month, 1, 0, 0, 0);
		// setting updated value to the client
		setHomelessdate(c.getTime());
	}
	
	public void updateHomelessDate() {
		setSelectedMonth(getHomelessMonth(getHomelessdate()));
		setSelectedYear(getHomelessYear(getHomelessdate()));
	}
	
	public int getHomelessYear(Date query) {
		if (query != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(query);
			return c.get(Calendar.YEAR);
		} else {
			return -1;
		}
	}

	public int getHomelessMonth(Date query) {
		if (query != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(query);
			return c.get(Calendar.MONTH);
		} else {
			return -1;
		}
	}

	public void validateBirthDateFormat(FacesContext ctx, UIComponent component, Object value) {
		Date result = Util.validateDateFormat(ctx, component, value);
		if (result != null) {
			setDate(result);
		}
	}


    public StreamedContent getClientFormRealPhoto() throws IOException {
        File resultFile = new File(getOriginalPhotoFilePath());

        if (!resultFile.exists() || photoCheckSum.trim().equals("") || photoName.trim().equals("")) {
            return null;
        } else {
            StreamedContent sc = Util.loadResizedPhotoFromDisk(resultFile);
            return sc;
        }
    }

    public void setClientFormRealPhoto(StreamedContent clientFormRealPhoto) {
        this.clientFormRealPhoto = clientFormRealPhoto;
    }

    public String getOriginalPhotoFilePath() {
        File f = new File(Configuration.photos+"/"+getPhotoName());
        String str = f.getAbsolutePath().toString();
        if (!f.exists()) {
            return str + " не найден в хранилище";
        } else {
            return str;
        }
    }

    public void setOriginalPhotoFilePath(String originalPhotoFilePath) {
        this.originalPhotoFilePath = originalPhotoFilePath;
    }


    public SubRegion getLastLiving() {
        return lastLiving;
    }

    public void setLastLiving(SubRegion lastLiving) {
        this.lastLiving = lastLiving;
    }

    public SubRegion getLastRegistration() {
        return lastRegistration;
    }

    public void setLastRegistration(SubRegion lastRegistration) {
        this.lastRegistration = lastRegistration;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public Boolean getHasNotice() {
        return hasNotice;
    }

    public void setHasNotice(Boolean hasNotice) {
        this.hasNotice = hasNotice;
    }
}
