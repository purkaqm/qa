package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by admin on 06.05.2014.
 */
public enum AgendaPageLocators implements ILocatorable {
    URL_NAME("Agenda"),
    CURRENT("//li[contains(@class, 'hl')]/*"),
    TAB_LINK("//li[contains(@class, 'nl')]/a[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    TAB_TASKS("Action Items"),
    TAB_RISKS("Risks"),
    TAB_ISSUES("Issues"),
    TAB_IDEAS("Ideas"),
    TAB_PROJECTS("Projects"),;
    String loc;

    AgendaPageLocators(String s) {
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
