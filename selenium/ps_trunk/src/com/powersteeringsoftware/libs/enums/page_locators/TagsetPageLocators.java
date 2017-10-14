package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 03.06.11
 * Time: 17:20
 */
public enum TagsetPageLocators implements ILocatorable {
    EDIT_TAG_SET_DIALOG("EditTagSet"),
    LINK("//a"),
    UPDATE_LINK_NAME("Update"),
    EDIT_LINK("link=Edit"),
    URL("/admin/tags/Tagset.epage"),
    BACK_LINK("link=Back to tags list"),;
    private String locator;

    TagsetPageLocators(String s) {
        locator = s;
    }

    @Override
    public String getLocator() {
        return locator;
    }
}
