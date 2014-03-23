package ru.homeless.tests;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddBasicUserTest {
	@Test
	/*
	 * This test will add new user using the simple form.
	 * Actually, it will show how to call a custom menu and fill all forms items correctly
	 */
	public void addUserBasicForm() {
		//TODO: Continue this test implementation when client adding mechanism will be created completely
		
		Util.driver = new FirefoxDriver();
		Util.basicSelectLogin();
		
		//Set submenu visible by clicking to the top item
		Util.driver.findElement(By.xpath("//div[@id='megaMenu:j_idt5']/ul/li/a/span[text()='Клиенты']")).click();
		//Click to "Добавить клиента"
		Util.driver.findElement(By.xpath("//div[@id='megaMenu:j_idt5']/ul/li/ul/table/tbody/tr/td/ul/li/a/span[text()='Добавить клиента']")).click();
		
		//wait while "add client" form will be shown
		WebDriverWait wait = new WebDriverWait(Util.driver, Util.defaultPageTimeout);
		wait.until(ExpectedConditions.visibilityOf(Util.driver.findElement(By.id("add_client:addClient"))));

		
		Util.driver.findElement(By.id("add_client:new_cedit_surname")).sendKeys("Иванов");
		Util.driver.findElement(By.id("add_client:new_cedit_firstname")).sendKeys("Иван");
		Util.driver.findElement(By.id("add_client:new_cedit_middlename")).sendKeys("Иванович");
		Util.driver.findElement(By.id("add_client:new_cedit_bdate")).sendKeys("01.01.1950");
		Util.driver.findElement(By.id("add_client:new_cedit_firstname")).sendKeys("Иван");
		
		//click to the selector for combobox
		Util.driver.findElement(By.xpath("//div[@id='add_client:newSelectGender']/div/span")).click();
		//and then select gender
		
		Util.driver.findElement(By.xpath("//div[@id='add_client:newSelectGender_panel']/div/ul/li[text()='Мужской']")).click();

		//click to the selector for combobox
		Util.driver.findElement(By.xpath("//div[@id='add_client:newSelectMonth']/div/span")).click();
		//and then select month of homeless state
		Util.driver.findElement(By.xpath("//div[@id='add_client:newSelectMonth_panel']/div/ul/li[text()='Июль']")).click();
		
		//Integer form items could contain a zero at form load. We need to reset it before
		Util.driver.findElement(By.id("add_client:newSelectedYear")).clear();
		Util.driver.findElement(By.id("add_client:newSelectedYear")).sendKeys("2001");
		
		Util.driver.findElement(By.id("add_client:cedit_born_place")).sendKeys("с. Кукуево, Новгородской обл.");
		
		//That's all. Currently, adding of new client is not implemented, but it will be done in nearset future
		
		Util.logout();
	}
	
	@AfterClass
	public static void finishTesting() {
		Util.driver.quit();
	}

}
