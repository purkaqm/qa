package com.powersteeringsoftware.core.adapters.tags;

import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.ILocatorable;

public class TagDetailsPageAdapter {

	enum TagDetailsPage implements ILocatorable{
		BUTTON_ADD_UPDATE("dom=window.dojo.query('form#tagValuesForm a')[0]"),
		LINK_EDIT("link=Edit");

		String locator;

		TagDetailsPage(String _locator){
			locator = _locator;
		}

		public String getLocator(){
			return locator;
		}
	}

	public static void pushButtonAddUpdate() {
		SeleniumDriverSingleton.getDriver().click(TagDetailsPage.BUTTON_ADD_UPDATE.getLocator());
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}


	public static void clickEdit(){
		SeleniumDriverSingleton.getDriver().click(TagDetailsPage.LINK_EDIT.getLocator());
	}

}
