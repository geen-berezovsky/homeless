package ru.homeless.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ru.homeless.entities.Document;
import ru.homeless.entities.Worker;
import ru.homeless.util.HibernateUtil;
import ru.homeless.util.Util;

@Repository
public class WorkerDAO extends GenericDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(WorkerDAO.class);

	@SuppressWarnings("unchecked")
	public Document getWorkerDocumentById(int id) {

		List<Document> list = getSessionFactory().getCurrentSession().createCriteria(Document.class).add(Restrictions.eq("worker", id)).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public <T> Map<Boolean, String> updateItem(T item) {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		HashMap<Boolean, String> map = new HashMap<Boolean, String>();
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(item);
			tx.commit();
			if (tx.wasCommitted()) {
				log.info(item.getClass().getSimpleName() + " has been successfully updated");
				map.put(true, item.getClass().getSimpleName() + " успешно обновлен");
			} else {
				log.warn("Transaction of saving item " + item.getClass().getSimpleName() + " data haven't been committed");
				map.put(false, "Не могу закоммитить транзакцию для item " + item.getClass().getSimpleName() + "! Откатываемся!");
			}
		} catch (Exception e) {
			if (tx != null) {
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

}
