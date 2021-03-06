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
import ru.homeless.entities.ServicesType;
import ru.homeless.services.GenericService;
import ru.homeless.util.Util;

@ManagedBean(name = "clientdocuments")
@ViewScoped
public class ClientDocumentsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ClientDocumentsBean.class);
	private List<Document> documentsList = null;

    private List<Document> documentsWithAbsentRegistrationList = null;
	private Document selectedDocument;
	private List<DocType> docTypes;
	
	@ManagedProperty(value = "#{GenericService}")
	private GenericService genericService;

    private String tempRegVisibility;

	public ClientDocumentsBean() {
	}

    private void toggleTempRegVisibility(boolean value) {
        if (value)
            this.tempRegVisibility = "display: block;";
        else this.tempRegVisibility = "display: none;";
    }

    public void itemSelected() {
        RequestContext rc = RequestContext.getCurrentInstance();

        if (selectedDocument.getRegistration() == 3) {
            toggleTempRegVisibility(true);
        } else {
            toggleTempRegVisibility(false);
            selectedDocument.setTempRegDateFrom(null);
            selectedDocument.setTempRegDateTo(null);
        }

        rc.update("add_document:tempRegDateFrom");
        rc.update("add_document:tempRegDateFrom_l");
        rc.update("add_document:tempRegDateTo");
        rc.update("add_document:tempRegDateTo_l");
    }


    public void reload() {
		HttpSession session = Util.getSession();
        if (Util.getCurrentClient() != null) {
            documentsList = getGenericService().getInstancesByClientId(Document.class, Util.getCurrentClient().getId());
            documentsWithAbsentRegistrationList = new ArrayList<>();
            for (Document d : documentsList) {
                if (d.getDoctype().getId() == 1) { //this is Passport, Parent's Passport or Birth Document
                    if (d.getRegistration() == 1) { //Registration is "Нет"
                        //only in that case we can create a basic document, because the client has a passport, but don't have a registration
                        documentsWithAbsentRegistrationList.add(d);
                    }
                }
                if (d.getDoctype().getId() == 3) { //this is Birth Document
                    documentsWithAbsentRegistrationList.add(d);
                }
                if (d.getDoctype().getId() == 13) { //this is Khodataystvo
                    documentsWithAbsentRegistrationList.add(d);
                }
                if (d.getDoctype().getId() == 21) { //this is Parent's Passport
                    if (d.getRegistration() == 1) { //Registration is "Нет"
                        //only in that case we can create a basic document, because the client has a passport, but don't have a registration
                        documentsWithAbsentRegistrationList.add(d);
                    }
                }

            }
            newSelectedDocument(); // set new document
            this.tempRegVisibility = "display: none;";

            RequestContext rc = RequestContext.getCurrentInstance();
            FacesContext context = FacesContext.getCurrentInstance();
            ClientFormBean cdb = context.getApplication().evaluateExpressionGet(context, "#{clientform}", ClientFormBean.class);
            cdb.updateDocumentsTabHeader();

            rc.update("m_tabview:documents_form:doclistId");
        }

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

	public String getStringRegistrationConfirmation(int i, int j) {
        String result = "";
        switch (i) {
            case 0: { result = "Не указано"; break; }
            case 1: { result = "Нет"; break; }
            case 2: { result = "Да"; break; }
            case 3: {
                Document td = getGenericService().getInstanceById(Document.class, j);
                result = "Временная (c "+Util.formatDate(td.getTempRegDateFrom()) + " по "+Util.formatDate(td.getTempRegDateTo())+")";
                break;
            }
        }
		return result;
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
        itemSelected();
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
		selectedDocument.setClient(Util.getCurrentClient().getId());

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
        rc.update("add_document:add_docs_panel_grid");
        //rc.update("addDocumentWv");
        rc.execute("PF('addDocumentWv').show()");
    }

    public String getTempRegVisibility() {
        return tempRegVisibility;
    }

    public void setTempRegVisibility(String tempRegVisibility) {
        this.tempRegVisibility = tempRegVisibility;
    }

    public List<Document> getDocumentsWithAbsentRegistrationList() {
        return documentsWithAbsentRegistrationList;
    }

    public void setDocumentsWithAbsentRegistrationList(List<Document> documentsWithAbsentRegistrationList) {
        this.documentsWithAbsentRegistrationList = documentsWithAbsentRegistrationList;
    }

}

