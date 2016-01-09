package ru.homeless.entities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OverdueItem {
    private static ThreadLocal<DateFormat> threadLocalDateFormat = new ThreadLocal<>();
    private String clientId;
    private String clientName;
    private Date inShelter;
    private String workerName;

    private boolean hasTyphVaccine;
    private boolean hasDyphVaccine;
    private boolean hasHepathVaccine;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    //TODO: what about timezones?
    public String getInShelter() {
        return getDateFormat().format(inShelter);
    }

    public void setInShelter(Date inShelter) {
        this.inShelter = inShelter;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getHasTyphVaccine() {
        return getOverdueLabel(hasTyphVaccine);
    }

    public void setHasTyphVaccine(boolean hasTyphVaccine) {
        this.hasTyphVaccine = hasTyphVaccine;
    }

    public String getHasDyphVaccine() {
        return getOverdueLabel(hasDyphVaccine);
    }

    public void setHasDyphVaccine(boolean hasDyphVaccine) {
        this.hasDyphVaccine = hasDyphVaccine;
    }

    public String getHasHepathVaccine() {
        return getOverdueLabel(hasHepathVaccine);
    }

    public void setHasHepathVaccine(boolean hasHepathVaccine) {
        this.hasHepathVaccine = hasHepathVaccine;
    }

    private String getOverdueLabel(boolean overdue) {
        if (overdue) {
            return "";
        } else {
            return "✘";
        }
    }

    private static DateFormat getDateFormat() {
        DateFormat df = threadLocalDateFormat.get();
        if (df == null) {
            df = new SimpleDateFormat("dd.MM.yyyy");
            threadLocalDateFormat.set(df);
        }
        return df;
    }
}
