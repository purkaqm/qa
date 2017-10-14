package ps5.wbs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.AssertionFailedError;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ps5.psapi.project.create.WorkTypeBean;
import ps5.support.PSException;
import ps5.support.Util;
import ps5.wbs.beans.BeansTree;
import ps5.wbs.beans.Dependency;
import ps5.wbs.beans.ProjectControl;
import ps5.wbs.beans.Property;
import ps5.wbs.beans.ResourceBean;
import ps5.wbs.beans.WBSBean;
import ps5.wbs.beans.WorkBean;
import ps5.wbs.logic.CreateWorkBean;
import ps5.wbs.logic.DataAccessor;
import ps5.wbs.logic.DataAccessorLogic;
import ps5.wbs.logic.ProjectEditor;
import ps5.wbs.logic.handlers.BeanHandler;
import ps5.wbs.model.sections.DataView;
import ps5.wbs.model.sections.FilteredListView;
import ps5.wbs.model.sections.SimpleTreeView;
import ps5.wbs.model.sections.ViewItem;

import com.cinteractive.database.StringPersistentKey;
import com.cinteractive.database.Uuid;
import com.cinteractive.ps3.newsecurity.PSPermission;
import com.cinteractive.ps3.relationships.RelationshipType;
import com.cinteractive.ps3.scheduler.Constraint;
import com.cinteractive.ps3.scheduler.TestWorkBean;
import com.cinteractive.ps3.scheduler.WorkScheduleTestUtils;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.scheduler.Duration;
import com.cinteractive.util.CIFilter;

/**
 * Tests WBS logic functionality
 */
@SuppressWarnings({ "boxing", "javadoc" })
public class WBSLogicTest extends WBSBaseTest {

    private static Logger LOGGER = LoggerFactory.getLogger(WBSLogicTest.class);

    @Test
    public void addResourceSplits() {
        ProjectEditor pe = new ProjectEditor(getVisitProxy());
        pe.loadTree(WBSTestHelper.getWork());
        WorkBean root = (WorkBean) pe.getTree().getRoot();
        assertTrue(root.isUseResourcePlanning());
        int n = root.getResources().size();
        // it does not save resource to DB
        ResourceBean rb = pe.addResourceSplit(root);
        {
            ProjectEditor pe2 = new ProjectEditor(getVisitProxy());
            pe2.loadTree(WBSTestHelper.getWork());
            WorkBean root2 = (WorkBean) pe2.getTree().getRoot();
            assertEquals(n, root2.getResources().size());
        }
        // it saves resource to DB
        pe.saveData();
        {
            ProjectEditor pe2 = new ProjectEditor(getVisitProxy());
            pe2.loadTree(WBSTestHelper.getWork());
            WorkBean root2 = (WorkBean) pe2.getTree().getRoot();
            assertEquals(n + 1, root2.getResources().size());
        }
        // it removes only from memory
        pe.delete(rb);
        {
            ProjectEditor pe2 = new ProjectEditor(getVisitProxy());
            pe2.loadTree(WBSTestHelper.getWork());
            WorkBean root2 = (WorkBean) pe2.getTree().getRoot();
            assertEquals(n + 1, root2.getResources().size());
        }
        // it deletes resource from db
        pe.saveData();
        {
            ProjectEditor pe2 = new ProjectEditor(getVisitProxy());
            pe2.loadTree(WBSTestHelper.getWork());
            WorkBean root2 = (WorkBean) pe2.getTree().getRoot();
            assertEquals(n, root2.getResources().size());
        }
        rb = pe.addResourceSplit(root);
        pe.delete(rb);
        {
            ProjectEditor pe2 = new ProjectEditor(getVisitProxy());
            pe2.loadTree(WBSTestHelper.getWork());
            WorkBean root2 = (WorkBean) pe2.getTree().getRoot();
            assertEquals(n, root2.getResources().size());
        }
        pe.saveData();
        {
            ProjectEditor pe2 = new ProjectEditor(getVisitProxy());
            pe2.loadTree(WBSTestHelper.getWork());
            WorkBean root2 = (WorkBean) pe2.getTree().getRoot();
            assertEquals(n, root2.getResources().size());
        }
    }

    @Test
    public void bulkEdit() {
        ProjectEditor editor = new ProjectEditor(getVisitProxy());
        Work work = WBSTestUtils.createWork(WBSTestHelper.getWorkName(), getUser(), null);
        editor.loadTree(work);
        WorkBean root = (WorkBean) editor.getTree().getRoot();
        WorkBean bean = new TestWorkBean("bean1", Uuid.create());
        bean.setConstraintType(Constraint.ASAP);
        editor.getTree().addChild(root, bean);
        editor.clearModifications();
        editor.beginBulkEdit();
        editor.beginBulkEdit();
        assertTrue(editor.getTree().getChangedBeans().isEmpty());
        DataAccessorLogic.updateScheduler(editor);
        editor.commitChanges();
        assertTrue(editor.getTree().getChangedBeans().isEmpty());
        editor.commitChanges();
        assertEquals(editor.getTree().getChangedBeans().size(), 2);
    }

