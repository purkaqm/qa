package ps5.wbs;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.AssertionFailedError;
import ps5.psapi.project.create.WorkTypeBean;
import ps5.services.state.Visit;
import ps5.wbs.beans.BeansTree;
import ps5.wbs.beans.CheckpointBean;
import ps5.wbs.beans.CustomFieldBean;
import ps5.wbs.beans.Dependency;
import ps5.wbs.beans.DocumentBean;
import ps5.wbs.beans.GPBean;
import ps5.wbs.beans.IndexedDependency;
import ps5.wbs.beans.ResourceBean;
import ps5.wbs.beans.RoleAdapterBean;
import ps5.wbs.beans.TextMessageBean;
import ps5.wbs.beans.WBSBean;
import ps5.wbs.beans.WorkBean;
import ps5.wbs.logic.CalculatorManager;
import ps5.wbs.logic.OwnerDataAccessor.OwnerColumnBean;
import ps5.wbs.logic.ProjectEditor;
import ps5.wbs.logic.handlers.BeanHandler;
import ps5.wbs.logic.handlers.VisitProxy;
import ps5.wbs.logic.validation.ValidationError;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.database.StringPersistentKey;
import com.cinteractive.database.Uuid;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.documents.Document;
import com.cinteractive.ps3.documents.UrlDocument;
import com.cinteractive.ps3.entities.Group;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.ResourcePool;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.mockups.CustomField;
import com.cinteractive.ps3.relationships.HierarchyUtils;
import com.cinteractive.ps3.relationships.RelationshipType;
import com.cinteractive.ps3.resources.Need;
import com.cinteractive.ps3.scheduler.Constraint;
import com.cinteractive.ps3.scheduler.calendar.DateCalculator;
import com.cinteractive.ps3.tags.PSTag;
import com.cinteractive.ps3.tags.TagSet;
import com.cinteractive.ps3.tollgate.Tollgate;
import com.cinteractive.ps3.types.PSType;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkStatus;
import com.cinteractive.ps3.work.WorkUtil.SortOrder;
import com.cinteractive.scheduler.Duration;
import com.cinteractive.search.comparator.LinkedComparator;
import com.cinteractive.util.Log;

@SuppressWarnings("javadoc")
public class WBSTestHelper {

    public abstract class DependencySet implements Set<Object> {
    }

    public abstract class IndexedDependencySet implements Set<Object> {
    }

    public abstract class SetOfTags implements Set<Object> {
    }

    public abstract class UserSet implements Set<Object> {
    }

    public final static String DOC_NAME = "WWW.UNIT_TEST_URLS.test";

    public final static String OWNER_MAIL = "UNIT_TEST_OWNER@test.te";

    public final static String POOL_NAME = "UNIT_TEST_POOL";

    private final static String DEFAULT_TOLLGATE_NAME = "UNIT_TEST_DEFAULT_TOLLGATE_";

    private final static String DEFAULT_WORK_NAME = "UNIT_TEST_DEFAULT_WORK_1";

    private static UrlDocument defaultDocument;

    private static Group[] defaultGroup;

    private static Need defaultNeed;

    private static ResourcePool defaultResourcePool;

    private static TagSet[] defaultTags;

    private static Tollgate defaultTollgate;

    private static User[] defaultUsers;

    private static Work defaultWork;

    // perfomance optimization
    private static int intData = 0;

    private static final Log LOG = new Log(WBSTest.class);

    private static VisitProxy visit;

    private final static String WORK_NAME = "UNIT_TEST_WORK";

    public static WBSBean createBean(Class<WBSBean> cl) {
        WBSBean bean = null;
        if (cl.equals(WBSBean.class)) {
            bean = new WBSBean(Uuid.create());
        } else if (cl.equals(WorkBean.class)) {
            bean = new WorkBean(Uuid.create());
        } else if (cl.equals(GPBean.class)) {
            bean = new GPBean(Uuid.create());
        } else if (cl.equals(ResourceBean.class)) {
            bean = new ResourceBean(Uuid.create());
        }
        if (bean != null) {
            bean.initialize();
            return bean;
        }
        throw new IllegalArgumentException("Unexpected class");
    }

