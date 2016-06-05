package ru.homeless.beans;

import com.google.common.collect.Tables;
import org.apache.log4j.Logger;
import org.primefaces.model.StreamedContent;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.*;
import ru.homeless.services.CustDocService;
import ru.homeless.services.StdDocService;
import ru.homeless.util.Util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "custdoc")
@SessionScoped
public class CustDocBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(CustDocBean.class);

    @ManagedProperty(value = "#{CustDocService}")
	private CustDocService custDocService;

    private List<CustDocEntity> custDocumentList;
    private CustDocEntity selectedEntity;
    private StreamedContent file;


    public void downloadContract(int id) throws IOException {

        //User can click to Download Button, but don't select the string in p:dataTable, so the button will be pre-generated with ID of CustDocEntity list
        for (CustDocEntity entity : getCustDocumentList()) {
            if (entity.getId() == id) {
                selectedEntity = entity;
                break;
            }
        }
        if (selectedEntity!=null) {
            if (selectedEntity.getObject() instanceof CustomDocumentRegistry) {
                CustomDocumentRegistry customDocumentRegistry = (CustomDocumentRegistry) selectedEntity.getObject();
                String requestSuffix = "/getGeneratedWordDocument?requestType=16&clientId="+ customDocumentRegistry.getClient() + "&docId=" + customDocumentRegistry.getId() + "&workerId="+customDocumentRegistry.getPerformerId();
                String saveFilePath = "/tmp" + File.separator + "StandardDocument.docx";
                String docType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                String docName = "StandardDocument.docx";
                file = Util.downloadDocument(requestSuffix, saveFilePath, docType, docName);
            }
            if (selectedEntity.getObject() instanceof ZAGSRequestDocumentRegistry) {
                ZAGSRequestDocumentRegistry zagsRequestDocumentRegistry = (ZAGSRequestDocumentRegistry) selectedEntity.getObject();
                String requestSuffix = "/getGeneratedWordDocument?requestType=14&clientId="+ zagsRequestDocumentRegistry.getClient() + "&docId=" + zagsRequestDocumentRegistry.getId() + "&workerId="+zagsRequestDocumentRegistry.getPerformerId();
                String saveFilePath = "/tmp" + File.separator + "ZAGSRequestDocument.docx";
                String docType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                String docName = "ZAGSRequestDocument.docx";
                file = Util.downloadDocument(requestSuffix, saveFilePath, docType, docName);
            }
        }
    }

    public void deleteSelectedDocument() {
        if (selectedEntity!=null) {
            getCustDocService().deleteInstance(selectedEntity.getObject());
        }
    }

    public CustDocService getCustDocService() {
        return custDocService;
    }

    public void setCustDocService(CustDocService custDocService) {
        this.custDocService = custDocService;
    }


    public List<CustDocEntity> getCustDocumentList() {
        HttpSession session = Util.getSession();
        Client client = (Client) session.getAttribute("client");
        if (client != null && client.getId() != 0) {
            return getCustDocService().getCustDocEntitiesList(client.getId());
        } else
        {
            return new ArrayList<>();
        }
    }

    public void setCustDocumentList(List<CustDocEntity> custDocumentList) {
        this.custDocumentList = custDocumentList;
    }

    public CustDocEntity getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(CustDocEntity selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }
}