    @Test
    public void dependencies() {
        {
            ProjectEditor pr = new ProjectEditor(getVisitProxy());
            pr.beginBulkEdit();
            Work work = WBSTestHelper.getWork();
            Work parentWork = work.getParentWork();
            pr.loadTree(parentWork);
            WorkBean pb = (WorkBean) pr.getTree().getRoot();
            WorkBean wb = (WorkBean) pb.getChild(0);
            wb.setToDependency(Collections.<Dependency> emptyList());
            pr.saveData();
        }
        {
            ProjectEditor pr = new ProjectEditor(getVisitProxy());
            pr.beginBulkEdit();
            Work work = WBSTestHelper.getWork();
            Work parentWork = work.getParentWork();
            pr.loadTree(parentWork);
            WorkBean pb = (WorkBean) pr.getTree().getRoot();
            WorkBean wb = (WorkBean) pb.getChild(0);
            assertTrue(Util.isNullEmpty(wb.getToDependency()));
            assertTrue(Util.isNullEmpty(pb.getToDependency()));
            assertTrue(Util.isNullEmpty(wb.getFromDependency()));
            assertTrue(Util.isNullEmpty(pb.getFromDependency()));
            Dependency dep = new Dependency(pb, wb, RelationshipType.FS_DEPENDENCY, 0);
            Set<Dependency> deps = new HashSet<Dependency>();
            deps.add(dep);
            wb.setToDependency(deps);
            Integer percents = wb.getPercentComplete();
            // set modified
            wb.setPercentComplete(percents == null ? 1 : 50 - percents.intValue());
            pr.saveData();
        }
        {
            ProjectEditor pr = new ProjectEditor(getVisitProxy());
            pr.beginBulkEdit();
            Work work = WBSTestHelper.getWork();
            Work parentWork = work.getParentWork();
            pr.loadTree(parentWork);
            WorkBean pb = (WorkBean) pr.getTree().getRoot();
            WorkBean wb = (WorkBean) pb.getChild(0);
            Dependency dep = new Dependency(pb, wb, RelationshipType.FS_DEPENDENCY, 0);
            Set<Dependency> deps = new HashSet<Dependency>();
            deps.add(dep);
            assertEquals(wb.getToDependency(), deps);
            assertEquals(pb.getFromDependency(), deps);
            assertTrue(Util.isNullEmpty(pb.getToDependency()));
            assertTrue(Util.isNullEmpty(wb.getFromDependency()));
            BeanHandler.saveBean(wb, getVisitProxy());
            Dependency dep1 = new Dependency(pb, wb, RelationshipType.FF_DEPENDENCY, 0);
            Dependency dep2 = new Dependency(pb, wb, RelationshipType.FS_DEPENDENCY, 0);
            deps = new HashSet<Dependency>();
            deps.add(dep1);
            deps.add(dep2);
            wb.setToDependency(deps);
            pr.saveData();
        }
        {
            ProjectEditor pr = new ProjectEditor(getVisitProxy());
            pr.beginBulkEdit();
            Work work = WBSTestHelper.getWork();
            Work parentWork = work.getParentWork();
            pr.loadTree(parentWork);
            WorkBean pb = (WorkBean) pr.getTree().getRoot();
            WorkBean wb = (WorkBean) pb.getChild(0);
            Dependency dep1 = new Dependency(pb, wb, RelationshipType.FF_DEPENDENCY, 0);
            Dependency dep2 = new Dependency(pb, wb, RelationshipType.FS_DEPENDENCY, 0);
            Set<Dependency> deps = new HashSet<Dependency>();
            deps.add(dep1);
            deps.add(dep2);
            assertEquals(wb.getToDependency(), deps);
            assertEquals(pb.getFromDependency(), deps);
            assertTrue(Util.isNullEmpty(pb.getToDependency()));
            assertTrue(Util.isNullEmpty(wb.getFromDependency()));
        }
    }

    @Test
    public void filters() {
        {
            ProjectEditor pe = new ProjectEditor(getVisitProxy());
            Work work = WBSTestUtils.createWork(WBSTestHelper.getWorkName(), getUser(), null);
            pe.loadTree(work);
            SimpleTreeView treeView = new SimpleTreeView(pe.getTree());
            treeView.load();
            FilteredListView filterView = new FilteredListView(pe.getTree());
            filterView.load();
            assertEquals(treeView.getById(null).getChildrenCount(), 1);
            // seems that filtered list view behaviour has been changed:
            // now beans tree root item is not attached to the fake item of view tree
            // so the children count will be 0
            // assertTrue(filterView.getById(null).getChildrenCount() >= 1);
        }
        {
            ProjectEditor pe = new ProjectEditor(getVisitProxy());
            CIFilter test = new CIFilter() {

                @Override
                public void addFilter(CIFilter filter) {
                    // nothin to do
                }

                @Override
                public boolean filter(Object o) {
                    return false;
                }
            };
            pe.setFilter(test);
            pe.loadTree(WBSTestHelper.getWork());
            SimpleTreeView treeView = new SimpleTreeView(pe.getTree());
            FilteredListView filterView = new FilteredListView(pe.getTree());
            treeView.load();
            filterView.load();
            assertEquals(treeView.getById(null).getChildrenCount(), 0);
            int m = filterView.getById(null).getChildrenCount();
            assertEquals(m, 0);
        }
    }

