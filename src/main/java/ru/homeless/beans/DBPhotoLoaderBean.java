package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.Client;
import ru.homeless.services.ClientService;
import ru.homeless.util.Util;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by maxim on 29.05.2016.
 */
@ManagedBean
@RequestScoped
public class DBPhotoLoaderBean {

    @ManagedProperty(value = "#{ClientService}")
    private ClientService clientService;

    public static Logger log = Logger.getLogger(DBPhotoLoaderBean.class);

    private StreamedContent prepareStub(boolean small) {
        InputStream imageInByteArray = null;

        BufferedImage bi = null;
        Rectangle2D rect = null;
        if (small) {
            bi = new BufferedImage(177, 144, BufferedImage.TYPE_INT_ARGB);
            rect = new Rectangle2D.Double(0, 0, 177, 144);
        } else {
            bi = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
            rect = new Rectangle2D.Double(0, 0, 640, 480);
        }

        Graphics2D g = bi.createGraphics();
        Stroke drawingStroke = new BasicStroke(3);


        g.setStroke(drawingStroke);
        g.draw(rect);
        g.setPaint(Color.LIGHT_GRAY);
        g.fill(rect);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            baos.flush();
            imageInByteArray = new ByteArrayInputStream(baos.toByteArray());
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DefaultStreamedContent(imageInByteArray, "image/png");
    }

    public StreamedContent getClientFormAvatar() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String id = context.getExternalContext().getRequestParameterMap().get("clientId");
            InputStream imageInByteArray = null;
            if (!id.trim().equals("") && !id.equals("0")) {
                log.debug("Requested photo of client with id " + id);
                Client client = clientService.getInstanceById(Client.class, Integer.parseInt(id));
                if (client.getAvatar() == null || client.getAvatar().toString().trim().equals("")) {
                    return prepareStub(true);
                }
                try {
                    imageInByteArray = client.getAvatar().getBinaryStream();
                    return new DefaultStreamedContent(imageInByteArray, "image/png");
                } catch (SQLException e) {
                    log.error(e.getMessage(), e);
                }
            } else {
                return prepareStub(true);
           }
        }
        return new DefaultStreamedContent();
    }

    public String getOriginalPhotoFilePathByClient(Client client) {
        if (client.getPhotoName() == null) {
            return null;
        }
        File f = Paths.get(Configuration.photos, client.getPhotoName()).toFile();
        if (!f.exists()) {
            f = Paths.get(Configuration.profilesDir, String.valueOf(client.getId()),
                    client.getPhotoName()).toFile();
        }
        String path = f.getAbsolutePath();
        return f.exists() ? path : String.format("%s не найден в хранилище.", path);
    }



    public StreamedContent getClientFormRealPhoto() throws IOException {

        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String id = context.getExternalContext().getRequestParameterMap().get("clientId");
            log.info("Requested original photo of client with id " + id);

            InputStream imageInByteArray = null;
            if (!id.trim().equals("") && !id.equals("0")) {
                Client client = clientService.getInstanceById(Client.class, Integer.parseInt(id));

                if (client == null) {
                    return null;
                }
                if (client.getAvatar() == null || client.getAvatar().toString().trim().equals("")) {
                    return prepareStub(false);
                }

                File resultFile = new File(getOriginalPhotoFilePathByClient(client));
                if (!resultFile.exists() || client.getPhotoCheckSum().trim().equals("") || client.getPhotoName().trim().equals("")) {
                    return null;
                } else {
                    return Util.loadResizedPhotoFromDisk(resultFile);
                }
            } else {
                return prepareStub(false);
            }
        }
    }

    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

}
