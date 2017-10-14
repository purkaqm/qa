package com.powersteeringsoftware.core.util.waiters;

import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.thoughtworks.selenium.Wait;

public class ElementBecomeUnvisibleWaiter extends AbstractElementWaiter {
	public static final String ERROR_MESSAGE_BECOME_UNVISIBLE= "Element hasn't set unvisible. Element locator = ";

	public ElementBecomeUnvisibleWaiter(String _locator){
		super(_locator, ERROR_MESSAGE_BECOME_UNVISIBLE+_locator);
	}

	public boolean until() {
		return !SeleniumDriverSingleton.getDriver().isVisible(getLocator());
	}

	public static void waitElementBecomeUnvisible(String _locator){
		Wait presentWaiter = new ElementBecomeUnvisibleWaiter(_locator);
		presentWaiter.wait(presentWaiter.toString(), CoreProperties.getWaitForElementToLoad());
	}

}
