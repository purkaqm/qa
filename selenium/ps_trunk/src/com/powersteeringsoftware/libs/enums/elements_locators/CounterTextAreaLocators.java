package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.06.2010
 * Time: 17:49:53
 */
public enum CounterTextAreaLocators implements ILocatorable {
    MAXLENGHT("maxlength"),
    COUNTER_SPAN("/following-sibling::div/span"),;
    String loc;

    CounterTextAreaLocators(String s) {
        loc = s;
    }

    public String getLocator() {
        return loc;
    }
}
