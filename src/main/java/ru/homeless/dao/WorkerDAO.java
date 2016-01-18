package ru.homeless.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.PropertyPermission;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.homeless.beans.BasicDocumentBean;
import ru.homeless.entities.BasicDocumentRegistry;
import ru.homeless.entities.BasicDocumentRegistryType;
import ru.homeless.entities.Document;
import ru.homeless.util.Util;

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

    /**
     * This function includes only Basic documents and don't include Transit and Unknown types
     * @param from
     * @param till
     * @return
     */
    public String getDocNumNonTransit(Date from, Date till, int id) {
        //exclusions

         /*
            For each BasicDocumentRegistryType (except Transit) we need to take the count of existing documents such type and iterate it
            Then, the following part of the number will be count of all existing BasicDocumentRegistryType except Transit
         */
        BasicDocumentRegistryType transit = getInstanceById(BasicDocumentRegistryType.class,16);
        BasicDocumentRegistryType unknown = getInstanceById(BasicDocumentRegistryType.class,20);
        BasicDocumentRegistryType request = getInstanceById(BasicDocumentRegistryType.class,id);
        List<BasicDocumentRegistry> allDocumentsList = getSessionFactory().getCurrentSession().createCriteria(BasicDocumentRegistry.class)
                .add(Restrictions.ge("date", from))
                .add(Restrictions.le("date", till))
                .add(Restrictions.ne("type", transit))
                .add(Restrictions.ne("type", unknown)).list();

        List<BasicDocumentRegistry> allDocumentsThisID = getSessionFactory().getCurrentSession().createCriteria(BasicDocumentRegistry.class)
                .add(Restrictions.ge("date", from))
                .add(Restrictions.le("date", till))
                .add(Restrictions.eq("type", request)).list();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(allDocumentsThisID.size()+1);
        stringBuilder.append("/");
        stringBuilder.append(allDocumentsList.size()+1);
        return stringBuilder.toString();
/*
        String r1 = "select count(*) from BasicDocumentRegistry where date>="+Util.parseDateForMySql(from)+" and date <="+Util.parseDateForMySql(till)+ " and type <> "+16+" and type <> "+20;
        String r2 = "select count(*) from BasicDocumentRegistry where date>="+Util.parseDateForMySql(from)+" and date <="+Util.parseDateForMySql(till)+ " and type = "+id;

        System.out.println(r1);
        System.out.println(r2);

        int f1 = ((Long)getSessionFactory().getCurrentSession().createQuery(r1).uniqueResult()).intValue();
        int f2 = ((Long)getSessionFactory().getCurrentSession().createQuery(r2).uniqueResult()).intValue();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(f1+1);
        stringBuilder.append("/");
        stringBuilder.append(f2);
        return stringBuilder.toString();
*/
    }
/*
    public int getMaxBaseDocumentRegistryId() {


        DetachedCriteria maxId = DetachedCriteria.forClass(BasicDocumentRegistry.class).setProjection(Projections.max("id"));
        List<BasicDocumentRegistry> list = getSessionFactory().getCurrentSession().createCriteria(BasicDocumentRegistry.class).add(Property.forName("id").eq(maxId)).list();
        if (list != null && list.size() > 0) {
            return list.get(0).getId();
        } else {
            return 0;
        }
    }
    */

    public int getMaxBaseDocumentRegistryDocNumForTranzit() {

        List<BasicDocumentRegistryType> list0 = getSessionFactory().getCurrentSession().createCriteria(BasicDocumentRegistryType.class).add(Restrictions.eq("id", 16)).list();
        List<BasicDocumentRegistry> list = getSessionFactory().getCurrentSession().createCriteria(BasicDocumentRegistry.class).add(Restrictions.eq("type", list0.get(0))).list();
        int max = 0;
        if (list != null && list.size() > 0) {
            try {
                for (BasicDocumentRegistry b : list) {
                    if (! b.getDocNum().contains("/")) {
                        Integer val = Integer.parseInt(b.getDocNum());
                        if (val > max) {
                            max = val;
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e);
            }

            return max;
        } else {
            return 0;
        }
    }

}
