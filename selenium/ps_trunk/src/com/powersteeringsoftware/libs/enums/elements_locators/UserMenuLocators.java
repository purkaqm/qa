package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 11.09.12
 * Time: 22:08
 * To change this template use File | Settings | File Templates.
 */
public enum UserMenuLocators implements ILocatorable {

    USER_POPUP_MORE_LINK("//a[contains(text(),'More')]"),;

    private String locator;

    UserMenuLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }
}
