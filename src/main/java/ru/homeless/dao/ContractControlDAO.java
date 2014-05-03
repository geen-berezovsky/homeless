package ru.homeless.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.homeless.entities.ContractControl;
import ru.homeless.entities.Document;

@Repository
public class ContractControlDAO extends GenericDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ContractControlDAO.class);

	@SuppressWarnings("unchecked")
	public List<ContractControl> getItemsByServContractId(int id) {
		return getSessionFactory().getCurrentSession().createCriteria(ContractControl.class).add(Restrictions.eq("servcontract", id)).list();
	}

}
