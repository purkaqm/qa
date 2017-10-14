package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 30.09.2010
 * Time: 16:35:46
 */
public enum TabsDialogLocators implements ILocatorable {
    TAB_PRESSED_ATTRIBUTE("aria-pressed"),
    TAB_PRESSED_ATTRIBUTE_TRUE("true"),;
    private String locator;

    TabsDialogLocators(String loc) {
        locator = loc;
    }

    public static String getTabLocator(ILocatorable name) {
        return "//span[text()='" + name.getLocator() + "']/parent::div";
    }

    public String getLocator() {
        return locator;
    }

}