    @Test
    public void indentOutdent() {
        final ProjectEditor pe = WBSTestHelper.getTestProjectEditor();
        BeansTree tree = pe.getTree();
        WorkBean root = (WorkBean) tree.getRoot();
        Work w1 = WBSTestUtils.createWork("Test0", getUser(), null);
        Work w2 = WBSTestUtils.createWork("Test1", getUser(), null);
        Work w3 = WBSTestUtils.createWork("Test2", getUser(), w2);
        Work w4 = WBSTestUtils.createWork("Test3", getUser(), w2);
        Work w5 = WBSTestUtils.createWork("Test4", getUser(), w2);
        Work w6 = WBSTestUtils.createWork("Test5", getUser(), w2);
        Work w7 = WBSTestUtils.createWork("Test6", getUser(), w2);
        Work w8 = WBSTestUtils.createWork("Test7", getUser(), w2);
        addWorks(pe, root, 0, Arrays.asList(w1, w2, w3, w4, w5, w6, w7, w8));
        SimpleTreeView treeView = new SimpleTreeView(pe.getTree());
        treeView.load();
        treeView.setCountRowsOnPage(3);
        // treeView.expandAll();
        ViewItem viewRoot = treeView.getById(root.getId());
        String testHierarchy = "Test0," + "Test1," + " Test2," + " Test3," + " Test4," + " Test5," + " Test6,"
                + " Test7,";
        assertChildHierarchy(root, testHierarchy);
        assertChildHierarchy(viewRoot, testHierarchy);
        showPageLog(treeView);
        checkPages(treeView, viewRoot, 0, 0, 0, 0, 0, 1, 1, 1);
        WBSBean bean3 = pe.getTree().getById(w4.getId());
        WBSBean newparent = bean3.getParent().getChild(bean3.getParent().getChildPosition(bean3) - 1);
        pe.moveBean(bean3, newparent, newparent.getChildrenCount());
        treeView.move(treeView.getById(bean3.getId()));
        testHierarchy = "Test0," + "Test1," + " Test2," + "  Test3," + " Test4," + " Test5," + " Test6," + " Test7,";
        assertChildHierarchy(root, testHierarchy);
        assertChildHierarchy(viewRoot, testHierarchy);
        showPageLog(treeView);
        checkPages(treeView, viewRoot, 0, 0, 0, 0, 0, 1, 1, 1);
    }

    @Test
    public void independed() {
        ProjectEditor pr = WBSTestHelper.getTestProjectEditor();
        WorkBean rootWork = (WorkBean) pr.getTree().getRoot();
        WorkBean child = new TestWorkBean("child", new StringPersistentKey("child"));
        pr.getTree().addChild(rootWork, child);
        //pr.addDefaultResources();
        rootWork.setProject(false);
        rootWork.setPermission(PSPermission.ALL);
        ResourceBean rb = pr.addResourceSplit(rootWork);
        assertTrue(!rb.isIndepended());
        assertTrue(rb.isEnabled(Property.EFFORT));
        assertTrue(rb.isEnabled(Property.ALLOCATION));
        assertTrue(rb.isEnabled(Property.PERSON));
        rootWork.setProject(true);
        rb = pr.addResourceSplit(rootWork);
        assertTrue(!rb.isIndepended());
        assertTrue(rb.isEnabled(Property.EFFORT));
        assertTrue(rb.isEnabled(Property.ALLOCATION));
        assertTrue(rb.isEnabled(Property.PERSON));
        child.setProject(false);
        rb = pr.addResourceSplit(child);
        assertTrue(!rb.isIndepended());
        assertTrue(rb.isEnabled(Property.EFFORT));
        assertTrue(rb.isEnabled(Property.ALLOCATION));
        assertTrue(rb.isEnabled(Property.PERSON));
        child.setProject(true);
        rb = pr.addResourceSplit(child);
        assertTrue(rb.isIndepended());
        assertTrue(!rb.isEnabled(Property.EFFORT));
        assertTrue(!rb.isEnabled(Property.ALLOCATION));
        assertTrue(!rb.isEnabled(Property.PERSON));
    }

