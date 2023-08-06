package com.nagp.test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.nagp.constants.FunctionalGroups;
import com.nagp.constants.Severity;
import com.nagp.core.TestBase;
import com.nagp.dataobjects.DataProviders;
import com.nagp.pageobjects.CommonPage;
import com.nagp.pageobjects.LoginPage;
import com.nagp.pageobjects.NewCustomerAccountPage;

/**
 * @author poonam
 * This class will verify Create new customer account page
 */
public class Test_VerifyNewCustomerFunctionality extends TestBase {
	
	
	/**
	 * This test case will verify Create new customer account page
	 * 1) check default welcome message
	 * 2) check create button visibility
	 * 3) checking error messages on customer account form
	 * 
	 * @param createAccountTitle		screen title
	 * @param firstNameField			First name field
	 * @param lastNameField				Last name field
	 * @param emailField				email field
	 * @param passwordField				password field
	 * @param confirmationField			confirmation field
	 * 
	 */
	@Test(dataProvider = "TestData", dataProviderClass = DataProviders.class, groups = { Severity.CRITICAL, FunctionalGroups.CUSTOMER_ACCOUNT })
	public void testNewCustomerAccount(String createAccountTitle, String firstNameField, String lastNameField, String emailField, String passwordField, String confirmationField) {

		SoftAssert softAssert = new SoftAssert();
		LoginPage.clickSignInButton();
		softAssert.assertTrue(CommonPage.verifyDefaultWelcomMessage(), "Default welcome message is not present");
		softAssert.assertTrue(NewCustomerAccountPage.verifyCreateAccountButtonVisibility(), "Create account button is not visible");
		NewCustomerAccountPage.clickCreateAccount();
		softAssert.assertTrue(CommonPage.getPageTitle().equalsIgnoreCase(createAccountTitle), "Create new customer form title is not visible");
		NewCustomerAccountPage.submitCreateAccountForm();
		softAssert.assertTrue(NewCustomerAccountPage.isErrorMessagePresent(firstNameField), "first name field error message is not present");
		softAssert.assertTrue(NewCustomerAccountPage.isErrorMessagePresent(lastNameField), "last name field error message is not present");
		softAssert.assertTrue(NewCustomerAccountPage.isErrorMessagePresent(emailField), "email field error message is not present");
		softAssert.assertTrue(NewCustomerAccountPage.isErrorMessagePresent(passwordField), "password field error message is not present");
		softAssert.assertTrue(NewCustomerAccountPage.isErrorMessagePresent(confirmationField), "password confirmation field error message is not present");
		softAssert.assertAll();
	}

}
