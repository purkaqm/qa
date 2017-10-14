package com.powersteeringsoftware.tests.project_central.actions;

import com.powersteeringsoftware.libs.elements.SimpleGrid;
import com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.Cost;
import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.objects.Role;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.resources.ResourcePool;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.EstimatedPage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.tests.project_central.TestData;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators.*;

/**
 * Class for actions in grid (with status, deleting popup, view, etc)
 * for depends see TestDriver
 * User: szuev
 * Date: 21.05.2010
 * Time: 12:44:15
 */
public class OurGridTCs extends GeneralGridTCs {

    private WBSPage checkAvailableStatusesInGrid(Work parent) {
        WBSPage pc = WorkManager.openWBS(parent);
        List<String> statuses = pc.getGrid().getAllStatusesFromFirstCell();
        PSLogger.info("Statuses from popup: " + statuses);
        Collections.sort(statuses);
        List<String> expected = Work.Status.getStringList();
        Collections.sort(expected);
        Assert.assertEquals(statuses, expected,
                "Seems incorrect statuses from popup: " + statuses);
        return pc;
    }

    public void checkRootStatusesAndReset(Work parent, List<String> sts) {
        WBSPage pc = checkAvailableStatusesInGrid(parent);
        testWorkStatusAndReset(pc, parent, parent, sts);
    }

    public void testView(Work root) {
        WBSPage pc = WorkManager.openWBS(root);
        SummaryWorkPage sum = pc.getGrid().openSummaryPageByView(root.getName());
        Assert.assertEquals(sum.getContainerHeader(), root.getName(), "Incorrect header of summary page");
        pc = sum.openProjectPlanning();
        Assert.assertEquals(pc.getContainerHeader(), root.getName(),
                "Incorrect header of project central page page");
        SummaryWorkPage sum2 = pc.getGrid().openSummaryPageByView(root.getChild(3).getName());
        Assert.assertEquals(sum2.getContainerHeader(), root.getChild(3).getName(), "Incorrect header of summary page for child " + root.getChild(3).getName());
        WBSPage projectCentral2 = sum2.openProjectPlanning();
        Assert.assertEquals(projectCentral2.getContainerHeader(), root.getChild(3).getName(),
                "Incorrect header of project central page page for child " + root.getChild(3).getName());

    }

