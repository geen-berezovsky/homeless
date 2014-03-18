package ru.homeless.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import ru.homeless.entities.ContractResult;
import ru.homeless.entities.ServContract;
import ru.homeless.util.HibernateUtil;

public class ClientControlResultDAO {

	
	public static Logger log = Logger.getLogger(ClientControlResultDAO.class);

	@SuppressWarnings("unchecked")
	public List<ContractResult> getAllContractResults() {
		Session session = null;
		List<ContractResult> results = new ArrayList<ContractResult>();
		session = HibernateUtil.getSession();
		try {
			results = session.createCriteria(ContractResult.class).list();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return results;
	}
	
	public ContractResult getContractResultById(int id) {
		Session session = null;
		ContractResult result = null;
		session = HibernateUtil.getSession();
		try {
			result = (ContractResult) session.createCriteria(ContractResult.class).add(Restrictions.eq("id", id)).list().get(0);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}

		
}
