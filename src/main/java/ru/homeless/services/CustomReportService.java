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
    public CustomStatisticsReportEntity getReportDataByDependencies(Date from, Date till) {
        return  customReportDAO.getReportDataByDependencies(from, till);
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

    @Override
    public CustomStatisticsReportEntity getReportDataBySeeRelatives(Date from, Date till) {
        return customReportDAO.getReportDataBySeeRelatives(from, till);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByWhereSleeping(Date from, Date till) {
        return customReportDAO.getReportDataByWhereSleeping(from, till);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByHomelessReasons(Date from, Date till) {
        return customReportDAO.getReportDataByHomelessReasons(from, till);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByChronicalDiseasters(Date from, Date till) {
        return customReportDAO.getReportDataByChronicalDiseasters(from, till);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByBreadwinnersAll(Date from, Date till) {
        return customReportDAO.getReportDataByBreadwinnersAll(from, till);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByBreadwinnersAdults(Date from, Date till) {
        return customReportDAO.getReportDataByBreadwinnersChilds(from, till);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByAge(Date from, Date till) {
        return customReportDAO.getReportDataByAge(from, till);
    }


}
