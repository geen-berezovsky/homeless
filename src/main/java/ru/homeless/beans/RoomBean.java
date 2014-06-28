package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import ru.homeless.entities.Document;
import ru.homeless.entities.Room;
import ru.homeless.entities.Worker;
import ru.homeless.services.GenericService;
import ru.homeless.services.WorkerService;
import ru.homeless.util.Util;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@ManagedBean (name = "roomb")
@SessionScoped
public class RoomBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(RoomBean.class);

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    private List<Room> rooms;

    public Room getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(Room selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    private Room selectedRoom;

    public Integer getSelectedRoomId() {
        return selectedRoomId;
    }

    public void setSelectedRoomId(Integer selectedRoomId) {
        this.selectedRoomId = selectedRoomId;
    }

    private Integer selectedRoomId;

    public GenericService getGenericService() {
        return genericService;
    }

    public void setGenericService(GenericService genericService) {
        this.genericService = genericService;
    }

    @ManagedProperty(value = "#{GenericService}")
	private GenericService genericService;


	public RoomBean() {
		HttpSession session = Util.getSession();
        rooms = new ArrayList<>();
	}
	
	public void onShow() {
        rooms.clear();
        rooms.addAll(genericService.getInstances(Room.class));
	}


    public void updateSelectedData() {
        setSelectedRoom(genericService.getInstanceById(Room.class,selectedRoomId));

    }

    public void addNewRoom() {
        selectedRoom = new Room();
        selectedRoom.setRoomnumber("Новая комната");
        genericService.addInstance(selectedRoom);
        onShow();

    }

    public void deleteSelectedRoom() {
        setSelectedRoom(genericService.getInstanceById(Room.class,selectedRoomId));
        genericService.deleteInstance(selectedRoom);
        onShow();
    }

    public void updateRoomData() {
        genericService.updateInstance(selectedRoom);
        onShow();
    }

	
}
