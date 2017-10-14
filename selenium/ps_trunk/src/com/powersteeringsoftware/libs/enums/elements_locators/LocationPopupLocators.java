package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 22.10.12
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
public enum LocationPopupLocators implements ILocatorable {
    LINK("//ul/li/a"),
    TD_B("//td/b"),
    LOCATION_TXT("Location"),
    LOCATION("//td/b[contains(text(), 'Location')]"),
    WIDGET_ID("widgetid"),
    DIV("./*[@" + WIDGET_ID.loc + "]"),
    LOADING("workPopupLoading"),
    CLOSE_SCRIPT("window.dijit.byId('" + LOCATOR_REPLACE_PATTERN + "').closeAndRestoreFocus()"),;
    private String loc;

    LocationPopupLocators(String s) {
        loc = s;
    }

    @Override
    public String getLocator() {
        return loc;
    }

    public String replace(Object rep) {
        return loc.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(rep));
    }

}
