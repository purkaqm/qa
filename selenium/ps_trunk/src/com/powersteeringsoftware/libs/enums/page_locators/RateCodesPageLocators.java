package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by admin on 11.04.2014.
 */
public enum RateCodesPageLocators implements ILocatorable {
    DIALOG_RATE("id=RateCodeId"),
    TABLE_ROW("//table[@id='PSTable']//tr"),
    TABLE_ROW_CODE("//td[@class='rateCodeColumnValue']"),
    TABLE_ROW_AMOUNT("//td[@class='amountColumnValue']/div"),
    TABLE_ROW_DATE("//td[@class='effectiveSinceColumnValue']"),
    TABLE_ROW_AMOUNT_FUTURE("//td[@class='futureRateColumnValue last']/div"),
    TABLE_ROW_DATE_FUTURE("//td[@class='futureDateColumnValue']"),;
    private String loc;

    RateCodesPageLocators(String l) {
        loc = l;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
