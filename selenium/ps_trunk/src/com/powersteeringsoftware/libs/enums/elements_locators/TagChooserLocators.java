package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 04.06.2010
 * Time: 20:11:11
 */
public enum TagChooserLocators implements ILocatorable {
    OPEN_POPUP("/div"),
    POPUP_LOADING("//div[contains(@class, 'waitingUnderlay')]"),
    POPUP_MAIN("//div[@class='main']"),
    POPUP_LABELS(".//label/input"),
    POPUP_LABELS_PARENT("//div[@containerid='root']"),
    POPUP_HIDDEN_LABELS_PARENT("//div[@class='hidden' and @containerid='root']"),
    POPUP_LABEL_TYPE("type"),
    POPUP_LABEL_TYPE_RADIO("radio"),
    POPUP_LABEL_TYPE_CHECKBOX("checkbox"),
    POPUP_DONE("//input[@value='Done']"),
    POPUP_CANCEL("//input[@value='Cancel']"),
    POPUP_ALL("//div[@action='All']"),
    POPUP_NONE("//div[@action='None']"),
    INPUT_CONTENT("/div"),
    INPUT_CONTENT_EMPTY("//span[@class='no-value']");

    String locator;

    TagChooserLocators(String loc) {
        locator = loc;
    }

    public String getLocator() {
        return locator;
    }
}
