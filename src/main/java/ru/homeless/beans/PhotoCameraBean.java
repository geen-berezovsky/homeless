package ru.homeless.beans;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import ru.homeless.entities.Client;
import ru.homeless.services.GenericService;
import ru.homeless.util.Util;

@ManagedBean (name = "photoCamBean")
@ViewScoped
public class PhotoCameraBean implements Serializable{

	private static final long serialVersionUID = 1L;
	//private List<String> photos = new ArrayList<String>();
    private String filename;
	public static Logger log = Logger.getLogger(PhotoCameraBean.class);
	private String useVisible;
    @ManagedProperty(value = "#{GenericService}")
    private GenericService genericService;


    private String getRandomImageName() {  
        int i = (int) (Math.random() * 10000000);  
        return String.valueOf(i);
    }  
/*
    public List<String> getPhotos() {
        return photos;  
    }  
*/
    public PhotoCameraBean() {
    	setUseVisible("display: none;");
    }

    public void usePhoto() {
        //1. check that user don't have actual photo, or delete it from the target directory
    	//2. copy source file to the target directory with the new name
        //3. make an avatar and save it to the database
        HttpSession session = Util.getSession();
        String cid = session.getAttribute("cid").toString();
        Client client = getGenericService().getInstanceById(Client.class, Integer.parseInt(cid));
        log.info("Working with client "+client.getSurname());

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

    public GenericService getGenericService() {
        return genericService;
    }

    public void setGenericService(GenericService genericService) {
        this.genericService = genericService;
    }
}
