package ru.homeless.util;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.spi.LoggingEvent;
import ru.homeless.entities.Worker;

import javax.servlet.http.HttpSession;

/**
 * Created by maxim on 14.02.2016.
 */
public class HomelessDailyRollingFileAppender extends DailyRollingFileAppender {

    @Override
    protected void subAppend(LoggingEvent event) {
        HttpSession session = Util.getSession();
        String workerName = "";
        if (session!= null && session.getAttribute("worker")!=null) {
            Worker worker = ((Worker) session.getAttribute("worker"));
            workerName = "["+worker.getFirstname()+" "+worker.getSurname()+"]: ";
        }


        String modifiedMessage = workerName + event.getMessage();
        LoggingEvent modifiedEvent = new LoggingEvent(event.getFQNOfLoggerClass(), event.getLogger(), event.getTimeStamp(), event.getLevel(), modifiedMessage,
                event.getThreadName(), event.getThrowableInformation(), event.getNDC(), event.getLocationInformation(),
                event.getProperties());

        super.subAppend(modifiedEvent);
    }

}
