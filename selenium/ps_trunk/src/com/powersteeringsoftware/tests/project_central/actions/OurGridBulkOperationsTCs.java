package com.powersteeringsoftware.tests.project_central.actions;

import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.elements.SingleStatusSelector;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.Currency;
import com.powersteeringsoftware.libs.objects.*;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.resources.ResourcePool;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.tests.project_central.TestData;
import org.testng.Assert;

import java.util.*;

import static com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 18.08.2010
 * Time: 18:49:15
 */
public class OurGridBulkOperationsTCs {

    public void testSetStatusesUsingBulkEditOptionsBlock(Work root, List<Work.Status> rStatuses, Work... works) {
        WBSPage pc = WorkManager.openWBS(root);

        for (Work work : works) {
            rStatuses.remove(work.getStatus());
        }
        Work.Status toTest1 = null;
        Work.Status toTest2 = null;
        for (Work.Status st : rStatuses) {
            if (!st.isActive()) continue;
            if (toTest1 == null) {
                toTest1 = st;
                continue;
            }
            toTest2 = st;
            break;
        }

        pc.getGrid().selectWorks(works);

        PSLogger.info("Status to test: " + toTest1);
        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();

        bulk.setStatus(toTest1.getValue());
        bulk.update();

        for (Work work : works) {
            String statusFromPage = pc.getGrid().getStatus(work);
            Assert.assertEquals(statusFromPage, toTest1.getValue(),
                    "Incorrect status after updating: " + statusFromPage);
        }

        PSLogger.info("Status to test: " + toTest2);
        bulk = pc.openBulkEditBlock();

        bulk.setStatus(toTest2.getValue());
        bulk.update();
        AuxiliaryTCs.saveAndCheckBugLike73593(pc);
        for (Work work : works) {
            String statusFromPage = pc.getGrid().getStatus(work);
            Assert.assertEquals(statusFromPage, toTest2.getValue(),
                    "Incorrect status after updating and saving : " + statusFromPage + ".");
            work.setStatus(toTest2);
        }

    }


    public void testSetConstraintsUsingBulkEditOptionsBlock(Work work) {
        WBSPage pc = WorkManager.openWBS(work);

        pc.getGrid().selectWorks();
        Work.Constraint initialConstraintType = pc.getGrid().getConstraintType(work);
        PSLogger.info("Constraint from page: " + initialConstraintType);

        for (Work.Constraint constraintTypeToTest : Work.Constraint.values()) {
            PSLogger.info("Set constraint: " + constraintTypeToTest);
            WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();

            bulk.setConstraint(constraintTypeToTest);
            bulk.update();

            Work.Constraint constraintTypeFromPage = pc.getGrid().getConstraintType(work);

            Assert.assertEquals(constraintTypeFromPage, constraintTypeToTest, "Incorrect constraint after setting " + constraintTypeToTest);

            pc.resetArea();
            constraintTypeFromPage = pc.getGrid().getConstraintType(work);
            Assert.assertEquals(initialConstraintType, constraintTypeFromPage,
                    "Incorrect constraint after reset setting " + constraintTypeToTest +
                            ", should be as initial " + initialConstraintType +
                            ", but on page " + constraintTypeFromPage
            );
        }


        Work.Constraint constraintTypeToTest = TestData.getRandomFromArray(Work.Constraint.values());
        PSLogger.info("Set test constraint: " + constraintTypeToTest);
        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();
        bulk.setConstraint(constraintTypeToTest);
        bulk.update();
        if (constraintTypeToTest.equals(initialConstraintType)) {
            if (pc.isSaveAreaEnabled()) {
                PSLogger.knis(75078);
                pc.saveArea();
            }
            //Assert.assertFalse(pc.isSaveAreaEnabled(), "Save is enabled after setting '" + constraintTypeToTest + "' for " + work);
        } else {
            pc.saveArea();
        }
        work.setConstraint(constraintTypeToTest);

        pc = pc.getGrid().openWBSPageByDisplayFromParent(work);
        Work.Constraint constraintTypeFromPage = pc.getGrid().getConstraintType(work);
        Assert.assertEquals(constraintTypeFromPage, constraintTypeToTest,
                "Incorrect constraint after saving setting " + constraintTypeToTest +
                        ", should be " + constraintTypeFromPage
        );
    }

