package ru.homeless.report.entities;

import java.util.Map;

public class CustomStatisticsReportEntity implements ICustomStatisticsReport {
	private int queryType;
	private Map<String, Integer> valueAndQuantity;
	
	public int getQueryType() {
		return queryType;
	}
	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}
	public Map<String, Integer> getValueAndQuantity() {
		return valueAndQuantity;
	}
	public void setValueAndQuantity(Map<String, Integer> valueAndQuantity) {
		this.valueAndQuantity = valueAndQuantity;
	}
}
