package com.powersteeringsoftware.libs.pages;

import static com.powersteeringsoftware.libs.enums.page_locators.BasicCommonsLocators.LAST_VISITED_MORE;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 03.08.11
 * Time: 16:33
 */
public class HistoryPage extends PSPage {
    @Override
    public void open() {
        //TODO! for 11.0
        PSPage res = openLastVisitedPage(LAST_VISITED_MORE.getLocator());
        url = res.url;
        document = res.document;
    }
}
