package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 08.06.2010
 * Time: 17:13:12
 */
public enum MenuLocators implements ILocatorable {
    MENU("//table[contains(@class, 'dijitMenuTable')]"),
    ROW("//tr"),
    ROW_HIDDEN("hidden"),
    ROW_DISABLED("dijitMenuItemDisabled"),
    ITEM("//td[contains(@class, 'dijitMenuItemLabel')]"),
    INPUT("//td//input"),
    LINK("//td//a"),
    INPUT_TYPE("type"),
    INPUT_CLASS("class"),
    INPUT_CLASS_DIJIT("dijitCheckBoxInput"),;

    public static final String STYLE_WIDGET = "widgetid";
    public static final String STYLE_NONE = ".*display:\\s*none.*";
    public static final String STYLE_HIDDEN = ".*visibility:\\s*hidden.*";
    public static final String HAS_SUBMENU_ATTR = "aria-haspopup";
    public static final String HAS_SUBMENU_TRUE = "true";


    String locator;

    MenuLocators(String loc) {
        locator = loc;
    }

    public String getLocator() {
        return locator;
    }
}
