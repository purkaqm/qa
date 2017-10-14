package com.powersteeringsoftware.tests.project_central.actions;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.DatePicker;
import com.powersteeringsoftware.libs.enums.page_locators.InboxPageLocators;
import com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.WorkDependency;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.HistoryWorkPage;
import com.powersteeringsoftware.libs.pages.InboxPage;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.tests.project_central.TestData;
import org.testng.Assert;

import java.util.*;

import static com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators.*;

/**
 * Class for actions in MY_MAIN_WORK work grip
 * User: szuev
 * Date: 24.05.2010
 * Time: 17:07:14
 */
public class MyGridTCs extends GeneralGridTCs {

    public void setStatuses(Work main) {

        Work[] works = new Work[]{
                main,   //0
                main.getChild("A"),     //1
                main.getChild("E"),     //2
                main.getChild("F"),     //3
                main.getChild("C"),     //4
                main.getChild("B"),     //5 <--do not change status for B
        }; // D is a folder without status
        for (Work w : works) {
            if (w == null || !w.exist())
                PSSkipException.skip("Can't find " + (w == null ? "a child" : "work " + w.getName()) + " for test");
        }
        List<Work.Status> statuses = Work.Status.getList();
        statuses.remove(Work.Status.COMPLETED);
        statuses.remove(Work.Status.PROPOSED);

        List<Work.Status> rStatuses = statuses.subList(0, 4);
        rStatuses.add(Work.Status.COMPLETED);
        TestData.mixList(rStatuses);

        rStatuses.add(Work.Status.PROPOSED); // last status for B


        PSLogger.info("Set " + rStatuses + " for " + Arrays.asList(works));
        WBSPage pc = WorkManager.openWBS(main);
        pc.getGrid().setStatus(works[0], rStatuses.get(0));
        pc.getGrid().setStatus(works[1], rStatuses.get(1));
        AuxiliaryTCs.saveAndCheckBugLike73593(pc);
        pc.getGrid().setStatus(works[2], rStatuses.get(2));
        AuxiliaryTCs.saveAndCheckBugLike73593(pc);
        pc.getGrid().setStatus(works[3], rStatuses.get(3));
        pc.getGrid().setStatus(works[4], rStatuses.get(4));
        AuxiliaryTCs.saveAndCheckBugLike73593(pc);

        for (int i = 0; i < works.length; i++) {
            Assert.assertEquals(pc.getGrid().getStatus(works[i]),
                    rStatuses.get(i).getValue(), "Incorrect status in row #" + works[i].getGeneralIndex() +
                            ": expected " + rStatuses.get(i)
            );
            works[i].setStatus(rStatuses.get(i));
        }
    }

    public void testStatusInInformationPopup(Work main) {
        Work[] works = new Work[]{
                main,   //0
                main.getChild("A"),     //1
                main.getChild("E"),     //2
                main.getChild("F"),     //3
                main.getChild("C"),     //4
                main.getChild("B"),     //5
        }; // D is a folder without status

        WBSPage pc = WorkManager.openWBS(main);
        for (Work work : works) {
            WBSPage.InformationDialog dialog = pc.getGrid().callSubMenu(work.getName()).information();
            List<String> info = dialog.getWorkInfo();
            dialog.ok();
            PSLogger.info("For " + work + " information popup: " + info);
            Assert.assertEquals(info.get(1), work.getStatus().getValue(), "Incorrect status in info popup");
        }
    }

    public void testInformationPopup(Work parent, User u) {
        testInformationPopup(parent);
        // seems, it is unnecessary checking:
        //AuxiliaryTCs.setNewUserName(u);
        //testInformationPopup(WorkManager.openWBS(parent), parent, parent.getChildren().get(0));
    }

