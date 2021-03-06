package ru.homeless.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.homeless.dao.OverdueDAO;
import ru.homeless.entities.OverdueItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("OverdueService")
@Transactional(readOnly = false)
public class OverdueService extends GenericService implements Serializable {

    private static final long serialVersionUID = 1L;

    public void setOverdueDAO(OverdueDAO overdueDAO) {
        this.overdueDAO = overdueDAO;
    }

    @Autowired
    private OverdueDAO overdueDAO;


    @Transactional
    public List<OverdueItem> getOverdueItems() {
        return overdueDAO.getOverdueItems();
    }
}
