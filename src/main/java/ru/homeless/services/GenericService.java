package ru.homeless.services;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.homeless.dao.GenericDAO;

@Service("GenericService")
@Transactional(readOnly = false)
public class GenericService implements IGenericService, Serializable {

	private static final long serialVersionUID = 1L;
	@Autowired
	private GenericDAO genericDAO;

	public GenericDAO getGenericDAO() {
		return genericDAO;
	}

	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	@Override
	@Transactional
	public <T> void addInstance(T instance) {
		getGenericDAO().addInstance(instance);
	}

	@Override
	@Transactional
	public <T> void deleteInstance(T instance) {
		getGenericDAO().deleteInstance(instance);
	}

	@Override
	@Transactional
	public <T> void updateInstance(T instance) {
		getGenericDAO().updateInstance(instance);
	}

	@Override
	@Transactional
	public <T> T getInstanceById(Class<T> clazz, int id) {
		return getGenericDAO().getInstanceById(clazz, id);
	}

	@Override
	@Transactional
	public <T> List<T> getInstances(Class<T> clazz) {
		return getGenericDAO().getInstances(clazz);
	}

	@Override
	@Transactional
	public <T> T getInstanceByCaption(Class<T> clazz, String caption) {
		return getGenericDAO().getInstanceByCaption(clazz, caption);
	}

	@Override
	@Transactional
	public <T> List<T> getInstancesByClientId(Class<T> clazz, int id) {
		return getGenericDAO().getInstancesByClientId(clazz, id);
	}
	

}
