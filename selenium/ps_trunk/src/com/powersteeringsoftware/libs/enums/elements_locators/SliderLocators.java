package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 09.06.2010
 * Time: 20:52:36
 */
public enum SliderLocators implements ILocatorable {
    ROLLER("//div[@class='dijitSliderImageHandle dijitSliderImageHandleH']"),
    INPUT("/parent::*//input[@type='hidden']"),;

    String loc;

    SliderLocators(String s) {
        loc = s;
    }

    public String getLocator() {
        return loc;
    }
}
