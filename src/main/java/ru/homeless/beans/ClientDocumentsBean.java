package ru.homeless.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import ru.homeless.converters.DocTypeConverter;
import ru.homeless.dao.ClientDocumentsDAO;
import ru.homeless.entities.DocType;
import ru.homeless.entities.Document;
import ru.homeless.entities.Worker;
import ru.homeless.util.Util;

@ManagedBean(name = "clientdocuments")
@ViewScoped
public class ClientDocumentsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ClientDocumentsBean.class);
	private int cid = 0;
	private List<Document> documentsList = null;
	private Document selectedDocument;
	private List<DocType> docTypes;

	public ClientDocumentsBean() {
	}

	public void reload() {
		HttpSession session = Util.getSession();
		String cids = session.getAttribute("cid").toString();

		if (cids != null && !cids.trim().equals("")) {
			this.cid = Integer.parseInt(cids);
			documentsList = new ClientDocumentsDAO().getAllClientDocuments(cid);
		}
		newSelectedDocument(); // set new document
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public List<Document> getDocumentsList() {
		return documentsList;
	}

	public void setDocumentsList(List<Document> documentsList) {
		this.documentsList = documentsList;
	}

	public String formatDate(Date q) {
		if (q != null && !q.equals("")) {
			return Util.formatDate(q);
		} else {
			return "";
		}
	}

	public String getStringRegistrationConfirmation(int i) {
		if (i == 0) {
			return "Не указано";
		} else {
			if (i == 1) {
				return "Нет";
			} else {
				if (i == 2) {
					return "Да";
				}
			}
		}
		return "";
	}

	public Document getSelectedDocument() {
		return selectedDocument;
	}

	public void setSelectedDocument(Document selectedDocument) {
		this.selectedDocument = selectedDocument;
	}

	public void deleteDocument() {
		ClientDocumentsDAO cd = new ClientDocumentsDAO();
		cd.deleteDocument(cd.getDocumentById(selectedDocument.getId()));
		reload();
	}

	public void editDocument() {
		selectedDocument = new ClientDocumentsDAO().getDocumentById(selectedDocument.getId());
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update("add_document");	//force updating the add document form	
	}

	public void newSelectedDocument() {
		selectedDocument = new Document();
	}

	public void addSelectedDocument() {

		// some validation
		if ((selectedDocument.getRegistration() == 0 || selectedDocument.getRegistration() == 1) && !selectedDocument.getCity().trim().equals("")) {
			selectedDocument.setRegistration(2);
		}
		if ((selectedDocument.getRegistration() == 0 || selectedDocument.getRegistration() == 1) && !selectedDocument.getAddress().trim().equals("")) {
			selectedDocument.setRegistration(2);
		}

		//finally, set the client
		selectedDocument.setClient(cid);
		
		//... and the worker
		HttpSession httpsession = Util.getSession();
		selectedDocument.setWorker((Worker) httpsession.getAttribute("worker"));
		
		new ClientDocumentsDAO().updateDocument(selectedDocument);
		reload(); //for updating related view
	}

	/* Validators */
	public void validatePrefix(FacesContext ctx, UIComponent component, Object value) {
	}

	public void validateNumber(FacesContext ctx, UIComponent component, Object value) {
	}

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

	public void validateWho(FacesContext ctx, UIComponent component, Object value) {
	}

	public void validateCity(FacesContext ctx, UIComponent component, Object value) {
	}

	public void validateAddress(FacesContext ctx, UIComponent component, Object value) {
	}

	public List<DocType> getDocTypes() {
		return docTypes;
	}

	public void setDocTypes(List<DocType> docTypes) {
		this.docTypes = docTypes;
	}

	@PostConstruct
	// special for converter!
	public void init() {
		setDocTypes(DocTypeConverter.docTypesDB);
	}

}
