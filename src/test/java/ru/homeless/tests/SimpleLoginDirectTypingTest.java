package ru.homeless.tests;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SimpleLoginDirectTypingTest {
	public SimpleLoginDirectTypingTest() {
	}
	
	@Test
	/*
	 * This test will put direct login name and passowrd to the proper form items and perform form submit.
	 * After that it will wait a little and try to perform logout by clicking the logout button element.
	 */
	public void directTypingLogin() {
		Util.driver = new FirefoxDriver();
		Util.directTypingLogin();
		Util.logout();
	}
	
	@AfterClass
	public static void finishTesting() {
		Util.driver.quit();
	}

}
