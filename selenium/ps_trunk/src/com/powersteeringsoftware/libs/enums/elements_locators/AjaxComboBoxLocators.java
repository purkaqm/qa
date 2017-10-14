package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by admin on 24.10.2014.
 */
public enum AjaxComboBoxLocators implements ILocatorable {
    ARROW("./div/div"),
    LOADING("//div[contains(@class, 'psComboPreloader')]"),;

    String locator;
    String alternativeLocator;

    AjaxComboBoxLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }

}
