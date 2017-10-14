package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 18.06.2010
 * Time: 17:47:47
 */
public enum LightboxDialogLocators implements ILocatorable {
    POPUP("//div[@class='lightbox dijitDialogFixed']"),
    CLOSE("//div[@class='dijitInline LightboxClose']"),;
    private String locator;

    LightboxDialogLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }
}
