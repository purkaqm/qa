package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 21.01.14
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
public enum PortfolioDetailsPageLocators implements ILocatorable {
    URL("/person/PortfolioDetails.epage"),
    EDIT("link=Edit");
    private String loc;

    PortfolioDetailsPageLocators(String l) {
        loc = l;
    }

    @Override
    public String getLocator() {
        return loc;
    }

}
