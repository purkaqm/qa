package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.10.11
 * Time: 16:12
 */
public enum UserChooserLocators implements ILocatorable {
    GRID_TABLE_OWNER_INPUT("//input[contains(@id,'ps_ui_FilteredAjaxListSelect_')]"),;

    String locator;

    UserChooserLocators(String s) {
        locator = s;
    }

    @Override
    public String getLocator() {
        return locator;
    }
}
