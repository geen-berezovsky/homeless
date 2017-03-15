package ru.homeless.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.homeless.dao.ContractDAO;
import ru.homeless.entities.ContractControl;
import ru.homeless.entities.Document;

import java.io.Serializable;
import java.util.List;

public interface IContractService extends IGenericService {

    public List<ContractControl> getContractControlPointsByServContractId(int servContractId);
    public Document getWorkerDocumentForContractByWorkerId(int workerId);
    public Document getClientDocumentForContractByContractId(int contractId);
}
