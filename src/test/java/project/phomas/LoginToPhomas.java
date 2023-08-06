package project.phomas;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import phamas.commonmethods.CommonMethods;

public class LoginToPhomas extends CommonMethods {

	// @SuppressWarnings("deprecation")
	// public static void main(String[] args) throws IOException,
	// InterruptedException {

	@SuppressWarnings("deprecation")
	@Test
	public void phomasAutomation() throws Exception {

		System.setProperty("webdriver.chrome.driver", readData("chromePath"));

		WebDriver driver = new ChromeDriver();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Actions act = new Actions(driver);

		driver.get(readData("url"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		usernameField(driver).sendKeys(readData("username"));
		passwordField(driver).sendKeys(readData("password"));
		signInButton(driver).click();

		driver.findElement(By.xpath("//a[@href='/order/status-overview']")).click();
		driver.findElement(By.xpath("//span[contains(text(),'Toggle secondary filters')]")).click();
		Thread.sleep(2000);

		// Reading Column names
		// readExcel(readData("filePath"), readData("sheetName"), 0, 9);
		int rowCount = rowCount(readData("filePath"), readData("sheetName"));
		int columnCount = columnCount(readData("filePath"), readData("sheetName"));

		System.out.println("Row Count is -> " + rowCount);
		System.out.println("Column count is -> " + columnCount);

		try {
		for (int i = 1; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
					if (readExcel(readData("filePath"), readData("sheetName"), i, j) != null) {

						String data = readExcel(readData("filePath"), readData("sheetName"), i, j);
						// System.out.println(readExcel(readData("filePath"), readData("sheetName"), 0,
						// j));
						dropDownElement(driver, readExcel(readData("filePath"), readData("sheetName"), 0, j)).click();
						if (data.contains(";")) {
							for (String value : data.split(";")) {
								dropdownTextBox(driver).sendKeys(value);
								driver.findElement(By
										.xpath("//span[@class='mat-option-text' and contains(text(),'" + value + "')]"))
										.click();
								dropdownTextBox(driver).clear();
							}
							Thread.sleep(1000);
							act.moveToElement(orderStatusOverviewTitle(driver)).click().build().perform();
						} else {
							if (data.contains("Active")) {
								// dropdownTextBox(driver).sendKeys(data);
								driver.findElement(By.xpath(
										"//span[@class='mat-checkbox-label' and contains(text(),'" + data + "')]"))
										.click();
								Thread.sleep(1000);
								act.moveToElement(orderStatusOverviewTitle(driver)).click().build().perform();
								try {
									if (driver.findElement(By.xpath(
											"//span[@class='mat-checkbox-label' and contains(text(),'" + data + "')]"))
											.isDisplayed()) {
										act.moveToElement(orderStatusOverviewTitle(driver)).click().build().perform();
									}
								} catch (Exception e) {
								}
							} else {
								dropdownTextBox(driver).sendKeys(data);
								driver.findElement(By
										.xpath("//span[@class='mat-option-text' and contains(text(),'" + data + "')]"))
										.click();
								Thread.sleep(1000);
								act.moveToElement(orderStatusOverviewTitle(driver)).click().build().perform();
								try {
									if (driver.findElement(By.xpath(
											"//span[@class='mat-option-text' and contains(text(),'" + data + "')]"))
											.isDisplayed()) {
										act.moveToElement(orderStatusOverviewTitle(driver)).click().build().perform();
									}
								} catch (Exception e) {
								}
							}
						}
					}
//				else {
//					break;
//				}
			}
			wait(2000);
			driver.findElement(By.xpath("//mat-icon[@data-mat-icon-name='excel']")).click();
			wait(8000);
			driver.navigate().refresh();
			wait(2500);
			driver.findElement(By.xpath("//span[contains(text(),'Toggle secondary filters')]")).click();
		}} catch (Exception e) {
			System.out.println("Null data");
			
		}

		// String data2 = readExcel(readData("filePath"), readData("sheetName"), 6, 2);
//	        if(data.contains(";")) {
//	        	for (String value: data.split(";")) {
//	                System.out.println(value);
//	             }
//	        } else {
//	        	System.out.println(data);
//	        }

		// dropDownElement(driver, "Season").click();
		// driver.findElement(By.xpath("//span[contains(text(),'OFW23')]")).click();
		// wait(2500);

		// Remove the focus from the Season dropdown
		// js.executeScript("arguments[0].blur();", seasonDropdown);
		//
		// moveToElementAndClick(orderStatusOverviewTitle(driver));
		// act.moveToElement(orderStatusOverviewTitle(driver)).click().build().perform();
		//
		// //Click on the excel button
		// Thread.sleep(2500);
		// driver.findElement(By.xpath("//mat-icon[@data-mat-icon-name='excel']")).click();

	}

}