    public void setTags(Work main, PSTag tg1, PSTag tg2) {
        WBSPage pc = WorkManager.openWBS(main);

        Work[] works = new Work[]{
                main,   //0
                main.getChild("A"),     //1
                main.getChild("D"),     //2
                main.getChild("E"),     //3
                main.getChild("C"),     //4
                main.getChild("B"),     //5
        };

        WBSPage.Columns columns = pc.getOptions().getColumns();
        Assert.assertNotNull(columns.getCheckbox(OPTIONS_BLOCK_COLUMNS_TAGS), "Can't find " + OPTIONS_BLOCK_COLUMNS_TAGS.getLocator() + " checbox");
        columns.uncheckAll();
        columns.getCheckbox(OPTIONS_BLOCK_COLUMNS_TAGS).click();
        columns.apply();

        pc.hideGantt(); // v12, web-driver.

        PSLogger.info("Test saving for not multiple tag");
        pc.getGrid().setTags(works[4], tg2.getChilds().get(1));
        works[4].setTags(tg2.getChilds().get(1));
        checkTagAfterSetting(pc, works[4], tg1, tg2);
        pc.saveArea();
        checkTagAfterSetting(pc, works[4], tg1, tg2);

        PSLogger.info("Test saving for multiple tag");
        pc.getGrid().setTags(works[2], tg1.getChilds().get(1));
        works[2].setTags(tg1.getChilds().get(1));
        checkTagAfterSetting(pc, works[2], tg1, tg2);
        pc.saveArea();
        checkTagAfterSetting(pc, works[2], tg1, tg2);

        PSLogger.info("Test group tags saving");
        pc.getGrid().setTags(works[0], tg2.getChilds().get(0));
        pc.getGrid().setTags(works[0], tg1);
        works[0].setTags(tg1, tg2.getChilds().get(0));

        /*
        //todo: doesn't work. why?
        pc.getGrid().setTags(works[1], tg2.getChildren().get(1));
        works[1].setTags(tg2.getChildren().get(1));
        checkTagAfterSetting(pc, works[1]);
         */

        pc.getGrid().setTags(works[3], tg1.getChilds().get(0), tg1.getChilds().get(2));
        works[3].setTags(tg1.getChilds().get(0), tg1.getChilds().get(2));

        pc.getGrid().setTags(works[5], tg2.getChilds().get(2));
        works[5].setTags(tg2.getChilds().get(2));

        pc.saveArea();
        checkTagAfterSetting(pc, works[0], tg1, tg2);
        checkTagAfterSetting(pc, works[3], tg1, tg2);
        checkTagAfterSetting(pc, works[5], tg1, tg2);
        checkTagAfterSetting(pc, works[1], tg1, tg2);
    }

    private static void checkTagAfterSetting(WBSPage pc, Work work, PSTag tg1, PSTag tg2) {
        List<PSTag> expected = work.getTags();
        List<PSTag> fromPage = pc.getGrid().getTags(work, tg1, tg2);
        PSLogger.info("for work " + work.getName() + " should be " + expected);
        PSLogger.info("for work " + work.getName() + " from page " + fromPage);
        Collections.sort(expected);
        Collections.sort(fromPage);
        Assert.assertEquals(fromPage, expected, "Incorrect lists of tags for work " + work);
    }


