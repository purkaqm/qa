package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 20.11.13
 * Time: 16:52
 * To change this template use File | Settings | File Templates.
 */
public enum CostBasePageLocators implements ILocatorable {
    URL_ESTIMATED("/project/costs/Estimated.epage"),
    URL_ACTUAL("/project/costs/Actual.epage"),
    OPEN_DIALOG("AddCostShow"),
    DIALOG("AddCost"),
    DIALOG_AMOUNT("costAmount"),
    DIALOG_DESCRIPTION("description"),
    DIALOG_DATE("//div[@id='widget_date']"),
    DIALOG_END_DATE("//div[@id='widget_endDate']"),
    DIALOG_ADD("//button[@onclick='addCost(this)']"),
    DIALOG_SUBMIT("//input[@value='Submit Costs']"),
    DIALOG_TABLE("newCostsTable"),
    TR("/tr"),
    TR_EST_DESC("//span"),
    TR_EST_DESC_ACR("/acronym"),
    TABLE("//table[@id='PSTable']"),
    TABLE_TR(TABLE.loc + "/" + TR.loc),
    TABLE_AMOUNT("//td[contains(@class, 'amountColumnValue')]"),
    TABLE_RESOURCE_ROLE("//td[@class='resourceColumnValue']"),
    TABLE_RESOURCE_ROLE_USER("//td[@class='resourceColumnValue']/div"),
    TABLE_DATE("//td[@class='dateColumnValue']"),
    TABLE_DESC("//td[@class='descriptionColumnValue']"),
    TABLE_ADD_TYPE("//td[@class='typeColumnValue']"),
    TABLE_ACTIVITY("//td[@class='activityColumnValue']/div"),
    TABLE_HOURS("//td[@class='hoursColumnValue']"),

    NEXT("linkFwd"),
    MORE("//img[@class='paginationChangeImg']"),;
    private String loc;

    CostBasePageLocators(String loc) {
        this.loc = loc;
    }

    @Override
    public String getLocator() {
        return loc;
    }

    public String replace(Object o) {
        return loc.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(o));
    }
}
