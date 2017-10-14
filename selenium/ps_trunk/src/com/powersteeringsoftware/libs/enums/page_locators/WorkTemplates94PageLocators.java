package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 07.10.12
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 */
public enum WorkTemplates94PageLocators implements ILocatorable {
    NAME_COLUMN_LINKS("//td[@class='templateColumnValue']//a"),
    NEXT("//img[contains(@alt, 'Next Page')]"),
    CREATE_NEW("//input[@value='Create new']", "//input[@value='Add new template']", "//input[@value='Add New']"),;
    private String loc;

    WorkTemplates94PageLocators(String s) {
        loc = s;
    }

    WorkTemplates94PageLocators(String s, String s2, String s3) {
        loc = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._12) ? s3 : TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._10_0) ? s2 : s;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
