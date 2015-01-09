package ru.homeless.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import ru.homeless.report.entities.IOldSchoolReport;
import ru.homeless.report.entities.OldSchoolReportEntity;
import ru.homeless.util.Util;

@Repository
public class CustomReportDAO extends GenericDAO implements ICustomReportDAO {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(CustomReportDAO.class);

	@Override
	public OldSchoolReportEntity getReportDataByGender(Date from, Date till) {

		List<?> res = getSessionFactory().getCurrentSession()
				.createSQLQuery("select case WHEN gender=0 THEN 'Женщины' ELSE 'Мужчины' END as 'Пол', count(*) as 'Количество' from Client where date(regdate) BETWEEN " + Util.parseDateForMySql(from) + " AND " + Util.parseDateForMySql(till) + " group by gender").addScalar("Пол")
				.addScalar("Количество").list();

		OldSchoolReportEntity oldSchoolReportEntity = new OldSchoolReportEntity();
		oldSchoolReportEntity.setQueryType(IOldSchoolReport.QUERY_XXX_TYPE);
		Map<String, Integer> map = new HashMap<String, Integer>();

		for (Object o : res) {
			Object[] xy = (Object[]) o;
			map.put(xy[0].toString(), Integer.parseInt(xy[1].toString()));
		}
		oldSchoolReportEntity.setValueAndQuantity(map);
		return oldSchoolReportEntity;
	}
}