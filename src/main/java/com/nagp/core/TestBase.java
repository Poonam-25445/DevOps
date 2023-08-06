package com.nagp.core;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.nagp.keywords.MKeywords;
import com.nagp.logs.LoggingManager;
import com.nagp.util.CommonUtil;

/**
 * This is the base class in which all the initialization required to run the
 * test cases occurs. This class basically includes: 1) Initialization of
 * webdriver, extent reporting and logging. 2) Closing of browser and webdriver.
 */
public class TestBase {
	protected CommonUtil util;
	public static String BROWSER_VALUE = null;
	public static String PLATFORM_VALUE = null;
	protected DriverOptions driverOptions;

	static {
		Config.initConstants();
	}

	/**
	 * Instantiates a new test base.
	 */
	protected TestBase() {
		String log4jPath = MKeywords.getAbsolutePath(Config.log4jPath);
		PropertyConfigurator.configure(log4jPath + File.separator + "log4j.properties");
		driverOptions = new DriverOptions();
		driverOptions.url = getDefaultURL();
	}

	/**
	 * Gets the default URL.
	 *
	 * @return the default URL
	 */
	protected String getDefaultURL() {
		return Config.ApplicationURL;
	}

	/**
	 * This method: 1) creates screenshot folder path if not exists. 2) creates
	 * ZipPath folder if not exists. 4) start video recording if set as 'true'.
	 * 
	 * @throws Exception
	 */
	@BeforeSuite(alwaysRun = true)
	protected void BeforeSuite() throws Exception {
		LoggingManager.getConsoleLogger().info("-----------------EXECUTION START----------------------");
		LoggingManager.getConsoleLogger().info(" : TestBase - BeforeSuite called");
		CommonUtil.isFolderExistAtPath(Config.TestReportFolder);
		CommonUtil.isFolderExistAtPath(Config.ScreenShotsPath);
		CommonUtil.isFolderExistAtPath(Config.ZipPath);
		CommonUtil.isFolderExistAtPath(Config.ExportFilePath);
		FileUtils.deleteDirectory(CommonUtil.getCurrentResultDirectory(Config.TestReportFolder, Config.CurrentTestResultPrefix));
	}

	/**
	 * This method creates extent reporting parent class instance.
	 */
	@BeforeClass(alwaysRun = true)
	protected void BeforeClass() {
		LoggingManager.getConsoleLogger().info(" : TesTBase - BeforeClass called -> " + getClass().getName());
		LoggingManager.getInstance().createParentTestNode(getClass().getName());
	}

	@BeforeTest(alwaysRun = true)
	protected void BeforeTest() {
		LoggingManager.getConsoleLogger().info(" : TesTBase - BeforeTest called");
	}

	/**
	 * Method executes the prerequisites for test execution example: creating driver
	 * instance, launching browser with application URL. This will also execute the
	 * extent report child node creation steps
	 * 
	 * @param webdriverHost IP of the hub machine
	 * @param webdriverPort Port on which grid is running
	 * @param browser       browser for AUT
	 * @param platform      OS value example: WINDOWS, MAC etc
	 * @param method        test method to be invoked
	 * 
	 * @throws FrameworkException User defined exception instance
	 */
	@BeforeMethod(alwaysRun = true)
	@Parameters({ "webdriverHost", "webdriverPort", "browser", "platform" })
	protected void BeforeMethod(String webdriverHost, int webdriverPort, String browser, String platform,
			Method method) {
		try {
			BROWSER_VALUE = browser;
			PLATFORM_VALUE = platform;
			LoggingManager.getConsoleLogger().info(" : TestBase - BeforeMethod called : " + method.getName());
			LoggingManager.getConsoleLogger().info(" : Browser called -> " + browser);
			setDriverOptions(webdriverHost, webdriverPort, browser, platform);
			DriverFactory.getInstance().setDriver(driverOptions);
			LoggingManager.getInstance().createChildTestNode(method.getName());
			LoggingManager.getReportLogger().info("Browser called -> " + browser);
		} catch (FrameworkException e) {
			LoggingManager.getConsoleLogger()
					.error("Framework exception : " + e.getMessage() + " | " + e.getStackTrace());
		} catch (Exception e) {
			LoggingManager.getConsoleLogger().error("System error occured -> " + e.getMessage());
		}
	}

	/**
	 * Sets the driver options based on the parameters received
	 * 
	 * @param webdriverHost IP of the hub machine
	 * @param webdriverPort Port on which grid is running
	 * @param browser       browser for AUT
	 * @param platform      OS value example: WINDOWS, MAC etc
	 */
	protected void setDriverOptions(String webdriverHost, int webdriverPort, String browser, String platform) {
		driverOptions.browser = browser;
		driverOptions.platformType = platform;
		driverOptions.webdriverHost = webdriverHost;
		driverOptions.webdriverPort = webdriverPort;
	}

	/**
	 * This method closes the browser after test execution
	 *
	 * @param result gives user the result of test case.
	 */
	@AfterMethod(alwaysRun = true)
	protected void AfterMethod(ITestResult result) {
		LoggingManager.getConsoleLogger().info(" : TestBase - AfterMethod called -> " + result.getName());

		/* Closes the browser instance */
		DriverFactory.getInstance().removeDriver();
	}

	/**
	 * Clear the extent report parent instance after test class is executed
	 */
	@AfterClass(alwaysRun = true)
	protected void AfterClass() {
		LoggingManager.getConsoleLogger().info(" : TestBase - AfterClass called -> " + getClass().getName());
		LoggingManager.getInstance().clearParentTestNode();
	}

	@AfterTest(alwaysRun = true)
	protected void AfterTest() {
		LoggingManager.getConsoleLogger().info(" : TestBase - AfterTest called");
	}

	/**
	 * This method: 1) closes the extent reporting and flushes the report to an HTML
	 * file. 2) creates the zip folder of screenshots. 3) Email the test report if
	 * set as true. 4) stops recording if set as true.
	 *
	 * @throws Exception on error.
	 */
	@AfterSuite(alwaysRun = true)
	protected void afterSuite() throws Exception {
		LoggingManager.getConsoleLogger().info(" : TestBase - AfterSuite called");
		LoggingManager.getInstance().writeTestReport();
		CommonUtil.setScreenshotRelativePath();
		String zipFilePath = Config.ZipPath + CommonUtil.getCurrentResultDirectory(Config.TestReportFolder, Config.CurrentTestResultPrefix).getName() + ".zip";
		CommonUtil.zipFolder(Paths.get(Config.ScreenShotsPath), Paths.get(zipFilePath));
		LoggingManager.getConsoleLogger().info("---------------EXECUTION COMPLETED--------------------");
	}
}
