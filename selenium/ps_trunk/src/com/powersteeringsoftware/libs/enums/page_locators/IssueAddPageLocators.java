package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 16.07.2010
 * Time: 20:07:48
 */
public enum IssueAddPageLocators implements ILocatorable {
    URL("project/IssueAdd.epage"),
    ISSUE_TAG("//div[contains(@config, \"" + LOCATOR_REPLACE_PATTERN + "\")]"),
    ISSUE_PRIORITY("prioritySelect"),
    ISSUE_PRIORITY_OPTION("value=" + LOCATOR_REPLACE_PATTERN),
    CUSTOM_FIELD_ROW("tr"),
    CUSTOM_FIELD_LABEL("/th"),
    CUSTOM_FIELD_LABEL_TOOLTIP("/th/span"),
    CUSTOM_FIELD_TAG_CHOOSER("//div[contains(@class, 'Selector')]"),
    CUSTOM_FIELD_CONFIG("config"),
    CUSTOM_FIELD_HIERAR("\"hierar\":true"),
    CUSTOM_FIELD_FLAT_TAG_CHOOSER("Combo"),
    ;
    String locator;

    IssueAddPageLocators(String s) {
        locator = s;
    }


    public String getLocator() {
        return locator;
    }

    public String replace(String st) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, st);
    }

    public String replace(int i) {
        return replace(String.valueOf(i));
    }

}
