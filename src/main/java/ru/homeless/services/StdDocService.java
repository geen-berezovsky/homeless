package ru.homeless.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.homeless.dao.RoomDAO;
import ru.homeless.dao.StdDocDAO;
import ru.homeless.entities.BasicDocumentRegistry;

import java.io.Serializable;
import java.util.List;

@Service("StdDocService")
@Transactional(readOnly = false)
public class StdDocService extends GenericService implements Serializable {

	private static final long serialVersionUID = 1L;

    public StdDocDAO getStdDocDAO() {
        return stdDocDAO;
    }

    public void setStdDocDAO(StdDocDAO stdDocDAO) {
        this.stdDocDAO = stdDocDAO;
    }

    @Autowired
    private StdDocDAO stdDocDAO;

    @Transactional
    public List<BasicDocumentRegistry> getStandardDocumentsList(int id) {
        return getStdDocDAO().getStandardDocumentsList(id);
    }

    @Transactional
    public List<BasicDocumentRegistry> getTranzitDocumentsList(int id) {
        return getStdDocDAO().getTranzitDocumentsList(id);
    }

}
