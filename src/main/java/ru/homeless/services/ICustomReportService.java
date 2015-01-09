package ru.homeless.services;

import java.util.Date;

import ru.homeless.report.entities.OldSchoolReportEntity;



public interface ICustomReportService extends IGenericService {

	public OldSchoolReportEntity getReportDataByGender(Date from, Date till);
	
}
