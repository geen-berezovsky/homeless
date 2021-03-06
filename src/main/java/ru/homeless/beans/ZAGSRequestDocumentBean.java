package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.Client;
import ru.homeless.entities.Worker;
import ru.homeless.entities.ZAGSRequestDocumentRegistry;
import ru.homeless.services.WorkerService;
import ru.homeless.util.Util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@ManagedBean (name = "zagsrequest")
@ViewScoped
public class ZAGSRequestDocumentBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ZAGSRequestDocumentBean.class);


    private Worker worker;
    private Client client;

	private String forWhom = "";
    private String respAddress = Configuration.zagsRequestDefaultResponseAddress;
    private String whereWasBorn = "";
    private String address = "";
    private String mother = "";
    private String father = "";

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    private StreamedContent file;

    private Date date;

    @ManagedProperty(value = "#{WorkerService}")
	private WorkerService workerService;

    public void openDlg() {
        HttpSession session = Util.getSession();
        worker = (Worker) session.getAttribute("worker");

        this.client = Util.getCurrentClient();
        this.whereWasBorn = client.getWhereWasBorn();
        this.date = new Date();

        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('zagsRequestWv').show();");
    }

    public void export() throws IOException {

        //Prepare new entity and add it to the database
        ZAGSRequestDocumentRegistry zagsRequestDocumentRegistry = new ZAGSRequestDocumentRegistry(client.getId(), forWhom, respAddress, whereWasBorn, address, mother, father, date, worker.getId());

        workerService.addInstance(zagsRequestDocumentRegistry);
        log.debug("Inserted object with ID=" + zagsRequestDocumentRegistry.getId());


        String requestSuffix = "/getGeneratedWordDocument?requestType=14&clientId="+ client.getId() + "&docId=" + zagsRequestDocumentRegistry.getId() + "&workerId="+worker.getId();
        String saveFilePath = "/tmp" + File.separator + "ZAGSRequestDocument.docx";
        String docType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        String docName = "ZAGSRequestDocument.docx";

        //RESET ALL FIELDS
        forWhom = "";
        respAddress = Configuration.zagsRequestDefaultResponseAddress;
        whereWasBorn = "";
        address = "";
        mother = "";
        father = "";
        date = new Date();

        file = Util.downloadDocument(requestSuffix, saveFilePath, docType, docName);
    }

    public void updateForm() {
    }

    public WorkerService getWorkerService() {
        return workerService;
    }

    public void setWorkerService(WorkerService workerService) {
        this.workerService = workerService;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getForWhom() {
        return forWhom;
    }

    public void setForWhom(String forWhom) {
        this.forWhom = forWhom;
    }

    public String getWhereWasBorn() {
        return whereWasBorn;
    }

    public void setWhereWasBorn(String whereWasBorn) {
        this.whereWasBorn = whereWasBorn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getRespAddress() {
        return respAddress;
    }

    public void setRespAddress(String respAddress) {
        this.respAddress = respAddress;
    }
}
