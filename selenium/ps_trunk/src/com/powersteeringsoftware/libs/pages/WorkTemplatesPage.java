package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.enums.page_locators.WorkTemplates93PageLocators;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 07.10.12
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class WorkTemplatesPage extends PSPage {
    @Override
    public void open() {
        clickAdminTemplatesWork();
        Assert.assertTrue(checkUrl(WorkTemplates93PageLocators.URL));
    }

    public SummaryWorkPage openSummaryForTemplate(String name) {
        return openSummaryForTemplate(name, true);
    }

    public abstract SummaryWorkPage openSummaryForTemplate(String name, boolean doCheck);

    public static WorkTemplatesPage getInstance() {
        return TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4) ? new WorkTemplates94Page() : new WorkTemplates93Page();
    }
}
