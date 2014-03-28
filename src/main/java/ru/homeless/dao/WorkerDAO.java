package ru.homeless.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import ru.homeless.entities.Client;
import ru.homeless.entities.Document;
import ru.homeless.entities.Worker;
import ru.homeless.util.HibernateUtil;
import ru.homeless.util.Util;

public class WorkerDAO {

	public static Logger log = Logger.getLogger(WorkerDAO.class);

	@SuppressWarnings("unchecked")
	public List<Worker> getAllWorkers() {
		Session session = null;
		List<Worker> workers = new ArrayList<Worker>();
		session = HibernateUtil.getSession();
		try {
			workers = session.createCriteria(Worker.class).list();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return workers;
	}
	
	@SuppressWarnings("unchecked")
	public Document getWorkerDocumentById(int id) {
		Session session = null;
		List<Document> documents = new ArrayList<Document>();
		session = HibernateUtil.getSession();
		try {
			documents = session.createCriteria(Document.class).add(Restrictions.eq("worker", id)).list();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		if (documents != null && documents.size() > 0) {
			return documents.get(0);
		} else {
			return null;
		}
	}
	
	public <T> Map<Boolean,String> updateItem(T item) {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		HashMap<Boolean, String> map = new HashMap<Boolean, String>();
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(item);
			tx.commit();
			if (tx.wasCommitted()) {
				log.info(item.getClass().getSimpleName()+" has been successfully updated");
				map.put(true, item.getClass().getSimpleName()+" успешно обновлен");
			} else {
				log.warn("Transaction of saving item "+item.getClass().getSimpleName()+" data haven't been committed");
				map.put(false, "Не могу закоммитить транзакцию для item "+item.getClass().getSimpleName()+"! Откатываемся!");
			}
		} catch (Exception e) {
			if (tx!=null) {
				tx.rollback();
			}
			log.error(e.getMessage());
			map.put(false, e.getMessage());
			return map;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return map;
	}

	
	
	
	public boolean login(String name, String password) {
		/*
		 * Note: name = firstname + surname
		 */
		
		List<Worker> workers = getAllWorkers();
		for (Worker w : workers) {
			if ((w.getFirstname()+" "+w.getSurname()).equals(name) && password.equals(w.getPassword())) {
				log.info(name+" has successfully logged in at "+new Date().toString());
				HttpSession session = Util.getSession();
				session.setAttribute("worker", w);
				return true;
			}
		}
		
		return false;

	}

}
