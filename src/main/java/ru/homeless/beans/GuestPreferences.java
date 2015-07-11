package ru.homeless.beans;

import ru.homeless.entities.Worker;
import ru.homeless.services.GenericService;
import ru.homeless.services.WorkerService;
import ru.homeless.util.Util;

import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

/**
 * Created by maxim on 11.07.2015.
 */
@SessionScoped
public class GuestPreferences {
    private String theme;


    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

}