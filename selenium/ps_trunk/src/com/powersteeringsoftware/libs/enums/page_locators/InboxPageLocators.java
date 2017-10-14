package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.05.11
 * Time: 18:33
 */
public enum InboxPageLocators implements ILocatorable {
    URL("person/Inbox.epage"),
    QUESTION_TABLE("//table[@class='questions']"),
    QUESTION_YES("//input[contains(@id, 'radioYes')]"),
    QUESTION_NO("//input[contains(@id, 'radioNo')]"),
    SUBMIT("//input[@value='Submit']"),
    FORM("Main"),
    QUESTION("%1$s has asked you to take ownership of %2$s on %3$s");
    private String locator;

    InboxPageLocators(String loc) {
        locator = loc;
    }

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
