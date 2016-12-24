package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import ru.homeless.entities.Worker;
import ru.homeless.services.WorkerService;
import ru.homeless.util.Util;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxim on 11.07.2015.
 */
@ManagedBean(name="themeService", eager = true)
@SessionScoped
public class ThemeService {

    private List<Theme> themes;
    private String theme;
    private GuestPreferences gp;
    public static Logger log = Logger.getLogger(ThemeService.class);

    @ManagedProperty(value = "#{WorkerService}")
    private WorkerService workerService;

    @PostConstruct
    public void init() {
        setGp(new GuestPreferences()); //persistent class
        setTheme(gp.getTheme()); //theme from the database

        themes = new ArrayList<Theme>();
        themes.add(new Theme(0, "Afterdark", "afterdark"));
        themes.add(new Theme(1, "Afternoon", "afternoon"));
        themes.add(new Theme(2, "Afterwork", "afterwork"));
        themes.add(new Theme(3, "Aristo", "aristo"));
        themes.add(new Theme(4, "Black-Tie", "black-tie"));
        themes.add(new Theme(5, "Blitzer", "blitzer"));
        themes.add(new Theme(6, "Bluesky", "bluesky"));
        themes.add(new Theme(7, "Bootstrap", "bootstrap"));
        themes.add(new Theme(8, "Casablanca", "casablanca"));
        themes.add(new Theme(9, "Cupertino", "cupertino"));
        themes.add(new Theme(10, "Cruze", "cruze"));
        themes.add(new Theme(11, "Dark-Hive", "dark-hive"));
        themes.add(new Theme(12, "Delta", "delta"));
        themes.add(new Theme(13, "Dot-Luv", "dot-luv"));
        themes.add(new Theme(14, "Eggplant", "eggplant"));
        themes.add(new Theme(15, "Excite-Bike", "excite-bike"));
        themes.add(new Theme(16, "Flick", "flick"));
        themes.add(new Theme(17, "Glass-X", "glass-x"));
        themes.add(new Theme(18, "Home", "home"));
        themes.add(new Theme(19, "Hot-Sneaks", "hot-sneaks"));
        themes.add(new Theme(20, "Humanity", "humanity"));
        themes.add(new Theme(21, "Le-Frog", "le-frog"));
        themes.add(new Theme(22, "Midnight", "midnight"));
        themes.add(new Theme(23, "Mint-Choc", "mint-choc"));
        themes.add(new Theme(24, "Overcast", "overcast"));
        themes.add(new Theme(25, "Pepper-Grinder", "pepper-grinder"));
        themes.add(new Theme(26, "Redmond", "redmond"));
        themes.add(new Theme(27, "Rocket", "rocket"));
        themes.add(new Theme(28, "Sam", "sam"));
        themes.add(new Theme(29, "Smoothness", "smoothness"));
        themes.add(new Theme(30, "South-Street", "south-street"));
        themes.add(new Theme(31, "Start", "start"));
        themes.add(new Theme(32, "Sunny", "sunny"));
        themes.add(new Theme(33, "Swanky-Purse", "swanky-purse"));
        themes.add(new Theme(34, "Trontastic", "trontastic"));
        themes.add(new Theme(35, "UI-Darkness", "ui-darkness"));
        themes.add(new Theme(36, "UI-Lightness", "ui-lightness"));
        themes.add(new Theme(37, "Vader", "vader"));
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public void openDlg() {
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('changeThemeWv').show();");
    }

    public void saveTheme() {
        gp.setTheme(theme); //persist theme value to the database and set it to the runtime
        HttpSession session = Util.getSession();
        Worker worker = (Worker) session.getAttribute("worker");
        worker.setPrimefacesskin(theme);
        getWorkerService().updateInstance(worker);
        session.setAttribute("worker", worker);
        log.info("User "+worker.getFirstname() + " " +worker.getSurname() + " has set new personal theme \""+ theme + "\"");
    }


    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public GuestPreferences getGp() {
        return gp;
    }

    public void setGp(GuestPreferences gp) {
        this.gp = gp;
    }

    public WorkerService getWorkerService() {
        return workerService;
    }

    public void setWorkerService(WorkerService workerService) {
        this.workerService = workerService;
    }
}
