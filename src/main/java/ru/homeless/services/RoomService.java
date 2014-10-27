package ru.homeless.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.homeless.dao.RoomDAO;
import ru.homeless.dao.WorkerDAO;
import ru.homeless.entities.Document;
import ru.homeless.entities.Worker;

import java.io.Serializable;
import java.util.List;

@Service("RoomService")
@Transactional(readOnly = false)
public class RoomService extends GenericService implements Serializable {

	private static final long serialVersionUID = 1L;

    public RoomDAO getRoomDAO() {
        return roomDAO;
    }

    public void setRoomDAO(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    @Autowired
    private RoomDAO roomDAO;


	@Transactional
    public boolean isRoomReadyToBeDeleted(int id) {
        return getRoomDAO().isRoomReadyToBeDeleted(id);
    }

    @Transactional
    public int getCurrentRoomLiversNumber(int roomId) {
        return getRoomDAO().getCurrentRoomLiversNumber(roomId);
    }
}
