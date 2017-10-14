package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 03.08.11
 * Time: 15:13
 */
public enum HomePageLocators implements ILocatorable {
    URL("Home.page"),;
    String locator;

    HomePageLocators(String locator) {
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }

}
