package ru.homeless.tests;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Util {

	public static WebDriver driver;
	public static final long defaultPageTimeout = 10;
	public static final String defaultWorkerUsername = "Валентина Борейко";
	public static final String defaultWorkerPassword = "111";
	public static String defaultURL = "http://localhost:8080/homeless/";
	
	
	public static void logout () {
		//Pressing "Выход" link
		driver.findElement(By.linkText("Выход")).click();
		//... and check that exist has been performed correctly
		WebDriverWait wait = new WebDriverWait(driver, Util.defaultPageTimeout);
		wait.until(ExpectedConditions.titleContains("Добро пожаловать!"));
	}


	public static void directTypingLogin() {
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
		wait.until(ExpectedConditions.titleContains("Добро пожаловать, "+Util.defaultWorkerUsername+"!"));
		//If there found actual title, driver will go on. Otherwise it will fail with detailed message.
	}

    public static void setActiveClient(int id) {
        driver.findElement(By.linkText("Клиенты")).click();
        driver.findElement(By.xpath("//div[@id='mainMenu:j_idt7']/ul/li/ul/li[2]/a/span")).click();
        driver.findElement(By.id("searchForm:_id")).clear();
        driver.findElement(By.id("searchForm:_id")).sendKeys(String.valueOf(id));
        driver.findElement(By.id("searchForm:submitSearchById")).click();
        driver.manage().timeouts().pageLoadTimeout(Util.defaultPageTimeout, TimeUnit.SECONDS);
        driver.findElement(By.id("searchForm:foundClientsList:0:selectButton")).click();
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

    ) throws InterruptedException {
        driver.findElement(By.linkText("Клиенты")).click();
        driver.findElement(By.xpath("//div[@id='mainMenu:j_idt7']/ul/li/ul/li[6]/a/span")).click(); // Добавить клиента

        //hack for waiting while new client is created
        Thread.sleep(3000);

        //assert that this is the new client
        String s = driver.findElement(By.id("m_tabview:base_form:cedit_surname")).getAttribute("value");
        String f = driver.findElement(By.id("m_tabview:base_form:cedit_firstname")).getAttribute("value");
        String m = driver.findElement(By.id("m_tabview:base_form:cedit_middlename")).getAttribute("value");
        String d = driver.findElement(By.id("m_tabview:base_form:cedit_bdate")).getAttribute("value");

        Assert.assertTrue(s.equals(""));
        Assert.assertTrue(f.equals(""));
        Assert.assertTrue(m.equals(""));
        Assert.assertTrue(d.equals(""));

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
        driver.findElement(By.xpath("//*[@id=\"m_tabview:base_form:selectMonth_panel\"]/div/ul/li["+fromM+"]")).click();
        driver.findElement(By.id("m_tabview:base_form:selectedYear")).clear();
        driver.findElement(By.id("m_tabview:base_form:selectedYear")).sendKeys(String.valueOf(fromY));
        driver.findElement(By.id("m_tabview:base_form:cedit_born_place")).sendKeys(String.valueOf(brthPlace));
        if (important) {
            driver.findElement(By.xpath("//*[@id=\"m_tabview:base_form:j_idt84\"]/div[2]")).click();
        }

        driver.findElement(By.id("m_tabview:base_form:saveClientForm")).click();

    }

    public static void hideRightPanel() {
        driver.findElement(By.xpath("//*[@id='reminders']/div[1]/a[2]/span")).click();
    }
}
