package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import ru.homeless.entities.Client;
import ru.homeless.services.ClientService;

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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    private int avaId;

    public static Logger log = Logger.getLogger(DBPhotoLoaderBean.class);

    public StreamedContent getClientFormAvatar() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String id = context.getExternalContext().getRequestParameterMap().get("clientId");
            log.info("Requested photo of client with id " + id);

            InputStream imageInByteArray = null;
            if (!id.trim().equals("") && !id.equals("0")) {
                Client client = clientService.getInstanceById(Client.class, Integer.parseInt(id));
                try {
                    imageInByteArray = client.getAvatar().getBinaryStream();
                } catch (SQLException e) {
                    log.error(e.getMessage(), e);
                }
            } else {
                BufferedImage bi = new BufferedImage(177, 144, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = bi.createGraphics();
                Stroke drawingStroke = new BasicStroke(3);
                Rectangle2D rect = new Rectangle2D.Double(0, 0, 177, 144);

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
           }
            return new DefaultStreamedContent(imageInByteArray, "image/png");
        }
    }

    public int getAvaId() {
        return (int) new Date().getTime();
    }

    public void setAvaId(int avaId) {
        this.avaId = avaId;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }
}
