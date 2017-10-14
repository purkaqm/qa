package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 17.04.13
 * Time: 21:35
 * To change this template use File | Settings | File Templates.
 */
public enum MoveWorkPageLocators implements ILocatorable {
    LOCATION_FIELD("popup_parentShow"),
    LOCATION_POPUP("//div[@id='popup_parent']"),
    MOVE("//input[@value='Move']"),
    COPY("//input[@value='Copy']"),;
    private String loc;

    MoveWorkPageLocators(String s) {
        loc = s;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