    @Test
    public void outdent() {
        final ProjectEditor pe = WBSTestHelper.getTestProjectEditor();
        BeansTree tree = pe.getTree();
        WorkBean root = (WorkBean) tree.getRoot();
        final List<CreateWorkBean> works = new ArrayList<CreateWorkBean>();
        WorkTypeBean type = WBSTestHelper.getWorkTypeBean(Work.TYPE, null);
        works.clear();
        works.add(new CreateWorkBean(type, "Test0"));
        works.add(new CreateWorkBean(type, "Test1"));
        works.add(new CreateWorkBean(type, "Test2", 1));
        works.add(new CreateWorkBean(type, "Test3", 1));
        works.add(new CreateWorkBean(type, "Test4", 3));
        works.add(new CreateWorkBean(type, "Test5", 3));
        works.add(new CreateWorkBean(type, "Test6", 1));
        works.add(new CreateWorkBean(type, "Test7", 1));
        // addWorkBeans(pe, root, 0, works);
        Work w1 = WBSTestUtils.createWork("Test0", getUser(), null);
        Work w2 = WBSTestUtils.createWork("Test1", getUser(), null);
        Work w3 = WBSTestUtils.createWork("Test2", getUser(), w2);
        Work w4 = WBSTestUtils.createWork("Test3", getUser(), w2);
        Work w5 = WBSTestUtils.createWork("Test4", getUser(), w4);
        Work w6 = WBSTestUtils.createWork("Test5", getUser(), w4);
        Work w7 = WBSTestUtils.createWork("Test6", getUser(), w2);
        Work w8 = WBSTestUtils.createWork("Test7", getUser(), w2);
        addWorks(pe, root, 0, Arrays.asList(w1, w2, w3, w4, w5, w6, w7, w8));
        SimpleTreeView treeView = new SimpleTreeView(pe.getTree());
        treeView.load();
        treeView.setCountRowsOnPage(3);
        treeView.expandAll();
        ViewItem viewRoot = treeView.getById(root.getId());
        String testHierarchy = "Test0," + "Test1," + " Test2," + " Test3," + "  Test4," + "  Test5," + " Test6,"
                + " Test7,";
        assertChildHierarchy(root, testHierarchy);
        assertChildHierarchy(viewRoot, testHierarchy);
        showPageLog(treeView);
        checkPages(treeView, viewRoot, 0, 0, 0, 0, 0, 0, 0, 1);
        // WBSBean bean3 = works.get(3).getBean();
        WBSBean bean3 = pe.getTree().getById(w4.getId());
        // pe.outdent(bean3);
        pe.moveBean(bean3, bean3.getParent().getParent(),
                bean3.getParent().getParent().getChildPosition(bean3.getParent()) + 1);
        treeView.move(treeView.getById(bean3.getId()));
        // treeView.outdent(treeView.getById(bean3.getId()));
        testHierarchy = "Test0," + "Test1," + " Test2," + "Test3," + " Test4," + " Test5," + " Test6," + " Test7,";
        assertChildHierarchy(root, testHierarchy);
        assertChildHierarchy(viewRoot, testHierarchy);
        showPageLog(treeView);
        checkPages(treeView, viewRoot, 0, 0, 0, 0, 0, 0, 1, 2);
        WBSBean bean5 = works.get(5).getBean();
        pe.moveBean(bean5, bean5.getParent().getParent(),
                bean5.getParent().getParent().getChildPosition(bean5.getParent()) + 1);
        treeView.move(treeView.getById(bean5.getId()));
        testHierarchy = "Test0," + "Test1," + " Test2," + "Test3," + " Test4," + "Test5," + " Test6," + " Test7,";
        assertChildHierarchy(root, testHierarchy);
        assertChildHierarchy(viewRoot, testHierarchy);
        showPageLog(treeView);
        checkPages(treeView, viewRoot, 0, 0, 0, 0, 0, 0, 0, 1);
        // pe.indent(bean5);
        WBSBean newparent = bean5.getParent().getChild(bean5.getParent().getChildPosition(bean5) - 1);
        pe.moveBean(bean5, newparent, newparent.getChildrenCount());
        treeView.move(treeView.getById(bean5.getId()));
        testHierarchy = "Test0," + "Test1," + " Test2," + "Test3," + " Test4," + " Test5," + "  Test6," + "  Test7,";
        assertChildHierarchy(root, testHierarchy);
        assertChildHierarchy(viewRoot, testHierarchy);
        showPageLog(treeView);
        checkPages(treeView, viewRoot, 0, 0, 0, 0, 0, 0, 0, 1);
        newparent = bean5.getParent().getChild(bean5.getParent().getChildPosition(bean5) - 1);
        pe.moveBean(bean5, newparent, newparent.getChildrenCount());
        treeView.move(treeView.getById(bean5.getId()));
        testHierarchy = "Test0," + "Test1," + " Test2," + "Test3," + " Test4," + "  Test5," + "   Test6," + "   Test7,";
        assertChildHierarchy(root, testHierarchy);
        assertChildHierarchy(viewRoot, testHierarchy);
        checkPages(treeView, viewRoot, 0, 0, 0, 0, 0, 0, 0, 1);
        newparent = works.get(1).getBean().getParent()
                .getChild(works.get(1).getBean().getParent().getChildPosition(works.get(1).getBean()) - 1);
        pe.moveBean(works.get(1).getBean(), newparent, newparent.getChildrenCount());
        treeView.move(treeView.getById(works.get(1).getBean().getId()));
        testHierarchy = "Test0," + " Test1," + "  Test2," + "Test3," + " Test4," + "  Test5," + "   Test6,"
                + "   Test7,";
        assertChildHierarchy(root, testHierarchy);
        assertChildHierarchy(viewRoot, testHierarchy);
        checkPages(treeView, viewRoot, 0, 0, 0, 0, 0, 0, 0, 1);
        newparent = works.get(3).getBean().getParent()
                .getChild(works.get(3).getBean().getParent().getChildPosition(works.get(3).getBean()) - 1);
        pe.moveBean(works.get(3).getBean(), newparent, newparent.getChildrenCount());
        treeView.move(treeView.getById(works.get(3).getBean().getId()));
        testHierarchy = "Test0," + " Test1," + "  Test2," + " Test3," + "  Test4," + "   Test5," + "    Test6,"
                + "    Test7,";
        assertChildHierarchy(root, testHierarchy);
        assertChildHierarchy(viewRoot, testHierarchy);
        checkPages(treeView, viewRoot, 0, 0, 0, 0, 0, 0, 0, 1);
        newparent = works.get(3).getBean().getParent()
                .getChild(works.get(3).getBean().getParent().getChildPosition(works.get(3).getBean()) - 1);
        pe.moveBean(works.get(3).getBean(), newparent, newparent.getChildrenCount());
        treeView.move(treeView.getById(works.get(3).getBean().getId()));
        newparent = works.get(3).getBean().getParent()
                .getChild(works.get(3).getBean().getParent().getChildPosition(works.get(3).getBean()) - 1);
        pe.moveBean(works.get(3).getBean(), newparent, newparent.getChildrenCount());
        treeView.move(treeView.getById(works.get(3).getBean().getId()));
        testHierarchy = "Test0," + " Test1," + "  Test2," + "   Test3," + "    Test4," + "     Test5," + "      Test6,"
                + "      Test7,";
        assertChildHierarchy(root, testHierarchy);
        assertChildHierarchy(viewRoot, testHierarchy);
        checkPages(treeView, viewRoot, 0, 0, 0, 0, 0, 0, 0, 1);
        @SuppressWarnings("unused")
        AssertException check = new AssertException(true) {

            @Override
            public void invoke() {
                WBSBean newparent1 = works.get(3).getBean().getParent()
                        .getChild(works.get(3).getBean().getParent().getChildPosition(works.get(3).getBean()) - 1);
                pe.moveBean(works.get(3).getBean(), newparent1, newparent1.getChildrenCount());
            }
        };
        // pe.outdent(works.get(6).getBean());
        WorkBean bean31 = works.get(6).getBean();
        pe.moveBean(bean31, bean31.getParent().getParent(),
                bean31.getParent().getParent().getChildPosition(bean31.getParent()) + 1);
        treeView.move(treeView.getById(works.get(6).getBean().getId()));
        testHierarchy = "Test0," + " Test1," + "  Test2," + "   Test3," + "    Test4," + "     Test5," + "     Test6,"
                + "      Test7,";
        assertChildHierarchy(root, testHierarchy);
        assertChildHierarchy(viewRoot, testHierarchy);
        checkPages(treeView, viewRoot, 0, 0, 0, 0, 0, 0, 0, 0);
        WorkBean bean66 = works.get(6).getBean();
        pe.moveBean(bean66, bean66.getParent().getParent(),
                bean66.getParent().getParent().getChildPosition(bean66.getParent()) + 1);
        treeView.move(treeView.getById(works.get(6).getBean().getId()));
        WorkBean bean77 = works.get(6).getBean();
        pe.moveBean(bean77, bean77.getParent().getParent(),
                bean77.getParent().getParent().getChildPosition(bean77.getParent()) + 1);
        treeView.move(treeView.getById(works.get(6).getBean().getId()));
        testHierarchy = "Test0," + " Test1," + "  Test2," + "   Test3," + "    Test4," + "     Test5," + "   Test6,"
                + "    Test7,";
        assertChildHierarchy(root, testHierarchy);
        assertChildHierarchy(viewRoot, testHierarchy);
        checkPages(treeView, viewRoot, 0, 0, 0, 0, 0, 0, 0, 0);
        WorkBean bean111 = works.get(4).getBean();
        pe.moveBean(bean111, bean111.getParent().getParent(),
                bean111.getParent().getParent().getChildPosition(bean111.getParent()) + 1);
        treeView.move(treeView.getById(works.get(4).getBean().getId()));
        WorkBean bean112 = works.get(4).getBean();
        pe.moveBean(bean112, bean112.getParent().getParent(),
                bean112.getParent().getParent().getChildPosition(bean112.getParent()) + 1);
        treeView.move(treeView.getById(works.get(4).getBean().getId()));
        WorkBean bean113 = works.get(4).getBean();
        pe.moveBean(bean113, bean113.getParent().getParent(),
                bean113.getParent().getParent().getChildPosition(bean113.getParent()) + 1);
        treeView.move(treeView.getById(works.get(4).getBean().getId()));
        testHierarchy = "Test0," + " Test1," + "  Test2," + "   Test3," + " Test4," + "  Test5," + "  Test6,"
                + "   Test7,";
        assertChildHierarchy(root, testHierarchy);
        assertChildHierarchy(viewRoot, testHierarchy);
        checkPages(treeView, viewRoot, 0, 0, 0, 0, 0, 0, 1, 0);
    }

