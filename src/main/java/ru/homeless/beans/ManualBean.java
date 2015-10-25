package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import ru.homeless.converters.ContractPointsTypeConverter;
import ru.homeless.converters.ContractResultTypeConverter;
import ru.homeless.converters.ShelterResultConverter;
import ru.homeless.entities.*;
import ru.homeless.primefaces.model.ContractPointsDataModel;
import ru.homeless.services.GenericService;
import ru.homeless.services.WorkerService;
import ru.homeless.util.Util;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@ManagedBean(name = "user_manual")
@ViewScoped
public class ManualBean implements Serializable {
    private boolean rendered = false;

    public void setBrowserBean(BrowserWindowBean browserBean) {
        this.browserBean = browserBean;
    }

    @ManagedProperty(value="#{browser}")
    private BrowserWindowBean browserBean;
    
    public int getHeight(){
        return (int) (browserBean.getHeight()*0.8);
    }

    public int getWidth(){
        return (int) (browserBean.getWidth()*0.8);
    }
}

