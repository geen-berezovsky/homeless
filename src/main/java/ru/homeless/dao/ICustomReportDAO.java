package ru.homeless.dao;

import java.util.Date;
import java.util.Map;

import ru.homeless.report.entities.OldSchoolReportEntity;

public interface ICustomReportDAO extends IGenericDAO {

	public OldSchoolReportEntity getReportDataByGender(Date from, Date till);
	
	
	
}
