package ru.homeless.dao;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.homeless.entities.Client;

@Repository
public class ClientDAO extends GenericDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ClientDAO.class);
	private List<Client> clients = null;
	@SuppressWarnings("unchecked")
	public List<Client> getClientsByCriteria(int id, String surname, String firstname, String middlename, String _date) {
		
		if (id != 0) {
			Client c = (Client) getSessionFactory().getCurrentSession().createCriteria(Client.class).add(Restrictions.eq("id", id)).uniqueResult();
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
				Criteria c = getSessionFactory().getCurrentSession().createCriteria(Client.class).add(Restrictions.like("surname", "%" + surname + "%"))
						.add(Restrictions.like("firstname", "%" + firstname + "%")).add(Restrictions.like("middlename", "%" + middlename + "%"))
						.add(Restrictions.eq("date", date));
				c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				clients  = c.list();
			} else {
				Criteria c = getSessionFactory().getCurrentSession().createCriteria(Client.class).add(Restrictions.like("surname", "%" + surname + "%"))
						.add(Restrictions.like("firstname", "%" + firstname + "%"))
						.add(Restrictions.like("middlename", "%" + middlename + "%"));
				c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
					clients  = c.list();
			}
		}
		return clients ;
	}
}
