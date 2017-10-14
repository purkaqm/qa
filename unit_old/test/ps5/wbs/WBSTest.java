package ps5.wbs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.hasItems;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ps5.psapi.project.create.WorkTypeBean;
import ps5.psapi.project.create.WorkTypeHelp;
import ps5.support.Util;
import ps5.wbs.WBSTestHelper.IndexedDependencySet;
import ps5.wbs.beans.BeansTree;
import ps5.wbs.beans.CheckpointBean;
import ps5.wbs.beans.Dependency;
import ps5.wbs.beans.DocumentBean;
import ps5.wbs.beans.GPBean;
import ps5.wbs.beans.IndexedDependency;
import ps5.wbs.beans.ProjectControl;
import ps5.wbs.beans.Property;
import ps5.wbs.beans.ResourceBean;
import ps5.wbs.beans.ResourceSplitBean;
import ps5.wbs.beans.RoleAdapterBean;
import ps5.wbs.beans.TextMessageBean;
import ps5.wbs.beans.WBSBean;
import ps5.wbs.beans.WorkBean;
import ps5.wbs.logic.CalculatorManager;
import ps5.wbs.logic.CreateWorkBean;
import ps5.wbs.logic.CreateWorkHelper;
import ps5.wbs.logic.DataAccessor;
import ps5.wbs.logic.OwnerDataAccessor.OwnerColumnBean;
import ps5.wbs.logic.ProjectEditor;
import ps5.wbs.logic.ProjectEditorUtils;
import ps5.wbs.logic.handlers.BeanHandler;
import ps5.wbs.logic.validation.ValidationError;
import ps5.wbs.logic.validation.ValidationHelp;
import ps5.wbs.model.ColumnValue;
import ps5.wbs.model.DependencyColumn;
import ps5.wbs.model.DisplaySettings;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.database.Uuid;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.documents.Document;
import com.cinteractive.ps3.documents.UrlDocument;
import com.cinteractive.ps3.entities.Group;
import com.cinteractive.ps3.entities.ResourcePool;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.mockups.CustomField;
import com.cinteractive.ps3.newsecurity.PSPermission;
import com.cinteractive.ps3.relationships.DocumentRelationship;
import com.cinteractive.ps3.relationships.RelationshipType;
import com.cinteractive.ps3.resources.Need;
import com.cinteractive.ps3.scheduler.Constraint;
import com.cinteractive.ps3.scheduler.TestWorkBean;
import com.cinteractive.ps3.tollgate.Tollgate;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkStatus;
import com.cinteractive.ps3.work.WorkUtil.SortOrder;
import com.cinteractive.scheduler.Duration;

/**
 * Tests for WBS backend functionality
 */
