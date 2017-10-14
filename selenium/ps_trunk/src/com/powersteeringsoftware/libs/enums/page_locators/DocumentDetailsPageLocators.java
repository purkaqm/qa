package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 04.09.11
 * Time: 20:01
 * To change this template use File | Settings | File Templates.
 */
public enum DocumentDetailsPageLocators implements ILocatorable {
    UPLOAD_INPUT("id=upload"),
    COMMENTS_TEXTAREA("id=html_Textarea"),
    UPLOAD_BUTTON_LINK("//a[text()='Upload New Version']"),

    TITLE_INPUT("id=docTitle"),
    DESCRIPTION_TEXTAREA("id=TextArea"),
    SUBMIT("//input[@value='Save Changes']"),

    TABLE("//table[contains(@id, 'PSTable')]"),
    ROW("//tr"),
    VERSION_COLUMN("//td[@class='versionColumnValue']"),
    VERSION_COLUMN_LINK("/a"),
    VERSION_COLUMN_LINK_TXT("#" + LOCATOR_REPLACE_PATTERN),
    COMMENTS_COLUMN("//td[contains(@class, 'commentsColumnValue')]"),

    EDIT_DETAILS_LINK("//a[text()='Edit Details']"),;
    private String locator;

    DocumentDetailsPageLocators(String s) {
        locator = s;
    }

    @Override
    public String getLocator() {
        return locator;
    }

    public String replace(Object o) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(o));
    }
}
