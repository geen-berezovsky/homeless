package ru.homeless.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import ru.homeless.util.HibernateUtil;

@Entity
@Table (name = "ContractResult")
public class ContractResult implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String caption;
	
	public ContractResult() {
		
	}
	
	public boolean equals(Object o) {
		if (o == null) return false;
		if (! (o instanceof ContractResult)) {
			return false;
		}
		ContractResult d = (ContractResult) o;
        if (new Integer(getId()).equals(new Integer(d.getId()))) {
			return true;
		} else {
			return false;	
		}
	}

	
	public ContractResult(String caption) {
		setCaption(caption);
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	@Override
	public String toString() {
		return caption;
	}
	
	public enum PredefinedValues{
		SUCCESSEFULLY_COMPLETED(2),
		IN_PROGRESS(1);
		
		PredefinedValues(int id){
			this.id = id;
		}
		private Integer id;
		
		public ContractResult getContractResult(){
			return (ContractResult) HibernateUtil.getSession().get(ContractResult.class, id);
		}
		
		public boolean isSame(ContractResult cr){
			return getContractResult().equals(cr);
		}
	}
}
