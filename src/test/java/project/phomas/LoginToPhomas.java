package project.phomas;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import phamas.commonmethods.AzureBlobStorage;
import phamas.commonmethods.CommonMethods;
import phamas.commonmethods.PowerhsellExecutor;

public class LoginToPhomas extends CommonMethods {

	// @SuppressWarnings("deprecation")
	// public static void main(String[] args) throws IOException,
	// InterruptedException {

	@SuppressWarnings("deprecation")
	@Test
	public void phomasAutomation() throws Exception {

		System.setProperty("webdriver.chrome.driver", readData("chromePath"));

		String path = createFolder("Phomas");

		// Launching browser with desired capabilities
		WebDriver driver = new ChromeDriver(chromeOptions(path, readData("headless")));

		// WebDriver driver = new ChromeDriver();

		Actions act = new Actions(driver);

		driver.get(readData("url"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		usernameField(driver).sendKeys(readData("username"));
		passwordField(driver).sendKeys(readData("password"));
		signInButton(driver).click();

		orderStatusOverviewLink(driver).click();
		toggleFilter(driver).click();
		Thread.sleep(2000);

		// Reading Column names
		int rowCount = rowCount(readData("filePath"), readData("sheetName"));
		int columnCount = columnCount(readData("filePath"), readData("sheetName"));

		System.out.println("Row Count is -> " + rowCount);
		System.out.println("Column count is -> " + columnCount);

		int i = 1;
		int count = 0;

		for (i = 1; i <= rowCount; i++) {

			try {

				for (int j = 0; j < columnCount; j++) {
					String data = readExcel(readData("filePath"), readData("sheetName"), i, j);

					if (data != "") {

						if (data.equals(readExcel(readData("filePath"), readData("sheetName"), (i) - 1, j))) {
							wait(1000);
						} else if (data.contains("Active")) {
							dropDownElement(driver, readExcel(readData("filePath"), readData("sheetName"), 0, j))
									.click();
							Thread.sleep(500);
							driver.findElement(
									By.xpath("//span[@class='mat-checkbox-label' and contains(text(),'" + data + "')]"))
									.click();
							Thread.sleep(500);
							act.moveToElement(orderStatusOverviewTitle(driver)).click().build().perform();
							try {
								if (driver.findElement(By.xpath(
										"//span[@class='mat-checkbox-label' and contains(text(),'" + data + "')]"))
										.isDisplayed()) {
									act.moveToElement(orderStatusOverviewTitle(driver)).click().build().perform();
								}
							} catch (Exception e) {
							}
						} else if (data.contains(";")) {
							dropDownElement(driver, readExcel(readData("filePath"), readData("sheetName"), 0, j))
									.click();
							Thread.sleep(500);

							if (i != 1) {
								checkboxSelectedValue(driver).click();
							}

							for (String value : data.split(";")) {
								dropdownTextBox(driver).sendKeys(value);
								driver.findElement(By.xpath(
										"//span[@class='mat-option-text' and normalize-space(text())='" + value + "']"))
										.click();
								dropdownTextBox(driver).clear();
							}
							Thread.sleep(500);
							act.moveToElement(orderStatusOverviewTitle(driver)).click().build().perform();
						} else {
							dropDownElement(driver, readExcel(readData("filePath"), readData("sheetName"), 0, j))
									.click();
							Thread.sleep(500);

							try {
								if (i != 1) {
									checkboxSelectedValue(driver).click();
								}
							} catch (Exception e) {
							}
							dropdownTextBox(driver).sendKeys(data);
							Thread.sleep(500);
							try {
								if (selectAllLabel(driver).isDisplayed() || seasonActiveLabel(driver).isDisplayed()) {
									driver.findElement(
											By.xpath("//span[@class='mat-option-text' and normalize-space(text())='"
													+ data + "']"))
											.click();
									Thread.sleep(500);
									act.moveToElement(orderStatusOverviewTitle(driver)).click().build().perform();
									try {
										if (driver.findElement(
												By.xpath("//span[@class='mat-option-text' and normalize-space(text())='"
														+ data + "']"))
												.isDisplayed()) {
											act.moveToElement(orderStatusOverviewTitle(driver)).click().build()
													.perform();
										}
									} catch (Exception e1) {
									}
								}
							} catch (Exception e) {
								//if(seasonInactiveLabel(driver).isDisplayed()) {
								driver.findElement(By.xpath("//mat-icon[contains(text(),'close')]")).click();
								Thread.sleep(500);
								act.moveToElement(orderStatusOverviewTitle(driver)).click().build().perform();

								//count--;

								try {
									if (driver.findElement(
											By.xpath("//span[@class='mat-option-text' and normalize-space(text())='"
													+ data + "']"))
											.isDisplayed()) {
										act.moveToElement(orderStatusOverviewTitle(driver)).click().build().perform();
									}
								} catch (Exception e1) {
								}
							//}
							}
						}
					}
				}
			} catch (Exception e) {
			}
			
			try {
				// if(readExcel(readData("filePath"), readData("sheetName"), i, 0) != "" &&
				// activeLabel(driver).isDisplayed()) {
				if (readExcel(readData("filePath"), readData("sheetName"), i, 0) != ""
						&& seasonActiveLabelNew(driver).getText().contains("Active")) {
					wait(1000);
					driver.findElement(By.xpath("//mat-icon[@data-mat-icon-name='excel']")).click();
					count ++;
					WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
					wait.until(ExpectedConditions.invisibilityOf(
							driver.findElement(By.xpath("//mat-progress-spinner[@role='progressbar']"))));

					File file = new File(path);
					File[] fileList = file.listFiles();
					//for (String name : fileList) {
					for(int a=fileList.length-1; a<fileList.length; a++) {
						String name = fileList[0].getName();
						System.out.println(name);
						File f = new File(path+"/"+name);
						File name2 = new File(path+"/"+"Phomas_"+"RowNumber_"+i+".xlsx");
						Thread.sleep(500);
						f.renameTo(name2);
					}
					
				} else {

				}
			} catch (Exception e) {
			}
		}

		// Count no of files
		File directory = new File(path);
		int noOfFiles = directory.list().length;
		System.out.println("No of files is -> " + noOfFiles);
		System.out.println("Count is -> " + count);

		Assert.assertEquals(count, noOfFiles, "No of files downloaded is not equal to no of rows in excel sheet");

		System.out.println("----------------------------------------");

//		File file = new File(createFolder("Phomas"));
//		String[] fileList = file.list();
//		for (String name : fileList) {
//			System.out.println(name);
//			File f = new File(path+"/"+name);
//			File name2 = new File(path+"/"+"Phomas_"+"RowNumber_"+count+".xlsx");
//			f.renameTo(name2);
//		}
		
		File file2 = new File(createFolder("Phomas"));
		String[] fileList2 = file2.list();
		for (String name2 : fileList2) {
			System.out.println(name2);
		}

		// PowerhsellExecutor.processFile(createFolder("Phomas"));
		// AzureBlobStorage.fileUploads(createFolder("Phomas"));

		// driver.close();
	}
}
