package ru.homeless.services;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.homeless.dao.WorkerDAO;
import ru.homeless.entities.Document;
import ru.homeless.entities.Worker;

@Service("WorkerService")
@Transactional(readOnly = false)
public class WorkerService extends GenericService implements Serializable {

	private static final long serialVersionUID = 1L;
	@Autowired
	private WorkerDAO workerDAO;

	@Transactional
	public Document getWorkerDocumentById(int id) {
		return getWorkerDAO().getWorkerDocumentById(id);
	}

	public WorkerDAO getWorkerDAO() {
		return workerDAO;
	}

	public void setWorkerDAO(WorkerDAO workerDAO) {
		this.workerDAO = workerDAO;
	}

	@Transactional
	public Worker login(String name, String password) {
		/*
		 * Note: name = firstname + surname
		 */
        List<Worker> workers = getInstances(Worker.class);
		for (Worker w : workers) {
            if ((w.getFirstname() + " " + w.getSurname()).equals(name) && DigestUtils.sha1Hex(password).equals(w.getPassword())) {
				return w;
			}
		}
		return null;
	}
/*
    public int getMaxBaseDocumentRegistryId() {
        return workerDAO.getMaxBaseDocumentRegistryId();
    }
*/
    public int getMaxBaseDocumentRegistryDocNumForTranzit() {
        return workerDAO.getMaxBaseDocumentRegistryDocNumForTranzit();
    }

    public String getDocNumNonTransit(Date from, Date till, int id) {
        return workerDAO.getDocNumNonTransit(from, till, id);
    }
}
