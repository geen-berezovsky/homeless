package ru.homeless.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.homeless.dao.ICustomReportDAO;
import ru.homeless.report.entities.CustomStatisticsReportEntity;

@Service("CustomReportService")
@Transactional(readOnly = false)
public class CustomReportService extends GenericService implements ICustomReportService {

	@Autowired
	private ICustomReportDAO customReportDAO;

	@Override
	public CustomStatisticsReportEntity getReportDataByGender(Date from, Date till) {
		return customReportDAO.getReportDataByGender(from, till);
	}

	@Override
	public CustomStatisticsReportEntity getReportDataByMartialStatus(Date from, Date till) {
		return customReportDAO.getReportDataByMartialStatus(from, till);
	}

	
}
