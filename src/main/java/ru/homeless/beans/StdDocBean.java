package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.BasicDocumentRegistry;
import ru.homeless.entities.Client;
import ru.homeless.entities.Worker;
import ru.homeless.services.GenericService;
import ru.homeless.services.StdDocService;
import ru.homeless.services.WorkerService;
import ru.homeless.util.Util;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "stddoc")
@SessionScoped
public class StdDocBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(StdDocBean.class);

    @ManagedProperty(value = "#{StdDocService}")
	private StdDocService stdDocService;

    public List<BasicDocumentRegistry> getStandardDocumentList() {
        return getDocumentsList(false);
    }

    public void deleteSelectedDocument() throws SQLException {
        getStdDocService().deleteInstance(selectedStdDoc);
        FacesContext context = FacesContext.getCurrentInstance();
        ClientFormBean cfb = context.getApplication().evaluateExpressionGet(context, "#{clientform}", ClientFormBean.class);
        cfb.reloadAll();
        refreshLists(false);
    }

    public void deleteSelectedTranzitDocument() {
        getStdDocService().deleteInstance(selectedTrDoc);
        refreshLists(true);
    }

    public void refreshLists(Boolean b) {
        if (b == null) {
            refreshLists(true);
            refreshLists(false);
            return;
        }

        if (b) {
            if (tranzitDocumentList != null) {
                tranzitDocumentList.clear();
            }
            tranzitDocumentList = getTranzitDocumentList();
        } else {
            if (standardDocumentList != null) {
                standardDocumentList.clear();
            }
            standardDocumentList = getStandardDocumentList();
        }
    }

    public void setStandardDocumentList(List<BasicDocumentRegistry> standardDocumentList) {
        this.standardDocumentList = standardDocumentList;
    }

    /*
        public List<BasicDocumentRegistry> getStandardDocumentList() {
            return new ArrayList<BasicDocumentRegistry>();
        }
    */
    private List<BasicDocumentRegistry> standardDocumentList;

    public List<BasicDocumentRegistry> getTranzitDocumentList() {
        return getDocumentsList(true);
    }

    private List<BasicDocumentRegistry> getDocumentsList(boolean b) {
        HttpSession session = Util.getSession();
        if (session != null && session.getAttribute("cid")!=null) {
            String cids = session.getAttribute("cid").toString();
            if (cids != null && !cids.trim().equals("")) {
                if (b) {
                    //return tranzit documents
                    return getStdDocService().getTranzitDocumentsList(Integer.parseInt(cids));
                } else {
                    //return basic documents
                    return getStdDocService().getStandardDocumentsList(Integer.parseInt(cids));
                }
            }
        }
        return new ArrayList<>();
    }

    public void setTranzitDocumentList(List<BasicDocumentRegistry> tranzitDocumentList) {
        this.tranzitDocumentList = tranzitDocumentList;
    }

    private List<BasicDocumentRegistry> tranzitDocumentList;

    private BasicDocumentRegistry selectedStdDoc;

    public BasicDocumentRegistry getSelectedTrDoc() {
        return selectedTrDoc;
    }

    public void setSelectedTrDoc(BasicDocumentRegistry selectedTrDoc) {
        this.selectedTrDoc = selectedTrDoc;
    }

    private BasicDocumentRegistry selectedTrDoc;

    public BasicDocumentRegistry getSelectedStdDoc() {
        return selectedStdDoc;
    }

    public void setSelectedStdDoc(BasicDocumentRegistry selectedStdDoc) {
        this.selectedStdDoc = selectedStdDoc;
    }

    public String formatDate(Date q) {
        if (q != null && !q.equals("")) {
            return Util.formatDate(q);
        } else {
            return "";
        }
    }

    public String getDocumentIdStr(Integer value) {
        if (value == 0) return ""; else
            return String.valueOf(value);
    }

    public String getWorkerName(int i) {
        Worker w = getStdDocService().getInstanceById(Worker.class, i);
        return w.getSurname()+" "+w.getFirstname();
    }

    public StdDocBean() {
        standardDocumentList = new ArrayList<>();
    }

    public StdDocService getStdDocService() {
        return stdDocService;
    }

    public void setStdDocService(StdDocService stdDocService) {
        this.stdDocService = stdDocService;
    }


}
