package ru.homeless.dao;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.homeless.entities.Document;
import ru.homeless.entities.Room;
import ru.homeless.entities.ShelterHistory;

import java.io.Serializable;
import java.util.List;

@Repository
public class RoomDAO extends GenericDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(RoomDAO.class);

    @SuppressWarnings("unchecked")
    public boolean isRoomReadyToBeDeleted(int id) {
        List<ShelterHistory> list = getSessionFactory().getCurrentSession().
                createCriteria(ShelterHistory.class).add(Restrictions.eq("roomId", id)).add(Restrictions.eq("shelterresult",1)).list();

        if (list != null && list.size() > 0) {
            return false;
        } else {
            return true;
        }

    }

}
