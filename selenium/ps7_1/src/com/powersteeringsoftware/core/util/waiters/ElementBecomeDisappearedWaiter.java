package com.powersteeringsoftware.core.util.waiters;

import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.thoughtworks.selenium.Wait;

/**
 * Waiter for element become "not present"
 *
 *
 *
 */
public class ElementBecomeDisappearedWaiter extends AbstractElementWaiter {

	public static final String ERROR_MESSAGE_DISSAPEARED= "Element hasn't dissapeared. Element locator = ";

	public ElementBecomeDisappearedWaiter(String _locator){
		super(_locator, ERROR_MESSAGE_DISSAPEARED+_locator);
	}

	public boolean until() {
		return !SeleniumDriverSingleton.getDriver().isElementPresent(getLocator());
	}

	public static void waitElementDissapeared(String _locator){
		Wait presentWaiter = new ElementBecomeDisappearedWaiter(_locator);
		presentWaiter.wait(presentWaiter.toString(), CoreProperties.getWaitForElementToLoad());
	}
}
