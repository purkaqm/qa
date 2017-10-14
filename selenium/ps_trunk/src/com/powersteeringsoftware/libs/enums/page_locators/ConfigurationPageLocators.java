package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by admin on 14.04.2014.
 */
public enum ConfigurationPageLocators implements ILocatorable {
    ENABLE("id=enableCosts"),
    SUBMIT("//input[@value='Save']");

    ConfigurationPageLocators(String l) {
        loc = l;
    }

    String loc;

    @Override
    public String getLocator() {
        return loc;
    }
}
