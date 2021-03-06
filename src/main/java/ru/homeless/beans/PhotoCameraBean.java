package ru.homeless.beans;

import java.io.*;
import java.nio.file.*;
import java.sql.SQLException;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import org.springframework.stereotype.Component;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.Client;
import ru.homeless.services.ClientService;
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
        setUseVisible("display: none;");
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
        log.info(servletContext.getServerInfo()+servletContext.toString());
        log.info("Trying to save image to: "+newFileName);
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
        context.execute("PF('cameraServiceWv').show();");
    }

    public void showOpenDlg() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('uploadPhotoWv').show();");
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

    public void deleteActualPhoto(Client client) {
        String new_filename = String.valueOf(client.getId()) + "_" + client.getPhotoName();
        Path src_file = Paths.get(Configuration.photos, client.getPhotoName());
        if (!Files.exists(src_file)) {
            src_file = Paths.get(Configuration.profilesDir, String.valueOf(client.getId()),
                    client.getPhotoName());
        }
        Path dst_file = Paths.get(Configuration.profilesDir, String.valueOf(client.getId()),
                new_filename);
        try {
            Files.move(src_file, dst_file, REPLACE_EXISTING);
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Невозможно удалить оригинальный файл", "Его не существует в хранилище"));
            e.printStackTrace();
        }
        log.info("File " + src_file + " has been renamed to " + dst_file);
    }

    public void deletePhoto() throws SQLException {
        //We don't delete the photo forever!!!
        //We clean the fields in database and rename the original photo to ID_{OLD_FILENAME}
        Client client = Util.getCurrentClient();
        client.setAvatar(null);
        if (client.getPhotoName() != null) {
            deleteActualPhoto(client);
        }
        client.setPhotoName("");
        client.setPhotoCheckSum("");
        getClientService().updateInstance(client);
        //Reloading the main bean
        //Evaluating ClientForm Bean
        FacesContext context = FacesContext.getCurrentInstance();
        ClientFormBean cf = context.getApplication().evaluateExpressionGet(context, "#{clientform}", ClientFormBean.class);
        cf.reloadAll();
    }

}
