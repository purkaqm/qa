package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

public enum AddNewReportPageLocator implements ILocatorable{

	REPORT_WIZARD_START_LINK("link=Start now");
	
	
	AddNewReportPageLocator(String locator){
		this.locator = locator;
	}
	
	@Override
	public String getLocator() {
		return locator;
	}

	
	private String locator;

}
