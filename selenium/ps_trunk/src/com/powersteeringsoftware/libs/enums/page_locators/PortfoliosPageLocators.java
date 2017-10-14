package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 17.01.14
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */
public enum PortfoliosPageLocators implements ILocatorable {
    NEW("//input[@value='Add Portfolio']"),;
    private String loc;

    PortfoliosPageLocators(String l) {
        loc = l;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
