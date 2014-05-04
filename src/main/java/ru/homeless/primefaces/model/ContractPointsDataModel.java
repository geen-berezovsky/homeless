package ru.homeless.primefaces.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import ru.homeless.entities.ContractPoints;

public class ContractPointsDataModel extends ListDataModel<ContractPoints> implements SelectableDataModel<ContractPoints> {
	
	public ContractPointsDataModel() {
		
	}
	
	public ContractPointsDataModel(List<ContractPoints> data) {
		super(data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ContractPoints getRowData(String arg0) {
		List<ContractPoints> data = (List<ContractPoints>) getWrappedData();
		for (ContractPoints c : data) {
			int val = -1;
			try {
				val = Integer.parseInt(arg0);
			} catch (NumberFormatException e) {
				//do nothing
			}
			if (c.getId() == val) {
				return c;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(ContractPoints arg0) {
		return arg0.getId();
	}

}
