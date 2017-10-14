package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 09.07.2010
 * Time: 17:19:15
 */
public enum MultipleTagChooserLocators implements ILocatorable {
    SEARCH_DIV("//div[@class='toolbar-top clearfix']"),
    SEARCH_INPUT("//input[@class='txt']"),
    SEARCH_SUBMIT("//input[@value='Search']"),
    SEARCH_SHOW_CHECKED("//input[@value='Show Checked']"),
    ALL_DISPLAYED("//div[@action='AllDisp']"),;
    private String locator;

    MultipleTagChooserLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }
}
