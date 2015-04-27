package ru.homeless.services;

import java.util.Date;

import ru.homeless.report.entities.CustomStatisticsReportEntity;



public interface ICustomReportService extends IGenericService {

	public CustomStatisticsReportEntity getReportDataByGender(Date from, Date till);
	
	public CustomStatisticsReportEntity getReportDataByMartialStatus(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByEducation(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByChilds(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByStudentsOrNot(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByProfession(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByLiveInFlat(Date from, Date till);
	
	
}
