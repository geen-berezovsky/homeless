package ru.homeless.report.entities;

public class ProvidedServicesByClientReportEntity {

    private int id;
    private String worker;
    private String client_id;
    private String client_fio;
    private String service_type;
    private String date;

    public ProvidedServicesByClientReportEntity(int id, String worker, String client_id, String client_fio, String service_type, String date) {
        this.id = id;
        this.worker = worker;
        this.client_id = client_id;
        this.client_fio = client_fio;
        this.service_type = service_type;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_fio() {
        return client_fio;
    }

    public void setClient_fio(String client_fio) {
        this.client_fio = client_fio;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
