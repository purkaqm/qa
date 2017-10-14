package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created by szuev on 07.03.14.
 */
public abstract class APSPage extends Page {
    public String getPageVersion() {
        String title = getPageTitle();
        PSLogger.info("Page title is '" + title + "'");
        return title == null ? "" : title.replaceFirst("^.*\\|\\s+[A-Za-z]+\\s+", "").trim();
    }

    public PowerSteeringVersions getAppVersion() {
        if (!TestSession.isVersionPresent()) {
            String version = getPageVersion();
            for (PowerSteeringVersions ver : PowerSteeringVersions.values()) {
                if (ver.getValue().equals(version)) {
                    TestSession.putObject(TestSession.Keys.APPLICATION_VERSION_NUM, ver.toString());
                    break;
                }
            }
        }
        return TestSession.getAppVersion();
    }
}