    public static Object createTestData(Class<?> dataType, VisitProxy v) {
        return createTestData(null, dataType, v);
    }

    public static Object createTestData(WBSBean bean, Class<?> dataType, VisitProxy v) {
        InstallationContext context = v.getContext();
        if (Boolean.class.isAssignableFrom(dataType)) {
            return intData++ % 2 == 0;
        }
        if (Integer.class.isAssignableFrom(dataType)) {
            return new Integer(intData++ % 100);
        }
        if (String.class.isAssignableFrom(dataType)) {
            return "TestString" + intData++;
        }
        if (PersistentKey.class.isAssignableFrom(dataType)) {
            return Uuid.get("" + intData++);
        }
        if (ResourceBean.class.isAssignableFrom(dataType)) {
            return new ResourceBean(Uuid.get("" + intData++));
        }
        if (DocumentBean.class.isAssignableFrom(dataType)) {
            return new DocumentBean(null, Uuid.get("" + intData++));
        }
        if (CheckpointBean.class.isAssignableFrom(dataType)) {
            return new CheckpointBean(Uuid.get("" + intData++));
        }
        if (GPBean.class.isAssignableFrom(dataType)) {
            Tollgate tg = WBSTestHelper.getTollgate();
            ProjectEditor pe = new ProjectEditor(getVisit());
            pe.loadTree(tg);
            return pe.getTree().getRoot();
        }
        if (WorkBean.class.isAssignableFrom(dataType)) {
            return new WorkBean(Uuid.get("" + intData++));
        }
        if (TextMessageBean.class.isAssignableFrom(dataType)) {
            return new TextMessageBean(Uuid.get("" + intData++));
        }
        if (WBSBean.class.isAssignableFrom(dataType)) {
            return new WBSBean();
        }
        if (Timestamp.class.isAssignableFrom(dataType)) {
            long startDate = 946684800; // 01.01.2000
            long oneDay = 86400; // 1 day
            long date = startDate + oneDay * intData++;
            Timestamp ts = new Timestamp(date * 1000);
            if (bean instanceof WorkBean) {
                DateCalculator dc = CalculatorManager.getWorkCalculator(getContext(), (WorkBean) bean);
                ts = dc.toDayBoundary(ts);
            }
            return ts;
        }
        if (Duration.class.isAssignableFrom(dataType)) {
            return Duration.getDaysDuration(intData++);
        }
        if (SortOrder.class.isAssignableFrom(dataType)) {
            return SortOrder.values()[intData++ % SortOrder.values().length];
        }
        if (Float.class.isAssignableFrom(dataType)) {
            return new Float(intData++);
        }
        if (DependencySet.class.isAssignableFrom(dataType)) {
            Set<Dependency> test = new HashSet<Dependency>();
            WorkBean b1 = new WorkBean(Uuid.create());
            WorkBean b2 = bean instanceof WorkBean ? (WorkBean) bean : new WorkBean(Uuid.create());
            for (int i = 0; i < 2; ++i) {
                test.add(new Dependency(b1, b2, RelationshipType.FF_DEPENDENCY, 0));
            }
            return test;
        }
        if (IndexedDependencySet.class.isAssignableFrom(dataType)) {
            Set<IndexedDependency> test = new HashSet<IndexedDependency>();
            for (int i = 0; i < 1; ++i) {
                test.add(new IndexedDependency(intData++ % 2 + 1, RelationshipType.FF_DEPENDENCY, 2));
            }
            return test;
        }
        if (UserSet.class.isAssignableFrom(dataType)) {
            Set<User> test = new HashSet<User>();
            for (int i = 0; i < 2; ++i) {
                test.add(getUser(intData++));
            }
            return test;
        }
        if (SetOfTags.class.isAssignableFrom(dataType)) {
            return getTags(intData++);
        }
        if (User.class.isAssignableFrom(dataType)) {
            return getUser(intData++);
        }
        if (OwnerColumnBean.class.isAssignableFrom(dataType)) {
            return new OwnerColumnBean(getUser(intData++), null);
        }
        if (WorkStatus.class.isAssignableFrom(dataType)) {
            return WorkStatus.values()[intData++ % WorkStatus.values().length];
        }
        if (Constraint.class.isAssignableFrom(dataType)) {
            return Constraint.values()[intData++ % Constraint.values().length];
        }
        if (Group.class.isAssignableFrom(dataType)) {
            return getGroup(intData++);
        }
        if (RoleAdapterBean.class.isAssignableFrom(dataType)) {
            return new RoleAdapterBean(new StringPersistentKey("SOME ID"));
        }
        if (CustomField.class.isAssignableFrom(dataType)) {
            User user = getUser(2);
            List<CustomField> fields = new ArrayList<CustomField>(CustomField.getFor(context, user.getPSType()));
            return fields.isEmpty() ? null : new CustomFieldBean(fields.get(intData++ % fields.size()), user);
        }
        if (ResourcePool.class.isAssignableFrom(dataType)) {
            ResourcePool pool = ResourcePool.createNew("TestName" + intData++, Nobody.get(context), context);
            return pool;
        }
        throw new IllegalArgumentException("Unexpected data type");
    }