    public void setOwner(Work main, User newUser) {
        Work[] works = new Work[]{
                main,//0
                main.getChild("A"),     //1
                main.getChild("D"),     //2
                main.getChild("E"),     //3
                main.getChild("C"),     //4
                main.getChild("B"),     //5
                main.getChild("F"),     //6
        };
        for (Work w : works) {
            if (w == null || !w.exist())
                PSSkipException.skip("Can't find " + (w == null ? "a child" : "work " + w.getName()) + " for test");
        }
        User admin = BasicCommons.getCurrentUser();
        PSLogger.info("Current user is " + admin.getFullName());
        Map<Work, User> map = new HashMap<Work, User>();

        WBSPage pc = WorkManager.openWBS(works[0]);

        String ownerStr = OPTIONS_BLOCK_COLUMNS_PROJECT_OWNER.getLocator();
        WBSPage.Columns display = pc.getOptions().getColumns();
        display.uncheckAll();
        display.getCheckbox(ownerStr).click();
        display.apply();

        List<String> header = pc.getGrid().getTableHeaderList();
        PSLogger.info("Header: " + header);
        Assert.assertTrue(header.size() == 1 && header.contains(ownerStr), "Incorrect header from page, should be " +
                ownerStr);
        PSCalendar current = PSCalendar.getCurrentEmptyCalendar();
        PSLogger.info("Current date is " + current.getDate());

        setOwner(pc, works[0], newUser, false);
        map.put(works[0], newUser);

        setOwner(pc, works[1], newUser, false);
        map.put(works[1], newUser);

        setOwner(pc, works[4], newUser, false);
        map.put(works[4], newUser);

        // going to reject:
        setOwner(pc, works[2], newUser, false);

        pc.saveArea();
        if (!SeleniumDriverFactory.isNewWindowAllowed()) {
            BasicCommons.logOut();
        } else {
            SeleniumDriverFactory.initNewDriver();
        }
        try {
            PSLogger.info("Accept or reject works on inbox page under " + newUser);
            try {
                BasicCommons.logIn(newUser);
            } catch (Exception e) {
                PSLogger.fatal(e);
                PSSkipException.skip("Skipp tc");
            }
            current.setTimeZone(newUser.getTimeZone());
            PSLogger.info("Expected messages :");
            Map<String, Work> expected = new HashMap<String, Work>();
            for (Work work : new Work[]{works[0], works[1], works[4], works[2]}) {
                String expMsg = InboxPageLocators.QUESTION.replace(admin.getFullName(), work.getName(), current.toString());
                PSLogger.info("Expected message is '" + expMsg + "'");
                expected.put(expMsg, work);
            }

            PSLogger.info("User time " + current);
            InboxPage page = new InboxPage();
            page.open();
            List<InboxPage.QuestionTable> ques = page.getQuestionsTables();
            PSLogger.debug("From page : " + ques);
            Map<Work, InboxPage.QuestionTable> found = new HashMap<Work, InboxPage.QuestionTable>();
            for (InboxPage.QuestionTable q : ques) {
                PSLogger.info(q.getQuestion());
                for (String expMsg : expected.keySet()) {
                    if (q.getQuestion().contains(expMsg)) {
                        found.put(expected.get(expMsg), q);
                    }
                }
            }
            PSLogger.debug("Found : " + found);
            Assert.assertEquals(found.size(), 4, "Incorrect questions");
            found.get(works[0]).accept();
            found.get(works[1]).accept();
            found.get(works[4]).accept();
            found.get(works[2]).reject(); // reject for last work
            page.submit();
            BasicCommons.logOut();
        } finally {
            if (!SeleniumDriverFactory.isNewWindowAllowed()) {
                BasicCommons.logIn(admin);
            } else {
                SeleniumDriverFactory.stopLastSeleniumDriver();
            }
        }

        pc = WorkManager.openWBS(works[0]);
        display = pc.getOptions().getColumns();
        display.uncheckAll();
        display.getCheckbox(ownerStr).click();
        display.apply();

        for (Work work : works) {
            User expected = map.containsKey(work) ? map.get(work) : admin;
            String actual = pc.getGrid().getOwner(work);
            Assert.assertEquals(actual, expected.getFullName(), "incorrect owner for " + work +
                    " after submitting, should be " + expected);
            work.setOwner(expected);
        }

        current.setTimeZone(admin.getTimeZone());
        PSLogger.info(admin.getFormatFullName() + " date is " + current);

        PSLogger.info("Validate history event");
        HistoryWorkPage history = pc.openHistory();
        history.clearEndDateFilter();
        history.selectAll();
        List<HistoryWorkPage.Event> events = history.getEvents();
        PSLogger.info("History events from page:");
        for (HistoryWorkPage.Event e : events)
            PSLogger.info(e);
        HistoryWorkPage.Event accept = events.get(0);
        Assert.assertTrue(accept.isDelegationAccept(admin, newUser, works[0], current), "Incorrect accept event");
        HistoryWorkPage.Event ask = events.get(1);
        Assert.assertTrue(ask.isDelegationAsked(admin, newUser, works[0], current), "Incorrect asked event");

    }

