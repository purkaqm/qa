package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 05.07.2010
 * Time: 14:06:37
 */
public enum PeopleManageActivePageLocators implements ILocatorable {
    SEARCH_BUTTON("//div[@id='PeopleFilters']//input[@value='Search']"),
    USERS_COUNT("//span[@class='paginationMsg']"),
    IMG_ALT("alt"),
    IMG_ALT_DISABLED("disabled"),
    NEXT_IMG("//img[contains(@alt, 'Next Page')]"),
    PREV_IMG("css=img[alt='Previous Page']"),
    LAST_IMG("//img[contains(@alt, 'Last Page')]"),
    FIRST_IMG("//img[contains(@alt, 'First Page')]"),
    PAGE("//div[@class='left']/a[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    PAGE_ACTIVE("//div[@class='left']/b"),
    USER_ROW_BY_EMAIL("//a[text()='" + LOCATOR_REPLACE_PATTERN + "']/parent::td/parent::tr"),
    USER_NAME_COLUMN("//td[@class='nameColumnValue']"),
    USER_NAME_COLUMN_DIV("/div"),
    USER_NO_ACCESS_IMG("//img[contains(@src, 'user-noaccess.gif')]"),
    USER_EMAIL_COLUMN("//td[@class='emailColumnValue']/a"),
    USER_ROW("//table[@id='PSTable']//tr"),
    USER_ELEMENT("//div[@class='link' and text()='" + LOCATOR_REPLACE_PATTERN + "']"),

   ;

    private String locator;

    PeopleManageActivePageLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(Object rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(rep));
    }
}
