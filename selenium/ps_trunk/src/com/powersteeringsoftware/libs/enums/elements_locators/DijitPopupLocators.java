package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 04.10.11
 * Time: 18:19
 */
public enum DijitPopupLocators implements ILocatorable {
    NODE("div"),
    CLASS("dijitPopup"),
    DISABLED("dijitDisabled"),
    THIS("//" + NODE.locator + "[@class='" + CLASS.locator + "']"),;

    DijitPopupLocators(String s) {
        locator = s;
    }

    String locator;

    @Override
    public String getLocator() {
        return locator;
    }
}
