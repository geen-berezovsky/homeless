package ru.homeless.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ru.homeless.entities.Breadwinner;
import ru.homeless.entities.ChronicDisease;
import ru.homeless.entities.Education;
import ru.homeless.entities.Reasonofhomeless;
import ru.homeless.util.HibernateUtil;

public class ChronicDiseaseDAO {
	public static Logger log = Logger.getLogger(ChronicDiseaseDAO.class);

	@SuppressWarnings("unchecked")
	public List<ChronicDisease> getAllChronicDiseaseTypes() {
		Session session = null;
		List<ChronicDisease> diseases = new ArrayList<ChronicDisease>();
		session = HibernateUtil.getSession();
		try {
			diseases = session.createCriteria(ChronicDisease.class).list();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return diseases;
	}

}
