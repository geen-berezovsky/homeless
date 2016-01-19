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
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
        HttpSession session = Util.getSession();
        worker = (Worker) session.getAttribute("worker");
        String cids = session.getAttribute("cid").toString();

        Calendar cal = Calendar.getInstance();
        dateFrom = cal.getTime();
        cal.add(Calendar.YEAR, 1);
        dateTill = cal.getTime();

        city = "";

        if (cids != null && !cids.trim().equals("")) {
            this.client = getWorkerService().getInstanceById(Client.class, Integer.parseInt(cids));
        }

        RequestContext rc = RequestContext.getCurrentInstance();
        for (String s : strings.split("-")) {
            rc.execute(s);
        }
    }

    public void export(int basicDocumentRegistryTypeId) throws IOException {
        //Prepare new entity and add it to the database
        int requestType = 0;
        String filename = "";
        int selectedDocumentId = 0;

        switch (basicDocumentRegistryTypeId) {
            case 11: {
                requestType = 10;
                filename = "RegistrationDocument.docx";
                selectedDocumentId = selectedDocument.getId();
                break;
            }
            case 14: {
                requestType = 8;
                filename = "DispensaryDocument.docx";
                selectedDocumentId = 0;
                dateTill = null;
                break;
            }
            case 15: {
                requestType = 2;
                filename = "SocHelpDocument.docx";
                selectedDocumentId = 0;
                dateTill = null;
                break;
            }
            case 12: {
                requestType = 6;
                filename = "SanDocument.docx";
                selectedDocumentId = 0;
                dateTill = null;
                break;
            }

            case 13: {
                requestType = 4;
                filename = "TravelDocument.docx";
                selectedDocumentId = 0;
                dateTill = null;
                break;
            }

            case 16: {
                requestType = 12;
                filename = "TranzitDocument.docx";
                selectedDocumentId = 0;
                dateTill = null;
                break;
            }


            default: {
                requestType = 0;
                filename = "Unknown.docx";
                selectedDocumentId = 0;
                dateTill = null;
                break;
            }
        }

        BasicDocumentRegistryType type = workerService.getInstanceById(BasicDocumentRegistryType.class, basicDocumentRegistryTypeId);

        String docNum= "";

        int currYear = Calendar.getInstance().get(Calendar.YEAR);

        Date from = new GregorianCalendar(currYear,0,1).getTime();
        Date till = new Date();

        log.info("Requested period for counting document number is from "+from+" till "+till);
        docNum = getWorkerService().getDocNum(from, till, basicDocumentRegistryTypeId);
        log.info("Document number is "+docNum);

        BasicDocumentRegistry basicDocumentRegistry = new BasicDocumentRegistry(client.getId(), docNum, type, selectedDocumentId, dateFrom, dateTill, worker.getId(), new Date(), city);

        workerService.addInstance(basicDocumentRegistry);
        log.debug("Inserted object with ID=" + basicDocumentRegistry.getId());

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String issueDateStr = format.format(dateFrom);

        String requestSuffix = "/getGeneratedWordDocument?requestType="+requestType+"&clientId="+ client.getId() + "&docId=" + basicDocumentRegistry.getId() + "&issueDate="+issueDateStr + "&workerId="+worker.getId() + "&docNum="+docNum + "&travelCity="+ URLEncoder.encode(city, "UTF-8");
        String saveFilePath = System.getProperty("java.io.tmpdir") + File.separator + filename;
        log.info("requestSuffix = "+requestSuffix);
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
