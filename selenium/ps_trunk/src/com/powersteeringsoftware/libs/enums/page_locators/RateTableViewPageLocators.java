package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by admin on 07.04.14.
 */
public enum RateTableViewPageLocators implements ILocatorable {
    NEW("//input[@value='Add New']"),
    DIALOG("id=AddEditRate"),
    DIALOG_OK("//input[@value='Save']"),
    DIALOG_CANCEL("//input[@value='Cancel']"),
    DIALOG_ROLE("id=widget_RateRoleId"),
    DIALOG_ACTIVITY("//div[@class='Selector Combo']"),
    DIALOG_DATE("id=widget_RateEffectiveSinceId"),
    DIALOG_CHOOSE_CODE("id=ChooseRateCodeId"),
    DIALOG_CUSTOME_CODE("id=CustomRateId"),
    DIALOG_CODE("id=widget_RateCodeSelectId"),
    DIALOG_AMOUNT("id=RateAmountId"),
    DIALOG_CURRENCY("id=widget_RateCurrencyId"),

    TABLE_ROW("//table[@id='PSTable']//tr"),
    TABLE_ROW_ROLE("//td[@class='roleColumnValue']"),
    TABLE_ROW_ACTIVITY("//td[@class='tagsetColumnValue']/div"),
    TABLE_ROW_DATE("//td[@class='effectiveSinceColumnValue']"),
    TABLE_ROW_AMOUNT("//td[@class='amountColumnValue']/div"),
    TABLE_ROW_DEF("//img[@alt='Default']"),;
    String loc;

    RateTableViewPageLocators(String s) {
        loc = s;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
