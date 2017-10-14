package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 15.06.2010
 * Time: 18:34:33
 */
public enum NumberSpinnerLocator implements ILocatorable {
    ARROW_DOWN("//div[contains(@class, 'dijitDownArrowButton')]"),
    ARROW_UP("//div[contains(@class, 'dijitUpArrowButton')]"),
    INPUT("//input[@type='text']"),
    INPUT_REGEXP("[+-]?\\d*\\.?\\d{1,}([eE][-+]?\\d+)?"),
    INPUT_MAX("8999999999999999"),
    INPUT_MIN("-8999999999999999"),;
    String loc;

    NumberSpinnerLocator(String loc) {
        this.loc = loc;
    }

    public String getLocator() {
        return loc;
    }
}
