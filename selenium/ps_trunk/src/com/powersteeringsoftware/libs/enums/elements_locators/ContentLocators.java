package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.11.2010
 * Time: 18:42:24
 */
public enum ContentLocators implements ILocatorable {
    ID("content"),
    THIS("css=div#" + ID.getLocator()),;

    String locator;

    ContentLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }
}
