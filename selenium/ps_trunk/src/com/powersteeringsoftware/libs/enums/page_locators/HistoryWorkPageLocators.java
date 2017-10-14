package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 13.05.11
 * Time: 18:44
 */
public enum HistoryWorkPageLocators implements ILocatorable {
    VIEW_SELECTOR("choice"),
    VIEW_SELECTOR_ALL("label=All"),
    GO("//input[@value='Go' and @name='Submit']"),
    TABLE("//table[@id='PSTable']"),
    ROW("/tbody/tr"),
    TH_EVENT("//th[1]"),
    TD_EVENT("//td[1]"),
    TD_AUTHOR_TXT("//td[2]/div"),
    TD_ITEM_LINK("//td[3]/a"),
    TD_DATE("//td[4]"),
    TD_DESC_SPAN("//td[5]/span"),
    TD_DESC_SPAN_MENU("//div[contains(@id,'userInfoDiv')]"),

    EVENT_DAY_DATE_FORMAT("MM/dd/yyyy"),
    EVENT_DATE_FORMAT(EVENT_DAY_DATE_FORMAT.getLocator() + " hh:mm a"),
    EVENT_WORK_DELEGATION_NAME("Work Delegation"),

    EVENT_WORK_DELEGATION_ASKED("%1$s asked %2$s to take ownership of %3$s %4$s"),
    EVENT_WORK_DELEGATION_ACCEPTED("%1$s accepted ownership of %2$s %3$s from %4$s"),
    EVENT_WORK_DELEGATION_REJECT("%1$s declined ownership of %3$s %3$s"),
    DATE_START_SELECTOR("id=widget_startDateOne"),
    DATE_TO_SELECTOR("id=widget_finishDateOne");

    private String locator;

    HistoryWorkPageLocators(String s) {
        locator = s;
    }

    @Override
    public String getLocator() {
        return locator;
    }

    public String replace(String... reps) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.ROOT);
        formatter.format(locator, reps);
        return sb.toString();
    }
}
