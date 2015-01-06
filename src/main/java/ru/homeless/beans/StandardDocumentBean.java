package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import ru.homeless.entities.Client;
import ru.homeless.entities.Document;
import ru.homeless.entities.Worker;
import ru.homeless.services.WorkerService;
import ru.homeless.util.Util;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;

@ManagedBean (name = "standardDocument")
@ViewScoped
public class StandardDocumentBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(StandardDocumentBean.class);

	private Worker worker;
    private Client client;

    private String type = "Заявление";
	private String number = "";
	private String forWhom = "";

    private String preamble = "";
    private String mainPart;
    private String finalPart;

    private String signature = "С уважением\nДиректор СПб БОО \'Ночлежка\"\nСвердлин Григорий";
    private String performer;


    @ManagedProperty(value = "#{WorkerService}")
	private WorkerService workerService;


    public void openDlg() {

        HttpSession session = Util.getSession();
        worker = (Worker) session.getAttribute("worker");
        String cids = session.getAttribute("cid").toString();

        if (cids != null && !cids.trim().equals("")) {
            this.client = getWorkerService().getInstanceById(Client.class, Integer.parseInt(cids));
        }

        this.mainPart = "К нам обратился за помощью "+client.getSurname()+" "+client.getFirstname()+" "+client.getMiddlename()+" "+client.getDate() +" г.р.";
        this.performer = "Исполнитель: "+worker.getSurname()+" "+worker.getFirstname().substring(0,1)+". "+worker.getMiddlename()+".";

        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("standardDocumentWv.show();");
    }

/*
    public void updateDocument() {
        //get active document data from database
        document = getWorkerService().getWorkerDocumentById(worker.getId());
        if (document == null) {
            document = new Document();
        }
    }

*/

	/*
	public void updateWorkerSettings() {
        if (document == null) {
            updateDocument();
        }
		FacesMessage msg = null;
        boolean foundErrors = false;
		//first, check the password
			//it is right password?
			if (oldPassword.trim().equals(worker.getPassword())) {
				//found old password, let's check if new password fields are filled
				if (! newPassword.trim().equals("")) {
					//ok, let's check newPassword2
					if (! newPassword2.trim().equals("") && newPassword2.trim().equals(newPassword.trim())) {
						//all passwords are correct
                        log.info("Updating the password...");
						worker.setPassword(newPassword.trim());
                        log.info("Password has been successfully updated");
						msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Изменение пароля", "Пароль успешно обновлен");
					} else {
                        log.info("Password has not been updated");
                        foundErrors = true;
						msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Изменение пароля", "Вы не указали или указали некорректное повторение нового пароля");
					}
				} else {
					//old password exists, but no new password provided
                    log.info("No new password has been provided");
					msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Изменение пароля", "Вы не указали новый пароль");
				}
			} else {
                log.info("Incorrect old password");
                foundErrors = true;
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Изменение пароля", "Вы указали некорректный старый пароль");
			}
            FacesContext.getCurrentInstance().addMessage(null, msg);
		//then, check passport data
		if (document.getDocPrefix().trim().equals("") || document.getDocNum().trim().equals("") || 
				document.getDate() == null || document.getWhereAndWhom().trim().equals("")) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Паспортные данные", "Пожалуйста, заполните все данные по паспорту!");
		} else {
			//Saving document data
			try {
                document.setRegistration(0); // THIS IS FAKE FOR COMPATIBILITY
                document.setWorker(worker.getId());
                if (document.getId() == null) {
                    getWorkerService().addInstance(document);
                } else {
                    getWorkerService().updateInstance(document);
                }

				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Паспортные данные обновлены", "");
                log.info("Passport data successfully updated");
			} catch (Exception e) {
                log.info("Cannot update password data");
                foundErrors = true;
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Паспортные данные не обновлены!", "Пожалуйста, посмотрите логи.");
				e.printStackTrace();
			}
		}
        FacesContext.getCurrentInstance().addMessage(null, msg);
		
		//then check all private fields
		if (worker.getSurname().trim().equals("") || worker.getFirstname().trim().equals("") || 
				worker.getRules() == null || worker.getWarrantNum().trim().equals("") ||
				worker.getWarrantDate() == null) {
			    msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Изменение данных работника", "Вы указали не все данные!");
                foundErrors = true;
                log.info("Cannot update worker personal data");
		} else {
			//Saving document data
			try {
				getWorkerService().updateInstance(worker);
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Данные работника обновлены", "");
                log.info("Worker personal data successfully updated");
			} catch (Exception e) {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Данны работника не обновлены!", "Пожалуйста, посмотрите логи.");
                foundErrors = true;
                log.info("Cannot update worker personal data, please see the logs");
			}
		}
        FacesContext.getCurrentInstance().addMessage(null, msg);

        if (!foundErrors) {
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("mySettingsWv.hide();");
        }
	}
	*/
	//Validators
    /*
	public void validateTextOnly(FacesContext ctx, UIComponent component, Object value) {
		Util.validateTextOnly(ctx, component, value);
	}

	public void validateWarrantDateFormat(FacesContext ctx, UIComponent component, Object value) {
		try {
			Date date = (Date) value;
			worker.setWarrantDate(date);
		} catch (Exception e) {
			log.info("The date format is abnormal. Trying to re-parse it. "+e.getMessage());
			Date result = Util.validateDateFormat(ctx, component, value);
			if (result != null) {
				worker.setWarrantDate(result);
			}
		}
	}

	public void validateDocumentDateFormat(FacesContext ctx, UIComponent component, Object value) {
		try {
			Date date = (Date) value;
			document.setDate(date);
		} catch (Exception e) {
			log.info("The date format is abnormal. Trying to re-parse it. "+e.getMessage());
			Date result = Util.validateDateFormat(ctx, component, value);
			if (result != null) {
				document.setDate(result);
			}
		}
	}
*/

    //Getters and setters

	public WorkerService getWorkerService() {
		return workerService;
	}

	public void setWorkerService(WorkerService workerService) {
		this.workerService = workerService;
	}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getForWhom() {
        return forWhom;
    }

    public void setForWhom(String forWhom) {
        this.forWhom = forWhom;
    }

    public String getPreamble() {
        return preamble;
    }

    public void setPreamble(String preamble) {
        this.preamble = preamble;
    }

    public String getMainPart() {
        return mainPart;
    }

    public void setMainPart(String mainPart) {
        this.mainPart = mainPart;
    }

    public String getFinalPart() {
        return finalPart;
    }

    public void setFinalPart(String finalPart) {
        this.finalPart = finalPart;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public Worker getWorker() {
        return worker;
    }
    public void setWorker(Worker worker) {
        this.worker = worker;
    }




}
