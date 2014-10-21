package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import ru.homeless.services.ClientService;
import ru.homeless.util.Util;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by maxim on 21.10.14.
 */
@ManagedBean (name = "fileUploadView")
@ViewScoped
public class FileUploadView {

    public static Logger log = Logger.getLogger(FileUploadView.class);

    private UploadedFile file;
    private File tempFile;

    @ManagedProperty(value = "#{ClientService}")
    private ClientService clientService;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void upload() throws IOException, SQLException {
        if(file != null && !file.getFileName().trim().equals("")) {
            log.info("Uploaded filename = "+file.getFileName());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Отлично! Файл ", file.getFileName() + " успешно загружен!");
            FacesContext.getCurrentInstance().addMessage(null, message);

            tempFile = new File((System.getProperty("java.io.tmpdir")+"/"+String.valueOf(new Date().getTime()))+".png");
            log.info("Saving uploaded file to "+tempFile.getAbsolutePath());

            BufferedImage bufferedImage = ImageIO.read(file.getInputstream());
            ImageIO.write(bufferedImage, "png", tempFile);

            Util.applyNewPhoto(clientService, tempFile, Util.getRandomImageName());
            //RequestContext rc = RequestContext.getCurrentInstance();
            //rc.update("photoUp");
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Неудача! Файл ", file.getFileName() + " не был загружен!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

}
