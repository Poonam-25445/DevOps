package com.nagp.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class reads the "app.properties" file.
 */
public class Config {
	
	private static Logger log = Logger.getLogger(Config.class);
	
	/** The path where extent report file will be generated. */
	public static String ExtentReportsPath;
	
	/** The path where test reports will be generated. */
	public static String TestReportFolder;
		
	/** The extent report file title. */
	public static String ReportTitle;
	
	/** The heading of extent report. */
	public static String ReportName;
	
	/** The path where screen shots will be generated. */
	public static String ScreenShotsPath;
	
	/** The URL of the application under test. */
	public static String ApplicationURL;
	
	/** The name of the browser (firefox, chrome, ie, safari) used to open the application URL. */
	public static String Browser;
	
	/** The application environment (like RC, QA, STAGING, DEV) */
	public static String Environment;
	
	/** The path where app.properties file is present. */
	public static String AppConfig;
	
	/** The path of CSV file in which test data is present. */
	public static String dataInputFile;
	
	/** The path of file in which element locators are present. */
	public static String locatorsFile;
	
	/** The ofile input stream. */
	public static FileInputStream ofileInputStream;
	
	/** The path where test report archive file be generated. */
	public static String ZipPath;
	
	/** The path of test data file. */
	public static String testData;
	
	/** If 'true' screenshot will be taken when test case is failed. */
	public static String screenshotOnFailure;
	
	/** If 'true' screenshot will be taken when test case is skipped. */
	public static String screenshotOnSkip;
	
	/** If 'true' screenshot will be taken when test case is passed. */
	public static String screenshotOnPass;

	public static String ExportFilePath;
	
	public static String log4jPath;
	
	/** The Firefox driver exe file path. */
	public static String FirefoxDriverPath;
	
	/** The IE driver exe file path. */
	public static String IEDriverPath;
	
	public static String CurrentTestResultPrefix;
	
	/**
	 * Sole constructor. (For invocation by subclass 
	 * constructors, typically implicit.)
	 */
	public Config() {
		
	}

	/**
	 * Reads the app.properties file and store the values in above defined fields.
	 */
	public static void initConstants() {

		String path = System.getProperty("user.dir") + "\\Config\\app.properties";
		Properties prop = new Properties();

		try {
			ofileInputStream = new FileInputStream(path.replace("\\", File.separator));
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd_HHmmssSSS_z");
			Date now = new Date();
			String dateTime = sdfDate.format(now);
			if (ofileInputStream != null) {
				prop.load(ofileInputStream);
				CurrentTestResultPrefix = prop.getProperty("CurrentTestResultPrefix");
				ApplicationURL = prop.getProperty("ApplicationURL");
				log4jPath = System.getProperty("user.dir") + prop.getProperty("log4jPath").replace("\\", File.separator);
				dataInputFile = System.getProperty("user.dir") + prop.getProperty("CSVFile").replace("\\", File.separator);
				locatorsFile = System.getProperty("user.dir") + prop.getProperty("ORXmlFile").replace("\\", File.separator);
				ExtentReportsPath = System.getProperty("user.dir") + prop.getProperty("ExtentReportsPath").replace("\\", File.separator).replace("${timestamp}", dateTime).replace("${CurrentTestResultPrefix}", CurrentTestResultPrefix);
				ReportTitle = prop.getProperty("ReportTitle");
				ReportName = prop.getProperty("ReportName");
				TestReportFolder =System.getProperty("user.dir") + prop.getProperty("TestReports").replace("\\", File.separator);
				ScreenShotsPath = System.getProperty("user.dir") + prop.getProperty("ScreenShotsPath").replace("\\", File.separator).replace("${timestamp}", dateTime).replace("${CurrentTestResultPrefix}", CurrentTestResultPrefix);
				AppConfig = System.getProperty("user.dir") + prop.getProperty("AppConfig").replace("\\", File.separator);
				ZipPath = System.getProperty("user.dir") + prop.getProperty("ZipPath").replace("\\", File.separator);
				testData = System.getProperty("user.dir") + prop.getProperty("TestDataPath").replace("\\", File.separator);
				screenshotOnFailure = prop.getProperty("ScreenshotOnFailure");
				screenshotOnSkip = prop.getProperty("ScreenshotOnSkip");
				screenshotOnPass = prop.getProperty("ScreenshotOnPass");
				ExportFilePath = prop.getProperty("ExportFilePath").replace("\\", File.separator);
				IEDriverPath = System.getProperty("user.dir") + prop.getProperty("IEDriverPath");
				if(ApplicationURL == null) {
					log.info("Environment " + Environment + " is not supported by test suite.");	
				}
			}

		} catch (IOException e) {
			System.err.println("Cannot find the app.properties file at " + path);
		} finally {
			if (ofileInputStream != null) {
				try {
					ofileInputStream.close();
				} catch (IOException e) {
					System.err.println("Cannot close the app.properties file instance.");
				}
			}
		}

	}
}