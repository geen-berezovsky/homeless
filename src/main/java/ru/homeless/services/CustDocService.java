package ru.homeless.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.homeless.dao.CustDocDAO;
import ru.homeless.dao.StdDocDAO;
import ru.homeless.entities.BasicDocumentRegistry;
import ru.homeless.entities.CustDocEntity;

import java.io.Serializable;
import java.util.List;

@Service("CustDocService")
@Transactional(readOnly = false)
public class CustDocService extends GenericService implements Serializable {

	private static final long serialVersionUID = 1L;

    @Autowired
    private CustDocDAO custDocDAO;

    @Transactional
    public List<CustDocEntity> getCustDocEntitiesList(int id) {
        return getCustDocDAO().getCustDocEntitiesList(id);
    }

    public CustDocDAO getCustDocDAO() {
        return custDocDAO;
    }

    public void setCustDocDAO(CustDocDAO custDocDAO) {
        this.custDocDAO = custDocDAO;
    }
}
