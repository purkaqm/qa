package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 28.03.13
 * Time: 1:10
 * To change this template use File | Settings | File Templates.
 */
public enum EditObjectTypePageLocators implements ILocatorable {
    COPY("//input[@value='Copy']"),
    UPDATE("//input[@value='Update']"),
    PLURAL_NAME("typeNamePlural"),
    NAME("typeName"),
    BACK("link=Back to object types");
    private String locator;

    EditObjectTypePageLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(String toRep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, toRep);
    }
}
