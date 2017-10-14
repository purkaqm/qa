package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 29.06.2010
 * Time: 12:31:34
 */
public enum UsersDialogLocators implements ILocatorable {
    SAVE_BUTTON("//div[@class='clearfix']//input[@value='Save']"),;
    private String locator;

    UsersDialogLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }
}
