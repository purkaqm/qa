package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Link;

import static com.powersteeringsoftware.libs.enums.page_locators.ResourcePoolSummaryPageLocators.BACK;

/**
 * Created by admin on 29.04.2014.
 */
public class ResourcePoolSummaryPage extends PSPage {
    @Override
    public void open() {
        //donotnothing
    }

    public ResourcePoolListPage back() {
        new Link(BACK).click(false);
        ResourcePoolListPage res = new ResourcePoolListPage();
        res.waitForPageToLoad();
        return res;
    }
}
