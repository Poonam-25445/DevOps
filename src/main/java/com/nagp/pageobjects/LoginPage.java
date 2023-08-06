package com.nagp.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import com.nagp.core.DriverFactory;
import com.nagp.keywords.MKeywords;
import com.nagp.keywords.SeKeywords;

public class LoginPage {
	
	private static String pageName = "Login";	
	
	/**
	 * Enter a user name in text box.
	 *
	 * @param strUserName	user name value.
	 */
	public static void setUserName(String strUserName) {
		By userNameTextbox = MKeywords.findElement(pageName, "UsernameTextBox");
		SeKeywords.waitForElementVisibility(userNameTextbox, 30);
		if (SeKeywords.isIeBrowser) {
			((JavascriptExecutor) DriverFactory.getDriver()).executeScript(String.format("document.getElementById('email').value='%s';", strUserName));
		} else {
			SeKeywords.setText(userNameTextbox, strUserName);
		}
	}

	/**
	 * Enter the password in text box.
	 *
	 * @param strPassword	password value.
	 */
	public static void setPassword(String strPassword){
		if (SeKeywords.isIeBrowser) {
			((JavascriptExecutor) DriverFactory.getDriver()).executeScript(String.format("document.getElementById('pass').value='%s';", strPassword));
		} else {
		SeKeywords.setText(MKeywords.findElement(pageName,"PasswordTextBox"), strPassword);
		}
	}

	/**
	 * Click login button.
	 */
	public static void clickLogin(){
		SeKeywords.click(MKeywords.findElement(pageName, "LoginButton"));
	}
	
	/**
	 * Login to application
	 * 
	 * @param username		 username/email id
	 * @param password		 password
	 */
	public static void login(String username, String password) {
		LoginPage.clickSignInButton();
		LoginPage.setUserName(username);
		LoginPage.setPassword(password);
		LoginPage.clickLogin();
	}
	
	/**
	 * Click on SignIn button
	 */
	public static void clickSignInButton() {
		SeKeywords.click(MKeywords.findElement(pageName, "SignInButton"));
	}

	/**
	 * Get login error message
	 * 
	 * @return string
	 */
	public static String getLoginErrorMessage() {
		By errorMessage = MKeywords.findElement(pageName,"LoginError");
		SeKeywords.waitForStalenessOfElement(errorMessage, 10);
		return SeKeywords.getText(errorMessage);
	}
	
	/**
	 * Get invalid user error message
	 * 
	 * @return string*/
	public static String getInvalidUserErrorMessage() {
		By errorMessage = MKeywords.findElement(pageName,"InvalidUserError");
		SeKeywords.waitForStalenessOfElement(errorMessage, 10);
		return SeKeywords.getText(errorMessage);
	}

	/**
	 * Verify if login button is present or not
	 * 
	 * @return true/false
	 */
	public static boolean verifyLoginButton() {
		return SeKeywords.isElementPresent(MKeywords.findElement(pageName, "SignInButton"));
	}

	/**
	 * This method checks whether Forget Password button is present or not.	 
	 * 
	 * @return true, If Present
	 */
	public static boolean verifyForgotPasswordButton() {
		return SeKeywords.isElementPresent(MKeywords.findElement(pageName, "ForgetPasswordButton"));
	}

	/**
	 * Verify text message we get after login
	 * 
	 * @return string
	 */
	public static String verifyAfterLoginText() {
		SeKeywords.waitForStalenessOfElement(MKeywords.findElement(pageName, "WelcomeText"), 30);
		return SeKeywords.getText(MKeywords.findElement(pageName, "WelcomeText"));
	}

}
