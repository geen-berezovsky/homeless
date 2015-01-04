package ru.homeless.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import ru.homeless.report.entities.OneTimeServicesReportEntity;
import ru.homeless.report.entities.OutOfShelterReportEntity;
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

	@Override
	public List<OutOfShelterReportEntity> getOutOfShelterReport(Date from, Date till) {
		List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("SELECT c.id, c.surname, c.date, sh.roomId, sh.inShelter, cp.caption, cc.endDate,  " +
				"sh.outShelter, IFNULL(c.memo, '-'), IFNULL(c.contacts, '-'), w.surname, sh.id " +
				"FROM ServContract sc INNER JOIN Client c ON(sc.client = c.id) " +
				"INNER JOIN Worker w ON(sc.worker = w.id) INNER JOIN ContractControl cc ON(cc.servcontract=sc.id) " +
				"INNER JOIN ContractPoints cp ON(cp.id = cc.contractpoints) LEFT JOIN ShelterHistory sh ON(c.id = sh.client) WHERE (DATE(sh.outShelter) BETWEEN " 
				+ Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till) + ")")
				.addScalar("c.id")
				.addScalar("c.surname")
				.addScalar("c.date")
				.addScalar("IFNULL(c.contacts, '-')")
				.addScalar("IFNULL(c.memo, '-')")
				.addScalar("sh.roomId")
				.addScalar("sh.inShelter")
				.addScalar("sh.outShelter")
				.addScalar("cp.caption")
				.addScalar("cc.endDate")
				.addScalar("w.surname").list();
		
		List<OutOfShelterReportEntity> result = new ArrayList<OutOfShelterReportEntity>();
		
		for (Object o : res) {
			Object[] xy = (Object[])o;
			result.add(new OutOfShelterReportEntity(xy[0].toString(), xy[1].toString(), xy[2].toString(), xy[3].toString(),
					xy[4].toString(), xy[5].toString(), xy[6].toString(), xy[7].toString(),
					xy[8].toString(), xy[9].toString(), xy[10].toString()));
		 }
		return result;
	}

	@Override
	public List<OneTimeServicesReportEntity> getOneTimeServicesReport(Date from, Date till) {
		List<OneTimeServicesReportEntity> comb = new ArrayList<OneTimeServicesReportEntity>();
		
		List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("SELECT CONCAT(w.firstname, ' ', w.surname) as S_NAME, s.caption as S_TYPE FROM GivenCertificate g LEFT JOIN Worker w ON(g.worker=w.id) LEFT JOIN ServicesType s ON(s.id=g.type) WHERE (g.date > "
				+ Util.parseDateForMySql(from) + " AND g.date < " + Util.parseDateForMySql(till) + ")")
				.addScalar("S_NAME")
				.addScalar("S_TYPE").list();
		for (Object o : res) {
			Object[] xy = (Object[])o;
			comb.add(new OneTimeServicesReportEntity(xy[0].toString(), xy[1].toString()));
		}
		List<?> res2 = getSessionFactory().getCurrentSession().createSQLQuery("SELECT CONCAT(w.firstname, ' ', w.surname) as S_NAME, s.caption as S_TYPE  FROM Tranzit t LEFT JOIN  Worker w ON(t.n_worker=w.id) LEFT JOIN ServicesType s ON(s.id=IF(t.id IS NOT NULL, '100', t.id) )  WHERE (t.servdate > " 
				+ Util.parseDateForMySql(from) + " AND t.servdate < " + Util.parseDateForMySql(till) + ")")
				.addScalar("S_NAME")
				.addScalar("S_TYPE").list();
		for (Object o : res2) {
			Object[] xy = (Object[])o;
			comb.add(new OneTimeServicesReportEntity(xy[0].toString(), xy[1].toString()));
		}
		List<?> res3 = getSessionFactory().getCurrentSession().createSQLQuery("SELECT CONCAT(w.firstname, ' ', w.surname) as S_NAME, s.caption as S_TYPE FROM RecievedService r LEFT JOIN Worker w ON(r.worker=w.id) LEFT JOIN ServicesType s" +
				" ON(r.servicesType=s.id) WHERE (r.date > " 
				+ Util.parseDateForMySql(from) + " AND r.date < " + Util.parseDateForMySql(till) + ")")
				.addScalar("S_NAME")
				.addScalar("S_TYPE").list();
		for (Object o : res3) {
			Object[] xy = (Object[])o;
			comb.add(new OneTimeServicesReportEntity(xy[0].toString(), xy[1].toString()));
		}
		return comb;
	}

}