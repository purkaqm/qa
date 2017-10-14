package com.powersteeringsoftware.core.util.waiters;

import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.thoughtworks.selenium.Wait;

public class ElementBecomeVisibleWaiter extends AbstractElementWaiter{

	public static final String ERROR_MESSAGE_VISIBLE = "Element hasn't become visible. Element locator = ";

	public ElementBecomeVisibleWaiter(String _locator){
		super(_locator, ERROR_MESSAGE_VISIBLE+_locator);
	}

	public boolean until() {
		return SeleniumDriverSingleton.getDriver().isVisible(getLocator());
	}

	public static void waitElementBecomeVisible(String _locator){
		Wait presentWaiter = new ElementBecomeVisibleWaiter(_locator);
		presentWaiter.wait(presentWaiter.toString(), CoreProperties.getWaitForElementToLoad());
	}

}
