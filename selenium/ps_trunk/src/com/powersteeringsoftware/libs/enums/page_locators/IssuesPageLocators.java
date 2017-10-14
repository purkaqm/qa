package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 01.04.11
 * Time: 19:40
 */
public enum IssuesPageLocators implements ILocatorable {
    NEW_ISSUE_BUTTON("//input[@value='New Issue']"),;
    private String locator;

    IssuesPageLocators(String s) {
        locator = s;
    }

    @Override
    public String getLocator() {
        return locator;
    }
}
