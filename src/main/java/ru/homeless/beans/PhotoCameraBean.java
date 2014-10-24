package ru.homeless.beans;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.sun.xml.messaging.saaj.util.ByteInputStream;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.LobHelper;
import org.hibernate.SessionFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.SpringProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Component;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.Client;
import ru.homeless.services.ClientService;
import ru.homeless.services.GenericService;
import ru.homeless.util.Util;

import static java.nio.file.StandardCopyOption.*;

@ManagedBean (name = "photoCamBean")
@ViewScoped
@Component
public class PhotoCameraBean implements Serializable{

	private static final long serialVersionUID = 1L;
    private String filename;
	public static Logger log = Logger.getLogger(PhotoCameraBean.class);
	private String useVisible;

    @ManagedProperty(value = "#{ClientService}")
    private ClientService clientService;

    private File resultFile;

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    private String newFileName;


    public PhotoCameraBean() {
    	setUseVisible("display: none;");
    }

    public void usePhoto() throws IOException, SQLException {
        Util.applyNewPhoto(clientService, resultFile, filename);
    }

    public void onhide() {
    	//photos.clear();
    	setUseVisible("display: none;");
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update(":webcamera_form");
    }
    
    public void oncapture(CaptureEvent captureEvent) {  
    	//photos.clear();
    	setUseVisible("display: block;");
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update(":webcamera_form:b_panel");

    	
        filename = Util.getRandomImageName();
        byte[] data = captureEvent.getData();  
          
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        File destTempDir = new File(servletContext.getRealPath("") + File.separator + "images" + File.separator + "temp");
        newFileName = servletContext.getRealPath("") + File.separator + "images" + File.separator + "temp"+File.separator + filename + ".png";
        resultFile = new File(newFileName);
        if (!destTempDir.exists()) {
        	destTempDir.mkdirs();
        }
        log.info(servletContext.getServerInfo()+servletContext.getContextPath().toString());
        //String newFileName = destTempDir.getAbsolutePath() + ;

        //photos.add(photo);
        
        log.info("Trying to save image to: "+newFileName);
        /*
        for (String s : photos) {
        	log.warn("Photo "+s);
        }
          */
        FileImageOutputStream imageOutput;  
        try {  
            imageOutput = new FileImageOutputStream(new File(newFileName));  
            imageOutput.write(data, 0, data.length);  
            imageOutput.close();  
        }  
        catch(IOException e) {
            throw new FacesException("Error in writing captured image.", e);
        }
    }

    public void showDlg() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("cameraServiceWv.show();");
    }

    public void showOpenDlg() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("uploadPhotoWv.show();");
    }

    public String getUseVisible() {
		return useVisible;
	}

	public void setUseVisible(String useVisible) {
		this.useVisible = useVisible;
	}

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void deletePhoto() throws  SQLException {
        //We don't delete the photo forever!!!
        //We clean the fields in database and rename the original photo to ID_{OLD_FILENAME}
        HttpSession session = Util.getSession();
        String cid = session.getAttribute("cid").toString();
        Client client = getClientService().getInstanceById(Client.class, Integer.parseInt(cid));
        client.setAvatar(null);
        String new_filename = String.valueOf(client.getId())+"_"+client.getPhotoName();

        Path src_file = Paths.get(Configuration.photos+"/" + client.getPhotoName());
        Path dst_file = Paths.get(Configuration.photos+"/" + new_filename);
        try {
            Files.move(src_file, dst_file, REPLACE_EXISTING);
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Невозможно удалить оригинальный файл", "Его не существует в хранилище"));
            e.printStackTrace();
        }

        client.setPhotoName("");
        client.setPhotoCheckSum("");

        getClientService().updateInstance(client);

        log.info("File "+src_file+" has been renamed to "+dst_file);

        //Reloading the main bean
        //Evaluating ClientForm Bean
        FacesContext context = FacesContext.getCurrentInstance();
        ClientFormBean cf = context.getApplication().evaluateExpressionGet(context, "#{clientform}", ClientFormBean.class);
        cf.reloadAll(client.getId());
    }

}
