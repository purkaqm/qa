package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 09.06.2010
 * Time: 16:12:44
 */
public enum DialogLocators implements ILocatorable {
    POPUP("//div[@class='dijitDialog psDialog'][" + LOCATOR_REPLACE_PATTERN + "]"),
    TITLE("//div[@class='dijitDialogTitleCnt']"),
    CLOSE("//span[@class='dijitDialogCloseIcon']"),
    BODY("//div[@class='dijitDialogPaneContent']"),
    NEXT("/div"),;
    String locator;

    DialogLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(int i) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(i));
    }
}
