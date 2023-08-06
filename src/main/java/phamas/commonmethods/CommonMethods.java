package phamas.commonmethods;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Driver;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CommonMethods {
	
	public static WebDriver driver;
	public static String Data;
	
	public static String readData(String key) throws IOException {
	
	FileReader reader=new FileReader("src\\test\\resources\\testdata.properties");  
      
    Properties p=new Properties();  
    p.load(reader);  
    
    String value = p.getProperty(key);
    return value;
	
	}
	
	public static String readExcel(String ExcelName, String sheet, int rowno, int colno) throws Exception {
		
		FileInputStream file = new FileInputStream(ExcelName);
		@SuppressWarnings("resource")
		Workbook wb = new XSSFWorkbook(file);
		Sheet sh = wb.getSheet(sheet);
		Data = sh.getRow(rowno).getCell(colno).getStringCellValue();
		return Data;
	}
	
	public static int columnCount(String ExcelName, String sheet) throws Exception {
		
		FileInputStream file = new FileInputStream(ExcelName);
		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet sh = wb.getSheet(sheet);
		int columnCount = sh.getRow(0).getLastCellNum();
		
		return columnCount;
	}
	
    public static int rowCount(String ExcelName, String sheet) throws Exception {
		
		FileInputStream file = new FileInputStream(ExcelName);
		@SuppressWarnings("resource")
		Workbook wb = new XSSFWorkbook(file);
		Sheet sh = wb.getSheet(sheet);
		int rowCount = sh.getLastRowNum();
		
		return rowCount;
	}
	
	public static void wait(int time) throws InterruptedException {
		Thread.sleep(time);
	}
	
	public static void moveToElement(WebElement element) {
		//act.moveToElement(element).build().perform();
	}
	
	public static void moveToElementAndClick(WebElement element) {
		//act.moveToElement(element).click().build().perform();
	}
	
	public static WebElement usernameField(WebDriver driver) {
		WebElement usernameField = driver.findElement(By.xpath("//input[@formcontrolname='username']"));
		return usernameField;
	}
	
	public static WebElement passwordField(WebDriver driver) {
		WebElement passwordField = driver.findElement(By.xpath("//input[@formcontrolname='password']"));
		return passwordField;
	}
	
	public static WebElement signInButton(WebDriver driver) {
		WebElement signInButton = driver.findElement(By.xpath("//span[text()='SIGN IN']"));
		return signInButton;
	}
	
    public static WebElement orderStatusOverviewTitle(WebDriver driver) {
    	WebElement orderStatusOverviewTitle = driver.findElement(By.xpath("//span[text()='Order Status Overview']"));
    	return orderStatusOverviewTitle;
    }
	
	public static WebElement dropDownElement(WebDriver driver, String colName) {
		WebElement dropDownElement = driver.findElement(By.xpath("//mat-label[contains(text(),'"+colName+"')]/../../../mat-select/div/div/div"));
		return dropDownElement;
	}
	
	public static WebElement dropdownTextBox(WebDriver driver) {
		WebElement dropdownTextBox = driver.findElement(By.xpath("//input[contains(@placeholder,'Search for')]"));
		return dropdownTextBox;
	}
	

}
