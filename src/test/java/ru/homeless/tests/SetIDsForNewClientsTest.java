package ru.homeless.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SetIDsForNewClientsTest {

    @Test
    public void show() {
        for (SimpleClient simpleClient : TestData.testClients) {
            System.out.println(simpleClient.getSurname());
        }

    }

/*
    @BeforeClass
    public static void init() {
        Util.driver = new FirefoxDriver();
        Util.driver.manage().window().maximize();

        new TestData(); //init test data
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
*/
}
