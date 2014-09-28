package ru.homeless.beans;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Blob;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.homeless.entities.Client;
import ru.homeless.services.ClientService;
import ru.homeless.services.GenericService;
import ru.homeless.util.Util;

@ManagedBean (name = "photoCamBean")
@ViewScoped
public class PhotoCameraBean implements Serializable{

	private static final long serialVersionUID = 1L;
    private String filename;
	public static Logger log = Logger.getLogger(PhotoCameraBean.class);
	private String useVisible;

    @ManagedProperty(value = "#{ClientService}")
    private ClientService clientService;

    private File resultFile;


    private String getRandomImageName() {  
        int i = (int) (Math.random() * 10000000);  
        return String.valueOf(i);
    }  

    public PhotoCameraBean() {
    	setUseVisible("display: none;");
    }

    public void usePhoto() {
        //1. check that user don't have actual photo, or delete it from the target directory
    	//2. copy source file to the target directory with the new name
        //3. make an avatar and save it to the database
        HttpSession session = Util.getSession();
        String cid = session.getAttribute("cid").toString();
        Client client = getClientService().getInstanceById(Client.class, Integer.parseInt(cid));
        log.info("Working with client "+client.getSurname());

        //PREPARE BUFFERED IMAGE AND SET ITS SIZE
        BufferedImage bi = new BufferedImage(177, 144, BufferedImage.TYPE_INT_ARGB);
        /*

        Graphics2D g = bi.createGraphics();
        Stroke drawingStroke = new BasicStroke(3);
        Rectangle2D rect = new Rectangle2D.Double(0, 0, 177, 144);
        g.setStroke(drawingStroke);
        g.draw(rect);
        g.setPaint(Color.LIGHT_GRAY);
        g.fill(rect);
*/
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
        Image scaledImage = bi.getScaledInstance(177, 144, Image.SCALE_SMOOTH);
        bi.getGraphics().drawImage(scaledImage, 0, 0, new Color(0,0,0), null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] resizedBytes;
        try {
            ImageIO.write(bi, "png", baos);
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
        log.info("Resized bytes = "+resizedBytes.length);

        //SAVING DATA
        if (!getClientService().setClientAvatar(client, resizedBytes)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Аватар не сохранен!", "Подробности в логе.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }


        RequestContext context = RequestContext.getCurrentInstance();
        context.update("main_avatatr");

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
        String newFileName = servletContext.getRealPath("") + File.separator + "images" + File.separator + "temp"+File.separator + filename + ".png";
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
