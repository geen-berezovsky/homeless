package ru.homeless.dao;

import static ru.homeless.util.Util.getCurDateDaysOnly;

import java.io.Serializable;
import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.homeless.entities.*;
import ru.homeless.util.Util;

@Repository
public class ClientDAO extends GenericDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ClientDAO.class);

    public List<MyClientsEntity> getMyContracts(int workerId, Date from, Date to) {
        Criteria c = null;
        if (from == null && to == null) {
            c = createCreteiaContractsForWorker(workerId);
        } else {
            c = createCreteiaContractsForWorker(workerId).add(Restrictions.between("startDate",from, to));
        }
        return getMyContractsByCriteria(c);
    }
    
    private Criteria createCreteiaContractsForWorker(int workerId){
    	return getSessionFactory().getCurrentSession().createCriteria(ServContract.class).add(Restrictions.eq("result", getInstanceById(ContractResult.class, 1))).add(Restrictions.eq("worker", getInstanceById(Worker.class, workerId)));
    }

    /**
     * Return contracts for criteria 
     * @param workerId
     * @param dateToend
     * @return
     */
    private List<MyClientsEntity> getMyContractsByCriteria(Criteria c){
        List<MyClientsEntity> myClientsEntities = new ArrayList<MyClientsEntity>();
        c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        @SuppressWarnings("unchecked")
		List<ServContract> contracts  = c.list();

        for (ServContract sc : contracts) {
            MyClientsEntity myClientsEntity = new MyClientsEntity();
            Client client = getInstanceById(Client.class, sc.getClient());
            myClientsEntity.setId(client.getId()); //client id
            myClientsEntity.setContractNum(sc.getId());
            myClientsEntity.setFirstname(client.getFirstname());
            myClientsEntity.setMiddlename(client.getMiddlename());
            myClientsEntity.setSurname(client.getSurname());
            myClientsEntity.setDate(Util.formatDate(client.getDate()));
            myClientsEntity.setCreatedDate(Util.formatDate(sc.getStartDate()));
            myClientsEntity.setEndDate(Util.formatDate(sc.getStopDate()));
            myClientsEntity.setContractResult(sc.getResult());
            myClientsEntities.add(myClientsEntity);
        }

        return myClientsEntities;
    }

	@SuppressWarnings("unchecked")
	public List<Client> getClientsByCriteria(int id, String surname, String firstname, String middlename, String _date) {
        List<Client> clients = new ArrayList<Client>();
		if (id != 0) {
			Client c = (Client) getSessionFactory().getCurrentSession().createCriteria(Client.class).add(Restrictions.eq("id", id)).uniqueResult();
			clients.add(c);
		} else {
			if (!_date.equals("")) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				Date date = null;
				try {
					date = formatter.parse(_date);
				} catch (ParseException e) {
					log.error("Cannot parse date " + _date);
				}
				log.info("date not null"+date);
				Criteria c = getSessionFactory().getCurrentSession().createCriteria(Client.class).add(Restrictions.like("surname", "%" + surname + "%"))
						.add(Restrictions.like("firstname", "%" + firstname + "%")).add(Restrictions.like("middlename", "%" + middlename + "%"))
						.add(Restrictions.eq("date", date));
				c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				clients  = c.list();
			} else {
				Criteria c = getSessionFactory().getCurrentSession().createCriteria(Client.class).add(Restrictions.like("surname", "%" + surname + "%"))
						.add(Restrictions.like("firstname", "%" + firstname + "%"))
						.add(Restrictions.like("middlename", "%" + middlename + "%"));
				c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
					clients  = c.list();
			}
		}
		return clients ;
	}

    public boolean setClientAvatar(Client client, byte[] resizedBytes) {
        Blob avatar = Hibernate.getLobCreator(getSessionFactory().getCurrentSession()).createBlob(resizedBytes);
        client.setAvatar(avatar);
        try {
            updateInstance(client);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    /**
     * Returns info about shelter ended from current date before <b>dateToEnd</b>. Including both dates.
     * @param workerId
     * @param dateToEnd
     * @return
     */
    public List<ShelterHistory> getShelterEndsBefore(Date dateToEnd){
    	Criteria shelterInfoCriteria = getSessionFactory().getCurrentSession().createCriteria(ShelterHistory.class);

        shelterInfoCriteria.add(Restrictions.ge("outShelter", getCurDateDaysOnly()));
        shelterInfoCriteria.add(Restrictions.le("outShelter", dateToEnd));
    	shelterInfoCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    	return shelterInfoCriteria.list();
    }
    
    /**
     * Returns ShelterHistory for clients, who should leave before today, but shelter status is living.
     * @return
     */
    public List<ShelterHistory> getEndedShelterAndNotLeaving(){
    	Criteria shelterInfoCriteria = getSessionFactory().getCurrentSession().createCriteria(ShelterHistory.class);
    	
        shelterInfoCriteria.add(Restrictions.le("outShelter", getCurDateDaysOnly()));
        shelterInfoCriteria.add(Restrictions.eq("shelterresult", ru.homeless.entities.ShelterResult.Results.LIVING.getId()));
        
    	shelterInfoCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    	return shelterInfoCriteria.list();
    }
    
    /**
     * Returns shelters info for client in this period
     * @param workerId
     * @param dateToEnd
     * @return
     */
    public List<ShelterHistory> getActiveShelters(Client client){
        Criteria shelterInfoCriteria = getSessionFactory().getCurrentSession().createCriteria(ShelterHistory.class);

        shelterInfoCriteria.add(Restrictions.eq("client", client));
        
        shelterInfoCriteria.add(Restrictions.eq("shelterresult", ru.homeless.entities.ShelterResult.Results.LIVING.getId()));
        shelterInfoCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return shelterInfoCriteria.list();
    }

    /**
    * Returns list of SubRegion by Region id.
    * @return
    */
    public List<SubRegion> getSubRegionsByRegion(Region region) {
        Criteria subregions = getSessionFactory().getCurrentSession().createCriteria(SubRegion.class).add(Restrictions.eq("region",region));
        subregions.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return subregions.list();
    }

    /**
     * New contract number is the count(*) unique records from existing table ServContract + 1
     * This method returns count(*) from ServContract only
     * @return
     */
    public int getCountOfServContracts() {
        return ((Long)getSessionFactory().getCurrentSession().createQuery("select count(*) from ServContract ").uniqueResult()).intValue();
    }


    public List<ShelterHistory> getActiveSheltersForContract(ServContract contract){
        Criteria shelterInfoCriteria = getSessionFactory().getCurrentSession().createCriteria(ShelterHistory.class);

        shelterInfoCriteria.add(Restrictions.eq("servContract", contract));        
        shelterInfoCriteria.add(Restrictions.eq("shelterresult", ru.homeless.entities.ShelterResult.Results.LIVING.getId()));
        
        shelterInfoCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return shelterInfoCriteria.list();
    }

    public boolean hasBeenRegistered(int clientId) {
        Criteria registryDocCriteria = getSessionFactory().getCurrentSession().createCriteria(BasicDocumentRegistry.class);
        BasicDocumentRegistryType request = getInstanceById(BasicDocumentRegistryType.class,11);
        registryDocCriteria.add(Restrictions.eq("client", clientId));
        registryDocCriteria.add(Restrictions.eq("type", request));
        registryDocCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (registryDocCriteria.list().size()>0) {
            return true;
        } else {
            return false;
        }
    }

}
