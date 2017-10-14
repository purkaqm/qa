package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 28.03.13
 * Time: 0:43
 * To change this template use File | Settings | File Templates.
 */
public enum ObjectTypesPageLocators implements ILocatorable {
    LINK("//a[@id='editWorkType' and text()='" + LOCATOR_REPLACE_PATTERN + "']"),;
    private String locator;

    ObjectTypesPageLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(String toRep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, toRep);
    }
}
