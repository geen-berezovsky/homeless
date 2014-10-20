package ru.homeless.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import ru.homeless.converters.DocTypeConverter;
import ru.homeless.entities.DocType;
import ru.homeless.entities.Document;
import ru.homeless.services.GenericService;
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
	
	@ManagedProperty(value = "#{GenericService}")
	private GenericService genericService;

	public ClientDocumentsBean() {
	}

	public void reload() {
		HttpSession session = Util.getSession();
		String cids = session.getAttribute("cid").toString();

		if (cids != null && !cids.trim().equals("")) {
			this.cid = Integer.parseInt(cids);
			documentsList = getGenericService().getInstancesByClientId(Document.class, cid);
		}
		newSelectedDocument(); // set new document
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update("doclistId");	
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
		getGenericService().deleteInstance(getGenericService().getInstanceById(Document.class, selectedDocument.getId()));
		reload();
	}

	public void editDocument() {
		selectedDocument = getGenericService().getInstanceById(Document.class, selectedDocument.getId());
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update("add_document");	//force updating the add document form	
	}

	public void newSelectedDocument() {
		selectedDocument = new Document();
	}

	
	public void addSelectedDocument() {

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
		docTypes = new ArrayList<DocType>();
		docTypes.addAll(getGenericService().getInstances(DocType.class));
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
        rc.update("add_document:add_docs_panel_grid");
        //rc.update("addDocumentWv");
        rc.execute("addDocumentWv.show()");
    }

}

