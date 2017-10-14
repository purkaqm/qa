package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 04.06.13
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
public enum ClearCacheJspPageLocators implements ILocatorable {
    URL("admin/clear_cache.jsp"),
    LINK_ENTIRE("link=Clear Entire Cache"),
    LINK_PORTFOLIOS("link=Clear Portfolio Cache"),;
    private String locator;

    ClearCacheJspPageLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }

}
