package com.powersteeringsoftware.core.managers.dojo;

import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.ILocatorable;
import com.powersteeringsoftware.core.util.waiters.ElementBecomeUnvisibleWaiter;
import com.powersteeringsoftware.core.util.waiters.ElementBecomeVisibleWaiter;
import com.powersteeringsoftware.core.util.waiters.TimerWaiter;


public class WorkItemComponentAdapter {

	public enum WorkItemComponentAdapterLocators implements ILocatorable{

		WORKITEM_DIV("shared_popup_id"),
		TAB_FAVORITES("dijit_layout__TabButton_0"),
		TAB_BROWSE("dijit_layout__TabButton_1"),
		TAB_SEARCH("dijit_layout__TabButton_2");


		String locator;
		WorkItemComponentAdapterLocators(String _locator){
			locator = _locator;
		}

		public String getLocator() {
			return locator;
		}
	}

	public WorkItemComponentAdapter(){
	}

	public void clickTabFavorites(){
		SeleniumDriverSingleton.getDriver().click(WorkItemComponentAdapterLocators.TAB_FAVORITES.getLocator());
		ElementBecomeVisibleWaiter.waitElementBecomeVisible(WorkItemComponentAdapterLocators.TAB_FAVORITES.getLocator());
	}

	public void clickTabBrowse(){
		SeleniumDriverSingleton.getDriver().click(WorkItemComponentAdapterLocators.TAB_BROWSE.getLocator());
		ElementBecomeVisibleWaiter.waitElementBecomeVisible(WorkItemComponentAdapterLocators.TAB_BROWSE.getLocator());
	}

	public void clickSearch(){
		SeleniumDriverSingleton.getDriver().click(WorkItemComponentAdapterLocators.TAB_SEARCH.getLocator());
		ElementBecomeVisibleWaiter.waitElementBecomeVisible(WorkItemComponentAdapterLocators.TAB_SEARCH.getLocator());
	}

	public void clickFavoritesWorkItem(String _locator){
		clickTabFavorites();
		SeleniumDriverSingleton.getDriver().click(_locator);
		ElementBecomeUnvisibleWaiter.waitElementBecomeUnvisible(_locator);
		new TimerWaiter(3000).waitTime();
	}

}
