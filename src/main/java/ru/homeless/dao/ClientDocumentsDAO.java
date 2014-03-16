package ru.homeless.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.primefaces.component.log.Log;

import ru.homeless.entities.Client;
import ru.homeless.entities.Document;
import ru.homeless.entities.RecievedService;
import ru.homeless.entities.ServicesType;
import ru.homeless.entities.Worker;
import ru.homeless.util.HibernateUtil;
import ru.homeless.util.Util;

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
	
	public Document getDocumentById(int id) {
		Session session = null;
		Document doc = null;
		session = HibernateUtil.getSession();
		try {
			doc = (Document) session.createCriteria(Document.class).add(Restrictions.eq("id", id)).list().get(0);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return doc;
	}

	
	public void updateDocument(Document selectedDocument) {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(selectedDocument);
		tx.commit();
		if (session != null && session.isOpen()) {
			session.close();
		}
	}
	
	public void deleteDocument(Document selectedDocument) {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		log.info("Deleting fucking document "+selectedDocument.getId());
		session.delete(selectedDocument);
		log.info("Deleted "+selectedDocument.getId());
		tx.commit();
		if (session != null && session.isOpen()) {
			session.close();
		}
	}

	

}