    public static InstallationContext getContext() {
        return getVisit().getContext();
    }

    public static Document getDocument() {
        if (defaultDocument == null) {
            try {
                defaultDocument = (UrlDocument) PSObject.getByName(UrlDocument.TYPE, DOC_NAME, false, null, null,
                        getContext()).get(0);
            } catch (Exception ex) {
                defaultDocument = WBSTestUtils.createDocument(DOC_NAME, getOwner());
            }
        }
        return defaultDocument;
    }

    public static String getEmail(int i) {
        return "TestEmail" + i + "@xxxxx.xx";
    }

    public static Group getGroup(int id) {
        return defaultGroup[id % defaultGroup.length];
    }

    public static Need getNeed() {
        if (defaultNeed == null) {
            try {
                defaultNeed = getWork().getNeeds().iterator().next();
            } catch (Exception ex) {
                defaultNeed = WBSTestUtils.createNeed(getWork(), getOwner());
            }
        }
        return defaultNeed;
    }

    public static User getOwner() {
        return getVisit().getPrincipal();
    }

    public static ResourcePool getResourcePool() {
        if (defaultResourcePool == null) {
            try {
                defaultResourcePool = (ResourcePool) PSObject.getByName(ResourcePool.TYPE, POOL_NAME, false, null,
                        null, getContext());
            } catch (Exception ex) {
            }
            if (defaultResourcePool == null) {
                defaultResourcePool = WBSTestUtils.createResourcePool(POOL_NAME, getOwner());
            }
        }
        return defaultResourcePool;
    }

    /**
     * Sets sequence of children accordingly to their position
     */
    @SuppressWarnings({ "unchecked", "serial" })
    public static Comparator<WBSBean> getSequenceComparator() {
        // NOTE should we ignore trailing nulls ?
        final boolean nullsSortHigh = false;
        final boolean ascending = true;
        return new LinkedComparator<WBSBean>() {

            @Override
            protected final int doCompare(WBSBean left, WBSBean right) {
                Integer l = left.getSequence();
                Integer r = right.getSequence();
                return compare(l, r, nullsSortHigh, ascending);
            }
        };
    }

    public static Set<PSTag> getTags(int id) {
        TagSet tagSet = getTagSet(id);
        Set<PSTag> tags = new HashSet<PSTag>();
        for (Enumeration<?> en = tagSet.getTags().getPersistents(); en.hasMoreElements();) {
            tags.add((PSTag) en.nextElement());
        }
        return tags;
    }

    public static TagSet getTagSet(int id) {
        return defaultTags[id % defaultTags.length];
    }

    public static TagSet getTagSet(String name, int numberOfTags, InstallationContext context) {
        TagSet tagSet = TagSet.createNew(name + Uuid.create().toString(), context);
        tagSet.setDescription("some descr");
        tagSet.setAllowMultipleLinks(true);
        tagSet.setIsHierarchical(true);
        tagSet.setEnableAlerts(true);
        tagSet.setIsMandatory(false);
        tagSet.setLocked(false);
        tagSet.setApplyPermissions(true);
        for (int i = 0; i < numberOfTags; ++i) {
            tagSet.addTag(name + String.format("_%2d", i));
        }
        return tagSet;
    }