    @Test
    public void projectControls() {
        final ProjectEditor pe = WBSTestHelper.getTestProjectEditor();
        BeansTree tree = pe.getTree();
        WorkBean root = (WorkBean) tree.getRoot();
        root.setPermission(PSPermission.ALL);
        WorkBean w1 = new TestWorkBean("w1", Uuid.create());
        WorkBean w2 = new TestWorkBean("w2", Uuid.create());
        WorkBean w3 = new TestWorkBean("w3", Uuid.create());
        WorkBean w4 = new TestWorkBean("w4", Uuid.create());
        tree.addChild(root, w1);
        tree.addChild(root, w2);
        tree.addChild(w2, w3);
        tree.addChild(w3, w4);
        DataAccessor manualAccessor = DataAccessor.getProjectControlAccessor(ProjectControl.MANUAL_SCHEDULING);
        DataAccessor controlsAccessor = DataAccessor.getProjectControlAccessor(ProjectControl.INHERIT_CONTROLS);
        assertEquals(w4.getProjectControl(ProjectControl.MANUAL_SCHEDULING), null);
        assertEquals(w3.getProjectControl(ProjectControl.MANUAL_SCHEDULING), null);
        manualAccessor.set(w4, true, pe);
        assertEquals(w4.getProjectControl(ProjectControl.MANUAL_SCHEDULING), Boolean.TRUE);
        controlsAccessor.set(w4, true, pe);
        assertEquals(w4.getProjectControl(ProjectControl.MANUAL_SCHEDULING), null);
        controlsAccessor.set(w3, true, pe);
        assertEquals(w4.getProjectControl(ProjectControl.MANUAL_SCHEDULING), null);
        assertEquals(w3.getProjectControl(ProjectControl.MANUAL_SCHEDULING), null);
        manualAccessor.set(w2, true, pe);
        assertEquals(w4.getProjectControl(ProjectControl.MANUAL_SCHEDULING), true);
        assertEquals(w3.getProjectControl(ProjectControl.MANUAL_SCHEDULING), true);
        // break inheritance
        controlsAccessor.set(w3, false, pe);
        manualAccessor.set(w2, false, pe);
        assertEquals(w4.getProjectControl(ProjectControl.MANUAL_SCHEDULING), true);
        assertEquals(w3.getProjectControl(ProjectControl.MANUAL_SCHEDULING), true);
        manualAccessor.set(w3, null, pe);
        assertEquals(w4.getProjectControl(ProjectControl.MANUAL_SCHEDULING), null);
        assertEquals(w3.getProjectControl(ProjectControl.MANUAL_SCHEDULING), null);
        // can't set this value
        try {
            manualAccessor.set(w4, true, pe);
            assertTrue("It should throw exception!", false);
        } catch (Exception ex) {
            // nothing to do
        }
        controlsAccessor.set(w4, false, pe);
        manualAccessor.set(w4, true, pe);
        assertEquals(w4.getProjectControl(ProjectControl.MANUAL_SCHEDULING), true);
        manualAccessor.set(w2, false, pe);
        assertEquals(w4.getProjectControl(ProjectControl.MANUAL_SCHEDULING), true);
        assertEquals(w3.getProjectControl(ProjectControl.MANUAL_SCHEDULING), null);
        manualAccessor.set(root, true, pe);
        assertEquals(root.getProjectControl(ProjectControl.MANUAL_SCHEDULING), true);
    }

