package ru.homeless.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ru.homeless.entities.DocType;
import ru.homeless.util.HibernateUtil;

public class DocTypesDAO {
	public static Logger log = Logger.getLogger(DocTypesDAO.class);

	@SuppressWarnings("unchecked")
	public List<DocType> getAvailbleDocTypes() {
		Session session = null;
		List<DocType> docTypes = new ArrayList<DocType>();
		session = HibernateUtil.getSession();
		try {
			docTypes = session.createCriteria(DocType.class).list();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return docTypes;
	}

}
