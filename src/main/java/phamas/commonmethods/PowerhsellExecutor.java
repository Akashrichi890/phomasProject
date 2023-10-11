package phamas.commonmethods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.openqa.selenium.WebDriver;

public class PowerhsellExecutor {

	public static WebDriver driver;
	public static String Data;

	public static String processFile(String folderPath) throws IOException {

		String command = "powershell.exe  ".concat(CommonMethods.readData("powershellFile"))
				.concat("; Convert-CsvInBatch -Folder '").concat(folderPath)
				.concat("' -Delimiter ';' -ArchiveFL '_archive'");
		Process powerShellProcess = Runtime.getRuntime().exec(command);

		powerShellProcess.getOutputStream().close();

		String line;
		System.out.println("Output:");
		BufferedReader stdout = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
		while ((line = stdout.readLine()) != null) {
			System.out.println(line);
		}
		stdout.close();

		System.out.println("Error:");
		BufferedReader stderr = new BufferedReader(new InputStreamReader(powerShellProcess.getErrorStream()));
		while ((line = stderr.readLine()) != null) {
			System.out.println(line);
		}
		stderr.close();
		System.out.println("Done");
		return null;

	}

}
