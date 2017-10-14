package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.06.2010
 * Time: 15:31:13
 */
public enum DisplayTextBoxLocators implements ILocatorable {
    ICON("/parent::div/following-sibling::div/img"),
    ICON2("/parent::div/parent::div//img");
    private String locator;

    DisplayTextBoxLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }
}
