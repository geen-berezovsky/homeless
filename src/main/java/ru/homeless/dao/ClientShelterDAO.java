package ru.homeless.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import ru.homeless.entities.ServContract;
import ru.homeless.entities.ShelterHistory;
import ru.homeless.util.HibernateUtil;

public class ClientShelterDAO {

	
	public static Logger log = Logger.getLogger(ClientShelterDAO.class);

	@SuppressWarnings("unchecked")
	public List<ShelterHistory> getAllClientShelters(int cid) {
		Session session = null;
		List<ShelterHistory> shelters = new ArrayList<ShelterHistory>();
		session = HibernateUtil.getSession();
		try {
			shelters = session.createCriteria(ShelterHistory.class).add(Restrictions.eq("client", cid)).list();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return shelters;
	}
	
	public ShelterHistory getShelterById(int id) {
		Session session = null;
		ShelterHistory con = null;
		session = HibernateUtil.getSession();
		try {
			con = (ShelterHistory) session.createCriteria(ShelterHistory.class).add(Restrictions.eq("id", id)).list().get(0);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return con;
	}

	
	public void updateShelter(ShelterHistory selectedShelter) {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(selectedShelter);
		tx.commit();
		if (session != null && session.isOpen()) {
			session.close();
		}
	}
	
	public void deleteShelter(ShelterHistory selectedShelter) {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		session.delete(selectedShelter);
		tx.commit();
		if (session != null && session.isOpen()) {
			session.close();
		}
	}

	
	
}
