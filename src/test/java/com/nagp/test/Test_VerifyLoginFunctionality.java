package com.nagp.test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.nagp.constants.FunctionalGroups;
import com.nagp.constants.Severity;
import com.nagp.core.TestBase;
import com.nagp.dataobjects.DataProviders;
import com.nagp.pageobjects.CommonPage;
import com.nagp.pageobjects.LoginPage;

/**
 * @author poonam
 * This class will verify login functionality
 * 
 */
public class Test_VerifyLoginFunctionality extends TestBase {
	
	/**
	 * This test case verify login functionality with valid user name and password
	 * 
	 * @param username				Username or email id
	 * @param password				User password
	 * @param loginPageTitle		Page title
	 */

	@Test(dataProvider = "TestData", dataProviderClass = DataProviders.class, groups = { Severity.SMOKE, FunctionalGroups.LOGIN })
	public void testSuccessfulLogin(String username, String password, String loginPageTitle) {

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(CommonPage.getPageTitle(), loginPageTitle, "Login Page title is not as expected");
		softAssert.assertTrue(CommonPage.getURL().startsWith("https"), "Https Url not present");
		LoginPage.clickSignInButton();

		softAssert.assertTrue(LoginPage.verifyLoginButton(), "LoginButton not Present");
		softAssert.assertTrue(LoginPage.verifyForgotPasswordButton(), "Forgot Password button not Present");

		LoginPage.setUserName(username);
		LoginPage.setPassword(password);
		LoginPage.clickLogin();
		softAssert.assertEquals(LoginPage.verifyAfterLoginText(), CommonPage.WELCOME_MESSAGE,"Account not able to login properly");
		softAssert.assertAll();
	}
}
