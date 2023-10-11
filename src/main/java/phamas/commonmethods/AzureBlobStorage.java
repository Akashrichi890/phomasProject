package phamas.commonmethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

public class AzureBlobStorage {

	public static List<String> filesListFromFolder(String path) {
		List<String> filePathList = new ArrayList<>();
		File directoryPath = new File(path);
		File filesList[] = directoryPath.listFiles();
		if (filesList != null) {
			for (File file : filesList) {
				if (file.isDirectory()) {
					filePathList.addAll(filesListFromFolder(file.getAbsolutePath()));
				} else {
					filePathList.add(file.getAbsolutePath());
				}
			}
		}
		return filePathList;
	}

	public static boolean fileUploads(String filePath) {
		try {

			List<String> filePathList = filesListFromFolder(filePath);

			String storageConnectionString = CommonMethods.readData("azure.storage.connectStr");

			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

			// Create the blob client.
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

			// Retrieve reference to a previously created container.
			CloudBlobContainer container = blobClient
					.getContainerReference(CommonMethods.readData("azure.storage.container"));

			filePathList.stream().forEach(file -> {
				String newPath = file.replace(filePath, "");
				try {
					CloudBlockBlob blob = container.getBlockBlobReference(newPath);
					File source = new File(file);
					blob.upload(new FileInputStream(source), source.length());
				} catch (URISyntaxException | StorageException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(file);
			});
			
		} catch (Exception e) {
			System.out.print("===================");
			e.printStackTrace();
		}
		return true;
	}
}
