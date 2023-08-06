package com.nagp.logs;

import java.io.IOException;


import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.nagp.core.Config;
import com.nagp.core.DriverFactory;
import com.nagp.core.TestBase;
import com.nagp.util.CommonUtil;

/**
 * This class takes care of all the logging. Using this class, you can :
 * <ul>
 * <li>Write console logs. See {@link #getConsoleLogger()}</li>
 * <li>Write report logs. See {@link #getReportLogger()}</li>
 * </ul>
 * {@link LoggingManager} has been kept Singleton as there is only one logging
 * manager required throughout the application. In order for the report logging
 * to work, a currently executing test case must capture events such
 * {@link BeforeClass}, {@link BeforeMethod}, {@link AfterMethod} and
 * {@link AfterClass} and correspondingly inform the {@link LoggingManager} via
 * helper methods such {@link #createParentTestNode(String)},
 * {@link #createChildTestNode(String)}, {@link #clearChildTestNode()} and
 * {@link #clearParentTestNode()} respectively so as to print reports correctly.
 * 
 */
public class LoggingManager {

	private final ThreadLocal<ExtentTest> parentNodeReportLogger;
	private final ThreadLocal<ExtentTest> childNodeReportLogger;
	private final ThreadLocal<String> currentElementPath;
	private final ThreadLocal<Logger> consoleLogger;
	private ExtentReports extentReports;

	/**
	 * 
	 * Helper class to create singleton instance of {@link LoggingManager}
	 *
	 */
	private static class LazyHolder {
		private static final LoggingManager INSTANCE = new LoggingManager();
	}

	/**
	 * 
	 * @return instance of {@link LoggingManager}
	 */
	public static LoggingManager getInstance() {
		return LazyHolder.INSTANCE;
	}

	private LoggingManager() {
		// Keep this constructor private to prevent LoggingManager instance creation
		parentNodeReportLogger = new ThreadLocal<>();
		childNodeReportLogger = new ThreadLocal<>();
		currentElementPath = new ThreadLocal<>();
		consoleLogger = new ThreadLocal<Logger>() {
			@Override
			protected Logger initialValue() {
				return Logger.getLogger("Thread (" + Thread.currentThread().getId() +"-"+Thread.currentThread().hashCode ()+ "-"+Thread.currentThread().getName()+")");
			}
		};
		extentReports = ExtentManager.getExtentReportInstance();
	}

	/**
	 * Returns a {@link Logger} instance to log information to console
	 *
	 * @return Logger
	 */
	public static Logger getConsoleLogger() {
		return getInstance().consoleLogger.get();
	}

	/**
	 * Creates a parent test based on the provided name
	 *
	 * @param parentTestName
	 *            name of parent test
	 */
	public void createParentTestNode(String parentTestName) {
		ExtentTest parentTest = extentReports.createTest(parentTestName);
		parentNodeReportLogger.set(parentTest);
	}

	/**
	 * Creates a child test based on the provided name
	 *
	 * @param childTestName
	 *            name of child test
	 */
	public void createChildTestNode(String childTestName) {
		ExtentTest childTest = parentNodeReportLogger.get().createNode(childTestName);
		childNodeReportLogger.set(childTest);
	}

	/**
	 * Returns an instance of report logger to log reports
	 *
	 * @return report logger of type {@link ExtentTest}
	 */
	public static ExtentTest getReportLogger() {
		return getInstance().childNodeReportLogger.get();
	}

	/**
	 * Clear the parent test
	 */
	public void clearParentTestNode() {
		parentNodeReportLogger.remove();
	}

	/**
	 * Clear the child test
	 */
	public void clearChildTestNode() {
		childNodeReportLogger.remove();
	}

	/**
	 * Write the test report to the file
	 */
	public synchronized void writeTestReport() {
		extentReports.flush();
	}

	/**
	 * Get the current set element path
	 *
	 * @return Current set element path
	 */
	public String getCurrentElementPath() {
		return currentElementPath.get();
	}

	/**
	 * Set the element path
	 *
	 * @param elementPath
	 *            element path
	 */
	public void setCurrentElementPath(String elementPath) {
		currentElementPath.set(elementPath);
	}

	/**
	 * This method logs the test result into the extent report
	 *
	 * @param result
	 *            ITestResult instance with the current status of the test case
	 */
	public void logTestStatus(ITestResult result) {
		if(null==getReportLogger()) {
			createChildTestNode(result.getName());
		}
		try {
			if (result.getStatus() == ITestResult.SUCCESS) {
				getConsoleLogger().info("Test case PASSED");
				getReportLogger().pass(MarkupHelper.createLabel(result.getName() + " Test case PASSED", ExtentColor.GREEN));
				// Evidence can be logged into the report for test success via screenshot
				if (Config.screenshotOnPass.equalsIgnoreCase("true")) {
					String screenShotPath = CommonUtil.captureScreenshot(DriverFactory.getDriver(),"Pass", result);
					getReportLogger().pass("Snapshot below: "+ getReportLogger().addScreenCaptureFromPath(screenShotPath));
				}
			} else if (result.getStatus() == ITestResult.FAILURE) {
				getConsoleLogger().info("Test case FAILED");
				getReportLogger().fail(result.getThrowable());
				getReportLogger().fail(MarkupHelper.createLabel(result.getName() + " Test case FAILED", ExtentColor.RED));
				if (Config.screenshotOnFailure.equalsIgnoreCase("true")) {
					String screenShotPath = CommonUtil.captureScreenshot(DriverFactory.getDriver(),"Failure", result);
					getReportLogger().fail("Snapshot below: "+ getReportLogger().addScreenCaptureFromPath(screenShotPath));
					getConsoleLogger().info("This test method " + result.getName() + " failed on browser ->" + TestBase.BROWSER_VALUE);
				}
			} else if (result.getStatus() == ITestResult.SKIP) {
				getConsoleLogger().info("Test case SKIPPED");
				getReportLogger().skip(result.getThrowable());
				getReportLogger().skip(MarkupHelper.createLabel(result.getName() + " Test case SKIPPED", ExtentColor.BLUE));
				if (Config.screenshotOnSkip.equalsIgnoreCase("true")) {
					String screenShotPath = CommonUtil.captureScreenshot(DriverFactory.getDriver(),"Skipped", result);
					getReportLogger().skip("Snapshot below: "+ getReportLogger().addScreenCaptureFromPath(screenShotPath));
				}
			}
			getConsoleLogger().info(" Test Method completed -> " + result.getName());
		} catch (IOException e) {
			getConsoleLogger().error("Exception occured during result logging\n" + e);
		} finally {
			LoggingManager.getInstance().clearChildTestNode();
		}
	}
}
