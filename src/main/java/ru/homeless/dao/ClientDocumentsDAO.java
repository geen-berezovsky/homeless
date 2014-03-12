package ru.homeless.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ru.homeless.entities.Document;
import ru.homeless.util.HibernateUtil;

public class ClientDocumentsDAO {
	public static Logger log = Logger.getLogger(ClientDocumentsDAO.class);

	@SuppressWarnings("unchecked")
	public List<Document> getAllClientDocuments(int cid) {
		Session session = null;
		List<Document> documents = new ArrayList<Document>();
		session = HibernateUtil.getSession();
		try {
			documents = session.createCriteria(Document.class).add(Restrictions.eq("client", cid)).list();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return documents;
	}

}
