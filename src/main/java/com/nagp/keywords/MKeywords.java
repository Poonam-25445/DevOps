package com.nagp.keywords;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.collections4.CollectionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;
import com.nagp.core.Config;
import com.nagp.core.DriverFactory;
import com.nagp.logs.LoggingManager;
import com.nagp.util.ReadFileUtil;

/**
 * This class contains the methods that read the element locators and their
 * values from ObjectRepository.xml file.
 */
public class MKeywords {

	/**
	 * Get page the element.
	 *
	 * @param locatorValue value of locator.
	 * @param locatorType  type of locator.
	 * @return By returns By data type.
	 * @throws Exception on error.
	 * @see Exception
	 */
	public static By getElement(String locatorValue, String locatorType) {
		By element = null;
		try {
			switch (locatorType.toLowerCase()) {
			case "id":
				element = By.id(locatorValue);
				break;
			case "name":
				element = By.name(locatorValue);
				break;
			case "classname":
			case "class":
				element = By.className(locatorValue);
				break;
			case "tagname":
				element = By.tagName(locatorValue);
				break;
			case "linktext":
				element = By.linkText(locatorValue);
				break;
			case "partiallinktext":
				element = By.partialLinkText(locatorValue);
				break;
			case "cssselector":
				element = By.cssSelector(locatorValue);
				break;
			case "xpath":
				element = By.xpath(locatorValue);
				break;
			default:
				throw new Exception(
						"DOM FINDER : did not find the correct dom finder type in the file for locator value: "
								+ locatorValue);
			}
		} catch (Exception ex) {
			LoggingManager.getReportLogger().log(Status.INFO, "Failed to read data from XML file" + ex);
			LoggingManager.getConsoleLogger().info("Failed to read data from XML file" + ex);
		}
		return element;
	}
	
	/**
	 * Finds the page element.
	 *
	 * @param PageName   page name tag in ObjectRepository.xml file.
	 * @param objectName name of element.
	 * @return By returns By data type.
	 */
	public static By findElement(String PageName, String objectName) {
		By element = null;
		List<String> listLocator = getLocatorDetails(objectName, PageName);
		if (listLocator != null && !listLocator.isEmpty()) {
			element = getElement(listLocator.get(0), listLocator.get(1));
		}
		return element;
	}

