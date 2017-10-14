package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by admin on 09.05.2014.
 */
public enum EditPortfolioPageLocators implements ILocatorable {
    NAME("id=PortfolioName"),
    DESCENDED_FROM("id=popup_descfrShow"),
    DESCENDED_FROM_POPUP("//div[@id='popup_descfr']"),
    SUBMIT("//input[@value='Save Changes']"),;

    EditPortfolioPageLocators(String s) {
        loc = s;
    }

    String loc;

    @Override
    public String getLocator() {
        return loc;
    }
}
