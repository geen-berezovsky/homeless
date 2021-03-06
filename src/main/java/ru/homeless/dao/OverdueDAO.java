package ru.homeless.dao;

import java.math.BigInteger;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.homeless.entities.OverdueItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class OverdueDAO extends GenericDAO {
    public static Logger log = Logger.getLogger(OverdueDAO.class);
    private static ThreadLocal<DateFormat> threadLocalDateFormat = new ThreadLocal<>();

    public List<OverdueItem> getOverdueItems() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        final DateFormat format = getDateFormat();
        String criticalVaccinationDay = format.format(calendar.getTime());
        calendar.add(Calendar.MONTH, -5);
        String criticalFluorographyDay = format.format(calendar.getTime());
        log.info("Querying overdue items");

        String query = "SELECT c.id, CONCAT(c.firstname, ' ', c.surname), noVac.dipthVac, noVac.hepotitsVac, noVac.typhVac, " +
                "CONCAT(w.firstname, ' ', w.surname), noVac.inShelter, (noVac.fluorogr >= '"+criticalFluorographyDay+"')" + " FROM (SELECT * FROM ShelterHistory WHERE roomId is not null " +
                " AND((dipthVac is null OR hepotitsVac is null OR typhVac is null)" + " AND inShelter < '" + criticalVaccinationDay + "') OR (fluorogr is null) OR (fluorogr < '"
         +criticalFluorographyDay+"')) noVac " + " LEFT JOIN Client c ON(noVac.client = c.id) LEFT JOIN ServContract s ON " +
                "(s.client=noVac.client) LEFT JOIN Worker w ON(s.worker=w.id)";
        List<?> data = getSessionFactory().getCurrentSession().createSQLQuery(
                query)
                .addScalar("c.id")
                .addScalar("CONCAT(c.firstname, ' ', c.surname)")
                .addScalar("noVac.inShelter")
                .addScalar("noVac.dipthVac")
                .addScalar("noVac.hepotitsVac")
                .addScalar("noVac.typhVac")
                .addScalar("CONCAT(w.firstname, ' ', w.surname)")
                .addScalar("(noVac.fluorogr >= '"+criticalFluorographyDay+"')").list();

        log.info("Got " + data.size() + " items");
        List<OverdueItem> result = new ArrayList<>();
        for (Object row : data) {
            result.add(convert(row));
        }
        return result;
    }

    private OverdueItem convert(Object row) {
        log.info("Converting item: " + row + " " + row.getClass());
        OverdueItem item = new OverdueItem();
        Object[] rowData = (Object[]) row;
        item.setClientId((Integer) rowData[0]);
        item.setClientName((String) rowData[1]);
        item.setInShelter((Date)rowData[2]);
        item.setHasDyphVaccine(isVaccinated(rowData[3]));
        item.setHasHepathVaccine(isVaccinated(rowData[4]));
        item.setHasTyphVaccine(isVaccinated(rowData[5]));
        item.setIsFluorographyDone(rowData[7] != null && !rowData[7].equals(BigInteger.ZERO));
        item.setWorkerName((String) rowData[6]);
        return item;
    }

    private boolean isVaccinated(Object cellData) {
        return cellData != null;
    }

    private static DateFormat getDateFormat() {
        DateFormat df = threadLocalDateFormat.get();
        if (df == null) {
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            threadLocalDateFormat.set(df);
        }
        return df;
    }
}
