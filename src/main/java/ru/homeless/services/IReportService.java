package ru.homeless.services;

import java.util.Date;
import java.util.List;

import ru.homeless.report.entities.OneTimeServicesReportEntity;
import ru.homeless.report.entities.OutOfShelterReportEntity;
import ru.homeless.report.entities.OuterReportEntity;
import ru.homeless.report.entities.OverVacReportEntity;
import ru.homeless.report.entities.ResultWorkReportEntity;


public interface IReportService extends IGenericService {

	public List<ResultWorkReportEntity> getResultWorkReport(Date from, Date till);
	
	public List<OutOfShelterReportEntity> getOutOfShelterReportEntity(Date from, Date till);
	
	public List<OneTimeServicesReportEntity> getOneTimeServicesReportEntity(Date from, Date till);
	
	public List<OverVacReportEntity> getOverVacReportEntity();
	
	public List<OuterReportEntity> getOuterReportEntity();

}
