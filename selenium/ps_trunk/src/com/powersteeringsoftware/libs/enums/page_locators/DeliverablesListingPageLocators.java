package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 16.06.11
 * Time: 14:10
 */
public enum DeliverablesListingPageLocators implements ILocatorable {
    URL("/project/DeliverablesListing.epage"),
    BUTTON_ADD_NEW("AddDeliverableShow"),
    BUTTON_DELETE("//div[@class='psGrid']//input[@value='Delete']"),

    TABLE("//table[@id='PSTable']"),
    TABLE_ROWS("//tr[@class]"),
    TABLE_GATE("//td[@class='gateColumnValue']"),
    TABLE_LINKS("//td[@class='titleColumnValue']/a"),
    TABLE_CHECKBOX("//td[@class='deleteColumnValue']/input"),
    TABLE_LINK(TABLE_LINKS.locator + "[contains(text(), '" + LOCATOR_REPLACE_PATTERN + "')]"),

    ADD_DELIVERABLE_POPUP_ID("AddDeliverable"),
    ADD_DELIVERABLE_POPUP_SELECT_WORK_TYPE("workType"),
    ADD_DELIVERABLE_POPUP_SELECT_GATE("gateSelection"),
    ADD_DELIVERABLE_POPUP_BUTTON_CONTINUE("continueBtn"),
    ADD_DELIVERABLE_POPUP_BUTTON_CANCEL("AddDeliverableHide"),;
    String locator;

    DeliverablesListingPageLocators(String s) {
        locator = s;
    }

    @Override
    public String getLocator() {
        return locator;
    }

    public String replace(String rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, rep);
    }
}
