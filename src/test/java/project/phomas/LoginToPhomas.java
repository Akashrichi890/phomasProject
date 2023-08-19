package project.phomas;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

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

		for (int i = 1; i < rowCount; i++) {
			try {
				for (int j = 0; j < columnCount; j++) {
					String data = readExcel(readData("filePath"), readData("sheetName"), i, j);
					
					if (data != "") {
					
					if(data.equals(readExcel(readData("filePath"), readData("sheetName"), (i)-1, j))) {
						wait(1000);
					} else if (data.contains("Active")) {
						dropDownElement(driver, readExcel(readData("filePath"), readData("sheetName"), 0, j)).click();
						Thread.sleep(500);
						driver.findElement(By.xpath(
								"//span[@class='mat-checkbox-label' and contains(text(),'" + data + "')]"))
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
					}  else if (data.contains(";")) {
						dropDownElement(driver, readExcel(readData("filePath"), readData("sheetName"), 0, j)).click();
						Thread.sleep(500);
						
						if(i!=1) {
							checkboxSelectedValue(driver).click();
						}
						
						for (String value : data.split(";")) {
							dropdownTextBox(driver).sendKeys(value);
							driver.findElement(By
									.xpath("//span[@class='mat-option-text' and normalize-space(text())='"+value+"']"))
									.click();
							dropdownTextBox(driver).clear();
						}
						Thread.sleep(500);
						act.moveToElement(orderStatusOverviewTitle(driver)).click().build().perform();
					}
					else {
						dropDownElement(driver, readExcel(readData("filePath"), readData("sheetName"), 0, j)).click();
						Thread.sleep(500);
							
						if(i!=1) {
						checkboxSelectedValue(driver).click();
						}
							dropdownTextBox(driver).sendKeys(data);
							Thread.sleep(500);
							driver.findElement(By
									.xpath("//span[@class='mat-option-text' and normalize-space(text())='"+data+"']"))
									.click();
							Thread.sleep(500);
							act.moveToElement(orderStatusOverviewTitle(driver)).click().build().perform();
							try {
								if (driver.findElement(By.xpath(
										"//span[@class='mat-option-text' and normalize-space(text())='"+data+"']"))
										.isDisplayed()) {
									act.moveToElement(orderStatusOverviewTitle(driver)).click().build().perform();
								}
							} catch (Exception e1) {
							}
			}
		} 
		}
				wait(1000);

//				String excelFileName = "C:\\Users\\DELL\\Downloads\\"+"Phomas - KL - "+readExcel(readData("filePath"), readData("sheetName"), i, 0)+".xls"; //name of excel file
//
//				OutputStream fileOut = new FileOutputStream(excelFileName);
//
//				XSSFWorkbook wb = new XSSFWorkbook();
//				XSSFSheet sheet = wb.createSheet("Sheet1") ;
//
//				//Write your code for the content generation here.
//
//				//write this workbook to an Outputstream.
//
//				wb.write(fileOut);
//				fileOut.flush();
//				fileOut.close();
				
				driver.findElement(By.xpath("//mat-icon[@data-mat-icon-name='excel']")).click();
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//mat-progress-spinner[@role='progressbar']")));
		}
		catch(Exception e) {  	
		}
		}
			// j exit
//			wait(1000);
//			driver.findElement(By.xpath("//mat-icon[@data-mat-icon-name='excel']")).click();
//			wait(4000);
//			driver.navigate().refresh();
//			wait(1000);
//			driver.findElement(By.xpath("//span[contains(text(),'Toggle secondary filters')]")).click();
		

	}
}


