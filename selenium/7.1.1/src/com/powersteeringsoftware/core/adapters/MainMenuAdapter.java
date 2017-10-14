package com.powersteeringsoftware.core.adapters;

import com.powersteeringsoftware.core.enums.MainMenuLocators;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.waiters.ElementBecomePresentWaiter;
import com.powersteeringsoftware.core.util.waiters.TimerWaiter;

public class MainMenuAdapter {

	private MainMenuAdapter(){
	}

	public static void clickMainMenuLocator(MainMenuLocators _locator){
		ElementBecomePresentWaiter.waitElementBecomePresent(_locator.getLocator());
		SeleniumDriverSingleton.getDriver().click(_locator.getLocator());
	}

	/**
	 * Click main menu item "Browse"
	 */
	public static void clickBrowse(){
		MainMenuAdapter.clickMainMenuLocator(MainMenuLocators.BROWSE);
	}


	public static void clickAdmin(){
		MainMenuAdapter.clickMainMenuLocator(MainMenuLocators.ADMIN);
	}


	/**
	 * Click main menu item "Browse>>New work"
	 */
	public static void clickBrowseNewWork(){
		clickBrowse();
		MainMenuAdapter.clickMainMenuLocator(MainMenuLocators.BROWSE_NEW_WORK);
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	/**
	 * Click main menu item Admin>>Templates:Work
	 */
	public static void clickAdminTemplatesWork(){
		clickAdmin();
		MainMenuAdapter.clickMainMenuLocator(MainMenuLocators.ADMIN_TEMPLATES_WORK);
		SeleniumDriverSingleton.getDriver().waitForFrameToLoad("jspFrame", CoreProperties.getWaitForElementToLoadAsString());
	}

	/**
	 * Click main menu item Browse>>Work Tree
	 */
	public static void clickBrowseWorkTree(){
		clickBrowse();
		clickMainMenuLocator(MainMenuLocators.BROWSE_WORK_TREE);
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	/**
	 * Click main menu item Admin>>Configuration: Tags
	 */
	public static void clickAdminConfigurationsTags(){
		clickAdmin();
		clickMainMenuLocator(MainMenuLocators.ADMIN_CONFIGURATION_TAGS);
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public static void clickAdminCustomFields(){
		clickAdmin();
		clickMainMenuLocator(MainMenuLocators.ADMIN_CUSTOM_FIELDS);
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public static void clickAdminProcesses(){
		clickAdmin();
		clickMainMenuLocator(MainMenuLocators.ADMIN_PROCESSES);
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public static void clickBrowseTimesheet() {
		clickBrowse();
		clickMainMenuLocator(MainMenuLocators.BROWSE_TIMESHEETS);
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
		new TimerWaiter(1000).waitTime();
	}

	public static void clickBrowseManageTime(){
		clickBrowse();
		clickMainMenuLocator(MainMenuLocators.BROWSE_MANAGE_TIMESHEET);
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
		new TimerWaiter(1000).waitTime();
	}

	public static void clickBrowseMeasureLibrary() {
	clickBrowse();
	clickMainMenuLocator(MainMenuLocators.BROWSE_MEASURES_LIBRARY);
	SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	new TimerWaiter(1000).waitTime();
}

}

