package ru.homeless.tests;

import com.thoughtworks.selenium.SeleniumException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static Logger log = Logger.getLogger(Util.class);
    private static String OS = System.getProperty("os.name").toLowerCase();

	public static WebDriver driver;
	public static final long defaultPageTimeout = 10;
	public static final String defaultWorkerUsername = "Валентина Борейко";
	public static final String defaultWorkerPassword = "111";
	public static String defaultURL = "http://localhost:8080/homeless/";

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

	public static void logout () {
        try {
            log.info("Performing logout");
            //Pressing "Выход" link
            driver.findElement(By.linkText("Выход")).click();
            //... and check that exist has been performed correctly
            WebDriverWait wait = new WebDriverWait(driver, Util.defaultPageTimeout);
            wait.until(ExpectedConditions.titleContains("Добро пожаловать!"));
            log.info("Logout done");
        } catch (Exception e) {
            log.error(e);
            takePicture(Thread.currentThread().getStackTrace()[1].getMethodName());
        }
	}


	public static void directTypingLogin() throws IOException {
        try {
            log.info("Performing login");
            //open URL
            driver.get(Util.defaultURL);
            //find inputText element with username and put value there
            driver.findElement(By.id("form:username_input")).sendKeys(Util.defaultWorkerUsername);
            //find inputText element with password and put value there
            driver.findElement(By.id("form:password")).sendKeys(Util.defaultWorkerPassword);
            //Submit form
            driver.findElement(By.id("form:loginButton")).click();
            //Waiting NOT MORE THAN 5 seconds for page loading and TEST if the title conains "Добро пожаловать, username!"
            WebDriverWait wait = new WebDriverWait(driver, Util.defaultPageTimeout);
            wait.until(ExpectedConditions.titleContains("Добро пожаловать, " + Util.defaultWorkerUsername + "!"));
            //If there found actual title, driver will go on. Otherwise it will fail with detailed message.
            log.info("Login done");
        } catch (Exception e) {
            log.error(e);
            takePicture(Thread.currentThread().getStackTrace()[1].getMethodName());
        }
	}

    public static void setActiveClient(int id) {
        try {
            log.info("Finding client id = " + id + " with standard search by ID");
            driver.findElement(By.linkText("Клиенты")).click();
            driver.findElement(By.xpath("//div[@id='mainMenu:j_idt7']/ul/li/ul/li[2]/a/span")).click();
            driver.findElement(By.id("searchForm:_id")).clear();
            driver.findElement(By.id("searchForm:_id")).sendKeys(String.valueOf(id));
            driver.findElement(By.id("searchForm:submitSearchById")).click();
            log.info("Waiting " + Util.defaultPageTimeout + " seconds while a client is opening");
            Thread.sleep(Util.defaultPageTimeout * 1000);
            driver.findElement(By.id("searchForm:foundClientsList:0:selectButton")).click();
        } catch (Exception e) {
            log.error(e);
            takePicture(Thread.currentThread().getStackTrace()[1].getMethodName());
        }
    }

    public static void addNewClient(
            String surname,
            String firstname,
            String middlename,
            String date,
            String gender,
            int fromM,
            int fromY,
            String brthPlace,
            boolean important
    ) {
        try {
            log.info("Adding client " + surname + " " + firstname + " " + middlename + " (" + date + ")");
            driver.findElement(By.linkText("Клиенты")).click();
            driver.findElement(By.xpath("//div[@id='mainMenu:j_idt7']/ul/li/ul/li[6]/a/span")).click(); // Добавить клиента

            log.info("Waiting default timeout "+Util.defaultPageTimeout+" seconds while page is not completely loaded");
            //hack for waiting while new client is created
            Thread.sleep(Util.defaultPageTimeout*1000);

            log.info("Asserting that it is new client");
            //assert that this is the new client
            String s = driver.findElement(By.id("m_tabview:base_form:cedit_surname")).getAttribute("value");
            String f = driver.findElement(By.id("m_tabview:base_form:cedit_firstname")).getAttribute("value");
            String m = driver.findElement(By.id("m_tabview:base_form:cedit_middlename")).getAttribute("value");
            String d = driver.findElement(By.id("m_tabview:base_form:cedit_bdate")).getAttribute("value");

            Assert.assertTrue(s.equals(""));
            Assert.assertTrue(f.equals(""));
            Assert.assertTrue(m.equals(""));
            Assert.assertTrue(d.equals(""));

            log.info("Sending new data to form");
            driver.findElement(By.id("m_tabview:base_form:cedit_surname")).sendKeys(surname);
            driver.findElement(By.id("m_tabview:base_form:cedit_firstname")).sendKeys(firstname);
            driver.findElement(By.id("m_tabview:base_form:cedit_middlename")).sendKeys(middlename);
            driver.findElement(By.id("m_tabview:base_form:cedit_bdate")).sendKeys(date);

            driver.findElement(By.xpath("//*[@id='m_tabview:base_form:selectGender']/div[3]/span")).click();
            if (gender.equalsIgnoreCase("m")) {
                driver.findElement(By.xpath("//*[@id='m_tabview:base_form:selectGender_panel']/div/ul/li[1]")).click();
            } else {
                driver.findElement(By.xpath("//*[@id='m_tabview:base_form:selectGender_panel']/div/ul/li[2]")).click();
            }

            driver.findElement(By.xpath("//*[@id='m_tabview:base_form:selectMonth']/div[3]/span")).click();
            driver.findElement(By.xpath("//*[@id=\"m_tabview:base_form:selectMonth_panel\"]/div/ul/li[" + fromM + "]")).click();
            driver.findElement(By.id("m_tabview:base_form:selectedYear")).clear();
            driver.findElement(By.id("m_tabview:base_form:selectedYear")).sendKeys(String.valueOf(fromY));
            driver.findElement(By.id("m_tabview:base_form:cedit_born_place")).sendKeys(String.valueOf(brthPlace));
            if (important) {
                driver.findElement(By.xpath("//*[@id=\"m_tabview:base_form:j_idt84\"]/div[2]")).click();
            }

            log.info("Pressing Save button");
            driver.findElement(By.id("m_tabview:base_form:saveClientForm")).click();
            log.info("Done. New Client is added.");
        } catch (Exception e) {
            log.error(e);
            takePicture(Thread.currentThread().getStackTrace()[1].getMethodName());
        }
    }

    public static SimpleClient updateSFM(SimpleClient simpleClient) {
        try {
            log.info("Waiting " + Util.defaultPageTimeout/2 + " seconds for opening Memo");
            Thread.sleep(Util.defaultPageTimeout * 500);

            String s = driver.findElement(By.id("m_tabview:base_form:cedit_surname")).getAttribute("value");
            String f = driver.findElement(By.id("m_tabview:base_form:cedit_firstname")).getAttribute("value");
            String m = driver.findElement(By.id("m_tabview:base_form:cedit_middlename")).getAttribute("value");
            String d = driver.findElement(By.id("m_tabview:base_form:cedit_bdate")).getAttribute("value");

            log.info("Sending new data to form");
            String newSurname = "-"+s.substring(0,1)+f.substring(0,1)+m.substring(0,1);
            String newFirstname = "-"+s.substring(0,1)+f.substring(0,1)+m.substring(0,1);
            String newMiddlename = "-"+s.substring(0,1)+f.substring(0,1)+m.substring(0,1);
            driver.findElement(By.id("m_tabview:base_form:cedit_surname")).sendKeys(newSurname);
            driver.findElement(By.id("m_tabview:base_form:cedit_firstname")).sendKeys(newFirstname);
            driver.findElement(By.id("m_tabview:base_form:cedit_middlename")).sendKeys(newMiddlename);

            log.info("Pressing Save button");
            driver.findElement(By.id("m_tabview:base_form:saveClientForm")).click();
            simpleClient.setSurname(s + newSurname);
            simpleClient.setFirstname(f + newFirstname);
            simpleClient.setMiddlename(m+newMiddlename);
            log.info("Done. New Client is added.");
        } catch (Exception e) {
            log.error(e);
            takePicture(Thread.currentThread().getStackTrace()[1].getMethodName());
        }
        return simpleClient;
    }

    public static SimpleClient getSFM(SimpleClient simpleClient) {
        try {
            log.info("Waiting " + Util.defaultPageTimeout/2 + " seconds for opening Memo");
            Thread.sleep(Util.defaultPageTimeout * 500);

            String s = driver.findElement(By.id("m_tabview:base_form:cedit_surname")).getAttribute("value");
            String f = driver.findElement(By.id("m_tabview:base_form:cedit_firstname")).getAttribute("value");
            String m = driver.findElement(By.id("m_tabview:base_form:cedit_middlename")).getAttribute("value");
            String d = driver.findElement(By.id("m_tabview:base_form:cedit_bdate")).getAttribute("value");
            simpleClient.setSurname(s);
            simpleClient.setFirstname(f);
            simpleClient.setMiddlename(m);
            simpleClient.setBirthDate(d);
        } catch (Exception e) {
            log.error(e);
            takePicture(Thread.currentThread().getStackTrace()[1].getMethodName());
        }
        return simpleClient;
    }

    public static String getMemo(SimpleClient simpleClient) {
        String text = "";
        try {
            log.info("Opening comments");
            log.info("Waiting " + Util.defaultPageTimeout/2 + " seconds for opening Memo");
            Thread.sleep(Util.defaultPageTimeout * 500);
            driver.findElement(By.partialLinkText("Примечания")).click();
            WebElement editorFrame = driver.findElement(By.xpath("//*[@id='m_tabview:client_memo:memo_editor']/div/iframe"));
            driver.switchTo().frame(editorFrame);
            WebElement editorBody = driver.findElement(By.cssSelector("body"));
            text = editorBody.getText();
            driver.switchTo().defaultContent();
            Thread.sleep(1000);
            driver.findElement(By.id("m_tabview:client_memo:saveClientFormMemoButton")).click();
            driver.findElement(By.partialLinkText("Базовая информация")).click();
        } catch (Exception e) {
            log.error(e);
            takePicture(Thread.currentThread().getStackTrace()[1].getMethodName());
        }
        return text;
    }


    public static void hideRightPanel() {
        try {
            log.info("Hiding right panel");
            driver.findElement(By.xpath("//*[@id='reminders']/div[1]/a[2]/span")).click();
        } catch (Exception e) {
            log.error(e);
            takePicture(Thread.currentThread().getStackTrace()[1].getMethodName());
        }
    }

    public static int findClientIdBySimpleData(SimpleClient simpleClient) {
        try {
            log.info("Waiting "+Util.defaultPageTimeout/2+" seconds while all page is loaded");
            Thread.sleep(Util.defaultPageTimeout * 500);
            log.info("Getting client's ID by surname, firstname, middlename, dateOfBirth");
            driver.findElement(By.linkText("Клиенты")).click();
            driver.findElement(By.xpath("//div[@id='mainMenu:j_idt7']/ul/li[1]/ul/li[1]/a/span")).click(); // Добавить клиента

            driver.findElement(By.id("searchForm:surname")).clear();
            driver.findElement(By.id("searchForm:surname")).sendKeys(simpleClient.getSurname());
            Thread.sleep(100);
            driver.findElement(By.id("searchForm:firstname")).clear();
            driver.findElement(By.id("searchForm:firstname")).sendKeys(simpleClient.getFirstname());
            Thread.sleep(100);
            driver.findElement(By.id("searchForm:middlename")).clear();
            driver.findElement(By.id("searchForm:middlename")).sendKeys(simpleClient.getMiddlename());
            Thread.sleep(100);
            driver.findElement(By.id("searchForm:date")).clear();
            driver.findElement(By.id("searchForm:date")).sendKeys(simpleClient.getBirthDate());
            Thread.sleep(100);
            driver.findElement(By.id("searchForm:submitSearchByName")).click();
            log.info("Waiting "+Util.defaultPageTimeout/2+" seconds while a search is running");
            Thread.sleep(Util.defaultPageTimeout * 500);
            driver.findElement(By.id("searchForm:foundClientsList:0:selectButton")).click();
            log.info("Waiting " + Util.defaultPageTimeout/2 + " seconds while a client is opening");
            Thread.sleep(Util.defaultPageTimeout * 500);
            String idString = driver.findElement(By.xpath("//div[@id='m_tabview:base_form:header']/div[1]/span[@class='ui-layout-unit-header-title']")).getText();
            log.info("Found client "+idString);
            String retValueStr = idString.substring(idString.indexOf("(ID = ") + 6, idString.indexOf(","));
            Integer retValue = Integer.parseInt(retValueStr);
            if (retValue != null && retValue!=0) {
                return retValue;
            } else {
                throw new SeleniumException ("Cannot parse value \""+retValueStr+"\" as Integer!");
            }
        } catch (Exception e) {
            log.error(e);
            takePicture(Thread.currentThread().getStackTrace()[1].getMethodName());
        }
        return 0;
    }

    public static void addMemo(String text) {
        try {
            log.info("Opening comments");
            log.info("Waiting " + Util.defaultPageTimeout/2 + " seconds for opening Memo");
            Thread.sleep(Util.defaultPageTimeout * 500);
            driver.findElement(By.partialLinkText("Примечания")).click();

            WebElement editorFrame = driver.findElement(By.xpath("//*[@id='m_tabview:client_memo:memo_editor']/div/iframe"));
            driver.switchTo().frame(editorFrame);
            WebElement editorBody = driver.findElement(By.cssSelector("body"));
            String newText = " "+text;
            editorBody.click();
            editorBody.sendKeys(newText);
            driver.switchTo().defaultContent();
            Thread.sleep(1000);
            driver.findElement(By.id("m_tabview:client_memo:saveClientFormMemoButton")).click();
            driver.findElement(By.partialLinkText("Базовая информация")).click();

        } catch (Exception e) {
            log.error(e);
            takePicture(Thread.currentThread().getStackTrace()[1].getMethodName());
        }

    }

    public static void takePicture(String prefix) {
        File screenshotsDir = new File("screenshots");
        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdirs();
        }
        //Something goes wrong, taking snapshot
        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String screenshotFileName = prefix + "-" + new Date().getTime() + ".png";
        log.info("Taking screenshot. Filename is " + screenshotFileName);
        try {
            FileUtils.copyFile(srcFile, new File(screenshotsDir+"/"+screenshotFileName));
        } catch (IOException e) {
            log.error(e);
        }
        throw new SeleniumException ("Some shit happens while running tests. Please see logs and screenshots.");
    }

    public static void performInit() throws IOException {
        if (!isWindows()) {
            String Xport = System.getProperty("lmportal.xvfb.id",":10");

            if (System.getenv("webdriver") == null || System.getenv("webdriver").trim().equals("")) {
                final File firefoxPath = new File(System.getProperty("lmportal.deploy.firefox.path", "/usr/bin/firefox"));
                FirefoxBinary firefoxBinary = new FirefoxBinary(firefoxPath);
                firefoxBinary.setEnvironmentProperty("DISPLAY", Xport);
                driver = new FirefoxDriver(firefoxBinary, null);
            } else {
                System.setProperty("webdriver.chrome.driver","/opt/chromedriver/chromedriver");
                driver = new ChromeDriver();
            }

        } else {
            if (System.getenv("webdriver") == null || System.getenv("webdriver").trim().equals("")) {
                driver = new FirefoxDriver();
            } else {
                System.setProperty("webdriver.chrome.driver","C:/tools/chromedriver.exe");
                driver = new ChromeDriver();
            }
        }

        new TestData();

        driver.manage().window().maximize();

        directTypingLogin();
        hideRightPanel();


    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}
