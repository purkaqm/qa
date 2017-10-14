package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 12.05.2010
 * Time: 15:14:36
 * To change this template use File | Settings | File Templates.
 */
public enum WorkTreePageLocators implements ILocatorable {
    URL("WorkTree.page"),
    CUSTOM_PERMISSIONS_IMG("//img[@title='Custom Permissions']"),
    WORK_LOCATOR("//div[3]/div/a"),;
    String locator;

    WorkTreePageLocators(String loc) {
        locator = loc;
    }

    public String getLocator() {
        return locator;
    }
}
