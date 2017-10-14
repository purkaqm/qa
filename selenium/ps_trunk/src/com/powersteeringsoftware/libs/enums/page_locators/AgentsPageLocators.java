package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 12.10.11
 * Time: 17:11
 */
public enum AgentsPageLocators implements ILocatorable {
    REINDEX_BASIC_SEARCH("Reindex Search", "Reindex Basic Search"),
    DOCUMENT_SEARCH_REINDEX("Reindex Document Content"),
    TIME_CONVERSION_AGENT("Time Conversion Agent"),
    AGENT_LINK("link=" + LOCATOR_REPLACE_PATTERN),

    FRAME("jspFrame"),
    RUN_AGENT("link=Run Agent");
    String locator;

    AgentsPageLocators(String s) {
        locator = s;
    }

    AgentsPageLocators(String loc90, String loc91) {
        locator = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_1) ? loc91 : loc90;
    }

    @Override
    public String getLocator() {
        return locator;
    }

    public String replace(Object o) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(o));
    }
}
