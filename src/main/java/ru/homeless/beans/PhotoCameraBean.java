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

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
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


    private String getRandomImageName() {  
        int i = (int) (Math.random() * 10000000);  
        return String.valueOf(i);
    }  

    public PhotoCameraBean() {
    	setUseVisible("display: none;");
    }

    public void usePhoto() throws SQLException {
        //1. check that user don't have actual photo, or delete it from the target directory
    	//2. copy source file to the target directory with the new name
        //3. make an avatar and save it to the database
        HttpSession session = Util.getSession();
        String cid = session.getAttribute("cid").toString();
        Client client = getClientService().getInstanceById(Client.class, Integer.parseInt(cid));
        log.info("Working with client "+client.getSurname());

        //PREPARE BUFFERED IMAGE AND SET ITS SIZE
        BufferedImage bi = new BufferedImage(177, 144, BufferedImage.TYPE_INT_ARGB);

        //READING IMAGE FROM DISK TO BYTE ARRAY
        byte[] bytes = new byte[(int) resultFile.length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(resultFile);
            fis.read(bytes);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //CONVERT BYTE ARRAY TO THE BUFFERED IMAGE
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            bi = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage resizedImage = new BufferedImage(177, 144, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bi, 0, 0, 177, 144, null);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] resizedBytes;
        try {
            ImageIO.write(resizedImage, "png", baos);
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resizedBytes = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //SAVING DATA
        if (!getClientService().setClientAvatar(client, resizedBytes)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Аватар не сохранен!", "Подробности в логе.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        //Update the client's data on page

        InputStream imageInByteArray = new ByteArrayInputStream(baos.toByteArray());
        imageInByteArray = new ByteArrayInputStream(resizedBytes);

        //Evaluating ClientForm Bean
        FacesContext context = FacesContext.getCurrentInstance();
        ClientFormBean cf = context.getApplication().evaluateExpressionGet(context, "#{clientform}", ClientFormBean.class);
        //cf.refreshTabs();
        //cf.setClientFormAvatar(new DefaultStreamedContent(imageInByteArray, "image/png"));
        cf.reloadAll();

        //RequestContext rc = RequestContext.getCurrentInstance();
        //rc.update("photo_main_avatar");

        //NOW MOVE THE ORIGINAL FILE FROM CACHE TO THE STORAGE
        Path src_file = Paths.get(newFileName);
        File pf = new File(Configuration.photos);
        if (! pf.exists()) {
            pf.mkdirs();
        }

        Path dst_file = Paths.get(Configuration.photos+"/" + filename + ".png");


        try {
            Files.move(src_file, dst_file, REPLACE_EXISTING);
        } catch (IOException e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Оригинал фото не сохранен!", "Подробности в логе.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }

        log.info(Configuration.photos+"/"+ filename + ".png");

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

    	
        filename = getRandomImageName();
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
}
