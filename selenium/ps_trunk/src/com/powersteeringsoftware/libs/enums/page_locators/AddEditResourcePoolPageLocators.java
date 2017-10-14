package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by admin on 29.04.2014.
 */
public enum AddEditResourcePoolPageLocators implements ILocatorable {
    NAME("id=poolName"),
    DESCRIPTION("id=poolDescription"),
    SUBMIT("//input[@value='Save Changes']"),;
    String loc;

    AddEditResourcePoolPageLocators(String s) {
        loc = s;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
