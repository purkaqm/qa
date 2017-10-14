package ps5.wbs.test.beans;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.ps3.relationships.DependencyType;

public class Tree {

    private Map<PersistentKey, WBSBean> beans = new HashMap<PersistentKey, WBSBean>();

    // public static class Builder {
    // private Tree tree = new Tree();
    // private Map<PersistentKey, WBSBean> beans = new HashMap<PersistentKey, WBSBean>();
    //
    // public void addChild(WBSBean parent, WBSBean node) {
    // tree.addChild(parent, node);
    // beans.put(node.getId(), node);
    // }
    //
    // public void setRoot(WBSBean root) {
    // tree.setRoot(root);
    // beans.put(root.getId(), root);
    // }
    //
    // public void addDependency(WBSBean from, WBSBean to, DependencyType type) {
    // addDependency(new DependentWorks(from, to, type));
    // }
    //
    // public void addDependency(DependentWorks dw) {
    // beans.put(dw.getFrom().getId(), dw.getFrom());
    // beans.put(dw.getTo().getId(), dw.getTo());
    // List fromList = tree.getFromDependencies(dw.getFrom());
    // List toList = tree.getToDependencies(dw.getTo());
    // fromList.add(dw);
    // toList.add(dw);
    // }
    //
    // public Iterator getWorksIterator() {
    // return tree.getChildrenIterator(null);
    // }
    //
    // public Tree createTree() {
    // List workList = new ArrayList();
    // Iterator it = getWorksIterator();
    // while (it.hasNext()) {
    // workList.add((WBSBean) it.next());
    // }
    //
    // Collections.reverse(workList);
    // tree._workList = workList;
    // tree.setWorksMap(beans);
    // return tree;
    // }
    //
    // public WBSBean getWork(PersistentKey id) {
    // return beans.get(id);
    // }
    // }
    // ----------------------------
    private ChangeTracker changeTracker = new ChangeTracker();

    private WBSBean root;

    public Tree(WBSBean root) {
        this.root = root;
        addChangeTracker(root);
    }

    static class ChangeTracker {

        private Set<WBSBean> beans = new HashSet<WBSBean>();

        public void clear() {
            beans.clear();
        }

        public Set<WBSBean> getBeans() {
            return beans;
        }

        public void setChanged(WBSBean bean) {
            beans.add(bean);
        }
    }

    public void addChild(WBSBean parent, WBSBean child) {
        parent.addChild(child);
        beans.put(child.getId(), child);
        addChangeTracker(child);
    }

    public void addDependency(Dependency dep) {
        beans.put(dep.getFrom().getId(), dep.getFrom());
        beans.put(dep.getTo().getId(), dep.getTo());
        dep.getTo().addToDependency(dep);
    }

    public void addDependency(WBSBean from, WBSBean to, DependencyType type) {
        addDependency(new Dependency(from, to, type));
    }

    public void clearChanges() {
        changeTracker.clear();
    }

    public Set<WBSBean> getChangedBeans() {
        return changeTracker.getBeans();
    }

    public Iterator getChildrenIterator(final WBSBean node) {
        return getChildrenIterator(node, Integer.MAX_VALUE);
    }

    public Iterator getChildrenIterator(final WBSBean node, final int level) {
        return new Iterator() {

            Iterator currIt = node == null || level == 0 ? Collections.EMPTY_LIST.iterator() : node
                    .getChildrenIterator();

            LinkedList<Iterator> stack = new LinkedList<Iterator>();

            @Override
            public boolean hasNext() {
                return currIt.hasNext();
            }

            @Override
            public Object next() {
                WBSBean node = (WBSBean) currIt.next();
                stack.add(currIt);
                if (node.hasChildren() && stack.size() < level) {
                    currIt = node.getChildrenIterator();
                } else {
                    while (!currIt.hasNext() && !stack.isEmpty()) {
                        currIt = stack.removeLast();
                    }
                }
                return node;
            }

            @Override
            public void remove() {
                throw new RuntimeException("Method not supported");
            }
        };
    }

    public WBSBean getRoot() {
        return root;
    }

    public void move(WBSBean bean, WBSBean newParent, int position) {
    }

    public void remove(WBSBean bean) {
    }

    public void reorder(int expandedLevel) {
        Iterator it = getChildrenIterator(getRoot(), expandedLevel);
        int n = 1;
        getRoot().setNumber(n);
        while (it.hasNext()) {
            WBSBean bean = (WBSBean) it.next();
            bean.setNumber(++n);
        }
    }

    private void addChangeTracker(WBSBean bean) {
        bean.setChangeTracker(changeTracker);
    }
}