@SuppressWarnings({ "boxing", "javadoc" })
public class WBSTest extends WBSBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WBSTest.class);

    @Test
    public void addDeleteMove() {
        Work w = WBSTestUtils.createWork(WBSTestHelper.getWorkName(), getUser(), null);
        // tests ADD
        // -----------------------------------------------------------------------------------
        ProjectEditor pe = new ProjectEditor(getVisitProxy());
        pe.loadTree(w);
        WorkBean root = (WorkBean) pe.getTree().getRoot();
        assertEquals(pe.getTree().getByIndex(1).getName(), w.getName());
        List<CreateWorkBean> works = new ArrayList<CreateWorkBean>();
        WorkTypeBean type = WBSTestHelper.getWorkTypeBean(Work.TYPE, null);
        works.add(new CreateWorkBean(type, "Test1"));
        works.add(new CreateWorkBean(type, "Test2"));
        IndexedDependency deps[] = new IndexedDependency[] {
                new IndexedDependency(-1, RelationshipType.FS_DEPENDENCY, 0),
                new IndexedDependency(-2, RelationshipType.SS_DEPENDENCY, 0),
                new IndexedDependency(-4, RelationshipType.SF_DEPENDENCY, 0) };
        CreateWorkBean cwb = new CreateWorkBean(type, "Test3", null);
        cwb.setDependencies(Arrays.asList(deps));
        works.add(cwb);
        works.add(new CreateWorkBean(type, "Test4"));
        pe.addWorks(root, 0, new CreateWorkHelper(works));
        WBSBean bean = pe.getTree().getByIndex(1);
        WorkBean b1 = (WorkBean) pe.getTree().getByIndex(2);
        WorkBean b2 = (WorkBean) pe.getTree().getByIndex(3);
        WorkBean b3 = (WorkBean) pe.getTree().getByIndex(4);
        WorkBean b4 = (WorkBean) pe.getTree().getByIndex(5);
        assertEquals(bean, root);
        String testHierarchy = "Test1," + "Test2," + "Test3," + "Test4";
        assertChildHierarchy(bean, testHierarchy);
        assertTrue(root.getToDependency().isEmpty());
        assertTrue(root.getFromDependency().isEmpty());
        assertTrue(b1.getToDependency().isEmpty());
        assertEquals(b1.getFromDependency().size(), 1);
        assertEquals(b1.getFromDependency().iterator().next().getTo(), b3);
        assertTrue(b2.getToDependency().isEmpty());
        assertEquals(b2.getFromDependency().size(), 1);
        assertEquals(b2.getFromDependency().iterator().next().getTo(), b3);
        assertEquals(b3.getToDependency().size(), 3);
        assertTrue(b3.getFromDependency().isEmpty());
        assertTrue(b4.getToDependency().isEmpty());
        assertEquals(b4.getFromDependency().size(), 1);
        assertEquals(b4.getFromDependency().iterator().next().getTo(), b3);
        // -----------------------------------------------------------------------------------
        works = new ArrayList<CreateWorkBean>();
        deps = new IndexedDependency[] { new IndexedDependency(-1, RelationshipType.FS_DEPENDENCY, 0),
                new IndexedDependency(2, RelationshipType.SS_DEPENDENCY, 0), };
        works.add(new CreateWorkBean(type, "Test5"));
        cwb = new CreateWorkBean(type, "Test6", null);
        cwb.setDependencies(Arrays.asList(deps));
        works.add(cwb);
        pe.addWorks(root, root.getChildrenCount(), new CreateWorkHelper(works));
        WorkBean b5 = (WorkBean) pe.getTree().getByIndex(6);
        WorkBean b6 = (WorkBean) pe.getTree().getByIndex(7);
        testHierarchy = "Test1," + "Test2," + "Test3," + "Test4," + "Test5," + "Test6";
        assertChildHierarchy(bean, testHierarchy);
        assertTrue(b1.getToDependency().isEmpty());
        // assertEquals(b1.getFromDependency().size(), 2);
        assertTrue(b5.getToDependency().isEmpty());
        assertEquals(b5.getFromDependency().size(), 1);
        assertEquals(b5.getFromDependency().iterator().next().getTo(), b6);
        assertEquals(b6.getToDependency().size(), 2);
        assertTrue(b6.getFromDependency().isEmpty());
        Iterator<Dependency> it = b6.getToDependency().iterator();
        WorkBean ref1 = it.next().getFrom();
        WorkBean ref2 = it.next().getFrom();
        assertTrue(ref1.equals(b1) || ref2.equals(b1));
        assertTrue(ref1.equals(b5) || ref2.equals(b5));
        // adds hierarchical structure
        // -----------------------------------------------------------------------------------
        works = new ArrayList<CreateWorkBean>();
        cwb = new CreateWorkBean(type, "Test7", null);
        works.add(cwb);
        cwb = new CreateWorkBean(type, "Test8", 0);
        works.add(cwb);
        cwb = new CreateWorkBean(type, "Test9", 0);
        works.add(cwb);
        cwb = new CreateWorkBean(type, "Test10", 1);
        works.add(cwb);
        pe.addWorks(b6, 0, new CreateWorkHelper(works));
        assertEquals(b6.getChildrenCount(), 1);
        WorkBean b7 = (WorkBean) b6.getChild(0);
        assertEquals(b7.getName(), "Test7");
        assertEquals(b7.getChildrenCount(), 2);
        WorkBean b8 = (WorkBean) b7.getChild(0);
        WorkBean b9 = (WorkBean) b7.getChild(1);
        assertEquals(b8.getName(), "Test8");
        assertEquals(b9.getName(), "Test9");
        assertEquals(b8.getChildrenCount(), 1);
        WorkBean b10 = (WorkBean) b8.getChild(0);
        assertEquals(b10.getName(), "Test10");
        // tests DELETE
        // -----------------------------------------------------------------------------------
        pe.delete(b5);
        pe.delete(b6);
        assertEquals(root.getChildrenCount(), 4);
        {
            ProjectEditor pe2 = new ProjectEditor(getVisitProxy());
            pe2.loadTree(w);
            assertEquals(pe.getTree().getRoot().getChildrenCount(), 4);
        }
        // tests MOVE
        // -----------------------------------------------------------------------------------
        // false means under
        pe.moveBean(b4, b2, 0);
        List<WBSBean> beans = root.getChildren();
        assertBeans(beans, b1, b4, b2, b3);
        assertSequence(beans, 1, 2, 3, 4);
        {
            ProjectEditor pe2 = new ProjectEditor(getVisitProxy());
            pe2.loadTree(w);
            assertEquals(pe2.getTree().getRoot().getChildrenCount(), 4);
            beans = pe2.getTree().getRoot().getChildren();
            assertBeans(beans, b1, b4, b2, b3);
            assertSequence(beans, 1, 2, 3, 4);
        }
        // true means after
        pe.moveBean(b3, b4, b4.getParent().getChildPosition(b4) + 1);
        beans = root.getChildren();
        assertBeans(beans, b1, b4, b3, b2);
        assertSequence(beans, 1, 2, 3, 4);
        {
            ProjectEditor pe2 = new ProjectEditor(getVisitProxy());
            pe2.loadTree(w);
            assertEquals(pe2.getTree().getRoot().getChildrenCount(), 4);
            beans = pe2.getTree().getRoot().getChildren();
            assertBeans(beans, b1, b4, b3, b2);
            assertSequence(beans, 1, 2, 3, 4);
        }
        pe.moveBean(b1, b4, b4.getParent().getChildPosition(b4) + 1);
        beans = root.getChildren();
        assertBeans(beans, b4, b1, b3, b2);
        assertSequence(beans, 1, 2, 3, 4);
        {
            ProjectEditor pe2 = new ProjectEditor(getVisitProxy());
            pe2.loadTree(w);
            assertEquals(pe2.getTree().getRoot().getChildrenCount(), 4);
            beans = pe2.getTree().getRoot().getChildren();
            assertBeans(beans, b4, b1, b3, b2);
            assertSequence(beans, 1, 2, 3, 4);
        }
        pe.moveBean(b4, b3, 0);
        beans = root.getChildren();
        assertBeans(beans, b1, b4, b3, b2);
        assertSequence(beans, 1, 2, 3, 4);
        {
            ProjectEditor pe2 = new ProjectEditor(getVisitProxy());
            pe2.loadTree(w);
            assertEquals(pe2.getTree().getRoot().getChildrenCount(), 4);
            beans = pe2.getTree().getRoot().getChildren();
            assertBeans(beans, b1, b4, b3, b2);
            assertSequence(beans, 1, 2, 3, 4);
        }
    }

    /**
     * Tests add tollgate
     */
    @Test
    public void addTollgate() {
        Work w = WBSTestUtils.createWork(WBSTestHelper.getWorkName(), getUser(), null);
        ProjectEditor pe = new ProjectEditor(getVisitProxy());
        pe.loadTree(w);
        WorkBean root = (WorkBean) pe.getTree().getRoot();
        List<WorkTypeBean> types = WorkTypeHelp.getWorkTemplates(w, getVisit());
        WorkTypeBeanEx ttTemplate = null;
        for (WorkTypeBean t : types) {
            WorkTypeBeanEx type = new WorkTypeBeanEx(t, getVisitProxy());
            if (type.getType().equals(Tollgate.TYPE)) {
                ttTemplate = type;
                break;
            }
        }
        if (ttTemplate == null) {
            throw new RuntimeException("Can't find tollgate template");
        }
        List<CreateWorkBean> works = new ArrayList<CreateWorkBean>();
        works.add(new CreateWorkBean(ttTemplate, "Test1"));
        pe.addWorks(root, 0, new CreateWorkHelper(works));
        assertTrue(!pe.getMessageTracker().getMessages().hasNext());
        GPBean gp = (GPBean) root.getChild(0);
        assertTrue(gp.hasChildren());
    }

    /**
     * Tests accessing to beans data via properties
     */
    @Test
    public void beansProperties() {
        Object testData[][] = new Object[][] {
                { Property.NAME, WBSBean.class, String.class },
                { Property.NUMBER, WBSBean.class, Integer.class },
                { Property.SEQUENCE, WBSBean.class, Integer.class },
                { Property.getActionProperty(WBSBean.Action.ADD_CHILD), WBSBean.class, Boolean.class },
                { Property.getActionProperty(WBSBean.Action.ADD_RESOURCE_FILTER), WBSBean.class, Boolean.class },
                { Property.getActionProperty(WBSBean.Action.CHANGE_PARENT), WBSBean.class, Boolean.class },
                { Property.getActionProperty(WBSBean.Action.DELETE), WBSBean.class, Boolean.class },
                { Property.getActionProperty(WBSBean.Action.MOVE), WBSBean.class, Boolean.class },
                // works data
                { Property.OWNER, WorkBean.class, User.class },
                { Property.CONSTRAINT, WorkBean.class, Constraint.class },
                { Property.PLANNED_START_DATE, WorkBean.class, Timestamp.class },
                { Property.PLANNED_END_DATE, WorkBean.class, Timestamp.class },
                { Property.PLANNED_LABOR_TIME, WorkBean.class, Duration.class },
                { Property.ACTUAL_START_DATE, WorkBean.class, Timestamp.class },
                { Property.ACTUAL_END_DATE, WorkBean.class, Timestamp.class },
                { Property.SYSTEM_START_DATE, WorkBean.class, Timestamp.class },
                { Property.SYSTEM_END_DATE, WorkBean.class, Timestamp.class },
                { Property.SYSTEM_LABOR_TIME, WorkBean.class, Duration.class },
                { Property.BASELINE_START_DATE, WorkBean.class, Timestamp.class },
                { Property.BASELINE_END_DATE, WorkBean.class, Timestamp.class },
                { Property.BASELINE_LABOR_TIME, WorkBean.class, Duration.class },
                { Property.DEPENDENCY, WorkBean.class, WBSTestHelper.DependencySet.class },
                { Property.PRIORITY, WorkBean.class, Integer.class },
                { Property.STATUS, WorkBean.class, WorkStatus.class },
                { Property.PERCENT_COMPLETE, WorkBean.class, Integer.class },
                { Property.SORT_ORDER, WorkBean.class, SortOrder.class },
                { Property.getCustomFieldProperty(Uuid.get("1")), WBSBean.class, CustomField.class },
                { Property.getUsersByRoleProperty(Uuid.get("1")), WorkBean.class, WBSTestHelper.UserSet.class },
                { Property.getProjectControlProperty(ProjectControl.CONTROL_COSTS), WorkBean.class, Boolean.class },
                { Property.getProjectControlProperty(ProjectControl.INHERIT_CALENDAR), WorkBean.class, Boolean.class },
                { Property.getProjectControlProperty(ProjectControl.INHERIT_PERMISSIONS), WorkBean.class, Boolean.class },
                { Property.getProjectControlProperty(ProjectControl.INHERIT_CONTROLS), WorkBean.class, Boolean.class },
                { Property.getProjectControlProperty(ProjectControl.MANUAL_SCHEDULING), WorkBean.class, Boolean.class },
                // Gated Project
                { Property.ACTIVE_GATE, GPBean.class, PersistentKey.class },
                // Resource
                { Property.EFFORT, ResourceBean.class, Float.class },
                { Property.ALLOCATION, ResourceBean.class, Float.class },
                { Property.ROLE, ResourceBean.class, RoleAdapterBean.class },
                { Property.RESOURCE_POOL, ResourceBean.class, ResourcePool.class },
                { Property.PERSON, ResourceBean.class, User.class } };
        Class<?>[] dataTypes = WBSTestHelper.getTestedDataTypes(false);
        for (Object d[] : testData) {
            WBSBean beans[] = new WBSBean[] { new WBSBean(Uuid.get("1")), new WorkBean(Uuid.get("2")),
                    new GPBean(Uuid.get("3")), new ResourceBean(Uuid.get("4")) };
            Property property = (Property) d[0];
            Class<?> beanType = (Class<?>) d[1];
            Class<?> dataType = (Class<?>) d[2];
            for (WBSBean bean : beans) {
                bean.initialize();
                for (Class<?> type : dataTypes) {
                    boolean catched = false;
                    Object data1 = WBSTestHelper.createTestData(bean, type, getVisitProxy());
                    Object data2 = WBSTestHelper.createTestData(bean, type, getVisitProxy());
                    if (data1 == null || data2 == null) {
                        continue;
                    }
                    Exception e = null;
                    try {
                        property.set(bean, data1);
                        assertTrue(bean.getClass().getName() + " set " + property.getName() + "(" + data1 + ")",
                                bean.isModified(property));
                        bean.clearModified();
                        assertTrue(!bean.isModified(property));
                        property.set(bean, data2);
                        assertTrue(
                                bean.getClass().getName() + " set " + property.getName() + "(" + data2 + ") " + type,
                                bean.isModified(property));
                        assertEquals(bean.toString(), property.get(bean), data2);
                        assertTrue(bean.isModified(property));
                    } catch (ValidationError er) {
                        catched = true;
                        e = er;
                    } catch (ClassCastException ex) {
                        // special management of Set<User>, Set<Dependency>, Set<PSTag> data types
                        if (!Set.class.isAssignableFrom(type) || !Set.class.isAssignableFrom(dataType)
                                || type.equals(dataType)) {
                            property.set(bean, data1);
                            fail("Class Cast Exception:" + bean.getClass().getName() + " set " + property.getName()
                                    + "(" + data1 + "," + data2 + ")");
                        }
                    }
                    if (catched && beanType.isInstance(bean) && type.equals(dataType)) {
                        LOGGER.error("", e);
                        fail("Should not throw exception:" + bean.getClass().getName() + " set " + property.getName()
                                + "(" + data1 + ")");
                    }
                    if (!catched && !beanType.isInstance(bean) && !type.equals(dataType)) {
                        fail("Should throw exception:" + bean.getClass().getName() + " set " + property.getName() + "("
                                + data1 + ")");
                    }
                }
            }
        }
    }

    /**
     * Tests accessing to data by columns
     */
    @Test
    public void columnAccessor() {
        ProjectEditor pr = new ProjectEditor(getVisitProxy());
        Work w = WBSTestUtils.createWork(WBSTestHelper.getWorkName(), getUser(), null);
        pr.loadTree(w);
        WBSBean bean = new TestWorkBean("A1_", Uuid.toUuid("1"));
        BeansTree tree = new BeansTree(bean);
        WBSBean bean2 = new TestWorkBean("A2", Uuid.toUuid("2"));
        WBSBean bean3 = new TestWorkBean("A3", Uuid.toUuid("3"));
        WBSBean bean4 = new TestWorkBean("A4", Uuid.toUuid("4"));
        WBSBean bean5 = new TestWorkBean("A5", Uuid.toUuid("5"));
        WBSBean bean6 = new TestWorkBean("A6", Uuid.toUuid("6"));
        tree.addChild(bean, bean2);
        tree.addChild(bean, bean3);
        tree.addChild(bean2, bean4);
        tree.addChild(bean4, bean5);
        tree.addChild(bean2, bean6);
        tree.clearBeansModifications();
        bean6.setNumber(23);
        bean.setName("A1");
        bean2.setEnabled(Property.NAME, false);
        assertTrue(bean2.isEnabledStateModified(Property.NAME));
        assertTrue(!bean2.isEnabled(Property.NAME));
        assertEquals(tree.getChangedBeans().size(), 3);
        ColumnValue columns[] = new ColumnValue[] { ColumnValue.NAME, ColumnValue.NUMBER };
        Set<WBSBean> modified = tree.getChangedBeans();
        List<String> modActual = new ArrayList<String>();
        for (WBSBean b : modified) {
            for (ColumnValue c : columns) {
                if (c.isModified(b) || c.isEnabledStateModified(b)) {
                    String str = b.getName() + ":" + c.getPropertyName() + ":" + c.getDisplayValue(b, pr);
                    modActual.add(str);
                }
            }
        }
        assertThat(modActual, hasItems("A2:NAME:A2", "A1:NAME:A1", "A6:NUMBER:23"));
    }

    /**
     * Tests accessing to data by columns
     */
    @Test
    public void columnAccessor2() {
        ProjectEditor pr = new ProjectEditor(getVisitProxy());
        Work w = WBSTestUtils.createWork(WBSTestHelper.getWorkName(), getUser(), null);
        pr.loadTree(w);
        Object testData[][] = new Object[][] {
                { ColumnValue.NAME, WBSBean.class },
                { ColumnValue.NUMBER, WBSBean.class },
                { ColumnValue.OWNER, WBSBean.class },
                { ColumnValue.SEQUENCE, WBSBean.class },
                // works data
                { ColumnValue.CONSTRAINT, WorkBean.class }, { ColumnValue.PLANNED_START_DATE, WorkBean.class },
                { ColumnValue.PLANNED_END_DATE, WorkBean.class }, { ColumnValue.PLANNED_LABOR_TIME, WorkBean.class },
                { ColumnValue.ACTUAL_START_DATE, WorkBean.class }, { ColumnValue.ACTUAL_END_DATE, WorkBean.class },
                { ColumnValue.SYSTEM_START_DATE, WorkBean.class }, { ColumnValue.SYSTEM_END_DATE, WorkBean.class },
                { ColumnValue.SYSTEM_LABOR_TIME, WorkBean.class }, { ColumnValue.BASELINE_START_DATE, WorkBean.class },
                { ColumnValue.BASELINE_END_DATE, WorkBean.class }, { ColumnValue.BASELINE_LABOR_TIME, WorkBean.class },
                { ColumnValue.DEPENDENCY, WorkBean.class },
                { ColumnValue.PRIORITY, WorkBean.class },
                { ColumnValue.STATUS, WorkBean.class },
                { ColumnValue.PERCENT_COMPLETE, WorkBean.class },
                { ColumnValue.getProjectControlColumn(ProjectControl.CONTROL_COSTS, pr), WorkBean.class },
                { ColumnValue.getProjectControlColumn(ProjectControl.INHERIT_CALENDAR, pr), WorkBean.class },
                { ColumnValue.getProjectControlColumn(ProjectControl.INHERIT_PERMISSIONS, pr), WorkBean.class },
                { ColumnValue.getProjectControlColumn(ProjectControl.INHERIT_CONTROLS, pr), WorkBean.class },
                { ColumnValue.getProjectControlColumn(ProjectControl.MANUAL_SCHEDULING, pr), WorkBean.class },
                { ColumnValue.getCustomFieldColumn(Uuid.get("1")), WBSBean.class },
                // Gated Project
                { ColumnValue.ACTIVE_GATE, GPBean.class },
                // Resource
                { ColumnValue.EFFORT, ResourceBean.class }, { ColumnValue.ALLOCATION, ResourceBean.class },
                { ColumnValue.ROLE, ResourceBean.class }, { ColumnValue.RESOURCE_POOL, ResourceBean.class },
                { ColumnValue.PERSON, ResourceBean.class } };
        // all incompatible type gives error in this test
        for (Object d[] : testData) {
            ColumnValue column = (ColumnValue) d[0];
            Class<?> beanType = (Class<?>) d[1];
            WBSBean bean1 = new WBSBean(Uuid.get("1"));
            TestWorkBean bean2 = new TestWorkBean("2", Uuid.get("2"));
            GPBean bean3 = new GPBean(Uuid.get("3"));
            bean3.setPSType(Tollgate.TYPE);
            ResourceBean bean4 = new ResourceBean(Uuid.get("4"));
            WBSBean beans[] = new WBSBean[] { bean1, bean2, bean3, bean4 };
            for (WBSBean bean : beans) {
                bean.setPermission(PSPermission.ALL);
                String value = null;
                try {
                    value = column.getDisplayValue(bean, pr);
                } catch (Exception ex) {
                    //
                }
                if (!beanType.isInstance(bean)) {
                    assertTrue(value == null || value.isEmpty());
                }
            }
        }
    }

    /**
     * Tests copying only modified properties
     */
    @Test
    public void copyFields1() {
        {
            WBSBean bean1 = new WBSBean();
            WBSBean bean2 = new WBSBean();
            WBSBean bean3 = new WorkBean(Uuid.create());
            assertTrue(ProjectEditorUtils.copyUpdatedFields(bean1, bean2, bean3).isEmpty());
        }
        {
            WorkBean bean1 = new TestWorkBean("Test1", "1");
            WorkBean bean2 = new TestWorkBean("Test2", "2");
            WorkBean bean3 = new TestWorkBean("Test2", "3");
            bean1.setOwner(WBSTestHelper.getUser(1));
            bean1.setEnabled(Property.NAME, false);
            bean2.setOwner(WBSTestHelper.getUser(2));
            bean3.setOwner(WBSTestHelper.getUser(3));
            bean1.clearSaved();
            Set<Property> props = ProjectEditorUtils.copyUpdatedFields(bean1, bean2, bean3);
            assertEquals(props, new HashSet<Property>(Arrays.asList(new Property[] { Property.OWNER })));
            assertEquals(bean1.getName(), "Test1");
            assertEquals(bean1.getOwner(), WBSTestHelper.getUser(2));
            assertTrue(!bean1.isEnabled(Property.NAME));
            bean1.setOwner(WBSTestHelper.getUser(1));
            bean2.setName("Test4");
            bean1.clearSaved();
            props = ProjectEditorUtils.copyUpdatedFields(bean1, bean2, bean3);
            assertEquals(props, new HashSet<Property>(Arrays.asList(new Property[] { Property.NAME, Property.OWNER })));
            assertEquals(bean1.getName(), "Test4");
        }
    }

    /**
     * Tests copying only modified properties
     */
    @Test
    public void copyFields11() {
        WorkBean bean1 = new TestWorkBean("Test1", "1");
        WorkBean bean2 = new TestWorkBean("Test2", "2");
        WorkBean bean3 = new TestWorkBean("Test2", "3");
        bean1.setOwner(WBSTestHelper.getUser(1));
        bean1.setEnabled(Property.NAME, false);
        bean2.setOwner(WBSTestHelper.getUser(2));
        bean3.setOwner(WBSTestHelper.getUser(3));
        bean1.clearSaved();
        Set<Property> props = ProjectEditorUtils.copyUpdatedFields(bean1, bean2, bean3);
        assertEquals(props, new HashSet<Property>(Arrays.asList(new Property[] { Property.OWNER })));
        assertEquals(bean1.getName(), "Test1");
        assertEquals(bean1.getOwner(), WBSTestHelper.getUser(2));
        assertTrue(!bean1.isEnabled(Property.NAME));
        bean1.setOwner(WBSTestHelper.getUser(1));
        bean2.setName("Test4");
        bean1.clearSaved();
        props = ProjectEditorUtils.copyUpdatedFields(bean1, bean2, bean3);
        assertEquals(props, new HashSet<Property>(Arrays.asList(new Property[] { Property.NAME, Property.OWNER })));
        assertEquals(bean1.getName(), "Test4");
    }

    /**
     * Tests copying only modified properties
     */
    @Test
    @Ignore
    public void copyFields2() {
        Object testData[][] = new Object[][] {
                { Property.NAME, WBSBean.class, String.class },
                // {Property.ID, WBSBean.class, PersistentKey.class},
                { Property.NUMBER, WBSBean.class, Integer.class },
                { Property.OWNER, WBSBean.class, User.class },
                { Property.SEQUENCE, WBSBean.class, Integer.class },
                // {Property.getTagSetProperty(Uuid.get("1")), WBSBean.class, WBSTestUtils.SetOfTags.class},
                // {Property.IS_ENABLED, WBSBean.class,},
                // works data
                { Property.CONSTRAINT, WorkBean.class, Constraint.class },
                { Property.PLANNED_START_DATE, WorkBean.class, Timestamp.class },
                { Property.PLANNED_END_DATE, WorkBean.class, Timestamp.class },
                { Property.PLANNED_LABOR_TIME, WorkBean.class, Duration.class },
                { Property.ACTUAL_START_DATE, WorkBean.class, Timestamp.class },
                { Property.ACTUAL_END_DATE, WorkBean.class, Timestamp.class },
                { Property.BASELINE_START_DATE, WorkBean.class, Timestamp.class },
                { Property.BASELINE_END_DATE, WorkBean.class, Timestamp.class },
                { Property.BASELINE_LABOR_TIME, WorkBean.class, Duration.class },
                { Property.DEPENDENCY, WorkBean.class, WBSTestHelper.DependencySet.class },
                { Property.PRIORITY, WorkBean.class, Integer.class },
                { Property.STATUS, WorkBean.class, WorkStatus.class },
                { Property.PERCENT_COMPLETE, WorkBean.class, Integer.class },
                // {Property.ACTIVITY_CODE_TAG, WorkBean.class, //X
                // {Property.ASSIGNED_TO, WorkBean.class, //X RO SUMMARY
                { Property.getCustomFieldProperty(Uuid.get("1")), WorkBean.class, CustomField.class },
                { Property.getUsersByRoleProperty(Uuid.get("1")), WorkBean.class, WBSTestHelper.UserSet.class },
                { Property.getProjectControlProperty(ProjectControl.CONTROL_COSTS), WorkBean.class, Boolean.class },
                { Property.getProjectControlProperty(ProjectControl.INHERIT_CALENDAR), WorkBean.class, Boolean.class },
                { Property.getProjectControlProperty(ProjectControl.INHERIT_PERMISSIONS), WorkBean.class, Boolean.class },
                { Property.getProjectControlProperty(ProjectControl.INHERIT_CONTROLS), WorkBean.class, Boolean.class },
                { Property.getProjectControlProperty(ProjectControl.MANUAL_SCHEDULING), WorkBean.class, Boolean.class },
                // Gated Project
                { Property.ACTIVE_GATE, GPBean.class, GPBean.class },
                // Resource
                { Property.EFFORT, ResourceBean.class, Float.class },
                { Property.ALLOCATION, ResourceBean.class, Float.class },
                { Property.ROLE, ResourceBean.class, Group.class },
                { Property.RESOURCE_POOL, ResourceBean.class, ResourcePool.class },
                { Property.PERSON, ResourceBean.class, User.class } };
        @SuppressWarnings("unchecked")
        Class<WBSBean> beans[] = new Class[] { WBSBean.class, WorkBean.class, GPBean.class, ResourceBean.class };
        for (Class<WBSBean> beanClass : beans) {
            for (Object d[] : testData) {
                Property property = (Property) d[0];
                Class<?> beanType = (Class<?>) d[1];
                Class<?> dataType = (Class<?>) d[2];
                if (beanType.equals(beanClass)) {
                    continue;
                }
                WBSBean bean1 = WBSTestHelper.createBean(beanClass);
                WBSBean bean2 = WBSTestHelper.createBean(beanClass);
                WBSBean bean3 = WBSTestHelper.createBean(beanClass);
                Object data1 = WBSTestHelper.createTestData(dataType, getVisitProxy());
                Object data2 = WBSTestHelper.createTestData(dataType, getVisitProxy());
                Object data3 = WBSTestHelper.createTestData(dataType, getVisitProxy());
                Object[] data = new Object[] { data1, data2, data3 };
                for (int i = 0; i < 27; ++i) {
                    data1 = data[i / 9];
                    data2 = data[i % 9 / 3];
                    data3 = data[i % 3];
                    try {
                        property.set(bean1, data2);
                        property.set(bean1, data1);
                        property.set(bean2, data1);
                        property.set(bean2, data2);
                        property.set(bean3, data2);
                        property.set(bean3, data3);
                        // it should copy only modified fields
                        // bean1.clearSaved();
                        ProjectEditorUtils.copyUpdatedFields(bean1, bean2, bean3);
                        assertEquals(property.toString(), property.get(bean1),
                                Util.checkObjectsEquals(data2, data3) ? data1 : data2);
                    } catch (ValidationError e) {
                        //
                    }
                }
            }
        }
    }

    @Test
    public void copySelectedProps() {
        WBSBean bean1 = new WBSBean();
        WBSBean bean2 = new WBSBean();
        bean1.setSequence(1);
        bean2.setSequence(2);
        bean1.setName("NAME1");
        bean2.setName("NAME2");
        bean1.copyPropertiesFrom(bean2, Collections.singletonList(Property.SEQUENCE), true);
        assertEquals(bean1.getName(), "NAME1");
        assertEquals(bean1.getSequence(), 2);
        bean1.copyPropertiesFrom(bean2, Collections.singletonList(Property.NAME), true);
        assertEquals(bean1.getName(), "NAME2");
        assertEquals(bean1.getSequence(), 2);
    }

    /**
     * Tests accessing to beans data via data accessors
     */
    @Test
    public void dataAccessor() {
        Object testData[][] = new Object[][] {
                { DataAccessor.NAME, WBSBean.class, String.class, false },
                { DataAccessor.NUMBER, WBSBean.class, Integer.class, true },
                { DataAccessor.SEQUENCE, WBSBean.class, Integer.class, true },
                // works data
                { DataAccessor.CONSTRAINT, WorkBean.class, Constraint.class, false },
                { DataAccessor.PLANNED_START_DATE, WorkBean.class, Timestamp.class, false },
                { DataAccessor.PLANNED_END_DATE, WorkBean.class, Timestamp.class, false },
                { DataAccessor.PLANNED_LABOR_TIME, WorkBean.class, Duration.class, false },
                { DataAccessor.ACTUAL_START_DATE, WorkBean.class, Timestamp.class, false },
                { DataAccessor.ACTUAL_END_DATE, WorkBean.class, Timestamp.class, false },
                { DataAccessor.BASELINE_START_DATE, WorkBean.class, Timestamp.class, true },
                { DataAccessor.BASELINE_END_DATE, WorkBean.class, Timestamp.class, true },
                { DataAccessor.BASELINE_LABOR_TIME, WorkBean.class, Duration.class, true },
                { DataAccessor.DEPENDENCY, WorkBean.class, WBSTestHelper.DependencySet.class, false },
                { DataAccessor.PRIORITY, WorkBean.class, Integer.class, false },
                { DataAccessor.STATUS, WorkBean.class, WorkStatus.class, false },
                { DataAccessor.PERCENT_COMPLETE, WorkBean.class, Integer.class, false },
                { DataAccessor.getProjectControlAccessor(ProjectControl.CONTROL_COSTS), WorkBean.class, Boolean.class,
                        false },
                { DataAccessor.getProjectControlAccessor(ProjectControl.INHERIT_CALENDAR), WorkBean.class,
                        Boolean.class, true },
                { DataAccessor.getProjectControlAccessor(ProjectControl.INHERIT_PERMISSIONS), WorkBean.class,
                        Boolean.class, true },
                { DataAccessor.getProjectControlAccessor(ProjectControl.INHERIT_CONTROLS), WorkBean.class,
                        Boolean.class, true },
                { DataAccessor.getProjectControlAccessor(ProjectControl.MANUAL_SCHEDULING), WorkBean.class,
                        Boolean.class, false },
                { DataAccessor.getCustomFieldAccessor(Uuid.get("1")), WBSBean.class, CustomField.class, true },
                // Gated Project
                { DataAccessor.ACTIVE_GATE, GPBean.class, PersistentKey.class, true },
                // Resource
                { DataAccessor.OWNER, WorkBean.class, OwnerColumnBean.class, false },
                { DataAccessor.EFFORT, ResourceBean.class, Float.class, false },
                { DataAccessor.ALLOCATION, ResourceBean.class, Float.class, false },
                { DataAccessor.ROLE, ResourceBean.class, RoleAdapterBean.class, false },
                { DataAccessor.RESOURCE_POOL, ResourceBean.class, ResourcePool.class, false },
                { DataAccessor.PERSON, ResourceBean.class, User.class, false } };
        WorkBean root = new WorkBean(Uuid.create());
        WorkBean defWork = new WorkBean(Uuid.create());
        defWork.setPSType(Work.TYPE);
        BeansTree defTree = new BeansTree(root);
        defTree.addChild(root, defWork);
        Class<?>[] dataTypes = WBSTestHelper.getTestedDataTypes(true);
        ProjectEditor editor = new ProjectEditor(getVisitProxy());
        editor.loadTree(WBSTestHelper.getWork().getParentWork());
        editor.beginBulkEdit();
        for (Object d[] : testData) {
            WBSBean beans[] = new WBSBean[] { new WBSBean(Uuid.get("1")), new TestWorkBean("2", Uuid.get("2")),
                    (WBSBean) WBSTestHelper.createTestData(GPBean.class, getVisitProxy()),
                    new ResourceBean(Uuid.get("4")) };
            beans[0].setPSType(Work.TYPE);
            beans[1].setPSType(Work.TYPE);
            beans[2].setPSType(Work.TYPE);
            beans[3].setPSType(Need.TYPE);
            DataAccessor accessor = (DataAccessor) d[0];
            Class<?> beanType = (Class<?>) d[1];
            Class<?> dataType = (Class<?>) d[2];
            boolean readOnly = (Boolean) d[3];
            for (WBSBean bean : beans) {
                bean.initialize();
                for (Class<?> type : dataTypes) {
                    ValidationError validationError = null;
                    boolean roField = false;
                    Object data1 = WBSTestHelper.createTestData(bean, type, getVisitProxy());
                    Object data2 = WBSTestHelper.createTestData(bean, type, getVisitProxy());
                    if (data1 == null || data2 == null) {
                        continue;
                    }
                    if (bean instanceof ResourceBean && !defWork.equals(((ResourceBean) bean).getWork())) {
                        defTree.addResource(defWork, (ResourceBean) bean);
                    }
                    try {
                        accessor.set(bean, data2, editor);
                        accessor.set(bean, data1, editor);
                        assertTrue(bean.getClass().getSimpleName() + " set " + accessor.getPropertyName() + "(" + data1
                                + ") " + type, accessor.isModified(bean));
                        bean.clearModified();
                        assertTrue(!accessor.isModified(bean));
                        accessor.set(bean, data2, editor);
                        assertTrue(bean.getClass().getSimpleName() + " set " + accessor.getPropertyName() + "(" + data2
                                + ") " + type, accessor.isModified(bean));
                        if (!IndexedDependencySet.class.equals(type)) {
                            assertEquals(accessor.get(bean), data2);
                        }
                    } catch (ValidationError er) {
                        if ("DataHandlingError.RO_FIELD".equals(er.getKey())) {
                            roField = true;
                        }
                        validationError = er;
                    } catch (ClassCastException ex) {
                        // special management of Set<User>, Set<Dependency>, Set<PSTag> data types
                        if (!Set.class.isAssignableFrom(type) || !Set.class.isAssignableFrom(dataType)
                                || type.equals(dataType)) {
                            fail("Class Cast Exception:" + bean.getClass().getName() + " set "
                                    + accessor.getPropertyName() + "(" + data1 + "," + data2 + ")");
                        }
                    }
                    if (validationError != null && beanType.isInstance(bean) && type.equals(dataType) && !roField) {
                        if (!DataAccessor.STATUS.equals(accessor)
                                || !"DataHandlingError.UNABLE_TO_LOAD".equals(validationError.getKey())) {
                            LOGGER.error("", validationError);
                            fail("Should not throw exception:" + bean.getClass().getName() + " set "
                                    + accessor.getPropertyName() + "(" + data1 + ")");
                        }
                    }
                    if (validationError == null && readOnly) {
                        fail("Should throw exception:" + bean.getClass().getName() + " set "
                                + accessor.getPropertyName() + "(" + data1 + ")");
                    }
                }
            }
        }
    }

    @Test
    public void defaultWork() {
        Work w = WBSTestUtils.createWork(WBSTestHelper.getWorkName(), getUser(), null);
        WorkBean bean = (WorkBean) BeanHandler.createBean(w, getVisitProxy());
        assertEquals(bean.getOwner(), getUser());
    }

    /**
     * Tests tree's dependencies
     */
    @Test
    public void dependencies() {
        WBSBean bean = new TestWorkBean("", Uuid.create());
        final BeansTree tree = new BeansTree(bean);
        final WorkBean bean2 = new TestWorkBean("", Uuid.create());
        final WorkBean bean3 = new TestWorkBean("", Uuid.create());
        final WorkBean bean4 = new TestWorkBean("", Uuid.create());
        tree.addChild(bean, bean2);
        tree.addChild(bean, bean3);
        tree.addChild(bean2, bean4);
        final Dependency dep = new Dependency(bean2.getId(), bean3.getId(), RelationshipType.FS_DEPENDENCY, 0);
        Set<Dependency> dependencies = new HashSet<Dependency>();
        dependencies.add(dep);
        bean3.setToDependency(dependencies);
        Set<Dependency> deps = bean3.getToDependency();
        assertEquals(deps.size(), 1);
        assertTrue(!dep.isInitialized());
        assertTrue(bean2.getFromDependency().isEmpty());
        assertEquals(deps.iterator().next().getFrom(), null);
        @SuppressWarnings("unused")
        AssertException assertException = new AssertException(true) {

            @Override
            public void invoke() {
                tree.initializeDependency(dep, bean2, bean4);
            }
        };
        tree.initializeDependency(dep, bean2, bean3);
        deps = bean3.getToDependency();
        assertEquals(deps.size(), 1);
        assertTrue(dep.isInitialized());
        assertEquals(bean2.getFromDependency().size(), 1);
        assertEquals(deps.iterator().next().getFrom(), bean2);
        assertEquals(bean2.getFromDependency().iterator().next().getTo(), bean3);
        final Dependency dep2 = new Dependency(bean2.getId(), bean4.getId(), RelationshipType.FS_DEPENDENCY, 0);
        dependencies = new HashSet<Dependency>();
        dependencies.add(dep2);
        bean4.setToDependency(dependencies);
        tree.initializeDependency(dep2, bean2, bean4);
        // test dependency updation
        tree.clearBeansModifications();
        bean2.setNumber(100);
        assertEquals(bean2.isModified(Property.NUMBER), true);
        assertEquals(bean2.isModified(Property.DEPENDENCY_NUMBER), false);
        assertEquals(bean3.isModified(Property.NUMBER), false);
        assertEquals(bean3.isModified(Property.DEPENDENCY_NUMBER), true);
        // saved states were modified too
        assertEquals(bean2.getChangedProperties().size(), 2);
        assertEquals(bean3.getChangedProperties().size(), 1);
        tree.clearBeansModifications();
        tree.remove(bean2);
        assertEquals(bean3.getChangedProperties().size(), 1);
        assertEquals(bean3.getToDependency().size(), 0);
        assertEquals(bean4.getChangedProperties().size(), 1);
        assertEquals(bean4.getToDependency().size(), 0);
    }

    @Test
    public void dependencyColumn() {
        final ProjectEditor pr = WBSTestHelper.getTestProjectEditor();
        pr.loadTree(WBSTestHelper.getWork());
        WorkBean bean = (WorkBean) pr.getTree().getRoot();
        BeansTree tree = pr.getTree();
        WorkBean bean2 = new TestWorkBean("A2", Uuid.create());
        bean2.setConstraintType(Constraint.ASAP);
        WorkBean bean3 = new TestWorkBean("A3", Uuid.create());
        bean3.setConstraintType(Constraint.ASAP);
        WorkBean bean4 = new TestWorkBean("A4", Uuid.create());
        bean4.setConstraintType(Constraint.ASAP);
        WorkBean bean5 = new TestWorkBean("A5", Uuid.create());
        bean5.setConstraintType(Constraint.ASAP);
        WorkBean bean6 = new TestWorkBean("A6", Uuid.create());
        bean6.setConstraintType(Constraint.ASAP);
        tree.addChild(bean, bean2);
        tree.addChild(bean, bean3);
        tree.addChild(bean, bean4);
        tree.addChild(bean, bean5);
        tree.addChild(bean, bean6);
        tree.reorder(1);
        JSONArray value = new JSONArray();
        JSONObject dep = new JSONObject();
        dep.put("id", "null");
        dep.put("num", 2);
        dep.put("lag", -2);
        dep.put("type", "FF");
        dep.put("sign", "+");
        value.put(dep);
        dep = new JSONObject();
        dep.put("id", "null");
        dep.put("num", 3);
        dep.put("lag", 3);
        dep.put("type", "SS");
        dep.put("sign", "+");
        value.put(dep);
        dep = new JSONObject();
        dep.put("id", "null");
        dep.put("num", 2);
        dep.put("lag", 3); // only lag is differ
        dep.put("type", "FF");
        dep.put("sign", "+");
        value.put(dep);
        dep = new JSONObject();
        dep.put("id", bean4.getId().toString());
        dep.put("num", "null");
        dep.put("lag", 0); // only lag differs
        dep.put("type", "SF");
        dep.put("sign", "+");
        value.put(dep);
        DependencyColumn c = (DependencyColumn) ColumnValue.DEPENDENCY;
        Set<IndexedDependency> indDeps = c.fromJSONString(value.toString());
        IndexedDependency.idToNum(indDeps, pr);
        assertEquals(c.toDisplayString(indDeps, new DisplaySettings(getVisitProxy())), "2FF+3; 2FF-2; 3SS+3; 4SF");
        ColumnValue.DEPENDENCY.setValue(bean6, value.toString(), pr);
        Set<Dependency> deps = bean6.getToDependency();
        assertEquals(deps.size(), 3);
        assertTrue(bean.getFromDependency().isEmpty());
        assertEquals(bean2.getFromDependency().size(), 1);
        assertEquals(bean3.getFromDependency().size(), 1);
        assertEquals(bean4.getFromDependency().size(), 1);
        assertEquals(bean2.getFromDependency().iterator().next(), new Dependency(bean2, bean6,
                RelationshipType.FF_DEPENDENCY, -2));
        assertEquals(bean3.getFromDependency().iterator().next(), new Dependency(bean3, bean6,
                RelationshipType.SS_DEPENDENCY, 3));
        assertEquals(bean4.getFromDependency().iterator().next(), new Dependency(bean4, bean6,
                RelationshipType.SF_DEPENDENCY, 0));
    }

    @Test
    public void dependencyParser() {
        DependencyColumn c = (DependencyColumn) ColumnValue.DEPENDENCY;
        Collection<IndexedDependency> deps = c.parse("1");
        assertEquals(1, deps.size());
        IndexedDependency dep = deps.iterator().next();
        assertEquals(1, dep.getIndex().intValue());
        assertEquals(RelationshipType.FS_DEPENDENCY, dep.getType());
        assertEquals(0, dep.getLag());
        deps = c.parse("1; 2");
        assertEquals(2, deps.size());
        Iterator<IndexedDependency> iter = deps.iterator();
        dep = iter.next();
        assertEquals(1, dep.getIndex().intValue());
        assertEquals(RelationshipType.FS_DEPENDENCY, dep.getType());
        assertEquals(0, dep.getLag());
        dep = iter.next();
        assertEquals(2, dep.getIndex().intValue());
        assertEquals(RelationshipType.FS_DEPENDENCY, dep.getType());
        assertEquals(0, dep.getLag());
        deps = c.parse("1ss; 2fs; 3sf; 4ff");
        assertEquals(4, deps.size());
        iter = deps.iterator();
        dep = iter.next();
        assertEquals(1, dep.getIndex().intValue());
        assertEquals(RelationshipType.SS_DEPENDENCY, dep.getType());
        assertEquals(0, dep.getLag());
        dep = iter.next();
        assertEquals(2, dep.getIndex().intValue());
        assertEquals(RelationshipType.FS_DEPENDENCY, dep.getType());
        assertEquals(0, dep.getLag());
        dep = iter.next();
        assertEquals(3, dep.getIndex().intValue());
        assertEquals(RelationshipType.SF_DEPENDENCY, dep.getType());
        assertEquals(0, dep.getLag());
        dep = iter.next();
        assertEquals(4, dep.getIndex().intValue());
        assertEquals(RelationshipType.FF_DEPENDENCY, dep.getType());
        assertEquals(0, dep.getLag());
        deps = c.parse("1FS-2; 2SS+3; 3SF-4; 4FF+5");
        assertEquals(4, deps.size());
        iter = deps.iterator();
        dep = iter.next();
        assertEquals(1, dep.getIndex().intValue());
        assertEquals(RelationshipType.FS_DEPENDENCY, dep.getType());
        assertEquals(-2, dep.getLag());
        dep = iter.next();
        assertEquals(2, dep.getIndex().intValue());
        assertEquals(RelationshipType.SS_DEPENDENCY, dep.getType());
        assertEquals(3, dep.getLag());
        dep = iter.next();
        assertEquals(3, dep.getIndex().intValue());
        assertEquals(RelationshipType.SF_DEPENDENCY, dep.getType());
        assertEquals(-4, dep.getLag());
        dep = iter.next();
        assertEquals(4, dep.getIndex().intValue());
        assertEquals(RelationshipType.FF_DEPENDENCY, dep.getType());
        assertEquals(5, dep.getLag());
    }

    @Test
    public void documentHandler() {
        // check sequence
        Work work = WBSTestUtils.createWork(WBSTestHelper.getWorkName(), getUser(), null);
        UrlDocument document = WBSTestUtils.createDocument(WBSTestHelper.DOC_NAME, getUser());
        document.linkToWork(work);
        document.save();
        List<?> documents = work.getDocuments();
        assertTrue(!documents.isEmpty());
        Document doc = (Document) documents.get(0);
        assertEquals(document, doc);
        DocumentRelationship dr = (DocumentRelationship) work.getRelationships().getRelationship(work.getId(),
                doc.getId(), RelationshipType.OBJECT_DOCUMENT);
        DocumentBean bean = (DocumentBean) BeanHandler.createBean(dr, getVisitProxy());
        int seq = bean.getSequence() + 1;
        WorkBean workBean = (WorkBean) BeanHandler.createBean(work, getVisitProxy());
        BeansTree tree = new BeansTree(workBean);
        tree.addChild(workBean, bean);
        bean.setSequence(seq);
        BeanHandler.saveBean(bean, getVisitProxy());
        // remove from cache
        doc.setModified();
        dr.setModified();
        dr = (DocumentRelationship) work.getRelationships().getRelationship(work.getId(), doc.getId(),
                RelationshipType.OBJECT_DOCUMENT);
        bean = (DocumentBean) BeanHandler.createBean(dr, getVisitProxy());
        assertEquals(bean.getSequence(), seq);
    }

    /**
     * Testing functionality of enable/disable flags
     */
    @Test
    public void enableDisable() {
        WBSBean bean = new WBSBean(Uuid.toUuid("1"));
        bean.initialize();
        bean.setName("A1");
        assertTrue(bean.isEnabled(Property.NAME));
        assertTrue(!bean.isEnabledStateModified(Property.NAME));
        assertTrue(bean.isModified(Property.NAME));
        bean.setEnabled(Property.NAME, true);
        assertTrue(bean.isEnabled(Property.NAME));
        assertTrue(!bean.isEnabledStateModified(Property.NAME));
        assertTrue(bean.isModified(Property.NAME));
        bean.setEnabled(Property.NAME, false);
        assertTrue(!bean.isEnabled(Property.NAME));
        assertTrue(bean.isEnabledStateModified(Property.NAME));
        assertTrue(bean.isModified(Property.NAME));
    }

    /**
     * Test changing parent of bean
     */
    @Test
    public void hierarchyChange() {
        String n1 = WBSTestHelper.getWorkName() + Uuid.create().toString();
        String n2 = WBSTestHelper.getWorkName() + Uuid.create().toString();
        Work w1 = WBSTestUtils.createWork(n1, getUser(), null);
        Work w2 = WBSTestUtils.createWork(n2, getUser(), null);
        WBSBean b1 = BeanHandler.createBean(w1, getVisitProxy());
        WBSBean b2 = BeanHandler.createBean(w2, getVisitProxy());
        BeansTree tree = new BeansTree(b1);
        tree.addChild(b1, b2);
        BeanHandler.updateParent(b2, getVisitProxy());
        w2 = (Work) PSObject.get(w2.getId(), getContext());
        boolean isParent = w1.getId().equals(w2.getParentId());
        w2.deleteHard();
        w1.deleteHard();
        assertTrue(isParent);
    }

    /**
     * Tests moving of beans
     */
    @Test
    public void move() {
        WBSBean bean = new WorkBean(Uuid.create());
        BeansTree tree = new BeansTree(bean);
        WorkBean bean2 = new WorkBean(Uuid.create());
        bean2.setName("A2");
        WorkBean bean3 = new WorkBean(Uuid.create());
        bean3.setName("A3");
        WBSBean bean4 = new WorkBean(Uuid.create());
        bean4.setName("A4");
        WBSBean bean5 = new WorkBean(Uuid.create());
        bean5.setName("A5");
        WBSBean bean6 = new WBSBean();
        bean6.setName("A6");
        tree.addChild(bean, bean2);
        tree.addChild(bean, bean3);
        tree.addChild(bean, bean4);
        tree.addChild(bean3, bean5);
        tree.addChild(bean3, bean6);
        String testHierarchy = "A2," + "A3," + " A5," + " A6," + "A4";
        assertChildHierarchy(bean, testHierarchy);
        tree.move(bean6, bean, 0);
        testHierarchy = "A6," + "A2," + "A3," + " A5," + "A4";
        assertChildHierarchy(bean, testHierarchy);
        tree.move(bean6, bean, bean.getChildrenCount());
        testHierarchy = "A2," + "A3," + " A5," + "A4," + "A6";
        assertChildHierarchy(bean, testHierarchy);
        tree.move(bean6, bean, 2);
        testHierarchy = "A2," + "A3," + " A5," + "A6," + "A4";
        assertChildHierarchy(bean, testHierarchy);
        WBSBean bean7 = new WBSBean();
        bean7.setName("A7");
        tree.addChild(bean3, bean7, 0);
        testHierarchy = "A2," + "A3," + " A7," + " A5," + "A6," + "A4";
        assertChildHierarchy(bean, testHierarchy);
    }

    @Test
    public void resourceHandler() {
        Work work = WBSTestUtils.createWork(WBSTestHelper.getWorkName(), getUser(), null);
        Need need = WBSTestUtils.createNeed(work, getUser());
        ResourcePool pool = WBSTestUtils.createResourcePool(WBSTestHelper.POOL_NAME, getUser());
        need.setAllocation(25.5f);
        need.setEffort(33.3f);
        need.setPool(pool);
        need.setAssignedUser(getUser(), getUser());
        ResourceBean bean = (ResourceBean) BeanHandler.createBean(need, getVisitProxy());
        assertEquals(bean.getName(), null);
        assertEquals(bean.getAllocation(), 25.5f, 0.001f);
        assertEquals(bean.getEffort(), 33.3f, 0.001f);
        assertEquals(bean.getPool(), pool);
        assertEquals(bean.getPerson(), getUser());
    }

    @Test
    public void resourceHandler2() {
        Work work = WBSTestUtils.createWork(WBSTestHelper.getWorkName(), getUser(), null);
        Need need = WBSTestUtils.createNeed(work, getUser());
        ResourceBean res = (ResourceBean) BeanHandler.createBean(need, getVisitProxy());
        WorkBean workBean = (WorkBean) BeanHandler.createBean(work, getVisitProxy());
        res.setWork(workBean);
        BeansTree tree = new BeansTree(workBean);
        tree.addChild(workBean, res);
        Float effort = res.getEffort() == null || res.getEffort() == 100 ? 0.f : 100.f;
        res.setEffort(effort);
        BeanHandler.saveBean(res, getVisitProxy());
        WBSTestHelper.reloadDefaults();
        res = (ResourceBean) BeanHandler.createBean(WBSTestHelper.getNeed(), getVisitProxy());
        res.getEffort().equals(effort);
    }

    @Test
    public void saveStates() {
        ProjectEditor editor = new ProjectEditor(getVisitProxy());
        Work work = WBSTestUtils.createWork(WBSTestHelper.getWorkName(), getUser(), null);
        editor.loadTree(work);
        WorkBean root = (WorkBean) editor.getTree().getRoot();
        assertTrue(root.isSaved(Property.NAME));
        WBSBean bean = editor.getTree().getRoot();
        BeansTree tree = new BeansTree(bean);
        WorkBean bean2 = new TestWorkBean("bean2", Uuid.create());
        WorkBean bean3 = new TestWorkBean("bean3", Uuid.create());
        WorkBean bean4 = new TestWorkBean("bean4", Uuid.create());
        tree.addChild(bean, bean2);
        tree.addChild(bean, bean3);
        tree.addChild(bean, bean4);
        tree.addDependency(new Dependency(bean2, bean3, RelationshipType.FS_DEPENDENCY, 0));
        tree.clearBeansModifications();
        assertTrue(bean2.isSaved(Property.PLANNED_START_DATE));
        assertTrue(bean2.isSaved(Property.PLANNED_END_DATE));
        DataAccessor.CONSTRAINT.set(bean2, Constraint.MFO, editor);
        // if planned end date was null, then default value is set
        assertTrue(bean2.isSaved(Property.PLANNED_START_DATE));
        assertTrue(!bean2.isSaved(Property.PLANNED_END_DATE));
        bean2.setSaved(Property.PLANNED_END_DATE, true);
        // only RO state is changed
        bean2.setPlannedEndDate(new Timestamp(1234));
        assertTrue(bean2.isSaved(Property.PLANNED_START_DATE));
        assertTrue(!bean2.isSaved(Property.PLANNED_END_DATE));
        bean2.setSaved(Property.PLANNED_END_DATE, true);
        DataAccessor.CONSTRAINT.set(bean2, Constraint.MSO, editor);
        assertTrue(!bean2.isSaved(Property.PLANNED_START_DATE));
        assertTrue(!bean2.isSaved(Property.PLANNED_END_DATE));
    }

    /**
     * Test sequence sorting and assignment
     */
    @Test
    public void sequences() {
        WBSBean bean = new TestWorkBean("", Uuid.create());
        BeansTree tree = new BeansTree(bean);
        bean.setName("A1");
        WorkBean bean2 = new TestWorkBean("A2", Uuid.create());
        bean2.setSequence(100);
        WorkBean bean3 = new TestWorkBean("A3", Uuid.create());
        bean3.setSequence(100);
        WBSBean bean4 = new TestWorkBean("A4", Uuid.create());
        bean4.setSequence(100);
        WBSBean bean5 = new TestWorkBean("A5", Uuid.create());
        bean5.setSequence(100);
        WBSBean bean6 = new WBSBean();
        bean6.setSequence(100);
        bean6.setName("A6");
        bean6.initialize();
        // only A1 is attached to tree
        assertEquals(tree.getChangedBeans().size(), 1);
        tree.addChild(bean, bean2);
        tree.addChild(bean, bean3);
        tree.addChild(bean, bean4);
        tree.addChild(bean, bean5);
        tree.addChild(bean, bean6);
        bean2.setSequence(1);
        tree.clearBeansModifications();
        WBSTestHelper.reorderBySequence(bean);
        assertTrue(tree.getChangedBeans().isEmpty());
        WBSTestHelper.resequenceIfNecessary(bean);
        assertTrue(tree.getChangedBeans().isEmpty());
        bean5.setSequence(1);
        tree.clearBeansModifications();
        WBSTestHelper.reorderBySequence(bean);
        assertTrue(tree.getChangedBeans().isEmpty());
        WBSTestHelper.resequenceIfNecessary(bean);
        assertTrue(tree.getChangedBeans().isEmpty());
        bean5.setSequence(7);
        bean3.setSequence(30);
        tree.clearBeansModifications();
        WBSTestHelper.reorderBySequence(bean);
        assertTrue(tree.getChangedBeans().isEmpty());
        WBSTestHelper.resequenceIfNecessary(bean);
        assertTrue(tree.getChangedBeans().isEmpty());
        List<WBSBean> beans = bean.getChildren();
        assertEquals(beans.get(0), bean2);
        assertEquals(beans.get(1), bean5);
        assertEquals(beans.get(2), bean3);
        assertEquals(bean2.getSequence(), 1);
        assertEquals(bean5.getSequence(), 7);
        assertEquals(bean3.getSequence(), 30);
        assertEquals(bean4.getSequence(), 100);
        assertEquals(bean6.getSequence(), 100);
        bean5.setSequence(50);
        tree.clearBeansModifications();
        WBSTestHelper.resequenceIfNecessary(bean);
        assertEquals(tree.getChangedBeans().size(), 4);
        assertEquals(bean2.getSequence(), 1);
        assertEquals(bean5.getSequence(), 2);
        assertEquals(bean3.getSequence(), 3);
        assertEquals(bean4.getSequence(), 4);
        assertEquals(bean6.getSequence(), 5);
    }

    @Test
    public void sequences2() {
        WBSBean bean = new TestWorkBean("", Uuid.create());
        BeansTree tree = new BeansTree(bean);
        bean.setName("A1");
        WorkBean bean2 = new TestWorkBean("A2", Uuid.create());
        bean2.setSequence(100);
        WorkBean bean3 = new TestWorkBean("A3", Uuid.create());
        bean3.setSequence(100);
        WBSBean bean4 = new TestWorkBean("A4", Uuid.create());
        bean4.setSequence(100);
        WBSBean bean5 = new TestWorkBean("A5", Uuid.create());
        bean5.setSequence(100);
        WBSBean bean6 = new WBSBean();
        bean6.setSequence(100);
        bean6.setName("A6");
        bean6.initialize();
        // only A1 is attached to tree
        assertEquals(tree.getChangedBeans().size(), 1);
        tree.addChild(bean, bean2);
        tree.addChild(bean, bean3);
        tree.addChild(bean, bean4);
        tree.addChild(bean, bean5);
        tree.addChild(bean, bean6);
        bean2.setSequence(1);
        tree.clearBeansModifications();
        WBSTestHelper.reorderBySequence(bean);
        assertTrue(tree.getChangedBeans().isEmpty());
        WBSTestHelper.resequenceIfNecessary(bean);
        assertTrue(tree.getChangedBeans().isEmpty());
        bean5.setSequence(1);
        tree.clearBeansModifications();
        WBSTestHelper.reorderBySequence(bean);
        assertTrue(tree.getChangedBeans().isEmpty());
        WBSTestHelper.resequenceIfNecessary(bean);
        assertTrue(tree.getChangedBeans().isEmpty());
        bean5.setSequence(7);
        bean3.setSequence(30);
        tree.clearBeansModifications();
        WBSTestHelper.reorderBySequence(bean);
        assertTrue(tree.getChangedBeans().isEmpty());
        WBSTestHelper.resequenceIfNecessary(bean);
        assertTrue(tree.getChangedBeans().isEmpty());
        List<WBSBean> beans = bean.getChildren();
        assertEquals(beans.get(0), bean2);
        assertEquals(beans.get(1), bean5);
        assertEquals(beans.get(2), bean3);
        assertEquals(bean2.getSequence(), 1);
        assertEquals(bean5.getSequence(), 7);
        assertEquals(bean3.getSequence(), 30);
        assertEquals(bean4.getSequence(), 100);
        assertEquals(bean6.getSequence(), 100);
        bean5.setSequence(50);
        tree.clearBeansModifications();
        WBSTestHelper.resequenceIfNecessary(bean);
        assertEquals(tree.getChangedBeans().size(), 4);
        assertEquals(bean2.getSequence(), 1);
        assertEquals(bean5.getSequence(), 2);
        assertEquals(bean3.getSequence(), 3);
        assertEquals(bean4.getSequence(), 4);
        assertEquals(bean6.getSequence(), 5);
    }

    /**
     * Tests tree's functionality
     */
    @Test
    public void tree() {
        WorkBean bean = new TestWorkBean("", Uuid.create());
        BeansTree tree = new BeansTree(bean);
        bean.setName("A1");
        WorkBean bean2 = new TestWorkBean("A2", Uuid.create());
        WorkBean bean3 = new TestWorkBean("A3", Uuid.create());
        WBSBean bean4 = new TestWorkBean("A4", Uuid.create());
        WorkBean bean5 = new TestWorkBean("A5", Uuid.create());
        WorkBean bean6 = new TestWorkBean("A6", Uuid.create());
        // only A1 is attached to tree
        assertEquals(tree.getChangedBeans().size(), 1);
        tree.addChild(bean, bean2);
        tree.addChild(bean, bean3);
        tree.addChild(bean2, bean4);
        tree.addChild(bean4, bean5);
        tree.addChild(bean2, bean6);
        String testHierarchy = "A2," + " A4," + "  A5," + " A6," + "A3";
        assertChildHierarchy(bean, testHierarchy);
        ResourceSplitBean rb1 = new ResourceSplitBean();
        ResourceSplitBean rb2 = new ResourceSplitBean();
        ResourceSplitBean rb3 = new ResourceSplitBean();
        ResourceSplitBean rb4 = new ResourceSplitBean();
        bean5.addResource(rb1);
        bean5.addResource(rb2);
        bean6.addResource(rb3);
        bean.addResource(rb4);
        // A1
        // A2
        // A4
        // A5
        // A6
        // A3 -> Dependent on A2
        // no changes were made
        assertEquals(tree.getChangedBeans().size(), 1);
        // bean3 updated "to" dependency
        tree.addDependency(new Dependency(bean2, bean3, RelationshipType.FS_DEPENDENCY, 0));
        // To and From were updated
        assertEquals(tree.getChangedBeans().size(), 3);
        assertEquals(bean3.getToDependency().size(), 1);
        assertEquals(bean2.getFromDependency().size(), 1);
        assertTrue(bean3.getToDependency().iterator().next().isInitialized());
        assertEquals(bean2.getFromDependency().iterator().next().getTo(), bean3);
        assertEquals(bean3.getToDependency().iterator().next().getFrom(), bean2);
        // A3 was changed but it is already stored
        bean3.setName("A3_");
        bean3.setName("A3");
        bean2.setName("A2_");
        assertEquals(tree.getChangedBeans().size(), 3);
        tree.clearBeansModifications();
        bean2.setName("A2_");
        assertEquals(tree.getChangedBeans().size(), 0);
        bean2.setName("A2");
        assertEquals(tree.getChangedBeans().size(), 1);
        {
            List<WBSBean> children = bean5.getChildren();
            assertEquals(children.size(), 0);
        }
        {
            List<WBSBean> children = beanIteratorToList(bean5.getChildrenIterator(true));
            assertEquals(children.size(), 2);
            assertEquals(children.get(0), rb1);
            assertEquals(children.get(1), rb2);
        }
        {
            List<WBSBean> children = beanIteratorToList(tree.getChildrenIterator(bean, 0));
            assertEquals(children.size(), 0);
        }
        {
            List<WBSBean> children = beanIteratorToList(tree.getChildrenIterator(bean, 1));
            assertEquals(children.size(), 3);
            assertBean(children.get(0), "A2");
            assertBean(children.get(1), "A3");
            assertEquals(children.get(2), rb4);
        }
        {
            List<WBSBean> children = beanIteratorToList(tree.getChildrenIterator(bean, true, 1));
            assertEquals(children.size(), 4);
            assertBean(children.get(0), "A1");
            assertBean(children.get(1), "A2");
            assertBean(children.get(2), "A3");
            assertEquals(children.get(3), rb4);
        }
        {
            List<WBSBean> children = beanIteratorToList(tree.getChildrenIterator(bean, 2));
            assertEquals(children.size(), 5);
            assertBean(children.get(0), "A2");
            assertBean(children.get(1), "A4");
            assertBean(children.get(2), "A6");
            assertBean(children.get(3), "A3");
            assertEquals(children.get(4), rb4);
        }
        bean.deleteResource(rb4);
        {
            List<WBSBean> children = beanIteratorToList(tree.getChildrenIterator(bean, 3));
            assertEquals(children.size(), 6);
            assertBean(children.get(0), "A2");
            assertBean(children.get(1), "A4");
            assertBean(children.get(2), "A5");
            assertBean(children.get(3), "A6");
            assertEquals(children.get(4), rb3);
            assertBean(children.get(5), "A3");
        }
        {
            List<WBSBean> children = beanIteratorToList(tree.getChildrenIterator(bean));
            assertEquals(children.size(), 8);
            assertBean(children.get(0), "A2");
            assertBean(children.get(1), "A4");
            assertBean(children.get(2), "A5");
            assertEquals(children.get(3), rb1);
            assertEquals(children.get(4), rb2);
            assertBean(children.get(5), "A6");
            assertEquals(children.get(6), rb3);
            assertBean(children.get(7), "A3");
        }
        tree.clearBeansModifications();
        tree.reorder(0);
        assertEquals(bean.getNumber(), 1);
        assertTrue(bean.isSaved(Property.NUMBER));
        assertEquals(bean2.getNumber(), 0);
        bean.clearModified();
        assertTrue(bean.isSaved(Property.NUMBER));
        assertEquals(tree.getChangedBeans().size(), 1);
        tree.reorder(1);
        assertEquals(bean.getNumber(), 1);
        assertEquals(bean2.getNumber(), 2);
        assertEquals(bean3.getNumber(), 3);
        assertTrue(bean.isSaved(Property.NUMBER));
        assertTrue(bean2.isSaved(Property.NUMBER));
        assertTrue(bean3.isSaved(Property.NUMBER));
        assertEquals(tree.getChangedBeans().size(), 3);
        tree.clearBeansModifications();
        tree.reorder(5);
        assertTrue(bean2.isModified(Property.DEPENDENCY_NUMBER)); // due to dependency to A3
        assertEquals(bean.getNumber(), 1);
        assertEquals(bean2.getNumber(), 2);
        assertEquals(bean3.getNumber(), 6);
        assertEquals(bean4.getNumber(), 3);
        assertEquals(bean5.getNumber(), 4);
        assertEquals(bean6.getNumber(), 5);
        assertEquals(tree.getChangedBeans().size(), 5);
    }

    /**
     * Test parent-child validation functionality
     */
    @Test
    public void treeValidation() {
        Class<?> allBeans[] = new Class[] { WBSBean.class, WorkBean.class, ResourceBean.class, DocumentBean.class,
                GPBean.class, CheckpointBean.class, TextMessageBean.class };
        Object[][] types = new Object[][] {
                { WBSBean.class, new Class[] {} },
                {
                        WorkBean.class,
                        new Class[] { WBSBean.class, WorkBean.class, ResourceBean.class, DocumentBean.class,
                                GPBean.class } },
                { GPBean.class, new Class[] { CheckpointBean.class, ResourceBean.class } },
                {
                        CheckpointBean.class,
                        new Class[] { WBSBean.class, WorkBean.class, ResourceBean.class, DocumentBean.class,
                                GPBean.class, TextMessageBean.class } }, { DocumentBean.class, new Class[] {} },
                { ResourceBean.class, new Class[] {} }, { TextMessageBean.class, new Class[] {} }, };
        for (Object o : types) {
            Object[] params = (Object[]) o;
            final WBSBean parent = (WBSBean) WBSTestHelper.createTestData((Class<?>) params[0], getVisitProxy());
            for (Class<?> childClass : allBeans) {
                final WBSBean child = (WBSBean) WBSTestHelper.createTestData(childClass, getVisitProxy());
                final BeansTree tree = new BeansTree(parent);
                boolean throwEx = !ValidationHelp.ValidateParentChildRelation.isSupportedType(parent.getClass(),
                        child.getClass());
                @SuppressWarnings("unused")
                AssertException assertException = new AssertException(throwEx) {

                    @Override
                    public void invoke() {
                        tree.addChild(parent, child);
                    }

                    @Override
                    public void reportError(String s) {
                        fail(s + ": " + parent.getClass().getName() + ": " + child.getClass().getName());
                    }
                };
            }
        }
        final WBSBean work = new WorkBean(Uuid.create());
        final WBSBean work2 = new WorkBean(Uuid.create());
        final BeansTree tree = new BeansTree(work);
        @SuppressWarnings("unused")
        AssertException assertException = new AssertException(true) {

            @Override
            public void invoke() {
                tree.addChild(work, work2);
                tree.addChild(work, work2);
            }
        };
    }

    @Test
    public void workHandler() {
        Work work = WBSTestUtils.createWork(WBSTestHelper.getWorkName(), getUser(), null);
        Work parent = WBSTestUtils.createWork("Test-Parent", getUser(), null);
        Timestamp date1 = CalculatorManager.getWorkCalculator(getContext(),
                (WorkBean) BeanHandler.createBean(work, getVisitProxy())).toDayBoundary(new Timestamp(1));
        Timestamp date2 = CalculatorManager.getWorkCalculator(getContext(),
                (WorkBean) BeanHandler.createBean(work, getVisitProxy())).toDayBoundary(new Timestamp(2));
        Timestamp date3 = CalculatorManager.getWorkCalculator(getContext(),
                (WorkBean) BeanHandler.createBean(work, getVisitProxy())).toDayBoundary(new Timestamp(3));
        Timestamp date4 = CalculatorManager.getWorkCalculator(getContext(),
                (WorkBean) BeanHandler.createBean(work, getVisitProxy())).toDayBoundary(new Timestamp(4));
        Timestamp date5 = CalculatorManager.getWorkCalculator(getContext(),
                (WorkBean) BeanHandler.createBean(work, getVisitProxy())).toDayBoundary(new Timestamp(5));
        Timestamp date6 = CalculatorManager.getWorkCalculator(getContext(),
                (WorkBean) BeanHandler.createBean(work, getVisitProxy())).toDayBoundary(new Timestamp(6));
        Duration dur1 = Duration.getDaysDuration(1);
        Duration dur2 = Duration.getDaysDuration(2);
        work.setOwnerId(getUser().getId());
        work.getSchedules().setActualStartDate(date1);
        work.getSchedules().setActualEndDate(date2);
        work.getSchedules().setPlannedEndDate(date3);
        work.getSchedules().setPlannedStartDate(date4);
        work.getSchedules().setPlannedLaborTime(dur1);
        work.getSchedules().setSystemEndDate(date5);
        work.getSchedules().setSystemStartDate(date6);
        work.getSchedules().setSystemLaborTime(dur2);
        work.setParentWork(parent, getUser());
        work.setStatus(WorkStatus.DEFERRED);
        work.setPercentComplete(23);
        work.setAssignedPriority(2);
        work.setSequenceWithinParent(getUser(), 37);
        work.setInheritControls(false);
        work.setControlCost(true);
        work.setInheritCalendar(true);
        work.setInheritPermissions(true);
        work.setManualScheduling(true);
        WorkBean bean = (WorkBean) BeanHandler.createBean(work, getVisitProxy());
        assertEquals(getUser(), bean.getOwner());
        assertEquals(date1, bean.getActualStartDate());
        assertEquals(date2, bean.getActualEndDate());
        assertEquals(date3, bean.getPlannedEndDate());
        assertEquals(date4, bean.getPlannedStartDate());
        assertEquals(dur1, bean.getPlannedLaborTime());
        assertEquals(date5, bean.getSystemEndDate());
        assertEquals(date6, bean.getSystemStartDate());
        assertEquals(dur2, bean.getSystemLaborTime());
        assertEquals(23, bean.getPercentComplete());
        assertEquals(2, bean.getPriority());
        assertEquals(37, bean.getSequence());
        assertEquals(WorkStatus.DEFERRED, work.getStatus());
        assertTrue(Boolean.TRUE.equals(bean.getProjectControl(ProjectControl.CONTROL_COSTS)));
        assertTrue(Boolean.TRUE.equals(bean.getProjectControl(ProjectControl.INHERIT_CALENDAR)));
        assertTrue(!Boolean.TRUE.equals(bean.getProjectControl(ProjectControl.INHERIT_CONTROLS)));
        assertTrue(Boolean.TRUE.equals(bean.getProjectControl(ProjectControl.INHERIT_PERMISSIONS)));
        assertTrue(Boolean.TRUE.equals(bean.getProjectControl(ProjectControl.MANUAL_SCHEDULING)));
    }

    private List<WBSBean> beanIteratorToList(Iterator<? extends WBSBean> it) {
        List<WBSBean> list = new ArrayList<WBSBean>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }
}