	/**
	 * Pause the execution of test for defined time.
	 *
	 * @param milliseconds time in milliseconds.
	 */
	public static void Sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception ex) {
			LoggingManager.getReportLogger().log(Status.FAIL, "exception Occured while waiting " + ex);
			LoggingManager.getConsoleLogger().error("exception Occured while waiting" + ex);
		}
	}

	/**
	 * Get Absolute path to file
	 * 
	 * @param log4jPath log4jPath
	 * @return String path.
	 */
	public static String getAbsolutePath(String log4jPath) {
		File file = new File(log4jPath);
		String path = file.getAbsolutePath();
		return path;
	}

	/**
	 * Gets the locator details
	 * 
	 * @param objectName name of element.
	 * @param PageName   page name tag in ObjectRepository.xml file.
	 * @return List of string
	 */
	private static List<String> getLocatorDetails(String objectName, String PageName) {
		List<String> listLocator = null;
		try {
			LoggingManager.getInstance().setCurrentElementPath(objectName);
			String xmlPath = Config.locatorsFile;
			listLocator = ReadFileUtil.getXmlValue(objectName, xmlPath, PageName);
		} catch (Exception ex) {
			LoggingManager.getReportLogger().log(Status.INFO, "Failed to read data from XML file" + ex);
			LoggingManager.getConsoleLogger().info("Failed to read data from XML file" + ex);
		}
		return listLocator;
	}
	
	/**
	 * Gets the dropdown values.
	 *
	 * @param dropDownButton the drop down button selector
	 * @param dropDownList the drop down list selector
	 * @return the dropdown values
	 */
	public static ArrayList<String> getDropdownValues(By dropDownButton, By dropDownList) {
		ArrayList<String> dropdownValues = null;
		try {
			dropdownValues = new ArrayList<String>();
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(30));
			MKeywords.Sleep(2000);
			SeKeywords.moveToElement(dropDownButton);
			wait.until(ExpectedConditions.elementToBeClickable(dropDownButton));
			SeKeywords.scrollAndClick(dropDownButton);
			MKeywords.Sleep(1000);
			wait.until(ExpectedConditions.elementToBeClickable(dropDownList));
			WebElement ul = DriverFactory.getDriver().findElement(dropDownList);
			List<WebElement> options = ul.findElements(By.tagName("li"));
			for (WebElement option : options) {
				if (option.getText() != null && !option.getText().isEmpty() && !option.getText().equals("")) {
					Actions a=new Actions(DriverFactory.getDriver());
					a.moveToElement(option).build().perform();
					dropdownValues.add(option.getText());
				}
			}
		} catch (NoSuchElementException ex) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "No Element Found to enter text");
			LoggingManager.getConsoleLogger().error("No Element Found to enter text" + ex);
		}
		return dropdownValues;
	}
	
	public static boolean CompareTwoArrayOfStrings(ArrayList<String> reasonCodes, ArrayList<String> reasonCodesOnUI) {
		return CollectionUtils.containsAll(reasonCodes, reasonCodesOnUI);
	}
	
	/**
	 * This method selects drop-down value.
	 *
	 * @param dropDownbutton 	 drop down button element.
	 * @param dropDownList 		 drop down list appears after clicking on drop down button. 
	 * @param value 			 value user wants to select from the list.
	 */
	public static void selectDropdownValue(By dropDownbutton, By dropDownList, String value){
		try{
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(20));
			wait.until(ExpectedConditions.presenceOfElementLocated(dropDownbutton));
			SeKeywords.moveToElement(dropDownbutton);
			if (!SeKeywords.getText(dropDownbutton).equals(value)) {
				wait.until(ExpectedConditions.elementToBeClickable(dropDownbutton));
				SeKeywords.seleniumClick(dropDownbutton);
				wait.until(ExpectedConditions.visibilityOfElementLocated(dropDownList));
				// Scroll up to load all drop down values into the DOM
				WebElement ul = DriverFactory.getDriver().findElement(dropDownList);
				
				List<WebElement> list = ul.findElements(By.xpath(".//li/a/span[normalize-space()='" + value.trim() + "']"));
				if(list == null || list.isEmpty()) {
					list = ul.findElements(By.xpath(".//li/a[normalize-space()='" + value.trim() + "']"));
				}
				if(list == null || list.isEmpty()) {
					throw new NoSuchElementException(value +" option not found in dropdown");
				}
				WebElement option = list.get(0);
				
				if (SeKeywords.isFirefoxBrowser || SeKeywords.isIeBrowser) {
					JavaScriptKeywords.scrollElementIntoView(option, true);
				} else {
					Actions action = new Actions(DriverFactory.getDriver());
					action.moveToElement(option).build().perform();
				}
				wait.until(ExpectedConditions.elementToBeClickable(option));
				JavaScriptKeywords.clickUsingJs(option);
				// User needs to wait for a short time for the drop down to close after the
				// option is clicked. Following code is added to avoid hard wait like
				// Thread.sleep(1000)
				WebDriverWait shortWait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(2));
				try {
					shortWait.until(ExpectedConditions.invisibilityOfElementLocated(dropDownList));
				} catch (TimeoutException e) {
					LoggingManager.getReportLogger().log(Status.INFO,
							"\"" + "TimeOut while waiting invisibility of drop down after click. " + "\""
									+ LoggingManager.getInstance().getCurrentElementPath() + "\""
									+ " not found. " + "\"");
				}
				// This if is added to cater the fact that some drop down values do not get
				// selected with JS click but require a normal click
				if (SeKeywords.isElementVisible(dropDownList)) {
					option.click();
				}
				LoggingManager.getReportLogger().log(Status.PASS, "\"" + value + "\"" + " selected from " + "\""
						+ LoggingManager.getInstance().getCurrentElementPath() + "\"");
				LoggingManager.getConsoleLogger().info("\"" + value + "\"" + " selected from " + "\""
						+ LoggingManager.getInstance().getCurrentElementPath() + "\"");
			}
		
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Dropdown Element not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().error("No Dropdown Element Found" + e);
			throw(e);
		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "TimeOut. " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().error("Timeout. Element not found." + e);
			throw(e);
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Element not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().error("No Such Element Found" + e);
			throw(e);
		}
	}
	
	/**
	 * Search and wait for element for a specified length of time.
	 * 
	 * @param projectId
	 * @param grid
	 * @param gridItem
	 * @param seconds
	 * @return 
	 */
	public static boolean verifySearchedProduct(By grid, String itemName) 
	{
		boolean itemPresent = false;
		try {
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(30));
			wait.until(ExpectedConditions.presenceOfElementLocated(grid));
			WebElement ul = DriverFactory.getDriver().findElement(grid);
			List<WebElement> list = ul.findElements(By.className("product-item"));
			for (WebElement li : list) {
				WebElement productName = li.findElement(By.className("product-item-name"));
					if ((productName.getText().equalsIgnoreCase(itemName))) {
						itemPresent = true;
						LoggingManager.getReportLogger().log(Status.PASS, "\"" + "waiting for elment: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
						break;
					}
			}
			return itemPresent;
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Search Element not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().error("Search Element not found" + e);
			throw(e);
		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "TimeOut. " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().error("Timeout. Element not found." + e);
			throw(e);
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Element not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().error("No Such Element Found" + e);
			throw(e);
		}
	}
}
