package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 04.09.11
 * Time: 19:16
 * To change this template use File | Settings | File Templates.
 */
public enum DocumentMenuLocators implements ILocatorable {
    LINK("//a[contains(text(), '" + LOCATOR_REPLACE_PATTERN + "')]"),
    DOWNLOAD("Download"),
    UPDATE("Update"),
    UNLOCK("Unlock"),
    SEE_DETAILS("See details"),;
    private String locator;

    DocumentMenuLocators(String loc) {
        locator = loc;
    }

    @Override
    public String getLocator() {
        return locator;
    }

    public String replace(String loc) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, loc);
    }
}
