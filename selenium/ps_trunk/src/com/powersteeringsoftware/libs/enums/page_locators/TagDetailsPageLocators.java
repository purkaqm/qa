package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 05.04.11
 * Time: 16:32
 */
@Deprecated
public enum TagDetailsPageLocators implements ILocatorable {
    BUTTON_ADD_UPDATE("dom=window.dojo.query('form#tagValuesForm a')[0]"),
    LINK_EDIT("link=Edit");

    String locator;

    TagDetailsPageLocators(String _locator) {
        locator = _locator;
    }

    public String getLocator() {
        return locator;
    }
}
