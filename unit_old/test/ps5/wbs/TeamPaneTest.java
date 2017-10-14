package ps5.wbs;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ps5.resourcereview.beans.RrBeanPeriodData;
import ps5.wbs.beans.BeansTree;
import ps5.wbs.beans.Period;
import ps5.wbs.beans.ResourceBean;
import ps5.wbs.beans.ResourceBean.CalculatedField;
import ps5.wbs.beans.ResourceSplitBean;
import ps5.wbs.beans.RoleAdapterBean;
import ps5.wbs.beans.TPRoleBean;
import ps5.wbs.beans.WBSBean;
import ps5.wbs.beans.WorkBean;
import ps5.wbs.logic.CalculatorManager;
import ps5.wbs.logic.DataAccessor;
import ps5.wbs.logic.DataAccessor.VAAccessor.AllocData;
import ps5.wbs.logic.DataAccessorLogic;
import ps5.wbs.logic.ProjectEditor;
import ps5.wbs.logic.ProjectEditorUtils;
import ps5.wbs.logic.TeamPaneEditor;
import ps5.wbs.logic.handlers.VisitProxy;
import ps5.wbs.model.ColumnValue;

import com.cinteractive.database.Uuid;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.newsecurity.PSPermission;
import com.cinteractive.ps3.scheduler.Constraint;
import com.cinteractive.ps3.scheduler.TestWorkBean;
import com.cinteractive.ps3.scheduler.WorkScheduleTestUtils;
import com.cinteractive.ps3.scheduler.calendar.DateCalculator;
import com.cinteractive.ps3.session.PSSession;
import com.cinteractive.ps3.test.PSTestBase;
import com.cinteractive.scheduler.Duration;
import com.cinteractive.util.Log;
import com.cinteractive.util.TimeUtilities.TimeScale;

/**
 * Tests TeamPane functionality
 */
@SuppressWarnings("javadoc")
public class TeamPaneTest extends PSTestBase {

    private static int testId = 0;

    WorkScheduleTestUtils utils;

    private Log log = new Log(TeamPaneTest.class);

    public TeamPaneTest(String name) {
        super(name);
    }

    public User getOwner() {
        return WBSTestHelper.getOwner();
    }

    public VisitProxy getVisit() {
        return WBSTestHelper.getVisit();
    }

