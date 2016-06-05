package ru.homeless.beans;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import ru.homeless.configuration.Configuration;
import ru.homeless.converters.DocTypeConverter;
import ru.homeless.entities.*;
import ru.homeless.services.GenericService;
import ru.homeless.util.Util;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@ManagedBean(name = "scandocuments")
@ViewScoped
public class ScanDocumentsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ScanDocumentsBean.class);

	private List<DocType> docTypes;
    private DocumentScan selectedDocument;
    private List<DocumentScan> clientDocumentScans;
    private StreamedContent file;

	@ManagedProperty(value = "#{GenericService}")
	private GenericService genericService;

	public ScanDocumentsBean() {
	}

    public void reload() {
        clientDocumentScans = getGenericService().getInstancesByClientId(DocumentScan.class, Util.getCurrentClient());
		newSelectedDocument(); // set new document
        RequestContext rc = RequestContext.getCurrentInstance();
        log.debug("Updating docScanlistId");
        rc.update("m_tabview:documentsScan_form:docScanlistId");
    }

	public String formatDate(Date q) {
		if (q != null && !q.equals("")) {
			return Util.formatDate(q);
		} else {
			return "";
		}
	}

	public void deleteDocument() {
		getGenericService().deleteInstance(getGenericService().getInstanceById(DocumentScan.class, selectedDocument.getId()));
		reload();
	}

	public void editDocument() {
		selectedDocument = getGenericService().getInstanceById(DocumentScan.class, selectedDocument.getId());
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update("add_scan_document");	//force updating the add document form
	}

	public void newSelectedDocument() {
		selectedDocument = new DocumentScan();
	}


	public void addSelectedDocument() {

        Worker worker = (Worker) Util.getSession().getAttribute("worker");

        selectedDocument.setClient(Util.getCurrentClient());
        selectedDocument.setWorker(worker);
        selectedDocument.setUploadingDate(new Date());
        if (selectedDocument.getPath() == null || selectedDocument.getPath().trim().equals("")) {
            selectedDocument.setPath("ФАЙЛ НЕ ЗАГРУЖЕН!");
        }
        getGenericService().addInstance(selectedDocument);


/*
		log.info("Selected document "+selectedDocument.getDoctype().getCaption());
		
		// some validation
		if ((selectedDocument.getRegistration() == 0 || selectedDocument.getRegistration() == 1) && !selectedDocument.getCity().trim().equals("")) {
			selectedDocument.setRegistration(2);
		}
		if ((selectedDocument.getRegistration() == 0 || selectedDocument.getRegistration() == 1) && !selectedDocument.getAddress().trim().equals("")) {
			selectedDocument.setRegistration(2);
		}

		//finally, set the client
		selectedDocument.setClient(cid);

		getGenericService().updateInstance(selectedDocument);
		*/
        reload(); //for updating related view
	}

    /*
	public void validateDate(FacesContext ctx, UIComponent component, Object value) {
		try {
			Calendar c = GregorianCalendar.getInstance();
			c.setTime((Date) value);
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Неправильный формат даты!", "Формат должен быть ДД.ММ.ГГГГ!");
			throw new ValidatorException(msg);
		}
	}
*/
	public List<DocType> getDocTypes() {
		init();
        return docTypes;
	}

	public void setDocTypes(List<DocType> docTypes) {
		this.docTypes = docTypes;
	}

	@PostConstruct
	// special for converter!
	public void init() {
		docTypes = new ArrayList<DocType>();
		docTypes.addAll(getGenericService().getInstances(DocType.class));
        //remove from the list of document types the type with caption "???" - HS-4
        int a = 0;
        for (DocType dt : docTypes) {
            if (dt.getCaption().equals("???")) {
                break;
            } else {
                a++;
            }
        }
        docTypes.remove(a);// avoiding ConcurrencyModificationException - removing when the list is not in use
		DocTypeConverter.docTypesDB = new ArrayList<DocType>();
		DocTypeConverter.docTypesDB.addAll(docTypes);
	}

	public GenericService getGenericService() {
		return genericService;
	}

	public void setGenericService(GenericService genericService) {
		this.genericService = genericService;
	}

    public void resetForm() {
        RequestContext rc = RequestContext.getCurrentInstance();
        newSelectedDocument();
        rc.update("add_document_scan");
        rc.execute("addDocumentScanWv.show()");
    }

    public void handleScanDocUpload(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();
        FacesMessage msg = null;
        String fileName = file.getFileName();
        String docPath = Configuration.profilesDir+"/"+Util.getCurrentClient().getId()+"/"+fileName;
        //check if client already has the document with the same name
        if (new File(docPath).exists()) {
            for (int i=0; i<Integer.MAX_VALUE; i++) {
                fileName = String.valueOf(i)+"_"+fileName;
                docPath = Configuration.profilesDir+"/"+Util.getCurrentClient().getId()+"/"+fileName;
                if (! new File(docPath).exists()) {
                    break;
                }
            }
        }

        selectedDocument.setPath(fileName);


        FileOutputStream stream = new FileOutputStream(docPath);
        try {
            stream.write(IOUtils.toByteArray(file.getInputstream()));
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Документ загружен", "");
        } catch (Exception e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Документ не может быть загружен!", "Обратитесь к администратору");
        } finally {
            stream.close();
        }
        stream.close();
        if (msg != null) {
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public DocumentScan getSelectedDocument() {
        return selectedDocument;
    }

    public void setSelectedDocument(DocumentScan selectedDocument) {
        this.selectedDocument = selectedDocument;
    }

    public List<DocumentScan> getClientDocumentScans() {
        return clientDocumentScans;
    }

    public void setClientDocumentScans(List<DocumentScan> clientDocumentScans) {
        this.clientDocumentScans = clientDocumentScans;
    }

    public StreamedContent getFile() throws IOException {
        String fileName = selectedDocument.getPath();
        String docPath;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.responseReset();
        InputStream stream = null;
        if (fileName.equals("ФАЙЛ НЕ ЗАГРУЖЕН!")) {
            docPath = "/images/scanDownloadError.png";
            stream = externalContext.getResourceAsStream(docPath);
            fileName = "ERROR.png";
        } else {
            docPath = Configuration.profilesDir+"/"+Util.getCurrentClient().getId()+"/"+fileName;
            stream = new FileInputStream(docPath);
        }
        file = new DefaultStreamedContent(stream, externalContext.getMimeType(docPath), fileName);
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }
}

