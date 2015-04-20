package ru.homeless.dao;

import java.util.Date;

import ru.homeless.report.entities.CustomStatisticsReportEntity;

public interface ICustomReportDAO extends IGenericDAO {

	public CustomStatisticsReportEntity getReportDataByGender(Date from, Date till);
	
	public CustomStatisticsReportEntity getReportDataByMartialStatus(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByDependencies(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByEducation(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByChilds(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByStudentsOrNot(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByProfession(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByLiveInFlat(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataBySeeFamily(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByNightStay(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByReasonOfHomeless(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByChronicDiseases(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByAllBw(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataAdultBw(Date from, Date till);

}