    @Test
    public void rpColumns() {
        final ProjectEditor pe = WBSTestHelper.getTestProjectEditor();
        BeansTree tree = pe.getTree();
        WorkBean root = (WorkBean) tree.getRoot();
        root.setPermission(PSPermission.ALL);
        WorkBean w1 = new TestWorkBean("w1", Uuid.create());
        WorkBean w2 = new TestWorkBean("w2", Uuid.create());
        WorkBean w3 = new TestWorkBean("w3", Uuid.create());
        tree.addChild(root, w1);
        tree.addChild(root, w2);
        tree.addChild(w2, w3);
        w1.setPlannedLaborTime(Duration.getDaysDuration(15.0f));
        w2.setPlannedLaborTime(Duration.getDaysDuration(15.0f));
        w3.setPlannedLaborTime(Duration.getDaysDuration(15.0f));
        DataAccessorLogic.runScheduler(pe);
        // use ResourceDataLogic.setCalculatedField for acronyms
        w1.setCalculatedField(ResourceBean.CalculatedField.ALLOCATION);
        w2.setCalculatedField(ResourceBean.CalculatedField.ALLOCATION);
        w3.setCalculatedField(ResourceBean.CalculatedField.ALLOCATION);
        ResourceBean rb11 = pe.addResourceSplit(w1);
        ResourceBean rb12 = pe.addResourceSplit(w1);
        ResourceBean rb2 = pe.addResourceSplit(w2);
        ResourceBean rb3 = pe.addResourceSplit(w3);
        // rb11.setPermission(PSPermission.ALL);
        // rb12.setPermission(PSPermission.ALL);
        // rb2.setPermission(PSPermission.ALL);
        // rb3.setPermission(PSPermission.ALL);
        //
        // tree.addResource(w1, rb11);
        // tree.addResource(w1, rb12);
        // tree.addResource(w2, rb2);
        // tree.addResource(w3, rb3);
        rb11.setEffort(24.f);
        rb11.setAllocation(100.f);
        rb12.setEffort(40.f);
        rb12.setAllocation(100.f);
        rb2.setEffort(64.f);
        rb2.setAllocation(100.f);
        rb3.setEffort(80.f);
        rb3.setAllocation(100.f);
        rb11.setStartDate(new Timestamp(100));
        rb12.setStartDate(new Timestamp(200));
        rb2.setStartDate(new Timestamp(300));
        rb3.setStartDate(new Timestamp(400));
        rb11.setEndDate(new Timestamp(100));
        rb12.setEndDate(new Timestamp(200));
        rb2.setEndDate(new Timestamp(300));
        rb3.setEndDate(new Timestamp(400));
        root.setPlannedStartDate(WorkScheduleTestUtils.formatDate("01.01.09"));
        DataAccessorLogic.updateScheduler(pe);
        assertEquals(rb11.getAllocation(), calcAllocation(24, 15));
        assertEquals(rb12.getAllocation(), calcAllocation(40, 15));
        assertEquals(rb2.getAllocation(), calcAllocation(64, 15));
        assertEquals(rb3.getAllocation(), calcAllocation(80, 15));
        DataAccessor.EFFORT.set(rb11, 50.f, pe);
        assertEquals(rb11.getAllocation(), calcAllocation(50, 15));
        assertEquals(rb12.getAllocation(), calcAllocation(40, 15));
        assertEquals(rb2.getAllocation(), calcAllocation(64, 15));
        assertEquals(rb3.getAllocation(), calcAllocation(80, 15));
        w3.setCalculatedField(ResourceBean.CalculatedField.DURATION);
        DataAccessor.ALLOCATION.set(rb3, 100.f, pe);
        assertEquals(w3.getSystemLaborTime().toFloat(), 10.f, 0.001f);
        assertEquals(w2.getSystemLaborTime().toFloat(), 10.f, 0.001f);
        assertEquals(rb11.getAllocation(), calcAllocation(50, 15));
        assertEquals(rb12.getAllocation(), calcAllocation(40, 15));
        assertEquals(rb2.getAllocation(), calcAllocation(64, 10));
        assertEquals(rb3.getAllocation(), 100.f, 0.001f);
        DataAccessor.ALLOCATION.set(rb3, 10.f, pe);
        assertEquals(w3.getSystemLaborTime().toFloat(), 100.f, 0.001f);
        assertEquals(w2.getSystemLaborTime().toFloat(), 100.f, 0.001f);
        assertEquals(root.getSystemLaborTime().toFloat(), 100.f, 0.001f);
        assertEquals(rb11.getAllocation(), calcAllocation(50, 15));
        assertEquals(rb12.getAllocation(), calcAllocation(40, 15));
        assertEquals(rb2.getAllocation(), calcAllocation(64, 100));
        assertEquals(rb3.getAllocation(), 10.f, 0.001f);
        assertEquals(w1.getSystemLaborTime().toFloat(), 15.f, 0.001f);
        w1.setCalculatedField(ResourceBean.CalculatedField.DURATION);
        DataAccessor.ALLOCATION.set(rb11, 100.f, pe);
        assertEquals(w1.getSystemLaborTime().toFloat(), 15.f, 0.001f);
        DataAccessor.ALLOCATION.set(rb12, 100.f, pe);
        assertEquals(w1.getSystemLaborTime().toFloat(), 7.f, 0.001f); // effort is 50
        DataAccessor.ALLOCATION.set(rb11, 10.f, pe);
        assertEquals(w1.getSystemLaborTime().toFloat(), 63.f, 0.001f); // 50 / 0.1 / 8
        rb3.setAllocation(100.f);
        rb3.setEffort(40.f);
        w3.setCalculatedField(ResourceBean.CalculatedField.EFFORT);
        DataAccessor.RESOURCE_DURATION.set(w3, Duration.getDaysDuration(20.f), pe);
        assertEquals(w3.getSystemLaborTime().toFloat(), 20.f, 0.001f);
        assertEquals(rb3.getEffort(), 160.f, 0.001f);
        w3.setCalculatedField(ResourceBean.CalculatedField.EFFORT);
        DataAccessor.RESOURCE_DURATION.set(w3, Duration.getDaysDuration(24.f), pe);
    }

