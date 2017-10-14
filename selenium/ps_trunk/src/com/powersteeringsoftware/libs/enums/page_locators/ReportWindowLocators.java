package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 14.06.12
 * Time: 18:32
 * To change this template use File | Settings | File Templates.
 */
public enum ReportWindowLocators implements ILocatorable {
    URL("reportResult.svc?taskId=" + LOCATOR_REPLACE_PATTERN),
    NAME("advReportWindow"),
    WAIT("msg-loading-div"),
    TABLE("//td/table"),
    ROW("//tr"),
    CELL("//td"),
    SPAN("//span"),;
    String loc;

    ReportWindowLocators(String s) {
        loc = s;
    }

    @Override
    public String getLocator() {
        return loc;
    }

    public String replace(Object o) {
        return loc.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(o));
    }

}
