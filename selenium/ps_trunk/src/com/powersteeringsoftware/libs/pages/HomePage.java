package com.powersteeringsoftware.libs.pages;

import static com.powersteeringsoftware.libs.enums.page_locators.HomePageLocators.URL;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 03.08.11
 * Time: 15:11
 */
public class HomePage extends PSTablePage {

    private long lastClickSubmitTime;

    @Override
    public void open() {
        open(URL);
    }

    public long getLoadingTime() {
        return lastPageLoadingTime;
    }

    public long getLastClickSubmitTime() {
        return lastClickSubmitTime;
    }

    public void setLastClickSubmitTime(long time) {
        lastClickSubmitTime = time;
    }
}
