package ru.homeless.tests;

import com.thoughtworks.selenium.SeleniumException;
import org.apache.log4j.Logger;
import org.junit.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class BaseFormModificationsTest {

    public static Logger log = Logger.getLogger(BaseFormModificationsTest.class);

    /*
    This test
    1. Add TestData (10 clients (5 man, 5 woman))
    2. Search using name searching and set new IDs
    3. Randomly find clients by ID and update automatically generated values to perfonal info (3 * 10 times)
    4. Randomly find clients by ID and update automatically generated values to Contacts and Memos (3 * 10 times)
    5. Check that generated values are as expected and nothing generated or replaced wrong
     */

    @BeforeClass
    public static void init() throws IOException {
        Util.performInit();
    }

    @Test
	public void modification() throws InterruptedException, IOException {
        //***********
        //*** [1] ***
        //***********
        for (SimpleClient simpleClient : TestData.testClients) {
            Util.addNewClient(simpleClient.getSurname(), simpleClient.getFirstname(), simpleClient.getMiddlename(), simpleClient.getBirthDate(), simpleClient.getGender(), simpleClient.getHomelessFromM(), simpleClient.getHomelessFromY(), simpleClient.getBirthPlace(), simpleClient.isImportant());
        }

        //***********
        //*** [2] ***
        //***********
        for (SimpleClient simpleClient : TestData.testClients) {
            simpleClient.setId(Util.findClientIdBySimpleData(simpleClient));
            log.info("Client "+simpleClient.toString()+" is matched with ID = "+simpleClient.getId());
        }
        //***********
        //*** [3] ***
        //***********

        Random rand = null;
        int minRand = 0;
        int maxRand = TestData.testClients.size()-1;
        log.info("Generating 30 random values from "+minRand+" to "+maxRand);

        List<Integer> listNumbers = new ArrayList<>();
        for (int i=0; i<10; i++) {
            listNumbers.add(Util.randInt(minRand, maxRand));
        }

        String numbers = "";
        for (int i : listNumbers) {
            numbers += i+" ";
        }

        log.info("Generated numbers: "+numbers);

        for (int i : listNumbers) {
            SimpleClient simpleClient = TestData.testClients.get(i);
            Util.setActiveClient(simpleClient.getId());
            Util.addMemo(String.valueOf(simpleClient.getId()));
            SimpleClient scMDF = Util.updateSFM(simpleClient);
            TestData.testClients.get(i).setSurname(scMDF.getSurname());
            TestData.testClients.get(i).setFirstname(scMDF.getFirstname());
            TestData.testClients.get(i).setMiddlename(scMDF.getMiddlename());
        }

        //***********
        //*** [5] ***
        //***********
        FileWriter fw = new FileWriter("logresult.txt");
        for (SimpleClient simpleClient : TestData.testClients) {
            try {
                Thread.sleep(Util.defaultPageTimeout*1000);
                log.info("Opening client with ID = " + simpleClient.getId());
                Util.setActiveClient(simpleClient.getId());
                String str1 = simpleClient.toString();
                String str2 = Util.getSFM(simpleClient).toString();
                String str3 = Util.getMemo(simpleClient);

                fw.write("SimpleClient\t[Collection]:\t" + str1 + "\n");
                log.info("SimpleClient\t[Collection]:\t" + str1);
                fw.write("SimpleClient\t[Database]:\t" + str2 + "\n");
                log.info("SimpleClient\t[Database]:\t" + str2);
                fw.write("Memo\t\t[Database]:\t" + str3+"\n");
                log.info("Memo\t\t[Database]:\t" + str3);
                log.info("");
                fw.write(""+"\n");

                //CHECK THAT NO CLIENTS WERE OVERWRITTEN
                Assert.assertTrue(str1.toLowerCase().equals(str2.toLowerCase()));

                //CHECK THAT NO MEMO(COMMENTS) WERE OVERWRITTEN
                Assert.assertTrue(str3.trim().equals("") || str3.contains(String.valueOf(simpleClient.getId())));

            } catch (Exception e) {
                fw.close();
                throw new SeleniumException("Test unexpectedly finished with error. See the logresult.txt file");
            }
        }
        fw.close();

    }

    @AfterClass
	public static void finishTesting() {
        //Util.logout();
		//Util.driver.quit();
	}


}
