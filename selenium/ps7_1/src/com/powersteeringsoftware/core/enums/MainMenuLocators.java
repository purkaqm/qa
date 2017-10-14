package com.powersteeringsoftware.core.enums;

import com.powersteeringsoftware.core.util.ILocatorable;

/**
 * Locators for using in Selenium
 * @author selyaev_ag
 *
 */
public enum MainMenuLocators implements ILocatorable{
	/**
	 * Main menu item locator "Browse"
	 */
	BROWSE("dom=window.dojo.byId('BrowseShow');"),

	BROWSE_NEW_WORK("dom=window.dojo.query('div#BrowseMenu a[href*=createNewWorkFormInBrowseMenu]')[0]"),

	BROWSE_WORK_TREE("dom=window.dojo.query('div#BrowseMenu a[href*=WorkTree.page]')[0]"),

	BROWSE_TIMESHEETS("dom=window.dojo.query('div#BrowseMenu [href*=Timesheets]')[0]"),

	BROWSE_MANAGE_TIMESHEET("link=Manage Time"),
	
	BROWSE_MEASURES_LIBRARY("link=Measures Library"),
	/**
	 * Main menu item locators for "Admin" menu item
	 */
	ADMIN("AdminShow"),

	ADMIN_TEMPLATES_WORK("dom=window.dojo.query('div#AdminMenu a[href*=Work]')[0]"),

	ADMIN_CONFIGURATION_TAGS("dom=window.dojo.query('div#AdminMenu a[href*=TagSets]')[0]"),

	ADMIN_CUSTOM_FIELDS("dom=window.dojo.query('div#AdminMenu a[href*=CustomFields]')[0]"),

	ADMIN_PROCESSES("dom=window.dojo.query('div#AdminMenu a[href*=Processes]')[0]");



	String locator;

	MainMenuLocators(String _locator){
		locator = _locator;
	}

	public String getLocator(){
		return locator;
	}
}
