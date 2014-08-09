package ru.homeless.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import ru.homeless.entities.Client;
import ru.homeless.entities.Room;
import ru.homeless.entities.ShelterHistory;
import ru.homeless.entities.ShelterResult;
import ru.homeless.services.GenericService;
import ru.homeless.util.Util;

@ManagedBean (name = "clientshelter")
@SessionScoped
public class ClientShelterBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ClientShelterBean.class);
	private int cid = 0;
	private List<ShelterHistory> shelterList = null;
	private ShelterHistory selectedShelter;

    public List<ShelterResult> getShelterResultList() {
        return shelterResultList;
    }

    public void setShelterResultList(List<ShelterResult> shelterResultList) {
        this.shelterResultList = shelterResultList;
    }

    private List<ShelterResult> shelterResultList = null;


    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    private List<Room> rooms;

    public String shelterStatus(Integer sid) {
        Object obj = genericService.getInstanceById(ShelterResult.class,sid);
        if (obj != null) {
            return ((ShelterResult) obj).getCaption();
        } else {
            return "NULL";
        }
    }

	@ManagedProperty(value = "#{GenericService}")
	private GenericService genericService;
	
	public ClientShelterBean() {
        rooms = new ArrayList<>();
        shelterResultList = new ArrayList<>();
	}
	
	public String formatDate(Date q) {
		if (q != null && !q.equals("")) {
			return Util.formatDate(q);
		} else {
			return "";
		}
	}
	
	public void reload() {
		HttpSession session = Util.getSession();
		String cids = session.getAttribute("cid").toString();

		if (cids != null && !cids.trim().equals("")) {
			this.cid = Integer.parseInt(cids);
            //TODO: REFACTOR
            System.out.println();
//			setShelterList(getGenericService().getInstancesByClientId(ShelterHistory.class, cid));
            /*
            ArrayList<Object> objectArrayList = new ArrayList<>();
            objectArrayList.addAll(getGenericService().getInstancesByClientId(ShelterHistory.class, cid));
            for (Object o: objectArrayList) {
                log.info("Object val: "+o.getClass().getCanonicalName());
            }
            */
		}
		newSelectedShelter(); // set new shelter
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update("shelterlistId");	
	}
	
	
	public void deleteShelter() {
		getGenericService().deleteInstance(getGenericService().getInstanceById(ShelterHistory.class, selectedShelter.getId()));
		reload();
	}

	public void editShelter() {
		selectedShelter = getGenericService().getInstanceById(ShelterHistory.class, selectedShelter.getId());
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update("add_shelter");	//force updating the add shelter form	
	}

    public void validateStartDateFormat(FacesContext ctx, UIComponent component, Object value) {
        FacesMessage msg = null;
        if (value==null || value.toString().trim().equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Пожалуйста введите дату начала проживания", "Формат даты: ДД.ММ.ГГГГ");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ValidatorException(msg);
        }
        Date result = Util.validateDateFormat(ctx, component, value);
        if (result == null) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Некоректный формат даты", "Формат даты: ДД.ММ.ГГГГ");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ValidatorException(msg);
        }
    }

    public void validateStopDateFormat(FacesContext ctx, UIComponent component, Object value) {
        FacesMessage msg = null;
        if (value==null || value.toString().trim().equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Пожалуйста введите дату окончания проживания", "Формат даты: ДД.ММ.ГГГГ");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ValidatorException(msg);
        }
        Date result = Util.validateDateFormat(ctx, component, value);
        if (result == null) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Некоректный формат даты", "Формат даты: ДД.ММ.ГГГГ");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ValidatorException(msg);
        }
    }

    public void validateVaccinationDateFormat(FacesContext ctx, UIComponent component, Object value) {
        FacesMessage msg = null;
        Date result = Util.validateDateFormat(ctx, component, value);
        if (result == null) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Некоректный формат даты", "Формат даты: ДД.ММ.ГГГГ");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ValidatorException(msg);
        }
    }

    public void newSelectedShelter() {
		selectedShelter = new ShelterHistory();
	}

	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}

	public ShelterHistory getSelectedShelter() {
		return selectedShelter;
	}

	public void setSelectedShelter(ShelterHistory selectedShelter) {
		this.selectedShelter = selectedShelter;
	}

	public List<ShelterHistory> getShelterList() {
		return shelterList;
	}

	public void setShelterList(List<ShelterHistory> shelterList) {
		this.shelterList = shelterList;
	}

	public GenericService getGenericService() {
		return genericService;
	}

	public void setGenericService(GenericService genericService) {
		this.genericService = genericService;
	}


    public void onShow() {
        log.info("Called on show for Shelter");
        rooms.clear();
        rooms.addAll(genericService.getInstances(Room.class));
        shelterResultList.addAll(genericService.getInstances(ShelterResult.class));
    }

    public void addNewShelterData() {

        //setting actual client
        selectedShelter.setClient(getGenericService().getInstanceById(Client.class, cid));


        log.info("Adding new shelter record for client "+cid);
        log.info(selectedShelter.getClient());
        log.info("Дата заселения: "+selectedShelter.getInShelter());
        log.info("Дата выселения: "+selectedShelter.getOutShelter());
        log.info("Комната: "+selectedShelter.getRoomId());
        log.info("Флюха: "+selectedShelter.getFluorogr());
        log.info("Дифтерия: "+selectedShelter.getDipthVac());
        log.info("Гепатит: "+selectedShelter.getHepotitsVac());
        log.info("Тиф: "+selectedShelter.getTyphVac());
        log.info("Status: "+selectedShelter.getShelterresult().getCaption());
        getGenericService().addInstance(selectedShelter);
    }

}
