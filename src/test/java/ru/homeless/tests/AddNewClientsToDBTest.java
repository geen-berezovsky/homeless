package ru.homeless.tests;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

public class AddNewClientsToDBTest {

    @BeforeClass
    public static void init() {
        new TestData();
        Util.driver = new FirefoxDriver();
        Util.driver.manage().window().maximize();
    }

    @Test
    public void login() {
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
