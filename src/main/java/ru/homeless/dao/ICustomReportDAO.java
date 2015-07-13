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

    public CustomStatisticsReportEntity getReportDataBySeeRelatives(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByWhereSleeping(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByHomelessReasons(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByChronicalDiseasters(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByBreadwinnersAll(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByBreadwinnersChilds(Date from, Date till);

    public CustomStatisticsReportEntity getReportDataByAge(Date from, Date till);


}
