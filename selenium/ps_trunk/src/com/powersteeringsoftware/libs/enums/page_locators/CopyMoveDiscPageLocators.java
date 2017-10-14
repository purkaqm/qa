package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.08.11
 * Time: 17:06
 */
public enum CopyMoveDiscPageLocators implements ILocatorable {
    LOCATION_FIELD("popup_parentShow"),
    LOCATION_POPUP("//div[@id='popup_parent']"),
    MOVE_BUTTON("//input[@value='Move']"),;
    String locator;

    CopyMoveDiscPageLocators(String s) {
        locator = s;
    }

    @Override
    public String getLocator() {
        return locator;
    }
}
