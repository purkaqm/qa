package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 31.05.2010
 * Time: 13:59:18
 */
public enum ToolTipLocators implements ILocatorable {
    DIV("//div[@class='dijitTooltip']"),;
    String locator;

    ToolTipLocators(String loc) {
        locator = loc;
    }

    public String getLocator() {
        return locator;
    }
}
