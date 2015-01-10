package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
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
    private String name = "";
    private String whereWasBorn = "";
    private String address = "";
    private String mother = "";
    private String father = "";

    private Date date;

    @ManagedProperty(value = "#{WorkerService}")
	private WorkerService workerService;

    public void openDlg() {
        HttpSession session = Util.getSession();
        worker = (Worker) session.getAttribute("worker");
        String cids = session.getAttribute("cid").toString();

        if (cids != null && !cids.trim().equals("")) {
            this.client = getWorkerService().getInstanceById(Client.class, Integer.parseInt(cids));
        }

        this.name = client.getSurname()+" "+client.getFirstname()+" "+client.getMiddlename()+" "+Util.formatDate(client.getDate()) +" г.р.";
        this.whereWasBorn = client.getWhereWasBorn();
        this.date = new Date();

        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("zagsRequestWv.show();");
    }

    public void downloadDocument(int id) {
        RequestContext rc = RequestContext.getCurrentInstance();

        StringBuilder sb = new StringBuilder();
        sb.append("window.location.href = \"");
        sb.append(Configuration.reportEngineUrl);
        sb.append("/getGeneratedWordDocument?");
        sb.append("requestType=14");
        sb.append("&clientId=" + client.getId());
        sb.append("&docId=" + id);
        sb.append("\"");

        log.info("Executing " + sb.toString());

        rc.execute(sb.toString());

    }


    public void export() {

        //Prepare new entity and add it to the database
        ZAGSRequestDocumentRegistry zagsRequestDocumentRegistry = new ZAGSRequestDocumentRegistry(client.getId(), forWhom, name, whereWasBorn, address, mother, father, date, worker.getId());

        workerService.addInstance(zagsRequestDocumentRegistry);
        log.debug("Inserted object with ID=" + zagsRequestDocumentRegistry.getId());

        downloadDocument(zagsRequestDocumentRegistry.getId());


        //RESET ALL FIELDS
        forWhom = "";
        name = "";
        whereWasBorn = "";
        address = "";
        mother = "";
        father = "";
        date = new Date();

        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("zagsRequestWv.hide();");
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


}
