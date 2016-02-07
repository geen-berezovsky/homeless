package ru.homeless.tests;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class BaseFormModificationsTest {

    public static Logger log = Logger.getLogger(BaseFormModificationsTest.class);

    /*
    This test
    1. Add TestData (10 clients (5 man, 5 woman))
    2. Search using name searching and set new IDs
    3. Randomly find clients by ID and update automatically generated values to perfonal info (3 * 10 times)
    4. Randomly find clients by ID and update automatically generated values to Contacts and Memos (3 * 10 times)
    5. Check that generated values are as expexted and nothing generated or replaced wrong
     */

    @BeforeClass
    public static void init() throws IOException {
        Util.performInit();
    }

	@Test
    /*
    This is the [1]
     */
	public void addClientsToTheDatabase() throws InterruptedException {
        for (SimpleClient simpleClient : TestData.testClients) {
            Util.addNewClient(simpleClient.getSurname(), simpleClient.getFirstname(), simpleClient.getMiddlename(), simpleClient.getBirthDate(), simpleClient.getGender(), simpleClient.getHomelessFromM(), simpleClient.getHomelessFromY(), simpleClient.getBirthPlace(), simpleClient.isImportant());
        }
	}

    @Test
    /*
    This is the [2]
     */
    public void searchByNameAndSetIDs() throws IOException {
        for (SimpleClient simpleClient : TestData.testClients) {
            simpleClient.setId(Util.findClientIdBySimpleData(simpleClient));
            log.info("Client "+simpleClient.toString()+" is matched with ID = "+simpleClient.getId());
        }
    }



    @AfterClass
	public static void finishTesting() {
        Util.logout();
		Util.driver.quit();
	}


}
