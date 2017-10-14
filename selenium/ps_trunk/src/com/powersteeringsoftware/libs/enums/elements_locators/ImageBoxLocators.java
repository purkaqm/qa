package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 18.08.2010
 * Time: 16:03:32
 */
public enum ImageBoxLocators implements ILocatorable {
    NEXT("//div[@class='dijitInline LightboxNext']"),
    PREV("//div[@class='dijitInline LightboxPrev']"),
    OPEN_LIGHT_BOX("//div[@class='dijitInline LightboxZoom']"),
    TITLE("//div[@class='title']"),
    DESCRIPTION("//div[@class='description']"),
    GROUP("//div[@class='lightboxGroupText']"),
    IMAGE("//div[@class='lightboxContainer']//img"),;

    private String locator;

    ImageBoxLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }
}
