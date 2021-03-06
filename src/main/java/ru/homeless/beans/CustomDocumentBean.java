package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.*;
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
import java.util.List;

@ManagedBean (name = "customDocument")
@ViewScoped
public class CustomDocumentBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(CustomDocumentBean.class);

	private Worker worker;
    private Client client;

    private String origType = "Заявление";
    private String type = origType;
	private String forWhom = "";

    private String preamble = "";
    private String mainPart;

    private List<String> finalPart;

    private String origSignature = "С уважением\nПредседатель СПб БОО \"Ночлежка\"\nГригорий Свердлин";
    private String signature = origSignature;
    private String performer;

    private StreamedContent file;

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    @ManagedProperty(value = "#{WorkerService}")
	private WorkerService workerService;


    public void openDlg() {
        HttpSession session = Util.getSession();
        worker = (Worker) session.getAttribute("worker");
        this.client = Util.getCurrentClient();

        this.mainPart = "К нам обратился за помощью "+client.getSurname()+" "+client.getFirstname()+" "+client.getMiddlename()+" "+Util.formatDate(client.getDate()) +" г.р.";
        this.performer = "Исполнитель: "+worker.getSurname()+" "+worker.getFirstname().substring(0,1)+". "+worker.getMiddlename().substring(0, 1)+".";

        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('standardDocumentWv').show();");
    }

    public void export() throws IOException {
        //Prepare new entity and add it to the database
        String finalPartText = "";
        for (String s : finalPart) {
            finalPartText += s + " ";
        }

        String docNum = Util.getDocNum(workerService, 1); // This is the Custom Document


        CustomDocumentRegistry customDocumentRegistry = new CustomDocumentRegistry(client.getId(), docNum, type, preamble, mainPart, finalPartText, forWhom, signature, performer, worker.getId(), new Date());

        workerService.addInstance(customDocumentRegistry);
        log.debug("Inserted Custom Document object with ID=" + customDocumentRegistry.getId());

        /**
         * This code is necessary only for calculating correct document number. Actually, the document number is based on the count of
         * documents specified document type and the count of release documents starting the date of the start of current year.
         *
         * According the implementation, Custom Documents are stored in another than other documents database table, but the code which calculates the
         * document number should be the same for all.
         *
         * This is the only one reason why we inserting the record to BasicDocumentRegistry table and registered the type of Custom Document
         * in the table BasicDocumentRegistryType with the id=1. So, it is the fake and it is ignored when we show to end user the table
         * with generated basic documents.
         */
        BasicDocumentRegistryType bdrtype = workerService.getInstanceById(BasicDocumentRegistryType.class, 1);
        BasicDocumentRegistry basicDocumentRegistry = new BasicDocumentRegistry(client.getId(), docNum, bdrtype, 0, null, null, worker.getId(), new Date(), "");
        workerService.addInstance(basicDocumentRegistry);
        log.debug("Inserted BasicDocumentRegistry object with ID=" + basicDocumentRegistry.getId());
        /**
         * This code ends
         */

        String requestSuffix = "/getGeneratedWordDocument?requestType=16&clientId="+ client.getId() + "&docId=" + customDocumentRegistry.getId() + "&workerId="+worker.getId();
        String saveFilePath = "/tmp" + File.separator + "StandardDocument.docx";
        String docType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        String docName = "StandardDocument.docx";


        //RESET ALL FIELDS
        type = origType;
        forWhom = "";
        preamble = "";
        mainPart = "";
        finalPart = null;
        signature = origSignature;
        performer = "";

        file = Util.downloadDocument(requestSuffix, saveFilePath, docType, docName);

    }


    //Getters and setters

	public WorkerService getWorkerService() {
		return workerService;
	}

	public void setWorkerService(WorkerService workerService) {
		this.workerService = workerService;
	}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getForWhom() {
        return forWhom;
    }

    public void setForWhom(String forWhom) {
        this.forWhom = forWhom;
    }

    public String getPreamble() {
        return preamble;
    }

    public void setPreamble(String preamble) {
        this.preamble = preamble;
    }

    public String getMainPart() {
        return mainPart;
    }

    public void setMainPart(String mainPart) {
        this.mainPart = mainPart;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public Worker getWorker() {
        return worker;
    }
    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public List<String> getFinalPart() {
        return finalPart;
    }

    public void setFinalPart(List<String> finalPart) {
        this.finalPart = finalPart;
    }




}
