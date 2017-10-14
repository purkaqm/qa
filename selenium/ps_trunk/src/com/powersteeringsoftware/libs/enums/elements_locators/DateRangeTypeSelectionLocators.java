package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.08.2010
 * Time: 15:27:33
 */
public enum DateRangeTypeSelectionLocators implements ILocatorable {
    START_DATE("//table[@class='tableClass']//tr[1]//div"),
    END_DATE("//table[@class='tableClass']//tr[2]//div"),;
    private String locator;

    DateRangeTypeSelectionLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }
}
