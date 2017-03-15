package ru.homeless.dao;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.homeless.entities.ContractControl;
import ru.homeless.entities.Document;

import java.io.Serializable;
import java.util.List;

public interface IContractDAO extends IGenericDAO {

    public List<ContractControl> getContractControlPointsByServContractId(int servContractId);
    public Document getWorkerDocumentForContractByWorkerId(int workerId);
    public Document getClientDocumentForContractByContractId(int contractId);
}
