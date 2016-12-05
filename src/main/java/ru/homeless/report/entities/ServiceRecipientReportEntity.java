package ru.homeless.report.entities;

/**
 * Created by geen on 01.12.16.
 */
public class ServiceRecipientReportEntity {

    private String worker;

    private String serviceType;

    private Integer countOfUniqueClient;

    private Integer countOfService;

    public ServiceRecipientReportEntity() {
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getCountOfUniqueClient() {
        return countOfUniqueClient;
    }

    public void setCountOfUniqueClient(Integer countOfUniqueClient) {
        this.countOfUniqueClient = countOfUniqueClient;
    }

    public Integer getCountOfService() {
        return countOfService;
    }

    public void setCountOfService(Integer countOfService) {
        this.countOfService = countOfService;
    }
}
