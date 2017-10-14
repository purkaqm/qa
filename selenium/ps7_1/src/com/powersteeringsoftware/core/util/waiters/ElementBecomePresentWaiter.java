package com.powersteeringsoftware.core.util.waiters;

import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.thoughtworks.selenium.Wait;

public class ElementBecomePresentWaiter extends AbstractElementWaiter {


	public static final String ERROR_MESSAGE_PRESENT = "Element hasn't become present. Element locator = ";

	public ElementBecomePresentWaiter(String _locator){
		super(_locator, ERROR_MESSAGE_PRESENT+_locator);
	}

	public boolean until() {
		return SeleniumDriverSingleton.getDriver().isElementPresent(getLocator());
	}

	public static void waitElementBecomePresent(String _locator){
		Wait presentWaiter = new ElementBecomePresentWaiter(_locator);
		presentWaiter.wait(presentWaiter.toString(), CoreProperties.getWaitForElementToLoad());
	}
}
