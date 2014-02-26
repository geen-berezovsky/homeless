package ru.homeless.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ru.homeless.entities.Breadwinner;
import ru.homeless.entities.Education;
import ru.homeless.util.HibernateUtil;

public class BreadwinnerDAO {
	public static Logger log = Logger.getLogger(BreadwinnerDAO.class);

	@SuppressWarnings("unchecked")
	public List<Breadwinner> getAllBreadwinnerTypes() {
		Session session = null;
		List<Breadwinner> breadwinners = new ArrayList<Breadwinner>();
		session = HibernateUtil.getSession();
		try {
			breadwinners = session.createCriteria(Breadwinner.class).list();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return breadwinners;
	}

}
