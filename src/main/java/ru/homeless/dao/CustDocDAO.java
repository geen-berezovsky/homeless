package ru.homeless.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.homeless.entities.*;
import ru.homeless.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustDocDAO extends GenericDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(CustDocDAO.class);

    @SuppressWarnings("unchecked")
    public List<CustDocEntity> getCustDocEntitiesList(int id) {

        List<CustDocEntity> entities = new ArrayList<>();

        // CUSTOM DOCUMENT REGISTRY

        List<CustomDocumentRegistry> list = getInstancesByClientId(CustomDocumentRegistry.class, id);
        int iterator = 0;
        for (CustomDocumentRegistry customDocumentRegistry : list) {
            CustDocEntity entity = new CustDocEntity();
            entity.setObject(customDocumentRegistry);
            entity.setDocNum(customDocumentRegistry.getDocNum());
            entity.setType("Стандартный документ");
            Worker worker = (Worker) getSessionFactory().getCurrentSession().createCriteria(Worker.class).add(Restrictions.eq("id", customDocumentRegistry.getPerformerId())).list().get(0);
            entity.setWorker(worker.getFirstname()+" "+worker.getSurname());
            entity.setId(iterator);
            entity.setIssueDate(Util.formatDate(customDocumentRegistry.getDate()));
            entities.add(entity);
            iterator++;
        }

        // ZAGS DOCUMENT REGISTRY

        List<ZAGSRequestDocumentRegistry> list2 = getInstancesByClientId(ZAGSRequestDocumentRegistry.class, id);
        for (ZAGSRequestDocumentRegistry zagsRequestDocumentRegistry : list2) {
            CustDocEntity entity = new CustDocEntity();
            entity.setObject(zagsRequestDocumentRegistry);
            entity.setDocNum("");
            entity.setType("Запрос в ЗАГС");
            Worker worker = (Worker) getSessionFactory().getCurrentSession().createCriteria(Worker.class).add(Restrictions.eq("id", zagsRequestDocumentRegistry.getPerformerId())).list().get(0);
            entity.setWorker(worker.getFirstname()+" "+worker.getSurname());
            entity.setId(iterator);
            entity.setIssueDate(Util.formatDate(zagsRequestDocumentRegistry.getDate()));
            entities.add(entity);
            iterator++;
        }


        return entities;
    }

}
