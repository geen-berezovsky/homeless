package ru.homeless.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Created by maxim on 01.11.14.
 */
@Controller
public class ServicesImplController implements ServletContextAware {

    @Autowired
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    public ServletContext getContext() {
        return context;
    }

}
