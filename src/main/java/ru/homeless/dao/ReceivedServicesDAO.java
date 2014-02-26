package ru.homeless.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import ru.homeless.entities.Client;
import ru.homeless.entities.RecievedService;
import ru.homeless.entities.ServicesType;
import ru.homeless.entities.Worker;
import ru.homeless.util.HibernateUtil;
import ru.homeless.util.Util;

public class ReceivedServicesDAO {
	public static Logger log = Logger.getLogger(ReceivedServicesDAO.class);

	@SuppressWarnings("unchecked")
	public List<RecievedService> getReceivedServices(int id) {
		Session session = HibernateUtil.getSession();
		List<RecievedService> services = null;
		services = session.createCriteria(RecievedService.class).add(Restrictions.eq("client", id)).list();
		if (session != null && session.isOpen()) {
			session.close();
		}
		return services;
	}

	@SuppressWarnings("unchecked")
	public List<ServicesType> getAvailableServiceTypes() {
		Session session = HibernateUtil.getSession();
		List<ServicesType> services = null;
		services = session.createCriteria(ServicesType.class).list();
		if (session != null && session.isOpen()) {
			session.close();
		}
		return services;
	}

	public void addReceivedService(Client c, ServicesType st, Date date) {
		Session session = HibernateUtil.getSession();
		
		Transaction tx = session.beginTransaction();
		
		HttpSession httpsession = Util.getSession();
		Worker w = (Worker) httpsession.getAttribute("worker");
		
		c.getRecievedservices().add(new RecievedService(w,c,st,date));
		
		session.update(c);
		tx.commit();

		if (session != null && session.isOpen()) {
			session.close();
		}
	}

	public void deleteReceivedService(RecievedService selectedService) {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		Client c = selectedService.getClient();
		log.info("Was "+c.getRecievedservices().size());
		
		c.getRecievedservices().remove(selectedService);
		session.update(c);

		tx.commit();
		log.info("Now "+c.getRecievedservices().size());
		
		if (session != null && session.isOpen()) {
			session.close();
		}
	
	}

	
}
