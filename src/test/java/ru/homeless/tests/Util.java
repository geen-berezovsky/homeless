package ru.homeless.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Util {

	public static WebDriver driver;
	public static final long defaultPageTimeout = 5;
	public static final String defaultWorkerUsername = "Елена Кондрахина";
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


	public static void basicSelectLogin() {
		//open URL
		driver.get(Util.defaultURL);
		//try to select the username using combo in form

		//1. First, click on combobox - otherwise we can't access data there
		driver.findElement(By.xpath("//span[@id='form:username']/button")).click();
		//2. Second, click to the item, which conatin the necessary text
		driver.findElement(By.xpath("//div[@id='form:username_panel']/ul/li[text()='"+Util.defaultWorkerUsername+"']")).click();
		
		//find inputText element with password and put value there
		driver.findElement(By.id("form:password")).sendKeys(Util.defaultWorkerPassword);
		//Submit form
		driver.findElement(By.id("form:loginButton")).click();
		//Waiting NOT MORE THAN 5 seconds for page loading and TEST if the title conains "Добро пожаловать, username!"
		WebDriverWait wait = new WebDriverWait(driver, Util.defaultPageTimeout);
		wait.until(ExpectedConditions.titleContains("Добро пожаловать, "+Util.defaultWorkerUsername+"!"));
		//If there found actual title, driver will go on. Otherwise it will fail with detailed message.
	}

	
}
