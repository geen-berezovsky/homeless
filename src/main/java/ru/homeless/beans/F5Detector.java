package ru.homeless.beans;

import org.apache.log4j.Logger;
import ru.homeless.entities.Client;
import ru.homeless.util.Util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * Created by maxim on 02.06.2016.
 */
@SessionScoped
@ManagedBean
public class F5Detector {
    private String previousPage = null;
    public static Logger log = Logger.getLogger(F5Detector.class);


    public void checkF5() throws SQLException {
        String msg = "";
        UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        String id = viewRoot.getViewId();
        if (previousPage != null && (previousPage.equals(id))) {
            HttpSession session = Util.getSession();
            Client client = (Client) session.getAttribute("client");
            if (client != null) {
                FacesContext context = FacesContext.getCurrentInstance();
                ClientFormBean cfb = context.getApplication().evaluateExpressionGet(context, "#{clientform}", ClientFormBean.class);
                cfb.setClient(client);
                cfb.afterSearch();
                log.warn("Detected unexpected page refresh. Resetting all client's data at runtime.");
            }
        }
        previousPage = id;

    }
}