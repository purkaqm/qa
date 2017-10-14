package com.powersteeringsoftware.core.util.waiters;

import com.thoughtworks.selenium.Wait;;

public abstract class AbstractElementWaiter extends Wait {

	private String locator = "Locator has not set";
	private String errorMessage = "Error message has not set";

	AbstractElementWaiter(String _locator, String _errorMessage){
		locator = _locator;
		errorMessage = _errorMessage;
	}

	protected String getLocator() {
		return locator;
	}

	protected String getErrorMessage() {
		return errorMessage;
	}

	public String toString(){
		return getErrorMessage();

	}


}
