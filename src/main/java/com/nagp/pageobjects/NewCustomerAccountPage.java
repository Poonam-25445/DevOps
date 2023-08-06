package com.nagp.pageobjects;

import org.openqa.selenium.By;

import com.nagp.keywords.MKeywords;
import com.nagp.keywords.SeKeywords;

public class NewCustomerAccountPage {
	
	private static String pageName = "NewCustomerAccount";
	public static final String CREATE_NEW_CUSTOMER_TITLE = "Create New Customer Account";

	/**
	 * Check create account visibility
	 * 
	 * @return true/false
	 */
	public static boolean verifyCreateAccountButtonVisibility() {
		By createAccountButton = MKeywords.findElement(pageName, "CreateAccountButton");
		SeKeywords.waitForElementVisibility(createAccountButton, 20);
		return SeKeywords.isElementVisible(createAccountButton);
	}	
	
	/**
	 * Click create account button.
	 */
	public static void clickCreateAccount(){
		By createAccountButton = MKeywords.findElement(pageName, "CreateAccountButton");
		SeKeywords.waitForElementToBeClickable(createAccountButton, 20);
		SeKeywords.click(createAccountButton);
		SeKeywords.waitForPageTitle(CREATE_NEW_CUSTOMER_TITLE, 10);
	}

	/**
	 * Click on submit button for create account form
	 */
	public static void submitCreateAccountForm() {
		SeKeywords.click(MKeywords.findElement(pageName, "SubmitCreateAccountForm"));
	}
	
	/**
	 * This method get error messages for different create account form's fields
	 * 
	 * @param fieldName		Field name
	 * @return true/false
	 */
	public static boolean isErrorMessagePresent(String fieldName) {
		boolean isPresent ;
		switch(fieldName) {
		case "FirstName":
			isPresent = SeKeywords.isElementVisible(MKeywords.findElement(pageName, "FirstNameError"));
			break;
		case "LastName":
			isPresent = SeKeywords.isElementVisible(MKeywords.findElement(pageName, "LastNameError"));
			break;
		case "Email":
			isPresent = SeKeywords.isElementVisible(MKeywords.findElement(pageName, "EmailError"));
			break;
		case "Password":
			isPresent = SeKeywords.isElementVisible(MKeywords.findElement(pageName, "PasswordError"));
			break;
		case "Password-confirmation":
			isPresent = SeKeywords.isElementVisible(MKeywords.findElement(pageName, "PasswordConfirmationError"));
			break;
		default:
			isPresent = false;
		}
		return isPresent;
	}
	
	

}
