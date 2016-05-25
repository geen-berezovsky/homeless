package ru.homeless.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.github.aleksandy.petrovich.Case;
import com.github.aleksandy.petrovich.Gender;
import com.github.aleksandy.petrovich.Petrovich;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import ru.homeless.entities.Client;
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
        //Next string is commented at 17.01.2016 according latest contract template from G.Sverdlin
    	//workerData = workerData+", действующий на основании доверенности "+worker.getWarrantNum()+" от "+Util.convertDate(worker.getWarrantDate());
    	return workerData;
    }

    public static byte[] attachPhoto(Client client, Logger log) {
        //INSERT PHOTO
        byte[] blobAsBytes = null;
        if (client.getAvatar() != null) {
            Blob blob = client.getAvatar();

            int blobLength;
            try {
                blobLength = (int) blob.length();
                blobAsBytes = blob.getBytes(1, blobLength);

                //release the blob and free up memory. (since JDBC 4.0)
                blob.free();
            } catch (SQLException e) {
                log.error(e.getMessage(),e);
            }
        }
        return blobAsBytes;
    }


    public static Date parseDate(HttpServletRequest request, String param, Logger log) {
        Date parsedDate = null;
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        String sDate = request.getParameter(param);
        if (sDate!=null && !sDate.trim().equals(""))
            try {
                parsedDate = df.parse(sDate);
            } catch (ParseException e) {
                log.warn("Date "+sDate+" does not match the template dd.MM.yyyy, using the current date", e);
                parsedDate = new Date();
            }
        return parsedDate;
    }

    public static String parseDateForMySql(Date sDate) {
    	if(sDate==null) {
    		sDate = new Date();
    	}
        SimpleDateFormat mySQLFormat = new SimpleDateFormat("yyyy-MM-dd");
        String reformattedStr = mySQLFormat.format(sDate);
        return "'" + reformattedStr + "'";
    }
    

    public static String parseDateForReport(Date sDate) {
    	if(sDate!=null) {
	    	SimpleDateFormat mySQLFormat = new SimpleDateFormat("dd.MM.yyyy");
	        String reformattedStr = mySQLFormat.format(sDate);
	        return reformattedStr;
    	} else {
    		return "";
    	}
    }

    public static String parseDateForReport(Object sDate)  {
        System.out.println("Parsing "+sDate);
        if(sDate!=null && !sDate.equals("")) {
            SimpleDateFormat defaultMySQLFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.ENGLISH);
            SimpleDateFormat longMySQLFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss,S", Locale.ENGLISH);
            Date date = null;
            try {
                //try to get date without parsing
                date = (Date) sDate;
            } catch (Exception e) {

            }
            if (date != null) {
                return parseDateForReport(date);
            }

            //try to parse
            try {
                date = defaultMySQLFormat.parse(sDate.toString());
            } catch (ParseException e) {
                //do nothing
            }
            if (date != null) {
                return parseDateForReport(date);
            }
            //if still date == null, use another format
            try {
                date = longMySQLFormat.parse(sDate.toString());
            } catch (ParseException e) {
                //do nothing
            }
            if (date != null) {
                return parseDateForReport(date);
            }
        }
        return "";
    }


    public static String html2text(String html) {
	    Document document = Jsoup.parse(html);
	    document.select("br").append("\\n");
	    document.select("p").prepend("\\n");
	    return document.text().replaceAll("\\\\n", "\n");
	}


    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    public static String getFIORodPadezh(String f, String i, String o, Gender gender) {
        Petrovich.Names names = new Petrovich.Names(f,i,o, gender);
        String result = "";
        Petrovich.Names resName = new Petrovich().inflectTo(names, Case.GENITIVE);
        result = resName.lastName + " " + resName.firstName + " " + resName.middleName;
        return result;
    }

    public static String monthToNative(int month) {
        switch (month) {
            case 0: return "января";
            case 1: return "февраля";
            case 2: return "марта";
            case 3: return "апреля";
            case 4: return "мая";
            case 5: return "июня";
            case 6: return "июля";
            case 7: return "августа";
            case 8: return "сентября";
            case 9: return "октября";
            case 10: return "ноября";
            case 11: return "декабря";
        }
        return null;
    }
}
