package com.nagp.pageobjects;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.nagp.core.DriverFactory;
import com.nagp.keywords.MKeywords;
import com.nagp.keywords.SeKeywords;

public class AddressPage {

	private static String pageName = "Address";

	/**
	 * Verify the visibility of Manage address button
	 * 
	 * @return true/false
	 */
	public static boolean verifyManageAddressVisibility() {
		return SeKeywords.isElementVisible(MKeywords.findElement(pageName, "ManageAddresses"));
	}

	/**
	 * Click on manage address button
	 */
	public static void clickManageAddress() {
		SeKeywords.click(MKeywords.findElement(pageName, "ManageAddresses"));
	}

	/**
	 * Get the page title
	 * 
	 * @return string
	 */
	public static String getPageTitle() {
		SeKeywords.waitForStalenessOfElement(MKeywords.findElement(pageName, "PageTitle"), 20);
		return SeKeywords.getText(MKeywords.findElement(pageName, "PageTitle"));
	}

	/**
	 * Click on Add new address button
	 */
	public static void clickAddNewAddress() {
		SeKeywords.click(MKeywords.findElement(pageName, "AddNewAddress"));
	}

	/**
	 * Add new address with corresponding field values
	 * 
	 * @param firstName First name field
	 * @param lastName  Last name field
	 * @param telephone Telephone field
	 * @param street    Street field
	 * @param city      City field
	 * @param state     State field
	 * @param zipCode   Zip code field
	 * @param country   Country field
	 */
	public static void addNewAddress(String firstName, String lastName, String telephone, String street, String city,
			String state, String zipCode, String country) {
		SeKeywords.waitForElementVisibility(MKeywords.findElement(pageName, "FirstName"), 20);
		if (SeKeywords.isIeBrowser) {
			((JavascriptExecutor) DriverFactory.getDriver())
					.executeScript(String.format("document.getElementById('firstname').value='%s';", firstName));
			((JavascriptExecutor) DriverFactory.getDriver())
					.executeScript(String.format("document.getElementById('lastname').value='%s';", lastName));
			((JavascriptExecutor) DriverFactory.getDriver())
					.executeScript(String.format("document.getElementById('telephone').value='%s';", telephone));
			((JavascriptExecutor) DriverFactory.getDriver())
					.executeScript(String.format("document.getElementById('street_1').value='%s';", street));
			((JavascriptExecutor) DriverFactory.getDriver())
					.executeScript(String.format("document.getElementById('city').value='%s';", city));
			((JavascriptExecutor) DriverFactory.getDriver())
					.executeScript(String.format("document.getElementById('zip').value='%s';", zipCode));
		} else {
			SeKeywords.setText(MKeywords.findElement(pageName, "FirstName"), firstName);
			SeKeywords.setText(MKeywords.findElement(pageName, "LastName"), lastName);
			SeKeywords.setText(MKeywords.findElement(pageName, "Telephone"), telephone);
			SeKeywords.setText(MKeywords.findElement(pageName, "Street"), street);
			SeKeywords.setText(MKeywords.findElement(pageName, "City"), city);
			SeKeywords.setText(MKeywords.findElement(pageName, "ZipCode"), zipCode);
		}
		SeKeywords.selectDropdownValue(MKeywords.findElement(pageName, "State"), state);
		SeKeywords.selectDropdownValue(MKeywords.findElement(pageName, "Country"), country);
		SeKeywords.click(MKeywords.findElement(pageName, "SaveAddress"));
		SeKeywords.dismissAlert();
	}

	/**
	 * verify if address table is present on screen or not
	 * 
	 * @return true/false
	 */
	public static boolean verifyAddressTableIsPresent() {
		return SeKeywords.isElementVisible(MKeywords.findElement(pageName, "AddressTable"));
	}

	/**
	 * Get address table list size
	 * 
	 * @return integer size of the table
	 */
	public static int getAddressTableRowCount() {
		List<WebElement> count = SeKeywords.findAllElements(MKeywords.findElement(pageName, "AddressTableRows"));
		return count.size();
	}

	/**
	 * Get address updated message
	 * 
	 * @return string text
	 */
	public static String getAddressMessages() {
		return SeKeywords.getText(MKeywords.findElement(pageName, "SavedAddressMessage"));
	}

	/**
	 * Clickon Edit address button
	 */
	public static void editAddress() {
		SeKeywords.click(MKeywords.findElement(pageName, "EditAddress"));

	}

	/**
	 * Click delete address button
	 */
	public static void deleteAddress() {
		By element = MKeywords.findElement(pageName, "DeleteAddress");
		SeKeywords.waitForElementVisibility(element, 20);
		SeKeywords.moveToElement(element);
		SeKeywords.click(element);

	}

	/**
	 * Edit telephone field
	 */
	public static void editTelephone(String telephone) {
		SeKeywords.setText(MKeywords.findElement(pageName, "Telephone"), telephone);
	}

	/**
	 * Click on save address
	 */
	public static void saveAddress() {
		SeKeywords.click(MKeywords.findElement(pageName, "SaveAddress"));
	}

	/**
	 * Get telephone field message
	 * 
	 * @return string text
	 */
	public static String getTelephoneValue() {
		return SeKeywords.getText(MKeywords.findElement(pageName, "TelephoneColumn"));
	}

	/**
	 * Get popup message on deleting address
	 * 
	 * @return string text
	 */
	public static String getPopUpMessage() {
		SeKeywords.waitForStalenessOfElement(MKeywords.findElement(pageName, "ModalPopup"), 20);
		return SeKeywords.getText(MKeywords.findElement(pageName, "ModalPopup"));
	}

	/**
	 * Click on popu modal
	 */
	public static void clickPopMessage(String buttonName) {
		By modalButton = MKeywords.findElement(pageName, "ModelPopupFooter");
		SeKeywords.waitForElementToBeClickable(modalButton, 2);
		SeKeywords.clickModalPopButton(modalButton, buttonName);
	}
}
