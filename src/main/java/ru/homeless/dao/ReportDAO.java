package ru.homeless.dao;

import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import ru.homeless.entities.Room;
import ru.homeless.report.entities.*;
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

            String releaseDate = Util.parseDateForReport(xy[9]);
            if (releaseDate.equals("")) {
                releaseDate = "-";
            }

			result.add(new OutOfShelterReportEntity(xy[0].toString(), xy[1].toString(), Util.parseDateForReport(xy[2]), Util.html2text(xy[3].toString()),
					Util.html2text(xy[4].toString()), xy[5].toString(), Util.parseDateForReport(xy[6]), Util.parseDateForReport(xy[7]),
					xy[8].toString(), releaseDate, xy[10].toString()));
		 }
		return result;
	}

	@Override
	public List<OneTimeServicesReportEntity> getOneTimeServicesReport(Date from, Date till) {
		List<OneTimeServicesReportEntity> comb = new ArrayList<OneTimeServicesReportEntity>();
		
		List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("SELECT CONCAT(w.firstname, ' ', w.surname) as S_NAME, s.caption as S_TYPE FROM BasicDocumentRegistry g LEFT JOIN Worker w ON(g.performerId=w.id) LEFT JOIN BasicDocumentRegistryType s ON(s.id=g.type) WHERE (g.date >= "
				+ Util.parseDateForMySql(from) + " AND g.date <= " + Util.parseDateForMySql(till) + ")")
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

		List<?> res3 = getSessionFactory().getCurrentSession().createSQLQuery("SELECT CONCAT(w.firstname, ' ', w.surname) as S_NAME, s.caption as S_TYPE FROM RecievedService r LEFT JOIN Worker w ON(r.worker=w.id) LEFT JOIN ServicesType s" +
				" ON(r.servicesType=s.id) WHERE (r.date >= "
				+ Util.parseDateForMySql(from) + " AND r.date <= " + Util.parseDateForMySql(till) + ")")
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
	public Map<Room, List<OverVacReportEntity>> getOverVacReport() {
/*
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
*/

        Map<Room, List<OverVacReportEntity>> resMapByRoom = new TreeMap<>();

        //For each room prepare the list of people living there and add to the global list
        for (Room room : getInstances(Room.class)) {
            List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("" +
                    "select  concat(c.surname,' ',c.firstname,' ',c.middlename) as 'FIO', date_format(c.date,'%d.%m.%Y') as 'DR', " +
                    "    date_format(sh.inShelter,'%d.%m.%Y') as 'INS', date_format(sh.outShelter,'%d.%m.%Y') as 'OUTS'," +
                    "    concat(left(w.surname,1),left(w.firstname,1)) as 'WORKER', date_format(sh.fluorogr,'%d.%m.%Y') as 'FLG'," +
                    "    date_format(sh.hepotitsVac,'%d.%m.%Y') as 'HEP', date_format(sh.dipthVac,'%d.%m.%Y') as 'DIFT'," +
                    "    date_format(sh.typhVac,'%d.%m.%Y') as 'TYPTH', client_points.points as 'COMMENTS'" +
                    " from ShelterHistory sh left join Client c on sh.client = c.id left join ServContract sc on sh.client = sc.client left join Worker w on sc.worker = w.id " +
                    "left join (select ServContract.client client, group_concat(ContractPoints.abbreviation separator ', ') points from ContractControl, ContractPoints, ServContract where ServContract.id = ContractControl.servcontract and ServContract.contractresult =1 and ContractPoints.id=ContractControl.contractpoints group by ServContract.client) client_points on (client_points.client = c.id)" +
                    "where sh.roomId = "+room.getId()+" and sh.shelterresult = 1 and sc.contractresult =1;")
                    .addScalar("FIO")
                    .addScalar("DR")
                    .addScalar("INS")
                    .addScalar("OUTS")
                    .addScalar("WORKER")
                    .addScalar("FLG")
                    .addScalar("HEP")
                    .addScalar("DIFT")
                    .addScalar("TYPTH")
                    .addScalar("COMMENTS").list();

                    List<OverVacReportEntity> result = new ArrayList<OverVacReportEntity>();

            for (Object o : res) {
                Object[] xy = (Object[]) o;

                for (int i = 0; i < 10; i++) {
                    if (xy[i] == null) {
                        xy[i] = new String("");
                    }
                        }


            result.add(new OverVacReportEntity(xy[0].toString(), xy[1].toString(), xy[2].toString(), xy[3].toString(),
                                xy[4].toString(), xy[5].toString(), xy[6].toString(), xy[7].toString(),
                                xy[8].toString(), xy[9].toString()));
            }
            resMapByRoom.put(room, result);

        }


		return resMapByRoom;
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
			result.add(new OuterReportEntity(xy[0].toString(), xy[1].toString(), Util.parseDateForReport(xy[2]), Util.parseDateForReport(xy[3]),
					xy[4].toString(), Util.parseDateForReport(xy[5]), Util.parseDateForReport(xy[6]),
					Util.html2text(xy[7].toString()), Util.html2text(xy[8].toString()), xy[9].toString()));
		 }
		return result;
	}

    @Override
    public List<InnerReportEntity> getInnerReport() {
        List<InnerReportEntity> result = new ArrayList<InnerReportEntity>();
        List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("SELECT c.id, CONCAT(c.surname, '\n', c.firstname, '\n', c.middlename) as clName, c.date, sh.roomId, sh.inShelter, cp.caption, " +
                "cc.endDate, sh.outShelter, IFNULL(cc.comments, '-'), IFNULL(c.memo, '-'), w.surname " +
                "FROM ServContract sc INNER JOIN Client c ON(sc.client = c.id) " +
                "INNER JOIN Worker w ON(sc.worker = w.id) INNER JOIN ContractControl cc ON(cc.servcontract=sc.id) " +
                "INNER JOIN ContractPoints cp ON(cp.id = cc.contractpoints) LEFT JOIN ShelterHistory sh ON(c.id = sh.client) WHERE (sc.contractresult='1' AND sh.outShelter > CURDATE())")
                .addScalar("c.id")
                .addScalar("clName")
                .addScalar("c.date")
                .addScalar("IFNULL(c.memo, '-')")
                .addScalar("sh.roomId")
                .addScalar("sh.inShelter")
                .addScalar("sh.outShelter")
                .addScalar("cp.caption")
                .addScalar("cc.endDate")
                .addScalar("IFNULL(cc.comments, '-')")
                .addScalar("w.surname").list();

        for (Object o1 : res) {
            Object[] row = (Object[])o1;
            InnerReportEntity innerReportEntity = new InnerReportEntity();
            for(int i = 0; i<11; i++) {
                if (row[i] == null) {
                    row[i] = new String("");
                }
            }

            innerReportEntity.setClientId(row[0].toString());
            innerReportEntity.setName(Util.html2text(row[1].toString()));
            innerReportEntity.setDateOfBith(Util.parseDateForReport(row[2]));
            innerReportEntity.setComments(Util.html2text(row[3].toString()));
            innerReportEntity.setRoom(row[4].toString());
            innerReportEntity.setStartDate(Util.parseDateForReport(row[5]));
            innerReportEntity.setStopDate(Util.parseDateForReport(row[6]));
            innerReportEntity.setPurposes(row[7].toString());
            innerReportEntity.setReleaseDate(Util.parseDateForReport(row[8]));
            innerReportEntity.setReleaseSteps(Util.html2text(row[9].toString()));
            innerReportEntity.setWorkerSurname(row[10].toString());
            result.add(innerReportEntity);
        }

        return result;

    }


}