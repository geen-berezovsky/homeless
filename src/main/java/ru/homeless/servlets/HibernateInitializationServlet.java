package ru.homeless.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.homeless.util.HibernateUtil;

public class HibernateInitializationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void init(ServletConfig config) throws ServletException {
		System.out.println("\n**** Initializing Log4J Hibernate Init Servlet ********** \n");
		super.init(config);
		System.out.println("Session started: "+HibernateUtil.getSession().toString());
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
