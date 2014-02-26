package ru.homeless.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ru.homeless.entities.Breadwinner;
import ru.homeless.entities.Education;
import ru.homeless.entities.Reasonofhomeless;
import ru.homeless.util.HibernateUtil;

public class ReasonOfHomelessDAO {
	public static Logger log = Logger.getLogger(ReasonOfHomelessDAO.class);

	@SuppressWarnings("unchecked")
	public List<Reasonofhomeless> getAllReasonofhomelessTypes() {
		Session session = null;
		List<Reasonofhomeless> resasons = new ArrayList<Reasonofhomeless>();
		session = HibernateUtil.getSession();
		try {
			resasons = session.createCriteria(Reasonofhomeless.class).list();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return resasons;
	}

}
