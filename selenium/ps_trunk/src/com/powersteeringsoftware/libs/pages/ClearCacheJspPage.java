package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.enums.page_locators.ClearCacheJspPageLocators;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 04.06.13
 * Time: 16:03
 * To change this template use File | Settings | File Templates.
 */
public class ClearCacheJspPage extends Page {

    public void open() {
        super.open(ClearCacheJspPageLocators.URL.getLocator());
    }

    public void doClearEntire() {
        new Link(ClearCacheJspPageLocators.LINK_ENTIRE).clickWithConfirmation(true);
    }

    public void doClearPortfolios() {
        new Link(ClearCacheJspPageLocators.LINK_PORTFOLIOS).clickWithConfirmation(true);
    }
}
