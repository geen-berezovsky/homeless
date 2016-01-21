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

import ru.homeless.entities.Client;
import ru.homeless.entities.ContractControl;
import ru.homeless.entities.Document;
import ru.homeless.entities.ServContract;

@Repository
public class ContractDAO extends GenericDAO implements IContractDAO {

	public static Logger log = Logger.getLogger(ContractDAO.class);

    @SuppressWarnings("unchecked")
    public List<ContractControl> getContractControlPointsByServContractId(int servContractId) {
            List<ContractControl> list = getSessionFactory().getCurrentSession().createCriteria(ContractControl.class).add(Restrictions.eq("servcontract", servContractId)).list();
            if (list != null && list.size() > 0) {
                return list;
            } else {
                return null;
            }

    }

    @SuppressWarnings("unchecked")
    public Document getWorkerDocumentForContractByWorkerId(int workerId) {
        List<Document> list = getSessionFactory().getCurrentSession().createCriteria(Document.class).add(Restrictions.eq("worker",workerId)).list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return new Document();
        }
    }

    @SuppressWarnings("unchecked")
    public Document getClientDocumentForContractByContractId(int contractId) {
        /*
        List<Document> list = getSessionFactory().getCurrentSession().createCriteria(Document.class).add(Restrictions.eq("docNum",String.valueOf(contractId))).list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            //May be it was the contract id, test it
            list = getSessionFactory().getCurrentSession().createCriteria(Document.class).add(Restrictions.eq("id",contractId)).list();
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        }
        return new Document();
        */
        ServContract contract = getInstanceById(ServContract.class, contractId);
        Document document = getInstanceById(Document.class, contract.getDocumentId());
        if (document != null) {
            return document;
        } else {
            return new Document();
        }
    }
}
