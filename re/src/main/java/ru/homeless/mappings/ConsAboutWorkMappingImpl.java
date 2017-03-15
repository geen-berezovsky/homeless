package ru.homeless.mappings;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.homeless.entities.BasicDocumentRegistry;
import ru.homeless.entities.Client;
import ru.homeless.entities.Document;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.services.GenericService;
import ru.homeless.services.IGenericService;
import ru.homeless.shared.IDocumentMapping;
import ru.homeless.util.Util;

import javax.servlet.ServletContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by maxim on 30.11.14.
 */
@Component
public class ConsAboutWorkMappingImpl implements ICustomMappingWordDocument {

    public static final Logger log = Logger.getLogger(ConsAboutWorkMappingImpl.class);

    @Qualifier("GenericService")
    @Autowired
    private IGenericService genericService;

    //http://localhost:9090/homeless-report-engine/getGeneratedWordDocument?requestType=18&clientId=13000&docId=15660&issueDate=25.05.2016&workerId=2&docNum=446/1276&travelCity=

    @Override
    public WordprocessingMLPackage getDocument(Map map, Client client) {

        //We don't sent dateFrom and dateTill with URL request, so we need to get the necessary value from database

        Integer docId = Integer.parseInt(map.get("[input:docId]").toString());
        if (docId == null || docId == 0) {
            return null;
        }
        BasicDocumentRegistry documentRegistry = null;
        try {
            documentRegistry  = genericService.getInstanceById(BasicDocumentRegistry.class, docId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        Date dateFrom = documentRegistry .getDateFrom();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFrom);
        map.put("[t:dateFromDay]", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        map.put("[t:dateFromMonthWrite]",String.valueOf(Util.monthToNative(calendar.get(Calendar.MONTH))));
        map.put("[t:dateFromYear]", String.valueOf(calendar.get(Calendar.YEAR)));

        //"Current" date is known (we don't use new Date() for a some reasons)
        Date currDate = null;
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        try {
            currDate = df.parse(map.get("[t:today]").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String d = "";
        String m = "";
        String y = "";
        if (currDate != null) {
            calendar.setTime(currDate);
            d = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            m = Util.monthToNative(calendar.get(Calendar.MONTH));
            y = String.valueOf(calendar.get(Calendar.YEAR));

        } else {
            d = "";
            m = "";
            y = "";
        }
        map.put("[t:today:day]", d);
        map.put("[t:today:month:write]", m);
        map.put("[t:today:year]", y);

        return new DocTypeProcessor(IDocumentMapping.DOCUMENT_CONS_ABOUT_WORK_TEMPLATE_PATH).replaceParametersInDocument(map, Util.attachPhoto(client, log), ICustomMappingWordDocument.AVATAR_LOCATION_BOTTOM_CENTER);
    }

    @Override
    public WordprocessingMLPackage getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {
        return null;
    }
}
