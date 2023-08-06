package com.nagp.pageobjects;

import java.util.ArrayList;
import java.util.Arrays;

import org.openqa.selenium.By;

import com.nagp.keywords.MKeywords;
import com.nagp.keywords.SeKeywords;

public class CommonPage {
	
	private static String pageName = "Common";	
	public static final String MY_ACCOUNT = "My Account";
	public static final String MY_WISH_LIST = "My Wish List";
	public static final String SIGN_OUT = "Sign Out";
	public static final ArrayList<String> LOGIN_USER_LINKS = new ArrayList<String>(
			Arrays.asList(MY_ACCOUNT, MY_WISH_LIST, SIGN_OUT));
	public static final String WELCOME_MESSAGE = "Welcome, Poonam Verma!";
	
	/**
	 * Checks whether Url starts with Https.
	 *
	 * @return true , If yes
	 */
	public static String getURL() {
		return SeKeywords.getCurrentURL();
	}
	
	/**
	 * Get the page title.
	 *
	 * @return String, if page title verified.
	 */
	public static String getPageTitle() {
		return SeKeywords.getPageTitle();
	}

	/**
	 * Verify default welcome message on home screen
	 * 
	 * @return true/false
	 */
	public static boolean verifyDefaultWelcomMessage() {
		By defaultMessage = MKeywords.findElement(pageName, "DefaultLoginMsg");
		SeKeywords.waitForElementVisibility(defaultMessage, 30);
		return SeKeywords.isElementVisible(defaultMessage);
	}

	/**
	 * Verify Welcome user drodown info options
	 * 
	 * @return true/false
	 */
	public static boolean verifyDropDownValuesInUserMenu(ArrayList<String> linkListForAccessDeniedPage) {
		By userInfo = MKeywords.findElement(pageName, "UserInfo");
		SeKeywords.waitForElementToBeClickable(userInfo, 15);
		ArrayList<String> displayedList = MKeywords.getDropdownValues(userInfo,
				MKeywords.findElement(pageName, "WelcomeList"));
		displayedList.remove(displayedList.size() - 1); // remove the copyright string
		return MKeywords.CompareTwoArrayOfStrings(linkListForAccessDeniedPage, displayedList);
	}
	
	
	/**
	 * Click welcome list.
	 */
	public static void clickUserInfo(){
		SeKeywords.click(MKeywords.findElement(pageName, "UserInfo"));
	}
	
	/**
	 * Select welcome list option from drop down list.
	 *
	 * @param value		value user wants to select.
	 */
	public static void selectWelcomeListOption(String value){
		By userInfo = MKeywords.findElement("Common", "UserInfo");
		SeKeywords.waitForStalenessOfElement(userInfo, 30);  
		MKeywords.selectDropdownValue(userInfo, MKeywords.findElement("Common", "WelcomeList"), value);
	}

}
