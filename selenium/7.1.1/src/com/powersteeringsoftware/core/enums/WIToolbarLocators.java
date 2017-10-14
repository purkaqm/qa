package com.powersteeringsoftware.core.enums;

import com.powersteeringsoftware.core.util.ILocatorable;

public enum WIToolbarLocators implements ILocatorable {

	ISSUES("dom=window.dojo.query('[href*=Issues.epage]')[0]"),
	MEASURES("dom= window.dojo.byId('popMeasures')");

	String locator;

	WIToolbarLocators(String _locator) {
		locator = _locator;
	}

	public String getLocator() {
		return locator;
	}

}
