package ru.homeless.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.homeless.entities.BasicDocumentRegistry;
import ru.homeless.entities.BasicDocumentRegistryType;
import ru.homeless.entities.ShelterHistory;

import java.io.Serializable;
import java.util.List;

@Repository
public class StdDocDAO extends GenericDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(StdDocDAO.class);

    @SuppressWarnings("unchecked")
    public List<BasicDocumentRegistry> getStandardDocumentsList(int id) {
        Criteria c = getSessionFactory().getCurrentSession().createCriteria(BasicDocumentRegistry.class).add(Restrictions.eq("client", id)).add(Restrictions.not(Restrictions.eq("type", getInstanceById(BasicDocumentRegistryType.class,16)))).add(Restrictions.not(Restrictions.eq("type",getInstanceById(BasicDocumentRegistryType.class,1))));
        c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return c.list();
    }

    @SuppressWarnings("unchecked")
    public List<BasicDocumentRegistry> getTranzitDocumentsList(int id) {
        Criteria c = getSessionFactory().getCurrentSession().createCriteria(BasicDocumentRegistry.class).add(Restrictions.eq("client", id)).add(Restrictions.eq("type",getInstanceById(BasicDocumentRegistryType.class,16)));
        c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return c.list();
    }

}
