package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 16.06.2010
 * Time: 17:51:18
 */
public enum CheckBoxLocators implements ILocatorable {
    PARENT_DIV("/parent::div"),
    PARENT_DIV_CLASS("class"),
    PARENT_DIV_MIDDLE_ATTR("partlySelection"),
    PARENT_DIV_CHECKED("dijitCheckBoxChecked"),;

    private String locator;

    CheckBoxLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }
}
