package ru.homeless.beans;

import org.primefaces.context.RequestContext;
import ru.homeless.entities.OverdueItem;
import ru.homeless.services.OverdueService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;
import ru.homeless.entities.Client;
import ru.homeless.services.ClientService;

@ManagedBean(name = "overdue")
@SessionScoped
public class OverdueBean implements Serializable {
    @ManagedProperty(value = "#{OverdueService}")
    private OverdueService overdueService;
    private boolean rendered;
    List<OverdueItem> items;
    private static final Logger LOG = Logger.getLogger(OverdueBean.class);
    
    @ManagedProperty(value = "#{ClientService}")
    private ClientService clientService;
    
    public OverdueService getOverdueService() {
        return overdueService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public void setOverdueService(OverdueService overdueService) {
        this.overdueService = overdueService;
    }

    public void updateItems() {
        items = null;
    }
    
    public List<OverdueItem> getItems() {
        return items != null? items : (items = overdueService.getOverdueItems());
    }

    public void showDialog() {
        rendered = true;
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.update("overdueTableId");
        rc.execute("overdueDlgWv.show();");
    }

    public boolean isRendered(){
        return rendered;
    }
    
    public void onRowClick(final SelectEvent event) {
        OverdueItem overdueItem = (OverdueItem) event.getObject();
        try {
            Client client = clientService.getClientsByCriteria(overdueItem.getClientId(), null, null, null, null).get(0);
            LOG.info("Opening client from overdueTable tab ID = "+overdueItem.getClientId());
            FacesContext context = FacesContext.getCurrentInstance();
            ClientFormBean clientFormBean = context.getApplication().evaluateExpressionGet(context, "#{clientform}", ClientFormBean.class);
            clientFormBean.setClient(client);
            clientFormBean.afterSearch();
        } catch (SQLException e) {
            LOG.error("Error during ClienFormBean reloading for =" + overdueItem.getClientId(), e);
        }
    }
}
