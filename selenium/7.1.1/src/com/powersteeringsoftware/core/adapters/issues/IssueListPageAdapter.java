package com.powersteeringsoftware.core.adapters.issues;

import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.ILocatorable;

public class IssueListPageAdapter {

	enum IssueListPageLocators implements ILocatorable{
		BUTTON_NEW_ISSUE("dom=window.dojo.query('[value*=New Issue]')[0];");

		String locator;

		IssueListPageLocators(String _locator){
			locator = _locator;
		}
		public String getLocator() {
			return locator;
		}
	}

	public static void pushNewIssue() {
		SeleniumDriverSingleton.getDriver().click(IssueListPageLocators.BUTTON_NEW_ISSUE.getLocator());
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}



}
