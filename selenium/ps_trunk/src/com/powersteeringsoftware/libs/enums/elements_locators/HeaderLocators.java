package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.11.2010
 * Time: 17:42:40
 */
public enum HeaderLocators implements ILocatorable {
    ID("header"),
    THIS("css=div#" + ID.getLocator()),
    HEADER_110("css=div." + ID.getLocator());
    String locator;

    HeaderLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }
}
