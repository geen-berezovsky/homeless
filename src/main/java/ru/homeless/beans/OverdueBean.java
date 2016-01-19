package ru.homeless.beans;

import org.primefaces.context.RequestContext;
import ru.homeless.entities.OverdueItem;
import ru.homeless.services.OverdueService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@ManagedBean(name = "overdue")
@SessionScoped
public class OverdueBean implements Serializable {
    @ManagedProperty(value = "#{OverdueService}")
    private OverdueService overdueService;
    private boolean rendered;
    List<OverdueItem> items = Collections.emptyList();

    public OverdueService getOverdueService() {
        return overdueService;
    }

    public void setOverdueService(OverdueService overdueService) {
        this.overdueService = overdueService;
    }

    public List<OverdueItem> getItems() {
        return items;
    }

    public void showDialog() {
        rendered = true;
        items = overdueService.getOverdueItems();
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("overdueDlgWv.show();");
    }

    public boolean isRendered(){
        return rendered;
    }
}
