package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 07.06.2010
 * Time: 17:28:51
 */
public enum StatusChooserLocators implements ILocatorable {
    POPUP_LABELS(".//li//label"),
    POPUP_INPUTS(".//li//input"),
    POPUP_ALL(DijitPopupLocators.THIS.locator + "//div[text()='All']"),
    POPUP_NONE(DijitPopupLocators.THIS.locator + "//div[text()='None']"),;

    String locator;

    StatusChooserLocators(String loc) {
        locator = loc;
    }

    public String getLocator() {
        return locator;
    }
}
