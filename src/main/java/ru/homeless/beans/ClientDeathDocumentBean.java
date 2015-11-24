package ru.homeless.beans;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.Client;
import ru.homeless.services.GenericService;
import ru.homeless.util.Util;
import sun.net.www.MimeEntry;
import sun.net.www.MimeTable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean(name = "clientdeathdocument")
@ViewScoped
public class ClientDeathDocumentBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ClientDeathDocumentBean.class);

	@ManagedProperty(value = "#{GenericService}")
	private GenericService genericService;

	public ClientDeathDocumentBean() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        String dateInString = "01.01.1900 00:00:00";
        nullDate = sdf.parse(dateInString);
    }


    private Date deathDate;
    private String deathReason;
    private String deathCity;
    private Client client;
    private String deathDocPath;
    private Date nullDate;

    public static String getFileExt(String mimeType) {
        String extension = "";

        MimeTable testTable = MimeTable.getDefaultTable();
        Enumeration e = testTable.elements();
        while (e.hasMoreElements()) {
            MimeEntry entry = (MimeEntry) e.nextElement();
            String contentType = entry.getType();
            String extensionString = entry.getExtensionsAsList();
            String[] extensionArray = extensionString.split(",");
            extensionString = extensionArray[extensionArray.length - 1];
            mimeType = mimeType.replaceAll("/", ".*");
            if (contentType.matches(mimeType)) {
                extension = extensionString;
                break;
            }
        }
        return extension;
    }

    public void handleDeathDocUpload(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();
        HttpSession session = Util.getSession();
        String clientIdStr = session.getAttribute("cid").toString();
        FacesMessage msg = null;

        String extension = getFileExt(file.getContentType());
        if (extension.equals(".jpeg")) {
            extension = ".jpg";
        }

        deathDocPath = Configuration.profilesDir+"/"+clientIdStr+"/"+clientIdStr+"-deathCert"+extension;
        client = getGenericService().getInstanceById(Client.class, Integer.parseInt(session.getAttribute("cid").toString()));
        client.setDeathDocPath(deathDocPath);
        getGenericService().updateInstance(client);

        FileOutputStream stream = new FileOutputStream(deathDocPath);
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

    public void updateDeathFields() throws ParseException {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("clientDeathWv.show();");
        HttpSession session = Util.getSession();
        client = getGenericService().getInstanceById(Client.class, Integer.parseInt(session.getAttribute("cid").toString()));

        if (client.getDeathDate() == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            String dateInString = "01.01.1900";
            deathDate = sdf.parse(dateInString);
        } else {
            deathDate = client.getDeathDate();
        }

        if (client.getDeathDocPath() == null) {
            deathDocPath = "";
        } else{
            deathDocPath = client.getDeathDocPath();
        }

        if (client.getDeathCity() == null) {
            deathCity = "";
        } else {
            deathCity = client.getDeathCity();
        }
        if (client.getDeathReason() == null) {
            deathReason = "";
        } else {
            deathReason = client.getDeathReason();
        }
        requestContext.update(":clientDeathForm");

    }

    public void saveClientDeath() throws SQLException {
        FacesMessage msg = null;
        if (deathDate.equals(nullDate)) {
            client.setDeathDate(null);
        } else {
            client.setDeathDate(deathDate);
        }

        if (deathReason == null || deathReason.trim().equals("")) {
            client.setDeathReason(null);
        } else {
            client.setDeathReason(deathReason);
        }

        if (deathCity == null || deathCity.trim().equals("")) {
            client.setDeathCity(null);
        } else {
            client.setDeathCity(deathCity);
        }

        try {
            getGenericService().updateInstance(client);

            FacesContext context = FacesContext.getCurrentInstance();
            ClientFormBean cdb = context.getApplication().evaluateExpressionGet(context, "#{clientform}", ClientFormBean.class);
            cdb.reloadAll(Integer.parseInt(Util.getSession().getAttribute("cid").toString()));
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Данные сохранены", "");
        } catch (Exception e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Невозможно сохранить данные!", "Обратитесь к администратору");
            log.error(e);
        }
        if (msg != null) {
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public GenericService getGenericService() {
        return genericService;
    }

    public void setGenericService(GenericService genericService) {
        this.genericService = genericService;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public String getDeathCity() {
        return deathCity;
    }

    public void setDeathCity(String deathCity) {
        this.deathCity = deathCity;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getDeathDocPath() {
        return deathDocPath;
    }

    public void setDeathDocPath(String deathDocPath) {
        this.deathDocPath = deathDocPath;
    }

    public Date getNullDate() {
        return nullDate;
    }

    public void setNullDate(Date nullDate) {
        this.nullDate = nullDate;
    }
}

