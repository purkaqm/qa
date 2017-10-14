package com.powersteeringsoftware.tests.timesheet;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.Cost;
import com.powersteeringsoftware.libs.objects.Timesheets;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.AgentsPage;
import com.powersteeringsoftware.libs.pages.CostBasePage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.actions.TimesheetsManager;
import com.powersteeringsoftware.libs.tests.actions.UixManager;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.apache.commons.collections.map.HashedMap;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Test(groups = {"timesheets.test", TestSettings.NOT_IE_RC_GROUP})
public class TestDriver extends PSTestDriver {

    private TestData data;
    private static final String TIME_GROUP = "timesheets";

    @Override
    public PSTestData getTestData() {
        if (data == null) data = new TestData();
        return data;
    }


    @BeforeTest(description = "Set resource planning to on", timeOut = CoreProperties.BEFORE_TEST_TIMEOUT)
    public void beforeTest() {
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._8_2)) {
            return;
        }
        // for issue 69542:
        PSLogger.info("Set 'Resource Planning' to 'ON'");
        UixManager.setResourcePlanningOn();
    }

    // ----------------------------------------------------------------
    @Test(groups = TIME_GROUP, description = "Submit Timesheet", dependsOnMethods = "resubmitTimesheetLines")
    public void submitTimesheet() {
        Timesheets ts = getTestData().getTimesheets(TestData.SUBMIT);
        TimesheetsManager.setTimesheets(ts, true);
        Timesheets ts1 = TimesheetsManager.load(ts.getDDate());
        TimesheetsManager.validate(ts, ts1);
    }

    @Test(groups = TIME_GROUP, description = "Approve Timesheet",
            dependsOnMethods = "submitTimesheet")
    public void approveTimesheetLines() {
        Timesheets ts = getTestData().getTimesheets(TestData.SUBMIT);
        TimesheetsManager.approveAll(ts);
        Timesheets ts1 = TimesheetsManager.load(ts.getDDate());
        TimesheetsManager.validate(ts, ts1);
    }
    // ----------------------------------------------------------------

    @Test(groups = TIME_GROUP, description = "Copy Last Timesheet", dependsOnMethods = "submitTimesheet")
    public void copyLastTimesheet() {
        List<Timesheets> res = TestSession.getTimesheets();
        if (res.size() == 0) PSSkipException.skip("No timesheets found");

        Timesheets was = res.get(res.size() - 1);
        Timesheets toSet = getTestData().getTimesheets(TestData.COPY);
        TimesheetsManager.copyLast(was, toSet, true);
        Timesheets ts1 = TimesheetsManager.load(toSet.getDDate());
        TimesheetsManager.validate(toSet, ts1);
    }

    @Test(groups = TIME_GROUP, description = "Reject Timesheet")
    public void rejectTimesheetLines() {
        Timesheets ts = getTestData().getTimesheets(TestData.REJECT);
        TimesheetsManager.setTimesheets(ts, true);
        Timesheets ts1 = TimesheetsManager.load(ts.getDDate());
        TimesheetsManager.validate(ts, ts1);
        TimesheetsManager.rejectAll(ts, "Reject");
        Timesheets ts2 = TimesheetsManager.load(ts.getDDate());
        TimesheetsManager.validate(ts, ts2);
    }

    @Test(groups = TIME_GROUP, description = "Resubmit Timesheet", dependsOnMethods = "rejectTimesheetLines")
    public void resubmitTimesheetLines() {
        Timesheets was = getTestData().getTimesheets(TestData.REJECT);
        Timesheets now = getTestData().getTimesheets(TestData.RESUBMIT);
        now.setSDate(was.getDate());
        TimesheetsManager.setTimesheets(now, true);
        TimesheetsManager.approveAll(now);
    }

    @Test(description = "Validate Timesheet Costs", dependsOnGroups = TIME_GROUP)
    public void validateTimeCosts() {
        Map<Work, List<Cost>> _allCosts = new HashedMap();
        for (String id : new String[]{TestData.SUBMIT, TestData.COPY, TestData.REJECT, TestData.RESUBMIT}) {
            Timesheets t = getTestData().getTimesheets(id);
            Map<Work, List<Cost>> _tCosts = t.getCosts();
            for (Work w : _tCosts.keySet()) {
                if (_allCosts.containsKey(w)) {
                    _allCosts.get(w).addAll(_tCosts.get(w));
                } else {
                    _allCosts.put(w, _tCosts.get(w));
                }
            }
        }
        PSLogger.info("All costs: " + _allCosts);

        AgentsPage agents = new AgentsPage();
        agents.open();
        agents.timeConversion();

        // validate only single work
        Work work = getTestData().getDesignWork();
        SummaryWorkPage sum = WorkManager.enableCosts(work, true);

        CostBasePage costs = sum.openActualCosts();
        List<Cost> actual = new ArrayList<Cost>();
        for (Cost c : costs.getCosts(work)) {
            actual.add(c);
        }
        PSLogger.info("Costs(" + work.getName() + ") from page: " + actual);
        List<Cost> expected = _allCosts.get(work);
        PSLogger.info("Costs(" + work.getName() + ") expected: " + expected);
        Assert.assertTrue(expected.size() != 0, "No costs in the work!");
        for (Cost c : expected) {
            Assert.assertTrue(actual.contains(c), "Can't find expected costs (" + c + ")");
        }
        //all ok
        for (Work w : TestSession.getWorkList()) {
            if (!_allCosts.containsKey(w)) continue;
            for (Cost c : _allCosts.get(w)) {
                w.addCost(c);
            }
            PSLogger.info("Work " + w.getFullName() + " costs : " + w.getCosts());
        }
    }

    @Test(description = "Unapprove Timesheet and Validate Costs", dependsOnMethods = "validateTimeCosts")
    public void unApproveTimesheetLines() { //todo
        Timesheets ts = getTestData().getTimesheets(TestData.SUBMIT);
        Map<Work, List<Cost>> _tCosts = ts.getCosts();
        TimesheetsManager.unApproveAll(ts);
        Timesheets ts1 = TimesheetsManager.load(ts.getDDate());
        TimesheetsManager.validate(ts, ts1);

        // validate only single work
        Work work = getTestData().getDesignWork();
        CostBasePage costs = WorkManager.open(work).openActualCosts();
        List<Cost> actual = new ArrayList<Cost>();
        for (Cost c : costs.getCosts(work)) {
            actual.add(c);
        }
        PSLogger.info("Costs(" + work.getName() + ") from page: " + actual);
        List<Cost> unExpected = _tCosts.get(work);
        PSLogger.info("Costs(" + work.getName() + ") unexpected: " + unExpected);
        for (Cost c : unExpected) {
            Assert.assertFalse(actual.contains(c), "There is a cost (" + c + "), but it is unexpected");
        }

        for (Work w : TestSession.getWorkList()) {
            if (!_tCosts.containsKey(w)) continue;
            for (Cost c : _tCosts.get(w)) {
                w.removeCost(c);
            }
            PSLogger.info("Work " + w.getFullName() + " costs : " + w.getCosts());
        }
    }

    public static class TestData extends PSTestData {
        public static final String SUBMIT = "submit";
        public static final String COPY = "copy";
        public static final String REJECT = "reject";
        public static final String RESUBMIT = "resubmit";
    }
}
