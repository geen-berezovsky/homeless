package ru.homeless.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.List;

/**
 * Created by maxim on 11.07.2015.
 */
@ManagedBean
public class ThemeSwitcherView {

    private List<Theme> themes;

    @ManagedProperty("#{themeService}")
    private ThemeService service;

    @PostConstruct
    public void init() {
        themes = service.getThemes();
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public void setService(ThemeService service) {
        this.service = service;
    }
}