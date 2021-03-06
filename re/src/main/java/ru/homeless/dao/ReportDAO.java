package ru.homeless.dao;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import ru.homeless.entities.Room;
import ru.homeless.report.entities.*;
import ru.homeless.util.Util;

import java.util.*;

@Repository
public class ReportDAO extends GenericDAO implements IReportDAO {

    private static final long serialVersionUID = 1L;

    @Override
    public List<ResultWorkReportEntity> getResultWorkReport(Date from, Date till) {
        Session s = getSessionFactory().getCurrentSession();
        return s.createSQLQuery(
                "SELECT "
                + "CONCAT(wk.firstname, ' ', wk.surname) AS workerSurname, "
                + "cp.caption AS contractPointsCaption, "
                + "NOT ISNULL(sh.id) AS isLivingInShelter, "
                + "COUNT(cc.id) AS tasksPerformed "
                + "FROM ContractControl cc "
                + "INNER JOIN ContractPoints cp ON cp.id = cc.contractpoints "
                + "INNER JOIN ServContract sc ON sc.id = cc.servcontract "
                + "INNER JOIN Worker wk ON sc.worker = wk.id "
                + "INNER JOIN Client cl ON cl.id = sc.client "
                + "LEFT JOIN ShelterHistory sh ON sh.client = sc.client AND cc.endDate BETWEEN sh.inShelter AND sh.outShelter "
                + "WHERE cc.endDate IS NOT NULL AND cc.endDate BETWEEN :fromDate AND :tillDate "
                + "GROUP BY wk.id, workerSurname, contractPointsCaption, cp.id, isLivingInShelter "
                + "ORDER BY workerSurname, contractPointsCaption, isLivingInShelter")
                .addScalar("workerSurname", StringType.INSTANCE)
                .addScalar("contractPointsCaption", StringType.INSTANCE)
                .addScalar("isLivingInShelter", BooleanType.INSTANCE)
                .addScalar("tasksPerformed", IntegerType.INSTANCE)
                .setDate("fromDate", from)
                .setDate("tillDate", till)
                .setResultTransformer(Transformers.aliasToBean(ResultWorkReportEntity.class))
                .list();
    }

