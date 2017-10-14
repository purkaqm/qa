package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.08.2010
 * Time: 18:07:27
 */
public enum SingleStatusSelectorLocators implements ILocatorable {
    ITEM(".//td[contains(@class, 'dijitMenuItemLabel')]"),;
    private String locator;

    SingleStatusSelectorLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }
}
