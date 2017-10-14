package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 11.06.2010
 * Time: 14:59:55
 */
public enum InputLocators implements ILocatorable {
    PARENT_DIV_CLASS("class"),
    PARENT_DIV_CLASS_ERROR_VALUE("dijitError"),;
    String loc;

    InputLocators(String s) {
        loc = s;
    }

    public String getLocator() {
        return loc;
    }
}
