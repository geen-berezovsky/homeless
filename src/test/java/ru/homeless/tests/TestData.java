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

        testClients.add(new SimpleClient("Быкова","Леонидовна","Динара","25.03.1980","f",6,2000,"г. Ирбейское",true));
        testClients.add(new SimpleClient("Голубев","Денисович","Святополк","01.04.1969","m",2,2001,"п. Навля",false));
        testClients.add(new SimpleClient("Фомова","Станиславовна","Нина","28.11.1993","f",3,2003,"г. Дмитров",true));
        testClients.add(new SimpleClient("Дидиченко","Анатольевич","Кирилл","08.02.1980","m",1,2006,"с. Сорск",false));
        testClients.add(new SimpleClient("Фомина","Евгеньевна","Оксана","25.12.1965","f",8,2008,"г. Москва",true));
        testClients.add(new SimpleClient("Гусева","Владимировна","Руфина","24.01.1978","f",10,2009,"г. Талица",false));
        testClients.add(new SimpleClient("Арсеньева","Кирилловна","Степанида","14.05.1980","f",7,2001,"г. Первомайское",true));
        testClients.add(new SimpleClient("Титов","Макарович","Борислав","08.09.1968","m",1,2005,"г. Шентала",false));
        testClients.add(new SimpleClient("Чаурина","Петровна","Инна","04.09.1992","f",9,1997,"с. Киржач",true));
        testClients.add(new SimpleClient("Баженов","Вячеславович","Артем","22.01.1989","m",6,1995,"п. Сыктывкар",false));


    }

}
