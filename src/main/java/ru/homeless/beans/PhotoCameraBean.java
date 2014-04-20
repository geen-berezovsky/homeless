package ru.homeless.beans;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;

@ManagedBean (name = "photoCamBean")
@ViewScoped
public class PhotoCameraBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<String> photos = new ArrayList<String>();
	public static Logger log = Logger.getLogger(PhotoCameraBean.class);
	private String useVisible;
    
    private String getRandomImageName() {  
        int i = (int) (Math.random() * 10000000);  
          
        return String.valueOf(i);  
    }  
  
    public List<String> getPhotos() {
        return photos;  
    }  

    public PhotoCameraBean() {
    	setUseVisible("display: none;");
    }
    
    public void usePhoto() {
    	//String photo = photos.get(photos.size()-1);
    	photos.clear();
    }
    
    public void onhide() {
    	photos.clear();
    	setUseVisible("display: none;");
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update(":webcamera_form");
    }
    
    public void oncapture(CaptureEvent captureEvent) {  
    	photos.clear();
    	setUseVisible("display: block;");
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.update(":webcamera_form:b_panel");

    	
        String photo = getRandomImageName();  
        byte[] data = captureEvent.getData();  
          
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        File destTempDir = new File(servletContext.getRealPath("") + File.separator + "images" + File.separator + "temp");
        if (!destTempDir.exists()) {
        	destTempDir.mkdirs();
        }
        
        String newFileName = destTempDir.getAbsolutePath() + File.separator + photo + ".png";
        
        photos.add(photo);
        
        log.info("Trying to save image to: "+newFileName);
        
        for (String s : photos) {
        	log.warn("Photo "+s);
        }
          
        FileImageOutputStream imageOutput;  
        try {  
            imageOutput = new FileImageOutputStream(new File(newFileName));  
            imageOutput.write(data, 0, data.length);  
            imageOutput.close();  
        }  
        catch(Exception e) {  
            throw new FacesException("Error in writing captured image.");  
        }  
    }

	public String getUseVisible() {
		return useVisible;
	}

	public void setUseVisible(String useVisible) {
		this.useVisible = useVisible;
	}  

}
