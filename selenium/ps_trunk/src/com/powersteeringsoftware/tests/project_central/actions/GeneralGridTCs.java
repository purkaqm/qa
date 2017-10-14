package com.powersteeringsoftware.tests.project_central.actions;

import com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.WorkDependency;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.objects.works.WorkType;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.tests.project_central.TestData;
import org.testng.Assert;

import java.util.*;

import static com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 13.10.2010
 * Time: 19:39:25
 */
public class GeneralGridTCs {

    public void dragAndDrop(Work parent) {
        WBSPage pc = WorkManager.openWBS(parent);

        int index1 = TestData.getRandom().nextInt(parent.getChildren().size());
        int index2;
        while ((index2 = TestData.getRandom().nextInt(parent.getChildren().size())) == index1) ;

        Work ch1 = parent.getChildren().get(index1);
        Work ch2 = parent.getChildren().get(index2);
        List<String> before = pc.getGrid().getListTree();
        PSLogger.info("before: " + before);
        List<String> expected = new ArrayList<String>(before);
        expected.add(ch2.getRowIndex() - 1, expected.remove(ch1.getRowIndex() - 1));
        pc.getGrid().dragAndDrop(ch1, ch2);
        List<String> after = pc.getGrid().getListTree();
        PSLogger.info("after: " + after);
        Assert.assertEquals(after, expected, "Incorrect list for after drag'n'drop");
        for (Work ch : parent.getChildren()) { // setting right indexes:
            int index = expected.indexOf(ch.getName()) + 2;
            ch.setRowIndex(index);
        }
        Assert.assertFalse(pc.isSaveAreaEnabled(), "Save is enabled after drag'n'drop");
    }


