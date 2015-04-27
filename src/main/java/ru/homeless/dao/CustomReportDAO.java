package ru.homeless.dao;

import java.math.BigInteger;
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

    private GregorianCalendar calen;
    private String checker;
    private String schooler;
    private String schoolerDates;

    private void updateDates() {
        calen = (GregorianCalendar) GregorianCalendar.getInstance();
        calen.roll(Calendar.YEAR, -14);
        int m = calen.get(Calendar.MONTH) + 1;
        String m_str = String.valueOf(m);
        if (m < 10) {
            m_str = "0"+m;
        }
        checker = "'" + calen.get(Calendar.YEAR) + "-" + m_str + "-" + calen.get(Calendar.DATE)+"'";
        calen.roll(Calendar.YEAR, 7);
        schooler = "'" + calen.get(Calendar.YEAR) + "-" + m_str + "-" + calen.get(Calendar.DATE)+"'";
        schoolerDates = "date(date) BETWEEN " + checker+ " AND " + schooler;
    }

	public CustomStatisticsReportEntity prepareEntity(List<?> res, int queryType) {
		CustomStatisticsReportEntity customStatisticsReportEntity = new CustomStatisticsReportEntity();
		customStatisticsReportEntity.setQueryType(queryType);
		Map<String, Integer> map = new TreeMap<>();
		for (Object o : res) {
			Object[] xy = (Object[]) o;
			map.put(xy[0].toString(), Integer.parseInt(xy[1].toString()));
		}
		customStatisticsReportEntity.setValueAndQuantity(map);
		return customStatisticsReportEntity;
	}

    public CustomStatisticsReportEntity prepareEntity(Map<String, Integer> res, int queryType) {
        CustomStatisticsReportEntity customStatisticsReportEntity = new CustomStatisticsReportEntity();
        customStatisticsReportEntity.setQueryType(queryType);
        customStatisticsReportEntity.setValueAndQuantity(res);
        return customStatisticsReportEntity;
    }


    @Override
	public CustomStatisticsReportEntity getReportDataByGender(Date from, Date till) {

        long people = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).longValue();
        long women = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where gender='0' AND date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).longValue();
        long men = people - women;

        Map<String, Integer> map = new HashMap();
        map.put("Мужчины", Util.safeLongToInt(men));
        map.put("Женщины", Util.safeLongToInt(women));

        return prepareEntity(map, ICustomStatisticsReport.QUERY_GENDER_TYPE);
	}

	@Override
	public CustomStatisticsReportEntity getReportDataByMartialStatus(Date from, Date till) {

        long people = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).longValue();
        long unknown = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where martialStatus='0' AND date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).longValue();
        long married = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where martialStatus='1' AND date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).longValue();

        long single = people - unknown - married;
        Map<String, Integer> map = new HashMap();
        map.put("Состоит в браке", Util.safeLongToInt(married));
        map.put("Не состоит в браке", Util.safeLongToInt(single));
        map.put("Неизвестно", Util.safeLongToInt(unknown));
        return prepareEntity(map, ICustomStatisticsReport.QUERY_MARTIAL_STATUS_TYPE);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByDependencies(Date from, Date till) {

        long people = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).intValue();
        long hasDepends = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where dependents='1' AND date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).intValue();
        long noDepends = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where dependents='2' AND date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).intValue();
        long unknownDepends = people - hasDepends - noDepends;

        Map<String, Integer> map = new HashMap<>();
        map.put("Есть",Util.safeLongToInt(hasDepends));
        map.put("Нет",Util.safeLongToInt(noDepends));
        map.put("Неизвестно",Util.safeLongToInt(unknownDepends));
        return prepareEntity(map, ICustomStatisticsReport.QUERY_DEPENDENCIES_TYPE);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByEducation(Date from, Date till) {
        updateDates();

        List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("select e.caption, count(c.id) from Education e LEFT JOIN (SELECT * FROM Client WHERE date(date) < " + checker
                + " AND date(regdate)BETWEEN" + Util.parseDateForMySql(from) + "AND" + Util.parseDateForMySql(till) + ") c ON(e.id = c.education) group by e.caption order by e.caption")
                .addScalar("e.caption")
                .addScalar("count(c.id)")
                .list();

        return prepareEntity(res, ICustomStatisticsReport.QUERY_EDUCATION_TYPE);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByChilds(Date from, Date till) {
        updateDates();

        List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("select e.caption, count(c.id) from (SELECT * FROM Education WHERE audience='0') e LEFT JOIN (SELECT * FROM Client WHERE (" + schoolerDates + ") AND (date(regdate)BETWEEN" + Util.parseDateForMySql(from) + "AND" + Util.parseDateForMySql(till) + ")) c ON(e.id = c.education) group by e.caption order by e.caption")
                .addScalar("e.caption")
                .addScalar("count(c.id)")
                .list();


        return prepareEntity(res, ICustomStatisticsReport.QUERY_CHILDS_TYPE);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByStudentsOrNot(Date from, Date till) {

        long childs = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where ((" + schoolerDates + " AND (date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till) + ")))").uniqueResult()).longValue();
        long students = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where (isStudent='1' AND (" + schoolerDates + ") AND (date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till) + "))").uniqueResult()).longValue();
        long noStudents = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where (isStudent='2' AND (" + schoolerDates + ") AND (date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till) + "))").uniqueResult()).longValue();

        long uknownStud = childs - students - noStudents;

        Map<String, Integer> map = new HashMap();
        map.put("Сейчас учатся", Util.safeLongToInt(students));
        map.put("Сейчас не учатся", Util.safeLongToInt(noStudents));
        map.put("Неизвестно", Util.safeLongToInt(uknownStud));

        return prepareEntity(map, ICustomStatisticsReport.QUERY_STUDENTS_TYPE);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByProfession(Date from, Date till) {

        long people = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where date(date)<" + checker + " AND date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).longValue();
        long noProf = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where (profession ='нет' AND (date(date)<" + checker + ") AND (date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till) + "))").uniqueResult()).longValue();
        long uknownInfo = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where (profession is null AND (date(date)<" + checker + ") AND (date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till) + "))").uniqueResult()).longValue();
        long hasProf = people - noProf - uknownInfo;

        Map<String, Integer> map = new HashMap();
        map.put("Да", Util.safeLongToInt(hasProf));
        map.put("Нет", Util.safeLongToInt(noProf));
        map.put("Неизвестно", Util.safeLongToInt(uknownInfo));

        return prepareEntity(map, ICustomStatisticsReport.QUERY_PROFESSION_TYPE);

    }

    @Override
    public CustomStatisticsReportEntity getReportDataByLiveInFlat(Date from, Date till) {
        long people = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).longValue();
        long inFlat = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where liveInFlat='1' AND date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).longValue();
        long outFlat = ((BigInteger) getSessionFactory().getCurrentSession().createSQLQuery("select count(*) from Client where liveInFlat='2' AND date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till)).uniqueResult()).longValue();
        long uknownFlatStat = people - inFlat - outFlat;

        Map<String, Integer> map = new HashMap();
        map.put("Да", Util.safeLongToInt(inFlat));
        map.put("Нет", Util.safeLongToInt(outFlat));
        map.put("Неизвестно", Util.safeLongToInt(uknownFlatStat));

        return prepareEntity(map, ICustomStatisticsReport.QUERY_LIVE_IN_FLAT_TYPE);
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