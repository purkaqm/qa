package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.06.2010
 * Time: 13:38:28
 */
public enum ToggleButtonLocators implements ILocatorable {
    PARENT("/parent::*/parent::*/parent::span"),
    PARENT_CLASS("class"),
    PARENT_VALUE("dijitChecked"),;
    String locator;

    ToggleButtonLocators(String loc) {
        locator = loc;
    }

    @Override
    public String getLocator() {
        return locator;
    }
}
