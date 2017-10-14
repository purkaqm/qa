package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 11.09.12
 * Time: 21:17
 * To change this template use File | Settings | File Templates.
 */
public enum InviteNewUserPageLocators implements ILocatorable {
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    EMAIL("email"),
    SUBMIT("SubmitButton"),
    USER_LINK("//strong/div")
    ;
    private String locator;

    InviteNewUserPageLocators(String s) {
        locator = s;
    }

    @Override
    public String getLocator() {
        return locator;
    }
}
