package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 18.04.11
 * Time: 16:55
 */
public enum FrameLocators implements ILocatorable {
    RELATIVE("relative="),
    UPPER(RELATIVE.locator + "up"),
    TOP(RELATIVE.locator + "top"),
    IFRAME("//iframe"),
    FRAME("//frame"),
    JSP_FRAME("jspFrame");
    private String locator;

    FrameLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }
}
