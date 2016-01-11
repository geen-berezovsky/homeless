package ru.homeless.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ru.homeless.entities.Client;
import ru.homeless.entities.ContractControl;
import ru.homeless.services.IGenericService;

@Repository
public class GenericDAO implements IGenericService, Serializable {

	private static final long serialVersionUID = 1L;

	public static Logger log = Logger.getLogger(GenericDAO.class);

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public <T> void addInstance(T instance) {
		getSessionFactory().getCurrentSession().saveOrUpdate(instance);
	}

	public <T> void deleteInstance(T instance) {
		getSessionFactory().getCurrentSession().delete(instance);
	}

	public <T> void updateInstance(T instance) {
		getSessionFactory().getCurrentSession().saveOrUpdate(instance);
	}

	public <T> T getInstanceById(Class<T> clazz, int id) {
		List<T> list = getSessionFactory().getCurrentSession().createCriteria(clazz).add(Restrictions.eq("id", id)).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public <T> List<T> getInstances(Class<T> clazz) {
		return getSessionFactory().getCurrentSession().createCriteria(clazz).list();
	}

	public <T> T getInstanceByCaption(Class<T> clazz, String caption) {
		List<T> list = getSessionFactory().getCurrentSession().createCriteria(clazz).add(Restrictions.eq("caption", caption)).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public <T> List<T>getInstancesByClientId(Class<T> clazz, int id) {
		Criteria c = getSessionFactory().getCurrentSession().createCriteria(clazz).add(Restrictions.eq("client", id)); 
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return c.list();
	}

    @Override
    public <T> List<T> getInstancesByClientId(Class<T> clazz, Client client) {
        Criteria c = getSessionFactory().getCurrentSession().createCriteria(clazz).add(Restrictions.eq("client", client));
        c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return c.list();
    }

}
