package ru.homeless.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.homeless.entities.Worker;

/**
 * Created by maxim on 30.11.14.
 */
public class Util {

    public static String convertDate(Date date) {
        if (date == null) {
            return null;
        } else {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            return (df.format(date));
        }
    }
    
    public static String getWorkersBio(int worker) {
    	Worker w = new Worker();
    	return getWorkersBio(w);
    }
    
    public static String getWorkersBio(Worker worker) {
    	//Worker worker = contractService.getInstanceById(Worker.class, workerId);
    	String workerData = worker.getRules().getCaption()+" "+worker.getSurname()+" "+worker.getFirstname()+" "+worker.getMiddlename();
    	workerData = workerData+", действующий на основании доверенности "+worker.getWarrantNum()+" от "+Util.convertDate(worker.getWarrantDate());
    	return workerData;
    }



}
