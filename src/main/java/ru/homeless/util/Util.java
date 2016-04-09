package ru.homeless.util;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ru.homeless.beans.ClientFormBean;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.Client;
import ru.homeless.services.ClientService;
import ru.homeless.services.WorkerService;

public class Util {

    public static Logger log = Logger.getLogger(Util.class);

	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	}

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	public static String getContextRealPath() {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		return servletContext.getRealPath("");
	}

	public static String getUserName() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return session.getAttribute("username").toString();
	}

	public static String getUserId() {
		HttpSession session = getSession();
		if (session != null)
			return (String) session.getAttribute("userid");
		else
			return null;
	}

    public static String getRandomImageName() {
        return String.valueOf(new Date().getTime());
    }


    public static String formatDate(Date query) {
        if (query == null) {
            return "??.??.????";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            return formatter.format(query);
        }
	}

	public static String getImagesStorageLocation() {
		return "/tmp";
	}

	public static String getTempImagesStorageLocation() {
		return "/tmp";
	}

	public static void validateTextOnly(FacesContext ctx, UIComponent component, Object value) {
		if (!isTextOnlyValid(value.toString())) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"ФИО не может содержать цифры, спецсимволы и пробелы и быть пустым!\nТолько русский или латинский текст, а также тире.", "Пожалуйста, проверьте форму!");
			throw new ValidatorException(msg);
		}
	}

	public static boolean isTextOnlyValid(String str) {
		Pattern pattern = Pattern.compile("[a-zA-Z-]+|[а-яА-Я-ёЁ]+");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static Date validateDateFormat(FacesContext ctx, UIComponent component, Object value) {
        if (isDateValid(value.toString())) {
            Date sd = null;
            if (value instanceof Date) {
                sd = (Date) value;
            } else {
                try {
                    sd = new SimpleDateFormat("dd.MM.yyyy").parse(value.toString());
                } catch (ParseException e) {
                    log.error(e.getMessage(),e);
                }
            }
            String str = value.toString();
            if (!isDateValid(str)) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Некорректный формат даты!", "Используйте дд.мм.гггг");
                throw new ValidatorException(msg);
            } else {
                return sd;
            }
        } else {
            return null;
        }
	}

	public static boolean isDateValid(String str) {
		Pattern pattern = Pattern.compile("\\d{2}.\\d{2}.\\d{4}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			try {
				new SimpleDateFormat("dd.MM.yyyy").parse(str);
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	public static void validateNumFormat(FacesContext ctx, UIComponent component, Object value) {
		String str = value.toString();
		if (!isNumValid(str)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Некорректный формат!", "Допустимы только цифры.");
			throw new ValidatorException(msg);
		}
	}
	
	public static boolean isNumValid(String str) {
		Pattern pattern = Pattern.compile("^[0-9]+$");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

    public static StreamedContent loadResizedPhotoFromDisk(File resultFile) throws IOException {
        BufferedImage bi = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
        byte[] bytes = new byte[(int) resultFile.length()];
        FileInputStream fis = null;
        fis = new FileInputStream(resultFile);
        fis.read(bytes);
        fis.close();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        bi = ImageIO.read(in);
        BufferedImage resizedImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bi, 0, 0, 640, 480, null);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] resizedBytes;
        ImageIO.write(resizedImage, "png", baos);
        baos.flush();
        resizedBytes = baos.toByteArray();
        baos.close();
        InputStream imageInByteArray = new ByteArrayInputStream(baos.toByteArray());
        return new DefaultStreamedContent(imageInByteArray, "image/png");
    }

    private static final int AVATAR_WIDTH = 177;
    private static final int AVATAR_HEIGHT = 144;

    private static BufferedImage resizeImage(BufferedImage image, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return resizedImage;
    }
    
    private static byte[] getImageBytes(BufferedImage image) {
        byte[] bytes = new byte[0];
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", byteStream);
            byteStream.flush();
            bytes = byteStream.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }
    
    public static void applyNewPhoto(ClientService clientService, File sourceFileOnDisk, String finalFileName) throws IOException, SQLException {
        //1. check that user don't have actual photo, or delete it from the target directory
        //2. copy source file to the target directory with the new name
        //3. make an avatar and save it to the database
        Client client = Util.getCurrentClient();
        //PREPARE BUFFERED IMAGE AND SET ITS SIZE
        BufferedImage bi = ImageIO.read(sourceFileOnDisk);
        BufferedImage resizedImage = resizeImage(bi, AVATAR_WIDTH, AVATAR_HEIGHT);
        byte[] resizedBytes = getImageBytes(resizedImage);
        //Set photo name in database and photo checksum
        String checksum = DigestUtils.md5Hex(resizedBytes);
        log.info("New photo checksum: "+checksum);
        client.setPhotoCheckSum(checksum);
        client.setPhotoName(finalFileName+".png");
        log.info("New photo name: "+finalFileName+".png");
        //SAVING DATA
        if (!clientService.setClientAvatar(client, resizedBytes)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Аватар не сохранен!", "Подробности в логе.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        //Update the client's data on page

        //InputStream imageInByteArray = new ByteArrayInputStream(baos.toByteArray());
        //imageInByteArray = new ByteArrayInputStream(resizedBytes);

        //Evaluating ClientForm Bean
        FacesContext context = FacesContext.getCurrentInstance();
        ClientFormBean cf = context.getApplication().evaluateExpressionGet(context, "#{clientform}", ClientFormBean.class);
        cf.getClient().setAvatar(new javax.sql.rowset.serial.SerialBlob(resizedBytes));
        cf.getClient().setPhotoCheckSum(client.getPhotoCheckSum());
        cf.getClient().setPhotoName(client.getPhotoName());
        //cf.reloadAll(client.getId());

        //NOW MOVE THE ORIGINAL FILE FROM CACHE TO THE STORAGE
        Path src_file = Paths.get(sourceFileOnDisk.getAbsolutePath());
        File pf = new File(Configuration.photos);
        if (! pf.exists()) {
            pf.mkdirs();
        }
        
        Path dst_file = Paths.get(Configuration.profilesDir, String.valueOf(client.getId()), 
                finalFileName + ".png");
        try {
            Files.move(src_file, dst_file, REPLACE_EXISTING);
            log.info("Destination file after moving: "+Configuration.photos+"/"+ finalFileName + ".png");
        } catch (IOException e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Оригинал фото не сохранен!", "Подробности в логе.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }
        System.out.println("FINAL "+client.getPhotoCheckSum());
        RequestContext rc = RequestContext.getCurrentInstance();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rc.update(":m_tabview:base_form:photo_main_avatar");

    }

    public static StreamedContent downloadDocument(String requestSuffix, String saveFilePath, String docType,
                                                   String docName) throws IOException {
    try {
        URL url = new URL(Configuration.reportEngineUrl + requestSuffix);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestProperty("Accept-Charset", "UTF-8");
        InputStream inputStream = httpConn.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(saveFilePath);
        int bytesRead = -1;
        byte[] buffer = new byte[4096];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        inputStream.close();
        InputStream stream = new FileInputStream(new File(saveFilePath));
        return new DefaultStreamedContent(stream, docType, docName);
    } catch (ConnectException e) {
        log.error("Cannot access Report Engine!!!");
        //REDIRECT TO THE MAIN PAGE
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("main.xhtml");
        return null;
    }
    }

    /**
     * Return +dayCount from current date
     * @param dayCount
     * @return +day count and time 23:59:59 
     */
    public static Date getNDayFromCurrent(int dayCount){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return getMaximalTimeInDay(calendar.getTime());
    }
    
    /**
     * Returns 0 hour 0 minutes date of current date
     * @return
     */
    public static Date getCurDateDaysOnly(){
    	return getMinimalTimeInDay(new Date());
    }
    
    public static Date getMaximalTimeInDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 99); 
        return cal.getTime();
    }
    
    public static Date getMinimalTimeInDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Client getCurrentClient() {
        FacesContext context = FacesContext.getCurrentInstance();
        ClientFormBean cdb = context.getApplication().evaluateExpressionGet(context, "#{clientform}", ClientFormBean.class);
        if (cdb!=null && cdb.getClient()!=null) {
            return cdb.getClient();
        } else {
            return null;
        }
    }


    public static String getDocNum(WorkerService ws, int requestId) {
        String docNum = "";
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        Date from = new GregorianCalendar(currYear,0,1).getTime();
        Date till = new Date();
        log.info("Requested period for counting document number is from "+from+" till "+till);
        docNum = ws.getDocNum(from, till, requestId);
        log.info("Document number is "+docNum);
        return docNum;
    }
}
