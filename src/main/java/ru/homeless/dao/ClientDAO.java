package ru.homeless.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import ru.homeless.entities.Breadwinner;
import ru.homeless.entities.Client;
import ru.homeless.util.HibernateUtil;

public class ClientDAO {

	public static Logger log = Logger.getLogger(ClientDAO.class);
	private List<Client> clients = null;

	@SuppressWarnings("unchecked")
	public List<Client> getClientsByCriteria(int id, String surname, String firstname, String middlename, String _date) {
		Session session = HibernateUtil.getSession();

		if (id != 0) {
			Client c = (Client) session.createCriteria(Client.class).add(Restrictions.eq("id", id)).uniqueResult();
			if (clients == null) {
				clients = new ArrayList<Client>();
			}
			clients.add(c);
		} else {
			if (!_date.equals("")) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				Date date = null;
				try {
					date = formatter.parse(_date);
				} catch (ParseException e) {
					log.error("Cannot parse date " + _date);
				}
				log.info("date not null"+date);
				Criteria c = session.createCriteria(Client.class).add(Restrictions.like("surname", "%" + surname + "%"))
						.add(Restrictions.like("firstname", "%" + firstname + "%")).add(Restrictions.like("middlename", "%" + middlename + "%"))
						.add(Restrictions.eq("date", date));
				c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				clients  = c.list();
			} else {
				Criteria c = session.createCriteria(Client.class).add(Restrictions.like("surname", "%" + surname + "%"))
						.add(Restrictions.like("firstname", "%" + firstname + "%"))
						.add(Restrictions.like("middlename", "%" + middlename + "%"));
				c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
					clients  = c.list();
			}
		}

		if (session != null && session.isOpen()) {
			session.close();
		}

		return clients ;
	}

	public Client getClientById(int cid) {
		Session session = HibernateUtil.getSession();
		Client c = (Client) session.createCriteria(Client.class).add(Restrictions.eq("id", cid)).uniqueResult();
		if (session != null && session.isOpen()) {
			session.close();
		}
		return c;
	}

	public Map<Boolean,String> updateClientData(Client client) {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		HashMap<Boolean, String> map = new HashMap<Boolean, String>();
		try {
			tx = session.beginTransaction();
			session.update(client);
			tx.commit();
			if (tx.wasCommitted()) {
				log.info("Client "+client.getId()+" has been successfully updated");
				map.put(true, "Клиент "+client.getId()+" успешно обновлен");
			} else {
				log.warn("Transaction of saving client "+client.getId()+" data haven't been committed");
				map.put(false, "Не могу закоммитить транзакцию для клиента "+client.getId()+"! Откатываемся!");
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

}