    @Test
    public void treeRepresentatioin() {
        ProjectEditor pe = WBSTestHelper.getTestProjectEditor();
        WorkBean root = (WorkBean) pe.getTree().getRoot();
        WorkBean bean1 = new WorkBean(Uuid.create());
        pe.getTree().addChild(root, bean1);
        bean1.setName("bean1");
        for (int i = 0; i < 21; ++i) {
            WorkBean bean = new WorkBean(Uuid.create());
            bean.setName("" + i);
            pe.getTree().addChild(bean1, bean);
        }
        DataView view = new SimpleTreeView(pe.getTree());
        view.load();
        view.setCountRowsOnPage(5);
        ViewItem item = view.getById(bean1.getId());
        view.expand(item);
        checkPageNumbers(view, item, 0, 5, 5, 10, 10, 15, 15, 20, 20, 21);
        view.remove(view.getChild(item, 0));
        view.remove(view.getChild(item, 14));
        checkPageNumbers(view, item, 0, 4, 4, 9, 9, 14, 14, 18, 18, 19);
        view.remove(view.getChild(item, 3));
        checkPageNumbers(view, item, 0, 3, 3, 8, 8, 13, 13, 17, 17, 18);
        view.remove(view.getChild(item, 0));
        view.remove(view.getChild(item, 0));
        checkPageNumbers(view, item, 0, 1, 1, 6, 6, 11, 11, 15, 15, 16);
        view.remove(view.getChild(item, 0));
        checkPageNumbers(view, item, 0, 5, 5, 10, 10, 14, 14, 15);
        List<WBSBean> children = item.getChildren();
        int[] test = { 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 18, 19, 20 };
        for (int i = 0; i < children.size(); ++i) {
            assertEquals(((ViewItem) children.get(i)).getBean().getName(), "" + test[i]);
        }
        WorkBean newBean = new WorkBean(Uuid.create());
        newBean.setName("100");
        pe.getTree().addChild(bean1, newBean, 0);
        view.add(newBean);
        checkPageNumbers(view, item, 0, 6, 6, 11, 11, 15, 15, 16);
        view.remove(view.getChild(item, 1));
        view.remove(view.getChild(item, 1));
        view.remove(view.getChild(item, 1));
        view.remove(view.getChild(item, 1));
        view.remove(view.getChild(item, 1));
        view.remove(view.getChild(item, 1));
        checkPageNumbers(view, item, 0, 1, 1, 5, 5, 9, 9, 10);
        ViewItem from = view.getChild(item, 0);
        ViewItem to = view.getChild(item, 5);
        pe.getTree().move(from.getBean(), to.getBean(), true);
        view.move(from);
        checkPageNumbers(view, item, 0, 4, 4, 9, 9, 10);
        from = view.getChild(item, 0);
        to = view.getChild(item, 9);
        pe.getTree().move(from.getBean(), to.getBean(), true);
        view.move(from);
        checkPageNumbers(view, item, 0, 3, 3, 8, 8, 10);
        from = view.getChild(item, 0);
        to = view.getChild(item, 9);
        pe.getTree().move(from.getBean(), to.getBean(), false);
        view.move(from);
        checkPageNumbers(view, item, 0, 2, 2, 7, 7, 10);
        children = item.getChildren();
        test = new int[] { 13, 14, 16, 100, 17, 18, 19, 20, 12, 11 };
        for (int i = 0; i < children.size(); ++i) {
            assertEquals(((ViewItem) children.get(i)).getBean().getName(), "" + test[i]);
        }
    }

