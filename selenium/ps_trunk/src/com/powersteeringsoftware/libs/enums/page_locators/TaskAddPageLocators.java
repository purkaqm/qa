package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 30.01.13
 * Time: 13:44
 * To change this template use File | Settings | File Templates.
 */
public enum TaskAddPageLocators implements ILocatorable {
    URL("project/TaskAdd.epage"),
    NAME("id=nameMasterTask"),
    ADD("//input[@value='Add Action Item']"),;

    private String locator;

    TaskAddPageLocators(String s) {
        locator = s;
    }

    @Override
    public String getLocator() {
        return locator;
    }
}
