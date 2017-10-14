package ps5.wbs.test.logic;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ps5.wbs.test.beans.ResourceBean;
import ps5.wbs.test.beans.Tree;
import ps5.wbs.test.beans.WBSBean;
import ps5.wbs.test.beans.WorkBean;

import com.cinteractive.jdbc.RuntimeSQLException;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.documents.Document;
import com.cinteractive.ps3.relationships.DocumentRelationship;
import com.cinteractive.ps3.resources.Need;
import com.cinteractive.ps3.tollgate.Checkpoint;
import com.cinteractive.ps3.tollgate.Tollgate;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkUtil;

public class ProjectEditor {

    // private Map<PersistentKey, Object> objects = new HashMap<PersistentKey, Object>();
    public static Tree create(Work root, InstallationContext context) {
        WBSBean rootBean = BeanHandler.createBean(root);
        Tree tree = new Tree(rootBean);
        fillTree(tree, rootBean, context);
        return tree;
    }

    public static void createChildrenBeans(Tree tree, WBSBean root, InstallationContext context) {
        if (!(root instanceof WorkBean)) {
            return;
        }
        Work work = (Work) PSObject.get(root.getId(), context);
        List list = getChildren(work, true, true);
        for (Iterator it = list.iterator(); it.hasNext();) {
            WBSBean bean = BeanHandler.createBean(it.next());
            tree.addChild(root, bean);
        }
        list = getResources(work);
        for (Iterator it = list.iterator(); it.hasNext();) {
            WBSBean bean = BeanHandler.createBean(it.next());
            tree.addChild(root, bean);
        }
    }

    public static WBSBean createNewWork(WBSBean parent, InstallationContext context) {
        // Work parentWork = (Work)Work.get(parent.getId(), context);
        // WorkCreate creator = new WorkCreate();
        // //workCreate.sequenceWithinParent= sequenceInParent;
        // creator.inheritPermissions= true;
        // creator.inheritControls= true;
        // Date curTime= new Date();
        // creator.setStartDate(new Timestamp(curTime.getTime()));
        // creator.parent = new Parent(parent);
        // workCreate.owner = owner;
        // workCreate.workName = name.trim();
        return null;
    }

    public static void fillTree(Tree tree, WBSBean node, InstallationContext context) {
        createChildrenBeans(tree, node, context);
        for (Iterator it = node.getChildrenIterator(); it.hasNext();) {
            WBSBean bean = (WBSBean) it.next();
            fillTree(tree, bean, context);
        }
    }

    public static void saveBean(WBSBean bean, InstallationContext context) {
        BeanHandler.saveBean(bean, context);
    }

    // !!!!
    private static List getChildren(Work work, boolean docs, boolean text) {
        if (work instanceof Checkpoint) {
            return ((Checkpoint) work).getCheckpointKids(true, docs, text, true, true);
        } else if (work instanceof Tollgate) {
            return ((Tollgate) work).getCheckpoints();
        } else {
            return work.getChildProjects(null, WorkUtil.createWithinParentComparator(work.getSortOrder()),
                    WorkUtil.SHOW_TASKS_FLAG);
        }
    }

    private static List getResources(Work work) {
        Collection<Need> needs = work.getNeeds();
        return needs == null ? Collections.EMPTY_LIST : (List) needs;
    }

    private Tree beansTree;

    private enum BeanHandler {
        DOCUMENT {

            @Override
            public WBSBean create(Object x) {
                Document d = (Document) x;
                WBSBean bean = new WBSBean(d.getId());
                bean.setName(d.getName());
                return bean;
            }

            @Override
            public void save(WBSBean bean, PSObject object) {
            }
        },
        DOCUMENT_RELATIONSHIP {

            @Override
            public WBSBean create(Object x) {
                return DOCUMENT.create(((DocumentRelationship) x).getDocument());
            }

            @Override
            public void save(WBSBean bean, PSObject object) {
            }
        },
        NEED {

            @Override
            public WBSBean create(Object x) {
                Need need = (Need) x;
                ResourceBean bean = new ResourceBean(need.getId());
                // bean.setName(need.getName());
                bean.setPool(need.getPool());
                bean.setUser(need.getAssignedUser());
                return bean;
            }

            @Override
            public void save(WBSBean bean, PSObject object) {
            }
        },
        WORK {

            @Override
            public WBSBean create(Object x) {
                Work work = (Work) x;
                WorkBean bean = new WorkBean(work.getId());
                bean.setName(work.getName());
                return bean;
            }

            @Override
            public void save(WBSBean b, PSObject object) {
                WorkBean bean = (WorkBean) b;
                Work work = (Work) object;
                work.setName(bean.getName());
                // Set<TagSet> tagsets = work.getTagSets();
                // Set<PSTag> tags = bean.getSelectedTags(work.get
                work.save();
            }
        };

        public static WBSBean createBean(Object x) {
            if (x instanceof Document) {
                return DOCUMENT.create(x);
            } else if (x instanceof DocumentRelationship) {
                return DOCUMENT_RELATIONSHIP.create(x);
            } else if (x instanceof Work) {
                return WORK.create(x);
            } else if (x instanceof Need) {
                return NEED.create(x);
            }
            throw new IllegalArgumentException("Unexpected object type: " + x);
        }

        public static void saveBean(WBSBean bean, InstallationContext context) {
            if (!bean.isModified()) {
                return;
            }
            PSObject object;
            try {
                object = PSObject.get(bean.getId(), context);
            } catch (RuntimeSQLException ex) {
                // !!!!
                throw new RuntimeException("Object not found: " + ex.getLocalizedMessage());
            }
            if (bean instanceof WBSBean) {
                DOCUMENT.save(bean, object);
            } else if (bean instanceof WorkBean) {
                WORK.save(bean, object);
            } else if (bean instanceof ResourceBean) {
                NEED.save(bean, object);
            }
            throw new IllegalArgumentException("Unexpected object type: " + bean);
        }

        public abstract WBSBean create(Object x);

        public abstract void save(WBSBean bean, PSObject object);
    }

    public WBSBean add(WBSBean parent, WBSBean bean /* , int position */) {
        beansTree.addChild(parent, bean);
        return null;
    }

    public void delete(WBSBean bean) {
    }
}
