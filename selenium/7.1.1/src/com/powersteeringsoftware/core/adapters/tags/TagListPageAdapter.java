package com.powersteeringsoftware.core.adapters.tags;

import com.powersteeringsoftware.core.adapters.tags.TagEditWindowAdapter.TagEditWindowLocators;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.ILocatorable;
import com.powersteeringsoftware.core.util.waiters.ElementBecomeVisibleWaiter;

public class TagListPageAdapter {

	/**
	 * Page locators for using Selenium commands
	 */
	enum TagListPageLocators implements ILocatorable{
		BUTTON_ADD_NEW_TAG("dom=window.dojo.byId('AddTagSetShow')");

		String locator;

		TagListPageLocators(String _locator){
			locator = _locator;
		}

		public String getLocator() {
			return locator;
		}
	}


	public static void pushButtonAddNewTag() {
		SeleniumDriverSingleton.getDriver().click(TagListPageLocators.BUTTON_ADD_NEW_TAG.getLocator());
		ElementBecomeVisibleWaiter.waitElementBecomeVisible(TagEditWindowLocators.DIV_ID.getLocator());
	}

	public static void clickTagName(String name){
		SeleniumDriverSingleton.getDriver().click("link="+name);
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}
}
