package ru.homeless.services;

import java.util.List;

public interface IGenericService {
	
	public <T> void addInstance(T instance);
	public <T> void deleteInstance(T instance);
	public <T> void updateInstance(T instance);
	public <T> T getInstanceById(Class<T> clazz, int id);
	public <T> List<T> getInstances(Class<T> clazz);
	public <T> T getInstanceByCaption(Class<T> clazz, String caption);
	public <T> List<T> getInstancesByClientId(Class<T> clazz, int id);

}
