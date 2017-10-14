package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 29.03.13
 * Time: 21:25
 * To change this template use File | Settings | File Templates.
 */
public enum ObjectTypesJspPageLocators implements ILocatorable {
    URL("admin/object_types.jsp"),
    LINK("//a[text()='" + LOCATOR_REPLACE_PATTERN + "']"),

    EDIT_COPY("link=Copy"),
    EDIT_NAME("name=name"),
    EDIT_PLURAL_NAME("name=plural"),
    EDIT_SUBMIT("link=Submit"),

    EDIT_BACK("css=img[alt='Back']"),;
    private String locator;

    ObjectTypesJspPageLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(String toRep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, toRep);
    }
}

