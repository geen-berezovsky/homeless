package ru.homeless.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import ru.homeless.report.entities.OneTimeServicesReportEntity;
import ru.homeless.report.entities.OutOfShelterReportEntity;
import ru.homeless.report.entities.OuterReportEntity;
import ru.homeless.report.entities.OverVacReportEntity;
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
			 for (int i=0; i<=3; i++) {
					if (xy[i] == null) {
						xy[i] = new String("-");
					}
			 }
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
			
			for (int i=0; i<=10; i++) {
				if (xy[i] == null) {
					xy[i] = new String("-");
				}
			}
			result.add(new OutOfShelterReportEntity(xy[0].toString(), xy[1].toString(), Util.parseDateForReport((Date)xy[2]), Util.html2text(xy[3].toString()),
					Util.html2text(xy[4].toString()), xy[5].toString(), Util.parseDateForReport((Date)xy[6]), Util.parseDateForReport((Date)xy[7]),
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
			for (int i=0; i<=1; i++) {
				if (xy[i] == null) {
					xy[i] = new String("-");
				}
			}
			comb.add(new OneTimeServicesReportEntity(xy[0].toString(), xy[1].toString()));
		}
		List<?> res2 = getSessionFactory().getCurrentSession().createSQLQuery("SELECT CONCAT(w.firstname, ' ', w.surname) as S_NAME, s.caption as S_TYPE  FROM Tranzit t LEFT JOIN  Worker w ON(t.n_worker=w.id) LEFT JOIN ServicesType s ON(s.id=IF(t.id IS NOT NULL, '100', t.id) )  WHERE (t.servdate > " 
				+ Util.parseDateForMySql(from) + " AND t.servdate < " + Util.parseDateForMySql(till) + ")")
				.addScalar("S_NAME")
				.addScalar("S_TYPE").list();
		for (Object o : res2) {
			Object[] xy = (Object[])o;
			for (int i=0; i<=1; i++) {
				if (xy[i] == null) {
					xy[i] = new String("-");
				}
			}
			comb.add(new OneTimeServicesReportEntity(xy[0].toString(), xy[1].toString()));
		}
		List<?> res3 = getSessionFactory().getCurrentSession().createSQLQuery("SELECT CONCAT(w.firstname, ' ', w.surname) as S_NAME, s.caption as S_TYPE FROM RecievedService r LEFT JOIN Worker w ON(r.worker=w.id) LEFT JOIN ServicesType s" +
				" ON(r.servicesType=s.id) WHERE (r.date > " 
				+ Util.parseDateForMySql(from) + " AND r.date < " + Util.parseDateForMySql(till) + ")")
				.addScalar("S_NAME")
				.addScalar("S_TYPE").list();
		for (Object o : res3) {
			Object[] xy = (Object[])o;
			
			for (int i=0; i<=1; i++) {
				if (xy[i] == null) {
					xy[i] = new String("-");
				}
			}
			comb.add(new OneTimeServicesReportEntity(xy[0].toString(), xy[1].toString()));
		}
		return comb;
	}

	@Override
	public List<OverVacReportEntity> getOverVacReport() {
		List<OverVacReportEntity> result = new ArrayList<OverVacReportEntity>();
		
		List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("SELECT c.id, CONCAT(c.surname, ' ', c.firstname, ' ', c.middlename), c.date, sh.roomId, sh.inShelter, sh.outShelter, w.surname, sh.fluorogr, " +
				"sh.typhVac, sh.dipthVac, sh.hepotitsVac FROM (SELECT * FROM ShelterHistory WHERE roomId<>0) sh LEFT JOIN Client c " +
				"ON (sh.client = c.id) LEFT JOIN (SELECT * FROM ServContract WHERE contractresult=1) sc ON (c.id=sc.client) LEFT JOIN Worker w " +
				"ON (sc.worker = w.id)")
					.addScalar("c.id")
					.addScalar("CONCAT(c.surname, ' ', c.firstname, ' ', c.middlename)")
					.addScalar("c.date")
					.addScalar("sh.roomId")
					.addScalar("sh.inShelter")
					.addScalar("sh.outShelter")
					.addScalar("w.surname")
					.addScalar("sh.fluorogr")
					.addScalar("sh.typhVac")
					.addScalar("sh.dipthVac")
					.addScalar("sh.hepotitsVac").list();
		
		for (Object o : res) {
			Object[] xy = (Object[])o;
			
			for (int i=0; i<=10; i++) {
				if (xy[i] == null) {
					xy[i] = new String("-");
				}
			}
			
			result.add(new OverVacReportEntity(xy[0].toString(), xy[1].toString(), Util.parseDateForReport((Date)xy[2]), xy[3].toString(),
					Util.parseDateForReport((Date)xy[4]), Util.parseDateForReport((Date)xy[5]), xy[6].toString(), Util.parseDateForReport((Date)xy[7]),
					Util.parseDateForReport((Date)xy[8]), Util.parseDateForReport((Date)xy[9]), Util.parseDateForReport((Date)xy[10])));
		 }
		return result;
	}

	@Override
	public List<OuterReportEntity> getOuterReport() {
		List<OuterReportEntity> result = new ArrayList<OuterReportEntity>();
		List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("SELECT c.id, CONCAT(c.surname, '\n', c.firstname, '\n', c.middlename) as clName, c.date, sc.startDate, cp.caption,"+
				" cc.endDate, sc.stopDate, IFNULL(cc.comments, '-'), IFNULL(c.memo, '-'), w.surname, sh.id FROM"+
				" ServContract sc INNER JOIN Client c ON(sc.client = c.id) INNER JOIN Worker w ON(sc.worker = w.id)"+
				" INNER JOIN ContractControl cc ON(cc.ServContract=sc.id) INNER JOIN ContractPoints cp ON(cp.id = cc.contractpoints)"+
				" LEFT JOIN ShelterHistory sh ON(c.id = sh.client) WHERE (sc.contractresult='1' AND (sh.id is null OR sh.outShelter < CURDATE()))")
					.addScalar("c.id")
					.addScalar("clName")
					.addScalar("c.date")
					.addScalar("sc.startDate")
					.addScalar("cp.caption")
					.addScalar("cc.endDate")
					.addScalar("sc.stopDate")
					.addScalar("IFNULL(cc.comments, '-')")
					.addScalar("IFNULL(c.memo, '-')")
					.addScalar("w.surname").list();
		for (Object o : res) {
			Object[] xy = (Object[])o;
			
			for (int i=0; i<=9; i++) {
				if (xy[i] == null && i!=2 && i!=3 && i!=5 && i!=6) {
					xy[i] = new String("");
				}
			}
			result.add(new OuterReportEntity(xy[0].toString(), xy[1].toString(), Util.parseDateForReport((Date)xy[2]), Util.parseDateForReport((Date)xy[3]),
					xy[4].toString(), Util.parseDateForReport((Date)xy[5]), Util.parseDateForReport((Date)xy[6]),
					Util.html2text(xy[7].toString()), Util.html2text(xy[8].toString()), xy[9].toString()));
		 }
		return result;
	}

}