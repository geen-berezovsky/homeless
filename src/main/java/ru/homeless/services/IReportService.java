package ru.homeless.services;

import java.util.Date;
import java.util.List;

import ru.homeless.report.entities.ResultWorkReportEntity;


public interface IReportService extends IGenericService {

	public List<ResultWorkReportEntity> getResultWorkReport(Date from, Date till);

}
