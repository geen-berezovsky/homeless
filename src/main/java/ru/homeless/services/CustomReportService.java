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

    @Override
    public CustomStatisticsReportEntity getReportDataByEducation(Date from, Date till) {
        return customReportDAO.getReportDataByEducation(from, till);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByChilds(Date from, Date till) {
        return customReportDAO.getReportDataByChilds(from, till);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByStudentsOrNot(Date from, Date till) {
        return customReportDAO.getReportDataByStudentsOrNot(from, till);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByProfession(Date from, Date till) {
        return customReportDAO.getReportDataByProfession(from, till);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByLiveInFlat(Date from, Date till) {
        return customReportDAO.getReportDataByLiveInFlat(from, till);
    }


}
