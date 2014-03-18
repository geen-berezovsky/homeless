package ru.homeless.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import ru.homeless.entities.ServContract;
import ru.homeless.util.HibernateUtil;

public class ClientContractsDAO {

	
	public static Logger log = Logger.getLogger(ClientContractsDAO.class);

	@SuppressWarnings("unchecked")
	public List<ServContract> getAllClientContracts(int cid) {
		Session session = null;
		List<ServContract> contracts = new ArrayList<ServContract>();
		session = HibernateUtil.getSession();
		try {
			contracts = session.createCriteria(ServContract.class).add(Restrictions.eq("client", cid)).list();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return contracts;
	}
	
	public ServContract getContractById(int id) {
		Session session = null;
		ServContract con = null;
		session = HibernateUtil.getSession();
		try {
			con = (ServContract) session.createCriteria(ServContract.class).add(Restrictions.eq("id", id)).list().get(0);
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

	
	public void updateContract(ServContract selectedContract) {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(selectedContract);
		tx.commit();
		if (session != null && session.isOpen()) {
			session.close();
		}
	}
	
	public void deleteContract(ServContract selectedContract) {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		session.delete(selectedContract);
		tx.commit();
		if (session != null && session.isOpen()) {
			session.close();
		}
	}

	
}
