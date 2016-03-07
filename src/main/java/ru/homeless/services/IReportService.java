package ru.homeless.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import ru.homeless.entities.Room;
import ru.homeless.report.entities.*;


public interface IReportService extends IGenericService {

	public List<ResultWorkReportEntity> getResultWorkReport(Date from, Date till);
	
	public List<OutOfShelterReportEntity> getOutOfShelterReportEntity(Date from, Date till);
	
	public List<OneTimeServicesReportEntity> getOneTimeServicesReportEntity(Date from, Date till);

	public Map<Room, List<OverVacReportEntity>> getOverVacReportEntity();
	
	public List<OuterReportEntity> getOuterReportEntity();

    public List<InnerReportEntity> getInnerReportEntity();

}
