package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by admin on 07.04.14.
 */
public enum RateTablesPageLocators implements ILocatorable {
    RATE_TABLES_TAB("//a[contains(text(),'Rate Tables')]"),
    RATE_CODES_TAB("//a[contains(text(),'Rate Codes')]"),
    RATE_CONFIG_TAB("//a[contains(text(),'Configuration')]"),

    NEW("//input[@value='Add New']"),
    DIALOG_NEW("id=AddEditRateTable"),
    DIALOG_NEW_NAME("id=NameTextAreaId"),
    DIALOG_NEW_DESC("id=DescriptionTextAreaId"),
    DIALOG_NEW_SUBMIT("//input[@value='Save']"),
    TABLE_ROW("//table[@id='PSTable']//tr"),
    TABLE_ROW_NAME("//td[@class='nameColumnValue']/a"),
    TABLE_ROW_NAME_MENU_ARROW_HREF("javascript:void(0)"),
    TABLE_ROW_NAME_MENU_ARROW("//img"),
    TABLE_ROW_DESCRIPTION("//td[@class='descriptionColumnValue']"),
    TABLE_ROW_DEF("//img[@alt='Default']"),
    MENU_DEFAULT("Default"),
    DIALOG_DEFAULT("id=ConfirmDialogDefault"),
    DIALOG_DEFAULT_OK("//input[@value='Ok']");

    RateTablesPageLocators(String l) {
        loc = l;
    }

    String loc;

    @Override
    public String getLocator() {
        return loc;
    }
}
