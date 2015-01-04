package ru.homeless.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import ru.homeless.report.entities.ResultWorkReportEntity;
import ru.homeless.util.Util;

@Repository
public class ReportDAO extends GenericDAO implements IReportDAO {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ReportDAO.class);

	@Override
	public List<ResultWorkReportEntity> getResultWorkReport(Date from, Date till) {
		
		
		List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("SELECT DISTINCT w.surname, cp.caption, COUNT(sh.id), COUNT(c.id)-COUNT(sh.id) FROM ServContract sc"+
				" INNER JOIN Worker w ON (sc.worker = w.id) INNER JOIN ContractControl cc ON (sc.id=cc.servcontract)"+
				" INNER JOIN ContractPoints cp ON (cc.contractpoints=cp.id) INNER JOIN Client c ON (c.id=sc.client)"+
				" LEFT JOIN ShelterHistory sh ON (c.id = sh.client) WHERE cc.endDate is not null AND DATE(endDate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till) + " GROUP BY w.surname, cp.caption")
					.addScalar("w.surname")
					.addScalar("cp.caption")
					.addScalar("COUNT(sh.id)")
					.addScalar("COUNT(c.id)-COUNT(sh.id)").list();

		List<ResultWorkReportEntity> result = new ArrayList<ResultWorkReportEntity>();
		
		 for (Object o : res) {
			Object[] xy = (Object[])o;
			result.add(new ResultWorkReportEntity(xy[0].toString(), xy[1].toString(), Integer.parseInt(xy[2].toString()), Integer.parseInt(xy[3].toString())));
		 }
		return result;
	}

}