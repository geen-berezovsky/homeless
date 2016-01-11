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

import ru.homeless.entities.*;
import ru.homeless.services.RoomService;
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
        Object obj = getRoomService().getInstanceById(ShelterResult.class, sid);
        if (obj != null) {
            return ((ShelterResult) obj).getCaption();
        } else {
            return "NULL";
        }
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @ManagedProperty(value = "#{RoomService}")
	private RoomService roomService;

    public void updateRoomsData() {
        rooms.clear();
        rooms.addAll(roomService.getInstances(Room.class));
        for (Room r : rooms) {
            r.setCurrentnumoflivers(getRoomService().getCurrentRoomLiversNumber(r.getId()));
            //update current livers view in the database
            getRoomService().updateInstance(r);
        }
    }

    public ClientShelterBean() {
        rooms = new ArrayList<>();
        shelterList = new ArrayList<>();
        shelterResultList = new ArrayList<>();
	}
	
	public String formatDate(Date q) {
		if (q != null && !q.equals("")) {
			return Util.formatDate(q);
		} else {
			return "";
		}
	}

    public void updateCid() {
        HttpSession session = Util.getSession();
        String cids = session.getAttribute("cid").toString();

        if (cids != null && !cids.trim().equals("")) {
            this.cid = Integer.parseInt(cids);
        }
    }

	public void reload() {
		updateCid();
		newSelectedShelter(); // set new shelter
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update("shelterlistId");
        rc.update("add_shelter");	//force updating the add shelter form
	}
	
	
	public void deleteShelter() {
        getRoomService().deleteInstance(getRoomService().getInstanceById(ShelterHistory.class, selectedShelter.getId()));
        updateRoomsData();
		reload();
	}

	public void editShelter() {
        updateRoomsData();
        shelterResultList = new ArrayList<>();
        shelterResultList.addAll(getRoomService().getInstances(ShelterResult.class));
        selectedShelter = getRoomService().getInstanceById(ShelterHistory.class, selectedShelter.getId());
		RequestContext rc = RequestContext.getCurrentInstance();
        rc.update("add_shelter");
        rc.execute("addShelterWv.show();");
	}

    public void validateStartDateFormat(FacesContext ctx, UIComponent component, Object value) {
        FacesMessage msg = null;
        if (value==null || value.toString().trim().equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Пожалуйста введите дату начала проживания", "Формат даты: ДД.ММ.ГГГГ");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ValidatorException(msg);
        } else {
            Util.validateDateFormat(ctx, component, value);
        }
    }

    public void validateStopDateFormat(FacesContext ctx, UIComponent component, Object value) {
        FacesMessage msg = null;
        if (value==null || value.toString().trim().equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Пожалуйста введите дату окончания проживания", "Формат даты: ДД.ММ.ГГГГ");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ValidatorException(msg);
        } else {
            Util.validateDateFormat(ctx, component, value);
        }
    }

    public void validateVaccinationDateFormat(FacesContext ctx, UIComponent component, Object value) {
        Util.validateDateFormat(ctx, component, value);
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
        if (cid != 0) {
            return getRoomService().getInstancesByClientId(ShelterHistory.class, getRoomService().getInstanceById(Client.class, cid));
        } else {
            return new ArrayList<ShelterHistory>();
        }
	}

	public void setShelterList(List<ShelterHistory> shelterList) {
		this.shelterList = shelterList;
	}

    public void addNewShelter() {
        updateRoomsData();
        shelterResultList = new ArrayList<>();
        shelterResultList.addAll(getRoomService().getInstances(ShelterResult.class));
        selectedShelter = new ShelterHistory();
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.update("add_shelter");
        rc.execute("addShelterWv.show();");
    }

    public String formatRoomValue(Integer roomId) {
        if (roomId == null || roomId == 0) return null; else
            return getRoomService().getInstanceById(Room.class,roomId).getRoomnumber();
    }

    public void addNewShelterData() {

        //setting actual client
        selectedShelter.setClient(getRoomService().getInstanceById(Client.class, cid));

        log.info("Adding new shelter record for client "+cid);
        log.info(selectedShelter.getClient());
        log.info("Дата заселения: "+selectedShelter.getInShelter());
        log.info("Дата выселения: "+selectedShelter.getOutShelter());
        log.info("Комната: "+selectedShelter.getRoomId());
        log.info("Флюха: "+selectedShelter.getFluorogr());
        log.info("Дифтерия: "+selectedShelter.getDipthVac());
        log.info("Гепатит: "+selectedShelter.getHepotitsVac());
        log.info("Тиф: "+selectedShelter.getTyphVac());
        log.info("Status: "+selectedShelter.getShelterresult());
        getRoomService().addInstance(selectedShelter);
        selectedShelter = new ShelterHistory();

        //Update the table
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.update("add_shelter");
        rc.execute("addShelterWv.hide()");
        //RequestContext rc = RequestContext.getCurrentInstance();
        //rc.update(":m_tabview:shelter_form");

    }

    public void handleCloseEditShelterDlg() {
        //auto saving
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("saveEditShelterForm()");
        log.info("Dialog Edit Shelter has been closed unexpectedly. The selected Shelter is saved automatically.");
    }


}