    public static Class<?>[] getTestedDataTypes(boolean useIndexedDeps) {
        return new Class[] {
                Boolean.class,
                Integer.class,
                String.class,
                PersistentKey.class,
                ResourcePool.class,
                // +doc
                CheckpointBean.class,
                // GPBean.class,
                // +work
                // +textMsg
                Timestamp.class, Duration.class, Float.class, UserSet.class,
                useIndexedDeps ? IndexedDependencySet.class : DependencySet.class, SetOfTags.class, User.class,
                WorkStatus.class, Constraint.class, Group.class, CustomField.class };
    }

    /**
     * Return version of project editor working only on beans without saving data to real works
     */
    public static ProjectEditor getTestProjectEditor() {
        final ProjectEditor pe = new TestPE(getVisit());
        // WorkBean root = new TestWorkBean("Root Work Bean", new StringPersistentKey("rootId"));
        Work work = WBSTestUtils.createWork("Root Work Bean", getOwner(), null);
        WorkBean root = (WorkBean) BeanHandler.createBean(work, getVisit());
        // root.setId(new StringPersistentKey("rootId"));
        root.setSystemStartDate(new Timestamp(1));
        root.setSystemEndDate(new Timestamp(2));
        root.setStatus(WorkStatus.NOT_STARTED);
        BeansTree tree = new BeansTree(root);
        pe.loadTree(tree);
        return pe;
    }

    public static Tollgate getTollgate() {
        if (defaultTollgate == null) {
            try {
                defaultTollgate = (Tollgate) PSObject.getByName(Tollgate.TYPE, DEFAULT_TOLLGATE_NAME, false, null,
                        null, getContext()).get(0);
            } catch (Exception ex) {
                defaultTollgate = WBSTestUtils.createTollgateWithDeliverables(DEFAULT_TOLLGATE_NAME, getVisit(),
                        getWorkName("_deliv"));
            }
        }
        return defaultTollgate;
    }

    public static User getUser(int id) {
        return defaultUsers[id % defaultUsers.length];
    }

    public static VisitProxy getVisit() {
        return visit;
    }

    public static Work getWork() {
        if (defaultWork == null) {
            try {
                defaultWork = (Work) PSObject.getByName(Work.TYPE, DEFAULT_WORK_NAME, false, null, null, getContext())
                        .get(0);
                for (Object w : defaultWork.getChildren()) {
                    ((PSObject) w).deleteSoft(getOwner());
                }
                defaultWork.setUseResourcePlanning(true, getOwner());
            } catch (Exception ex) {
                Work parentWork = WBSTestUtils.createWork(DEFAULT_WORK_NAME + "Parent", getOwner(), null);
                defaultWork = WBSTestUtils.createWork(DEFAULT_WORK_NAME, getOwner(), parentWork);
            }
            getDocument().linkToWork(defaultWork);
            getDocument().save();
            defaultWork.save();
        }
        return defaultWork;
    }

    public static String getWorkName() {
        return WORK_NAME;
    }

    public static String getWorkName(String suffix) {
        return getWorkName() + suffix;
    }

    public static WorkTypeBean getWorkTypeBean(PSType type, PersistentKey templateId) {
        return new WorkTypeBeanEx(new WorkTypeBean(getVisit().getContext(), type, templateId), getVisit());
    }

    public static void init(VisitProxy visitProxy) {
        if (defaultUsers != null) {
            return;
        }
        visit = visitProxy;
        defaultUsers = new User[10];
        defaultTags = new TagSet[10];
        defaultGroup = new Group[10];
        for (int i = 0; i < defaultUsers.length; ++i) {
            defaultUsers[i] = User.createNewUser(getEmail(-i), getContext());
        }
        for (int i = 0; i < defaultTags.length; ++i) {
            defaultTags[i] = getTagSet("Test" + -i, 1, getContext());
        }
        for (int i = 0; i < defaultGroup.length; ++i) {
            Group g = Group.createNew(getEmail(-i), Nobody.get(getContext()), getContext());
            User u1 = getUser(i);
            User u2 = getUser(i + 1);
            g.addUser(u1);
            g.addUser(u2);
            defaultGroup[i] = g;
        }
    }

