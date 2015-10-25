package ru.homeless.beans;

import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Map;

@ManagedBean(name = "browser")
@SessionScoped
/*
 * Currently used to getting information about brower window size
 */
public class BrowserWindowBean implements Serializable {
    public BrowserWindowBean() {
    }

    private static final long serialVersionUID = 1L;
    public static Logger log = Logger.getLogger(BrowserWindowBean.class);

    private int height;
    private int width;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void updateSize() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        int width = Integer.parseInt(map.get("width"));
        int height = Integer.parseInt(map.get("height"));
        this.width = width;
        this.height = height;
    }
}
