package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 22.05.13
 * Time: 0:32
 * To change this template use File | Settings | File Templates.
 */
public enum AttachPageLocators implements ILocatorable {
    URL("/measures/Attach.epage"),
    PORTFOLIO_SELECTOR("PSPropertySelection"),
    PORTFOLIO_SELECTOR_MY_PROJECTS("My Projects"),
    TEMPLATES_TAG_CHOOSER("//div[@id='mtsDisp']"),

    TEMPLATES_TAG_LABEL("//label"),
    TEMPLATES_TAG_LABEL_FOR("for"),
    TEMPLATES_TAG_ALL("//div[text()='All']"),

    ATTACH("//input[@value='Attach']"),
    PREVIEW("//input[@value='Preview']"),;
    String locator;

    AttachPageLocators(String s) {
        locator = s;
    }

    @Override
    public String getLocator() {
        return locator;
    }
}
