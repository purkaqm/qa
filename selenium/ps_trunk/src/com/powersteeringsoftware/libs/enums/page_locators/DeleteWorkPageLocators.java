package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 07.06.13
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */
public enum DeleteWorkPageLocators implements ILocatorable {
    URL("project/DeleteWork"),
    URL_RESULT("project/DeleteWorkResults"),
    DELETE("//div[@id='content']//input[@value='Delete']"),
    DELETE_OK("//input[@value='Ok']"),;
    private String loc;

    DeleteWorkPageLocators(String s) {
        loc = s;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
