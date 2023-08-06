package com.nagp.keywords;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;
import com.nagp.core.DriverFactory;
import com.nagp.core.TestBase;
import com.nagp.logs.LoggingManager;

/**
 * This class contains all the keywords created using methods provided by Selenium Webdriver API.
 */
public class SeKeywords {

	public static boolean isIeBrowser = TestBase.BROWSER_VALUE.equalsIgnoreCase(DriverFactory.INTERNET_EXPLORER);
	public static boolean isFirefoxBrowser = TestBase.BROWSER_VALUE.equalsIgnoreCase(DriverFactory.FIREFOX);

	/**
	 * This method waits for visibility of the element present on web page for specified time.
	 *
	 * @param element	element on web page.
	 * @param secs 		time to wait in seconds.
	 */
	public static boolean isElementPresent(By element){
		boolean result = false;
		try
		{
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10));
			wait.until(ExpectedConditions.presenceOfElementLocated(element));
			LoggingManager.getReportLogger().log(Status.PASS, "\"" + "Element found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().info("Element found -> " + element.toString());
			result = true;
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "Element not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().info("No Such Element Found" + e);
		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "TimeOut. " + "\"" + element.toString() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().info("Timeout. Element not found." + e);
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "Element not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().info("No Such Element Found" + e);
		}
		return result;
	}
	
	/**
	 * Retrieves all the elements with the given selector
	 * 
	 * @param element 		the element which needs to be searched
	 */
	public static List<WebElement> findAllElements(By element){
		try {
			List<WebElement> foundElements = DriverFactory.getDriver().findElements(element);
			String logMessage = "\"" + "Elements found  for " + "\"" + element.toString() + ": \""+ foundElements.size() + "\"";
			LoggingManager.getReportLogger().log(Status.PASS, logMessage);
			LoggingManager.getConsoleLogger().info(logMessage);
			return foundElements;
		}
		catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "TimeOut. " + "\"" + element.toString() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().error("TimeOut. Element not found" + e);
			throw(e);
		} 
		catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Element not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().error("No Such Element Found" + e);
			throw(e);
		}	
	}

	
	/**
	 *  This method checks the enable state of element on web page.
	 *
	 * @param element	element on page.
	 * @return true, 	if element is enabled.
	 */
	public static boolean isElementEnabled(By element){
		boolean result = false;
		try{
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10));
			wait.until(ExpectedConditions.presenceOfElementLocated(element));
			if (DriverFactory.getDriver().findElement(element).isEnabled()) {
				LoggingManager.getReportLogger().log(Status.PASS, "\"" + "Element enabled: " + "\"" + element.toString() + "\"");
				LoggingManager.getConsoleLogger().info("Element enabled -> " + element.toString());
				result = true;
			} else {
				LoggingManager.getReportLogger().log(Status.INFO, "\"" + "Element not enabled: " + "\"" + element.toString() + "\"");
				LoggingManager.getConsoleLogger().info("Element not enabled -> " + element.toString());
				result = false;
			}
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "Element not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().info("No Such Element Found " + e);
		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "TimeOut. " + "\"" + element.toString() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().info("Timeout. Element not found." + e);
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "Element not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().info("No Such Element Found " + e);
		}
		return result;
	}
	
	/**
	 *  Moves the mouse to the specified element.
	 *
	 * @param element	element on page.
	 */
	public static void moveToElement(By element){
		try{
			Actions a=new Actions(DriverFactory.getDriver());
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(30));
			wait.until(ExpectedConditions.presenceOfElementLocated(element));
			if(SeKeywords.isFirefoxBrowser||SeKeywords.isIeBrowser) {
				JavaScriptKeywords.scrollElementIntoView(element, true);
			}else {
				a.moveToElement(DriverFactory.getDriver().findElement(element)).perform();
			}
			LoggingManager.getReportLogger().log(Status.PASS, "\"" + "Scrolled to " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().info("Scrolled to -> " + element.toString());
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Element not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().error("No Such Element Found" + e);
			throw(e);
		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "TimeOut. " + "\"" + element.toString() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().error("Timeout. Element not found." + e);
			throw(e);
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Element not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().error("No Such Element Found" + e);
			throw(e);

		}
	}


	/**
	 *  This method get the text of web element.
	 *
	 * @param element	element on web page.
	 * @return string,	web text.
	 */
	public static String getText(By element) {
		String text = null;
		try {
			SeKeywords.moveToElement(element);
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(30));
			wait.until(ExpectedConditions.presenceOfElementLocated(element));
			text = DriverFactory.getDriver().findElement(element).getText().trim();
			LoggingManager.getReportLogger().log(Status.PASS, "\"" + element.toString() + " is: " + text + "\"");
			LoggingManager.getConsoleLogger().info(element.toString() + " text is -> " + text );
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "getTextElement not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().error("getTextElement not found" + e);
			throw(e);
		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "TimeOut. " + "\"" + element.toString() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().error("Timeout. Element not found." + e);
			throw(e);
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Element not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().error("No Such Element Found" + e);
			throw(e);
		}
		return text;
	}
	


	/**
	 *  This method click on the button present on pop up.
	 *
	 * @param modal 	 pop-up box element.
	 * @param buttonName button name on pop-up.
	 */
	public static void clickModalPopButton(By modal, String buttonName){
		try{
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10));
			DriverFactory.getDriver().switchTo().activeElement();
			WebElement modalbuttons = DriverFactory.getDriver().findElement(modal);
			List<WebElement> options = modalbuttons.findElements(By.tagName("button"));
			for (WebElement option : options) {
				if (buttonName.equalsIgnoreCase(option.getText())){
					MKeywords.Sleep(2000);
					wait.until(ExpectedConditions.elementToBeClickable(option));
					Actions action_obj=new Actions(DriverFactory.getDriver());
					action_obj.moveToElement(option).click().build().perform();
					LoggingManager.getReportLogger().log(Status.PASS, "Clicked on the modal pop up button"  + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
					LoggingManager.getConsoleLogger().info("Clicked on the modal pop up button"  + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
					break;
				}
			}
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Modal Pop Button not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().error("Modal Pop Button not found" + e);
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
	 * This method waits for visibility of the element present on web page.
	 *
	 * @param element	element on web page.
	 * @param secs 		time to wait in seconds.
	 */
	public static boolean waitForElementVisibility(By element , int secs){
		boolean result = false;
		try
		{
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(secs));
			wait.until(ExpectedConditions.visibilityOfElementLocated(element));
			LoggingManager.getConsoleLogger().info("Presence of element visibilty verified for" + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			result = true;
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "Element not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().info("No Such Element Found" + e);
		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "TimeOut. " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().info("Timeout. Element not found." + e);
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "Element not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().info("No Such Element Found" + e);
		}
		return result;
	}
	
	/**
	 * This method wait for element to be clickable on web page.
	 *
	 * @param element 	web element to wait for.
	 * @param secs 		number of seconds to wait.
	 * 
	 */
	public static void waitForElementToBeClickable(By element , int secs){
		try
		{
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(secs));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			LoggingManager.getReportLogger().log(Status.PASS, "Element to be clicked verified: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().info("Element to be clicked verified: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "Clickable Element not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().info("Clickable Element not found" + e);
		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "TimeOut. " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().info("Timeout. Element not found." + e);
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "Element not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().info("No Such Element Found" + e);
		}
	}

	/**
	 * This method waits for the invisibility of the element present on web page. 
	 *
	 * @param element	element on web page.
	 * @param secs 		number of second to wait.
	 */
	public static boolean waitForElementInVisibility(By element , int secs){
		boolean result = false;
		try
		{
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(secs));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(element));
			LoggingManager.getReportLogger().log(Status.PASS, "Element invisibility verified for " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().info("Element invisibility verified for " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			result = true;
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "Element not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().info("No Such Element Found" + e);
		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "TimeOut. " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().info("Timeout. Element not found." + e);
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "Element not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().info("No Such Element Found" + e);
		}
		return result;
	}
	
	/**
	 * This method wait for staleness of element.
	 *
	 * @param element 	web element to wait for.
	 * @param secs 		number of seconds to wait.
	 * 
	 */
	public static void waitForStalenessOfElement(By element , int secs){
		try
		{
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(secs));
			wait.until(ExpectedConditions.stalenessOf(DriverFactory.getDriver().findElement(element)));
			LoggingManager.getReportLogger().log(Status.PASS, "Staleness of element is verified: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().info("Staleness of element is verified: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "Element not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().info("Element not found" + e);
		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "TimeOut. " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().info("Timeout. Element not found." + e);
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "Element not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().info("No Such Element Found" + e);
		}
	}

	/**
	 * This method enter the value in text box.
	 *
	 * @param textBox	text box element.
	 * @param value 	value user wants to enter in the text box.
	 * @param useBackspaceToClear 
	 */
	public static void setText(By textBox, String value) {
		try{
		    SeKeywords.moveToElement(textBox);
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(15));
			wait.until(ExpectedConditions.visibilityOfElementLocated(textBox));
			DriverFactory.getDriver().findElement(textBox).clear();
			sendKeys(textBox, value);
			LoggingManager.getReportLogger().log(Status.PASS, "\"" + value + "\"" + " is entered in text box " + "\"" + textBox.toString() + "\"");
			LoggingManager.getConsoleLogger().info("\"" + value + "\"" + " is entered in text box " + "\"" + textBox.toString() + "\"");
			JavaScriptKeywords.focusOutUsingJs(textBox);
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Textbox to enter text not found: " + "\"" + textBox.toString() + "\"");
			LoggingManager.getConsoleLogger().error("Textbox to enter text not found" + e);
			throw(e);
		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "TimeOut. " + "\"" + textBox.toString() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().error("Timeout. Element not found." + e);
			throw(e);
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Element not found: " + "\"" + textBox.toString() + "\"");
			LoggingManager.getConsoleLogger().error("No Such Element Found" + e);
			throw(e);

		}
	}

	/**
	 * This method enter value in text box and select the element appears from the drop-down list.
	 *
	 * @param textBox 	text box element.
	 * @param list 		list element.
	 * @param value 	value user wants to select.
	 * @param timeoutSeconds   seconds to wait for the textbox is visible of wait
	 */
	public static void searchAndSelect(By textBox , By list , String value){
		try {
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(30));
			wait.until(ExpectedConditions.visibilityOfElementLocated(textBox));
			SeKeywords.setText(textBox, value);
			JavaScriptKeywords.waitForAjax();
			wait.until(ExpectedConditions.visibilityOfElementLocated(list));
			DriverFactory.getDriver().findElement(textBox).sendKeys(Keys.TAB);
			
			LoggingManager.getReportLogger().log(Status.PASS, "\"" + value + "\"" + " searched & selected from " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().info(value + "\"" + " searched & selected from " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
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


	/**
	 *  This method click on specified element on web page.
	 *
	 * @param element	element to click on web page.
	 */
	public static void click(By element) {

		try {
			if(isFirefoxBrowser) {
				SeKeywords.waitForStalenessOfElement(element, 20);
			}
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(30));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			clickElement(element);
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Clickable Element not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().error("Clickable Element not found" + e);
			throw(e);
		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "TimeOut. " + "\"" + element.toString() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().error("Timeout. Element not found." + e);
			throw(e);
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Element not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().error("No Such Element Found" + e);
			throw(e);
		}
	}
	
	
	/**
	 *  This method click on specified element on web page.
	 *
	 * @param element	element to click on web page.
	 */
	public static void clickElement(By element) {

		try {
			if(isIeBrowser) {
				JavaScriptKeywords.clickUsingJs(DriverFactory.getDriver().findElement(element));
			}else {
				DriverFactory.getDriver().findElement(element).click();
			}
			LoggingManager.getReportLogger().log(Status.PASS, "\"" + " Clicked " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().info("\"" + " Clicked " + "\"" + element.toString() + "\"");
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Clickable Element not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().error("Clickable Element not found" + e);
			throw(e);
		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "TimeOut. " + "\"" + element.toString() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().error("Timeout. Element not found." + e);
			throw(e);
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Element not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().error("No Such Element Found" + e);
			throw(e);
		}
	}
	
	/**
	 *  This method click on specified element on web page.
	 *
	 * @param element	element to click on web page.
	 */
	public static void seleniumClick(By element) {

		try {
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(30));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			if(isIeBrowser) {
				JavaScriptKeywords.clickUsingJs(DriverFactory.getDriver().findElement(element));
			}else {
				DriverFactory.getDriver().findElement(element).click();
			} 
			LoggingManager.getReportLogger().log(Status.PASS, "\"" + " Clicked " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().info("\"" + " Clicked " + "\"" + element.toString() + "\"");
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Clickable Element not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().error("Clickable Element not found" + e);
			throw(e);
		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "TimeOut. " + "\"" + element.toString() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().error("Timeout. Element not found." + e);
			throw(e);
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Element not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().error("No Such Element Found" + e);
			throw(e);
		}
	}


	/**
	 * This method scroll the web page and click on web element.
	 *
	 * @param element	element on user wants to click.
	 */
	public static void scrollAndClick(By element) {
		try {
			moveToElement(element);
			seleniumClick(element);
			LoggingManager.getReportLogger().log(Status.PASS, "\"" + "Scrolled & Clicked " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().info("\"" + "Scrolled & Clicked " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Element not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().error("No Such Element Found" + e);
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
	 * This method select the value from drop down list.
	 *
	 * @param dropdown 	drop down web list.
	 * @param value 	value user wants to select from the dropdown list.
	 */
	public static void selectDropdownValue(By dropdown, String value) {
		try{
			Select drpdown = new Select(DriverFactory.getDriver().findElement(dropdown));
			drpdown.selectByVisibleText(value);
			LoggingManager.getReportLogger().log(Status.PASS, "\"" + " Selected " + "\"" + value + "\"" + "from Dropdown");
			LoggingManager.getConsoleLogger().info("\"" + " Selected " + "\"" + value + "\"" + "from Dropdown");
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
	 *  This method get the title of web page.
	 *
	 * @return string,	title of web page.
	 */
	public static String getPageTitle() {
		String title = null;
		try {
			MKeywords.Sleep(1000);
			title = DriverFactory.getDriver().getTitle();
			LoggingManager.getReportLogger().log(Status.PASS, "Title for " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"" + "is" + title + "\"");
			LoggingManager.getConsoleLogger().info( "Title for " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"" + "is" + title + "\"");
			
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Title not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().error("Title not found" + e);
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
		return title;
	}

	/**
	 * This method verify the presence of specified text on the page.
	 *
	 * @param text		text value user want to verify on web page.
	 * @return true,	if text is present on the web page.
	 */
	public static boolean verifyAnyTextOnPage(String text) {
		boolean result = false;
		try {
			if (DriverFactory.getDriver().getPageSource().contains(text)) {
				LoggingManager.getReportLogger().log(Status.PASS, "\"" + text + "\"" + " is present. " + "\"");
				LoggingManager.getConsoleLogger().info("\"" + text + "\"" + " is present. " + "\"");
				result = true;
			} else {
				LoggingManager.getReportLogger().log(Status.INFO, "\"" + text + "\"" + " is not found. " + "\"");
				LoggingManager.getConsoleLogger().info("\"" + text + "\"" + " is not found. " + "\"");
				result = false;
			}
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + text + "\"" + " is not found on Page " + "\"");
			LoggingManager.getConsoleLogger().info("Text is not found on Page " + e);
		}
		return result;
	}
	
	/**
	 * This method verify the visibility of element on web page.
	 *
	 * @param element the element
	 * @return true, if is element visible
	 */
	public static boolean isElementVisible(By element) {
		boolean result = false;
		try {
			if (DriverFactory.getDriver().findElement(element).isDisplayed()) {
				result = true;
				LoggingManager.getReportLogger().log(Status.PASS, "\"" + element.toString() + "\"" + " is visible");
				LoggingManager.getConsoleLogger().info( "\"" + element.toString() + "\"" + " is visible");
			}else {
				LoggingManager.getReportLogger().log(Status.INFO, "\"" + element.toString() + "\"" + " is not visible");
				LoggingManager.getConsoleLogger().info( "\"" + element.toString() + "\"" + " is not visible");
			}
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "Element not visible: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().info("No Such Element Found" + e);
		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "TimeOut. " + "\"" + element.toString() + "\"" + " not found. " + "\"");
			LoggingManager.getConsoleLogger().info("Timeout. Element not found." + e);
		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.INFO, "\"" + "Element not found: " + "\"" + element.toString() + "\"");
			LoggingManager.getConsoleLogger().info("No Such Element Found" + e);
		}
		return result;	
	}

	/**
	 * This Method returns the current page URL
	 * 
	 * @return current URL
	 */
	public static String getCurrentURL() {
		String URL = null;
		try {
			URL = DriverFactory.getDriver().getCurrentUrl();
			LoggingManager.getReportLogger().log(Status.PASS, "\"" + "Current URL is:  " + "\"" + URL + "\"");
			LoggingManager.getConsoleLogger().info( "\"" + "Current URL is:  " + "\"" + URL + "\"");
		} catch (NoSuchElementException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "URL not found. " + "\"");
			LoggingManager.getConsoleLogger().error("URL not found." + e);
			throw(e);

		} catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "TimeOut. URL not found. " + "\"");
			LoggingManager.getConsoleLogger().error("Timeout. Element not found." + e);
			throw(e);

		} catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "Element not found: " + "\"" + LoggingManager.getInstance().getCurrentElementPath() + "\"");
			LoggingManager.getConsoleLogger().error("No Such Element Found" + e);
			throw(e);
		}
		return URL;
	}

	/**
	 * This method waits for the visibility of web page Title
	 *
	 * @param pageTitle	pageTitle on web page.
	 * @param secs 		number of second to wait.
	 */
	public static void waitForPageTitle(String pageTitle , int secs){
		try
		{
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(secs));
			wait.until(ExpectedConditions.titleContains(pageTitle));
			LoggingManager.getReportLogger().log(Status.PASS, "page title " +pageTitle + " is verified." + "\"");
			LoggingManager.getConsoleLogger().info("page title " +pageTitle + " is verified." + "\"");
		}catch (TimeoutException e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "TimeOut. " + "page title " +pageTitle + " is not verified." + "\"");
			LoggingManager.getConsoleLogger().error("Timeout. page title " +pageTitle + " is not verified." + e);
			throw(e);
		}catch (Exception e) {
			LoggingManager.getReportLogger().log(Status.FAIL, "\"" + "page title " +pageTitle + " is not verified." + "\"");
			LoggingManager.getConsoleLogger().error("Page title " +pageTitle + " is not verified." + e);
			throw(e);
		}
	}

     
     /**
      * dismiss alert box.
      */
     public static void dismissAlert() {
 		int count=0;
 		   while(count++ < 5)
 		   {
 		        try
 		        {
 		        	DriverFactory.getDriver().switchTo().alert().dismiss();
 		            break;
 		        }
 		        catch(Exception ex)
 		        {
 		        	MKeywords.Sleep(10000);
 		           continue;
 		        }
 		   }
 	 }
     
    /**
     * @param element
     * @param input
     */
    public static void sendKeys(By element,String input) {
 		DriverFactory.getDriver().findElement(element).sendKeys(input);
	}
}
