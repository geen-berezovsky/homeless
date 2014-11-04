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
}