    @Override
    public List<OutOfShelterReportEntity> getOutOfShelterReport(Date from, Date till) {
        List<OutOfShelterReportEntity> result = new ArrayList<>();
        List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("SELECT c.id, c.surname, c.date, sh.roomId, sh.inShelter, cp.caption, cc.endDate,  "
                + "sh.outShelter, IFNULL(c.memo, '-'), IFNULL(c.contacts, '-'), w.surname, sh.id "
                + "FROM ServContract sc INNER JOIN Client c ON(sc.client = c.id) "
                + "INNER JOIN Worker w ON(sc.worker = w.id) INNER JOIN ContractControl cc ON(cc.servcontract=sc.id) "
                + "INNER JOIN ContractPoints cp ON(cp.id = cc.contractpoints) LEFT JOIN ShelterHistory sh ON(c.id = sh.client) WHERE (DATE(sh.outShelter) BETWEEN "
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

        for (Object o : res) {
            Object[] xy = (Object[]) o;

            for (int i = 0; i <= 10; i++) {
                if (xy[i] == null) {
                    xy[i] = "-";
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
        List<OneTimeServicesReportEntity> comb = new ArrayList<>();

        List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("SELECT CONCAT(w.firstname, ' ', w.surname) as S_NAME, s.caption as S_TYPE FROM BasicDocumentRegistry g LEFT JOIN Worker w ON(g.performerId=w.id) LEFT JOIN BasicDocumentRegistryType s ON(s.id=g.type) WHERE (g.date >= "
                + Util.parseDateForMySql(from) + " AND g.date <= " + Util.parseDateForMySql(till) + ")")
                .addScalar("S_NAME")
                .addScalar("S_TYPE").list();
        for (Object o : res) {
            Object[] xy = (Object[]) o;
            for (int i = 0; i <= 1; i++) {
                if (xy[i] == null) {
                    xy[i] = "-";
                }
            }
            comb.add(new OneTimeServicesReportEntity(xy[0].toString(), xy[1].toString()));
        }

        List<?> res3 = getSessionFactory().getCurrentSession().createSQLQuery("SELECT CONCAT(w.firstname, ' ', w.surname) as S_NAME, s.caption as S_TYPE FROM RecievedService r LEFT JOIN Worker w ON(r.worker=w.id) LEFT JOIN ServicesType s"
                + " ON(r.servicesType=s.id) WHERE (r.date >= "
                + Util.parseDateForMySql(from) + " AND r.date <= " + Util.parseDateForMySql(till) + ")")
                .addScalar("S_NAME")
                .addScalar("S_TYPE").list();
        for (Object o : res3) {
            Object[] xy = (Object[]) o;

            for (int i = 0; i <= 1; i++) {
                if (xy[i] == null) {
                    xy[i] = "-";
                }
            }
            comb.add(new OneTimeServicesReportEntity(xy[0].toString(), xy[1].toString()));
        }
        return comb;
    }

    @Override
    public Map<Room, List<OverVacReportEntity>> getOverVacReport() {
        Map<Room, List<OverVacReportEntity>> resMapByRoom = new TreeMap<>();

        //For each room prepare the list of people living there and add to the global list
        for (Room room : getInstances(Room.class)) {
            List<?> res = getSessionFactory().getCurrentSession().createSQLQuery(""
                    + "select  c.id, concat(c.surname,' ',c.firstname,' ',c.middlename) as 'FIO', date_format(c.date,'%d.%m.%Y') as 'DR', "
                    + "    date_format(sh.inShelter,'%d.%m.%Y') as 'INS', date_format(sh.outShelter,'%d.%m.%Y') as 'OUTS',"
                    + "    concat(left(w.surname,1),left(w.firstname,1)) as 'WORKER', date_format(sh.fluorogr,'%d.%m.%Y') as 'FLG',"
                    + "    date_format(sh.hepotitsVac,'%d.%m.%Y') as 'HEP', date_format(sh.dipthVac,'%d.%m.%Y') as 'DIFT',"
                    + "    date_format(sh.typhVac,'%d.%m.%Y') as 'TYPTH', client_points.points as 'COMMENTS'"
                    + " from ShelterHistory sh left join Client c on sh.client = c.id left join ServContract sc on sh.client = sc.client left join Worker w on sc.worker = w.id "
                    + "left join (select ServContract.client client, group_concat(ContractPoints.abbreviation separator ', ') points from ContractControl, ContractPoints, ServContract where ServContract.id = ContractControl.servcontract and ServContract.contractresult =1 and ContractPoints.id=ContractControl.contractpoints group by ServContract.client) client_points on (client_points.client = c.id)"
                    + "where sh.roomId = " + room.getId() + " and sh.shelterresult = 1 and sc.contractresult =1;")
                    .addScalar("id")
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

            List<OverVacReportEntity> result = new ArrayList<>();

            for (Object o : res) {
                Object[] xy = (Object[]) o;

                for (int i = 0; i < 10; i++) {
                    if (xy[i] == null) {
                        xy[i] = "";
                    }
                }

                result.add(new OverVacReportEntity(Integer.parseInt(xy[0].toString()), xy[1].toString(), xy[2].toString(), xy[3].toString(), xy[4].toString(),
                        xy[5].toString(), xy[6].toString(), xy[7].toString(), xy[8].toString(),
                        xy[9].toString(), xy[10].toString()));
            }
            resMapByRoom.put(room, result);

        }

        return resMapByRoom;
    }

    @Override
    public List<ProvidedServicesByClientReportEntity> getProvidedServicesByClientReport(Date from, Date till) {
        List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("select rs.id, "
                + "concat(w.surname,' ',left(w.firstname,1),'.',left(w.middlename,1),'.') as 'WORKER',"
                + "    c.id as 'CLIENT_ID',"
                + "    concat(c.surname,' ',c.firstname,' ',c.middlename) as 'FIO',"
                + "    st.caption as 'SERVICE_TYPE',"
                + "    date_format(rs.date,'%d.%m.%Y') as 'DATE'"
                + " from RecievedService rs "
                + "left join Client c on rs.client=c.id"
                + "    left join Worker w on rs.worker=w.id "
                + "left join ServicesType st on rs.servicesType=st.id "
                + "where rs.date>=" + Util.parseDateForMySql(from) + " and rs.date<=" + Util.parseDateForMySql(till) + " order by rs.date;")
                .addScalar("id")
                .addScalar("WORKER")
                .addScalar("CLIENT_ID")
                .addScalar("FIO")
                .addScalar("SERVICE_TYPE")
                .addScalar("DATE").list();

        List<ProvidedServicesByClientReportEntity> result = new ArrayList<>();

        for (Object o : res) {
            Object[] xy = (Object[]) o;

            for (int i = 0; i < 6; i++) {
                if (xy[i] == null) {
                    xy[i] = "";
                }
            }

            result.add(new ProvidedServicesByClientReportEntity(Integer.parseInt(xy[0].toString()), xy[1].toString(), xy[2].toString(), xy[3].toString(), xy[4].toString(), xy[5].toString()));
        }

        //Then adding entities from BasicDocumentRegistry
        res = getSessionFactory().getCurrentSession().createSQLQuery("SELECT bdr.id, "
                + "concat(w.surname,' ',left(w.firstname,1),'.',left(w.middlename,1),'.') as 'WORKER', "
                + "    c.id as 'CLIENT_ID', "
                + "    concat(c.surname,' ',c.firstname,' ',c.middlename) as 'FIO', "
                + "    bdrt.caption as 'SERVICE_TYPE', "
                + "    date_format(bdr.date,'%d.%m.%Y') as 'DATE' "
                + "FROM BasicDocumentRegistry bdr "
                + "    LEFT JOIN Worker w ON bdr.performerId=w.id "
                + "    LEFT JOIN Client c ON bdr.client = c.id "
                + "    LEFT JOIN BasicDocumentRegistryType bdrt ON bdrt.id=bdr.type "
                + "    where bdr.date>=" + Util.parseDateForMySql(from) + " and bdr.date<=" + Util.parseDateForMySql(till) + " order by bdr.date;")
                .addScalar("id")
                .addScalar("WORKER")
                .addScalar("CLIENT_ID")
                .addScalar("FIO")
                .addScalar("SERVICE_TYPE")
                .addScalar("DATE").list();

        for (Object o : res) {
            Object[] xy = (Object[]) o;

            for (int i = 0; i < 6; i++) {
                if (xy[i] == null) {
                    xy[i] = "";
                }
            }

            result.add(new ProvidedServicesByClientReportEntity(Integer.parseInt(xy[0].toString()), xy[1].toString(), xy[2].toString(), xy[3].toString(), xy[4].toString(), xy[5].toString()));
        }

        return result;
    }

    @Override
    public List<OuterReportEntity> getOuterReport() {
        List<OuterReportEntity> result = new ArrayList<>();
        List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("SELECT c.id, CONCAT(c.surname, '\n', c.firstname, '\n', c.middlename) as clName, c.date, sc.startDate, cp.caption,"
                + " cc.endDate, sc.stopDate, IFNULL(cc.comments, '-'), IFNULL(c.memo, '-'), w.surname, sh.id FROM"
                + " ServContract sc INNER JOIN Client c ON(sc.client = c.id) INNER JOIN Worker w ON(sc.worker = w.id)"
                + " INNER JOIN ContractControl cc ON(cc.ServContract=sc.id) INNER JOIN ContractPoints cp ON(cp.id = cc.contractpoints)"
                + " LEFT JOIN ShelterHistory sh ON(c.id = sh.client) WHERE (sc.contractresult='1' AND (sh.id is null OR sh.outShelter < CURDATE()))")
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
            Object[] xy = (Object[]) o;

            for (int i = 0; i <= 9; i++) {
                if (xy[i] == null && i != 2 && i != 3 && i != 5 && i != 6) {
                    xy[i] = "";
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
        List<InnerReportEntity> result = new ArrayList<>();
        List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("SELECT c.id, CONCAT(c.surname, '\n', c.firstname, '\n', c.middlename) as clName, c.date, sh.roomId, sh.inShelter, cp.caption, "
                + "cc.endDate, sh.outShelter, IFNULL(cc.comments, '-'), IFNULL(c.memo, '-'), w.surname "
                + "FROM ServContract sc INNER JOIN Client c ON(sc.client = c.id) "
                + "INNER JOIN Worker w ON(sc.worker = w.id) INNER JOIN ContractControl cc ON(cc.servcontract=sc.id) "
                + "INNER JOIN ContractPoints cp ON(cp.id = cc.contractpoints) LEFT JOIN ShelterHistory sh ON(c.id = sh.client) WHERE (sc.contractresult='1' AND sh.outShelter > CURDATE())")
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
            Object[] row = (Object[]) o1;
            InnerReportEntity innerReportEntity = new InnerReportEntity();
            for (int i = 0; i < 11; i++) {
                if (row[i] == null) {
                    row[i] = "";
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

    @Override
    public List<ServiceRecipientReportEntity> getServiceRecipientReport(Date from, Date till) {

        List resFromDB = loadOnceService(from, till);

		List<ServiceRecipientReportEntity> result = new ArrayList<>(resFromDB.size());

		for (Object o : resFromDB) {
			result.add(extractSREntity((Object[]) o));
		}

        return result;
    }

	/**
	 * Загружаем из БД выборку по по плановым и разовым услугам
	 * @param from Дата начала выборки
	 * @param till Дата конца выборки
	 * @return Список результатов
	 */
    private List loadOnceService(Date from, Date till) {
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(
				"(select st.caption as caption, count(distinct rs.client) as cntCl, count(*) as cnt " +
						"from RecievedService rs " +
						"left join ServicesType st on rs.servicesType = st.id " +
						"where rs.date >= " + wrapDate(from) + " and rs.date <= " + wrapDate(till) + " " +
						"group by rs.servicesType) " +
					"UNION ALL " +
					"(select cp.caption as caption, count(DISTINCT sc.client) as cntCl, count(*) as cnt " +
						"from ContractControl cc " +
						"left join ServContract sc on cc.servcontract = sc.id " +
						"left join ContractPoints cp on cc.contractpoints = cp.id " +
						"where cc.endDate >= " + wrapDate(from) + " and cc.endDate <= " + wrapDate(till) + " " +
						"group by cc.contractpoints) " +
					"order by caption"
					)
				.addScalar("caption")
				.addScalar("cntCl")
				.addScalar("cnt")
				;

		log.debug(query.getQueryString());

		return query.list();
	}

	/**
	 * Формируем ServiceRecipientReportEntity из нетипизированного результата запроса
	 * @param row Результат запроса - массив объектов (строк?)
	 * @return объект ServiceRecipientReportEntity, представляющий эту строку
	 */
    private ServiceRecipientReportEntity extractSREntity(Object[] row) {
		ServiceRecipientReportEntity entity = new ServiceRecipientReportEntity();

		int i = 0;
		entity.setServiceType(row[i++].toString());
		entity.setCountOfUniqueClient(Integer.valueOf(row[i++].toString()));
		entity.setCountOfService(Integer.valueOf(row[i++].toString()));

		return entity;
	}

	/**
	 * Приводит заданную дату к строке с подходящим форматом для использования в SQL-запросе
	 * @param date Дата для приведения
	 * @return Строка для подстановки в SQL-запрос
	 */
    private String wrapDate(final Date date) {
        return Util.parseDateForMySql(date);
    }

}
