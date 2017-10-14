package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 20.07.2010
 * Time: 14:03:05
 */
public enum TagsDependenciesLocators implements ILocatorable {
    //ROW("/tr[" + LOCATOR_REPLACE_PATTERN + "]"),
    ROWS("//tr[contains(@id, 'pk_TreeDisptest')]"),
    ROW_STYLE("style"),
    ROW_STYLE_UNVISIBLE("display: none"),
    SELECT("//select"),
    HELPER("//span[@class='label-tooltip']"),
    //HELPER("/parent::div//span/span/img"),
    LOADING("//td[@class='msg-loading']");
    private String locator;

    TagsDependenciesLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }
}
