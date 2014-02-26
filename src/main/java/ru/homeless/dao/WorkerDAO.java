package ru.homeless.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;

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
