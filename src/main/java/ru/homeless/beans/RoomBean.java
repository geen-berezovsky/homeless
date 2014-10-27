package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import ru.homeless.entities.Document;
import ru.homeless.entities.Room;
import ru.homeless.entities.Worker;
import ru.homeless.services.GenericService;
import ru.homeless.services.RoomService;
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
@ViewScoped
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


    public RoomService getRoomService() {
        return roomService;
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @ManagedProperty(value = "#{RoomService}")
	private RoomService roomService;


	public RoomBean() {
		HttpSession session = Util.getSession();
        rooms = new ArrayList<>();
	}


    public void openDlg() {
        updateRoomsData();
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.update("room_settings");
        rc.execute("roomSettingsWv.show();");
    }


    public void updateRoomsData() {
        rooms.clear();
        rooms.addAll(roomService.getInstances(Room.class));
        for (Room r : rooms) {
            r.setCurrentnumoflivers(getRoomService().getCurrentRoomLiversNumber(r.getId()));
            //update current livers view in the database
            getRoomService().updateInstance(r);
        }
	}


    public void updateSelectedData() {
        setSelectedRoom(roomService.getInstanceById(Room.class,selectedRoomId));

    }

    public void addNewRoom() {
        selectedRoom = new Room();
        selectedRoom.setRoomnumber("Новая комната");
        roomService.addInstance(selectedRoom);
        updateRoomsData();

    }

    public void deleteSelectedRoom() {
        setSelectedRoom(roomService.getInstanceById(Room.class,selectedRoomId));
        if (getRoomService().isRoomReadyToBeDeleted(selectedRoomId)) {
            roomService.deleteInstance(selectedRoom);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Данные по комнате "+selectedRoom.getRoomnumber()+" безвозвратно удалены", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"В комнате есть проживающие, ее удалить нельзя!", ""));
        }
        updateRoomsData();
    }

    public void updateRoomData() {
        roomService.updateInstance(selectedRoom);
        updateRoomsData();
    }

	
}
