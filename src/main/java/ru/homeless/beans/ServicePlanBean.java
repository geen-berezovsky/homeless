package ru.homeless.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import ru.homeless.entities.ContractControl;
import ru.homeless.services.ContractControlService;

@ManagedBean (name = "serviceplan")
@ViewScoped
public class ServicePlanBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{ContractControlService}")
	private ContractControlService contractControlService;
	
	private ContractControl serviceplanitem;
	
	public ServicePlanBean() {
		
	}

	public ContractControlService getContractControlService() {
		return contractControlService;
	}

	public void setContractControlService(ContractControlService contractControlService) {
		this.contractControlService = contractControlService;
	}

	public ContractControl getServiceplanitem() {
		return serviceplanitem;
	}

	public void setServiceplanitem(ContractControl serviceplanitem) {
		this.serviceplanitem = serviceplanitem;
	}
	
	

}
