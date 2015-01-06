package ru.homeless.dao;

import java.util.Date;
import java.util.List;

import ru.homeless.report.entities.OneTimeServicesReportEntity;
import ru.homeless.report.entities.OutOfShelterReportEntity;
import ru.homeless.report.entities.OuterReportEntity;
import ru.homeless.report.entities.OverVacReportEntity;
import ru.homeless.report.entities.ResultWorkReportEntity;


public interface IReportDAO extends IGenericDAO {

	public List<ResultWorkReportEntity> getResultWorkReport(Date from, Date till);
	
	public List<OutOfShelterReportEntity> getOutOfShelterReport(Date from, Date till);
	
	public List<OneTimeServicesReportEntity> getOneTimeServicesReport(Date from, Date till);
	
	public List<OverVacReportEntity> getOverVacReport();
	
	public List<OuterReportEntity> getOuterReport();
	
}
