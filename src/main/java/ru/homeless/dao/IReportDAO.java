package ru.homeless.dao;

import java.util.Date;
import java.util.List;

import ru.homeless.report.entities.ResultWorkReportEntity;


public interface IReportDAO extends IGenericDAO {

	public List<ResultWorkReportEntity> getResultWorkReport(Date from, Date till);
	
}
