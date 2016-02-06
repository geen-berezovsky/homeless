package ru.homeless.tests;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxim on 06.02.2016.
 */
public class TestData {

    public static List<SimpleClient> testClients = new ArrayList<>();

    public TestData() {
        //Creating 10 clients
        testClients.add(new SimpleClient("Сорвирогов","Станиславович","Александр","04.09.1966","m",6,2000,"с. Жирково",true));
        testClients.add(new SimpleClient("Обезьянов","Никифорович","Горислав","01.06.1968","m",2,2001,"п. Новомушино",false));
        testClients.add(new SimpleClient("Дерматинов","Модестович","Любомир","15.07.1992","m",3,2003,"г. Ясная поляна",true));
        testClients.add(new SimpleClient("Тараканов","Азарьевич","Борис","14.11.1951","m",1,2006,"с. Поповка",false));
        testClients.add(new SimpleClient("Брюхов","Порфирьевич","Казимир","30.11.1960","m",8,2008,"г. Владивосток",true));
        testClients.add(new SimpleClient("Мочинская","Меркуриевна","Ирида","28.09.1993","f",10,2009,"г. Хабаровск",false));
        testClients.add(new SimpleClient("Сорвирогова","Сильвестровна","Клеопатра","12.03.1982","f",7,2001,"г. Харьков",true));
        testClients.add(new SimpleClient("Горбатая","Севериновна","Аврора","12.03.1964","f",1,2005,"г. Одесса",false));
        testClients.add(new SimpleClient("Беломорова","Пименовна","Ника","26.04.1956","f",9,1997,"с. Соколово",true));
        testClients.add(new SimpleClient("Тюфякина","Соломоновна","Мелисса","30.01.1976","f",6,1995,"п. Долгово",false));
    }

}
