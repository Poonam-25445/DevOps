package com.nagp.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.nagp.logs.LoggingManager;

/**
 * 
 * This listener will inform the reporting manager on test results (failure,
 * success, skip) which occurred during test execution.
 * 
 */
public class TestNGListeners implements ITestListener{
	
	/**
	 * Report the fail status in Logging Manager on test case failure.
	 * 
	 * @param result	Test Result instance
	 */
	@Override
	public void onTestFailure(ITestResult result) {
		LoggingManager.getInstance().logTestStatus(result);
	}

	/**
	 * Report the skip status in Logging Manager on test case skipped.
	 * 
	 * @param result	Test Result instance
	 */
	@Override
	public void onTestSkipped(ITestResult result) {
		LoggingManager.getInstance().logTestStatus(result);
	}

	/**
	 * Report the success status in Logging Manager on test case success.
	 * 
	 * @param result		Test Result instance
	 */
	@Override
	public void onTestSuccess(ITestResult result) {
		LoggingManager.getInstance().logTestStatus(result);
	}

	/**
	 * Report the finish status in Logging Manager on on test suit finish
	 * 
	 * @param context	Test Context instance
	 */
	@Override
	public void onFinish(ITestContext context) {
		LoggingManager.getConsoleLogger().info("Test Suite executed -> " + context.getName());
		try {
		} catch (Exception exception) {
			LoggingManager.getConsoleLogger().info("JUnit XML report not created due to exception: " + exception);
		}
	}

	/**
	 * Report the start status in Logging Manager on on test suit start
	 * 
	 * @param context	Test Context instance
	 */
	@Override
	public void onStart(ITestContext context) {
		LoggingManager.getConsoleLogger().info("Test Suite started -> " + context.getName());
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
	}

	/**
	 * Report the test start status in Logging Manager on test test start
	 * 
	 * @param result		Test Result instance
	 */
	@Override
	public void onTestStart(ITestResult result) {
		LoggingManager.getConsoleLogger().info("Test Method started -> " + result.getName());
	}
	
}
