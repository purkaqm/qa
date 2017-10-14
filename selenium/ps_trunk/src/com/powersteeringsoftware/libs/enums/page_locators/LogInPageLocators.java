package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 14.01.12
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public enum LogInPageLocators implements ILocatorable {

    BLANK_PAGE("Comet.svc"),
    VERSION_V10("//div[@id='login']/h5"),
    VERSION_V11("//div[@class='version']"),
    VERSION_V13("//div[@class='loginBottom']//tr/td[2]/div[2]"),

    LOGIN_NAME_INPUTFIELD("user"),
    LOGIN_PASSWORD_INPUTFIELD("pass"),
    LOGIN_SUBMIT_BOTTON("Submit_0"),
    LICENSE_BUTTON("popLicenseShow"),
    LICENSE_DIALOG("popLicense"),
    LICENSE_ACCEPT("accept"),;

    String locator;

    LogInPageLocators(String locator) {
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }
}
