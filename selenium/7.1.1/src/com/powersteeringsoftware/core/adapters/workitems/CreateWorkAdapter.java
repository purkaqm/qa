package com.powersteeringsoftware.core.adapters.workitems;

import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.ILocatorable;
import com.thoughtworks.selenium.DefaultSelenium;

public class CreateWorkAdapter extends AbstractWorkItem{
	DefaultSelenium driver;

	enum CreateWorkLocators implements ILocatorable{
		INPUT_NAME_OF_WORK_ID("name"),
		BUTTON_FINISH_ID("finish");

		String locator;

		CreateWorkLocators(String _locator){
			locator = _locator;
		}

		public String getLocator() {
			return locator;
		}
	}

	public CreateWorkAdapter(DefaultSelenium _driver){
		driver = _driver;
	}


	public void clickFinishAndSave(){
		driver.click(CreateWorkLocators.BUTTON_FINISH_ID.getLocator());
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public void typeWorkName(String workName){
		driver.type(CreateWorkLocators.INPUT_NAME_OF_WORK_ID.getLocator(), workName);
	}
}
