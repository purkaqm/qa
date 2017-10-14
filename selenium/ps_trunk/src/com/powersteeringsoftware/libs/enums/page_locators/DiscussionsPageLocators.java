package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 28.03.11
 * Time: 19:47
 */
public enum DiscussionsPageLocators implements ILocatorable {
    SUBJECT_COLUMN_CELL_LINK("//td[@class='nameColumnValue']/a"), // todo: may be move id in AbstractDiscussionsPageLocators?
    NEW_DISCUSSION_BUTTON("//input[@value='New Discussion']"),;
    private String locator;

    DiscussionsPageLocators(String s) {
        locator = s;
    }


    @Override
    public String getLocator() {
        return locator;
    }
}
