package ru.homeless.dao;

import java.io.Serializable;
import java.util.List;
import java.util.PropertyPermission;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.homeless.beans.BasicDocumentBean;
import ru.homeless.entities.BasicDocumentRegistry;
import ru.homeless.entities.Document;

@Repository
public class WorkerDAO extends GenericDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(WorkerDAO.class);

	@SuppressWarnings("unchecked")
	public Document getWorkerDocumentById(int id) {
		List<Document> list = getSessionFactory().getCurrentSession().createCriteria(Document.class).add(Restrictions.eq("worker", id)).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

    public int getMaxBaseDocumentRegistryId() {
        DetachedCriteria maxId = DetachedCriteria.forClass(BasicDocumentRegistry.class).setProjection(Projections.max("id"));
        List<BasicDocumentRegistry> list = getSessionFactory().getCurrentSession().createCriteria(BasicDocumentRegistry.class).add(Property.forName("id").eq(maxId)).list();
        if (list != null && list.size() > 0) {
            return list.get(0).getId();
        } else {
            return 0;
        }
    }


}
