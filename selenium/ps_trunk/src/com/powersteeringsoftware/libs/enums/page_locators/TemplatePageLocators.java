package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 26.09.13
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
public enum TemplatePageLocators implements ILocatorable {
    URL("/metrics/Instance.epage"),
    URL_ID(URL.locator + "?sp=U" + LOCATOR_REPLACE_PATTERN),
    TR("//div[@id='content']//table[contains(@class, 'simple')]//tr"),
    ITEM_TABLE("//div[@class='psGrid']//table"),
    ITEM_TH("//th"),
    ITEM_TD("./tbody/tr/td"),;
    private String locator;

    TemplatePageLocators(String s) {
        locator = s;
    }

    @Override
    public String getLocator() {
        return locator;
    }

    public String replace(Object o) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(o));
    }
}
