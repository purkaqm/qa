package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 19.07.2010
 * Time: 14:35:25
 */
public enum TagsEditEPageLocators implements ILocatorable {
    URL("/admin/tags/TagsEdit"),
    TABLE("//table[@id='PSTable']"),
    NAME_COLUMN_INPUT("//td[@class='nameColumnValue']/input"),
    PARENT_COLUMN_DIV("//td[@class='parentColumnValue']/div"),
    ADD_MODE_BUTTON("//input[@value='Add more']"),
    UPDATE_VALUES_BUTTON("//input[@value='Update values']"),
    ROW_COUNT_ELEMENT("//span[@class='paginationMsg']");
    private String locator;

    TagsEditEPageLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }
}