    public void deleteChildBranchAndValidateTree(Work main) {
        List<String> works = Arrays.asList(
                main.getChild("I").getName(),
                main.getChild("J").getName(),
                main.getChild("K").getName(),
                main.getChild("L").getName(),
                main.getChild("M").getName(),
                main.getChild("N").getName(),
                main.getChild("O").getName());
        List<String> expectedAfterDeleting = new ArrayList<String>(works);

        WBSPage pc = WorkManager.openWBS(main);
        pc.getOptions().showLevel(8);
        List<String> fromPage = pc.getGrid().getListTree();

        int index = fromPage.indexOf(works.get(0));
        Assert.assertTrue(index != -1, "Can't find work " + works.get(0));

        //to debug:
        WBSPage.Columns display = pc.getOptions().getColumns();
        display.uncheckAll();
        display.getCheckbox(OPTIONS_BLOCK_COLUMNS_DATES_SCHEDULED).click();
        display.apply();

        PSLogger.info("Try to delete " + works.get(1) + " with children for parent " +
                works.get(0));
        pc.getGrid().callSubMenu(works.get(1)).delete().yes();
        expectedAfterDeleting.remove(1);
        expectedAfterDeleting.remove(1);
        expectedAfterDeleting.remove(1);

        checkSaveAfterDeletingSomeWork(pc);

        fromPage = pc.getGrid().getListTree();
        List<String> actualAfterDeleting = fromPage.subList(index, fromPage.size());
        PSLogger.info("From page : " + actualAfterDeleting);
        Assert.assertEquals(actualAfterDeleting,
                expectedAfterDeleting, "Incorrect list of children after first deleting : " + actualAfterDeleting);

        PSLogger.info("Try to delete " + works.get(0) + " with children for parent " +
                main.getName());
        pc.getGrid().callSubMenu(works.get(0)).delete().yes();

        checkSaveAfterDeletingSomeWork(pc);

        fromPage = pc.getGrid().getListTree();

        actualAfterDeleting = fromPage.subList(index, fromPage.size());
        PSLogger.info("From page : " + actualAfterDeleting);
        Assert.assertTrue(actualAfterDeleting.isEmpty(),
                "Incorrect list of children after first deleting : " + actualAfterDeleting);
        main.setDeleted();
        main.setChildrenDeleted();
    }


    /**
     * this testcase can change tree structure, so work with OUR_* tree
     */
    public void testIndentOutdent(Work parent) {
        Work workA = parent.getChildren().get(0);
        Work workB = parent.getChildren().get(1);

        WBSPage pc = WorkManager.openWBS(parent);
        //for debug:
        WBSPage.Columns display = pc.getOptions().getColumns();
        display.uncheckAll();
        display.getCheckbox(OPTIONS_BLOCK_COLUMNS_DATES_SCHEDULED).click();
        display.getCheckbox(OPTIONS_BLOCK_COLUMNS_DATES_CONSTRAINT).click();
        display.apply();

        //check B:
        Assert.assertFalse(pc.getGrid().callSubMenu(workB.getName()).isOutdentPresent(),
                "There is 'outdent' in submenu before indent action");

        pc.getGrid().callSubMenu(workB.getName()).indent();
        Assert.assertFalse(pc.getGrid().callSubMenu(workB.getName()).isIndentPresent(),
                "There is 'indent' in submenu after indent action");
        PSLogger.info(pc.getGrid().getListTree());
        PSLogger.save("after indent");
        pc.checkForPopupError();
        if (!pc.isSaveAreaEnabled())
            PSLogger.error("'Save' is disabled after indent");

        pc.getGrid().callSubMenu(workB.getName()).outdent();
        Assert.assertFalse(pc.getGrid().callSubMenu(workB.getName()).isOutdentPresent(),
                "There is 'outdent' in submenu after outdent action");
        List<String> tree = pc.getGrid().getListTree();
        PSLogger.info(tree);
        PSLogger.save("after outdent");
        pc.checkForPopupError();
        if (!pc.isSaveAreaEnabled())
            PSLogger.error("'Save' is disabled after outdent");

        // check A:
        Assert.assertFalse(pc.getGrid().callSubMenu(workA.getName()).isOutdentPresent(),
                "There is 'outdent' in submenu for work without predecessors");
        Assert.assertFalse(pc.getGrid().callSubMenu(workA.getName()).isIndentPresent(),
                "There is 'indent' in submenu without predecessors");

        Assert.assertTrue(tree.contains(workB.getName()),
                "Can't find " + workB.getName() + " in tree after outdent");

        if (pc.isSaveAreaEnabled())
            pc.saveArea();

    }

