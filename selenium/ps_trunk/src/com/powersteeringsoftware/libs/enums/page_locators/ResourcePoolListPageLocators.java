package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by admin on 29.04.2014.
 */
public enum ResourcePoolListPageLocators implements ILocatorable {
    NEW("//input[@value='Add New']"),
    TABLE_ROW("//table[@id='PSTable']//tr"),
    TABLE_ROW_NAME("//td[@class='nameColumnValue']/a"),
    TABLE_ROW_NAME_MENU_ARROW_HREF("javascript:void(0)"),
    TABLE_ROW_NAME_MENU_ARROW("//img"),
    TABLE_ROW_DESC("//td[@class='descriptionColumnValue']"),
    TABLE_ROW_MANAGER("//td[@class='managerColumnValue']/div");
    String loc;

    ResourcePoolListPageLocators(String s) {
        loc = s;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
