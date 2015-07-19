package ru.homeless.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.homeless.dao.IReportDAO;
import ru.homeless.report.entities.*;

@Service("ReportService")
@Transactional(readOnly = false)
public class ReportService extends GenericService implements IReportService {

	@Autowired
	private IReportDAO reportDAO;

	@Override
	public List<ResultWorkReportEntity> getResultWorkReport(Date from, Date till) {
		return reportDAO.getResultWorkReport(from, till);
	}

	@Override
	public List<OutOfShelterReportEntity> getOutOfShelterReportEntity(Date from, Date till) {
		return reportDAO.getOutOfShelterReport(from, till);
	}

	@Override
	public List<OneTimeServicesReportEntity> getOneTimeServicesReportEntity(Date from, Date till) {
		return reportDAO.getOneTimeServicesReport(from, till);
	}

	@Override
	public List<OverVacReportEntity> getOverVacReportEntity() {
		return reportDAO.getOverVacReport();
	}

	@Override
	public List<OuterReportEntity> getOuterReportEntity() {
		return reportDAO.getOuterReport();
	}

    @Override
    public List<InnerReportEntity> getInnerReportEntity() {
        return reportDAO.getInnerReport();
    }

    @Override
    public List<OuterReportEntity> getEvictedReportEntity() {
        return null;
    }


}
