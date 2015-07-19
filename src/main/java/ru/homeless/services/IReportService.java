package ru.homeless.services;

import java.util.Date;
import java.util.List;

import ru.homeless.report.entities.*;


public interface IReportService extends IGenericService {

	public List<ResultWorkReportEntity> getResultWorkReport(Date from, Date till);
	
	public List<OutOfShelterReportEntity> getOutOfShelterReportEntity(Date from, Date till);
	
	public List<OneTimeServicesReportEntity> getOneTimeServicesReportEntity(Date from, Date till);
	
	public List<OverVacReportEntity> getOverVacReportEntity();
	
	public List<OuterReportEntity> getOuterReportEntity();

    public List<InnerReportEntity> getInnerReportEntity();

    public List<OuterReportEntity> getEvictedReportEntity();

}
