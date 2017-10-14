package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 30.01.13
 * Time: 13:29
 * To change this template use File | Settings | File Templates.
 */
public enum TasksPageLocators implements ILocatorable {
    URL("project/Tasks.epage"),
    NEW_TASK_BUTTON("//input[@value='Add Action Item']"),;
    private String locator;

    TasksPageLocators(String s) {
        locator = s;
    }

    @Override
    public String getLocator() {
        return locator;
    }
}
