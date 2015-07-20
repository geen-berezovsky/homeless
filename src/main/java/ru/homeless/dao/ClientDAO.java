package ru.homeless.dao;

import java.io.Serializable;
import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.homeless.entities.*;
import ru.homeless.util.Util;

@Repository
public class ClientDAO extends GenericDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ClientDAO.class);

    public List<MyClientsEntity> getMyContracts(int workerId, Date startDate, Date endDate) {
        List<ServContract> contracts = new ArrayList<ServContract>();
        List<MyClientsEntity> myClientsEntities = new ArrayList<MyClientsEntity>();
        Criteria c = null;

        if (startDate == null && endDate == null) {
            c = getSessionFactory().getCurrentSession().createCriteria(ServContract.class).add(Restrictions.eq("result", getInstanceById(ContractResult.class, 1))).add(Restrictions.eq("worker", getInstanceById(Worker.class, workerId)));
        } else {
            c = getSessionFactory().getCurrentSession().createCriteria(ServContract.class).add(Restrictions.ne("result", getInstanceById(ContractResult.class, 1))).add(Restrictions.eq("worker", getInstanceById(Worker.class, workerId))).add(Restrictions.between("startDate",startDate, endDate));
        }
        c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        contracts  = c.list();

        for (ServContract sc : contracts) {
            MyClientsEntity myClientsEntity = new MyClientsEntity();
            Client client = getInstanceById(Client.class, sc.getClient());
            myClientsEntity.setId(client.getId()); //client id
            myClientsEntity.setContractNum(sc.getId());
            myClientsEntity.setFirstname(client.getFirstname());
            myClientsEntity.setMiddlename(client.getMiddlename());
            myClientsEntity.setSurname(client.getSurname());
            myClientsEntity.setDate(Util.formatDate(client.getDate()));
            myClientsEntity.setCreatedDate(Util.formatDate(sc.getStartDate()));
            myClientsEntity.setEndDate(Util.formatDate(sc.getStopDate()));
            myClientsEntity.setContractResult(sc.getResult());
            myClientsEntities.add(myClientsEntity);
        }

        return myClientsEntities;
    }


	@SuppressWarnings("unchecked")
	public List<Client> getClientsByCriteria(int id, String surname, String firstname, String middlename, String _date) {
        List<Client> clients = new ArrayList<Client>();
		if (id != 0) {
			Client c = (Client) getSessionFactory().getCurrentSession().createCriteria(Client.class).add(Restrictions.eq("id", id)).uniqueResult();
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

    public boolean setClientAvatar(Client client, byte[] resizedBytes) {
        Blob avatar = Hibernate.getLobCreator(getSessionFactory().getCurrentSession()).createBlob(resizedBytes);
        client.setAvatar(avatar);
        try {
            updateInstance(client);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
