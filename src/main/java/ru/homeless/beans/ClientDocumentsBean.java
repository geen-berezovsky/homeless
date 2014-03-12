package ru.homeless.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ru.homeless.dao.ClientDocumentsDAO;
import ru.homeless.dao.DocTypesDAO;
import ru.homeless.dao.NightStayDAO;
import ru.homeless.entities.DocType;
import ru.homeless.entities.Document;
import ru.homeless.entities.NightStay;
import ru.homeless.util.Util;

@ManagedBean (name = "clientdocuments")
@SessionScoped
public class ClientDocumentsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ClientDocumentsBean.class);
	private int cid = 12783;
	private List<Document> documentsList = null;
	private Document selectedDocument;
	private List<DocType> docTypes;
	
	public ClientDocumentsBean() {
	}
	
	public void reload() {
		HttpSession session = Util.getSession();
		String cids = session.getAttribute("cid").toString();
		
		if (cids != null && ! cids.trim().equals("")) {
			this.cid = Integer.parseInt(cids);
			log.info("Reloading docuemnts for client "+cid);
			documentsList = new ClientDocumentsDAO().getAllClientDocuments(cid);
			for (Document d : documentsList) {
				log.info(d.getId());
			}
			
		}

	}

	public List<String> getDocTypes() {
		DocTypesDAO nsDAO = new DocTypesDAO();
		List<String> l = new ArrayList<String>();
		for (DocType ns : nsDAO.getAvailbleDocTypes()) {
			l.add(ns.getCaption());
		}
		return l;
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
		log.info("Document id="+selectedDocument.getId()+" has been deleted");
	}
	public void editDocument() {
		log.info("Editing document id="+selectedDocument.getId());
	}


	public void setDocTypes(List<DocType> docTypes) {
		this.docTypes = docTypes;
	}

	public void newSelectedDocument() {
		selectedDocument = new Document();
		log.info("Setting new document");
	}
	
	public void addSelectedDocument() {
		log.info("Selected data:");
		log.info("\t"+selectedDocument.getDoctype().getCaption());
		log.info("\t"+selectedDocument.getDocPrefix());
		log.info("\t"+selectedDocument.getDocNum());
	}

	/* Validators */
	public void validatePrefix(FacesContext ctx, UIComponent component, Object value) { }
	public void validateNumber(FacesContext ctx, UIComponent component, Object value) { }
	public void validateDate(FacesContext ctx, UIComponent component, Object value) { }
	public void validateWho(FacesContext ctx, UIComponent component, Object value) { }
	public void validateCity(FacesContext ctx, UIComponent component, Object value) { }
	public void validateAddress(FacesContext ctx, UIComponent component, Object value) { }

	
	
}
