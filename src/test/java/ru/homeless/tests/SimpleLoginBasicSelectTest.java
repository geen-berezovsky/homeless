package ru.homeless.tests;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SimpleLoginBasicSelectTest {
	
	@Test
	/*
	 * This test will select 1st avaiable element with default username and try to login with default password
	 * This test is applicable for all workers and exact method should be applied for all tests
	 */
	public void basicSelectLogin() {
		Util.driver = new FirefoxDriver();
		Util.basicSelectLogin();
		Util.logout();
	}

	@AfterClass
	public static void finishTesting() {
		Util.driver.quit();
	}

}
