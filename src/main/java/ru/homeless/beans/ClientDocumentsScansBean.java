package ru.homeless.beans;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.MimeTypes;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import ru.homeless.converters.DocTypeConverter;
import ru.homeless.entities.DocType;
import ru.homeless.entities.Document;
import ru.homeless.services.GenericService;
import ru.homeless.util.Util;
import sun.net.www.MimeEntry;
import sun.net.www.MimeTable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@ManagedBean(name = "clientdocumentscan")
@ViewScoped
public class ClientDocumentsScansBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ClientDocumentsScansBean.class);

	@ManagedProperty(value = "#{GenericService}")
	private GenericService genericService;

	public ClientDocumentsScansBean() {
	}

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

        FileOutputStream stream = new FileOutputStream("C:/tmp/someshit."+getFileExt(file.getContentType()));
        try {
            stream.write(IOUtils.toByteArray(file.getInputstream()));
        } finally {
            stream.close();
        }
        stream.close();
    }

    public GenericService getGenericService() {
        return genericService;
    }

    public void setGenericService(GenericService genericService) {
        this.genericService = genericService;
    }
}