    protected void testInformationPopup(WBSPage pc, Work parent, Work child) {
        WBSPage.InformationDialog dialog = pc.getGrid().callSubMenu(child.getName()).information();
        List<String> info = dialog.getWorkInfo();
        dialog.ok();
        PSLogger.info("For " + child.getFullName() + " information popup: " + info);

        Assert.assertEquals(info.get(0), child.getOwner().getFullName(), "Project " + child.getName() + ": incorrect username in info popup");
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._10_0) && child.isFolder()) { //#86446
            Assert.assertTrue(info.size() == 1, "Incorrect info for folder, should be only owner");
            return;
        }
        Assert.assertEquals(info.get(2), child.getConstraint().getValue(), "Project " + child.getName() + ": incorrect constraints in info popup");

        // there is scheduled dates in information menu if manual scheduling off:
        Long constraintStart = child.getConstraintStartDateAsLong();
        Long constraintEnd = child.getConstraintEndDateAsLong();
        if (parent.getManualScheduling()) {
            if (constraintStart != null) {
                long actual = getCalendar(parent, info.get(3)).days();
                long expected = getCalendar(parent, constraintStart).days();
                Assert.assertEquals(actual, expected, "Incorrect start date in info popup");
            }
            if (constraintEnd != null) {
                long actual = getCalendar(parent, info.get(4)).days();
                long expected = getCalendar(parent, constraintEnd).days();
                Assert.assertEquals(actual, expected, "Incorrect end date in info popup");
            }
            return;
        }
        PSCalendar[] expectedScheduledDates = getScheduledDates(parent, child, 0);

        String[] sExpectedScheduledDates = new String[2];
        sExpectedScheduledDates[0] = expectedScheduledDates[0] != null ? expectedScheduledDates[0].toString() : "";
        sExpectedScheduledDates[1] = expectedScheduledDates[1] != null ? expectedScheduledDates[1].toString() : "";
        String reallyScheduledStart = info.get(3);
        String reallyScheduledEnd = info.get(4);
        PSLogger.debug(child.getName() + ", from page: " + reallyScheduledStart + "," + reallyScheduledEnd + ".");
        PSLogger.debug(child.getName() + ", expected: " + sExpectedScheduledDates[0] + "," + sExpectedScheduledDates[1] + ".");
        if (!reallyScheduledStart.equals(sExpectedScheduledDates[0])) {
            PSLogger.error("Incorrect scheduled start date in info popup for " + child.getName() + " : '" + info.get(3) + "'");
        }
        if (!reallyScheduledEnd.equals(sExpectedScheduledDates[1])) {
            PSLogger.error("Incorrect scheduled end date in info popup for " + child.getName() + " : '" + info.get(4) + "'");
        }
        /*Assert.assertEquals(reallyScheduledStart, sExpectedScheduledDates[0],
                "Incorrect scheduled start date in info popup for " + child.getName() + " : '" + info.get(3) + "'");
        Assert.assertEquals(reallyScheduledEnd, sExpectedScheduledDates[1],
                "Incorrect scheduled end date in info popup for " + child.getName() + " : '" + info.get(4) + "'");*/
    }

    public void testInformationPopup(Work parent) {
        WBSPage pc = WorkManager.openWBS(parent);
        for (Work work : parent.getChildren()) {
            testInformationPopup(pc, parent, work);
        }
        //parent:
        WBSPage.InformationDialog dialog = pc.getGrid().callSubMenu(parent.getName()).information();
        List<String> info = dialog.getWorkInfo();
        dialog.ok();
        PSLogger.info("For " + parent + " information popup: " + info);
        Assert.assertEquals(info.get(0), parent.getOwner().getFullName(),
                "Incorrect username in info popup");
        //Assert.assertEquals(info.get(2), work.getConstraint(), "Incorrect constraints in info popup");
        //project dates:
        String actualProjectStart = info.get(3);
        String actualProjectEnd = info.get(4);
        PSCalendar expectedStart = getCalendar(parent, parent.getProjectStartDateAsLong());
        PSCalendar expectedEnd = getCalendar(parent, parent.getProjectEndDateAsLong());
        PSLogger.debug("Project actual: " + actualProjectStart + "," + actualProjectEnd + ".");
        PSLogger.debug("Project expected: " + expectedStart + "," + expectedEnd + ".");
        /*Assert.assertEquals(actualProjectStart, expectedStart.toString(),
                "Incorrect scheduled start date in info popup for project " + parent + " : " + info.get(3));
        Assert.assertEquals(actualProjectEnd, expectedEnd.toString(),
                "Incorrect scheduled end date in info popup for project " + parent + " : " + info.get(4));*/
        if (!actualProjectEnd.equals(expectedStart.toString()))  //todo
            PSLogger.error("Incorrect scheduled start date in info popup for project " + parent + " : " + info.get(3));
        if (!actualProjectEnd.equals(expectedEnd.toString()))
            PSLogger.error("Incorrect scheduled end date in info popup for project " + parent + " : " + info.get(4));
        //todo: check other settings for parent
    }


    /**
     * return scheduled dates according to MSP
     * todo: ints hotfix method
     *
     * @param work     - Work
     * @param duration - duration
     * @return long - array with dates in days
     */
    public PSCalendar[] getScheduledDates(Work parent, Work work, int duration) {
        Long constraintStart = work.getConstraintStartDateAsLong();
        Long constraintEnd = work.getConstraintEndDateAsLong();
        PowerSteeringVersions ver = TestSession.getAppVersion();
        if (work.isMilestone()) {
            PSCalendar date = getCalendar(parent, work.getConstraintStartDateAsLong() != null ? work.getConstraintStartDateAsLong() : work.getConstraintEndDateAsLong());
            if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4))
                date = date.nextWorkDate(true);
            PSCalendar start = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._10_0) ? null : date;
            return new PSCalendar[]{start, date};
        }
        Work.Constraint constraint = (Work.Constraint) work.getConstraint();
        if (ver.verGreaterOrEqual(PowerSteeringVersions._10_0) && work.isFolder()) {
            return new PSCalendar[]{null, null}; // TODO
        }

        //ASAP:
        if (Work.Constraint.ASAP.equals(constraint)) {
            PSCalendar s;
            if (ver.verGreaterOrEqual(PowerSteeringVersions._10_0)) {
                s = parent.getCalendar().set(work.hasCreationDate() ? work.getCreationDate() : System.currentTimeMillis()); // #86091
                PSLogger.debug("Creation date for " + work + " is " + s + "," + work.getCreationPSDate());
            } else {
                s = getCalendar(parent, parent.getProjectStartDateAsLong());
            }

            if (s == null) return null;
            s = s.nextWorkDate(true);
            PSCalendar e = s.setWorkDays(duration);
            return new PSCalendar[]{s, e};
        }
        //ALAP:
        if (Work.Constraint.ALAP.equals(constraint)) {
            PSCalendar e = getCalendar(parent, parent.getProjectEndDateAsLong());
            if (e == null) return null;
            e = e.nextWorkDate(false);
            PSCalendar s = e.setWorkDays(-duration);
            return new PSCalendar[]{s, e};
        }

        // FD:
        if (Work.Constraint.FD.equals(constraint)) {
            PSCalendar s = getCalendar(parent, constraintStart);
            PSCalendar e = getCalendar(parent, constraintEnd);
            return new PSCalendar[]{s, e};
        }

        // FNET, FNLT, MFO:
        // todo: check this for 10.0, at least for fnlt this logic is incorrect now
        if (constraintEnd != null && constraintStart == null) {
            PSCalendar e = getCalendar(parent, constraintEnd).nextWorkDate(false);
            PSCalendar s = getCalendar(parent, constraintEnd).set(-duration);
            return new PSCalendar[]{s, e};
        }
        //SNET, SNLT, MSO:
        if (constraintStart != null && constraintEnd == null) {
            PSCalendar s;
            if (ver.verGreaterOrEqual(PowerSteeringVersions._10_0)) {
                s = parent.getCalendar().set(work.hasCreationDate() ? work.getCreationDate() : System.currentTimeMillis()); // #86091
            } else {
                s = getCalendar(parent, constraintStart);
            }
            PSCalendar e = s.set(duration);
            if (Work.Constraint.SNET.equals(constraint)) {
                return new PSCalendar[]{s.nextWorkDate(true), e.nextWorkDate(true)};
            }
            return new PSCalendar[]{s, e.nextWorkDate(true)};
        }
        return null;
    }

    protected PSCalendar getCalendar(Work work, String date) {
        if (date == null || date.isEmpty()) return null;
        return work.getCalendar().set(date);
    }

    protected PSCalendar getCalendar(Work work, Long date) {
        if (date == null) return null;
        return work.getCalendar().set(date);
    }

    protected void moveWorkInGantt(WBSPage pc, Work work, int position, float diffPcs) {
        int wasX = pc.getGantt().getPosition(work)[0];
        int wasWidth = pc.getGantt().getPosition(work)[1];

        if (work.isConstraintsDisabled()) {
            PSLogger.warn("Skipp moving for project " + work);
            return;
        }

        PSLogger.info("Try to move work: " + work.getName() + ", Was: " + wasX);
        pc.getGantt().move(work, position);
        Assert.assertTrue(pc.isSaveAreaEnabled(), "Save is not enabled after changing");
        int newX = pc.getGantt().getPosition(work)[0];
        int newWidth = pc.getGantt().getPosition(work)[1];
        int shouldX = wasX + position;

        PSLogger.info("Work: " + work.getName() + ", Should: " + shouldX + ", Really: " + newX + ", Was: " + wasX);
        List<String> messages = pc.getMessagesFromTopAndClose();
        PSLogger.info(messages);

        if (work.getConstraint() != null && !Work.Constraint.SNET.equals(work.getConstraint()))
            Assert.assertTrue(messages.size() != 0, "Can't find any message after moving " + work);
        else {
            if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4))
                PSLogger.warn(messages);
            else
                Assert.assertTrue(messages.size() == 0, "There are some messages");
        }

        // if FD then SNET
        // in 9.3 and later constraint duration is constant, do not check...:
        if (TestSession.getAppVersion().verLessOrEqual(PowerSteeringVersions._9_2) && Work.Constraint.FD.equals(work.getConstraint()))
            Assert.assertTrue(newWidth < wasWidth, "Incorrect width after moving for " + work.getName() + ": new width=" + newWidth + ", was width=" + wasWidth);
        /*else
Assert.assertEquals(newWidth, wasWidth, "Incorrect width after moving for " + work.getName());*/


        Assert.assertTrue(Math.abs(newX - shouldX) / position < diffPcs, "Incorrect position after moving for work " + work.getName());
    }

    protected void changeDurationInGantt(WBSPage pc, Collection<Work> works, int shouldWidth) {
        changeDurationInGantt(pc, works, shouldWidth, true);
    }

    protected void changeDurationInGantt(WBSPage pc, Collection<Work> works, int shouldWidth, boolean doThrow) {
        for (Work work : works) {
            if (work.isMilestone() || work.isConstraintsDisabled()) continue;
            int wasWidth = pc.getGantt().getPosition(work)[1];
            PSLogger.info("Work: " + work.getName() + ", Was: " + wasWidth);
            if (wasWidth == shouldWidth) {
                PSLogger.warn("Incorrect width specified: the same as was (" + shouldWidth + ")");
                return;
            }
            try {
                pc.getGantt().resize(work, shouldWidth);
                Assert.assertTrue(pc.isSaveAreaEnabled(), "Save is not enabled after changing");
            } catch (AssertionError t) {
                if (doThrow) throw t;
                PSLogger.error(t);
            }
        }
    }

    protected void changeDurationInGantt(WBSPage pc, Collection<Work> works, int shouldWidth, int diffPcs) {
        changeDurationInGantt(pc, works, shouldWidth);
        for (Work work : works) {
            if (work.isMilestone() || work.isConstraintsDisabled()) continue;
            int newWidth = pc.getGantt().getPosition(work)[1];
            PSLogger.info("Work: " + work.getName() + ", Should: " + shouldWidth + ", Really: " + newWidth);
            Assert.assertTrue(Math.abs(shouldWidth - newWidth) < diffPcs,
                    "Incorrect length for work " + work.getName() + " after changing in gantt");
        }
    }

    protected List<WorkDependency> makeDependencyUsingGantt(WBSPage pc, Work main, List<Work> works) {
        PSLogger.info("Make dependency for work " + main + " : " + works);
        List<WorkDependency> deps = new ArrayList<WorkDependency>();
        for (Work work : works) {
            deps.add(new WorkDependency(work));
            pc.getGantt().getPosition(work);
            pc.getGantt().getPosition(main);
            pc.getGantt().makeDependency(work, main);
            Assert.assertTrue(pc.isSaveAreaEnabled(), "Save is not enabled after make dependency in gantt");

            simpleCheckErrorsAfterChangingDependency(pc);
            /*List<String> errors = pc.getErrorMessagesFromTop();
            pc.closeMessages();
            String error = getErrorAfterChangingDependency(pc, work, main);
            PSLogger.info("From page: " + errors);
            PSLogger.info("Expected : " + error);
            errors.add(null);
            Assert.assertTrue(errors.contains(error), "Incorrect errors on page");*/

            if (work.isMilestone() || main.isMilestone()) continue;
            int x1 = pc.getGantt().getPosition(main)[0];
            int x2 = pc.getGantt().getPosition(work)[0];
            Assert.assertTrue(x2 <= x1, "Incorrect coordinates for works " + main.getName() + " and " + work.getName());
        }
        Collections.sort(deps);
        //String res = indexes.toString().replace(",", ";").replace("[", "").replace("]", "");
        PSLogger.debug("Expected dependency " + deps);
        return deps;
    }

    private static void simpleCheckErrorsAfterChangingDependency(WBSPage pc) {
        List<String> errors = pc.getErrorMessagesFromTop();
        pc.closeMessages();
        PSLogger.info("From page: " + errors);
        /*for (int i = 0; i < errors.size(); ) {
            if ((task != null && errors.get(i).matches(task)) || errors.get(i).matches(MESSAGE_ERROR_MOVE_DEPENDENCY_PATTERN.toPattern())) {
                errors.remove(i);
            } else {
                i++;
            }
        }*/
        //Assert.assertTrue(errors.size() == 0, "Incorrect errors from page");
    }

    private static String getErrorAfterChangingDependency(WBSPage pc, Work work1, Work work2) {
        if (work2.getConstraintStartDate() == null) return null;
        if (work1.isFolder()) return null;
        int[] coords1 = pc.getGantt().getPosition(work1);
        int[] coords2 = pc.getGantt().getPosition(work2);
        int x1 = coords1[0];
        if (work1.isMilestone()) {
            x1 -= GRID_GANTT_MILESTONE_WIDTH.getInt();
        } else {
            x1 += coords1[1];
        }
        int x2 = coords2[0];
        if (work2.isMilestone()) {
            x2 += GRID_GANTT_MILESTONE_WIDTH.getInt();
        }
        if (x1 > x2) {
            return MESSAGE_ERROR_MOVE.replace(work1.getRowIndex()) + " " +
                    MESSAGE_ERROR_DEPENDENCY.replace(work2.getRowIndex());
        }
        return null;
    }

    protected void setOnTrackStatusUsingGantt(WBSPage pc, List<Work> works, float diff) {
        PSLogger.info("Set status 'On Track' for works " + works);
        for (Work work : works) {
            if (work.isMilestone()) continue;
            if (work.isFolder()) continue;
            float wasPercentage = pc.getGantt().getPercentageCompleted(work);
            float toSet = (10F + TestData.getRandom().nextInt(80)) / 100F;
            PSLogger.info("Work: " + work.getName() + ", percentage was : " + wasPercentage);
            pc.getGantt().setPercentageCompletion(work, toSet);

            List<String> messages = pc.getMessagesFromTopAndClose();
            PSLogger.info(messages);
            try {
                Assert.assertTrue(messages.size() != 0, "Can't find any message after setting % of completion for " + work);
            } catch (AssertionError t) {
                if (TestSession.getAppVersion().verLessOrEqual(PowerSteeringVersions._9_2)) {
                    PSLogger.error(t);
                    PSLogger.knis(80826);
                } else {
                    throw t;
                }
            }

            Assert.assertTrue(pc.isSaveAreaEnabled(), "Save is not enabled after changing status in gantt");
            float newPercentage = pc.getGantt().getPercentageCompleted(work);
            int status = pc.getGantt().getStatus(work);
            PSLogger.info("Work: " + work.getName() + ", percentage new : " + newPercentage);
            Assert.assertEquals(status, 1, "Incorrect status, should be 'on track'");
            Assert.assertTrue(Math.abs(newPercentage - toSet) < diff,
                    "Incorrect percentage after setting, should be " + toSet);
        }
    }

    protected void setCompletedStatusUsingGantt(WBSPage pc, List<Work> works) {
        PSLogger.info("Set status 'Completed' for works " + works);
        for (Work work : works) {
            if (work.isMilestone()) continue;
            if (work.isFolder()) continue;
            pc.getGantt().setPercentageCompletion(work, 1.01F);

            List<String> messages = pc.getMessagesFromTopAndClose();
            PSLogger.info(messages);
            try {
                Assert.assertTrue(messages.size() != 0, "Can't find any message after setting % of completion for " + work);
            } catch (AssertionError t) {
                if (TestSession.getAppVersion().verLessOrEqual(PowerSteeringVersions._9_2)) {
                    PSLogger.error(t);
                    PSLogger.knis(80826);
                } else {
                    throw t;
                }
            }

            Assert.assertTrue(pc.isSaveAreaEnabled(), "Save is not enabled after changing status in gantt");
            float newPercentage = pc.getGantt().getPercentageCompleted(work);
            int status = pc.getGantt().getStatus(work);
            PSLogger.info("Work: " + work.getName() + ", percentage new : " + newPercentage);
            Assert.assertEquals(status, 2, "Incorrect status, should be 'complete'");
            Assert.assertEquals(newPercentage, 1F,
                    "Incorrect percentage after setting, should be 100%");
        }
    }


    @Deprecated
    private PSCalendar getProjectConstraintStartDate(WBSPage pc, Work parent) {
        if (parent.getConstraintStartDate() != null) return getCalendar(parent, parent.getConstraintStartDate());
        String start = null;
        for (Work work : parent.getChildren()) {
            String date = pc.getGrid().getConstraintStartDate(work);
            if (date.isEmpty()) continue;
            if (start == null) {
                start = date;
            } else if (PSCalendar.compare(start, date) > 0) {
                start = date;
            }
        }
        PSLogger.info("Project start date is " + start);
        return parent.setConstraintStartDate(start != null ? getCalendar(parent, start) : null);
    }

    protected List<Work> getRandomTestWorksForParent(Work parent, int size, WorkType... excludes) {
        List<Work> toTest = new ArrayList<Work>();
        List<WorkType> types = Arrays.asList(excludes);
        for (Work w : parent.getChildren()) {
            if (types.contains(w.getWorkType())) {
                continue;
            }
            toTest.add(w);
        }
        while (toTest.size() > size) {
            toTest.remove(TestData.getRandomFromList(toTest));
        }
        PSLogger.debug("Works to test : " + toTest);
        return toTest;
    }


    protected void compareConstraintDatesAndGantt(WBSPage pc, Work parent, float allowedDiff) {
        PSLogger.info("Compare Constraint Dates in grid and gantt");
        // collect grid information:
        Map<String, Float> gridInfo = new HashMap<String, Float>();
        int gridInfoMax = -1;
        PSCalendar startProject = getCalendar(parent, parent.getProjectStartDateAsLong());
        for (Work work : parent.getChildren()) {
            long start;
            long end;
            PSCalendar startWork = getCalendar(parent, pc.getGrid().getConstraintStartDate(work));
            PSCalendar endWork = getCalendar(parent, pc.getGrid().getConstraintEndDate(work));
            int duration = pc.getGrid().getConstraintDuration(work);
            if (work.isMilestone()) {
                Assert.assertEquals(duration, -1, "Incorrect duration for milestone");
                continue;
            }
            if (startWork != null && endWork != null) {
                start = startWork.days();
                end = endWork.days();
            } else {
                if (startWork == null) startWork = startProject;
                start = startWork.days();
                end = startProject.setWorkDays(duration).days();
            }
            int calendarDuration = (int) (end - start) + 1;
            PSLogger.info("Grid duration for work " + work + " in calendar days is " + calendarDuration);
            gridInfoMax = Math.max(gridInfoMax, calendarDuration);
            gridInfo.put(work.getName(), (float) calendarDuration);

        }
        PSLogger.info("Grid: " + gridInfo);

        // collect gantt information:
        WBSPage.Gantt gt = pc.getGantt();
        Map<String, Float> ganttInfo = new HashMap<String, Float>();
        int ganttInfoMax = -1;
        for (Work work : parent.getChildren()) {
            int d = gt.getPosition(work)[1];
            PSLogger.info("Gantt duration for work " + work + " is " + d);
            if (work.isMilestone()) {
                Assert.assertTrue(d <= GRID_GANTT_MILESTONE_WIDTH.getInt(),
                        "Incorrect width for milestone : " + d);
            } else if (work.isConstraintsDisabled()) {
                Assert.assertEquals(d, GRID_GANTT_ZERO_WIDTH.getInt(),
                        "Incorrect width for " + work);
            } else {
                ganttInfoMax = Math.max(ganttInfoMax, d);
            }
            ganttInfo.put(work.getName(), (float) d);
        }

        PSLogger.info("Gantt: " + ganttInfo);

        // compare:
        for (String work : gridInfo.keySet()) {
            float grid = gridInfo.get(work) / gridInfoMax;
            float gantt = ganttInfo.get(work) / ganttInfoMax;
            float diff = Math.abs(grid - gantt);
            PSLogger.info("For work " + work + " grid duration is " + grid + ", gantt duration is " + gantt +
                    ", diff is " + diff);
            Assert.assertTrue(diff < allowedDiff,
                    "Difference in durations for work " + work);
        }
    }

    protected void compareScheduledDatesAndGantt(WBSPage pc, Work parent, float defAllowedDiff) {
        PSLogger.info("Compare Scheduled Dates in grid and gantt");
        // collect grid information:
        List<List<Float>> gridInfo = new ArrayList<List<Float>>();
        List<Work> children = new ArrayList<Work>();
        gridInfo.add(Arrays.asList(0f, 1f));
        long x_0 = getCalendar(parent, pc.getGrid().getScheduledStartDate(parent)).days();
        long w_0 = getCalendar(parent, pc.getGrid().getScheduledEndDate(parent)).days() - x_0 + 1;
        for (Work work : parent.getChildren()) {
            String end = pc.getGrid().getScheduledEndDate(work);
            String start = pc.getGrid().getScheduledStartDate(work);
            PSLogger.info("Work " + work.getName() + ", scheduled dates : " + start + ", " + end);
            long x_i;
            long w_i;
            if (work.isConstraintsDisabled()) { // folder in 10.0  (#85854)
                Assert.assertTrue(start.isEmpty() && end.isEmpty(), "Some dates are present for project " +
                        work.getName() + ": " + start + ", " + end);
                continue;
            }
            if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_0) && work.isMilestone()) {
                if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4)) {
                    Assert.assertTrue(start.isEmpty(), "Scheduled start should be empty for milestone");
                }
                PSCalendar c = getCalendar(parent, end);
                c.toStart();
                x_i = c.days() + 1;
                w_i = 1;
            } else {
                x_i = getCalendar(parent, start).days();
                w_i = getCalendar(parent, end).days() - x_i + 1;
            }
            float x = (float) (x_i - x_0) / w_0;
            float w = (float) w_i / w_0;
            gridInfo.add(Arrays.asList(x, w));
            children.add(work);
        }

        // collect gantt information:
        WBSPage.Gantt gt = pc.getGantt();
        gt.scrollToCenter();
        List<List<Float>> ganttInfo = new ArrayList<List<Float>>();
        ganttInfo.add(Arrays.asList(0f, 1f));
        List<Float> allowedDiffs = new ArrayList<Float>();
        allowedDiffs.add(0F);
        int[] xw_0 = gt.getPosition(parent);
        for (Work work : parent.getChildren()) {
            int[] xw_i = gt.getPosition(work);
            if (work.isConstraintsDisabled()) { // folder in 10.0  (#85854)
                Assert.assertTrue(xw_i[1] == 0, "Gantt is present for project " + work.getName() + ", " +
                        "width: " + xw_i[1]);
                continue;
            }
            float x = (float) (xw_i[0] - xw_0[0]) / xw_0[1];
            float w = (float) xw_i[1] / xw_0[1];
            ganttInfo.add(Arrays.asList(x, w));
            if (work.isMilestone()) {
                float milestoneDiff = (float) GRID_GANTT_MILESTONE_WIDTH.getInt() / xw_0[1];
                if (xw_i[1] != GRID_GANTT_MILESTONE_WIDTH.getInt()) {
                    PSLogger.warn("Incorrect width for milestone: " + xw_i[1]);
                } // for ie and ps 9.1 width for milestone is 5pcs
                Assert.assertTrue(xw_i[1] <= GRID_GANTT_MILESTONE_WIDTH.getInt(), "Incorrect width for milestone");
                allowedDiffs.add(milestoneDiff);
            } else {
                allowedDiffs.add(defAllowedDiff);
            }
        }

        PSLogger.info("From gantt : " + ganttInfo);
        PSLogger.info("From grid (scheduled dates) : " + gridInfo);
        Assert.assertTrue(ganttInfo.size() == gridInfo.size(), "Something is incorrect with gantt or grid");
        for (int i = 1; i < ganttInfo.size(); i++) {
            Work ch = children.get(i - 1);
            String workName = ch.getName() + "," + ch.getType();
            PSLogger.info(workName + " gantt: " + ganttInfo.get(i) + ", grid: " + gridInfo.get(i));
            float xDiff = Math.abs(ganttInfo.get(i).get(0) - gridInfo.get(i).get(0));
            float wDiff = Math.abs(ganttInfo.get(i).get(1) - gridInfo.get(i).get(1));
            if (ch.isMilestone()) continue; // todo: do not check milestone.
            Assert.assertTrue(xDiff <= allowedDiffs.get(i),
                    "Incorrect scheduled start date in gantt or grid for work " +
                            workName + ": diff is " + xDiff + ", should be " + allowedDiffs.get(i)
            );
            Assert.assertTrue(wDiff <= allowedDiffs.get(i),
                    "Incorrect scheduled duration in gantt or grid for work " +
                            workName + ": diff is " + wDiff + ", should be " + allowedDiffs.get(i)
            );
        }
    }

    public void afterActionsInGantt() {
        if (CoreProperties.getBrowser().isFF() && LocalServerUtils.isDisplayEnabled()) {
            PSLogger.info("After gantt method");
            LocalServerUtils.resumeF11();
        }
    }

    public void renameWork(String suffix, Work... works) {
        WBSPage pc = WorkManager.openWBS(works[0]);
        WBSPage.Columns columns = pc.getOptions().getColumns();
        columns.uncheckAll();
        columns.getCheckbox(OPTIONS_BLOCK_COLUMNS_PROJECT_NAME).click();
        columns.apply();
        List<String> expected = pc.getGrid().getListTree();
        for (Work work : works) {
            String name = work.getName() + suffix;
            PSLogger.info("Set name '" + name + "' for work " + work);
            expected.set(expected.indexOf(work.getName()), name);
            pc.getGrid().setName(work, name);
            Assert.assertEquals(pc.getGrid().getName(work), name, "Incorrect name after setting new");
        }
        Assert.assertTrue(pc.isSaveAreaEnabled(), "Save is not enabled after renaming");
        pc.saveArea();
        List<String> after = pc.getGrid().getListTree();
        PSLogger.info("Tree after renaming : " + after);
        Assert.assertEquals(after, expected, "Incorrect tree after saving");
        for (Work work : works) {
            work.setName(work.getName() + suffix);
            WorkManager.open(work);
        }
    }

    protected void testWorkStatusAndReset(WBSPage pc, Work parent, Work work, List<String> toTest) {

        List<Work> works = new ArrayList<Work>(parent.getChildren());
        works.add(parent);
        List<String> before = getAllStatuses(works);
        int parentIndex = works.indexOf(work);

        assertAllStatusesInGrid(pc.getGrid(), before);

        List<String> after = new ArrayList<String>(before);

        PSLogger.info("Set statuses : " + toTest);
        for (String st : toTest) {
            PSLogger.info("Make " + work.getName() + " " + st);
            pc.getGrid().setStatus(work, st);
            after.set(parentIndex, st);
            assertAllStatusesInGrid(pc.getGrid(), after);
        }
        pc.resetArea();
        assertAllStatusesInGrid(pc.getGrid(), before);
    }

    private void assertAllStatusesInGrid(WBSPage.Grid grid, List<String> expectedStatuses) {
        List<String> statusesFromPage = grid.getAllStatusesFromPage();
        PSLogger.info("Statuses from page : " + statusesFromPage);
        PSLogger.info("Expected : " + expectedStatuses);
        Assert.assertEquals(statusesFromPage, expectedStatuses, "Incorrect statuses on page");
    }

    private List<String> getAllStatuses(List<Work> works) {
        Work.sortByRowIndex(works);
        List<String> res = new ArrayList<String>();
        for (Work work : works) {
            String st = work.getStatus().getValue();
            res.add(st);
        }
        return res;
    }

    public void checkShowLevelAndValidateTree(Work parent) {
        WBSPage pc = WorkManager.openWBS(parent);
        Work root = pc.makeTreeByShowLevel();
        String actual = root.print();
        PSLogger.info(actual);
        Assert.assertEquals(actual, parent.print(), "Incorrect structure of tree");
    }

    public void checkDisplayFromHereAndParentAndValidateTree(Work parent) {
        // search child with children to validate this functionality
        Work child = null;
        for (Work ch : parent.getChildren()) {
            if (ch.getChildren().size() != 0) {
                child = ch;
                break;
            }
        }
        if (child == null) {
            PSSkipException.skip("Incorrect parent was specified for this testcase");
        }
        WBSPage pc = WorkManager.openWBS(parent);
        Work root = pc.getGrid().
                openWBSPageByDisplayFromHere(child).makeTreeByDisplayFromParent();
        String tree2 = root.print();
        PSLogger.info(tree2);
        Assert.assertEquals(tree2, child.print(), "Incorrect structure of tree");
    }

    protected void checkSaveAfterDeletingSomeWork(WBSPage pc) {
        // for 9.2 and 9.3 now i do not see that save/reset enabled after deleting.
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_2)) {
            if (pc.isSaveAreaEnabled()) {
                PSLogger.warn("Save is enabled");
                pc.saveArea();
            }
            return;
        }
        try {
            Assert.assertTrue(pc.isSaveAreaEnabled(), "'Save' is disabled after deleting");
            Assert.assertTrue(pc.isResetAreaEnabled(), "'Reset' is disabled after deleting");
            pc.saveArea();
        } catch (AssertionError e) {
            if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_1)) {
                // according #74268 and #76728 save and reset should be enabled after bulk and group deleting
                throw new PSKnownIssueException(76728, e);
            } else {
                // on 8.0-9.0 save should be enabled in any case
                throw e;
            }
        }
    }

    public static void setOwner(WBSPage pc, Work work, User newOwner, boolean immediateDelegation) {
        pc.getGrid().setOwner(work, newOwner.getFullName());
        if (immediateDelegation) {
            String fromPage = pc.getGrid().getOwner(work);
            Assert.assertEquals(fromPage, newOwner.getFullName(), "incorrect owner for " + work + ", after setting " + newOwner);
        } else {
            String exMessage = WBSEPageLocators.MESSAGE_CHANGE_OWNER.replace(newOwner.getFullName(), work.getName());
            List<String> messages = pc.getMessagesFromTop();
            PSLogger.info("Messages: " + messages);
            Assert.assertTrue(messages.size() == 1, "Should be 1 message at page");
            Assert.assertEquals(messages.get(0), exMessage, "Incorrect message");
        }
        //work.setOwner(newOwner);
    }

}
