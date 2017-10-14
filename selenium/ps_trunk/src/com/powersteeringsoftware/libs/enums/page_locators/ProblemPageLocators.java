package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 27.04.2010
 * Time: 14:32:09
 * To change this template use File | Settings | File Templates.
 */
public enum ProblemPageLocators implements ILocatorable {
    PROBLEM("Problem Detected"),
    STALE_PAGE("Stale Page Encountered"),
    PROBLEM_HEADER("//strong[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    PROBLEM_HEADER_IE_RC("css=strong:contains('" + LOCATOR_REPLACE_PATTERN + "')"),

    PROBLEM_DETAILS_LINK("link=Details"),
    PROBLEM_EXCEPTION_TABLE("//table[@class='exception-display']"),
    PROBLEM_EXCEPTION_TD(".//tr/td"),
    PROBLEM_EXCEPTION_TD_STACK_TRACE("//ul/li"),
    PROBLEM_PAGE_FRAME_LOCATOR("relative=up"),

    SERVLET_EXCEPTION_HEADER("//h1[contains(text(), 'Servlet Exception')]"),
    SERVLET_EXCEPTION_DETAILS_LINK("link=[show]"),
    SERVLET_EXCEPTION_TRACE("//span[@id='trace']"),
    PERMISSION_EXCEPTION("//td[contains(text(),'Insufficient Permission')]"),
    OBJECT_DELETED_EXCEPTION("//td[contains(text(),'Object deleted')]"),
    SERVER_UNAVAILABLE_LOCATOR("//h1[text()='Service Temporarily Unavailable']"),

    ALERT_ERROR_HEADER("//img[contains(@src,'icon_alert_lrg.gif')]"),
    ALERT_ERROR_DETAILS_LINK("link=Error Details"),
    ALERT_ERROR_BODY("//div[@class='exception']"),

    FF_FRAME_UNAVAILABLE_LOCATOR("errorLongContent"),;


    String locator;

    ProblemPageLocators(String locator) {
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(ILocatorable loc) {
        return replace(loc.getLocator());
    }

    public String replace(Object o) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(o));
    }
}
