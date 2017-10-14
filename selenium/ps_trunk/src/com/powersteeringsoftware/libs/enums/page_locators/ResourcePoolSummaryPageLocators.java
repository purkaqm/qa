package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created by admin on 29.04.2014.
 */
public enum ResourcePoolSummaryPageLocators implements ILocatorable {
    BACK("link=Back to Manage Pools", "link=Back to list"),;

    ResourcePoolSummaryPageLocators(String l) {
        loc = l;
    }

    ResourcePoolSummaryPageLocators(String l_v11, String l_v12) {
        loc = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._12) ? l_v12 : l_v11;
    }

    private String loc;

    @Override
    public String getLocator() {
        return loc;
    }
}
