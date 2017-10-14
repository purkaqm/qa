package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 30.08.12
 * Time: 12:57
 * To change this template use File | Settings | File Templates.
 */
public enum MeasureInstancePageLocators implements ILocatorable {
    URL("/measures/Instance"),
    TABLE("PSTable"),;
    private String loc;

    MeasureInstancePageLocators(String s) {
        loc = s;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
