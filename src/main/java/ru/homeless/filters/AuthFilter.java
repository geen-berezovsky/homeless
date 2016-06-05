package ru.homeless.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class AuthFilter implements Filter {

	public AuthFilter() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	//Данный фильтр в рантайме необходим, чтобы отрезать незалогированных пользователей и перенаправить их на страницу логина
	//Для этого проверяем, что в текущей сессии установлена переменная username и она непуста
	//При этом, разрешаем ходить в поддиректорию public, через которую можем шарить контент (например, иконки для страницы логина)
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {

			//Получаем сессию и проверяем, что переменная username установлена или мы идем в public
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			HttpSession ses = req.getSession(false);
			String reqURI = req.getRequestURI();
            if (reqURI.equalsIgnoreCase("/homeless/") || reqURI.equalsIgnoreCase("/homeless/secure/")) {
                reqURI = "/homeless/secure/main.xhtml";
                res.sendRedirect(reqURI);
            }

			//Само собой, разрешаем идти на index.xhtml для перелогинивания 
			if (reqURI.indexOf("/index.xhtml") >= 0 || (ses != null && ses.getAttribute("username") != null) || reqURI.indexOf("/public/") >= 0
					|| reqURI.contains("javax.faces.resource"))
				chain.doFilter(request, response);
			else
				//Юзер полез туда, куда не нужно. Пошлем его на index.xhtml для логина
				res.sendRedirect(req.getContextPath() + "/index.xhtml");
		} catch (Throwable t) {
			System.out.println(t.getMessage());
		}
	}

	public void destroy() {
		
	}

}