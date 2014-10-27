package ru.homeless.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
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
        if (getCurrentRoomLiversNumber(id) > 0) {
            return false;
        } else {
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    public int getCurrentRoomLiversNumber(int roomId) {
        //get number of livers with status 'Living'
        Criteria c = getSessionFactory().getCurrentSession().createCriteria(ShelterHistory.class).add(Restrictions.eq("roomId",roomId)).add(Restrictions.eq("shelterresult",1));
        c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<ShelterHistory> list = c.list();
        return list.size();
    }

}
