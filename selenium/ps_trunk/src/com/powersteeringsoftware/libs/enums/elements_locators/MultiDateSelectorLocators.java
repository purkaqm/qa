package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 01.06.2010
 * Time: 19:20:28
 */
public enum MultiDateSelectorLocators implements ILocatorable {
    ADD("//img[@alt='add']"),
    DELETE("//img[@alt='delete']"),
    CHILD("/div/div[" + LOCATOR_REPLACE_PATTERN + "]"),
    CHILDREN("/div/div"),
    DATE_PICKER("//div[@dojoattachpoint='displayDiv']/div/div"),;
    String locator;

    MultiDateSelectorLocators(String loc) {
        locator = loc;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(Object i) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(i));
    }

}