    public void testSetStartEndDatesUsingBulkEditOptionsBlock(Work root, Work work) {
        int numDays = 3 * 365;

        WBSPage pc = WorkManager.openWBS(root);

        pc.getGrid().selectWorks(work);
        Work.Constraint constraintType = Work.Constraint.FD; // should be fixed days

        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();
        bulk.setConstraint(constraintType);
        String end = TestData.getRandomDate(numDays);
        String start = TestData.getRandomDate(-numDays);
        PSLogger.info("To set: " + start + ", " + end);
        bulk.setStartDate(start, true, true);
        bulk.setEndDate(end, true, false);
        bulk.update();

        String startFromPage = pc.getGrid().getConstraintStartDate(work);
        String endFromPage = pc.getGrid().getConstraintEndDate(work);
        List<String> messages = pc.getErrorMessagesFromTop();

        Work.Constraint constraintTypeFromPage = pc.getGrid().getConstraintType(work);
        if (messages.size() != 0) // todo: assert
            PSLogger.warn(messages);
        Assert.assertEquals(constraintTypeFromPage, constraintType, "Incorrect constraint type in grid");

        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4)) {//TODO: for 10.0 see 81295, 82059
            start = root.getCalendar().set(start).nextWorkDate(true).toString();
            end = root.getCalendar().set(end).nextWorkDate(false).toString();
        }
        PSLogger.info("Expected: " + start + ", " + end);
        PSLogger.info("From page: " + startFromPage + ", " + endFromPage + "; " + constraintTypeFromPage);
        Assert.assertEquals(startFromPage, start, "Incorrect start date in grid");
        Assert.assertEquals(endFromPage, end, "Incorrect end date in grid");
        pc.resetArea();
    }

    public void testSetPercentageUsingBulkEditOptionsBlock(Work work, List<String> testData) {
        WBSPage pc = WorkManager.openWBS(work);

        pc.getGrid().selectWorks(work);

        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();

        Input in = bulk.getPercentCompleteInput();

        for (String test : testData) {
            String txt = in.getValue();
            Assert.assertTrue(txt.isEmpty(), "There is some value in percent input : '" + txt + "'");
            PSLogger.info("Test value " + test + " for percent complete input in bulk edit block");
            in.type(test);
            boolean fromPageValidationResult = !in.isWrongInput();
            boolean expectedValidationResult = test.matches(in.getRegExp()) &&
                    Integer.valueOf(test) <= Integer.valueOf(in.getMax()) &&
                    Integer.valueOf(test) >= Integer.valueOf(in.getMin());
            Assert.assertEquals(fromPageValidationResult, expectedValidationResult,
                    expectedValidationResult ? "input is wrong" : "should be validation error for input");
            bulk.cancel();
            bulk = pc.openBulkEditBlock();
        }

        String initialStatusFromPage = pc.getGrid().getStatus(work);
        String initialPercentsFromPage = pc.getGrid().getPercentComplete(work);

        String test = testData.get(testData.size() - 1);
        PSLogger.info("Set percentage " + test);
        in.type(test);
        bulk.update();

        String percentsFromPage = pc.getGrid().getPercentComplete(work);
        PSLogger.info("Percentage from grid: " + percentsFromPage);
        Assert.assertEquals(percentsFromPage, test, "Incorrect percentage in grid");

        String statusFromPage = pc.getGrid().getStatus(work);
        PSLogger.info("Status from grid: " + statusFromPage);

        Assert.assertTrue(Work.Status.get(statusFromPage).isActive(), "Status is not active : " + statusFromPage);

        //Assert.assertEquals(statusFromPage, Work.Status.ON_TRACK.getValue(), "Incorrect status in grid after setting percentage, should be " + Work.Status.ON_TRACK);

        pc.resetArea();
        statusFromPage = pc.getGrid().getStatus(work);
        percentsFromPage = pc.getGrid().getPercentComplete(work);
        Assert.assertEquals(percentsFromPage, initialPercentsFromPage,
                "Incorrect percentage in grid after reset");
        Assert.assertEquals(statusFromPage, initialStatusFromPage,
                "Incorrect status in grid after setting percentage and reset, should be " +
                        initialStatusFromPage
        );
        // todo: should we checks saving?
    }

    public void testSetDependencyUsingBulkEditOptionsBlockAndReset(Work root, Work work, Work... works) {
        PSLogger.info("Test set dependencies");
        WBSPage pc = WorkManager.openWBS(root);

        pc.getGrid().selectWorks(work);
        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();

        List<WorkDependency> deps = getRandomDependencies(works);
        bulk.setDependency(deps);
        List<WorkDependency> fromBulk = bulk.getDependencyAsList();
        Collections.sort(fromBulk);
        Collections.sort(deps);
        Assert.assertEquals(fromBulk, deps, "Incorrect dependencies in bulk");
        bulk.cancel();

        bulk = pc.openBulkEditBlock();
        deps = getRandomDependencies(works);
        bulk.setDependency(deps);
        bulk.update();
        List<WorkDependency> fromGrid = pc.getGrid().getDependencyAsList(work);
        PSLogger.info("From page : " + fromGrid);
        Collections.sort(deps);
        Assert.assertEquals(fromGrid, deps, "Incorrect deps in grid after update, expected : " + deps);

        /*
        pc.saveArea();
        fromGrid = pc.getGrid().getDependency(work);
        dependencyAssertEquals(deps, fromGrid, "Incorrect deps in grid after update and save");
        */
        PSLogger.info("Test reset");
        pc.resetArea();
        fromGrid = pc.getGrid().getDependencyAsList(work);
        Assert.assertTrue(fromGrid.isEmpty(),
                "Incorrect deps in grid after update and resetting. should be nothing");
    }

    /**
     * WARNING! by some reason this testcase doesn't work in null-session (ff)
     *
     * @param work1 - first work
     * @param work2 - second work
     */
    public void testErrorDependencyUsingBulkEditOptionsBlockAndSave(Work root, Work work1, Work work2) {
        PSLogger.info("Test errors while set dependencies");
        WorkDependency.Type[] types = WorkDependency.Type.values();
        WBSPage pc = WorkManager.openWBS(root);

        PSLogger.info("Test error 'a parent cannot be a predecessor'");
        pc.getGrid().selectWorks(work1);
        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();
        WorkDependency depParent = new WorkDependency(work1.getParent(),
                TestData.getRandomFromArray(types), 0);
        bulk.setDependency(depParent);
        bulk.update();
        List<String> mes = pc.getErrorMessagesFromTop();
        PSLogger.info(mes);
        Assert.assertFalse(mes.size() == 0, "There are not error messages on page");
        String fromGrid1 = pc.getGrid().getDependency(work1);
        Assert.assertTrue(fromGrid1.isEmpty(), "There is dependency for work " + work1.getFullName() +
                " after setting parent");

        PSLogger.info("Test error 'an object cannot be its own predecessor'");
        pc.getGrid().selectWorks(work2);
        bulk = pc.openBulkEditBlock();
        WorkDependency dep = new WorkDependency(work2,
                TestData.getRandomFromArray(types), 0);
        bulk.setDependency(dep);
        bulk.update();
        mes = pc.getErrorMessagesFromTop();
        PSLogger.info(mes);
        Assert.assertFalse(mes.size() == 0, "There are not error messages on page");
        fromGrid1 = pc.getGrid().getDependency(work1);
        String fromGrid2 = pc.getGrid().getDependency(work2);

        Assert.assertTrue(fromGrid2.isEmpty(), "There is dependency for work " + work2.getFullName());
        if (LocalServerUtils.getLocalServer().isSystemSession()) {
            PSLogger.skip("Skip saving (session is null), reset");
            pc.resetArea();
            return;
        }
        // by some reason this code doesn't work in null-session (ff)
        // todo: investigate...
        Assert.assertEquals(fromGrid1, dep.toString(), "Incorrect dependency for work " + work1.getFullName());
        /*
        PSLogger.info("Test reset");
        pc.resetArea();
        for (ChildWork work : new ChildWork[]{work1, work2}) {
            String fromGrid = pc.getGrid().getDependency(work);
            Assert.assertTrue(fromGrid.isEmpty(), "There is dependency for work " + work.getFullName() +
                    " after resetting");
        }
        */

        PSLogger.info("Test saving");
        pc.saveArea();

        fromGrid1 = pc.getGrid().getDependency(work1);
        fromGrid2 = pc.getGrid().getDependency(work2);
        Assert.assertTrue(fromGrid2.isEmpty(), "There is dependency for work " + work2.getFullName() +
                ", but should be nothing");
        Assert.assertEquals(fromGrid1, dep.toString(), "Incorrect dependency for work " + work1.getFullName() +
                " after saving");
    }

    private static List<WorkDependency> getRandomDependencies(Work... works) {
        int maxLag = 100;
        WorkDependency.Type[] deps = WorkDependency.Type.values();
        List<WorkDependency> res = new ArrayList<WorkDependency>();
        int index1 = TestData.getRandom().nextInt(works.length);
        int index2 = TestData.getRandom().nextInt(works.length);
        boolean howToSet = false; //TestData.getRandom().nextBoolean();
        for (int i = 0; i < works.length; i++) {
            int lag = i == index2 ? 0 : TestData.getRandom().nextInt(maxLag) - TestData.getRandom().nextInt(maxLag);
            WorkDependency.Type dep = TestData.getRandomFromArray(deps);
            WorkDependency toAdd = new WorkDependency(works[i], dep, lag);
            res.add(toAdd);
            if (i != index1) {
                toAdd.setHowToSet(howToSet);
                //howToSet = !howToSet;
            }
        }
        return res;
    }

    public void testSetPriorityUsingBulkEditOptionsBlock(Work parent) {
        testSetSomeFieldUsingBulkEditOptionsBlock(parent, OPTIONS_BLOCK_COLUMNS_PROJECT_PRIORITY,
                GRID_TABLE_PRIORITY, BULK_EDIT_CONTROL_PRIORITY_LABEL);
    }

    public void testSetStatusReportingUsingBulkEditOptionsBlock(Work parent) {
        testSetSomeFieldUsingBulkEditOptionsBlock(parent, OPTIONS_BLOCK_COLUMNS_CONTROLS_STATUS_REPORTING,
                GRID_TABLE_STATUS_REPORTING, BULK_EDIT_CONTROL_STATUS_REPORTING_LABEL);
    }

    public void testSetCurrencyUsingBulkEditOptionsBlock(Work parent, List<Currency> allCurrency) {
        WBSPage pc = checkSpecifiedOptionAndGetWBSPage(parent, false, OPTIONS_BLOCK_COLUMNS_CONTROLS_CURRENCY);
        List<Work> works = parent.getChildren();
        List<Work> all = new ArrayList<Work>(works);
        all.add(0, parent);
        pc.getGrid().selectWorks(works);
        List<Currency> toReset = new ArrayList<Currency>(allCurrency);
        toReset.remove(parent.getCurrency());
        Currency toSave = PSTestData.getRandomFromList(toReset);
        PSLogger.info("Currency to save " + toSave);
        toReset.remove(toSave);
        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();
        bulk.getSingleStatusSelector(BULK_EDIT_CONTROL_CURRENCY_LABEL).setLabel(toSave.getDescription());
        bulk.update();
        for (Work work : works) {
            Currency fromPage = new Currency(pc.getGrid().getSingleStatusSelector(GRID_TABLE_CURRENCY, work).getContent());
            Assert.assertEquals(fromPage, toSave, "incorrect currency after update for " + work);
        }
        pc.saveArea();
        for (Work work : works) {
            work.setCurrency(toSave);
        }
        for (Work work : all) {
            Currency fromPage = new Currency(pc.getGrid().getSingleStatusSelector(GRID_TABLE_CURRENCY, work).getContent());
            Assert.assertEquals(fromPage, work.getCurrency(), "incorrect currency after save for " + work);
        }
        pc.getGrid().unSelectWorks(works);
        PSLogger.info("Currency to reset " + toReset);
        for (Currency c : toReset) {
            Work work = PSTestData.getRandomFromList(works);
            pc.getGrid().selectWorks(work);
            bulk = pc.openBulkEditBlock();
            bulk.getSingleStatusSelector(BULK_EDIT_CONTROL_CURRENCY_LABEL).setLabel(c.getDescription());
            bulk.update();
            for (Work w : all) {
                Currency actual = new Currency(pc.getGrid().getSingleStatusSelector(GRID_TABLE_CURRENCY, w).getContent());
                Currency expected;
                if (w.equals(work)) {
                    expected = c;
                } else {
                    expected = w.getCurrency();
                }
                Assert.assertEquals(actual, expected, "incorrect currency after update for " + w);
            }
            pc.resetArea();
            pc.getGrid().unSelectWorks(work);
        }
    }

    private void testSetSomeFieldUsingBulkEditOptionsBlock(Work parent,
                                                           WBSEPageLocators fieldId,
                                                           ILocatorable gridLocator,
                                                           ILocatorable bulkLocator) {
        WBSPage pc = checkSpecifiedOptionAndGetWBSPage(parent, false, fieldId);
        pc.getGrid().selectWorks();
        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();
        List<String> list = getLabelsFromBulkEditBlock(pc, bulk, bulkLocator);
        Assert.assertFalse(list.isEmpty(), "Empty list of " + fieldId.getLocator());
        TestData.mixList(list);

        testSetSomeFieldUsingBulkEditOptionsBlock(list,
                new ArrayList<String>(list),
                fieldId.getLocator(),
                parent,
                pc,
                bulk,
                gridLocator,
                bulkLocator);
    }

    private List<String> getLabelsFromBulkEditBlock(WBSPage pc,
                                                    WBSPage.BulkEditBlock bulk,
                                                    ILocatorable bulkLocator) {
        SingleStatusSelector selector = bulk.getSingleStatusSelector(bulkLocator);
        selector.openPopup();
        List<String> list = selector.getAllLabels();
        list.remove("");
        bulk.cancel(); // to close combobox
        bulk = pc.openBulkEditBlock();
        return list;
    }

    private void testSetSomeFieldUsingBulkEditOptionsBlock(List<String> toSetList,
                                                           List<String> expectedList,
                                                           String fieldName,
                                                           Work parent,
                                                           WBSPage pc,
                                                           WBSPage.BulkEditBlock bulk,
                                                           ILocatorable gridLocator,
                                                           ILocatorable bulkLocator) {
        PSLogger.info(fieldName + " to test " + toSetList);
        PSLogger.info(fieldName + " expected " + expectedList);

        String initialFromPage = pc.getGrid().getSingleStatusSelector(gridLocator, parent).
                getContent();
        int index = expectedList.indexOf(initialFromPage);
        if (index != -1) {
            toSetList.remove(index);
            expectedList.remove(index);
        }

        List<Work> works = new ArrayList<Work>(parent.getChildren());
        works.add(parent);
        int resetIndex = TestData.getRandom().nextInt(toSetList.size() - 1);
        for (int i = 0; i < toSetList.size(); i++) {
            PSLogger.info("Test " + fieldName + " '" + toSetList.get(i) + "'");
            bulk.getSingleStatusSelector(bulkLocator).setLabel(toSetList.get(i));
            bulk.update();
            if (i == toSetList.size() - 1) {
                PSLogger.info("Test save, " + toSetList.get(i));
                pc.saveArea();
            }
            for (Work work : works) {
                String fromPage = pc.getGrid().getSingleStatusSelector(gridLocator, work).getContent();
                Assert.assertEquals(fromPage, expectedList.get(i), "incorrect " + fieldName + " for " + work);
            }
            if (i == resetIndex) {
                PSLogger.info("Test reset");
                pc.resetArea();
                for (Work work : works) {
                    String fromPage = pc.getGrid().getSingleStatusSelector(gridLocator, work).getContent();
                    Assert.assertEquals(fromPage, initialFromPage, "not empty " + fieldName +
                            " for " + work + " after resetting");
                }
            }
            bulk = pc.openBulkEditBlock();
        }

    }

    private String getRandomDate(int d) { // workaround: do not set to weekends due to #83478
        return TestData.getRandomPSDate(d).nextWorkDate(true).toString();
    }

    public void testSetActualDatesUsingBulkEditOptionsBlock(Work work) {
        int numDays = 3 * 365;
        WBSPage pc = checkSpecifiedOptionAndGetWBSPage(work, false, OPTIONS_BLOCK_COLUMNS_PROJECT_STATUS, OPTIONS_BLOCK_COLUMNS_DATES_ACTUAL);
        pc.hideGantt();

        String sInitStatus = pc.getGrid().getStatus(work);
        String initActualStart = pc.getGrid().getActualStartDate(work);
        String initActualEnd = pc.getGrid().getActualEndDate(work);
        PSLogger.info("Initial state : >" + sInitStatus + "," + initActualStart + "," + initActualEnd + "<");
        if (!Work.Status.NOT_STARTED.equals(sInitStatus) && !Work.Status.PROPOSED.equals(sInitStatus)) {
            PSLogger.info("Change status to " + Work.Status.PROPOSED); // hotfix
            pc.getGrid().setStatus(work, Work.Status.PROPOSED);
            pc.saveArea();
            work.setStatus(Work.Status.PROPOSED);
            sInitStatus = Work.Status.PROPOSED.getValue();
        }
        pc.getGrid().selectWorks();
        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();
        String start = getRandomDate(-numDays);
        PSLogger.info("Set actual start date : " + start);
        bulk.setActualStartDate(start,
                TestData.getRandom().nextBoolean() | TestData.getRandom().nextBoolean(),
                TestData.getRandom().nextBoolean());
        bulk.update();

        String startFromPage = pc.getGrid().getActualStartDate(work);
        String endFromPage = pc.getGrid().getActualEndDate(work);
        String statusFromPage = pc.getGrid().getStatus(work);
        PSLogger.info("From page : " + statusFromPage + " " + startFromPage + " " + endFromPage);
        Assert.assertEquals(startFromPage, start, "Incorrect actual start date");
        Assert.assertEquals(statusFromPage, Work.Status.ON_TRACK.getValue(), "Incorrect status");
        Assert.assertTrue(endFromPage.isEmpty(), "Incorrect actual end date");
        pc.resetArea();
        statusFromPage = pc.getGrid().getStatus(work);
        Assert.assertEquals(statusFromPage, sInitStatus, "Incorrect status after resetting");

        start = getRandomDate(-numDays);
        String end = getRandomDate(numDays);
        PSLogger.info("Set actual start and end dates : " + start + " " + end);
        pc.openBulkEditBlock();
        bulk.setActualEndDate(end,
                TestData.getRandom().nextBoolean() | TestData.getRandom().nextBoolean(),
                TestData.getRandom().nextBoolean());
        bulk.setActualStartDate(start,
                TestData.getRandom().nextBoolean() | TestData.getRandom().nextBoolean(),
                TestData.getRandom().nextBoolean());
        bulk.update();

        startFromPage = pc.getGrid().getActualStartDate(work);
        endFromPage = pc.getGrid().getActualEndDate(work);
        statusFromPage = pc.getGrid().getStatus(work);
        PSLogger.info("From page : " + statusFromPage + " " + startFromPage + " " + endFromPage);
        Assert.assertEquals(startFromPage, start, "Incorrect actual start date");
        Assert.assertEquals(endFromPage, end, "Incorrect actual end date");
        Assert.assertEquals(statusFromPage, Work.Status.COMPLETED.getValue(), "Incorrect status");
        pc.resetArea();
        statusFromPage = pc.getGrid().getStatus(work);
        startFromPage = pc.getGrid().getActualStartDate(work);
        endFromPage = pc.getGrid().getActualEndDate(work);
        Assert.assertTrue(startFromPage.isEmpty(), "Incorrect actual start date");
        Assert.assertTrue(endFromPage.isEmpty(), "Incorrect actual end date");
        Assert.assertEquals(statusFromPage, sInitStatus, "Incorrect status after resetting");

    }

    public void testSetOwnerUsingBulkEditOptionsBlock(Work parent, User user1, User user2) {
        List<Work> works = new ArrayList<Work>(parent.getChildren());
        works.add(parent);

        List<Work> works1 = new ArrayList<Work>(works);

        WBSPage pc = checkSpecifiedOptionAndGetWBSPage(parent, false, OPTIONS_BLOCK_COLUMNS_PROJECT_OWNER);
        if (!pc.getGrid().isCellEditable(GRID_TABLE_OWNER, parent)) { // debug hotfix for 9.0 (like for tags)
            if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_1)) {
                Assert.fail("Column Owner is not editable");
            } else {
                PSLogger.warn("Column Owner is not editable");
                pc = checkSpecifiedOptionAndGetWBSPage(parent, false, OPTIONS_BLOCK_COLUMNS_PROJECT_OWNER);
            }
        }
        pc.getGrid().selectWorks();
        PSLogger.info("Set owner " + user1.getFullName() + " for all works: " + works1);
        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();
        bulk.setOwner(user1.getFullName());
        bulk.update();
        for (Work work : works1) {
            String fromPage = pc.getGrid().getOwner(work);
            PSLogger.info("Owner for " + work.getFullName() + " is " + fromPage);
            Assert.assertEquals(fromPage, user1.getFullName(), "incorrect, for work " + user1.getFullName());
        }

        TestData.mixList(works1);
        Work[] works2 = new Work[]{works1.remove(0), works1.remove(0)};

        PSLogger.info("Set owner " + user2.getFullName() + " for works " + Arrays.asList(works2) + "; " +
                "do not change owner for " + works1);
        pc.getGrid().unSelectWorks();
        pc.getGrid().selectWorks(works2);
        bulk = pc.openBulkEditBlock();
        bulk.setOwner(user2.getFullName());
        bulk.update();
        PSLogger.info("Checking for " + user1.getFullName());
        for (Work work : works1) {
            String fromPage = pc.getGrid().getOwner(work);
            PSLogger.info("Owner for " + work.getFullName() + " is " + fromPage);
            Assert.assertEquals(fromPage, user1.getFullName(),
                    "incorrect (" + work.getFullName() + ")");
        }
        PSLogger.info("Checking for " + user2.getFullName());
        for (Work work : works2) {
            String fromPage = pc.getGrid().getOwner(work);
            PSLogger.info("Owner for " + work.getFullName() + " is " + fromPage);
            Assert.assertEquals(fromPage, user2.getFullName(),
                    "incorrect (" + work.getFullName() + ")");
        }
        pc.resetArea();

        for (Work work : works) {
            String fromPage = pc.getGrid().getOwner(work);
            Assert.assertEquals(fromPage, work.getOwner().getFullName(), "incorrect owner for " + work.getFullName() +
                    " after resetting");
        }
    }

    public void testSetDurationAllocationEffortUsingBulkEditOptionsBlock(Work work) {

        WBSPage pc = checkSpecifiedOptionAndGetWBSPage(work, true, OPTIONS_BLOCK_COLUMNS_RESOURCE);
        pc.hideGantt();
        /*
        if (!pc.isSchedulerIsUpToDate()) {
            PSLogger.info("Scheduler is not up-to-date, run it");
            pc.runScheduler();
        }
        int duration = Integer.valueOf(pc.getGrid().getResoueceDuration(work).replace("d", ""));
        int expected = TestData.getCalendar().isWeekend() ? 0 : 1;
        Assert.assertEquals(duration, expected, "Incorrect default duration for " + work);
        */
        //todo: this is not necessary:
        /*if (expected == 0) {
            pc.getGrid().setResourceDuration(work, 1);
        }*/
        // by default allocation is calculated:
        Work.CalculatedField calcField = pc.getGrid().getCalculatedField(work);
        Assert.assertNotNull(calcField, "Can't find any calculated field");
        // todo: why?
        Assert.assertTrue(calcField.equals(Work.CalculatedField.ALLOCATION), "Allocation is not calculated before tc");

        pc.getGrid().selectWorks();

        float allocation = getRandomIntForTestDurationAllocationEffort();
        float effort = getRandomIntForTestDurationAllocationEffort();
        String expectedDuration = setEffortAllocationDuration(pc, effort, allocation, null);
        String actualDuration = pc.getGrid().getResourceDurationAsString(work);
        Assert.assertEquals(actualDuration, expectedDuration, "Incorrect duration after setting a=" + allocation + ",e=" + effort);
        calcField = pc.getGrid().getCalculatedField(work);
        Assert.assertTrue(Work.CalculatedField.DURATION.equals(calcField), "Duration is not calculated");

        effort = getRandomIntForTestDurationAllocationEffort();
        int duration = getRandomIntForTestDurationAllocationEffort();
        String expectedAllocation = setEffortAllocationDuration(pc, effort, null, duration);
        String actualAllocation = pc.getGrid().getAllocation(work);
        Assert.assertEquals(actualAllocation, expectedAllocation, "Incorrect allocation after setting e=" + effort + ",d=" + duration);
        calcField = pc.getGrid().getCalculatedField(work);
        Assert.assertTrue(Work.CalculatedField.ALLOCATION.equals(calcField), "Allocation is not calculated");

        allocation = getRandomIntForTestDurationAllocationEffort();
        duration = getRandomIntForTestDurationAllocationEffort();
        String expectedEffort = setEffortAllocationDuration(pc, null, allocation, duration);
        String actualEffort = pc.getGrid().getEffort(work);
        Assert.assertEquals(actualEffort, expectedEffort, "Incorrect effort after setting a=" + allocation + ",d=" + duration);
        calcField = pc.getGrid().getCalculatedField(work);
        Assert.assertTrue(Work.CalculatedField.EFFORT.equals(calcField), "Effort is not calculated");


        pc.resetArea();
        // by default allocation is calculated:
        calcField = pc.getGrid().getCalculatedField(work);
        Assert.assertTrue(Work.CalculatedField.ALLOCATION.equals(calcField), "Allocation is not calculated after resetting");
        duration = pc.getGrid().getResourceDuration(work);
        PSLogger.info("Duration from page: " + duration);
        effort = getRandomIntForTestDurationAllocationEffort();
        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();
        bulk.getEffortInput().type(String.valueOf(effort));
        bulk.update();
        actualAllocation = pc.getGrid().getAllocation(work);
        expectedAllocation = WBSPage.calculateEffortAllocationDuration(effort, null, duration);
        Assert.assertEquals(actualAllocation, expectedAllocation,
                "Incorrect allocation after setting effort " + effort);
        pc.saveArea();
        actualAllocation = pc.getGrid().getAllocation(work);
        Assert.assertEquals(actualAllocation, expectedAllocation,
                "Incorrect allocation after setting effort " + effort + " and saving area");

    }

    private static int getRandomIntForTestDurationAllocationEffort() {
        int max = 100;
        int min = 1;
        // in case of 0 there will be error like 'Can not set 0 to Effort column.' 
        return TestData.getRandom().nextInt(max - min) + min;
    }

    public void testSetRoleAndResourcePoolUsingBulkEditOptionsBlock(Work parent, ResourcePool pool, List<Role> roles) {

        List<Work> works = new ArrayList<Work>(parent.getChildren(true, true));

        WBSPage pc = checkSpecifiedOptionAndGetWBSPage(parent, true, OPTIONS_BLOCK_COLUMNS_RESOURCE);
        pc.getGrid().selectWorks();
        WBSPage.BulkEditBlock bulk;

        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_1)) { // due #75154 only for 90
            PSLogger.info("Set role " + roles.get(0) + " and pool " + pool +
                    " for works " + works);
            bulk = pc.openBulkEditBlock();
            bulk.setRole(roles.get(0).getName());
            bulk.setResourcePool(pool.getName());
            bulk.update();
            List<String> mes = pc.getErrorMessagesFromTop();
            PSLogger.info(mes);
            Assert.assertTrue(mes.size() == 2, "Seems incorrect errors on page (should be errors for role and pool)");
            for (Work work : works) {
                checkRoleAndPoolForWorkAndAssert(pc, work, work.getResource().getRole(), work.getResource().getPool());
            }
            checkRoleAndPoolForWorkAndAssert(pc, parent, roles.get(0), pool);

            pc.resetArea();
            pc.setIncludeIndependentWork();
        }
        works.add(parent);
        bulk = pc.openBulkEditBlock();
        bulk.setRole(roles.get(1).getName());
        bulk.setResourcePool(pool.getName());
        bulk.update();

        for (Work work : works) {
            checkRoleAndPoolForWorkAndAssert(pc, work,
                    work.hasSplitResources() ? null : roles.get(1),
                    work.hasSplitResources() ? null : pool);
        }
        pc.resetArea();
        for (Work work : works) {
            checkRoleAndPoolForWorkAndAssert(pc, work,
                    work.getResource(true).getRole(),
                    work.getResource(true).getPool());
        }
    }

    public void testSetTagsUsingBulkEditOptionsBlockAndReset(Work parent, PSTag tg1, PSTag tg2) {
        WBSPage pc = checkSpecifiedOptionAndGetWBSPage(parent, false, OPTIONS_BLOCK_COLUMNS_TAGS);
        List<Work> works = new ArrayList<Work>(parent.getChildren());
        works.add(parent);
        List<Work> worksWithoutTags = new ArrayList<Work>();
        List<Work> worksWithTags = new ArrayList<Work>();
        for (Work work : works) {
            List<PSTag> tags = pc.getGrid().getTags(work, tg1, tg2);
            if (tags.isEmpty()) worksWithoutTags.add(work);
            else worksWithTags.add(work);
        }
        PSLogger.info("Works with tags: " + worksWithTags);
        PSLogger.info("Works without tags: " + worksWithoutTags);
        pc.getGrid().selectWorks(worksWithoutTags);

        List<PSTag> expected = new ArrayList<PSTag>(Arrays.asList(
                tg1.getChilds().get(1),
                tg1.getChilds().get(2),
                tg2.getChilds().get(1)));
        PSLogger.info("Set tags " + expected + " for works " + worksWithoutTags);
        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();
        bulk.setTag(expected.get(0), expected.get(1));
        bulk.setTag(expected.get(2));
        bulk.update();
        for (Work work : worksWithoutTags) {
            List<PSTag> fromPage = pc.getGrid().getTags(work, tg1, tg2);
            PSLogger.info("tags for work " + work + " from page: " + fromPage);
            Assert.assertEquals(fromPage, expected, "Incorrect list of tags");
        }
        for (Work work : worksWithTags) {
            List<PSTag> fromPage = pc.getGrid().getTags(work, tg1, tg2);
            PSLogger.info("tags for work " + work + " from page: " + fromPage);
            Assert.assertEquals(fromPage, work.getTags(), "Incorrect list of tags");
        }
        pc.resetArea();
        for (Work work : works) {
            List<PSTag> fromPage = pc.getGrid().getTags(work, tg1, tg2);
            PSLogger.info("tags for work " + work + " from page: " + fromPage);
            Assert.assertEquals(fromPage, work.getTags(), "Incorrect list of tags after resetting");
        }

    }

    public void testSetManualSchedulingUsingBulkEditOptionsBlock(Work parent) {
        testSetSomeFieldWithInheritUsingBulkEditOptionsBlock(
                parent,
                parent.getChildren(),
                OPTIONS_BLOCK_COLUMNS_CONTROLS_MANUAL_SCHEDULING,
                "Yes",
                "No",
                BULK_EDIT_CONTROL_MANUAL_SCHEDULING_LABEL,
                GRID_TABLE_MANUAL_SCHEDULING,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_CONTROLS,
                BULK_EDIT_CONTROL_INHERIT_CONTROLS_LABEL,
                GRID_TABLE_INHERIT_CONTROLS);
    }

    public void testSetControlCostUsingBulkEditOptionsBlock(Work parent) {
        List<Work> children = new ArrayList<Work>();
        for (Work w : parent.getChildren()) { // get only with costs=off
            if (!w.getControlCost())
                children.add(w);
        }

        testSetSomeFieldWithInheritUsingBulkEditOptionsBlock(
                parent,
                children,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_CONTROL_COST,
                "Yes",
                "No",
                BULK_EDIT_CONTROL_CONTROL_COST_LABEL,
                GRID_TABLE_CONTROL_COST,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_CONTROLS,
                BULK_EDIT_CONTROL_INHERIT_CONTROLS_LABEL,
                GRID_TABLE_INHERIT_CONTROLS);
    }

    public void testSetCalendarUsingBulkEditOptionsBlock(Work parent, PSCalendar c1, PSCalendar c2) {
        testSetSomeFieldWithInheritUsingBulkEditOptionsBlock(
                parent,
                parent.getChildren(),
                OPTIONS_BLOCK_COLUMNS_CONTROLS_CALENDAR,
                c1.getName(),
                c2.getName(),
                BULK_EDIT_CONTROL_CALENDAR_LABEL,
                GRID_TABLE_CALENDAR,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_CALENDAR,
                BULK_EDIT_CONTROL_INHERIT_CALENDAR_LABEL,
                GRID_TABLE_INHERIT_CALENDAR);
    }

    public void testSetRateTableUsingBulkEditOptionsBlock(Work parent, String tb1, String tb2) {
        testSetSomeFieldWithInheritUsingBulkEditOptionsBlock(
                parent,
                parent.getChildren(),
                OPTIONS_BLOCK_COLUMNS_CONTROLS_RATE_TABLE,
                tb1, tb2,
                BULK_EDIT_CONTROL_RATE_TABLE_LABEL,
                GRID_TABLE_RATE_TABLE,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_RATE_TABLE,
                BULK_EDIT_CONTROL_INHERIT_RATE_TABLE_LABEL,
                GRID_TABLE_INHERIT_RATE_TABLE);
    }

    public void testSetPersonalRateRuleUsingBulkEditOptionsBlock(Work parent) {
        testSetSomeFieldWithInheritUsingBulkEditOptionsBlock(
                parent,
                parent.getChildren(),
                OPTIONS_BLOCK_COLUMNS_CONTROLS_PERSONAL_RATE_RULE,
                "No",
                "Yes",
                BULK_EDIT_CONTROL_PERSONAL_RATE_RULE_LABEL,
                GRID_TABLE_PERSONAL_RATE_RULE,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_PERSONAL_RATE_RULE,
                BULK_EDIT_CONTROL_INHERIT_PERSONAL_RATE_RULE_LABEL,
                GRID_TABLE_INHERIT_PERSONAL_RATE_RULE);
    }

    public void testSetActivityTypesUsingBulkEditOptionsBlock(Work parent, Work child, PSTag tg) {
        List<PSTag> types = tg.getChilds();
        PSTag type1 = types.get(0);
        PSTag type2 = types.get(types.size() - 1);
        Set<String> expectedActivity = new LinkedHashSet<String>();
        expectedActivity.add("");
        List<PSTag> expectedActivityTypes = new ArrayList<PSTag>();
        for (PSTag tag : new PSTag[]{type1, type2}) {
            expectedActivityTypes.add(tag);
            for (PSTag chTag : tag.getChilds()) {
                expectedActivity.add(chTag.getName());
            }
        }

        WBSPage pc = checkSpecifiedOptionAndGetWBSPage(parent, false,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_CONTROL_COST,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_CONTROLS,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_DEFAULT_ACTIVITY,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_DEFAULT_ACTIVITY,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_ACTIVITY_TYPES,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_ALL_ACTIVITIES,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_ACTIVITY_TYPES);
        pc.getGrid().selectWorks(child);

        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();
        for (ILocatorable loc : new ILocatorable[]{
                BULK_EDIT_CONTROL_INHERIT_CONTROLS_LABEL,
                BULK_EDIT_CONTROL_INHERIT_ACTIVITY_TYPES_LABEL,
                BULK_EDIT_CONTROL_ALL_ACTIVITIES_LABEL}) {
            bulk.getSingleStatusSelector(loc).setLabel(CONTROL_NO);
        }
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._8_2)) {
            // for editing default activity in grid:
            bulk.getSingleStatusSelector(BULK_EDIT_CONTROL_INHERIT_DEFAULT_ACTIVITY_LABEL).setLabel(CONTROL_NO);
        }
        bulk.getSingleStatusSelector(BULK_EDIT_CONTROL_CONTROL_COST_LABEL).setLabel(CONTROL_YES);
        bulk.setTag(type1, type2);
        bulk.update();
        List<String> mes = pc.getErrorMessagesFromTop();
        PSLogger.info("Message " + mes);
        Assert.assertTrue(mes.isEmpty(), "There is an error");
        List<PSTag> activityTypesFromPage = pc.getGrid().getTags(child, type1.getParent());
        SingleStatusSelector selector = pc.getGrid().getSingleStatusSelector(GRID_TABLE_DEFAULT_ACTIVITY, child);
        Assert.assertTrue(pc.getGrid().isCellEditable(selector), "Default Activity is not editable");
        selector.openPopup();
        List<String> activityFromPage = selector.getAllLabels();
        PSLogger.info("from page: " + activityFromPage + activityTypesFromPage);
        Assert.assertEquals(activityTypesFromPage, expectedActivityTypes,
                "incorrect activity types size after updating");
        Assert.assertEquals(activityFromPage, expectedActivity,
                "incorrect available default activity after updating");

        pc.resetArea();

    }


    public void testSetDefaultActivitiesUsingBulkEditOptionsBlock(Work parent, Work child, PSTag.Activity tg) {
        List<String> defaultActivities = tg.getValues();

        PSTestData.mixList(defaultActivities);
        PSLogger.info("Test data: " + defaultActivities);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_0)) {
            // according #72888 skip empty data
            defaultActivities.remove("");
        }

        WBSPage pc = checkSpecifiedOptionAndGetWBSPage(parent, false,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_CONTROL_COST,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_CONTROLS,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_DEFAULT_ACTIVITY,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_DEFAULT_ACTIVITY);
        pc.getGrid().selectWorks(child);

        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();
        for (ILocatorable loc : new ILocatorable[]{
                BULK_EDIT_CONTROL_INHERIT_CONTROLS_LABEL,
                BULK_EDIT_CONTROL_INHERIT_DEFAULT_ACTIVITY_LABEL}) {
            bulk.getSingleStatusSelector(loc).setLabel(CONTROL_NO);
        }
        bulk.getSingleStatusSelector(BULK_EDIT_CONTROL_CONTROL_COST_LABEL).setLabel(CONTROL_YES);

        for (String activity : defaultActivities) {
            // first empty label is for resetting in bulk block, second is for resetting in grid. so use setLastLabel method
            bulk.getSingleStatusSelector(BULK_EDIT_CONTROL_DEFAULT_ACTIVITY_LABEL).
                    setLastLabel(activity);
            bulk.update();
            List<String> mes = pc.getErrorMessagesFromTop();
            PSLogger.info("Message " + mes);
            Assert.assertTrue(mes.isEmpty(), "There is an error");
            String fromPage = pc.getGrid().getSingleStatusSelector(GRID_TABLE_DEFAULT_ACTIVITY, child).getContent();
            Assert.assertEquals(fromPage, activity, "Incorrect activity");
            bulk = pc.openBulkEditBlock();
        }
        pc.resetArea();

    }

    public void testSetInheritPermissionsUsingBulkEditOptionsBlock(Work parent, Work... childs) {
        WBSPage pc = checkSpecifiedOptionAndGetWBSPage(parent, false, OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_PERMISSIONS);
        for (Work work : parent.getChildren()) {
            Assert.assertTrue(pc.getGrid().isCellEditable(GRID_TABLE_INHERIT_PERMISSIONS, work),
                    "Cell is not editable for " + work);
        }

        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_0)) {
            Assert.assertFalse(pc.getGrid().isCellEditable(GRID_TABLE_INHERIT_PERMISSIONS, parent),
                    "Cell is editable for " + parent);
        }
        pc.getGrid().selectWorks(childs);

        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();
        bulk.getSingleStatusSelector(BULK_EDIT_CONTROL_INHERIT_PERMISSIONS_LABEL).setLabel(CONTROL_NO);
        bulk.update();
        for (Work work : childs) {
            String fromPage = pc.getGrid().getSingleStatusSelector(GRID_TABLE_INHERIT_PERMISSIONS, work).getContent();
            Assert.assertEquals(fromPage, CONTROL_NO.getLocator(), "Incorrect value from grid");
        }
        Assert.assertTrue(pc.isSaveAreaEnabled(), "Save is not enabled");
        pc.saveArea();

        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_2)) {
            //TODO:
            PSLogger.skip("Can't validate custom permissions from summary page");
        } else {
            SummaryWorkPage sum = pc.openSummaryPage();
            List<String> worksFromPage = sum.getWorksWithCustomPermissions();
            PSLogger.info("Works with custom permissions: " + worksFromPage);
            Assert.assertEquals(worksFromPage.size(), childs.length, "Incorrect list of works with permissions on summary");
            for (Work work : childs) {
                Assert.assertTrue(worksFromPage.contains(work.getName()), "Can't find work " + work +
                        " in list works with custom permissions");
            }
        }

        pc = checkSpecifiedOptionAndGetWBSPage(parent, false, OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_PERMISSIONS);
        pc.getGrid().selectWorks(childs);
        bulk = pc.openBulkEditBlock();
        bulk.getSingleStatusSelector(BULK_EDIT_CONTROL_INHERIT_PERMISSIONS_LABEL).setLabel(CONTROL_YES);
        bulk.update();
        Assert.assertTrue(pc.isSaveAreaEnabled(), "Save is not enabled");
        pc.saveArea();
        for (Work work : childs) {
            String fromPage = pc.getGrid().getSingleStatusSelector(GRID_TABLE_INHERIT_PERMISSIONS, work).getContent();
            Assert.assertEquals(fromPage, CONTROL_YES.getLocator(), "Incorrect value from grid");
        }

        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_2)) {
            //TODO:
            PSLogger.skip("Can't validate custom permissions from summary page");
        } else {
            SummaryWorkPage sum = pc.openSummaryPage();
            List<String> worksFromPage = sum.getWorksWithCustomPermissions();
            PSLogger.info("Works with custom permissions: " + worksFromPage);
            Assert.assertEquals(worksFromPage.size(), 0, "List of works with permissions on summary page is not empty");
        }
    }


    public void testSetPlanResourcesAndIncludeActionItemsUsingBulkEditOptionsBlock(Work parent, Work child) {
        WBSPage pc = checkSpecifiedOptionAndGetWBSPage(parent, false,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_PLAN_RESOURCES,
                OPTIONS_BLOCK_COLUMNS_CONTROLS_INCLUDE_ACTION_ITEMS);
        pc.getGrid().selectWorks(child);
        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();
        bulk.getSingleStatusSelector(BULK_EDIT_CONTROL_PLAN_RESOURCES_LABEL).setLabel(CONTROL_NO);
        bulk.getSingleStatusSelector(BULK_EDIT_CONTROL_INCLUDE_ACTION_ITEMS_LABEL).setLabel(CONTROL_YES);
        bulk.update();
        List<String> mes = pc.getErrorMessagesFromTop();
        PSLogger.info("Message " + mes);
        String expectedMessage = MESSAGE_ERROR_BULK_OPERATION_READ_ONLY_TEMPLATE.
                replace(GRID_TABLE_INCLUDE_ACTION_ITEMS.getLocator()) + child.getGeneralIndex();
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_0) &&
                mes.contains(MESSAGE_BULK_OPERATION.getLocator())) {
            throw new PSKnownIssueException("72792", MESSAGE_BULK_OPERATION.getLocator());
        }
        int expectedErrorsNumber = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3) ? 2 : 1;
        Assert.assertTrue(mes.size() == expectedErrorsNumber && mes.contains(expectedMessage),
                "Incorrect error message, should be " + expectedMessage);
        Assert.assertFalse(pc.getGrid().isCellEditable(GRID_TABLE_INCLUDE_ACTION_ITEMS, child), "Cell {" + GRID_TABLE_INCLUDE_ACTION_ITEMS.getLocator() +
                "} is editable for " + child.getName());

        bulk = pc.openBulkEditBlock();
        bulk.getSingleStatusSelector(BULK_EDIT_CONTROL_PLAN_RESOURCES_LABEL).setLabel(CONTROL_YES);
        bulk.getSingleStatusSelector(BULK_EDIT_CONTROL_INCLUDE_ACTION_ITEMS_LABEL).setLabel(CONTROL_YES);
        bulk.update();

        mes = pc.getErrorMessagesFromTop();
        PSLogger.info("Message " + mes);
        Assert.assertTrue(mes.size() == 0, "There is an error");
        String fromPage = pc.getGrid().getSingleStatusSelector(GRID_TABLE_INCLUDE_ACTION_ITEMS, child).getContent();
        Assert.assertEquals(fromPage, CONTROL_YES.getLocator(), "Incorrect value from grid");

        bulk = pc.openBulkEditBlock();
        bulk.getSingleStatusSelector(BULK_EDIT_CONTROL_PLAN_RESOURCES_LABEL).setLabel(CONTROL_YES);
        bulk.getSingleStatusSelector(BULK_EDIT_CONTROL_INCLUDE_ACTION_ITEMS_LABEL).setLabel(CONTROL_NO);
        bulk.update();

        mes = pc.getErrorMessagesFromTop();
        PSLogger.info("Message " + mes);
        Assert.assertTrue(mes.size() == 0, "There is an error");
        fromPage = pc.getGrid().getSingleStatusSelector(GRID_TABLE_INCLUDE_ACTION_ITEMS, child).getContent();
        Assert.assertEquals(fromPage, CONTROL_NO.getLocator(), "Incorrect value from grid");

        pc.resetArea();
    }


    private void testSetSomeFieldWithInheritUsingBulkEditOptionsBlock(Work parent,
                                                                      List<Work> children,
                                                                      WBSEPageLocators fieldId,
                                                                      String fieldValue,
                                                                      String fieldDefaultValue,
                                                                      ILocatorable fieldBulkLoc,
                                                                      ILocatorable fieldGridLoc,
                                                                      WBSEPageLocators inheritFieldId,
                                                                      ILocatorable inheritFieldBulkLoc,
                                                                      ILocatorable inheritFieldGridLoc) {
        WBSPage pc = checkSpecifiedOptionAndGetWBSPage(parent, false, fieldId, inheritFieldId);
        testSetSomeFieldWithInheritUsingBulkEditOptionsBlock(pc,
                parent,
                children,
                fieldId.getLocator(), fieldValue, fieldDefaultValue,
                fieldBulkLoc, fieldGridLoc,
                inheritFieldId.getLocator(),
                inheritFieldBulkLoc, inheritFieldGridLoc);
    }

    private void testSetSomeFieldWithInheritUsingBulkEditOptionsBlock(WBSPage pc,
                                                                      Work parent,
                                                                      List<Work> children,
                                                                      String fieldName,
                                                                      String fieldValue,
                                                                      String fieldDefaultValue,
                                                                      ILocatorable fieldBulkLoc,
                                                                      ILocatorable fieldGridLoc,
                                                                      String inheritFieldName,
                                                                      ILocatorable inheritFieldBulkLoc,
                                                                      ILocatorable inheritFieldGridLoc) {
        PSLogger.info("Test " + fieldName + " '" + fieldValue + "' with '" + inheritFieldName + "'='No' for parent");
        pc.getGrid().selectWorks(children);
        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();
        bulk.getSingleStatusSelector(fieldBulkLoc).setLabel(fieldValue);
        bulk.update();
        List<String> mes = pc.getErrorMessagesFromTop();
        PSLogger.info("Message " + mes);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_0) &&
                mes.contains(MESSAGE_BULK_OPERATION.getLocator())) {
            throw new PSKnownIssueException("72792", MESSAGE_BULK_OPERATION.getLocator());
        }
        int expectedErrorsNumber = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3) ? 2 : 1;
        Assert.assertEquals(mes.size(), expectedErrorsNumber, "Seems incorrect error from page");
        for (Work work : children) {
            String fromPage = pc.getGrid().getSingleStatusSelector(fieldGridLoc, work).getContent();
            Assert.assertEquals(fromPage, fieldDefaultValue, "Incorrect {" + fieldName + "} on page after updating");
        }
        Assert.assertFalse(pc.isSaveAreaEnabled(), "Save button is enabled");

        PSLogger.info("Test '" + fieldName + "'='" + fieldValue + "' with '" + inheritFieldName + "'='Yes' for parent");
        pc.getGrid().selectWorks();
        bulk = pc.openBulkEditBlock();
        bulk.getSingleStatusSelector(fieldBulkLoc).setLabel(fieldValue);
        bulk.getSingleStatusSelector(inheritFieldBulkLoc).setLabel(CONTROL_NO);
        bulk.update();
        mes = pc.getErrorMessagesFromTop();
        PSLogger.info("Message " + mes);

        if (TestSession.getAppVersion().inRange(PowerSteeringVersions._8_2, PowerSteeringVersions._9_2)) {
            String expectedMessage = MESSAGE_ERROR_BULK_OPERATION_READ_ONLY_TEMPLATE.replace(inheritFieldGridLoc.getLocator()) + "1";
            if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_0) &&
                    mes.contains(MESSAGE_BULK_OPERATION.getLocator())) {
                throw new PSKnownIssueException("72792", MESSAGE_BULK_OPERATION.getLocator());
            }
            Assert.assertTrue(mes.size() == expectedErrorsNumber && mes.contains(expectedMessage), "Seems incorrect error on page");
        } else {
            Assert.assertTrue(mes.size() == 0, "Seems there are errors on page");
        }

        List<Work> works = children;
        works.add(parent);
        for (Work work : works) {
            String fiedlValueFromPage = pc.getGrid().
                    getSingleStatusSelector(fieldGridLoc, work).getContent();
            String inherit = pc.getGrid().
                    getSingleStatusSelector(inheritFieldGridLoc, work).getContent();
            Assert.assertEquals(fiedlValueFromPage, fieldValue, "Incorrect {" + fieldName + "} on page after updating");
            Assert.assertEquals(inherit, CONTROL_NO.getLocator(),
                    "Incorrect inherit for " + fieldName + " on page after updating");
        }
        Assert.assertTrue(pc.isSaveAreaEnabled(), "Save button is disabled");
        pc.resetArea();

    }


    private static void checkRoleAndPoolForWorkAndAssert(WBSPage pc, Work work, Role exRole, ResourcePool exPool) {
        String role = pc.getGrid().getRole(work);
        String pool = pc.getGrid().getResourcePool(work);
        PSLogger.info("From page after updating: '" + role + "', '" + pool + "'");
        Assert.assertEquals(role, exRole == null ? null : exRole.getName(), "incorrect role for " + work.getFullName());
        Assert.assertEquals(pool, exPool == null ? null : exPool.getName(), "incorrect resource pool for " + work.getFullName());
    }

    private String setEffortAllocationDuration(WBSPage pc, Float effort, Float allocation, Integer duration) {
        WBSPage.BulkEditBlock bulk = pc.openBulkEditBlock();
        bulk.setEffortAllocationDuration(effort, allocation, duration);
        bulk.update();
        return WBSPage.calculateEffortAllocationDuration(effort, allocation, duration);
    }

    private WBSPage checkSpecifiedOptionAndGetWBSPage(Work parent,
                                                      boolean enablePlanResources,
                                                      WBSEPageLocators... controls) {
        SummaryWorkPage sum = WorkManager.open(parent);
        if (enablePlanResources)
            WorkManager.enablePlanResources(sum, parent);
        WBSPage pc = sum.openProjectPlanning();
        pc.getGrid().setIndexes(parent);
        WBSPage.Columns columns = pc.getOptions().getColumns();
        columns.uncheckAll();
        for (WBSEPageLocators control : controls)
            columns.getCheckbox(control).click();
        columns.apply();
        return pc;
    }


    public void testAllocateResource(Work parent, User user1, User user2) {
        List<Work> works = new ArrayList<Work>();
        works.addAll(parent.getChildren(true, true));
        works.add(0, parent);

        WBSPage pc = checkSpecifiedOptionAndGetWBSPage(parent, true, OPTIONS_BLOCK_COLUMNS_RESOURCE_PERSON);
        pc.setIncludeIndependentWork();

        pc.getGrid().selectWorks();
        WBSPage.AllocateResourceDialog dialog = pc.openAllocateResourceDialog();
        dialog.doSearch(user1.getFormatFullName());
        List<String> mes = pc.getErrorMessagesFromTop();
        Assert.assertTrue(mes.size() == 0, "There are errors on page: " + mes);
        for (Work work : works) {
            String fromPage = pc.getGrid().getPerson(work);
            Assert.assertEquals(fromPage, work.hasSplitResources() ? "" : user1.getFullName(),
                    "incorrect person for " + work + ", after setting " + user1);
        }

        Work work1 = works.remove(2);
        Work work2 = works.remove(3);
        pc.getGrid().unSelectWorks();
        pc.getGrid().selectWorks(work1, work2);
        WBSPage.AllocateResourceDialog dialog1 = pc.openAllocateResourceDialog();
        dialog1.doSearchByQualifications(user2.getFormatFullName());
        mes = pc.getErrorMessagesFromTop();
        Assert.assertTrue(mes.size() == 0, "There are errors on page: " + mes);
        for (Work work : works) {
            String fromPage = pc.getGrid().getPerson(work);
            Assert.assertEquals(fromPage, work.hasSplitResources() ? "" : user1.getFullName(),
                    "incorrect person for " + work + ", after setting " + user1);
        }
        for (Work work : new Work[]{work1, work2}) {
            String fromPage = pc.getGrid().getPerson(work);
            Assert.assertEquals(fromPage, work.hasSplitResources() ? "" : user2.getFullName(),
                    "incorrect person for " + work + ", after setting " + user2);
        }
        pc.resetArea();
    }
}


