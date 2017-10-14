package com.powersteeringsoftware.tests.project_central.actions;

import com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.tests.project_central.TestData;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.11.2010
 * Time: 14:08:29
 */
public class GeneralAddSubWorkPopupTCs {

    public void testDurationAllocationEffort(Work parent) {
        WBSPage pc = WBSPage.openWBSPage(parent);
        WBSPage.AddSubWorkDialog popup = pc.getGrid().callSubMenu().addUnder();

        Work[] chs = new Work[]{
                Work.create("1"),
                Work.createEmpty("2"),
                Work.createEmpty("3"),
                Work.createEmpty("4"),
                Work.createEmpty("5"),
        };

        List<Integer> expectedDurations = new ArrayList<Integer>();
        List<Float> expectedAllocations = new ArrayList<Float>();
        List<Float> expectedEfforts = new ArrayList<Float>();
        for (int i = 0; i < chs.length; i++) {
            expectedDurations.add(WBSEPageLocators.GRID_DEFAULT_DURATION.getInt());
            expectedAllocations.add(WBSEPageLocators.GRID_DEFAULT_ALLOCATION.getFloat());
            expectedEfforts.add(WBSEPageLocators.GRID_DEFAULT_EFFORT.getFloat());
        }


        chs[0].setResourceDuration(2 + TestData.getRandom().nextInt(100));
        chs[0].setAllocation(2 + TestData.getRandom().nextInt(100));
        float _effort = WBSPage.calculateEffort(chs[0].getAllocation(), chs[0].getResourceDuration());
        int effort = (int) (round(_effort));
        expectedEfforts.set(0, (float) effort);
        expectedDurations.set(0, chs[0].getResourceDuration());
        expectedAllocations.set(0, chs[0].getAllocation());

        chs[2].setAllocation(2 + TestData.getRandom().nextInt(100));
        chs[2].setEffort(2 + TestData.getRandom().nextInt(100));
        float _duration = WBSPage.calculateDuration(chs[2].getEffort(), chs[2].getAllocation());
        int duration = (int) (round(_duration));
        expectedDurations.set(2, duration);
        expectedAllocations.set(2, chs[2].getAllocation());
        expectedEfforts.set(2, chs[2].getEffort());

        chs[4].setResourceDuration(2 + TestData.getRandom().nextInt(100));
        chs[4].setEffort(2 + TestData.getRandom().nextInt(100));
        float _allocation = WBSPage.calculateAllocation(chs[4].getEffort(), chs[4].getResourceDuration());
        float allocation = (int) (round(_allocation));
        expectedAllocations.set(4, allocation);
        expectedDurations.set(4, chs[4].getResourceDuration());
        expectedEfforts.set(4, chs[4].getEffort());

        PSLogger.info("Test set different dae");
        popup.setChildWorks(chs);

        validateDAE(popup, chs, expectedDurations, expectedAllocations, expectedEfforts);

        Work[] chs2 = new Work[]{
                new Work("2a"),
                new Work("4a"),
        };
        chs2[0].setAllocation(2 + TestData.getRandom().nextInt(100));
        chs2[1].setAllocation(2 + TestData.getRandom().nextInt(100));
        expectedAllocations.set(1, chs2[0].getAllocation());
        expectedAllocations.set(3, chs2[1].getAllocation());
        for (int i = 0; i < chs.length; i++) {
            int d = (int) round(WBSPage.calculateDuration(expectedEfforts.get(i), expectedAllocations.get(i)));
            expectedDurations.set(i, d);
        }
        popup.makeDurationCalculated();
        popup.setChildWork(chs2[0], 2);
        popup.setChildWork(chs2[1], 4);
        validateDAE(popup, chs, expectedDurations, expectedAllocations, expectedEfforts);

        popup.close();

    }

    private static double round(float v) {
        return TestSession.getAppVersion().verGreaterThan(PowerSteeringVersions._12_1) ? Math.round(v) : Math.ceil(v);
    }

    private void validateDAE(WBSPage.AddSubWorkDialog popup,
                             Work[] chs,
                             List<Integer> expectedDurations,
                             List<Float> expectedAllocations,
                             List<Float> expectedEfforts) {
        PSLogger.save("before validating");
        List<Integer> durations = popup.getDuration(chs);
        List<Float> allocations = popup.getAllocation(chs);
        List<Float> efforts = popup.getEffort(chs);

        PSLogger.info("From page : " + durations + " " + allocations + " " + efforts);
        PSLogger.info("Expected : " + expectedDurations + " " + expectedAllocations + " " + expectedEfforts);
        assertDAE(durations, expectedDurations, "Incorrect list of durations");
        assertDAE(efforts, expectedEfforts, "Incorrect list of efforts");
        assertDAE(allocations, expectedAllocations, "Incorrect list of allocations");
    }

    private void assertDAE(List actual, List expected, String msg) {
        try {
            Assert.assertEquals(actual, expected, msg);
        } catch (AssertionError ae) {
            if (actual == null || expected == null) throw ae;
            if (actual.size() != expected.size()) throw ae;
            List _actual = new ArrayList();
            List _expected = new ArrayList();
            for (int i = 0; i < actual.size(); i++) {
                if (!testDAE((Number) actual.get(i), (Number) expected.get(i))) {
                    _actual.add(actual.get(i));
                    _expected.add(expected.get(i));
                }
            }
            if (_actual.isEmpty()) throw ae;
            Assert.fail(msg + ": actual: " + _actual + ", expected: " + _expected);
        }
    }

    private boolean testDAE(Number actual, Number expected) {
        return testDAE(actual, expected, 1);
    }

    private boolean testDAE(Number actual, Number expected, int diff) {
        return Math.abs(actual.floatValue() - expected.floatValue()) <= diff;
    }
}
