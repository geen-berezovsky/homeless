package ru.homeless.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ru.homeless.entities.Education;
import ru.homeless.util.HibernateUtil;

public class EductionDAO {
	public static Logger log = Logger.getLogger(EductionDAO.class);

	@SuppressWarnings("unchecked")
	public List<Education> getAllEducationTypes() {
		Session session = null;
		List<Education> educactions = new ArrayList<Education>();
		session = HibernateUtil.getSession();
		try {
			educactions = session.createCriteria(Education.class).list();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return educactions;
	}

}
