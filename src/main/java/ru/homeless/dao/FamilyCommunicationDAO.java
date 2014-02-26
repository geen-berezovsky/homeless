package ru.homeless.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ru.homeless.entities.Education;
import ru.homeless.entities.FamilyCommunication;
import ru.homeless.entities.NightStay;
import ru.homeless.util.HibernateUtil;

public class FamilyCommunicationDAO {
	public static Logger log = Logger.getLogger(FamilyCommunicationDAO.class);

	@SuppressWarnings("unchecked")
	public List<FamilyCommunication> getAllFamilyCommunicationTypes() {
		Session session = null;
		List<FamilyCommunication> familyCommunications = new ArrayList<FamilyCommunication>();
		session = HibernateUtil.getSession();
		try {
			familyCommunications = session.createCriteria(FamilyCommunication.class).list();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return familyCommunications;
	}

}
