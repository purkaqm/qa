package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 22.02.11
 * Time: 20:27
 */
public enum UserProfilePageLocators implements ILocatorable {
    URL("person/UserProfile.epage?sp=U"),
    EDIT_USER_TAB("link=Edit User"),;
    private String locator;

    UserProfilePageLocators(String s) {
        locator = s;
    }


    public String getLocator() {
        return locator;
    }
}
