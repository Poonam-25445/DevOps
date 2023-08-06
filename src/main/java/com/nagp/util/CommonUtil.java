package com.nagp.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.ITestResult;

import com.nagp.core.Config;
import com.nagp.core.TestBase;
import com.nagp.logs.LoggingManager;

/**
 * This class contains various methods which are getting used in test method creation.
 */
public class CommonUtil {

	private static String methodName;
	private static int fileNumber = 1;

	/**
	 * Instantiates a new util.
	 */
	private CommonUtil() {
		LoggingManager.getConsoleLogger().info(" : Util Constructor Called");
	}

	/**
	 * Deletes old files from specified directory.
	 *
	 * @param directory 	path of the directory contains old files.
	 * @throws IOException 	Signals that an I/O exception has occurred.
	 */
	public static void deleteOldFiles(String directory) throws IOException {
		File targetDir = new File(directory);
		FileUtils.cleanDirectory(targetDir);
	}

	/**
	 * Returns a unique text by using the date time stamp in the format
	 * yyyyMMdd_HHmmss_z.
	 *
	 * @return String	unique name generated.
	 */
	public static String generateUniqueName() {
		LoggingManager.getConsoleLogger().info(" : GenerateUniqueNameMethod Method Called");
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd_HHmmssSSS_z");
		Date now = new Date();
		String uniqueText = sdfDate.format(now);
		return (uniqueText);
	}


	/**
	 * Returns a unique text by using the date time stamp in the format
	 * yyyyMMdd_HHmmss_z.
	 *
	 * @param length    the number of characters to return 
	 * @return String	unique name generated.
	 */
	public static String generateUniqueName(int length) {
		return generateUniqueName().substring(0, length);
	}
	
	/**
	 * Captures the screenshot.
	 *
	 * @param eDriver 			webdriver object.
	 * @param screenshotName 	screenshot name.
	 * @param result 			ItestResult object.
	 * @return String			directory path where screenshot will be created.
	 */
	public static String captureScreenshot(WebDriver eDriver, String screenshotName, ITestResult result) {
		LoggingManager.getConsoleLogger().info(" : CaptureScreeshot Method Called");
		try {
			WebDriver augmentedDriver = new Augmenter().augment(eDriver);
	        File source = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
	        LoggingManager.getConsoleLogger().info("Screenshot taken.");
	        Throwable th = result.getThrowable();
	        String error = th.getClass().getName();
	        String dest = Config.ScreenShotsPath + screenshotName + "_" + result.getInstanceName()+"_"+result.getName()+"_"+error+".png";
			File destination = new File(dest);
			if (!result.getName().equals(methodName)) {
				methodName = result.getName();
				fileNumber = 1;
			}
			if (destination.exists()) {
				dest = Config.ScreenShotsPath + screenshotName + "_" + result.getInstanceName() + "_" + result.getName()+"_"+error+ "(" + fileNumber++ + ")" + ".png";
				destination = new File(dest);
			}
			FileUtils.copyFile(source, destination);
			return dest;
		} catch (Exception ex) {
			LoggingManager.getConsoleLogger().error(ex.getMessage());
			return ex.getMessage();
		}
	}

	/**
	 * Get the folder size in bytes.
	 *
	 * @param directory 	path of folder.
	 * @return long 		size in bytes.
	 */
	public static long getFolderSizeInByte(String directory) {
		File file = new File(directory);
		long size = FileUtils.sizeOfDirectory(file);
		return size;
	}

	/**
	 * Get the folder size in proper format like KB, MB, GB, Bytes.
	 *
	 * @param directory 	path of folder.
	 * @return String 		size in bytes.
	 */
	public static String getFolderSize(String directory) {
		String hrSize = null;
		Long size = getFolderSizeInByte(directory);
		double b = size;
		double k = size / 1024.0;
		double m = ((size / 1024.0) / 1024.0);
		double g = (((size / 1024.0) / 1024.0) / 1024.0);
		double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

		DecimalFormat dec = new DecimalFormat("0.00");

		if (t > 1) {
			hrSize = dec.format(t).concat(" TB");
		} else if (g > 1) {
			hrSize = dec.format(g).concat(" GB");
		} else if (m > 1) {
			hrSize = dec.format(m).concat(" MB");
		} else if (k > 1) {
			hrSize = dec.format(k).concat(" KB");
		} else {
			hrSize = dec.format(b).concat(" Bytes");
		}
		return hrSize;
	}

