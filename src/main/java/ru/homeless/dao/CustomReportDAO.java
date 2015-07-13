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
    public CustomStatisticsReportEntity getReportDataBySeeRelatives(Date from, Date till) {
        updateDates();
        List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("select f.caption, count(c.id) FROM FamilyCommunication f LEFT JOIN (SELECT * FROM Client WHERE (date(regdate)BETWEEN" + Util.parseDateForMySql(from) + "AND" + Util.parseDateForMySql(till) + ")) c ON (f.id=c.familycommunication) group by f.caption")
                .addScalar("f.caption")
                .addScalar("count(c.id)")
                .list();
        return prepareEntity(res, ICustomStatisticsReport.QUERY_SEE_FAMILY_TYPE);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByWhereSleeping(Date from, Date till) {
        updateDates();
        List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("select n.caption, count(c.id) FROM NightStay n LEFT JOIN (SELECT * FROM Client WHERE liveInFlat='2' AND date(regdate)BETWEEN" + Util.parseDateForMySql(from) + "AND" + Util.parseDateForMySql(till) + ") c ON (n.id=c.nightStay) group by n.caption")
                .addScalar("n.caption")
                .addScalar("count(c.id)")
                .list();
        return prepareEntity(res, ICustomStatisticsReport.QUERY_NIGHT_STAY_TYPE);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByHomelessReasons(Date from, Date till) {
        updateDates();
        List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("select r.caption, IFNULL(st.counts, 0) from (select l.reasonofhomeless_id, count(c.id) as counts from link_reasonofhomeless_client l inner join (select * from Client where date(regdate)BETWEEN" + Util.parseDateForMySql(from) + "AND" + Util.parseDateForMySql(till) +") c on(c.id=l.clients_id) group by l.reasonofhomeless_id) st right join ReasonOfHomeless r on(st.reasonofhomeless_id=r.id);")
                .addScalar("r.caption")
                .addScalar("IFNULL(st.counts, 0)")
                .list();
        return prepareEntity(res, ICustomStatisticsReport.QUERY_REASON_OF_HOMELESS_TYPE);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByChronicalDiseasters(Date from, Date till) {
        updateDates();
        List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("select r.caption, IFNULL(st.counts, 0) from (select l.diseases_id, count(c.id) as counts from link_chronicdisease_client l inner join (select * from Client where date(regdate)BETWEEN" + Util.parseDateForMySql(from) + "AND" + Util.parseDateForMySql(till) + ") c on(c.id=l.clients_id) group by l.diseases_id) st right join ChronicDisease r on(st.diseases_id=r.id);")
                .addScalar("r.caption")
                .addScalar("IFNULL(st.counts, 0)")
                .list();
        return prepareEntity(res, ICustomStatisticsReport.QUERY_CHRONIC_DIS_TYPE);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByBreadwinnersAll(Date from, Date till) {
        updateDates();
        List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("select r.caption, IFNULL(st.counts, 0) from (select l.breadwinners_id, count(c.id) as counts from link_breadwinner_client l inner join (select * from Client where date(regdate)BETWEEN" + Util.parseDateForMySql(from) + "AND" + Util.parseDateForMySql(till) + ") c on(c.id=l.clients_id) group by l.breadwinners_id) st right join Breadwinner r on(st.breadwinners_id=r.id);")
                .addScalar("r.caption")
                .addScalar("IFNULL(st.counts, 0)")
                .list();
        return prepareEntity(res, ICustomStatisticsReport.QUERY_ALL_BW_TYPE);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByBreadwinnersChilds(Date from, Date till) {
        updateDates();
        List<?> res = getSessionFactory().getCurrentSession().createSQLQuery("select r.caption, IFNULL(st.counts, 0) from (select l.breadwinners_id, count(c.id) as counts from link_breadwinner_client l inner join (select * from Client where ((date(regdate)BETWEEN" + Util.parseDateForMySql(from) + "AND" + Util.parseDateForMySql(till) + ") AND (date(date)<" + checker + "))) c on(c.id=l.clients_id) group by l.breadwinners_id) st right join Breadwinner r on(st.breadwinners_id=r.id);")
                .addScalar("r.caption")
                .addScalar("IFNULL(st.counts, 0)")
                .list();
        return prepareEntity(res, ICustomStatisticsReport.QUERY_ADULT_BW_TYPE);
    }

    @Override
    public CustomStatisticsReportEntity getReportDataByAge(Date from, Date till) {
        return null;
    }


}

