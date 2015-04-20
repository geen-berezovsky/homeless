package ru.homeless.dao;

import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import ru.homeless.report.entities.ICustomStatisticsReport;
import ru.homeless.report.entities.CustomStatisticsReportEntity;
import ru.homeless.util.Util;

@Repository
public class CustomReportDAO extends GenericDAO implements ICustomReportDAO {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(CustomReportDAO.class);

	public CustomStatisticsReportEntity prepareEntity(List<?> res, int queryType) {
		CustomStatisticsReportEntity customStatisticsReportEntity = new CustomStatisticsReportEntity();
		customStatisticsReportEntity.setQueryType(queryType);
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (Object o : res) {
			Object[] xy = (Object[]) o;
			map.put(xy[0].toString(), Integer.parseInt(xy[1].toString()));
		}
		customStatisticsReportEntity.setValueAndQuantity(map);
		return customStatisticsReportEntity;
	}

    @Override
	public CustomStatisticsReportEntity getReportDataByGender(Date from, Date till) {

		List<?> res = getSessionFactory().getCurrentSession()
				.createSQLQuery("select case WHEN gender=0 THEN 'Женщины' ELSE 'Мужчины' END as 'Пол', count(*) as 'Количество' from Client where date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till) + " group by gender").addScalar("Пол")
				.addScalar("Количество").list();

		return prepareEntity(res, ICustomStatisticsReport.QUERY_GENDER_TYPE);
	}

	@Override
	public CustomStatisticsReportEntity getReportDataByMartialStatus(Date from, Date till) {
        /*

		List<?> res = getSessionFactory().getCurrentSession()
				.createSQLQuery("select case when martialStatus=0 then 'Неизвестно' " +
						"when martialStatus=1 then 'Состоит в браке' else 'Не состоит в браке' end as 'Семейное положение', count(*) 'Количество' " +   
						"from Client where date(date)<DATE_ADD(sysdate(), INTERVAL -14 YEAR) and date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till) + " group by martialStatus")
						.addScalar("Семейное положение")
						.addScalar("Количество").list();
		return prepareEntity(res, ICustomStatisticsReport.QUERY_MARTIAL_STATUS_TYPE);

		*/
        int people = ((Integer) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).intValue();
        int unknown = ((Integer) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where martialStatus='0' AND date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).intValue();
        int married = ((Integer) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where martialStatus='1' AND date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).intValue();
        int single = people - unknown - married;

        List<Integer> abc;

        return null;
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByDependencies(Date from, Date till) {

        int people = ((Integer) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).intValue();
        int hasDepends = ((Integer) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where dependents='1' AND date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).intValue();
        int noDepends = ((Integer) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where dependents='2' AND date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).intValue();
        int unknownDepends = people - hasDepends - noDepends;

        CustomStatisticsReportEntity customStatisticsReportEntity = new CustomStatisticsReportEntity();
        customStatisticsReportEntity.setQueryType(ICustomStatisticsReport.QUERY_DEPENDENCIES_TYPE);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("Есть",hasDepends);
        map.put("Нет",noDepends);
        map.put("Неизвестно",unknownDepends);
        customStatisticsReportEntity.setValueAndQuantity(map);

        return customStatisticsReportEntity;
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByEducation(Date from, Date till) {
        return null;
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByChilds(Date from, Date till) {
        return null;
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByStudentsOrNot(Date from, Date till) {
        return null;
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByProfession(Date from, Date till) {
        return null;
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByLiveInFlat(Date from, Date till) {
        return null;
    }

    @Override
    public CustomStatisticsReportEntity getReportDataBySeeFamily(Date from, Date till) {
        return null;
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByNightStay(Date from, Date till) {
        return null;
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByReasonOfHomeless(Date from, Date till) {
        return null;
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByChronicDiseases(Date from, Date till) {
        return null;
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByAllBw(Date from, Date till) {
        return null;
    }

    @Override
    public CustomStatisticsReportEntity getReportDataAdultBw(Date from, Date till) {
        return null;
    }
}


/*
OLD:






package ru.homeless.extention;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Session;

import ru.homeless.generic.ExcelCell;
import ru.homeless.generic.Main;
import ru.homeless.generic.Util;

public class BigStatistic {
	private String firstRegDate;
	private String secondRegDate;
	private String regDates;
	private String checker;
	private String schooler;
	private String schoolerDates;
	private ArrayList<ExcelCell> cells;
	private ExcelCell cell;

	public BigStatistic(Date a, Date b) {
		firstRegDate = "'" + (a.getYear()+1900) + "-" + (a.getMonth()+1) + "-" + a.getDate() + "'";
		secondRegDate = "'" + (b.getYear()+1900) + "-" + (b.getMonth()+1) + "-" + b.getDate() + "'";
		regDates = "date(regdate) BETWEEN " + firstRegDate+ " AND " + secondRegDate;
		GregorianCalendar calen = (GregorianCalendar) GregorianCalendar.getInstance();
		calen.roll(Calendar.YEAR, -14);
		checker = "'" + calen.get(Calendar.YEAR) + "-" + calen.get(Calendar.MONTH)+1 + "-" + calen.get(Calendar.DATE)+"'";
		calen.roll(Calendar.YEAR, 7);
		schooler = "'" + calen.get(Calendar.YEAR) + "-" + calen.get(Calendar.MONTH)+1 + "-" + calen.get(Calendar.DATE)+"'";
		schoolerDates = "date(date) BETWEEN " + checker+ " AND " + schooler;
		cells = new ArrayList<ExcelCell>();
		genderStat();
		martialStat();
		dependStat();
		eduStat();
		eduChildStat();
		studentsOrNot();
		hasProfession();
		liveInFlat();
		seeFamily();
		nightStay();
		reasonsOfHomeless();
		chonicDis();
		allBw();
		adultBw();
	}

	private void genderStat() {
		Session session = Util.getSession();

		long people = ((BigInteger) session.createSQLQuery("select count(*) from Client where " + regDates).uniqueResult()).longValue();
		long women = ((BigInteger) session.createSQLQuery("select count(*) from Client where gender='0' AND " + regDates).uniqueResult()).longValue();

		if (session != null && session.isOpen()) {
			session.close();
		}

		long men = people - women;

		cell = null;
		cell = new ExcelCell(6, 5, "Мужчины");
		cells.add(cell);
		cell = new ExcelCell(7, 5, String.valueOf(men));
		cells.add(cell);
		cell = new ExcelCell(6, 6, "Женщины");
		cells.add(cell);
		cell = new ExcelCell(7, 6, String.valueOf(women));
		cells.add(cell);

	}

	private void martialStat() {
		Session session = Util.getSession();

		long people = ((BigInteger) session.createSQLQuery("select count(*) from Client where date(date)<" + checker + " AND " + regDates).uniqueResult()).longValue();
		long uknown = ((BigInteger) session.createSQLQuery("select count(*) from Client where martialStatus='0' and date(date)<" + checker + " AND " + regDates).uniqueResult()).longValue();
		long married = ((BigInteger) session.createSQLQuery("select count(*) from Client where martialStatus='1' and date(date)<" + checker + " AND " + regDates).uniqueResult()).longValue();

		if (session != null && session.isOpen()) {
			session.close();
		}

		long single = people - uknown - married;
		cell = null;
		cell = new ExcelCell(2, 35, "Состоит в браке");
		cells.add(cell);
		cell = new ExcelCell(3, 35, String.valueOf(married));
		cells.add(cell);
		cell = new ExcelCell(2, 36, "Не состоит в браке");
		cells.add(cell);
		cell = new ExcelCell(3, 36, String.valueOf(single));
		cells.add(cell);
		cell = new ExcelCell(2, 37, "Неизвестно");
		cells.add(cell);
		cell = new ExcelCell(3, 37, String.valueOf(uknown));
		cells.add(cell);

	}

	private void dependStat() {
		Session session = Util.getSession();

		long people = ((BigInteger) session.createSQLQuery("select count(*) from Client where date(date)<" + checker + " AND " + regDates).uniqueResult()).longValue();
		long hasDepends = ((BigInteger) session.createSQLQuery("select count(*) from Client where dependents='1' AND date(date)<" + checker + " AND " + regDates).uniqueResult()).longValue();
		long noDepends = ((BigInteger) session.createSQLQuery("select count(*) from Client where dependents='2' AND date(date)<" + checker + " AND " + regDates).uniqueResult()).longValue();

		if (session != null && session.isOpen()) {
			session.close();
		}

		long uknownDepends = people - hasDepends - noDepends;
		cell = null;
		cell = new ExcelCell(6, 35, "Есть");
		cells.add(cell);
		cell = new ExcelCell(7, 35, String.valueOf(hasDepends));
		cells.add(cell);
		cell = new ExcelCell(6, 36, "Нет");
		cells.add(cell);
		cell = new ExcelCell(7, 36, String.valueOf(noDepends));
		cells.add(cell);
		cell = new ExcelCell(6, 37, "Неизвестно");
		cells.add(cell);
		cell = new ExcelCell(7, 37, String.valueOf(uknownDepends));
		cells.add(cell);

	}

	private void eduStat() {
		Session session = Util.getSession();

		List<?> res = session.createSQLQuery("select e.caption, count(c.id) from Education e LEFT JOIN (SELECT * FROM Client WHERE date(date) < " + checker
				+ " AND " + regDates +") c ON(e.id = c.education) group by e.caption")
				.addScalar("e.caption")
				.addScalar("count(c.id)")
				.list();

		if (session != null && session.isOpen()) {
			session.close();
		}

		List<Object[]> list = (List<Object []>) res;
		cell = null;
		int cellrow = 58;
		for(Object[] row: list) {
			for(int i = 2; i<4; i++) {
				cell = new ExcelCell(i, cellrow, row[i-2].toString());
				cells.add(cell);
			}
			cellrow++;
		}

	}

	private void eduChildStat() {
		Session session = Util.getSession();

		List<?> res = session.createSQLQuery("select e.caption, count(c.id) from (SELECT * FROM Education WHERE audience='0') e LEFT JOIN (SELECT * FROM Client WHERE (" + schoolerDates + ") AND (" + regDates + ")) c ON(e.id = c.education) group by e.caption")
				.addScalar("e.caption")
				.addScalar("count(c.id)")
				.list();

		if (session != null && session.isOpen()) {
			session.close();
		}

		List<Object[]> list = (List<Object []>) res;
		cell = null;
		int cellrow = 58;
		for(Object[] row: list) {
			for(int i = 6; i<8; i++) {
				cell = new ExcelCell(i, cellrow, row[i-6].toString());
				cells.add(cell);
			}
			cellrow++;
		}
	}

	private void studentsOrNot() {
		Session session = Util.getSession();

		long childs = ((BigInteger) session.createSQLQuery("select count(*) from Client where ((" + schoolerDates + ") AND (" + regDates + "))").uniqueResult()).longValue();
		long students = ((BigInteger) session.createSQLQuery("select count(*) from Client where (isStudent='1' AND (" + schoolerDates + ") AND (" + regDates + "))").uniqueResult()).longValue();
		long noStudents = ((BigInteger) session.createSQLQuery("select count(*) from Client where (isStudent='2' AND (" + schoolerDates + ") AND (" + regDates +"))").uniqueResult()).longValue();

		if (session != null && session.isOpen()) {
			session.close();
		}

		long uknownStud = childs - students - noStudents;

		cell = null;
		cell = new ExcelCell(6, 83, "Сейчас учатся");
		cells.add(cell);
		cell = new ExcelCell(7, 83, String.valueOf(students));
		cells.add(cell);
		cell = new ExcelCell(6, 84, "Сейчас не учатся");
		cells.add(cell);
		cell = new ExcelCell(7, 84, String.valueOf(noStudents));
		cells.add(cell);
		cell = new ExcelCell(6, 85, "Неизвестно");
		cells.add(cell);
		cell = new ExcelCell(7, 85, String.valueOf(uknownStud));
		cells.add(cell);
	}

	private void hasProfession() {
		Session session = Util.getSession();

		long people = ((BigInteger) session.createSQLQuery("select count(*) from Client where date(date)<" + checker + " AND " + regDates).uniqueResult()).longValue();
		long noProf = ((BigInteger) session.createSQLQuery("select count(*) from Client where (profession ='нет' AND (date(date)<" + checker + ") AND (" + regDates + "))").uniqueResult()).longValue();
		long uknownInfo = ((BigInteger) session.createSQLQuery("select count(*) from Client where (profession is null AND (date(date)<" + checker + ") AND (" + regDates + "))").uniqueResult()).longValue();

		if (session != null && session.isOpen()) {
			session.close();
		}

		long hasProf = people - noProf - uknownInfo;
		cell = null;
		cell = new ExcelCell(2, 106, "Да");
		cells.add(cell);
		cell = new ExcelCell(3, 106, String.valueOf(hasProf));
		cells.add(cell);
		cell = new ExcelCell(2, 107, "Нет");
		cells.add(cell);
		cell = new ExcelCell(3, 107, String.valueOf(noProf));
		cells.add(cell);
		cell = new ExcelCell(2, 108, "Неизвестно");
		cells.add(cell);
		cell = new ExcelCell(3, 108, String.valueOf(uknownInfo));
		cells.add(cell);
	}

	private void liveInFlat() {
		Session session = Util.getSession();

		long people = ((BigInteger) session.createSQLQuery("select count(*) from Client where " + regDates).uniqueResult()).longValue();
		long inFlat = ((BigInteger) session.createSQLQuery("select count(*) from Client where liveInFlat='1' AND " + regDates).uniqueResult()).longValue();
		long outFlat = ((BigInteger) session.createSQLQuery("select count(*) from Client where liveInFlat='2' AND " + regDates).uniqueResult()).longValue();

		if (session != null && session.isOpen()) {
			session.close();
		}

		long uknownFlatStat = people - inFlat - outFlat;
		cell = null;
		cell = new ExcelCell(6, 106, "Да");
		cells.add(cell);
		cell = new ExcelCell(7, 106, String.valueOf(inFlat));
		cells.add(cell);
		cell = new ExcelCell(6, 107, "Нет");
		cells.add(cell);
		cell = new ExcelCell(7, 107, String.valueOf(outFlat));
		cells.add(cell);
		cell = new ExcelCell(6, 108, "Неизвестно");
		cells.add(cell);
		cell = new ExcelCell(7, 108, String.valueOf(uknownFlatStat));
		cells.add(cell);
	}

	private void seeFamily() {
		Session session = Util.getSession();

		List<?> res = session.createSQLQuery("select f.caption, count(c.id) FROM FamilyCommunication f LEFT JOIN (SELECT * FROM Client WHERE (" + regDates + ")) c ON (f.id=c.familycommunication) group by f.caption")
				.addScalar("f.caption")
				.addScalar("count(c.id)")
				.list();

		if (session != null && session.isOpen()) {
			session.close();
		}

		List<Object[]> list = (List<Object []>) res;
		cell = null;
		int cellrow = 128;
		for(Object[] row: list) {
			for(int i = 2; i<4; i++) {
				cell = new ExcelCell(i, cellrow, row[i-2].toString());
				cells.add(cell);
			}
			cellrow++;
		}

	}

	private void nightStay() {
		Session session = Util.getSession();

		List<?> res = session.createSQLQuery("select n.caption, count(c.id) FROM NightStay n LEFT JOIN (SELECT * FROM Client WHERE liveInFlat='2' AND " + regDates + ") c ON (n.id=c.nightStay) group by n.caption")
				.addScalar("n.caption")
				.addScalar("count(c.id)")
				.list();

		if (session != null && session.isOpen()) {
			session.close();
		}

		List<Object[]> list = (List<Object []>) res;
		cell = null;
		int cellrow = 128;
		for(Object[] row: list) {
			for(int i = 6; i<8; i++) {
				cell = new ExcelCell(i, cellrow, row[i-6].toString());
				cells.add(cell);
			}
			cellrow++;
		}
	}

	private void reasonsOfHomeless() {
		Session session = Util.getSession();

		List<?> res = session.createSQLQuery("select r.caption, IFNULL(st.counts, 0) from (select l.reasonofhomeless_id, count(c.id) as counts from link_reasonofhomeless_client l inner join (select * from Client where " + regDates +") c on(c.id=l.clients_id) group by l.reasonofhomeless_id) st right join ReasonOfHomeless r on(st.reasonofhomeless_id=r.id);")
				.addScalar("r.caption")
				.addScalar("IFNULL(st.counts, 0)")
				.list();

		List<Object[]> list = (List<Object []>) res;
		cell = null;
		int cellrow = 150;
		for(Object[] row: list) {
			for(int i = 2; i<4; i++) {
				cell = new ExcelCell(i, cellrow, row[i-2].toString());
				cells.add(cell);
			}
			cellrow++;
		}
	}

	private void chonicDis() {
		Session session = Util.getSession();

		List<?> res = session.createSQLQuery("select r.caption, IFNULL(st.counts, 0) from (select l.diseases_id, count(c.id) as counts from link_chronicdisease_client l inner join (select * from Client where " + regDates + ") c on(c.id=l.clients_id) group by l.diseases_id) st right join ChronicDisease r on(st.diseases_id=r.id);")
				.addScalar("r.caption")
				.addScalar("IFNULL(st.counts, 0)")
				.list();

		List<Object[]> list = (List<Object []>) res;
		cell = null;
		int cellrow = 150;
		for(Object[] row: list) {
			for(int i = 6; i<8; i++) {
				cell = new ExcelCell(i, cellrow, row[i-6].toString());
				cells.add(cell);
			}
			cellrow++;
		}
	}

	private void allBw() {
		Session session = Util.getSession();

		List<?> res = session.createSQLQuery("select r.caption, IFNULL(st.counts, 0) from (select l.breadwinners_id, count(c.id) as counts from link_breadwinner_client l inner join (select * from Client where " + regDates + ") c on(c.id=l.clients_id) group by l.breadwinners_id) st right join Breadwinner r on(st.breadwinners_id=r.id);")
				.addScalar("r.caption")
				.addScalar("IFNULL(st.counts, 0)")
				.list();

		List<Object[]> list = (List<Object []>) res;
		cell = null;
		int cellrow = 177;
		for(Object[] row: list) {
			for(int i = 2; i<4; i++) {
				cell = new ExcelCell(i, cellrow, row[i-2].toString());
				cells.add(cell);
			}
			cellrow++;
		}
	}

	private void adultBw() {
		Session session = Util.getSession();

		List<?> res = session.createSQLQuery("select r.caption, IFNULL(st.counts, 0) from (select l.breadwinners_id, count(c.id) as counts from link_breadwinner_client l inner join (select * from Client where ((" + regDates + ") AND (date(date)<" + checker + "))) c on(c.id=l.clients_id) group by l.breadwinners_id) st right join Breadwinner r on(st.breadwinners_id=r.id);")
				.addScalar("r.caption")
				.addScalar("IFNULL(st.counts, 0)")
				.list();

		List<Object[]> list = (List<Object []>) res;
		cell = null;
		int cellrow = 177;
		for(Object[] row: list) {
			for(int i = 6; i<8; i++) {
				cell = new ExcelCell(i, cellrow, row[i-6].toString());
				cells.add(cell);
			}
			cellrow++;
		}
	}


	public List getCells() {
		return cells;
	}

}


 */