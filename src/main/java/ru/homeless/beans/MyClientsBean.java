package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import ru.homeless.entities.Client;
import ru.homeless.entities.MyClientsEntity;
import ru.homeless.entities.ServContract;
import ru.homeless.entities.Worker;
import ru.homeless.services.ClientService;
import ru.homeless.util.Util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;

@ManagedBean(name = "myclients")
@ViewScoped
public class MyClientsBean implements Serializable {
	public static Logger log = Logger.getLogger(MyClientsBean.class);
	private static final long serialVersionUID = 1L;

	private List<MyClientsEntity> foundList;
    private List<MyClientsEntity> foundPrevList;
    private Date startDate;
    private Date endDate;

    @ManagedProperty(value = "#{ClientService}")
	private ClientService clientService;

	public MyClientsBean() {
		foundList = new ArrayList<MyClientsEntity>();
        foundPrevList = new ArrayList<MyClientsEntity>();
        Calendar c1 = GregorianCalendar.getInstance();
        endDate = c1.getTime();
        c1.add(Calendar.MONTH, -6);
        startDate = c1.getTime();
    }
	

	public void findClients() {
        HttpSession session = Util.getSession();
        Worker w = (Worker) session.getAttribute("worker");
        foundList.clear();
		foundList = getClientService().getMyContracts(w.getId());
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.update("myClientsForm");
        rc.execute("myClientsWv.show();");
    }

    public void requestNotActualClients() {
        HttpSession session = Util.getSession();
        Worker w = (Worker) session.getAttribute("worker");
        foundPrevList.clear();
        foundPrevList = getClientService().getMyClosedContracts(w.getId(), startDate, endDate);
    }

    public void findNotActualClients() {
        RequestContext rc = RequestContext.getCurrentInstance();
        foundPrevList.clear();
        rc.update("myPrevClientsForm");
        rc.execute("myPrevClientsWv.show();");
    }

	public ClientService getClientService() {
		return clientService;
	}

	public void setClientService(ClientService clientService) {
		this.clientService = clientService;
	}


    public List<MyClientsEntity> getFoundList() {
        return foundList;
    }

    public void setFoundList(List<MyClientsEntity> foundList) {
        this.foundList = foundList;
    }

    public void onRowDblClckSelect(final SelectEvent event) {
        MyClientsEntity myClientsEntity = (MyClientsEntity) event.getObject();
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("myClientsWv.hide();");
        rc.execute("myPrevClientsWv.hide();");

        FacesContext context = FacesContext.getCurrentInstance();
        ClientFormBean clientFormBean = context.getApplication().evaluateExpressionGet(context, "#{clientform}", ClientFormBean.class);
        try {
            clientFormBean.reloadAll(myClientsEntity.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<MyClientsEntity> getFoundPrevList() {
        return foundPrevList;
    }

    public void setFoundPrevList(List<MyClientsEntity> foundPrevList) {
        this.foundPrevList = foundPrevList;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
