package ru.homeless.tests;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.IOException;

public class AddNewClientsToDBTest {

    @BeforeClass
    public static void init() {

        if (!Util.isWindows()) {
            String Xport = System.getProperty("lmportal.xvfb.id",":10");
            final File firefoxPath = new File(System.getProperty("lmportal.deploy.firefox.path","/usr/bin/firefox"));
            FirefoxBinary firefoxBinary = new FirefoxBinary(firefoxPath);
            firefoxBinary.setEnvironmentProperty("DISPLAY", Xport);
            Util.driver = new FirefoxDriver(firefoxBinary, null);
        } else {
            Util.driver = new FirefoxDriver();
        }

        new TestData();

        Util.driver.manage().window().maximize();
    }




    @Test
    public void login() throws IOException {
        Util.directTypingLogin(); // LOGIN
    }

	@Test
	public void addClientsToTheDatabase() throws InterruptedException {
        //Util.setActiveClient(13000);
        Util.hideRightPanel();

        for (SimpleClient simpleClient : TestData.testClients) {
            Util.addNewClient(simpleClient.getSurname(),
                    simpleClient.getFirstname(),
                    simpleClient.getMiddlename(),
                    simpleClient.getBirthDate(),
                    simpleClient.getGender(),
                    simpleClient.getHomelessFromM(),
                    simpleClient.getHomelessFromY(),
                    simpleClient.getBirthPlace(),
                    simpleClient.isImportant());
        }


	}



	
	@AfterClass
	public static void finishTesting() {
        Util.logout();
		Util.driver.quit();
	}

}
