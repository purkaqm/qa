package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 25.05.12
 * Time: 19:24
 * To change this template use File | Settings | File Templates.
 */
public enum LinksMenuLocators implements ILocatorable {
    LINK("//a"),
    INDENT("indent"),
    PARENT("//li[@class='header-txt']"),;
    private String loc;

    LinksMenuLocators(String l) {
        loc = l;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
