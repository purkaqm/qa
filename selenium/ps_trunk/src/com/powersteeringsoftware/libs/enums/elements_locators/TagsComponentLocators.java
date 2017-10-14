package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 14.10.13
 * Time: 13:47
 * To change this template use File | Settings | File Templates.
 */
public enum TagsComponentLocators implements ILocatorable {
    SELECTOR("//div[contains(@class, 'Selector')]"),
    SELECTOR_COMBO("Combo"),
    SELECTOR_CONFIG("config"),
    SELECTOR_CONFIG_HIERARCHICAL("hierar"),
    SELECTOR_TR("tr"),
    SELECTOR_LABEL("//th"),
    SELECTOR_LABEL_2("//span");

    String locator;

    TagsComponentLocators(String loc) {
        locator = loc;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(String rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, rep);
    }
}
