package ru.homeless.beans;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import com.sun.jna.platform.win32.Netapi32Util;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.Worker;
import ru.homeless.services.WorkerService;
import ru.homeless.util.Util;

@ManagedBean(name = "user")
@SessionScoped
/*
 * This bean is only for providing more accuratelly log in for users using non
 * standard login name (concatenation of surname and name) with drop down list
 * box
 */
public class UserBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(UserBean.class);

    public String getDeploymentTimeStamp() {
        return deploymentTimeStamp;
    }

    public void setDeploymentTimeStamp(String deploymentTimeStamp) {
        this.deploymentTimeStamp = deploymentTimeStamp;
    }

    private String deploymentTimeStamp;

    @ManagedProperty(value = "#{WorkerService}")
	private WorkerService workerService;

    public UserBean() throws IOException {
        //update deployment timestamp
        FileInputStream fstream = new FileInputStream(Configuration.timestampFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine = "";
        while ((strLine = br.readLine()) != null) {
            deploymentTimeStamp = strLine;
        }
        // Close the input stream
        fstream.close();
    }
	
	/*
	 * username = Worker.getFirstname + Worker.getSurname
	 */
	private String username;
	private String password;

	public String getName() {
		return username;
	}

	public void setName(String name) {
		this.username = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getAllUserNames(String query) {
		List<String> names = new ArrayList<String>();
		
		for (Worker w : getWorkerService().getInstances(Worker.class)) {
			if (query.trim().equals("")) {
				names.add(w.getFirstname() + " " + w.getSurname());
			} else {
				if ((w.getFirstname() + " " + w.getSurname()).toLowerCase().contains(query.toLowerCase())) {
					names.add(w.getFirstname() + " " + w.getSurname());
				}
			}
		}
		return names;
	}

	public void login(ActionEvent actionEvent) {
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage msg = null;
		
		boolean loggedIn = false;
		
		Worker w = getWorkerService().login(username, password);
		if (w != null) {
			log.info(username + " has successfully logged in at " + new Date().toString());
            FacesContext fc = FacesContext.getCurrentInstance();
            ThemeService tsb = fc.getApplication().evaluateExpressionGet(fc, "#{themeService}", ThemeService.class);
            tsb.setTheme(w.getPrimefacesskin());

            HttpSession session = Util.getSession();
			session.setAttribute("username", username);
			session.setAttribute("worker", w);
			loggedIn = true;
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Добро пожаловать", username);
		} else {
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Ошибка входа", "Неправильное имя или пароль");
		}


		FacesContext.getCurrentInstance().addMessage(null, msg);
		context.addCallbackParam("loggedIn", loggedIn);
	}

	public void logout() throws IOException {
		HttpSession session = Util.getSession();
		session.invalidate();

		// Перенаправим на страницу логина после выхода
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect("../login.xhtml");
	}

	public WorkerService getWorkerService() {
		return workerService;
	}

	public void setWorkerService(WorkerService workerService) {
		this.workerService = workerService;
	}

}
