package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 13.12.12
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 */
public enum LoadAlternativeCalendarsPageLocators implements ILocatorable {
    URL("admin/load_alternative_calendars.jsp"),
    INPUT("name=xml-file"),
    SUBMIT("name=action");

    String locator;

    LoadAlternativeCalendarsPageLocators(String loc) {
        locator = loc;
    }

    public String getLocator() {
        return locator;
    }

}
