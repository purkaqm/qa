package com.powersteeringsoftware.tests.project_central.actions;

import com.powersteeringsoftware.libs.elements.DatePicker;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.works.GatedProject;
import com.powersteeringsoftware.libs.objects.works.Template;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.objects.works.WorkType;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.actions.WorkTemplateManager;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.tests.project_central.TestData;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 21.09.2010
 * Time: 18:07:42
 */
public class GatedProjectsTCs {

    /**
     * for bug #69772
     */
    public void validateAddUnderAndAddAfterWorkTypesForGatedProject(GatedProject gp, Work child) {
        List<WorkType> allWorkTypes = TestSession.getWorkTypeList();
        List<WorkType> gatedWorkTypes = Work.getBuiltInTypes();
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_2)) {// see 75668 for more details.
            gatedWorkTypes.remove(Work.Type.MSP_PROJECT);
        }
        gatedWorkTypes.remove(Work.Type.ACTION_ITEM);

        List<Work> gates = gp.getChildren();
        Work gate = gates.get(TestData.getRandom().nextInt(gates.size()));

        WBSPage pc = WorkManager.openWBS(gp);
        List<WorkType>[] types = addUnderAndAddAfterWorkTypes(pc, gate, child);

        Assert.assertTrue(types[0].containsAll(gatedWorkTypes) && gatedWorkTypes.containsAll(types[0]) && types[0].size() == gatedWorkTypes.size(),
                "Incorrect 'Add Under' types for gate, should be : " + gatedWorkTypes);
        Assert.assertEquals(types[2], types[0], "Incorrect 'Add After' types for for gate descendant, should be : " + gatedWorkTypes);
        Assert.assertTrue(types[1].containsAll(allWorkTypes), "Incorrect 'Add Under' types for gate descendant, should be : " + allWorkTypes);

    }

    /**
     * for bug #69772
     */
    public void validateAddUnderAndAddAfterWorkTypesForGatedTemplate(Template template, Work child) {

        List<WorkType> allWorkTypes = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3) ?
                TestSession.getWorkTypeList() : Work.getBuiltInTypes();
        List<WorkType> gatedWorkTypes = Work.getBuiltInTypes();
        List<WorkType> allWorkTypesExclude = new ArrayList<WorkType>();
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_2)) {// see 75668 for more details.
            gatedWorkTypes.remove(Work.Type.MSP_PROJECT);
        }
        gatedWorkTypes.remove(Work.Type.ACTION_ITEM);

        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3)) {
            allWorkTypesExclude.add(template);
            allWorkTypes.remove(template);
        }

        List<Work> gates = template.getStructure().getChildren();
        Work gate = gates.get(TestData.getRandom().nextInt(gates.size()));

        WBSPage pc = WorkTemplateManager.openWBS(template);
        pc.getOptions().showLevel(8);

        List<WorkType>[] types = addUnderAndAddAfterWorkTypes(pc, gate, child);
        Assert.assertTrue(types[0].containsAll(gatedWorkTypes) && gatedWorkTypes.containsAll(types[0]) && types[0].size() == gatedWorkTypes.size(),
                "Incorrect 'Add Under' types for gate, should be : " + gatedWorkTypes);
        Assert.assertEquals(types[2], types[0], "Incorrect 'Add After' types for for gate descendant, should be : " + gatedWorkTypes);
        Assert.assertTrue(types[1].containsAll(allWorkTypes) && (allWorkTypesExclude.isEmpty() || !types[1].containsAll(allWorkTypesExclude)),
                "Incorrect 'Add Under' types for gate descendant, should be : " + allWorkTypes);

    }

    private List<WorkType>[] addUnderAndAddAfterWorkTypes(WBSPage pc,
                                                          Work gate,
                                                          Work child) {
        WBSPage.AddSubWorkDialog popup0 = pc.getGrid().callSubMenu(gate.getName()).addUnder();
        List<WorkType> types0 = Arrays.asList(popup0.getAllWorkTypes());
        popup0.setChildWorks(child);
        popup0.submit();
        // for 90 there is unsaved data
        if (pc.isResetAreaEnabled())
            pc.saveArea();
        gate.addChild(child);
        child.setCreated();

        WBSPage.AddSubWorkDialog popup1 = pc.getGrid().callSubMenu(child.getName()).addUnder();
        List<WorkType> types1 = Arrays.asList(popup1.getAllWorkTypes());
        popup1.cancel();
        WBSPage.AddSubWorkDialog popup2 = pc.getGrid().callSubMenu(child.getName()).addAfter();
        List<WorkType> types2 = Arrays.asList(popup2.getAllWorkTypes());
        popup2.cancel();

        PSLogger.info("'Add Under' types for gate: " + types0);
        PSLogger.info("'Add Under' types for gate child (" + child + "): " + types1);
        PSLogger.info("'Add After' types for gate child (" + child + "): " + types2);

        return new List[]{types0, types1, types2};
    }

    public void testSetConstraintDatesForSequentialGatedProject(GatedProject gp) {
        int numDays = 365;
        long current = System.currentTimeMillis();
        List<Work> works = new ArrayList<Work>(gp.getChildren());
        works.add(0, gp);

        WBSPage pc = WorkManager.openWBS(gp);

        // test-method project_central.TestDriver.validateScheduledDatesForSGP depends on this. set end date to current
        PSCalendar end = TestData.getRandomPSDate(current, numDays).nextWorkDate(true);
        PSCalendar start = TestData.getRandomPSDate(gp.getConstraintStartDateAsLong(), -numDays).nextWorkDate(false);
        PSLogger.info("Dates: " + end + "," + start);

        Work last = gp.getLastGate();
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_3)) {
            WBSPage.GateProjectDialog dialog = pc.getGrid().callGateProjectDialog(last, 2);
            Assert.assertNotNull(dialog, "Can not call dialog");
            dialog.getEndDatePicker(last.getName()).set(end.toString());
            dialog.getProjectStartDatePicker().set(start.toString());
            dialog.submit();
        } else {
            pc.getGrid().setConstraintEnd(last, end.toString());
            pc.getGrid().setConstraintStart(gp, start.toString());
            pc.saveArea();
        }

        gp.setConstraintStartDate(start);
        gp.setConstraintEndDate(end);
        testDatesForSequentialGatedProject(pc, works);

        end = TestData.getRandomPSDate(gp.getConstraintEndDateAsLong(), numDays).nextWorkDate(true);
        PSLogger.info("New end: " + end);

        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_3)) {
            WBSPage.GateProjectDialog dialog = pc.getGrid().callGateProjectDialog(works.get(0), 0);
            Assert.assertNotNull(dialog, "Can not call dialog");
            DatePicker dp = dialog.getEndDatePicker(last.getName());
            dp.useDropDownOrArrows(false);
            dp.set(end.toString());
            dialog.submit();
        } else {
            pc.getGrid().setConstraintEnd(last, end.toString());
            pc.saveArea();
        }

        gp.setConstraintEndDate(end);
        testDatesForSequentialGatedProject(pc, works);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3)) {
            for (Work w : works) { // in additional validate const type:
                Assert.assertEquals(pc.getGrid().getConstraint(w), w.getConstraint().getName(), "Incorrect constraint type for " + w);
            }
        }
    }


    public void testErrorsWithConstraintDates(GatedProject project, PSCalendar calendar) {
        List<Work> gates = project.getChildren();
        Work gate = gates.get(TestData.getRandom().nextInt(gates.size() - 1));
        Work last = project.getLastGate();

        int numDays = 3 * 365;

        WBSPage pc = WorkManager.openWBS(project);
        String projectEndDate = pc.getGrid().getConstraintEndDate(project);
        String projectStartDate = pc.getGrid().getConstraintStartDate(project);

        // test data:
        String projectStartDateToSet = calendar.set(projectEndDate).
                set(TestData.getRandomNotZeroInt(numDays)).toString();
        String gateEndDateToSet = calendar.set(projectStartDate).
                set(TestData.getRandomNotZeroInt(-numDays)).toString();
        String projectEndDateToSet = calendar.set(projectStartDate).
                set(TestData.getRandomNotZeroInt(-numDays)).toString();

        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_3)) {
            testUpdateStatusErrors(pc, project, gate, last, projectStartDateToSet, projectEndDateToSet, gateEndDateToSet);
        } else {
            testGridErrors(pc, project, gate, last, projectStartDateToSet, projectEndDateToSet, gateEndDateToSet);
        }
    }

    private void testGridErrors(WBSPage pc,
                                Work project,
                                Work gate,
                                Work last,
                                String projectStart,
                                String projectEnd,
                                String gateEnd) {
        PSLogger.info("Set project start date  to " + projectStart);
        validateErrorWithDatesInGrid(pc, project, projectStart, true, false);

        /*
        // there is 75762, should be assertion error
        pc.getGrid().setConstraintType(gate, Work.Constraint.FNLT);
        PSLogger.info("Set date " + gateEnd + " for gate " + gate);
        validateErrorWithDatesInGrid(pc, gate, gateEnd, false, true);
        pc.resetArea();
        */
        PSLogger.info("Set date " + gateEnd + " for gate " + gate);
        validateErrorWithDatesInGrid(pc, gate, gateEnd, false, false);

        PSLogger.info("Set project end date to  " + projectEnd);
        validateErrorWithDatesInGrid(pc, last, projectEnd, false, false);
    }

    private void testUpdateStatusErrors(WBSPage pc,
                                        Work project,
                                        Work gate,
                                        Work last,
                                        String projectStart,
                                        String projectEnd,
                                        String gateEnd) {
        WBSPage.GateProjectDialog dialog = pc.getGrid().callGateProjectDialog(project, 1);
        Assert.assertNotNull(dialog, "Can not call dialog");
        PSLogger.info("Set project start date  to " + projectStart);
        dialog.getProjectStartDatePicker().set(projectStart);
        validateErrorWithDatesInUpdateStatusPopup(dialog);
        //todo: validate error
        dialog.cancel();
        Assert.assertFalse(dialog.isVisible(), "Dialog still exist after canceling");

        dialog = pc.getGrid().callGateProjectDialog(project, 2);
        PSLogger.info("Set date " + gateEnd + " for gate " + gate);
        dialog.getEndDatePicker(gate.getName()).set(gateEnd);
        validateErrorWithDatesInUpdateStatusPopup(dialog);
        //todo: validate error
        dialog.close();
        Assert.assertFalse(dialog.isVisible(), "Dialog still exist after closing");

        dialog = pc.getGrid().callGateProjectDialog(gate, 0);
        PSLogger.info("Set project end date to  " + projectEnd);
        dialog.getEndDatePicker(last.getName()).set(projectEnd);
        validateErrorWithDatesInUpdateStatusPopup(dialog);
        //todo: validate error
        dialog.cancel();
        Assert.assertFalse(dialog.isVisible(), "Dialog still exist after canceling");
    }

    private static String validateErrorWithDatesInGrid(WBSPage pc, Work work, String date, boolean start, boolean saveEnabled) {
        if (start)
            pc.getGrid().setConstraintStart(work, date);
        else
            pc.getGrid().setConstraintEnd(work, date);
        List<String> errors = pc.getErrorMessagesFromTop();
        PSLogger.debug("Errors: " + errors);
        Assert.assertTrue(errors.size() == 1, "Expected one error on page");
        Assert.assertEquals(pc.isSaveAreaEnabled(), saveEnabled, "Save is " + (!saveEnabled ? "enabled" : "disabled"));
        return errors.get(0);
    }

    private static String validateErrorWithDatesInUpdateStatusPopup(WBSPage.GateProjectDialog dialog) {
        dialog.submit();
        Assert.assertTrue(dialog.isVisible(), "Can't find dialog after submitting");
        String msg = dialog.getErrorMessage();
        Assert.assertNotNull(msg, "Can't find error message");
        return msg;
    }

    public void testScheduledDatesForSequentialGatedProject(GatedProject project) {
        List<Work> works = project.getChildren();
        works.add(0, project);

        Work.Status status = works.get(0).getStatus();
        PSLogger.info("Status for project is " + status);
        if (Work.Status.CANCELED.equals(status) ||
                Work.Status.COMPLETED.equals(status)) {
            PSSkipException.skip("Status for project is " + status + "; skip testcase");
        }

        WBSPage pc = WorkManager.openWBS(works.get(0));
        pc.hideGantt();
        WBSPage.Columns columns = pc.getOptions().getColumns();
        columns.uncheckAll();
        columns.getCheckbox(OPTIONS_BLOCK_COLUMNS_PROJECT_STATUS).click();
        columns.getCheckbox(OPTIONS_BLOCK_COLUMNS_DATES_CONSTRAINT).click();
        columns.getCheckbox(OPTIONS_BLOCK_COLUMNS_DATES_SCHEDULED).click();
        columns.apply();

        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3)) {
            // do not see difference between constraint and scheduled dates now.
            //testDatesForSequentialGatedProject(pc, works, false);
            Long date = null;
            // simple check for ladder:
            for (Work w : project.getChildren()) {
                for (int i = 0; i < 2; i++) {
                    String sDate;
                    if (i == 0)
                        sDate = pc.getGrid().getScheduledStartDate(w);
                    else
                        sDate = pc.getGrid().getScheduledEndDate(w);
                    PSLogger.info("Scheduled " + (i == 0 ? "start" : "end") + " date for " + w.getName() + " is " + sDate);
                    Long _date = project.getCalendar().set(sDate).getTime();
                    if (date != null) {
                        Assert.assertTrue(date <= _date, "Incorrect scheduled " + (i == 0 ? "start" : "end") +
                                " date for " + w.getName() + ": less then date for previous gate");
                    }
                    date = _date;
                }
            }
            String start = pc.getGrid().getScheduledStartDate(project);
            String end = pc.getGrid().getScheduledEndDate(project);
            String _start = pc.getGrid().getScheduledStartDate(works.get(0));
            String _end = pc.getGrid().getScheduledEndDate(works.get(works.size() - 1));
            Assert.assertEquals(start, project.getConstraintStartDate(), "Incorrect scheduled start date for project");
            Assert.assertEquals(end, project.getConstraintEndDate(), "Incorrect scheduled end date for project");
            Assert.assertEquals(_start, project.getConstraintStartDate(), "Incorrect scheduled start date for first gate");
            Assert.assertEquals(_end, project.getConstraintEndDate(), "Incorrect scheduled end date for end gate");
            return;
        }

        PSCalendar projectStart = getCalendar(pc, GRID_TABLE_CONSTRAINT_START, works.get(0));
        PSCalendar projectEnd = getCalendar(pc, GRID_TABLE_CONSTRAINT_END, works.get(works.size() - 1));
        PSLogger.info("Project dates: " + projectStart + ", " + projectEnd);

        PSCalendar firstGateStart = getCalendar(pc, GRID_TABLE_SCHEDULED_START, works.get(1));
        PSCalendar firstGateEnd = getCalendar(pc, GRID_TABLE_SCHEDULED_END, works.get(1));
        PSLogger.info("First gate dates: " + firstGateStart + ", " + firstGateEnd);

        PSCalendar lastGateStart = getCalendar(pc, GRID_TABLE_SCHEDULED_START, works.get(works.size() - 1));
        PSCalendar lastGateEnd = getCalendar(pc, GRID_TABLE_SCHEDULED_END, works.get(works.size() - 1));
        PSLogger.info("Last gate dates: " + lastGateStart + ", " + lastGateEnd);

        // first gate is SNET
        Assert.assertEquals(firstGateStart.days(), projectStart.days(), "Incorrect scheduled start date for first gate");
        Assert.assertEquals(firstGateEnd.days(), projectStart.days(true), "Incorrect scheduled end date for first gate");

        // last gate if FNLT 
        Assert.assertEquals(lastGateStart.days(), projectEnd.days(false), "Incorrect scheduled start date for last gate");
        Assert.assertEquals(lastGateEnd.days(), projectEnd.days(), "Incorrect scheduled end date for last gate");

        for (int i = 2; i < works.size() - 2; i++) {
            PSCalendar thisGateStart = getCalendar(pc, GRID_TABLE_SCHEDULED_START, works.get(i));
            PSCalendar thisGateEnd = getCalendar(pc, GRID_TABLE_SCHEDULED_END, works.get(i));
            PSLogger.info(works.get(i).getName() + " gate dates: " + thisGateStart + ", " + thisGateEnd);
            Assert.assertTrue(thisGateStart.days() >= projectStart.days(), "Incorrect scheduled start date for gate " + works.get(i).getName());
            Assert.assertTrue(thisGateEnd.days() <= projectEnd.days(), "Incorrect scheduled end date for gate " + works.get(i).getName());
        }
        // for other validation bug #70398 should be fixed
    }

    private static PSCalendar getCalendar(WBSPage pc, ILocatorable loc, Work work) {
        return work.getCalendar().set(pc.getGrid().getDatePicker(loc, work).get());
    }

    private static void testDatesForSequentialGatedProject(WBSPage pc,
                                                           List<Work> works) {
        testDatesForSequentialGatedProject(pc, works, true);
    }

    private static void testDatesForSequentialGatedProject(WBSPage pc, List<Work> works, boolean constraint) {
        String str = constraint ? "Constraint" : "Scheduled";
        List<String> actualStart = new ArrayList<String>();
        List<String> expectedStart = new ArrayList<String>();
        List<String> actualEnd = new ArrayList<String>();
        List<String> expectedEnd = new ArrayList<String>();
        for (Work w : works) {
            String startFromPage = constraint ? pc.getGrid().getConstraintStartDate(w) : pc.getGrid().getScheduledStartDate(w);
            String endFromPage = constraint ? pc.getGrid().getConstraintEndDate(w) : pc.getGrid().getScheduledEndDate(w);
            PSLogger.info(str + " dates from page for " + w.getName() + ":" + startFromPage + "," + endFromPage);
            String start = w.getConstraintStartDate();
            String end = w.getConstraintEndDate();
            PSLogger.info("Expected dates " + w.getName() + ": " + start + "," + end);
            actualStart.add(startFromPage);
            expectedStart.add(start == null ? "" : start);
            actualEnd.add(endFromPage);
            expectedEnd.add(end == null ? "" : end);

        }
        printDates("Actual " + str + " dates", works, actualStart, actualEnd);
        printDates("Expected " + str + " dates", works, expectedStart, expectedEnd);
        Assert.assertEquals(actualStart, expectedStart, "Incorrect " + str + " start dates on page");
        Assert.assertEquals(actualEnd, expectedEnd, "Incorrect " + str + " end dates on page");
    }

    private static void printDates(String title, List<Work> works, List<String> start, List<String> end) {
        PSLogger.info(title + ":");
        for (int i = 0; i < works.size(); i++) {
            PSLogger.info(works.get(i).getName() + ": " + start.get(i) + "," + end.get(i));
        }
    }

    public void makeProjectCanceled(GatedProject parent) {
        List<Work> works = parent.getChildren();
        Work gate = works.get(TestData.getRandom().nextInt(works.size()));
        Work first = parent.getFirstGate();
        works.add(0, parent);


        WBSPage pc = WorkManager.openWBS(parent);
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_3)) {
            WBSPage.GateProjectDialog dialog = pc.getGrid().callGateProjectDialog(gate, 0);
            Assert.assertNotNull(dialog, "Can not call dialog");
            dialog.cancelGate(first.getName());
            dialog.submit();
            Assert.assertNull(dialog.getErrorMessage(), "There is an error in update status popup");
            Assert.assertFalse(dialog.isVisible(), "Dialog still visible after submiting");
        } else {
            pc.getGrid().setStatus(parent, Work.Status.CANCELED);
            List<String> errors = pc.getErrorMessagesFromTop();
            Assert.assertTrue(errors.size() == 0, "Have errors: " + errors);
            pc.saveArea();
        }
        for (Work work : works) {
            String statusFromPage = pc.getGrid().getStatus(work);
            String statusExpected = Work.Status.CANCELED.getValue();
            Assert.assertEquals(statusFromPage, statusExpected, "Incorrect status after canceling");
            work.setStatus(Work.Status.get(statusExpected));
        }
    }


    public void testSetActualDatesForSequentialGatedProject(GatedProject project) {
        List<Work> works = project.getChildren();
        works.add(0, project);

        WBSPage pc = WorkManager.openWBS(works.get(0));
        pc.hideGantt();
        WBSPage.Columns display = pc.getOptions().getColumns();
        display.uncheckAll();
        display.getCheckbox(OPTIONS_BLOCK_COLUMNS_DATES_ACTUAL).click();
        display.apply();

        List<String> endDates = new ArrayList<String>();
        List<String> startDates = new ArrayList<String>();
        for (Work work : works) {
            DatePicker endDP = pc.getGrid().getActualEndDatePicker(work);
            DatePicker startDP = pc.getGrid().getActualStartDatePicker(work);
            Assert.assertNotNull(endDP, "incorrect actual end date for " + work);
            Assert.assertNotNull(startDP, "incorrect actual start date for " + work);
            endDates.add(endDP.get());
            startDates.add(startDP.get());
        }

        setStartAndEndActualDatesForProject(pc, startDates, endDates, 0, 0, works, project.getCalendar());
        setStartActualDatesForGates(pc, startDates, endDates, works, project.getCalendar());
        setStartAndEndActualDatesForProject(pc, startDates, endDates, works.size() - 1, 1, works, project.getCalendar());
        //todo: add check for setting actual end dates
        pc.resetArea();
    }

    private static void setStartActualDatesForGates(WBSPage pc,
                                                    List<String> startDates,
                                                    List<String> endDates,
                                                    List<Work> works, PSCalendar calendar) {
        PSLogger.info("Test 'ladder' for gates");
        for (int i = works.size() - 1; i > 1; i--) {
            String dateToSet = endDates.get(i);
            pc.getGrid().getActualStartDatePicker(works.get(i)).set(dateToSet);
            String fromPage = pc.getGrid().getActualStartDatePicker(works.get(i)).get();
            Assert.assertEquals(fromPage, dateToSet, "Incorrect actual start date after setting for " + works.get(i));
            startDates.remove(i);
            startDates.add(i, dateToSet);
            PSCalendar expectedEndDate = calendar.set(dateToSet).setWorkDays(-1);
            String sActualEndDate = pc.getGrid().getActualEndDatePicker(works.get(i - 1)).get();
            PSCalendar actualEndDate = calendar.set(sActualEndDate);
            Assert.assertEquals(actualEndDate,
                    expectedEndDate, "Incorrect actual end date for " + works.get(i - 1).getName());
            endDates.remove(i - 1);
            endDates.add(i - 1, sActualEndDate);
        }
    }

    private static void setStartAndEndActualDatesForProject(WBSPage pc,
                                                            List<String> startDates,
                                                            List<String> endDates,
                                                            int workToSetEndDate,
                                                            int workToSetStartDate,
                                                            List<Work> works, PSCalendar calendar) {
        PSLogger.info("Test setting actual end and start dates for whole project");
        int numDays = 365;
        PSCalendar startPSPsCalendar;
        PSCalendar endPSPsCalendar;
        // duration should be greater than number of works:
        do {
            startPSPsCalendar = calendar.
                    set(startDates.get(0)).set(TestData.getRandomNotZeroInt(-numDays));
            endPSPsCalendar = calendar.
                    set(endDates.get(endDates.size() - 1)).set(TestData.getRandomNotZeroInt(numDays));
        } while (endPSPsCalendar.diffInWorkDays(startPSPsCalendar) < works.size() - 2);
        String start = startPSPsCalendar.toString();
        String end = endPSPsCalendar.toString();
        PSLogger.debug("Dates to test : " + start + " " + end);

        int firstIndex = 1;
        int lastIndex = works.size() - 1;
        Work parent = works.get(0);
        Work first = works.get(firstIndex);
        Work last = works.get(lastIndex);

        // if start date from page is less then specified end, then no errors:
        if (PSCalendar.compare(startDates.get(0), end) == -1) {
            // no errors, set end date, then start date
            pc.getGrid().getActualEndDatePicker(works.get(workToSetEndDate)).set(end);
            pc.getGrid().getActualStartDatePicker(works.get(workToSetStartDate)).set(start);
        } else {
            // set start then end
            pc.getGrid().getActualStartDatePicker(works.get(workToSetStartDate)).set(start);
            pc.getGrid().getActualEndDatePicker(works.get(workToSetEndDate)).set(end);
        }

        // validate end dates:
        Assert.assertEquals(pc.getGrid().getActualEndDatePicker(parent).get(), end, "Incorrect actual end date for project");
        endDates.remove(0);
        endDates.add(0, end);
        Assert.assertEquals(pc.getGrid().getActualEndDatePicker(last).get(), end, "Incorrect actual end date for last gate");
        endDates.remove(lastIndex);
        endDates.add(lastIndex, end);
        // validate start dates:
        Assert.assertEquals(pc.getGrid().getActualStartDatePicker(parent).get(), start, "Incorrect actual start date for project");
        startDates.remove(0);
        startDates.add(0, start);
        Assert.assertEquals(pc.getGrid().getActualStartDatePicker(first).get(), start, "Incorrect actual start date for first gate");
        startDates.remove(firstIndex);
        startDates.add(firstIndex, start);
    }

    public void testGridErrors(GatedProject parent) {

        int numDays = 3 * 365;

        List<Work> gates = parent.getChildren();
        Work last = gates.remove(gates.size() - 1);
        Work gate1 = gates.remove(TestData.getRandom().nextInt(gates.size()));
        Work gate2 = gates.remove(TestData.getRandom().nextInt(gates.size()));

        WBSPage pc = WorkManager.openWBS(parent);
        pc.hideGantt();
        WBSPage.Columns columns = pc.getOptions().getColumns();
        columns.uncheckAll();
        columns.getCheckbox(OPTIONS_BLOCK_COLUMNS_DATES_ACTUAL).click();
        columns.apply();
        // from page:
        String projectEnd = pc.getGrid().getActualEndDatePicker(parent).get();
        String projectStart = pc.getGrid().getActualStartDatePicker(parent).get();
        String gateEnd = pc.getGrid().getActualEndDatePicker(gate2).get();
        String gateStart = pc.getGrid().getActualStartDatePicker(gate2).get();

        // test data to set:
        String afterProjectEnd = parent.getCalendar().set(projectEnd).
                set(TestData.getRandomNotZeroInt(numDays)).toString();
        String beforeProjectStart = parent.getCalendar().set(projectStart).
                set(TestData.getRandomNotZeroInt(-numDays)).toString();
        String afterGateEnd = parent.getCalendar().set(gateEnd).
                set(TestData.getRandomNotZeroInt(numDays)).toString();
        String beforeGateStart = parent.getCalendar().set(gateStart).
                set(TestData.getRandomNotZeroInt(-numDays)).toString();

        // validate errors:

        setIncorrectActualDateInGrid(pc, parent, afterProjectEnd, true);

        setIncorrectActualDateInGrid(pc, gate1, beforeProjectStart, false);

        setIncorrectActualDateInGrid(pc, last, beforeProjectStart, false);

        setIncorrectActualDateInGrid(pc, gate2, beforeGateStart, false);

        setIncorrectActualDateInGrid(pc, gate2, afterGateEnd, true);

        if (pc.isSaveAreaEnabled()) {
            PSLogger.knis(72712);
            pc.saveArea();
        }
    }

    private void setIncorrectActualDateInGrid(WBSPage pc, Work work, String dateToSet, boolean start) {
        String dateWas = start ? pc.getGrid().getActualStartDate(work) : pc.getGrid().getActualEndDate(work);
        PSLogger.info("Actual " + (start ? "Start" : "End") + " date for " + work.getName() + " is " + dateWas);
        (start ? pc.getGrid().getActualStartDatePicker(work) :
                pc.getGrid().getActualEndDatePicker(work)).set(dateToSet);
        List<String> mess = pc.getErrorMessagesFromTop();
        PSLogger.info("Errors from page " + mess);
        Assert.assertTrue(mess.size() == 1, "Should be an error on page after setting " + dateToSet +
                " as Actual " + (start ? "Start" : "End") + " for " + work.getName());
        pc.closeMessages();
        String dateNow = start ? pc.getGrid().getActualStartDate(work) : pc.getGrid().getActualEndDate(work);
        Assert.assertEquals(dateNow, dateWas, "Incorrect dates after checking error for work " + work.getName());
    }


    public void testSetConstraintTypesForNonSequentialGatedProject(GatedProject parent, PSCalendar calendar) {
        String current = calendar.toString();

        Work last = parent.getLastGate();
        Work first = parent.getFirstGate();

        WBSPage pc = WorkManager.openWBS(parent);
        pc.hideGantt();

        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3)) {
            Assert.assertFalse(pc.getGrid().isConstraintTypeEditable(parent), "Constraint type is editable for gp");
            for (Work w : parent.getChildren()) {
                Assert.assertFalse(pc.getGrid().isConstraintTypeEditable(w), "Constraint type is editable for " + w.getName());
            }
            //PSSkipException.skip("Skip due to #80828");
            return;
        }
        pc.getGrid().setConstraintType(last, Work.Constraint.FNLT);
        List<String> messages = pc.getErrorMessagesFromTop();
        Assert.assertTrue(messages.size() == 0, "There are errors on page : " + messages);
        validateConstraintType(pc, last, Work.Constraint.FNLT);
        validateConstraintType(pc, parent, Work.Constraint.FNLT);
        validateConstraintType(pc, first, Work.Constraint.ASAP);
        validateConstraintEndDate(pc, last, current);
        validateConstraintEndDate(pc, parent, current);
        validateConstraintEndDate(pc, first);
        validateConstraintStartDate(pc, last);
        validateConstraintStartDate(pc, parent);
        validateConstraintStartDate(pc, first);


        pc.getGrid().setConstraintType(last, Work.Constraint.ASAP);
        Assert.assertTrue((messages = pc.getErrorMessagesFromTop()).size() == 0, "There are errors on page : " + messages);
        validateConstraintType(pc, last, Work.Constraint.ASAP);
        validateConstraintType(pc, parent, Work.Constraint.ASAP);
        validateConstraintType(pc, first, Work.Constraint.ASAP);
        validateConstraintEndDate(pc, last);
        validateConstraintEndDate(pc, parent);
        validateConstraintEndDate(pc, first);
        validateConstraintStartDate(pc, last);
        validateConstraintStartDate(pc, parent);
        validateConstraintStartDate(pc, first);

        pc.getGrid().setConstraintType(parent, Work.Constraint.FNLT);
        Assert.assertTrue((messages = pc.getErrorMessagesFromTop()).size() == 0, "There are errors on page : " + messages);
        validateConstraintType(pc, last, Work.Constraint.FNLT);
        validateConstraintType(pc, parent, Work.Constraint.FNLT);
        validateConstraintType(pc, first, Work.Constraint.ASAP);
        validateConstraintEndDate(pc, last, current);
        validateConstraintEndDate(pc, parent, current);
        validateConstraintEndDate(pc, first);
        validateConstraintStartDate(pc, last);
        validateConstraintStartDate(pc, parent);
        validateConstraintStartDate(pc, first);


        pc.getGrid().setConstraintType(last, Work.Constraint.SNET);
        Assert.assertTrue((messages = pc.getErrorMessagesFromTop()).size() == 0, "There are errors on page : " + messages);
        validateConstraintType(pc, last, Work.Constraint.SNET);
        validateConstraintType(pc, parent, Work.Constraint.ASAP);
        validateConstraintType(pc, first, Work.Constraint.ASAP);
        validateConstraintEndDate(pc, last);
        validateConstraintEndDate(pc, parent);
        validateConstraintEndDate(pc, first);
        validateConstraintStartDate(pc, last, current);
        validateConstraintStartDate(pc, parent);
        validateConstraintStartDate(pc, first);


        pc.getGrid().setConstraintType(parent, Work.Constraint.SNET);
        Assert.assertTrue((messages = pc.getErrorMessagesFromTop()).size() == 0, "There are errors on page : " + messages);
        validateConstraintType(pc, last, Work.Constraint.SNET);
        validateConstraintType(pc, parent, Work.Constraint.SNET);
        validateConstraintType(pc, first, Work.Constraint.SNET);
        validateConstraintEndDate(pc, last);
        validateConstraintEndDate(pc, parent);
        validateConstraintEndDate(pc, first);
        validateConstraintStartDate(pc, last, current);
        validateConstraintStartDate(pc, parent, current);
        validateConstraintStartDate(pc, first, current);

        pc.getGrid().setConstraintType(last, Work.Constraint.FNLT);
        Assert.assertTrue((messages = pc.getErrorMessagesFromTop()).size() == 0, "There are errors on page : " + messages);
        validateConstraintType(pc, last, Work.Constraint.FNLT);
        validateConstraintType(pc, parent, Work.Constraint.FD);
        validateConstraintType(pc, first, Work.Constraint.SNET);
        validateConstraintEndDate(pc, last, current);
        validateConstraintEndDate(pc, parent, current);
        validateConstraintEndDate(pc, first);
        validateConstraintStartDate(pc, last);
        validateConstraintStartDate(pc, parent, current);
        validateConstraintStartDate(pc, first, current);


        pc.getGrid().setConstraintType(parent, Work.Constraint.ASAP);
        Assert.assertTrue((messages = pc.getErrorMessagesFromTop()).size() == 0, "There are errors on page : " + messages);
        validateConstraintType(pc, last, Work.Constraint.ASAP);
        validateConstraintType(pc, parent, Work.Constraint.ASAP);
        validateConstraintType(pc, first, Work.Constraint.ASAP);
        validateConstraintEndDate(pc, last);
        validateConstraintEndDate(pc, parent);
        validateConstraintEndDate(pc, first);
        validateConstraintStartDate(pc, last);
        validateConstraintStartDate(pc, parent);
        validateConstraintStartDate(pc, first);

        PSLogger.info("checkConstraintTypesForNonSGP final");
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_2)) {
            if (pc.isSaveAreaEnabled()) {
                PSLogger.knis(72104);
                pc.saveArea();
            }
        } else {
            pc.resetArea();
        }

    }

    private void validateConstraintType(WBSPage pc, Work work, Work.Constraint expected) {
        Work.Constraint fromPage = pc.getGrid().getConstraintType(work);
        Assert.assertEquals(fromPage, expected, "Incorrect constraint type for " + work.getName());
    }

    private void validateConstraintEndDate(WBSPage pc, Work work, String expected) {
        String fromPage = pc.getGrid().getConstraintEndDate(work);
        Assert.assertEquals(fromPage, expected, "Incorrect constraint end date for " + work.getName());
    }

    private void validateConstraintStartDate(WBSPage pc, Work work, String expected) {
        String fromPage = pc.getGrid().getConstraintStartDate(work);
        Assert.assertEquals(fromPage, expected, "Incorrect constraint start date for " + work.getName());
    }

    private void validateConstraintEndDate(WBSPage pc, Work work) {
        String fromPage = pc.getGrid().getConstraintEndDate(work);
        Assert.assertTrue(fromPage.isEmpty(), "Incorrect constraint end date for " + work.getName() + ": " + fromPage +
                ". Should be empty");
    }

    private void validateConstraintStartDate(WBSPage pc, Work work) {
        String fromPage = pc.getGrid().getConstraintStartDate(work);
        Assert.assertTrue(fromPage.isEmpty(), "Incorrect constraint start date for " + work.getName() + ": " + fromPage +
                ". Should be empty");
    }

}
