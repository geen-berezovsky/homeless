package ru.homeless.services;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.homeless.dao.ClientDAO;
import ru.homeless.entities.Client;

@Service("ClientService")
@Transactional(readOnly = false)
public class ClientService extends GenericService implements Serializable {

	private static final long serialVersionUID = 1L;
	@Autowired
	private ClientDAO clientDAO;

	public ClientDAO getClientDAO() {
		return clientDAO;
	}

	public void setClientDAO(ClientDAO clientDAO) {
		this.clientDAO = clientDAO;
	}
	
	@Transactional
	public List<Client> getClientsByCriteria(int id, String surname, String firstname, String middlename, String _date) {
		return getClientDAO().getClientsByCriteria(id, surname, firstname, middlename, _date);
	}

    public boolean setClientAvatar(Client client, byte[] resizedBytes) {
        return getClientDAO().setClientAvatar(client, resizedBytes);
    }
}
