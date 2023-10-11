package phamas.commonmethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class CommonMethods {

	public static WebDriver driver;
	public static String Data;
	
	public static String createFolder(String projectName) {
		 
	      String path = "D:\\";
	      //Using Scanner class to get the folder name from the user  
	      path = path+projectName+"_"+java.time.LocalDate.now();  
	      System.out.println("Path is--> "+path);
	      //Instantiate the File class   
	      File f1 = new File(path);  
	      //Creating a folder using mkdir() method  
	      boolean bool = f1.mkdir();  
	      if(bool){  
	         System.out.println("Folder is created successfully");  
	      }else{  
	         System.out.println("Error Found!");  
	      }  
	      return path;
    }
	
    public static ChromeOptions chromeOptions(String path, String headless) throws Exception {
		
        ChromeOptions options = new ChromeOptions();
        
        if(headless.equals("true")) {
        options.addArguments("--headless=new");
        } else {
        	
        }
        
		Map<String, Object> prefs = new HashMap<String, Object>();

		prefs.put("download.default_directory", path);

		options.setExperimentalOption("prefs", prefs);
		
		return options;
	}

	public static String readData(String key) throws IOException {

		FileReader reader = new FileReader("src\\test\\resources\\testdata.properties");

		Properties p = new Properties();
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
		int rowCount = sh.getLastRowNum()+1;

		return rowCount;
	}

	public static void wait(int time) throws InterruptedException {
		Thread.sleep(time);
	}

	public static WebElement orderStatusOverviewLink(WebDriver driver) {
		WebElement orderStatusOverviewLink = driver.findElement(By.xpath("//a[@href='/order/status-overview']"));
		return orderStatusOverviewLink;
	}
	
	public static WebElement toggleFilter(WebDriver driver) {
		WebElement toggleFilter = driver.findElement(By.xpath("//span[contains(text(),'Toggle secondary filters')]"));
		return toggleFilter;
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
		WebElement dropDownElement = driver.findElement(
				By.xpath("//mat-label[contains(text(),'" + colName + "')]/../../../mat-select/div/div/div"));
		return dropDownElement;
	}

	public static WebElement selectAllCheckbox(WebDriver driver) {
		WebElement dropDownElement = driver.findElement(By.xpath("//span[contains(text(),'Select all')]"));
		return dropDownElement;
	}

	public static WebElement dropdownTextBox(WebDriver driver) {
		WebElement dropdownTextBox = driver.findElement(By.xpath("//input[contains(@placeholder,'Search for')]"));
		return dropdownTextBox;
	}

	public static WebElement checkboxSelectedValue(WebDriver driver) {
		WebElement checkboxSelectedValue = driver
				.findElement(By.xpath("//mat-pseudo-checkbox[contains(@class,'checked')]"));
		return checkboxSelectedValue;
	}
	
	public static WebElement inactiveLabel(WebDriver driver) {
		WebElement inactiveLabel = driver.findElement(By.xpath("//input[@type='checkbox']/../following-sibling::span[contains(text(),'Inactive')]"));
		return inactiveLabel;
	}
	
	public static WebElement activeLabel(WebDriver driver) {
		WebElement activeLabel = driver.findElement(By.xpath("//span[contains(text(),'- Active')]"));
		return activeLabel;
	}
	
	public static WebElement seasonActiveLabel(WebDriver driver) {
		WebElement seasonActiveLabel = driver.findElement(By.xpath("//input[@placeholder='Search for Season']/../../../../../mat-option/span/mat-checkbox/label/span[contains(text(),'Active')]"));
		return seasonActiveLabel;
	}
	
	public static WebElement seasonInactiveLabel(WebDriver driver) {
		WebElement seasonInactiveLabel = driver.findElement(By.xpath("//input[@placeholder='Search for Season']/../../../../../mat-option/span/mat-checkbox/label/span[contains(text(),'Inactive')]"));
		return seasonInactiveLabel;
	}
	
	public static WebElement seasonActiveLabelNew(WebDriver driver) {
		WebElement seasonActiveLabelNew = driver.findElement(By.xpath("//mat-label[text()='Season']/../../../mat-select/div/div/span/mat-select-trigger/span"));
		return seasonActiveLabelNew;
	}
	
	public static WebElement selectAllLabel(WebDriver driver) {
		//WebElement selectAllLabel = driver.findElement(By.xpath("//input[@placeholder='Search for Gender']/../../../../../mat-option/span/mat-checkbox/label/span/span[text()='Select all']"));
		WebElement selectAllLabel = driver.findElement(By.xpath("//span[contains(text(),'Select all')]"));
		return selectAllLabel;
	}

}

