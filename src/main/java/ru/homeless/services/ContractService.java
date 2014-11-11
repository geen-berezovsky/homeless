package ru.homeless.services;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.homeless.dao.ContractDAO;
import ru.homeless.dao.IContractDAO;
import ru.homeless.entities.Client;
import ru.homeless.entities.ContractControl;
import ru.homeless.entities.Document;

@Service("ContractService")
@Transactional(readOnly = false)
public class ContractService extends GenericService implements IContractService {

	@Autowired
	private IContractDAO contractDAO;

	@Transactional
    public List<ContractControl> getContractControlPointsByServContractId(int servContractId) {
        return contractDAO.getContractControlPointsByServContractId(servContractId);
    }

    @Override
    public Document getWorkerDocumentForContractByWorkerId(int workerId) {
        return contractDAO.getWorkerDocumentForContractByWorkerId(workerId);
    }

    @Override
    public Document getClientDocumentForContractByContractId(int contractId) {
        return contractDAO.getClientDocumentForContractByContractId(contractId);
    }

}
