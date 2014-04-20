package ru.homeless.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

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

}