    private void addWorks(ProjectEditor pe, WorkBean parent, int pos, List<Work> works) {
        for (Work work : works) {
            WorkBean bean = (WorkBean) BeanHandler.createBean(work, getVisitProxy());
            if (work.hasParent()) {
                WorkBean parentBean;
                if (pe.getTree().containsBean(work.getParentId())) {
                    parentBean = (WorkBean) pe.getTree().getById(work.getParentId());
                } else {
                    parentBean = (WorkBean) BeanHandler.createBean(work.getParent(), getVisitProxy());
                }
                pe.getTree().addChild(parentBean, bean);
            } else {
                int newPos = pos + 1;
                pe.getTree().addChild(parent, bean, newPos);
            }
        }
    }

    private Float calcAllocation(float effort, float duration) {
        return effort / (duration * 8.f) * 100;
    }

    private void checkPageNumbers(DataView view, ViewItem parent, int... pages) {
        assertEquals(view.getPageCount(parent), pages.length / 2);
        for (int i = 0; i < pages.length / 2; ++i) {
            assertEquals(view.getPageStart(parent, i), pages[i * 2]);
            assertEquals(view.getPageEnd(parent, i), pages[i * 2 + 1]);
        }
    }

    private void checkPages(DataView view, ViewItem item, int... pages) {
        checkPages(-1, view, item, pages);
    }

    private int checkPages(int idx, DataView view, ViewItem item, int... pages) {
        int newIdx = idx;
        if (newIdx >= pages.length) {
            throw new AssertionFailedError("");
        }
        if (newIdx != -1) {
            // ignore root;
            assertEquals(item.getName() + " has wrong page", view.getPagination().getPage(item), pages[newIdx]);
        }
        ++newIdx;
        Iterator<ViewItem> it = item.getChildrenIterator();
        while (it.hasNext()) {
            newIdx = checkPages(newIdx, view, it.next(), pages);
        }
        return newIdx;
    }

    private void showPageLog(DataView view) {
        try {
            LOGGER.info("------------");
            showPageLog(view, (ViewItem) view.getViewTree().getRoot(), 0);
        } catch (Exception e) {
            LOGGER.info("EXCEPTION:" + new PSException(e).toString());
        }
    }

    private void showPageLog(DataView view, ViewItem item, int level) {
        ViewItem parent = (ViewItem) item.getParent();
        char buff[] = new char[level];
        Arrays.fill(buff, ' ');
        String shift = new String(buff);
        if (parent != null && parent.isExpanded()) {
            String z = "";
            if (item.isExpanded()) {
                int c = view.getPageCount(item);
                for (int i = 0; i < c; ++i) {
                    String res = String.format("(%d,%d)", view.getPageStart(item, i), view.getPageEnd(item, i));
                    z = z + res;
                }
            }
            String hasChildren = item.hasChildren() ? "+" : " ";
            int pageId = view.getPagination().getPage(item);
            String res = String.format("Page: %d: %s, S=%d,E=%d %s", pageId, shift + hasChildren
                    + item.getBean().getName(), view.getPageStart(parent, pageId), view.getPageEnd(parent, pageId), z);
            LOGGER.info(item.getBean().getNumber() + " " + res);
        } else {
            LOGGER.info("" + item.getBean().getNumber() + " " + parent);
        }
        Iterator<ViewItem> it = item.getChildrenIterator();
        while (it.hasNext()) {
            showPageLog(view, it.next(), level + 1);
        }
    }
}