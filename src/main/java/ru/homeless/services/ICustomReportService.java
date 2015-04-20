package ru.homeless.services;

import java.util.Date;

import ru.homeless.report.entities.CustomStatisticsReportEntity;



public interface ICustomReportService extends IGenericService {

	public CustomStatisticsReportEntity getReportDataByGender(Date from, Date till);
	
	public CustomStatisticsReportEntity getReportDataByMartialStatus(Date from, Date till);
	
	
	
}
