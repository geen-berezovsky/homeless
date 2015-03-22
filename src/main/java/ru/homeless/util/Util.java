package ru.homeless.util;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ru.homeless.beans.ClientFormBean;
import ru.homeless.configuration.Configuration;
import ru.homeless.entities.Client;
import ru.homeless.services.ClientService;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		return formatter.format(query);
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
		Pattern pattern = Pattern.compile("[a-zA-Z-]+|[а-яА-Я-]+");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static Date validateDateFormat(FacesContext ctx, UIComponent component, Object value) {
		String str = value.toString();
		if (!isDateValid(str)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Некорректный формат даты!", "Используйте дд.мм.гггг");
			throw new ValidatorException(msg);
		} else {
			try {
				Date d = new SimpleDateFormat("dd.MM.yyyy").parse(str);
				return d;
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
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

    public static void applyNewPhoto(ClientService clientService, File sourceFileOnDisk, String finalFileName) throws IOException, SQLException {
        //1. check that user don't have actual photo, or delete it from the target directory
        //2. copy source file to the target directory with the new name
        //3. make an avatar and save it to the database
        HttpSession session = Util.getSession();
        String cid = session.getAttribute("cid").toString();
        Client client = clientService.getInstanceById(Client.class, Integer.parseInt(cid));
        //PREPARE BUFFERED IMAGE AND SET ITS SIZE
        BufferedImage bi = new BufferedImage(177, 144, BufferedImage.TYPE_INT_ARGB);

        //READING IMAGE FROM DISK TO BYTE ARRAY
        byte[] bytes = new byte[(int) sourceFileOnDisk.length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(sourceFileOnDisk);
            fis.read(bytes);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //CONVERT BYTE ARRAY TO THE BUFFERED IMAGE
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            bi = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage resizedImage = new BufferedImage(177, 144, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bi, 0, 0, 177, 144, null);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] resizedBytes;
        try {
            ImageIO.write(resizedImage, "png", baos);
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resizedBytes = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Set photo name in database and photo checksum
        String checksum = "";
        FileInputStream fisc = new FileInputStream(sourceFileOnDisk);
        checksum = org.apache.commons.codec.digest.DigestUtils.md5Hex(fisc);
        fisc.close();
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

        InputStream imageInByteArray = new ByteArrayInputStream(baos.toByteArray());
        imageInByteArray = new ByteArrayInputStream(resizedBytes);

        //Evaluating ClientForm Bean
        FacesContext context = FacesContext.getCurrentInstance();
        ClientFormBean cf = context.getApplication().evaluateExpressionGet(context, "#{clientform}", ClientFormBean.class);
        cf.reloadAll(client.getId());

        //NOW MOVE THE ORIGINAL FILE FROM CACHE TO THE STORAGE
        Path src_file = Paths.get(sourceFileOnDisk.getAbsolutePath());
        File pf = new File(Configuration.photos);
        if (! pf.exists()) {
            pf.mkdirs();
        }

        Path dst_file = Paths.get(Configuration.photos+"/" + finalFileName + ".png");
        try {
            Files.move(src_file, dst_file, REPLACE_EXISTING);
            log.info("Destination file after moving: "+Configuration.photos+"/"+ finalFileName + ".png");
        } catch (IOException e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Оригинал фото не сохранен!", "Подробности в логе.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }

    }

    public static StreamedContent downloadDocument(String requestSuffix, String saveFilePath, String docType,
                                                   String docName) throws IOException {

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
    }



}