    public static void printChildren(WBSBean bean, int level) {
        int newLevel = level;
        char buff[] = new char[newLevel];
        Arrays.fill(buff, ' ');
        String shift = new String(buff);
        Iterator<? extends WBSBean> it = bean instanceof WorkBean ? ((WorkBean) bean).getChildrenIterator(true) : bean
                .getChildrenIterator();
        while (it.hasNext()) {
            WBSBean child = it.next();
            WorkStatus status = child instanceof WorkBean ? ((WorkBean) child).getStatus() : null;
            String z = "";
            if (child instanceof WorkBean) {
                WorkBean work = (WorkBean) child;
                Set<Dependency> deps = work.getToDependency();
                if (deps != null && !deps.isEmpty()) {
                    Dependency d = (Dependency) deps.toArray()[0];
                    z = String.format("%03d: %s %s %d", work.getNumber(), d.getFrom().getName(),
                            d.getType().toString(), d.getFrom().getNumber());
                }
            }
            LOG.info(String.format("%d: %s\tid=%s\ttype=%s\tseq=%d\tst=%s %s", child.getNumber(),
                    shift + child.toString(), child.getId(), child.getPSType(), child.getSequence(), status, z));
            // log.info(String.format("%s\tid=%s\ttype=%s\tseq=%d\tst=%s", shift + child.toString(), child.getId(),
            // child.getPSType(), child.getSequence(), status));
            newLevel += 2;
            printChildren(child, newLevel);
            newLevel -= 2;
        }
    }

    public static void printTree(BeansTree tree) {
        LOG.info("------------------");
        WBSBean root = tree.getRoot();
        printChildren(root, 0);
    }

    public static void reloadDefaults() {
        getNeed().setModified();
        getDocument().setModified();
        getWork().setModified();
        getResourcePool().setModified();
        defaultWork = null;
        defaultNeed = null;
        defaultResourcePool = null;
        defaultDocument = null;
        defaultTollgate = null;
    }

    public static Tollgate reloadTollgate() {
        defaultTollgate.setModified();
        List<?> objects = HierarchyUtils.getDescendants(defaultTollgate, null, 1000);
        for (Object o : objects) {
            ((PSObject) o).setModified();
        }
        defaultTollgate = null;
        return getTollgate();
    }

    /**
     * Reorders children accordingly to their sequences
     */
    public static void reorderBySequence(WBSBean bean) {
        bean.sortChildren(getSequenceComparator());
    }

    /**
     * Checks sequences of children and set them if they have an irregular order
     */
    public static void resequenceIfNecessary(WBSBean bean) {
        Iterator<? extends WBSBean> it = bean.getChildrenIterator();
        WBSBean prev = null;
        Comparator<WBSBean> comp = getSequenceComparator();
        while (it.hasNext()) {
            WBSBean child = it.next();
            if (prev != null && comp.compare(prev, child) > 0) {
                BeansTree.resequence(bean);
            }
            prev = child;
        }
    }
}

@SuppressWarnings("javadoc")
abstract class AssertException {

    public AssertException(boolean catchException) {
        try {
            invoke();
        } catch (ValidationError e) {
            if (catchException) {
                return;
            }
            reportError("ValidationError is throw");
        } catch (Exception ex) {
            reportError("Common Exception is thrown: " + ex.getMessage());
        }
        if (catchException) {
            reportError("Expected exception is not thrown");
        }
    }

    public abstract void invoke();

    public void reportError(String s) {
        throw new AssertionFailedError(s);
    }
}

/**
 * Class does not updates real works
 */
@SuppressWarnings("javadoc")
class TestPE extends ProjectEditor {

    public TestPE(VisitProxy visit) {
        super(visit);
    }

    @Override
    protected void saveParent(WBSBean bean) {
        // do nothing
    }
}

@SuppressWarnings("javadoc")
class WorkTypeBeanEx extends WorkTypeBean {

    private static final long serialVersionUID = 3176177565368235369L;

    private VisitProxy visit;

    public WorkTypeBeanEx(WorkTypeBean typeBean, VisitProxy visit) {
        super(visit.getContext(), typeBean.getType(), typeBean.getTemplateId());
        this.visit = visit;
    }

    @Override
    public Visit getVisit() {
        return visit.getVisit();
    }
}