package ru.homeless.dao;

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
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -30);
        String criticalVaccinationDay = getDateFormat().format(calendar.getTime());

        log.info("Querying overdue items");

        String query = "SELECT c.id, CONCAT(c.firstname, ' ', c.surname), noVac.dipthVac, noVac.hepotitsVac, noVac.typhVac, noVac.fluorogr, " +
                "CONCAT(w.firstname, ' ', w.surname), noVac.inShelter" + " FROM (SELECT * FROM ShelterHistory WHERE roomId is not null " +
                " AND((dipthVac is null OR hepotitsVac is null OR typhVac is null)" + " AND inShelter > '" + criticalVaccinationDay + "')) noVac " + " LEFT JOIN Client c ON(noVac.client = c.id) LEFT JOIN ServContract s ON " +
                "(s.client=noVac.client) LEFT JOIN Worker w ON(s.worker=w.id)";
        List<?> data = getSessionFactory().getCurrentSession().createSQLQuery(
                query)
                .addScalar("c.id")
                .addScalar("CONCAT(c.firstname, ' ', c.surname)")
                .addScalar("noVac.inShelter")
                .addScalar("noVac.dipthVac")
                .addScalar("noVac.hepotitsVac")
                .addScalar("noVac.typhVac")
                .addScalar("CONCAT(w.firstname, ' ', w.surname)").list();

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
        item.setHasDyphVaccine(isVaccinated(rowData[2]));
        item.setHasHepathVaccine(isVaccinated(rowData[3]));
        item.setHasTyphVaccine(isVaccinated(rowData[4]));
        item.setWorkerName((String) rowData[5]);
        return item;
    }

    private boolean isVaccinated(Object cellData) {
        if (cellData == null) {
            return false;
        } else {
            return true;
        }
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
