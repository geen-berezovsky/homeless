package ru.homeless.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.homeless.dao.IGenericDAO;

@Service("GenericService")
@Transactional(readOnly = false)
public class GenericService implements IGenericService {

	@Autowired
	private IGenericDAO genericDAO;
	
	@Override
	@Transactional
	public <T> void addInstance(T instance) {
		genericDAO.addInstance(instance);
	}

	@Override
	@Transactional
	public <T> void deleteInstance(T instance) {
		genericDAO.deleteInstance(instance);
	}

	@Override
	@Transactional
	public <T> void updateInstance(T instance) {
		genericDAO.updateInstance(instance);
	}

	@Override
	@Transactional
	public <T> T getInstanceById(Class<T> clazz, int id) {
		return genericDAO.getInstanceById(clazz, id);
	}

	@Override
	@Transactional
	public <T> List<T> getInstances(Class<T> clazz) {
		return genericDAO.getInstances(clazz);
	}

	@Override
	@Transactional
	public <T> T getInstanceByCaption(Class<T> clazz, String caption) {
		return genericDAO.getInstanceByCaption(clazz, caption);
	}

	@Override
	@Transactional
	public <T> List<T> getInstancesByClientId(Class<T> clazz, int id) {
		return genericDAO.getInstancesByClientId(clazz, id);
	}

	public IGenericDAO getGenericDAO() {
		return genericDAO;
	}

	public void setGenericDAO(IGenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}


}
