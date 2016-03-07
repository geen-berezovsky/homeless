package ru.homeless.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import ru.homeless.entities.Room;
import ru.homeless.report.entities.*;


public interface IReportDAO extends IGenericDAO {

	public List<ResultWorkReportEntity> getResultWorkReport(Date from, Date till);
	
	public List<OutOfShelterReportEntity> getOutOfShelterReport(Date from, Date till);
	
	public List<OneTimeServicesReportEntity> getOneTimeServicesReport(Date from, Date till);

    public Map<Room, List<OverVacReportEntity>> getOverVacReport();
	
	public List<OuterReportEntity> getOuterReport();

    public List<InnerReportEntity> getInnerReport();

}