    public void testBulkDelete(Work parent) {

        Work workA = parent.getChildren().get(0);
        Work workB = parent.getChildren().get(1);

        WBSPage pc = WorkManager.openWBS(parent);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._8_2)) {
            pc.pushBulkDelete();
            List<String> messages = pc.getMessagesFromTopAndClose();
            PSLogger.info("Messages : " + messages);
            Assert.assertTrue(messages.size() == 1 &&
                            messages.get(0).contains(WBSEPageLocators.MESSAGE_BULK_OPERATION.getLocator()),
                    "Can't find message '" + WBSEPageLocators.MESSAGE_BULK_OPERATION.getLocator() + "'"
            );
        }

        PSLogger.info("Try to delete " + workA.getName() + " and " + workB.getName());
        pc.getGrid().selectWorks(workA, workB);
        pc.bulkDelete().no();
        pc.bulkDelete().yes();
        checkSaveAfterDeletingSomeWork(pc);
        List<String> items = pc.getGrid().getListTree();
        for (Work work : new Work[]{workA, workB}) {
            Assert.assertFalse(items.contains(work.getName()), "failed delete item " + work.getName());
            work.setDeleted();
            parent.removeChild(work);
        }
    }


    public void validateCriticalPaths(Work parent) {
        if (parent.getManualScheduling())
            PSSkipException.skip("This test-case only for work with disabled manual scheduling");

        WBSPage pc = WorkManager.openWBS(parent);

        List<Work> works = new ArrayList<Work>(parent.getChildren());
        works.add(0, parent);
        PSLogger.info(works);
        WBSPage.Gantt gt = pc.getGantt();

        for (Work work : works) {
            Assert.assertFalse(gt.isCriticalPath(work), "There is critical path for " + work);
        }
        WBSPage.Columns options = pc.getOptions().getColumns();
        options.uncheckAll();
        options.apply();
        pc.showGantt();
        pc.showCriticalPath();

        gt = pc.getGantt();
        List<Work> res = new ArrayList<Work>();
        for (Work work : works) {
            PSLogger.info("Check critical path for project " + work);
            if (gt.isCriticalPath(work) != work.getCriticalPath()) {
                res.add(work);
                PSLogger.error("Incorrect critical path for " + work.getName() + ", should be " + work.getCriticalPath());
            }
        }
        if (!res.isEmpty())
            Assert.fail("Incorrect critical path for some projects: " + res);

    }

    public void validateGantt(Work parent) {
        WBSPage pc = WorkManager.openWBS(parent);

        WBSPage.Columns display = pc.getOptions().getColumns();
        display.uncheckAll();
        display.getCheckbox(OPTIONS_BLOCK_COLUMNS_DATES_SCHEDULED).click();
        display.apply();
        pc.showGantt();
        pc.getGantt().scrollToCenter();

        pc.getOptions().fullScreen();

        compareScheduledDatesAndGantt(pc, parent, 0.03F);
        // increment:
        float next;
        float prev = 0;
        while ((next = pc.getSliderValue()) != 1) {
            Assert.assertTrue(next > prev, "Can't zoom out gantt");
            pc.incrementGantt();
            prev = next;
        }
        compareScheduledDatesAndGantt(pc, parent, 0.3F);

        //decrement:
        prev = next;
        do {
            pc.decrementGantt();
            next = pc.getSliderValue();
            Assert.assertTrue(prev > next, "Can't zoom gantt");
            prev = next;
        } while (next != 0.25);
        compareScheduledDatesAndGantt(pc, parent, 0.03F);
        pc.getOptions().standardScreen();
    }

    public void changeDurationInGantt(Work parent) {
        WBSPage pc = WorkManager.openWBS(parent);

        WBSPage.Columns display = pc.getOptions().getColumns();
        display.uncheckAll();
        display.getCheckbox(OPTIONS_BLOCK_COLUMNS_DATES_CONSTRAINT).click();
        display.apply();
        pc.showGantt();
        pc.getGantt().scrollToCenter();
        pc.getOptions().fullScreen();

        //pc.getGantt().validate();
        List<Work> works = getRandomTestWorksForParent(parent, 3, Work.Type.FOLDER);
        changeDurationInGantt(pc, works, 355, 35);
        compareConstraintDatesAndGantt(pc, parent, 0.4F);
        changeDurationInGantt(pc, works, 200, 35);
        compareConstraintDatesAndGantt(pc, parent, 0.4F);
        pc.resetArea();
        Assert.assertFalse(pc.isSaveAreaEnabled(), "Save is enabled after resetting");
        pc.getOptions().standardScreen();
    }

    public void moveWorksInGantt(Work parent) {
        PSLogger.saveFull();

        WBSPage pc = WorkManager.openWBS(parent);

        WBSPage.Columns display = pc.getOptions().getColumns();
        display.uncheckAll();
        display.getCheckbox(OPTIONS_BLOCK_COLUMNS_DATES_CONSTRAINT_TYPE).click();
        //display.getCheckbox(OPTIONS_BLOCK_COLUMNS_DATES_CONSTRAINT).click();
        display.apply();
        pc.showGantt();
        pc.getGantt().scrollToCenter();

        pc.getOptions().fullScreen();

        List<Work> toTest = getRandomTestWorksForParent(parent, 3, Work.Type.FOLDER);
        changeDurationInGantt(pc, toTest, 60);

        PSLogger.save("After resizing works");
        for (Work work : toTest)
            moveWorkInGantt(pc, work, 200, 0.1F);

        // validate constraints types
        for (Work work : toTest) {
            Work.Constraint fromPage = pc.getGrid().getConstraintType(work);
            Work.Constraint expected = work.isMilestone() ? Work.Constraint.FNET : Work.Constraint.SNET;
            PSLogger.info("For work " + work + " : " + fromPage);
            Assert.assertEquals(fromPage, expected,
                    "Incorrect constraint type for work " + work.getName() + " after moving");
        }
        pc.getOptions().standardScreen();
        pc.resetArea();
        Assert.assertFalse(pc.isSaveAreaEnabled(), "Save is enabled after resetting");

    }

    public void makeDependencyInGantt(Work parent) {
        PSLogger.saveFull();

        Work main1 = parent.getChild("C");
        Work main2 = parent.getChild("E"); // << milestone
        List<Work> chs = getRandomTestWorksForParent(parent, 4, Work.Type.FOLDER);

        /*List<Work> chs = new ArrayList<Work>();
        chs.add(parent.getChild("E"));
        chs.add(parent.getChild("F"));
        chs.add(parent.getChild("C"));
        chs.add(parent.getChild("B"));*/

        WBSPage pc = WorkManager.openWBS(parent);

        WBSPage.Columns columns = pc.getOptions().getColumns();
        columns.uncheckAll();
        columns.getCheckbox(OPTIONS_BLOCK_COLUMNS_PROJECT_DEPENDENCY).click();
        columns.apply();
        pc.showGantt();
        pc.getGantt().scrollToCenter();

        pc.getOptions().fullScreen();

        changeDurationInGantt(pc, chs, 60);

        testMakeDependencyUsingGantt(pc, main1, chs);

        if (!pc.getGantt().isRobot()) {
            //todo: skip to simplify. do not check if robot (ie8)
            testMakeDependencyUsingGantt(pc, main2, chs);
        }

        pc.getOptions().standardScreen();
        pc.resetArea();
        Assert.assertFalse(pc.isSaveAreaEnabled(), "Save is enabled after resetting");
    }

    private void testMakeDependencyUsingGantt(WBSPage pc, Work toSet, List<Work> list) {
        list.remove(toSet);
        List<WorkDependency> expected = makeDependencyUsingGantt(pc, toSet, list);
        List<WorkDependency> fromPage = pc.getGrid().getDependencyAsList(toSet);
        PSLogger.info("Dependency from page " + fromPage);
        PSLogger.info("Dependency expected " + expected);
        try {
            Assert.assertEquals(fromPage, expected, "Incorrect dependency for " + toSet + " after setting using gantt");
        } catch (AssertionError ae) {
            if (SeleniumDriverFactory.getDriver().getType().isIE()) {
                Work milestone = null;
                for (Work w : list) {
                    if (w.isMilestone()) {
                        milestone = w;
                        break;
                    }
                }
                expected.removeAll(fromPage);
                if (milestone != null && expected.size() == 1 && expected.get(0).getIndex() == milestone.getRowIndex()) {
                    PSLogger.warn(ae.getMessage());
                    PSLogger.knis(73345);
                    return;
                }
            }
            throw ae;
        }
    }

    public void changeStatusInGantt(Work parent) {
        if (CoreProperties.getBrowser().isIE(8)) {
            //todo: investigate it.
            PSSkipException.skip("This testcase doesn't work under ie 8");
        }

        WBSPage pc = WorkManager.openWBS(parent);

        WBSPage.Columns display = pc.getOptions().getColumns();
        display.uncheckAll();
        display.getCheckbox(OPTIONS_BLOCK_COLUMNS_PROJECT_STATUS).click();
        display.apply();
        pc.showGantt();
        pc.getGantt().scrollToCenter();

        pc.getOptions().fullScreen();

        List<Work> works1 = getRandomTestWorksForParent(parent, 2, Work.Type.FOLDER);
        List<Work> works2 = getRandomTestWorksForParent(parent, 2, Work.Type.FOLDER);
        Set<Work> works = new HashSet<Work>(works1);
        works.addAll(works2);
        PSLogger.info("Works to test: " + works);
        changeDurationInGantt(pc, works, 200, false);

        pc.getGantt().scrollToCenter(); // debug for ie 8
        setOnTrackStatusUsingGantt(pc, works1, 0.1F);
        validateStatusesAfterGantt(pc, works1, Work.Status.ON_TRACK);
        //pc.resetArea();
        //changeDurationInGantt(pc, parent, 300);
        setCompletedStatusUsingGantt(pc, works2);
        validateStatusesAfterGantt(pc, works2, Work.Status.COMPLETED);

        pc.getOptions().standardScreen();
        pc.resetArea();
        Assert.assertFalse(pc.isSaveAreaEnabled(), "Save is enabled after resetting");

    }

    private static void validateStatusesAfterGantt(WBSPage pc, List<Work> works, Work.Status status) {
        for (Work work : works) {
            String fromPage = pc.getGrid().getStatus(work);
            String expected = status.getValue();
            if (work.isMilestone()) {
                expected = Work.Status.PROPOSED.getValue();
            }
            if (work.isFolder()) {
                expected = "";
            }
            Assert.assertEquals(fromPage, expected, "Incorrect status for " + work.getName() +
                    " after setting using grid");
        }

    }

    /**
     * todo: this testcase is not ready
     */
    public void testRunSchedulerAutomatically(Work parent) {
        List<Work> chs = parent.getChildren();


        WBSPage pc = WorkManager.openWBS(parent);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_0)) {
            pc.hideGantt();
        }
        WBSPage.Columns display = pc.getOptions().getColumns();
        display.uncheckAll();
        for (WBSEPageLocators ch : new WBSEPageLocators[]{
                //"status",
                //"actual-dates",
                //"constraint-type",
                OPTIONS_BLOCK_COLUMNS_DATES_CONSTRAINT,
                OPTIONS_BLOCK_COLUMNS_DATES_SCHEDULED}) {
            display.getCheckbox(ch).click();
        }
        display.apply();

        PSLogger.info("Test changes when 'Run Scheduled automatically' is on");
        pc.setRunSchedulerAutomatically(true);
        try {
            testRunSchedulerAutomaticallyOn(pc, chs, parent.getCalendar());
        } catch (AssertionError a) {
            if (parent.getCreationPSDate().toString().equals(PSCalendar.getCurrentEmptyCalendar().toString())) { // the same day
                throw a;
            } else {
                PSLogger.error("Some dates haven't been scheduled as expected:");
                PSLogger.fatal(a); // hotfix:
                pc.resetArea();
            }
        }

        PSLogger.info("Test changes when 'Run Scheduled automatically' is off");
        pc.setRunSchedulerAutomatically(false);
        testRunSchedulerAutomaticallyOff(pc, chs, parent.getCalendar());
    }

    private void testRunSchedulerAutomaticallyOn(WBSPage pc, List<Work> chs, PSCalendar calendar) {
        //TODO: this is not general test
        int diff = 3;

        for (int i = 0; i < chs.size(); i++) {
            Work ch = chs.get(i);
            if (ch.isConstraintsDisabled()) { // folder in 10.0  (#85854)
                String s = pc.getGrid().getCellByName(GRID_TABLE_SCHEDULED_START, ch).getText();
                String e = pc.getGrid().getCellByName(GRID_TABLE_SCHEDULED_START, ch).getText();
                Assert.assertTrue(s.isEmpty() && e.isEmpty(), "Have some dates for work " + ch + ":" + s + "," + e);
                Assert.assertFalse(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_START, ch),
                        "Scheduled Start date is editable for " + ch);
                Assert.assertFalse(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_END, ch),
                        "Scheduled End date is editable for " + ch);
            } else if (ch.getConstraintStartDate() != null) {
                DatePicker dp = pc.getGrid().getConstraintStartDatePicker(ch);
                Assert.assertNotNull(dp, "Can't find constrain date");
                dp.useDatePopup(TestData.getRandom().nextBoolean());
                String was = dp.get();
                PSCalendar now = calendar.set(was).setWorkDays(diff);
                PSLogger.info("Set constraint start date for " + ch.getName() + " to " + now + " (from page " + was + ")");
                dp.set(now.toString());

                Assert.assertTrue(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_START, ch),
                        "Scheduled Start date is not changed for " + ch);
                if (ch.getConstraintEndDate() == null) {
                    Assert.assertTrue(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_END, ch),
                            "Scheduled Start date is not changed for " + ch);
                    Assert.assertFalse(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_DURATION, ch),
                            "Scheduled Duration is changed for " + ch);
                } else {
                    if (TestSession.getAppVersion().verLessOrEqual(PowerSteeringVersions._9_2)) {
                        Assert.assertTrue(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_START, ch),
                                "Scheduled Start date is not changed for " + ch);
                        Assert.assertFalse(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_END, ch),
                                "Scheduled End date is changed for " + ch);
                        Assert.assertTrue(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_DURATION, ch),
                                "Scheduled Duration is not changed for " + ch);
                    } else {
                        Assert.assertTrue(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_START, ch),
                                "Scheduled Start date is not changed for " + ch);
                        Assert.assertTrue(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_END, ch),
                                "Scheduled End date is not changed for " + ch);
                        Assert.assertFalse(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_DURATION, ch),
                                "Scheduled Duration is changed for " + ch);
                    }
                    //pc.resetArea();
                }
            } else if (ch.getConstraintEndDate() != null) {
                DatePicker dp = pc.getGrid().getConstraintEndDatePicker(ch);
                dp.useDatePopup(TestData.getRandom().nextBoolean());
                String was = dp.get();
                PSCalendar now = calendar.set(was).setWorkDays(-1);
                PSLogger.info("Set constraint end date for " + ch.getName() + " to " + now + " (from page " + was + ")");
                dp.set(now.toString());

                Assert.assertTrue(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_END, ch),
                        "Scheduled Start date is not changed for " + ch);
                if (ch.isMilestone()) {
                    if (TestSession.getAppVersion().verLessOrEqual(PowerSteeringVersions._9_3))
                        Assert.assertTrue(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_START, ch),
                                "Scheduled Start date is not changed for " + ch);
                    Assert.assertFalse(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_DURATION, ch),
                            "Scheduled Duration is changed for " + ch);
                } else { // seems, this code is not used:
                    Assert.assertFalse(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_START, ch),
                            "Scheduled Start date is changed for " + ch);
                    Assert.assertTrue(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_DURATION, ch),
                            "Scheduled Duration is not changed for " + ch);
                }
            } else {
                if (i != 0)
                    pc.resetArea();
                int newDuration = ch.getConstraintDuration() + diff;
                PSLogger.info("Set constraint duration for " + ch.getName() + " to " + newDuration);
                pc.getGrid().setConstraintDuration(ch, ch.getConstraintDuration() + diff);

                Assert.assertFalse(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_START, ch),
                        "Scheduled Start date is changed for " + ch);
                Assert.assertTrue(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_END, ch),
                        "Scheduled End date is not changed for " + ch);
                Assert.assertTrue(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_DURATION, ch),
                        "Scheduled Duration is not changed for " + ch);
            }
        }

        pc.resetArea();
    }

    private void testRunSchedulerAutomaticallyOff(WBSPage pc, List<Work> chs, PSCalendar calendar) {
        int diff = 5;
        for (int i = 0; i < chs.size(); i++) {
            Work ch = chs.get(i);
            if (ch.getConstraintStartDate() != null) {
                DatePicker dp = pc.getGrid().getConstraintStartDatePicker(ch);
                dp.useDatePopup(TestData.getRandom().nextBoolean());
                String was = dp.get();
                PSCalendar now = calendar.set(was).setWorkDays(diff);
                PSLogger.info("Set constraint start date for " + ch.getName() + " to " + now + " (from page " + was + ")");
                dp.set(now.toString());

            } else if (ch.getConstraintEndDate() != null) {
                DatePicker dp = pc.getGrid().getConstraintEndDatePicker(ch);
                dp.useDatePopup(TestData.getRandom().nextBoolean());
                String was = dp.get();
                PSCalendar now = calendar.set(was).setWorkDays(-1);
                PSLogger.info("Set constraint end date for " + ch.getName() + " to " + now + " (from page " + was + ")");
                dp.set(now.toString());
            } else {
                int newDuration = ch.getConstraintDuration() + diff;
                PSLogger.info("Set constraint duration for " + ch.getName() + " to " + newDuration);
                pc.getGrid().setConstraintDuration(ch, ch.getConstraintDuration() + diff);
            }
            Assert.assertFalse(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_START, ch),
                    "Scheduled Start date is changed for " + ch);
            Assert.assertFalse(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_END, ch),
                    "Scheduled Start date is changed for " + ch);
            Assert.assertFalse(pc.getGrid().isCellEdited(GRID_TABLE_SCHEDULED_DURATION, ch),
                    "Scheduled Duration is changed for " + ch);

        }
        pc.resetArea();
    }

}
