package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 09.07.2010
 * Time: 13:43:59
 */
public enum FlatTagChooserLocators implements ILocatorable {
    POPUP_LOADING("//div[contains(@class, 'waitingUnderlay')]"),
    ITEM("//div[@class='dijitTreeComboNode']/div"),
    ITEM_LINK("//div/a"),
    INPUT("//input"),;
    String locator;

    FlatTagChooserLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }
}
