package ru.homeless.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ru.homeless.util.HibernateUtil;
import ru.homeless.util.Util;

public class HibernateInitializationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(HibernateInitializationServlet.class);
	
	public void init(ServletConfig config) throws ServletException {
		log.info("\n**** Initializing Log4J Hibernate Init Servlet ********** \n");
		super.init(config);
		log.info("Session started: "+HibernateUtil.getSession().toString());
	}

	/**
	 * Handles the requests from http client.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
	}


}
