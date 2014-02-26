package ru.homeless.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ru.homeless.entities.Education;
import ru.homeless.entities.NightStay;
import ru.homeless.util.HibernateUtil;

public class NightStayDAO {
	public static Logger log = Logger.getLogger(NightStayDAO.class);

	@SuppressWarnings("unchecked")
	public List<NightStay> getAllNightStayTypes() {
		Session session = null;
		List<NightStay> nightStays = new ArrayList<NightStay>();
		session = HibernateUtil.getSession();
		try {
			nightStays = session.createCriteria(NightStay.class).list();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return nightStays;
	}

}