    public void test73649() {
        final ProjectEditor pe = WBSTestHelper.getTestProjectEditor();
        BeansTree tree = pe.getTree();
        WorkBean root = (WorkBean) tree.getRoot();
        root.setPermission(PSPermission.ALL);
        WorkBean w1 = new TestWorkBean("w1", Uuid.create());
        tree.addChild(root, w1);
        w1.setConstraintType(Constraint.FD);
        w1.setPlannedStartDate(WorkScheduleTestUtils.formatDate("06.20.2011"));
        w1.setPlannedEndDate(WorkScheduleTestUtils.formatDate("07.15.2011"));
        DataAccessorLogic.runScheduler(pe);
        ResourceSplitBean resSplit = (ResourceSplitBean) pe.addResourceSplit(w1);
        assertEquals(resSplit.getEffort(), 160.f);
        assertEquals(resSplit.getAllocation(), 100.f);
        Period period = new Period(w1.getSystemStartDate(), TimeScale.WEEKLY);
        Period period0 = new Period(new Date(w1.getSystemStartDate().getTime() - 86400 * 7 * 1000), TimeScale.WEEKLY);
        pe.calculateVAData(period0);
        pe.calculateVAData(period);
        DataAccessor va = DataAccessor.getVAAccessor(period);
        va.set(resSplit, AllocData.getByEffort(10.f), pe);
        List<ResourceBean> resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 2);
        assertEquals(resources.get(0).getEffort(), 10.f);
        assertEquals(resources.get(0).getAllocation(), 25.f);
        assertEquals(resources.get(0).getStartDate(), getDate("06.20.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("06.26.2011"));
        assertEquals(resources.get(1).getStartDate(), getDate("06.27.2011"));
        assertEquals(resources.get(1).getEndDate(), getDate("07.15.2011"));
        // ------------------------------------
        w1.setPlannedStartDate(WorkScheduleTestUtils.formatDate("06.13.2011"));
        DataAccessorLogic.runScheduler(pe);
        resources = getSortedResources(resSplit);
        assertEquals(resSplit.getPeriod(period0).getAllocated(), 10.f);
        assertEquals(resSplit.getPeriod(period).getAllocated(), 40.f);
        assertEquals(resources.get(0).getStartDate(), getDate("06.13.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("06.17.2011"));
        assertEquals(resources.get(1).getStartDate(), getDate("06.20.2011"));
        assertEquals(resources.get(1).getEndDate(), getDate("07.15.2011"));
    }

    public void testEAD() {
        final ProjectEditor pe = WBSTestHelper.getTestProjectEditor();
        BeansTree tree = pe.getTree();
        WorkBean root = (WorkBean) tree.getRoot();
        root.setPermission(PSPermission.ALL);
        WorkBean w1 = new TestWorkBean("w1", Uuid.create());
        tree.addChild(root, w1);
        w1.setConstraintType(Constraint.ASAP);
        w1.setPlannedLaborTime(Duration.getDaysDuration(6));
        DataAccessorLogic.runScheduler(pe);
        ResourceSplitBean res = (ResourceSplitBean) pe.addResourceSplit(w1);
        res.setCalculatedField(CalculatedField.EFFORT);
        assertEquals(res.getEffort(), 48.f);
        assertEquals(res.getAllocation(), 100.f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 6.f);
        assertEquals(res.getCalculatedField(), CalculatedField.EFFORT);
        DataAccessor.ALLOCATION.set(res, 101.f, pe);
        assertEquals(res.getEffort(), 48.48f);
        assertEquals(res.getAllocation(), 101.f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 6.f);
        assertEquals(res.getCalculatedField(), CalculatedField.EFFORT);
        DataAccessor.ALLOCATION.set(res, 50.f, pe);
        assertEquals(res.getEffort(), 24.f);
        assertEquals(res.getAllocation(), 50.f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 6.f);
        assertEquals(res.getCalculatedField(), CalculatedField.EFFORT);
        DataAccessor.RESOURCE_DURATION.set(res.getWork(), Duration.getDaysDuration(3.f), pe);
        assertEquals(res.getEffort(), 12.f);
        assertEquals(res.getAllocation(), 50.f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 3.f);
        assertEquals(res.getCalculatedField(), CalculatedField.EFFORT);
        DataAccessor.EFFORT.set(res, 25.f, pe);
        assertEquals(res.getEffort(), 25.f);
        assertRoughly(res.getAllocation(), 104.17f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 3.f);
        assertEquals(res.getCalculatedField(), CalculatedField.ALLOCATION);
        DataAccessor.EFFORT.set(res, 24.f, pe);
        assertEquals(res.getEffort(), 24.f);
        assertEquals(res.getAllocation(), 100.f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 3.f);
        assertEquals(res.getCalculatedField(), CalculatedField.ALLOCATION);
        DataAccessor.ALLOCATION.set(res, 50.f, pe);
        assertEquals(res.getEffort(), 24.f);
        assertEquals(res.getAllocation(), 50.f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 6.f);
        assertEquals(res.getCalculatedField(), CalculatedField.DURATION);
        DataAccessor.EFFORT.set(res, 25.f, pe);
        assertEquals(res.getEffort(), 25.f);
        assertRoughly(res.getAllocation(), 44.64f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 7.f);
        assertEquals(res.getCalculatedField(), CalculatedField.DURATION);
        pe.clearModifications();
        // bulk edit!!!
        pe.beginBulkEdit(true);
        DataAccessor.ALLOCATION.set(res, 50.f, pe);
        pe.commitChanges();
        assertEquals(res.getEffort(), 28.f);
        assertRoughly(res.getAllocation(), 50.f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 7.f);
        assertEquals(res.getCalculatedField(), CalculatedField.DURATION);
        pe.clearModifications();
        pe.beginBulkEdit(true);
        DataAccessor.EFFORT.set(res, 12.f, pe);
        pe.commitChanges();
        assertEquals(res.getEffort(), 12.f);
        assertRoughly(res.getAllocation(), 50.f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 3.f);
        assertEquals(res.getCalculatedField(), CalculatedField.DURATION);
        pe.clearModifications();
        pe.beginBulkEdit(true);
        DataAccessor.RESOURCE_DURATION.set(res.getWork(), Duration.getDaysDuration(4.f), pe);
        pe.commitChanges();
        assertEquals(res.getEffort(), 16.f);
        assertRoughly(res.getAllocation(), 50.f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 4.f);
        assertEquals(res.getCalculatedField(), CalculatedField.EFFORT);
        pe.clearModifications();
        pe.beginBulkEdit(true);
        DataAccessor.ALLOCATION.set(res, 100.f, pe);
        pe.commitChanges();
        assertEquals(res.getEffort(), 32.f);
        assertRoughly(res.getAllocation(), 100.f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 4.f);
        assertEquals(res.getCalculatedField(), CalculatedField.EFFORT);
        pe.clearModifications();
        pe.beginBulkEdit(true);
        DataAccessor.EFFORT.set(res, 43.f, pe);
        pe.commitChanges();
        assertEquals(res.getEffort(), 43.f);
        assertRoughly(res.getAllocation(), 134.34f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 4.f);
        assertEquals(res.getCalculatedField(), CalculatedField.ALLOCATION);
        pe.clearModifications();
        pe.beginBulkEdit(true);
        DataAccessor.ALLOCATION.set(res, 10.f, pe);
        DataAccessor.RESOURCE_DURATION.set(res.getWork(), Duration.getDaysDuration(10.f), pe);
        pe.commitChanges();
        assertEquals(res.getEffort(), 8.f);
        assertRoughly(res.getAllocation(), 10.f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 10.f);
        assertEquals(res.getCalculatedField(), CalculatedField.EFFORT);
        DataAccessor.EFFORT.set(res, 10.f, pe);
        pe.clearModifications();
        pe.beginBulkEdit(true);
        DataAccessor.ALLOCATION.set(res, 10.f, pe);
        DataAccessor.RESOURCE_DURATION.set(res.getWork(), Duration.getDaysDuration(10.f), pe);
        pe.commitChanges();
        assertEquals(res.getEffort(), 8.f);
        assertRoughly(res.getAllocation(), 10.f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 10.f);
        assertEquals(res.getCalculatedField(), CalculatedField.EFFORT);
        pe.clearModifications();
        pe.beginBulkEdit(true);
        DataAccessor.EFFORT.set(res, 30.f, pe);
        DataAccessor.RESOURCE_DURATION.set(res.getWork(), Duration.getDaysDuration(20.f), pe);
        pe.commitChanges();
        assertEquals(res.getEffort(), 30.f);
        assertRoughly(res.getAllocation(), 18.75f);
        assertEquals(res.getWork().getSystemLaborTime().getAmount(), 20.f);
        assertEquals(res.getCalculatedField(), CalculatedField.ALLOCATION);
    }

    public void testInitialization() {
        final ProjectEditor pe = WBSTestHelper.getTestProjectEditor();
        BeansTree tree = pe.getTree();
        WorkBean root = (WorkBean) tree.getRoot();
        root.setPermission(PSPermission.ALL);
        WorkBean w1 = new TestWorkBean("w1", Uuid.create());
        WorkBean w2 = new TestWorkBean("w2", Uuid.create());
        tree.addChild(root, w1);
        tree.addChild(root, w2);
        DataAccessorLogic.runScheduler(pe);
        ResourceBean r1 = pe.addResourceSplit(w1);
        User u = (User) WBSTestHelper.createTestData(User.class, getVisit());
        u.setFirstName("a");
        u.setLastName("a");
        r1.setPerson(u);
        r1.setRole((RoleAdapterBean) WBSTestHelper.createTestData(RoleAdapterBean.class, getVisit()));
        ResourceBean r2 = pe.addResourceSplit(w1);
        r2.setRole((RoleAdapterBean) WBSTestHelper.createTestData(RoleAdapterBean.class, getVisit()));
        ResourceBean r2_1 = pe.addResourceSplit(w1);
        r2_1.setRole((RoleAdapterBean) WBSTestHelper.createTestData(RoleAdapterBean.class, getVisit()));
        ResourceBean r3 = pe.addResourceSplit(w1);
        u = (User) WBSTestHelper.createTestData(User.class, getVisit());
        u.setFirstName("c");
        u.setLastName("c");
        r3.setPerson(u);
        @SuppressWarnings("unused")
        ResourceBean r4 = pe.addResourceSplit(w1);
        assertTrue(!r1.isNew());
        assertTrue(!r2.isNew());
        assertTrue(!r3.isNew());
        TeamPaneEditor tpe = new TeamPaneEditor(pe);
        tpe.loadTree(null);
        WBSTestHelper.printTree(tpe.getTree());
        assertEquals(3, tpe.getTree().getRoot().getChildrenCount());
        checkRoles(tpe.getTree().getRoot().getChildrenIterator(), r1, r3, r2);
        // TPRoleBean role1 = (TPRoleBean)tpe.getRoot().getChild(0);
        // assertEquals(role1.getUserId(), r1.getPerson().getId());
        // assertEquals(role1.getRoleId(), r1.getRole().getId());
        //
        // TPRoleBean role3 = (TPRoleBean)tpe.getRoot().getChild(1);
        // assertEquals(role3.getUserId(), r3.getPerson().getId());
        // assertEquals(role3.getRoleId(), null);
        //
        // TPRoleBean role2 = (TPRoleBean)tpe.getRoot().getChild(2);
        // assertEquals(role2.getUserId(), null);
        // assertEquals(role2.getRoleId(), r2.getRole().getId());
        pe.clearModifications();
        ResourceBean rAdded = pe.addResourceSplit(w1);
        u = (User) WBSTestHelper.createTestData(User.class, getVisit());
        u.setFirstName("b-addNew");
        u.setLastName("b-addNew");
        rAdded.setPerson(u);
        rAdded.setRole((RoleAdapterBean) WBSTestHelper.createTestData(RoleAdapterBean.class, getVisit()));
        tpe.synchronize(false);
        WBSTestHelper.printTree(tpe.getTree());
        checkRoles(tpe.getTree().getRoot().getChildrenIterator(), r1, rAdded, r3, r2);
        // do the same again
        tpe.synchronize(false);
        WBSTestHelper.printTree(tpe.getTree());
        checkRoles(tpe.getTree().getRoot().getChildrenIterator(), r1, rAdded, r3, r2);
        pe.delete(r1);
        tpe.synchronize(false);
        WBSTestHelper.printTree(tpe.getTree());
        checkRoles(tpe.getTree().getRoot().getChildrenIterator(), rAdded, r3, r2);
        // TPRoleBean role4 = (TPRoleBean)tpe.getRoot().getChild(3);
        // assertEquals(role4.getUserId(), null);
        // assertEquals(role4.getRoleId(), null);
    }

    public void testPeriods() {
        final ProjectEditor pe = WBSTestHelper.getTestProjectEditor();
        BeansTree tree = pe.getTree();
        WorkBean root = (WorkBean) tree.getRoot();
        root.setPermission(PSPermission.ALL);
        WorkBean w1 = new TestWorkBean("w1", Uuid.create());
        tree.addChild(root, w1);
        DataAccessorLogic.runScheduler(pe);
        ResourceBean r1 = pe.addResourceSplit(w1);
        User u = (User) WBSTestHelper.createTestData(User.class, getVisit());
        u.setFirstName("a");
        u.setLastName("a");
        r1.setPerson(u);
        r1.setRole((RoleAdapterBean) WBSTestHelper.createTestData(RoleAdapterBean.class, getVisit()));
        ResourceBean r3 = pe.addResourceSplit(w1);
        u = (User) WBSTestHelper.createTestData(User.class, getVisit());
        u.setFirstName("c");
        u.setLastName("c");
        r3.setPerson(u);
        r1.setEffort(20.f);
        r1.setAllocation(50.f);
        TeamPaneEditor tpe = new TeamPaneEditor(pe);
        tpe.loadTree(null);
        WBSTestHelper.printTree(tpe.getTree());
        w1.setActualStartDate(WorkScheduleTestUtils.formatDate("10.19.10"));
        w1.setActualEndDate(WorkScheduleTestUtils.formatDate("10.25.10"));
        DataAccessorLogic.runScheduler(pe);
        Period period = new Period(WorkScheduleTestUtils.formatDate("10.19.10"), TimeScale.WEEKLY);
        tpe.calculateAllBeans(period);
        TPRoleBean roleBean = (TPRoleBean) tpe.getTree().getRoot().getChild(0);
        RrBeanPeriodData data = roleBean.getPeriod(period);
        assertEquals(20.f, data.getAllocated());
        assertEquals(40.f, data.getCapacity());
    }

    public void testVAAllocation() {
        final ProjectEditor pe = WBSTestHelper.getTestProjectEditor();
        BeansTree tree = pe.getTree();
        WorkBean root = (WorkBean) tree.getRoot();
        root.setPermission(PSPermission.ALL);
        WorkBean w1 = new TestWorkBean("w1", Uuid.create());
        tree.addChild(root, w1);
        w1.setConstraintType(Constraint.ASAP);
        w1.setPlannedLaborTime(Duration.getDaysDuration(5));
        w1.setActualStartDate(WorkScheduleTestUtils.formatDate("01.17.2011"));
        DataAccessorLogic.runScheduler(pe);
        ResourceSplitBean resSplit = (ResourceSplitBean) pe.addResourceSplit(w1);
        assertEquals(resSplit.getEffort(), 40.f);
        assertEquals(resSplit.getAllocation(), 100.f);
        // one resource per word
        // |---------------------|
        // *********************
        assertEquals(w1.getCalculatedField(), ResourceBean.CalculatedField.EFFORT);
        ResourceBean vaRes = (ResourceBean) resSplit.getChild(0);
        assertEquals(vaRes.getEffort(), 40.f);
        assertEquals(vaRes.getAllocation(), 100.f);
        assertEquals(vaRes.getStartDate(), getDate("01.17.2011"));
        assertEquals(vaRes.getEndDate(), getDate("01.21.2011"));
        w1.setPlannedLaborTime(Duration.getDaysDuration(10));
        DataAccessorLogic.runScheduler(pe);
        // one resource per work
        // |------------------------------------------|
        // ******************************************
        assertEquals(resSplit.getChildrenCount(), 1);
        assertEquals(resSplit.getEffort(), 80.f);
        assertEquals(resSplit.getAllocation(), 100.f);
        assertEquals(vaRes.getEffort(), 80.f);
        assertEquals(vaRes.getAllocation(), 100.f);
        assertEquals(vaRes.getStartDate(), getDate("01.17.2011"));
        assertEquals(vaRes.getEndDate(), getDate("01.28.2011"));
        Period period = new Period(w1.getSystemStartDate(), TimeScale.WEEKLY);
        DataAccessor va = DataAccessor.getVAAccessor(period);
        va.set(resSplit, AllocData.getByPercentAllocation(50.f), pe);
        // two resources per work
        // |------------------------------------------|
        // ********20**********|********40***********
        List<ResourceBean> resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 2);
        assertEquals(resources.get(0).getEffort(), 20.f);
        assertEquals(resources.get(0).getAllocation(), 50.f);
        assertEquals(resources.get(0).getStartDate(), getDate("01.17.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("01.23.2011"));
        assertEquals(resources.get(1).getEffort(), 40.f);
        assertEquals(resources.get(1).getAllocation(), 100.f);
        assertEquals(resources.get(1).getStartDate(), getDate("01.24.2011"));
        assertEquals(resources.get(1).getEndDate(), getDate("01.28.2011"));
        assertEquals(resSplit.getEffort(), 60.f);
        assertEquals(resSplit.getAllocation(), 75.f);
        // two resources per work
        // |-----------------------------------------------------------------------------------|
        // ********20**********|********140**************************************************
        w1.setPlannedLaborTime(Duration.getDaysDuration(20));
        DataAccessorLogic.runScheduler(pe);
        resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 2);
        assertEquals(resources.get(0).getEffort(), 20.f);
        assertEquals(resources.get(0).getAllocation(), 50.f);
        assertEquals(resources.get(0).getStartDate(), getDate("01.17.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("01.23.2011"));
        assertEquals(resources.get(1).getEffort(), 120.f);
        assertEquals(resources.get(1).getAllocation(), 100.f);
        assertEquals(resources.get(1).getStartDate(), getDate("01.24.2011"));
        assertEquals(resources.get(1).getEndDate(), getDate("02.11.2011"));
        assertEquals(resSplit.getEffort(), 140.f);
        assertEquals(resSplit.getAllocation(), 87.5f);
        DataAccessor.ALLOCATION.set(resSplit, 100.f, pe);
        // one resource per work
        // |-----------------------------------------------------------------------------------|
        // ********160*************************************************************************
        resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 1);
        assertEquals(resources.get(0).getEffort(), 160.f);
        assertEquals(resources.get(0).getAllocation(), 100.f);
        assertEquals(resources.get(0).getStartDate(), getDate("01.17.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("02.11.2011"));
        assertEquals(resSplit.getEffort(), 160.f);
        assertEquals(resSplit.getAllocation(), 100.f);
        w1.setPlannedLaborTime(Duration.getDaysDuration(10));
        DataAccessorLogic.runScheduler(pe);
        // one resource per work
        // |-------------------------------------|
        // ********80***************************
        resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 1);
        assertEquals(resources.get(0).getEffort(), 80.f);
        assertEquals(resources.get(0).getAllocation(), 100.f);
        assertEquals(resources.get(0).getStartDate(), getDate("01.17.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("01.28.2011"));
        assertEquals(resSplit.getEffort(), 80.f);
        assertEquals(resSplit.getAllocation(), 100.f);
        va = DataAccessor.getVAAccessor(new Period(getDate("01.26.2011"), TimeScale.WEEKLY));
        va.set(resSplit, AllocData.getByEffort(12.f), pe);
        // 12.f - 50% allocation for 3 days
        // two resources per work
        // |------------------------------------------|
        // ********56***************|********12******
        resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 2);
        assertEquals(resources.get(0).getEffort(), 56.f);
        assertEquals(resources.get(0).getAllocation(), 100.f);
        assertEquals(resources.get(0).getStartDate(), getDate("01.17.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("01.25.2011"));
        assertEquals(resources.get(1).getEffort(), 12.f);
        assertEquals(resources.get(1).getAllocation(), 50.f);
        assertEquals(resources.get(1).getStartDate(), getDate("01.26.2011"));
        assertEquals(resources.get(1).getEndDate(), getDate("01.28.2011"));
        assertEquals(resSplit.getEffort(), 68.f);
        assertEquals(resSplit.getAllocation(), 85.f);
        w1.setPlannedLaborTime(Duration.getDaysDuration(5));
        DataAccessorLogic.runScheduler(pe);
        // one resource per work
        // |-----------------|
        // ********40*******
        resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 1);
        assertEquals(resources.get(0).getEffort(), 40.f);
        assertEquals(resources.get(0).getAllocation(), 100.f);
        assertEquals(resources.get(0).getStartDate(), getDate("01.17.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("01.21.2011"));
        assertEquals(resSplit.getEffort(), 40.f);
        assertEquals(resSplit.getAllocation(), 100.f);
        w1.setPlannedLaborTime(Duration.getDaysDuration(10));
        DataAccessorLogic.runScheduler(pe);
        va.set(resSplit, AllocData.getByPercentAllocation(50.f), pe);
        DataAccessor.EFFORT.set(resSplit, 40.f, pe);
        // one resource per work
        // |-------------------------------------|
        // ********40***************************
        resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 1);
        assertEquals(resources.get(0).getEffort(), 40.f);
        assertEquals(resources.get(0).getAllocation(), 50.f);
        assertEquals(resources.get(0).getStartDate(), getDate("01.17.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("01.28.2011"));
        assertEquals(resSplit.getEffort(), 40.f);
        assertEquals(resSplit.getAllocation(), 50.f);
        // Test VACOLUMNS
        // one resources per work
        // |------------------------------------------|
        // ********80********|**********20***********
        pe.getDisplaySettings().put(ProjectEditor.DISP_PARAM.SHOW_HOURS, true);
        ColumnValue.getVAColumn(period).setValue(resSplit, "80", pe);
        resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 2);
        assertEquals(resources.get(0).getEffort(), 80.f);
        assertEquals(resources.get(0).getAllocation(), 200.f);
        assertEquals(resources.get(0).getStartDate(), getDate("01.17.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("01.23.2011"));
        assertEquals(resources.get(1).getEffort(), 20.f);
        assertEquals(resources.get(1).getAllocation(), 50.f);
        assertEquals(resources.get(1).getStartDate(), getDate("01.24.2011"));
        assertEquals(resources.get(1).getEndDate(), getDate("01.28.2011"));
        assertEquals(resSplit.getEffort(), 100.f);
        assertEquals(resSplit.getAllocation(), 125.f);
        // Test VACOLUMNS
        // one resources per work
        // |------------------------------------------|
        // ********80********|**********20***********
        pe.getDisplaySettings().put(ProjectEditor.DISP_PARAM.SHOW_HOURS, false);
        ColumnValue.getVAColumn(period).setValue(resSplit, "100", pe);
        resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 2);
        assertEquals(resources.get(0).getEffort(), 40.f);
        assertEquals(resources.get(0).getAllocation(), 100.f);
        assertEquals(resources.get(0).getStartDate(), getDate("01.17.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("01.23.2011"));
        assertEquals(resources.get(1).getEffort(), 20.f);
        assertEquals(resources.get(1).getAllocation(), 50.f);
        assertEquals(resources.get(1).getStartDate(), getDate("01.24.2011"));
        assertEquals(resources.get(1).getEndDate(), getDate("01.28.2011"));
        assertEquals(resSplit.getEffort(), 60.f);
        assertEquals(resSplit.getAllocation(), 75.f);
    }

    public void testVAAllocation2() {
        final ProjectEditor pe = WBSTestHelper.getTestProjectEditor();
        BeansTree tree = pe.getTree();
        WorkBean root = (WorkBean) tree.getRoot();
        root.setPermission(PSPermission.ALL);
        WorkBean w1 = new TestWorkBean("w1", Uuid.create());
        tree.addChild(root, w1);
        w1.setConstraintType(Constraint.ASAP);
        w1.setPlannedLaborTime(Duration.getDaysDuration(6));
        w1.setActualStartDate(WorkScheduleTestUtils.formatDate("01.17.2011"));
        DataAccessorLogic.runScheduler(pe);
        ResourceSplitBean resSplit = (ResourceSplitBean) pe.addResourceSplit(w1);
        assertEquals(resSplit.getEffort(), 48.f);
        assertEquals(resSplit.getAllocation(), 100.f);
        // one resource per word
        // |---------------------|
        // *********************
        assertEquals(w1.getSystemStartDate(), getDate("01.17.2011"));
        assertEquals(w1.getSystemEndDate(), getDate("01.24.2011"));
        List<ResourceBean> resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 1);
        assertEquals(resources.get(0).getEffort(), 48.f);
        assertEquals(resources.get(0).getAllocation(), 100.f);
        assertEquals(resources.get(0).getStartDate(), getDate("01.17.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("01.24.2011"));
        Period period = new Period(getDate("01.24.2011"), TimeScale.WEEKLY);
        DataAccessor va = DataAccessor.getVAAccessor(period);
        va.set(resSplit, AllocData.getByEffort(8), pe);
        // two resources per work
        // |-----------------------|
        // ********20**********|*8*
        resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 2);
        assertEquals(resources.get(0).getEffort(), 40.f);
        assertEquals(resources.get(0).getAllocation(), 100.f);
        assertEquals(resources.get(0).getStartDate(), getDate("01.17.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("01.21.2011"));
        assertEquals(resources.get(1).getEffort(), 8.f);
        assertEquals(resources.get(1).getAllocation(), 100.f);
        assertEquals(resources.get(1).getStartDate(), getDate("01.24.2011"));
        assertEquals(resources.get(1).getEndDate(), getDate("01.24.2011"));
        assertEquals(resSplit.getEffort(), 48.f);
        assertEquals(resSplit.getAllocation(), 100.f);
        // the same action should not change anything
        va.set(resSplit, AllocData.getByEffort(8), pe);
        resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 2);
        assertEquals(resources.get(0).getEffort(), 40.f);
        assertEquals(resources.get(0).getAllocation(), 100.f);
        assertEquals(resources.get(0).getStartDate(), getDate("01.17.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("01.21.2011"));
        assertEquals(resources.get(1).getEffort(), 8.f);
        assertEquals(resources.get(1).getAllocation(), 100.f);
        assertEquals(resources.get(1).getStartDate(), getDate("01.24.2011"));
        assertEquals(resources.get(1).getEndDate(), getDate("01.24.2011"));
        assertEquals(resSplit.getEffort(), 48.f);
        assertEquals(resSplit.getAllocation(), 100.f);
        // shift work forward
        w1.setActualStartDate(getDate("01.20.2011"));
        DataAccessorLogic.runScheduler(pe);
        resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 2);
        assertEquals(resources.get(0).getEffort(), 40.f);
        assertEquals(resources.get(0).getAllocation(), 100.f);
        assertEquals(resources.get(0).getStartDate(), getDate("01.20.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("01.26.2011"));
        assertEquals(resources.get(1).getEffort(), 8.f);
        assertEquals(resources.get(1).getAllocation(), 100.f);
        assertEquals(resources.get(1).getStartDate(), getDate("01.27.2011"));
        assertEquals(resources.get(1).getEndDate(), getDate("01.27.2011"));
        assertEquals(resSplit.getEffort(), 48.f);
        assertEquals(resSplit.getAllocation(), 100.f);
        // shift work forward
        w1.setActualStartDate(getDate("01.12.2011"));
        DataAccessorLogic.runScheduler(pe);
        resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 2);
        assertEquals(resources.get(0).getEffort(), 40.f);
        assertEquals(resources.get(0).getAllocation(), 100.f);
        assertEquals(resources.get(0).getStartDate(), getDate("01.12.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("01.18.2011"));
        assertEquals(resources.get(1).getEffort(), 8.f);
        assertEquals(resources.get(1).getAllocation(), 100.f);
        assertEquals(resources.get(1).getStartDate(), getDate("01.19.2011"));
        assertEquals(resources.get(1).getEndDate(), getDate("01.19.2011"));
        assertEquals(resSplit.getEffort(), 48.f);
        assertEquals(resSplit.getAllocation(), 100.f);
        // test zero allocation
        period = new Period(getDate("01.10.2011"), TimeScale.MONTHLY);
        va = DataAccessor.getVAAccessor(period);
        va.set(resSplit, AllocData.getByPercentAllocation(0), pe);
        resources = getSortedResources(resSplit);
        assertEquals(resources.size(), 1);
        assertEquals(resources.get(0).getEffort(), 0.f);
        assertEquals(resources.get(0).getAllocation(), 0.f);
        assertEquals(resources.get(0).getStartDate(), getDate("01.12.2011"));
        assertEquals(resources.get(0).getEndDate(), getDate("01.19.2011"));
    }

    @Override
    protected void setUp() {
        super.setUp();
        WBSTestHelper.init(WBSTestUtils.createVisitProxy(WBSTestUtils.createUserIfAbsence(WBSTestHelper.OWNER_MAIL,
                getContext(), false)));
        PSSession session = PSSession.createAutoSession(Nobody.get(getContext()));
        User user = session.getPrincipal();
        utils = WorkScheduleTestUtils.getInstance(getContext(), user, session);
        log.info(String.format("-------------------- %s (%d) --------------------", getName(), ++testId));
    }

    private void assertRoughly(float f1, float f2) {
        float d = Math.abs(f1 - f2);
        float m = Math.max(Math.abs(f1), Math.abs(f2));
        if (m != 0) {
            assertTrue(d / m < 0.01f);
        }
    }

    private void checkRoles(Iterator<? extends WBSBean> it, ResourceBean... resources) {
        int i = 0;
        while (it.hasNext()) {
            TPRoleBean role = (TPRoleBean) it.next();
            if (i >= resources.length) {
                assertTrue("Number of roles is more than expected", false);
            }
            assertEquals(role.getUser(), resources[i].getPerson());
            assertEquals(role.getRole(), resources[i].getRole());
            ++i;
        }
        if (i < resources.length) {
            assertTrue("Number of roles is less than expected", false);
        }
    }

    private Timestamp getDate(String time) {
        DateCalculator calc = CalculatorManager.createDateCalculator(CalculatorManager
                .getDefaultCalculator(getContext()));
        return calc.toDayBoundary(WorkScheduleTestUtils.formatDate(time));
    }

    private List<ResourceBean> getSortedResources(ResourceSplitBean resSplit) {
        List<ResourceBean> resources = new ArrayList<ResourceBean>();
        ProjectEditorUtils.getFromIterator(resources, resSplit.getChildrenIterator());
        Comparator<ResourceBean> cmp = new Comparator<ResourceBean>() {

            @Override
            public int compare(ResourceBean o1, ResourceBean o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        };
        Collections.sort(resources, cmp);
        return resources;
    }
}
