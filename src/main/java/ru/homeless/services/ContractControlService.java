package ru.homeless.services;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.homeless.dao.ContractControlDAO;
import ru.homeless.dao.WorkerDAO;
import ru.homeless.entities.ContractControl;
import ru.homeless.entities.Document;
import ru.homeless.entities.Worker;

@Service("ContractControlService")
@Transactional(readOnly = false)
public class ContractControlService extends GenericService implements Serializable {

	private static final long serialVersionUID = 1L;
	@Autowired
	private ContractControlDAO contractControlDAO;

	@Transactional
	public List<ContractControl> getItemsByServContractId(int id) {
		return getContractControlDAO().getItemsByServContractId(id);
	}

	public ContractControlDAO getContractControlDAO() {
		return contractControlDAO;
	}

	public void setContractContrlDAO(ContractControlDAO contractContrlDAO) {
		this.contractControlDAO = contractContrlDAO;
	}

}
