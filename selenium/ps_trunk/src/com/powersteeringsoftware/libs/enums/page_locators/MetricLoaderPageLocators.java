package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 11.08.11
 * Time: 20:43
 */
public enum MetricLoaderPageLocators implements ILocatorable {
    UPLOAD("Upload"),
    GO("//input[@value='Go']"),
    ERROR_CELL("//td[contains(@class, 'msgStrColumnValue')]"),;
    String loc;

    MetricLoaderPageLocators(String s) {
        loc = s;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
