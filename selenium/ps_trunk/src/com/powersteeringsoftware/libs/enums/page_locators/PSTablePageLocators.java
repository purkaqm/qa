package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by admin on 07.05.2014.
 */
public enum PSTablePageLocators implements ILocatorable {
    NAME_COLUMN("//td[@class='nameColumnValue']"),
    LINK(".//a"),
    MORE("paginateViewToggle"),
    TABLE("//table[@id='PSTable']"),
    TABLE_ROW("//tr"),
    TABLE_ROW_NAME("//td[@class='nameColumnValue']//a"),;
    private String loc;

    PSTablePageLocators(String l) {
        loc = l;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
