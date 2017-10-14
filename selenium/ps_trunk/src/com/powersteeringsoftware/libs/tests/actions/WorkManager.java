package com.powersteeringsoftware.libs.tests.actions;

import com.powersteeringsoftware.libs.elements.AssignUsersComponent;
import com.powersteeringsoftware.libs.elements.DatePicker;
import com.powersteeringsoftware.libs.elements.SimpleGrid;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.*;
import com.powersteeringsoftware.libs.objects.works.GatedProject;
import com.powersteeringsoftware.libs.objects.works.Template;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.*;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WorkManager {


    public static AbstractWorkPage createProject(Work p) {
        return createProject(p, false);
    }

    public static AbstractWorkPage createDescendant(Work parent, Work p) {
        if (parent instanceof GatedProject.Gate) {
            return addDeliverables((GatedProject) parent.getParent(), parent, p);
        }
        SummaryWorkPage sum = open(parent);
        CreateWorkPage page = sum.addNewDescendant();
        SummaryWorkPage res = (SummaryWorkPage) _processCreationProject(page, p, null).finish(false);
        checkPage(res, parent);
        p.setCreated();
        p.setParent(parent);
        return res;
    }


    public static AbstractWorkPage createProject(Work p, Boolean howToSetParent) {
        AbstractWorkPage res = _processCreationProject(p, howToSetParent).finish();
        checkPage(res, p);
        if (res instanceof SummaryWorkPage)
            checkSummarySaveButton((SummaryWorkPage) res, p);
        String id = res.getUrlId();
        PSLogger.debug("Project id is " + id);
        p.setId(id);
        p.setCreated();
        return res;
    }

    public static void checkSummarySaveButton(SummaryWorkPage res, Work p) {
        if (p.isDatesFixed()) return;
        SimpleGrid grid = res.getGrid();
        if (grid != null) {
            PSLogger.info("check Summary grid for project " + p);
            checkSummarySaveButton(grid, p, true);
        }
    }

    static void checkSummarySaveButton(SimpleGrid grid, Work p, boolean doExpand) {
        boolean shouldBeEnabled = p.isBrokenDates();
        if (!shouldBeEnabled) return;
        if (doExpand)
            grid.expandTree();
        SimpleGrid.ApplyButton save = grid.getSaveButton();
        if (save.isEnabled()) {
            PSLogger.save("Some unsaved data on project " + p);
            save.submit();
            p.setDatesFixed();
            if (!shouldBeEnabled)
                Assert.fail("Save is enabled after creating/opening project/template " + p); //#85788
        } else if (shouldBeEnabled) {
            PSLogger.warn("Expected enabled save #85570");
        }
    }

    private static void checkPage(AbstractWorkPage res, Work p) {
        Assert.assertNotNull(res, "There is an error on page");
        String header = res.getContainerHeader();
        Assert.assertEquals(header, p.getName(), "Can't find header for project " + p.getFullName());
    }


    public static AbstractWorkPage createProjects(Work p) {
        SummaryWorkPage res = (SummaryWorkPage) createProject(p, null);
        for (Work w : p.getChildren()) {
            PSLogger.info("Add child " + w);
            CreateWorkPage page = res.addNewDescendant();
            res = (SummaryWorkPage) _processCreationProject(page, w, null).finish();
            checkPage(res, p);
            w.setCreated();
        }
        return res;
    }

    public static SummaryWorkPage editManualScheduling(Work p) {
        return editManualScheduling(open(p), p);
    }

    public static SummaryWorkPage editManualScheduling(SummaryWorkPage sum, Work p) {
        EditWorkPage edit = sum.edit();
        edit.selectManualScheduling(p.getManualScheduling());
        edit.selectManualSchedulingForDependencies(p.getManualSchedulingForDependencies());
        return edit.submitChanges();
    }

    public static void setOwner(Work w, User u) {
        PSLogger.info("Set owner " + u + " for work " + w);
        SummaryWorkPage sum = open(w);
        String fromPage = sum.getOwner();
        PSLogger.info("Owner From page '" + fromPage + "'");
        String name = u.getFullName();
        if (name.equals(fromPage)) {
            PSLogger.warn("Can't set new owner " + u + ": equals existing");
            return;
        }
        EditWorkPage edit = sum.edit();
        edit.getAssignUserComponent().doAssign(u, BuiltInRole.OWNER);
        edit.submitChanges();
        w.setOwner(u);
    }

    public static void setUser(Work w, Role role, List<User> users) {
        setUser(w, role, users.toArray(new User[]{}));
    }

    public static void setUser(Work w, Role role, User... users) {
        PSLogger.info("Set " + Arrays.toString(users) + " to " + role + " for work " + w);
        List<User> res = new ArrayList<User>();
        for (User u : users) {
            if (w.getUsers().containsKey(role) && w.getUsers().get(role).contains(u)) {
                PSLogger.debug("Work already contains " + u + " on role " + role);
                continue;
            }
            res.add(u);
        }
        if (res.isEmpty()) {
            PSLogger.warn("setUsers:Nothing to set");
            return;
        }
        SummaryWorkPage sum = open(w);
        EditWorkPage edit = sum.edit();
        for (User u : res) {
            edit.getAssignUserComponent().doAssign(u, role);
        }
        edit.submitChanges();
        for (User u : res)
            w.addUser(role, u);
    }


    public static void edit(Work w1, Work w2) {
        PSLogger.info("Edit work " + w1);
        //todo:
        boolean popup = false;
        EditWorkPage page = open(w1).edit();
        if (!w1.getName().equals(w2.getName())) {
            page.setName(w2.getName());
        }
        if (!w1.getStatus().equals(w2.getStatus())) {
            page.getOptions().setStatus(w1.getStatus());
        }
        if (!w1.getPriority().equals(w2.getPriority())) {
            page.getOptions().setPriority(w2.getPriority());
        }

        User o1 = w1.getOwner();
        User o2 = w2.getOwner();
        User owner = null;
        if (o1.equals(o2)) owner = o1;
        if (owner == null || w1.hasUsersToAssign() || w2.hasUsersToAssign()) {
            AssignUsersComponent comp = page.getAssignUserComponent();
            comp.removeAll(owner);
            setUsers(comp, w2);
        }
        page.submitChanges(popup);
        w1.setName(w2.getName());
        w1.setStatus(w2.getStatus());
        w1.setPriority(w2.getPriority());
        w1.clearUsers();
        Map<Role, List<User>> map = w2.getUsers();
        for (Role r : map.keySet()) {
            for (User u : map.get(r))
                w1.addUser(r, u);
        }
    }


    public static SummaryWorkPage editProject(SummaryWorkPage sum, Work work) {
        boolean popup = false;
        EditWorkPage edit = sum.edit();
        if (work.getStatusReporting() != null)
            edit.setStatusReporting(work.getStatusReporting());

        edit.setInheritPermissions(work.getInheritPermissions());

        edit.setInheritControls(work.getInheritControls());
        if (!work.getInheritControls()) {
            edit.setControlCost(work.getControlCost());
            edit.selectManualScheduling(work.getManualScheduling());
            edit.selectManualSchedulingForDependencies(work.getManualSchedulingForDependencies());
        }

        edit.setScheduleConstraint(work.getDummyConstraint());

        if (work.isConstraintEnd()) {
            edit.setConstraintEnd(work.getConstraintEndDate());
        }

        if (work.isConstraintStart() && (!(work instanceof GatedProject) || !((GatedProject) work).getEnforceSequential())) {
            edit.setConstraintStart(work.getConstraintStartDate());
        }

        SummaryWorkPage res = edit.submitChanges(popup);
        SimpleGrid grid = res.getGrid();
        if (grid != null) {
            SimpleGrid.ApplyButton save = grid.getSaveButton();
            if (save.isEnabled()) {
                save.submit();
                Assert.fail("Grid Save is enabled after editing project " + work);
            }
        }
        return res;
    }

    private static CreateWorkPage _processCreationProject(Work p, Boolean howToSetParent) {
        PSLogger.info("Create project '" + p.getFullName() + "' {" + p + "}");
        CreateWorkPage page = new CreateWorkPage();
        page.open(p.isOrg() ? 1 : 0);
        return _processCreationProject(page, p, howToSetParent);
    }

    private static CreateWorkPage _processCreationProject(CreateWorkPage page, Work p, Boolean setParent) {
        if (p.isWork()) {
            page.selectType(p.getType());
            page.next();
        }
        page.setName(p.getName());
        if (setParent != null && p.hasParent()) {
            page.setParent(p.getParent(), setParent);
        }

        for (PSTag tag : p.getTags()) {
            page.getTagsComponent().setTag(tag);
        }

        if (p.isGated()) {
            setUsers(page.getAssignUserComponent(), p);
            setProjectConstraintDates(page, p);
        }

        // next if not gated and has constraints or users
        if (p.hasSecondCreateWorkPage()) {
            page = page.next();
            if (!p.isGated()) {
                setUsers(page.getAssignUserComponent(), p);
                setProjectConstraintDates(page, p);
            }
            if (p.hasStatus()) {
                page.getOptions().setStatus(p.getStatus());
            }
            if (p.hasPriority()) {
                page.getOptions().setPriority(p.getPriority());
            }

        }
        return page;
    }


    public static DeliverablesListingPage addDeliverables(GatedProject gP, Work gate, Work work) {
        SummaryWorkPage summary = open(gP);
        DeliverablesListingPage adapterDeliverables = summary.clickAddRemove();
        DeliverablesListingPage.AddDeliverableDialog dialog = adapterDeliverables.pushAddNew();
        dialog.setWorkType(work.getType());
        dialog.setGate(gate.getName());
        CreateWorkPage createWorkAdapter = dialog.pushContinue();
        createWorkAdapter.setName(work.getName());
        DeliverablesListingPage res = (DeliverablesListingPage) createWorkAdapter.finish();
        work.setParent(gate);
        work.setCreated();
        return res;
    }

    public static void setUsers(AssignUsersComponent comp, Work w) {
        if (TestSession.getAppVersion().verLessOrEqual(PowerSteeringVersions._7_1))
            PSSkipException.skip("Assigning users is not supported now for old PS versions");
        if (!w.hasUsersToAssign()) return;
        Map<Role, List<User>> map = w.getUsers();
        PSLogger.info("Users to assign : " + map);
        for (Role role : map.keySet()) {
            for (User u : map.get(role))
                comp.doAssign(u, role);
        }
    }

    private static void setChampionOnCreating(CreateWorkPage page, GatedProject gP) {
        PSLogger.info("Set champions:" + gP.getChampionsList());
        PowerSteeringVersions version = TestSession.getAppVersion();
        if (version.verGreaterThan(PowerSteeringVersions._7_1)) {
            for (User champion : gP.getChampionsList()) {
                page.getAssignUserComponent().makeChampion(champion.getFirstName());
            }
            return;
        }
        page.getAssignUserComponent().oldMakeChampion(gP.getChampionsList().get(0).getFirstName());
    }

    private static void setProjectConstraintDates(CreateWorkPage page, Work p) {
        if (p.skipDates()) {
            return;
        }
        PSLogger.info("Set constraint dates: [" + p.getConstraintStartDate() + ", " + p.getConstraintEndDate() + "]");
        if (!p.isUseAdvancedDatesComponent()) {
            if (p.isConstraintStart()) {
                Assert.assertTrue(page.isConstraintStartDatePresent(), "Can't find constraint start date for " + p.getConstraint());
                PSLogger.info("Set constraint start to " + p.getConstraintStartDate());
                page.getConstraintStartDP().type(p.getConstraintStartDate());
            }
            if (p.isConstraintEnd()) {
                Assert.assertTrue(page.isConstraintEndDatePresent(), "Can't find constraint end date for " + p.getConstraint());
                PSLogger.info("Set constraint end to " + p.getConstraintEndDate());
                page.getConstraintEndDP().type(p.getConstraintEndDate());
            }

        } else { // this is for gates:
            if (((GatedProject) p).getEnforceSequential()) {
                for (Work ch : p.getChildren()) {
                    if (ch.isConstraintStart())
                        setGateConstraintStart(page, ch, ch.getConstraintStartDate());
                    if (ch.isConstraintEnd())
                        setGateConstraintEnd(page, ch, ch.getConstraintEndDate());
                }
                PSLogger.info("Validate dates");
                for (Work ch : p.getChildren()) {
                    String e = page.getGateConstraintEndDate(ch.getName());
                    String s = page.getGateConstraintStartDate(ch.getName());
                    PSLogger.debug("From page for " + ch.getName() + ": " + s + "," + e);
                    Assert.assertEquals(s, ch.getConstraintStartDate(), "Incorrect constraint start date for " + ch.getName());
                    Assert.assertEquals(e, ch.getConstraintEndDate(), "Incorrect constraint end date for " + ch.getName());
                }
            } else {
                GatedProject.Gate first = ((GatedProject) p).getFirstGate();
                GatedProject.Gate last = ((GatedProject) p).getLastGate();
                if (p.isConstraintEnd())
                    setGateConstraintEnd(page, last, p.getConstraintEndDate());
                if (p.isConstraintStart())
                    setGateConstraintStart(page, first, p.getConstraintStartDate());
                first.setConstraintStartDate(p.getConstraintStartDate());
                last.setConstraintEndDate(p.getConstraintEndDate());
            }
        }
        PSLogger.save("After setting dates");
    }

    private static void setGateConstraintEnd(CreateWorkPage page, Work gate, String date) {
        DatePicker dp = page.getGateConstraintEndDP(gate.getName());
        Assert.assertNotNull(dp, "Can't find constraint end date for " + gate.getName());
        PSLogger.info("Set constraint end for " + gate.getName() + " to " + date);
        dp.type(date);
    }

    private static void setGateConstraintStart(CreateWorkPage page, Work gate, String date) {
        DatePicker dp = page.getGateConstraintStartDP(gate.getName());
        Assert.assertNotNull(dp, "Can't find constraint start date for " + gate.getName());
        PSLogger.info("Set constraint start for " + gate.getName() + " to " + date);
        dp.type(date);
    }

    public static SummaryWorkPage open(Work w, boolean doCheck, boolean doRefresh) {
        if (w.getId() != null) {
            SummaryWorkPage sum = SummaryWorkPage.getInstance(doCheck, w.getId());
            if (sum.checkUrl()) {
                PSLogger.info("Summary for project " + w.getName() + " is already opened");
                if (doRefresh) {
                    PSLogger.debug("Refresh summary");
                    sum.refresh();
                }
                return sum;
            }
        }
        if (w.getId() == null || !AbstractWorkPage.useDirectLink()) {
            if (w instanceof GatedProject.Gate) {
                return open(w.getParent(), doCheck, doRefresh);
            }
            SummaryWorkPage res = null;
            if (w.isTemplateRoot()) {
                SummaryWorkPage sum = WorkTemplateManager.open((Template) w.getWorkType(), doCheck, doRefresh);
                res = sum.getGrid().openSummaryPage(w.getName());
            } else if (w.isInTemplate()) {
                SummaryWorkPage sum = open(w.getParent(), doCheck, doRefresh);
                if (w.hasParent() && w.getParent() instanceof GatedProject.Gate) {
                    DeliverablesListingPage list = sum.clickAddRemove();
                    res = list.openWork(w.getParentName(), w.getName());
                } else {
                    res = sum.getGrid().openSummaryPage(w.getName());
                }
            }
            if (res != null) {
                if (w.getId() == null) {
                    w.setId(res.getUrlId());
                }
                return res;
            }
        }
        return SummaryWorkPage.openWork(w, doCheck);
    }

    public static SummaryWorkPage open(Work w, boolean doCheck) {
        return open(w, doCheck, false);
    }

    public static SummaryWorkPage open(Work w) {
        return open(w, true, false);
    }

    public static EditWorkPage openEdit(Work w) {
        if (w.getId() == null) {
            SummaryWorkPage sum = open(w);
            w.setId(sum.getUrlId());
            return sum.edit();
        }
        return EditWorkPage.doNavigatePageEdit(w.getId());
    }

    public static WBSPage openWBS(Work w) {
        return openWBS(w, true);
    }

    public static WBSPage openWBS(Work w, boolean doRefresh) {
        String url = w.getWbsPCUrl();
        if (url != null) {
            WBSPage pc = WBSPage.getInstance();
            if (pc.getUrl().endsWith(url)) {
                if (doRefresh) pc.refresh();
                pc.getGrid().setIndexes(w);
                return pc;
            }
        }
        WBSPage res = WBSPage.openWBSPage(w);
        checkSummarySaveButton(res.getGrid(), w, false); // for 10.0, now fix db
        return res;
    }

    public static WorkTreePage openTree() {
        WorkTreePage tree = new WorkTreePage();
        tree.open();
        return tree;
    }

    public static PermissionsWorkPage openPermissions(Work w) {
        SummaryWorkPage sum = open(w);
        return sum.editPermissions();
    }

    public static PermissionsWorkPage editPermissions(Work w, PSPermissions p) {
        PermissionsWorkPage edit = openPermissions(w);
        PSPermissions res = PermissionsManager.setDefault(edit.getContent(), p);
        w.setPermissions(res);
        return edit;
    }

    public static boolean exists(Work w) {
        //todo
        try {
            open(w);
        } catch (AssertionError e) {
            PSLogger.warn(e);
            w.setDeleted();
            return false;
        }
        w.setCreated();
        return true;
    }

    public static void deleteProject(Work w) {
        deleteProject(open(w));
        w.setDeleted();
    }

    public static void archiveProject(Work w) {
        archiveProject(open(w));
    }

    public static void deleteProject(SummaryWorkPage sum) {
        DeleteWorkPage.Results res = null;
        PSPage p = sum.callDelete().doDelete();
        if (p instanceof DeleteWorkPage) res = ((DeleteWorkPage) p).delete();
        else if (p instanceof DeleteWorkPage.Results) res = (DeleteWorkPage.Results) p;
        res.ok();
    }

    public static SummaryWorkPage archiveProject(SummaryWorkPage sum) {
        return sum.callArchive().yes();
    }

    public static SummaryWorkPage enableCosts(Work work, boolean enable) {
        SummaryWorkPage sum = open(work);
        EditWorkPage edit = sum.edit();
        boolean popup = false;
        if (work.hasParent()) {
            edit.setInheritControls(false);
        }
        edit.setControlCost(enable);
        SummaryWorkPage res = edit.submitChanges(popup);
        if (work.hasParent()) {
            work.setInheritControls(false);
        }
        work.setControlCost(enable);
        return res;
    }

    /**
     * example:
     * long d = 24 * 60 * 60 * 1000L;
     * Cost a1 = new Cost(Cost.Type.ACTUAL, 432.43223d, "actual1", System.currentTimeMillis() + 12 * d);
     * Cost e1 = new Cost(43312.43d, "es1", System.currentTimeMillis() - 51 * d, System.currentTimeMillis() - 13 * d);
     * Cost e2 = new Cost(-3215462.43d, "es2", System.currentTimeMillis() - 222 * d, System.currentTimeMillis() - 2 * d);
     * WorkManager.addCosts(getTestData().getRootWork(), e1, e2, a1);
     *
     * @param w
     * @param costs
     */
    public static void addCosts(Work w, Cost... costs) {
        PSLogger.info("Add costs " + Arrays.toString(costs) + " to work " + w.getName() + ". Currency is " + w.getCurrency());
        //Assert.assertTrue(w.getControlCost(), "Costs should be enabled on work " + w);

        List<Cost> estimated = new ArrayList<Cost>();
        List<Cost> actual = new ArrayList<Cost>();
        for (Cost c : costs) {
            if (c.getType().equals(Cost.Type.ESTIMATED)) {
                estimated.add(c);
            } else {
                actual.add(c);
            }
        }
        addCosts(w, true, estimated, true);
        addCosts(w, false, actual, true);
    }

    public static EstimatedPage openEstimatedCosts(Work w) {
        if (w.getId() != null && AbstractWorkPage.useDirectLink()) {
            EstimatedPage res = new EstimatedPage(w.getId());
            res.open();
            return res;
        }
        SummaryWorkPage sum = open(w);
        return sum.openEstimatedCosts();
    }

    public static ActualPage openActualCosts(Work w) {
        if (w.getId() != null && AbstractWorkPage.useDirectLink()) {
            ActualPage res = new ActualPage(w.getId());
            res.open();
            return res;
        }
        SummaryWorkPage sum = open(w);
        return sum.openActualCosts();
    }

    public static void collectCosts(Work w) {
        CostBasePage page = openEstimatedCosts(w);
        w.setControlCost(true);
        for (Cost c : page.getCosts(w)) {
            w.addCost(c);
        }
        page = page.openActualCosts();
        for (Cost c : page.getCosts(w)) {
            w.addCost(c);
        }
    }

    public static void addCosts(Work w, List<Cost> costs) {
        addCosts(w, costs.toArray(new Cost[]{}));
    }

    public static void addCosts(Work w, boolean estimated, List<Cost> costs, boolean doValidate) {
        if (costs.isEmpty()) return;
        CostBasePage page;
        if (estimated)
            page = openEstimatedCosts(w);
        else
            page = openActualCosts(w);
        List<Cost> before = null;
        if (doValidate) {
            before = page.getCosts(w);
            List<Cost> _costs = w.getCosts();
            boolean addAll = _costs.isEmpty();
            for (Cost c : before) { // todo?
                if (addAll || !_costs.contains(c)) {
                    w.addCost(c);
                }
            }
            PSLogger.info((estimated ? "Estimated" : "Actual") + " costs before(" + before.size() + ") : " + before);
        }
        CostBasePage.AddDialog dialog = page.callAddDialog();
        for (Cost c : costs) {
            dialog.setAmount(String.valueOf(c.getAmount()));
            if (c.getDescription() != null)
                dialog.setDescription(c.getDescription());
            if (c.getDate() != null)
                dialog.setDate(c.getSDate(), true);
            if (estimated && c.getEndDate() != null) {
                ((EstimatedPage.EstimatedAddDialog) dialog).setEndDate(c.getSEndDate(), true);
            }
            dialog.add();
        }
        dialog.submit();
        if (!doValidate) return;

        List<Cost> after = page.getCosts(w);
        PSLogger.info((estimated ? "Estimated" : "Actual") + "costs after(" + after.size() + ") : " + after);
        Assert.assertTrue(costs.size() + before.size() == after.size(), "Incorrect size of costs on page. Expected new costs(" + costs.size() + ") : " + costs);

        List<Cost> errors = new ArrayList<Cost>();
        for (Cost c : costs) {
            if (!after.contains(c)) {
                errors.add(c);
            } else {
                w.addCost(c);
            }
        }
        Assert.assertTrue(errors.isEmpty(), "Some " + (estimated ? "Estimated" : "Actual") + " costs are absent: " + errors);
    }

    public static void enablePlanResources(SummaryWorkPage page, Work work) {
        if (work.getPlanResources()) {
            PSLogger.debug("Plan resources is already enabled for work " + work.getName());
            return;
        }
        PSLogger.info("Enable 'Plan Resources' for " + work.getName());
        page.openDetails();
        if (page.isPlanResourcesEnabled()) return;
        if (!BasicCommons.getCurrentUser().isEditWorkEnabled()) {
            PSSkipException.skip("This test-case is not enabled: user " + BasicCommons.getCurrentUser() +
                    " has not permissions for editing work");
        }
        EditWorkPage edit = page.edit();
        edit.checkPlanResources();

        page = work.getChildren().size() != 0 ? edit.submitChangesWithConfirmation() : edit.submitChanges();
        SimpleGrid grid = page.getGrid();
        if (grid != null) {
            SimpleGrid.ApplyButton save = grid.getSaveButton();
            if (save.isEnabled()) {
                save.submit();
                Assert.fail("Save has been enabled after enabling plan resources"); // like #85296
            }
        }
        Assert.assertTrue(page.isPlanResourcesEnabled(), "Plan Resources is not enabled");
        work.setPlanResources(true);
        for (Work w : work.getAllChildren()) {
            w.setPlanResources(true);
        }
    }
}