    public void setStatusesAndSave(Work workA, List<String> statuses) {
        WBSPage pc = WorkManager.openWBS(workA);
        String was = pc.getGrid().getStatus(workA);
        if (statuses.get(0).equals(was)) {
            statuses.add(statuses.remove(0));
        }
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._13)) {
            statuses.remove(Work.Status.CANCELED.getValue()); //todo: hotfix
        }
        PSLogger.info("Statuses: " + statuses);
        for (String status : statuses) {
            PSLogger.info("Set status : " + status);
            pc.getGrid().setStatus(workA, status);
            AuxiliaryTCs.saveAndCheckBugLike73593(pc);
            String fromPage = pc.getGrid().getStatus(workA);
            PSLogger.info("From page: " + fromPage);
            Assert.assertEquals(fromPage, status, "Incorrect status after setting");
            List<String> errs = pc.getErrorMessagesFromTop();
            Assert.assertTrue(errs.size() == 0, "There are errors: " + errs);
            workA.setStatus(Work.Status.get(status));
        }
    }

    private WBSPage selectTags(Work work, PSTag tg1, PSTag tg2) {
        WBSPage pc = WorkManager.openWBS(work);
        setNeededColumn(pc, OPTIONS_BLOCK_COLUMNS_TAGS);
        for (PSTag t : new PSTag[]{tg1, tg2})
            Assert.assertTrue(pc.getGrid().isTagCellEditable(work, t), "Not editable tag " + t);
        return pc;
    }

    public void setTags(Work workB, PSTag tg1, PSTag tg2) {
        WBSPage pc;
        try {
            pc = selectTags(workB, tg1, tg2);
        } catch (AssertionError ae) {
            if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_1)) throw ae;
            PSLogger.warn(ae); // it is debug!
            pc = selectTags(workB, tg1, tg2);
        }
        List<PSTag> expected = new ArrayList<PSTag>(Arrays.asList(
                tg1.getChilds().get(0),
                tg2.getChilds().get(0)));
        PSLogger.info("Set tags  " + expected);
        pc.getGrid().setTags(workB, expected.get(0));
        pc.getGrid().setTags(workB, expected.get(1));
        pc.saveArea();
        List<PSTag> fromPage = pc.getGrid().getTags(workB, tg1, tg2);
        PSLogger.info("From page: " + fromPage);
        Assert.assertEquals(expected, fromPage, "Incorrect list of tags");
        workB.setTags(expected);
    }

    public void setOwner(Work workC, User ownerToSet) {
        WBSPage pc = WorkManager.openWBS(workC);

        setNeededColumn(pc, OPTIONS_BLOCK_COLUMNS_PROJECT_OWNER);

        PSLogger.info("Set owner '" + ownerToSet.getFullName() + "' for " + workC.getName());
        pc.getGrid().setOwner(workC, ownerToSet.getFullName());
        Assert.assertTrue(pc.isSaveAreaEnabled(), "'Save' button is disabled");
        PSKnownIssueException ex = null;
        try {
            pc.saveArea();
        } catch (PSKnownIssueException e) {
            PSLogger.warn(e.getMessage());
            ex = e;
        }
        String fromPage = pc.getGrid().getOwner(workC);
        Assert.assertEquals(fromPage, ownerToSet.getFullName(),
                "incorrect owner for " + workC + ", after setting " + ownerToSet);
        workC.setOwner(ownerToSet);
        if (ex != null) throw ex;
    }


    /**
     * depends on testFilterNameAnyOf
     */
    public void testDelete(Work workD) {
        WBSPage pc = WorkManager.openWBS(workD);
        Work toDelete = workD.getChildren().get(0);

        PSLogger.info("Try to delete " + toDelete.getName());
        pc.getGrid().callSubMenu(toDelete.getName()).delete().yes();
        List<String> items = pc.getGrid().getListTree();
        Assert.assertFalse(items.contains(toDelete.getName()), "failed delete item " + toDelete.getName());
        workD.removeChild(toDelete);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_1)) {
            // according #74268
            try {
                Assert.assertFalse(pc.isSaveAreaEnabled(), "'Save' is enabled after deleting");
                Assert.assertFalse(pc.isResetAreaEnabled(), "'Reset' is enabled after deleting");
            } catch (AssertionError e) {
                PSLogger.warn(e);
                try {
                    pc.saveArea(); // todo: this is hotfix due to changes in 92
                } catch (AssertionError ae) {
                    if (pc.isSaveAreaEnabled()) {
                        PSLogger.warn(ae); // seems bug like 70167
                        PSLogger.knis(70167);
                        pc.saveArea();
                    } else {
                        throw ae;
                    }
                }
                if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_2)) {
                    items = pc.getGrid().getListTree();
                    Assert.assertFalse(items.contains(toDelete.getName()), "item " + toDelete.getName() + " exists after saving");
                }
                throw new PSKnownIssueException(74268, e);
            }
        } else {
            // according #68646
            Assert.assertTrue(pc.isSaveAreaEnabled(), "'Save' button is not enabled");
            pc.saveArea();
        }
    }

    public WBSPage testDeleteCancel(Work workD) {
        WBSPage pc = WorkManager.openWBS(workD);
        Work toDelete = workD.getChildren().get(0);
        PSLogger.debug(workD + ":" + workD.getOwner());
        PSLogger.debug(toDelete + ":" + toDelete.getOwner());

        PSLogger.info("Try to delete " + toDelete.getFullName() + " and cancel");
        pc.getGrid().callSubMenu(toDelete.getName()).delete().no();
        List<String> items = pc.getGrid().getListTree();
        Assert.assertTrue(items.contains(toDelete.getName()),
                "Can't find item " + toDelete.getName() + " after cancel deleting");

        PSLogger.info("Try to delete " + toDelete.getFullName() + " and close popup");
        pc.getGrid().callSubMenu(toDelete.getName()).delete().close();
        List<String> items2 = pc.getGrid().getListTree();
        Assert.assertTrue(items2.contains(toDelete.getName()),
                "Can't find item " + toDelete.getName() + " after close deleting popup");
        return pc;
    }

    public void validateCosts(Work work) {
        EstimatedPage costs = WorkManager.openEstimatedCosts(work);
        PSLogger.save("Costs page");
        PSLogger.info(costs.getStringRows());
        List<Cost> actual = costs.getCosts(work);
        List<Cost> expected = new ArrayList<Cost>();
        List<Work.Need> resources = work.getResources();
        PSLogger.info("Splits: " + work.getSplitResources());
        PSLogger.info("Needs: " + resources);
        List<Cost> _costs = work.getCosts(Cost.Type.ESTIMATED);
        PSLogger.info("Costs on work(" + work.getName() + "," + work.getCurrency() +
                ") : " + _costs);
        for (Work.Need res : resources) {
            Cost c = res.getCost();
            if (c != null) {
                expected.add(c);
            }
        }
        Collections.sort(actual);
        Collections.sort(expected);
        PSLogger.info("Actual : " + actual);
        PSLogger.info("Expected : " + expected);
        Assert.assertEquals(actual.size(), expected.size(), "incorrect costs on page");

        for (int i = 0; i < expected.size(); i++) {
            Cost e = expected.get(i);
            Assert.assertTrue(actual.contains(e), "Can't find cost " + e);
            if (!_costs.contains(e))
                work.addCost(e);
            /*if (actual.contains(e)) {
                if (!_costs.contains(e))
                    work.addCost(e);
            } else {
                PSLogger.error("Can't find cost " + e);
                Cost a = actual.get(i);
                PSLogger.debug("DEBUG: " + e.equals(a) + "\t" +
                        ",(" + e.getDescription() + ")" + e.getDescription().equals(a.getDescription()) +
                        ",(" + e.getFormattedAmount() + "," + StrUtil.stringToUnicode(e.getFormattedAmount()) +
                        "|" + a.getFormattedAmount() + "," + StrUtil.stringToUnicode(a.getFormattedAmount()) + ")" +
                        e.getFormattedAmount().equals(a.getFormattedAmount()) +
                        ",(" + e.getAmount() + "|" + a.getAmount() + ")" + e.getAmount().equals(a.getAmount()) +
                        ",(" + e.getFormattedDate() + ")" + e.getFormattedDate().equals(a.getFormattedDate()) +
                        ",(" + e.getSDate() + ")" + e.getSDate().equals(a.getSDate()) +
                        ",(" + e.getSEndDate() + ")" + e.getSEndDate().equals(a.getSEndDate()) +
                        ",(" + e.getActivity() + ")" + e.getActivity().equals(a.getActivity()) +
                        ",(" + e.getAddType() + ")" + e.getAddType().equals(a.getAddType()) +
                        ",(" + e.getResource() + ")" + e.getResource().equals(a.getResource()));
            }*/
        }
    }

    public void setControlCost(Work root, Work work) {
        WBSPage pc = WBSPage.openWBSPage(root);
        setNeededColumn(pc, OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_CONTROLS, OPTIONS_BLOCK_COLUMNS_CONTROLS_CONTROL_COST);
        PSLogger.info("Set control cost to Yes for " + work.getName());
        if (!pc.getGrid().isCellEditable(GRID_TABLE_CONTROL_COST, work)) {
            pc.getGrid().getSingleStatusSelector(GRID_TABLE_INHERIT_CONTROLS, work).setLabel(CONTROL_NO);
        }
        pc.getGrid().getSingleStatusSelector(GRID_TABLE_CONTROL_COST, work).setLabel(CONTROL_YES);
        pc.saveArea();
        work.setInheritControls(false);
        work.setControlCost(true);
        PSLogger.save("After enabling costs");
        String actual = pc.getGrid().getSingleStatusSelector(GRID_TABLE_CONTROL_COST, work).getContent();
        Assert.assertEquals(actual, CONTROL_YES.getLocator(), "Incorrect cost setting");

    }

    public void setResourcePool(Work work, ResourcePool pool) {
        SummaryWorkPage sum = WorkManager.open(work);
        WorkManager.enablePlanResources(sum, work);
        WBSPage pc = sum.openProjectPlanning();

        setNeededColumn(pc, OPTIONS_BLOCK_COLUMNS_RESOURCE_POOL);

        PSLogger.info("Set resource pool '" + pool + "' for " + work.getName());
        pc.getGrid().setResourcePool(work, pool.getName());
        pc.saveArea();
        String fromPage = pc.getGrid().getResourcePool(work);
        Assert.assertEquals(fromPage, pool.getName(),
                "incorrect resource pool for " + work + ", after setting " + pool);
        work.getResource().setPool(pool);
    }

    public void setResourcePerson(Work root, Work work, User person) {
        SummaryWorkPage sum = WorkManager.open(root);
        WorkManager.enablePlanResources(sum, root);
        WBSPage pc = sum.openResourcePlanning();
        Work resource = work.getSplitResources().get(1);
        pc.getGrid().setPerson(resource, person);
        TimerWaiter.waitTime(2000);
        try {
            pc.saveArea();
        } catch (AssertionError ae) {
            PSLogger.error(ae);
            if (pc.isSaveAreaEnabled()) {
                pc.saveArea(); // ignore this bug.
            } else {
                throw ae;
            }
        }
        resource.getResource().setPerson(person);
        List<Work> works = new ArrayList<Work>();
        works.add(work);
        works.addAll(work.getSplitResources());
        validateResources(works, pc);
    }

    public static void addSplit(Work root, Work work) {
        if (work == null) work = root;
        SummaryWorkPage sum = WorkManager.open(root);
        WorkManager.enablePlanResources(sum, root);
        WBSPage pc = sum.openResourcePlanning();
        pc.getGrid().callSubMenu(work.getName()).addSplit();
        pc.saveArea();
        // validate
        pc.getOptions().showLevel(8);
        PSLogger.save("After add split");
        List<String> actual = pc.getGrid().getListTree();
        PSLogger.info("List from page : " + actual);
        for (Work w : work.splitResources()) {
            work.addChild(w);
        }
        List<String> expected = Work.getListTree(root, true, true, true);
        for (int i = 0; i < expected.size(); i++) {
            if (expected.get(i).equals(Work.RESOURCE_SPLIT_NAME)) {
                expected.set(i, WBSEPageLocators.GRID_RESOURCE_SPLIT.getLocator());
            }
        }
        PSLogger.info("Expected work list : " + expected);
        Assert.assertEquals(actual, expected, "Incorrect work list");
        List<Work> works = new ArrayList<Work>();
        works.add(work);
        works.addAll(work.getChildren(true, true));
        validateResources(works, pc);
    }

    private static void validateResources(List<Work> works, WBSPage pc) {
        pc.hideGantt();
        for (Work w : works) {
            String aRole = pc.getGrid().getRole(w);
            String aPool = pc.getGrid().getResourcePool(w);
            String aPerson = pc.getGrid().getPerson(w);
            PSLogger.info("Actual(" + w.getFullName() + "): " + aRole + "," + aPool + "," + aPerson);
            Role eRole = w.getResource(true).getRole();
            ResourcePool ePool = w.getResource(true).getPool();
            User ePerson = w.getResource(true).getPerson();
            Assert.assertEquals(aRole, eRole == null ? null : eRole.getName(), "Incorrect role for " + w.getFullName());
            Assert.assertEquals(aPool, ePool == null ? null : ePool.getName(), "Incorrect pool for " + w.getFullName());
            Assert.assertEquals(aPerson, ePerson == null ? "" : ePerson.getFullName(), "Incorrect person for " + w.getFullName());
        }
    }

    public void setResourceRole(Work root, Work work1, Work work2, Role role1, Role role2) {
        // enable resource plan for work1
        WorkManager.enablePlanResources(WorkManager.open(work1), work1);

        // enable resource plan for work2
        WorkManager.enablePlanResources(WorkManager.open(work2), work2);

        // open parent work
        WBSPage pc = WorkManager.openWBS(root);

        setNeededColumn(pc, OPTIONS_BLOCK_COLUMNS_RESOURCE_ROLE);

        pc.setIncludeIndependentWork();

        PSLogger.info("Set role '" + role2 + "' for " + work2.getName());
        pc.getGrid().setRole(work2, role2.getName());

        PSLogger.info("Set role '" + role1 + "' for " + work1.getName());
        pc.getGrid().setRole(work1, role1.getName());

        pc.saveArea();
        String fromPage1 = pc.getGrid().getRole(work1);
        String fromPage2 = pc.getGrid().getRole(work2);

        Assert.assertEquals(fromPage1, role1.getName(),
                "incorrect role for " + work1.getName() + ", after setting " + role1);
        Assert.assertEquals(fromPage2, role2.getName(),
                "incorrect role for " + work2.getName() + ", after setting " + role2);

        work1.getResource().setRole(role1);
        work2.getResource().setRole(role2);
    }


    private void setNeededColumn(WBSPage projectCentral, WBSEPageLocators... columnIds) {
        WBSPage.Columns columns = projectCentral.getOptions().getColumns();
        columns.uncheckAll();
        for (WBSEPageLocators id : columnIds) {
            PSLogger.info("Set checkBox  '" + id.getLocator() + "'");
            Assert.assertNotNull(columns.getCheckbox(id), "Can't find " + id + " checkbox");
            columns.getCheckbox(id).click();
        }
        columns.apply();
    }

    public void testResizing(Work work) {
        WBSPage pc = WorkManager.openWBS(work);
        pc.getOptions().fullScreen();
        pc.hideGantt();

        TimerWaiter.waitTime(5000);
        float factor = TestData.getRandom().nextFloat() + TestData.getRandom().nextFloat();
        PSLogger.info("Test resizing first column 'Name'; factor is " + factor);
        reSizeColumn(pc.getGrid(), null, factor);

        List<String> header = pc.getGrid().getTableHeaderList();
        for (String column : header) {
            factor = TestData.getRandom().nextFloat() + TestData.getRandom().nextFloat();
            PSLogger.info("Test resizing column '" + column + "'. Factor is " + factor);
            reSizeColumn(pc.getGrid(), column, factor);
        }
        pc.getOptions().standardScreen();
    }

    private static void reSizeColumn(SimpleGrid grid, String columnName, float factor) {
        int limit = SimpleGrid.getMinAllowedWidth(columnName);
        int allowedDifference = 2; // pixels
        int was = grid.getColumnWidth(columnName);
        int expected = (int) (was * factor);
        if (expected <= limit)
            expected = limit;
        grid.reSizeColumn(columnName, factor);
        int now = grid.getColumnWidth(columnName);
        PSLogger.info("Width now " + now + ", expected " + expected);
        PSLogger.info("Width was " + was);
        Assert.assertTrue(Math.abs(now - expected) <= allowedDifference,
                "Incorrect width for column '" + (columnName != null ? columnName : "Name") +
                        "' after resizing, expected " + expected + ", but was " + now
        );
    }


    public void renameWorks(Work root, Work w) {
        renameWork("_re", root, w);
    }
}
