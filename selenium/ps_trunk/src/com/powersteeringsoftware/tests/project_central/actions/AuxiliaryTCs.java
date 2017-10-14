package com.powersteeringsoftware.tests.project_central.actions;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.BuiltInRole;
import com.powersteeringsoftware.libs.objects.Currency;
import com.powersteeringsoftware.libs.objects.UixFeature;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.resources.RateTable;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.RateTablesPage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.tests.actions.ResourceManager;
import com.powersteeringsoftware.libs.tests.actions.UixManager;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.List;

/**
 * Class for auxiliary actions (no project central)
 * User: szuev
 * Date: 21.05.2010
 * Time: 12:53:31
 */
public class AuxiliaryTCs {

    public static void createWorks(Work p) {
        WorkManager.createProjects(p);
    }

    public static WBSPage createWork(Work w) {
        SummaryWorkPage sum = (SummaryWorkPage) WorkManager.createProject(w);
        WBSPage pc = WorkManager.editManualScheduling(sum, w).openProjectPlanning();
        Assert.assertTrue(pc.getGrid().isWorkPresent(w.getName()), "Can't find link with " + w.getName() + " after creating");
        w.setWbsPCUrl(pc.getUrl());
        return pc;
    }

    public static void setUixPageSettings(String value1, String value2, String value3) {
        UixManager.setFeatures(new Object[]{UixFeature.Code.RESOURCE_PLANING, value1},
                new Object[]{UixFeature.Code.IMMEDIATE_DELEGATION, value2},
                new Object[]{UixFeature.Code.RESOURCE_MANAGER, value3}
        );
    }

    public static void checkResourcePlanningOn() {
        if (!UixManager.isResourcePlanningOn())
            UixManager.setResourcePlanningOn();
    }

    public static void checkTeamPaneOn() {
        // todo: team pane is not supported now
        PSSkipException.skip("This test-case is not enabled, team pane is off");
    }

    static void saveAndCheckBugLike73593(WBSPage pc) {
        try {
            pc.saveArea();
        } catch (AssertionError e) {
            PSLogger.knis(73593);
            pc.saveArea();
        }
    }

    public static void createRateTable() {
        if (TestSession.getDefaultRateTable() != null) return;
        PSCalendar calendar = PSCalendar.getEmptyCalendar(System.currentTimeMillis());
        RateTable tb = new RateTable("test-table" + calendar.getTime());
        tb.setDescription("table for test project central");
        tb.addRate(BuiltInRole.OWNER, null, null, Currency.getCurrencyByName("USD"), 15d);
        tb.addRate(BuiltInRole.CONTRIBUTOR, null, null, Currency.getCurrencyByName("EUR"), 3d);
        /*com.powersteeringsoftware.tests.resource_rates.TestDriver dr = new com.powersteeringsoftware.tests.resource_rates.TestDriver();
        RateTable tb = dr.getTestData().getRateTable(com.powersteeringsoftware.tests.resource_rates.TestDriver.TestData.RATE_TABLE_2);
        ResourceManager.addRateCode(dr.getTestData().getRateCode(com.powersteeringsoftware.tests.resource_rates.TestDriver.TestData.RATE_CODE_1),
                dr.getTestData().getRateCode(com.powersteeringsoftware.tests.resource_rates.TestDriver.TestData.RATE_CODE_2));*/

        ResourceManager.createRateTable(tb);
        RateTablesPage page = ResourceManager.openRateTables();
        List<String> tables = page.getTables();
        if (tables.size() > 1) {
            ResourceManager.setDefaultTable(tb);
        }
        ResourceManager.enableLaborCosts();
    }

}
