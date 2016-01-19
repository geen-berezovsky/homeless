package ru.homeless.services;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.homeless.dao.ClientDAO;
import ru.homeless.entities.*;
import ru.homeless.entities.ContractResult.PredefinedValues;
import ru.homeless.util.Util;

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

    @Transactional
    public boolean setClientAvatar(Client client, byte[] resizedBytes) {
        return getClientDAO().setClientAvatar(client, resizedBytes);
    }

    @Transactional
    public List<MyClientsEntity> getMyContracts(int workerId, Date startDate, Date endDate) {
        return  getClientDAO().getMyContracts(workerId, startDate, endDate);
    }

    @Transactional
    public List<ShelterHistory> getShelterEndsBefore(Date dateToEnd){
    	return getClientDAO().getShelterEndsBefore(dateToEnd);
    }

    @Transactional
    public List<ShelterHistory> getEndedShelterAndNotLeaving(){
        return getClientDAO().getEndedShelterAndNotLeaving();
    }
    
    @Transactional
    public List<ShelterHistory> getIntersetionWithActiveShelters(Client client, ShelterHistory sH){
        return getClientDAO().getActiveSheltersForClient(client);
    }

    @Transactional
    public List<SubRegion> getSubRegionsByRegion(Region region) {
        return getClientDAO().getSubRegionsByRegion(region);
    }

    @Transactional
    public int getCountOfServContracts() {
        return getClientDAO().getCountOfServContracts();
    }

    @Transactional
    public void closeContact(ServContract selectedContract){
        //update from G.Sverdlin: "If the contract is successfully finalized, set the endDate for all subitems"
        if ( isContractSuccessefullyCompleted(selectedContract) ) {
            Set<ContractControl> set = selectedContract.getContractcontrols();
            for (ContractControl cc : set) {
                cc.setEndDate(selectedContract.getStopDate());
            }
        }
        closeActiveSheltersForContract(selectedContract);
    }
    
    private boolean isContractSuccessefullyCompleted(ServContract selectedContract){
        return PredefinedValues.SUCCESSEFULLY_COMPLETED.isSame(selectedContract.getResult());
    }
    
    private void closeActiveSheltersForContract(ServContract selectedContrac){
        List<ShelterHistory> activeShelters = getClientDAO().getActiveSheltersForContract(selectedContrac);//selectedContract.get
        for (ShelterHistory sh: activeShelters){
            sh.setShelterresult(ShelterResult.Results.LEAVE_NORMALLY.getId());
            getClientDAO().updateInstance(sh);
        }
    }
}
