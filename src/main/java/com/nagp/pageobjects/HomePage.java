package com.nagp.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;

import com.nagp.core.DriverFactory;
import com.nagp.keywords.MKeywords;
import com.nagp.keywords.SeKeywords;

public class HomePage {
	
	private static String pageName = "Home";

	/**
	 * Get text from home screen
	 * 
	 * @return string
	 */
	public static String getTextFromHomePage() {
		return SeKeywords.getText(MKeywords.findElement(pageName, "HomePageText"));
	}

	/**
	 * Search item from home page
	 * 
	 * @param input		product to be searched
	 */
	public static void searchItem(String input) {
		By element = MKeywords.findElement(pageName, "SearchTextBox");
		By elementlist = MKeywords.findElement(pageName, "SearchList");
		SeKeywords.waitForElementVisibility(element, 10);
		if (SeKeywords.isIeBrowser) {
			((JavascriptExecutor) DriverFactory.getDriver()).executeScript(String.format("document.getElementById('search').value='%s';", input));
			DriverFactory.getDriver().findElement(element).sendKeys(Keys.SPACE);
		} else {
			SeKeywords.searchAndSelect(element, elementlist, input);
		}
	}

	/**
	 * Click on search button
	 */
	public static void clickSearchButton() {
		SeKeywords.click(MKeywords.findElement(pageName, "SearchButton"));
	}

	/**
	 * Check if search result is matched with the product or not
	 * 
	 * @return true/false
	 */
	public static boolean verifySearchItem(String productName) {
		return MKeywords.verifySearchedProduct(MKeywords.findElement(pageName, "SearchedItemGrid"), productName);
	}

	/**
	 * Check if search button is enable or not
	 * 
	 * @return true/false
	 */
	public static boolean checkSearchButtonEnable() {
		By element = MKeywords.findElement(pageName, "SearchButton");
		SeKeywords.waitForStalenessOfElement(element, 10);
		return SeKeywords.isElementEnabled(element);
	}

	/**
	 * Click on cart button
	 */
	public static void clickCart() {
		By element = MKeywords.findElement(pageName, "ShowCart");
		SeKeywords.waitForElementToBeClickable(element, 20);
		SeKeywords.click(element);
	}

	/**
	 * Check if cart popup is visible or not
	 * 
	 * @return true/fasle
	 */
	public static boolean isCartPopupVisible() {
		By element = MKeywords.findElement(pageName, "CartPopup");
		SeKeywords.waitForStalenessOfElement(element, 10);
		return SeKeywords.isElementVisible(element);
	}

	/**
	 * Get cart message
	 * 
	 * @return string
	 */
	public static String getCartMessage() {
		return SeKeywords.getText(MKeywords.findElement(pageName, "CartInfo"));
	}

	/**
	 * Close popup modal on cart
	 */
	public static void closeCartPopup() {
		SeKeywords.click(MKeywords.findElement(pageName, "CartClose"));
		
	}
	
	

}
