package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.*;
import ru.homeless.services.WorkerService;
import ru.homeless.util.Util;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

@ManagedBean (name = "regdocument")
@ViewScoped
public class RegDocumentBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(RegDocumentBean.class);

	private Worker worker;
    private Client client;

    private Date dateFrom;
    private Date dateTill;

    private StreamedContent file;

    private Document selectedDocument;

    @ManagedProperty(value = "#{WorkerService}")
	private WorkerService workerService;

    public void onRowSelect(SelectEvent event) {
        selectedDocument = (Document) event.getObject();
        openDlg();
    }

    public void openDlg() {
        HttpSession session = Util.getSession();
        worker = (Worker) session.getAttribute("worker");
        String cids = session.getAttribute("cid").toString();

        dateFrom = new Date();
        dateTill = new Date();

        if (cids != null && !cids.trim().equals("")) {
            this.client = getWorkerService().getInstanceById(Client.class, Integer.parseInt(cids));
        }

        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("regDocumentSelectWv.hide();");
        rc.execute("regDocumentWv.show();");
    }

    public void showSelectDocumentDialog() {
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("regDocumentSelectWv.show()");
    }

    public void export() throws IOException {
        //Prepare new entity and add it to the database

        RegistrationDocumentRegistry registrationDocumentRegistry = new RegistrationDocumentRegistry(client.getId(), selectedDocument.getId(), dateFrom, dateTill, worker.getId(), new Date());

        workerService.addInstance(registrationDocumentRegistry);
        log.debug("Inserted object with ID=" + registrationDocumentRegistry.getId());

        String requestSuffix = "/getGeneratedWordDocument?requestType=10&clientId="+ client.getId() + "&docId=" + registrationDocumentRegistry.getId();
        String saveFilePath = "/tmp" + File.separator + "RegistrationDocument.docx";
        String docType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        String docName = "RegistrationDocument.docx";

        file = Util.downloadDocument(requestSuffix, saveFilePath, docType, docName);

    }


    //Getters and setters

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

    public Document getSelectedDocument() {
        return selectedDocument;
    }

    public void setSelectedDocument(Document selectedDocument) {
        this.selectedDocument = selectedDocument;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTill() {
        return dateTill;
    }

    public void setDateTill(Date dateTill) {
        this.dateTill = dateTill;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }


}
