package ru.homeless.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.homeless.dao.IReportDAO;
import ru.homeless.report.entities.ResultWorkReportEntity;

@Service("ReportService")
@Transactional(readOnly = false)
public class ReportService extends GenericService implements IReportService {

	@Autowired
	private IReportDAO reportDAO;

	@Override
	public List<ResultWorkReportEntity> getResultWorkReport(Date from, Date till) {
		return reportDAO.getResultWorkReport(from, till);
	}
}
