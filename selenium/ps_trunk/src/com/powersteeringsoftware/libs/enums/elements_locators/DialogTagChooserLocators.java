package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 06.09.13
 * Time: 12:58
 * To change this template use File | Settings | File Templates.
 */
public enum DialogTagChooserLocators implements ILocatorable {
    LABEL("//label"),
    LABEL_ACRONYM("//acronym"),
    LABEL_FOR("for"),
    ALL("//div[@class='link' and text()='All']"),
    NONE("//div[@class='link' and text()='None']"),;

    String loc;

    DialogTagChooserLocators(String s) {
        loc = s;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
