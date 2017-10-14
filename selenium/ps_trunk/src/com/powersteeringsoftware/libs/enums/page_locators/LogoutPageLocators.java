package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 14.01.12
 * Time: 21:32
 * To change this template use File | Settings | File Templates.
 */
public enum LogoutPageLocators implements ILocatorable {
    URL("login/Log"), // login/Logout.page, login/Login,form.sdirect
    LOGOUT_RESTART_SESSION_LINK_1("link=here"),
    LOGOUT_RESTART_SESSION_LINK_2("//div[@class='MsgBox BlueBoxBorder']/a"),;

    String locator;

    LogoutPageLocators(String locator) {
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }
}
