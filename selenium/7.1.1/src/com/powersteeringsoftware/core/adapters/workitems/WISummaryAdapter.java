package com.powersteeringsoftware.core.adapters.workitems;

import org.testng.Assert;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.ILocatorable;

public class WISummaryAdapter extends AbstractWorkItem {

	enum ElementLocators implements ILocatorable {
		LINK_ADD_REMOVE("link=Add / Remove");

		String locator;

		ElementLocators(String _locator){
			locator = _locator;
		}

		public String getLocator() {
			return locator;
		}
	}

	/**
	 * Assert if loaded page 'Work Item:Summary'
	 *
	 */
	public void assertIsLoadedPageSummary() {
		Assert.assertEquals(isLoadedPageWISummary(), true,
				"You are not on the page 'Work Item:Summary'");
	}



	public WIDeliverablesAdapter clickAddRemove(){
		SeleniumDriverSingleton.getDriver().click(ElementLocators.LINK_ADD_REMOVE.getLocator());
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
		return new WIDeliverablesAdapter(SeleniumDriverSingleton.getDriver());
	}

	public boolean isDeliverableExist(String deliverableName){
		Long index = new Long(SeleniumDriverSingleton.getDriver().getEval(""
				+ "var str = window.dojo.byId('GatesAndDelivsTable').innerHTML; "
				+ "str.indexOf('"+deliverableName+"');"));

		if(index == -1) return false;

		return true;
	}
}
