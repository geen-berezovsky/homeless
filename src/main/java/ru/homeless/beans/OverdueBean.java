package ru.homeless.beans;

import ru.homeless.entities.OverdueItem;
import ru.homeless.services.OverdueService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "overdue")
@ViewScoped
public class OverdueBean implements Serializable {
    @ManagedProperty(value = "#{OverdueService}")
    private OverdueService overdueService;

    public OverdueService getOverdueService() {
        return overdueService;
    }

    public void setOverdueService(OverdueService overdueService) {
        this.overdueService = overdueService;
    }

    public List<OverdueItem> getItems() {
        List<OverdueItem> items = overdueService.getOverdueItems();
        return items;
    }
}
