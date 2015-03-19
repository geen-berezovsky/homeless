package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import ru.homeless.entities.*;
import ru.homeless.services.WorkerService;
import ru.homeless.util.Util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@ManagedBean (name = "basicdocument")
@ViewScoped
public class BasicDocumentBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(BasicDocumentBean.class);

	private Worker worker;
    private Client client;

    private Date dateFrom;
    private Date dateTill;

    private String city;

    private StreamedContent file;

    private Document selectedDocument;

    @ManagedProperty(value = "#{WorkerService}")
	private WorkerService workerService;

    public void onRowSelect(SelectEvent event) {
        selectedDocument = (Document) event.getObject();
        String strings = "regDocumentSelectWv.hide()" + "-" + "regDocumentWv.show()";
        openDlg(strings);
    }

    public void openDlg(String strings) {
        log.info("openDLG");
        HttpSession session = Util.getSession();
        worker = (Worker) session.getAttribute("worker");
        String cids = session.getAttribute("cid").toString();

        dateFrom = new Date();
        dateTill = new Date();
        city = "";

        if (cids != null && !cids.trim().equals("")) {
            this.client = getWorkerService().getInstanceById(Client.class, Integer.parseInt(cids));
        }

        RequestContext rc = RequestContext.getCurrentInstance();
        for (String s : strings.split("-")) {
            log.info("Executing "+s);
            rc.execute(s);
        }
    }

    public void export(int basicDocumentRegistryTypeId) throws IOException {
        //Prepare new entity and add it to the database
        int requestType = 0;
        String filename = "";
        int selectedDocumentId = 0;

        switch (basicDocumentRegistryTypeId) {
            case 1: {
                requestType = 10;
                filename = "RegistrationDocument.docx";
                selectedDocumentId = selectedDocument.getId();
                break;
            }
            case 2: {
                requestType = 8;
                filename = "DispensaryDocument.docx";
                selectedDocumentId = 0;
                break;
            }
            case 3: {
                requestType = 2;
                filename = "SocHelpDocument.docx";
                selectedDocumentId = 0;
                break;
            }
            case 4: {
                requestType = 6;
                filename = "SanDocument.docx";
                selectedDocumentId = 0;
                break;
            }
        }

        BasicDocumentRegistryType type = workerService.getInstanceById(BasicDocumentRegistryType.class, basicDocumentRegistryTypeId);
        log.info("LOG TYPE = "+type.getId()+" -> "+type.getCaption());

        BasicDocumentRegistry basicDocumentRegistry = new BasicDocumentRegistry(client.getId(), type, selectedDocumentId, dateFrom, dateTill, worker.getId(), new Date());

        workerService.addInstance(basicDocumentRegistry);
        log.debug("Inserted object with ID=" + basicDocumentRegistry.getId());

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String issueDateStr = format.format(dateFrom);


        String requestSuffix = "/getGeneratedWordDocument?requestType="+requestType+"&clientId="+ client.getId() + "&docId=" + basicDocumentRegistry.getId() + "&issueDate="+issueDateStr + "&workerId="+worker.getId();
        String saveFilePath = "/tmp" + File.separator + filename;
        String docType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        String docName = filename;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }



}