	/**
	 * Zip the specified folder.
	 *
	 * @param Destloc	path to store the zipped file.
	 * @param sourceLoc path of folder to be zipped.
	 */
	public static void zipfolder(String Destloc, String sourceLoc) {
		try {
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(Destloc + ".zip"));
			zipDir(sourceLoc, zos);
			zos.close();
		} catch (Exception ex) {
			LoggingManager.getConsoleLogger().error(ex.getMessage());
		}
	}

	/**
	 * Zip the specified directory.
	 *
	 * @param dir2zip	path of folder to be zipped.
	 * @param zos		content to be zipped.
	 */
	public static void zipDir(String dir2zip, ZipOutputStream zos){
		FileInputStream fis = null;

		File zipDir = new File(dir2zip);
		String[] dirList = zipDir.list();
		byte[] readBuffer = new byte[2156];
		int bytesIn = 0;
		try {
			for (int i = 0; i < dirList.length; i++) {
				File f = new File(zipDir, dirList[i]);
				if (f.isDirectory()) {
					String filePath = f.getPath();
					zipDir(filePath, zos);
					continue;
				}

				fis = new FileInputStream(f);
				ZipEntry anEntry = new ZipEntry(f.getPath());
				zos.putNextEntry(anEntry);
				while ((bytesIn = fis.read(readBuffer)) != -1) {
					zos.write(readBuffer, 0, bytesIn);
				}
			}
			LoggingManager.getConsoleLogger().info("Folder is zipped successfully.");
		} catch (FileNotFoundException ex) {
			LoggingManager.getConsoleLogger().error("Error ocurred while zipping the folder: " + ex.getMessage());
		} catch (IOException ex) {
			LoggingManager.getConsoleLogger().error("Error ocurred while zipping the folder: " + ex.getMessage());
		}catch (NullPointerException ex) {
			LoggingManager.getConsoleLogger().error("Error ocurred while zipping the folder: " + ex.getMessage());
		} catch (Exception e) {
			LoggingManager.getConsoleLogger().error("Error ocurred while zipping the folder: " + e.getMessage());
		}finally {
			try {
				if(fis!=null) {
					fis.close();
				}
			} catch (IOException e) {
				LoggingManager.getConsoleLogger().error(e.getStackTrace());
			}
		}
	}

	/**
	 * Sets the screenshot relative path.
	 */
	public static void setScreenshotRelativePath() {
		FileReader fr = null;
		FileWriter fw = null;
		LoggingManager.getConsoleLogger().info(" : sshotSetRelativePath Method Called");
		try {
			File f = new File(Config.ExtentReportsPath);
			ArrayList<String> lines = new ArrayList<String>();
			String line;
			fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				if (line.contains("<img")) {
					line = line.replace(Config.ScreenShotsPath, "./");
				}
				lines.add(line + "\n");
			}
			fr.close();
			br.close();
			fw = new FileWriter(f);
			BufferedWriter out = new BufferedWriter(fw);
			for (String s : lines)
				out.write(s);
			out.flush();
			fw.close();
			out.close();
		} catch (FileNotFoundException ex) {
			LoggingManager.getConsoleLogger().error(ex.getMessage());
		} catch (Exception ex) {
			LoggingManager.getConsoleLogger().error(ex.getMessage());
		}
	}

	/**
	 * Zip the screenshot created.
	 *
	 * @param screenShotsPath 	screen shots path
	 * @param zipPath 			zip path
	 * @throws Exception 		if error ocurred.
	 */
	public static void zipFolder(Path screenShotsPath, Path zipPath) throws Exception {

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));
		try {
			Files.walkFileTree(screenShotsPath, new SimpleFileVisitor<Path>() {
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					zos.putNextEntry(new ZipEntry(screenShotsPath.relativize(file).toString()));
					Files.copy(file, zos);
					zos.closeEntry();
					return FileVisitResult.CONTINUE;
				}
			});
		}catch(IOException e) {
			LoggingManager.getConsoleLogger().error("Error ocurred while closing the file: " + e.getMessage());
		}finally {
			zos.close();
		}

	}

	
	/**
	 * Checks if folder exist at the specified path.
	 *
	 * @param filePath  path of the file.
	 */
	public static void isFolderExistAtPath(String filePath) {
		File folder = new File(filePath);
		if (folder.exists() && folder.isDirectory()) {
			LoggingManager.getConsoleLogger().info(folder.getName()+ " Path: "+ folder.getAbsolutePath());
		} else {
			LoggingManager.getConsoleLogger().info(folder.getName()+" folder doesn't exist at the path: " + folder.getAbsolutePath());
			LoggingManager.getConsoleLogger().info("Creating Folder");
			folder.mkdirs();
			LoggingManager.getConsoleLogger().info(folder.getName()+ " folder created at path: "+folder.getAbsolutePath());
		}

	}
	/**
	 * This method will return export/import file path based on platform type
	 * 
	 * @param path		export/import path from app properties
	 * @return path		return update file path based on platform type
	 */
	public static String getFullPathBasedOnPlatformType(String path){
		path = path.replace("."+ File.separator, File.separator);  
		if (!TestBase.PLATFORM_VALUE.equalsIgnoreCase(Platform.LINUX.toString())) {
			path = System.getProperty("user.dir") + path;
		}
	    return path;  
	}
	
	public static File getCurrentResultDirectory(String path, String prefix) {
		File[] directories = new File(path).listFiles(File::isDirectory);
		Optional<File> currentResultDirectory = Arrays.stream(directories).filter(file -> file.getName().startsWith(prefix)).findFirst();
		return currentResultDirectory.orElse(null);
	}
}
