package ps5.wbs.test.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ps5.support.Util;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.database.Uuid;
import com.cinteractive.ps3.mockups.CustomField;
import com.cinteractive.ps3.tags.PSTag;

public class WBSBean extends TreeNode {

    private Set<Property> changedProperties = new HashSet<Property>();

    private Tree.ChangeTracker changeTracker;

    private Map<PersistentKey, CustomField> customFields = new HashMap<PersistentKey, CustomField>();

    private Set<Dependency> fromDep;

    private PersistentKey id; // not null

    private String name;

    private Integer number;

    private Map<PersistentKey, Set<PSTag>> tagSets = new HashMap<PersistentKey, Set<PSTag>>();

    private Set<Dependency> toDep;

    public WBSBean() {
        this(Uuid.create());
    }

    public WBSBean(PersistentKey id) {
        this.id = id;
    }

    public void clearModified() {
        changedProperties.clear();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof WBSBean && getId().equals(((WBSBean) o).getId());
    }

    public Set<Property> getChangedProperties() {
        return changedProperties;
    }

    public PersistentKey getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getNumber() {
        return number;
    }

    public Set<PSTag> getSelectedTags(PersistentKey tagSetId) {
        Set<PSTag> tags = tagSets.get(tagSetId);
        // TODO throw an error
        return tags == null ? new HashSet<PSTag>() : tags;
    }

    public Set<Dependency> getToDependency() {
        return toDep;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public boolean isModified() {
        return !changedProperties.isEmpty();
    }

    public boolean isModified(Property property) {
        return changedProperties.contains(property);
    }

    public boolean isModified(Property.Field field, Object id) {
        return isModified(new Property(field, id));
    }

    public void setChangeTracker(Tree.ChangeTracker changeTracker) {
        this.changeTracker = changeTracker;
    }

    public void setId(PersistentKey id) {
        this.id = id;
    }

    public void setModified(Property prop) {
        changedProperties.add(prop);
        if (changeTracker != null) {
            changeTracker.setChanged(this);
        }
    }

    public void setName(String name) {
        updateModified(Property.Name, this.name, name);
        this.name = name;
    }

    public void setNumber(Integer number) {
        updateModified(Property.Number, this.number, number);
        this.number = number;
    }

    public void setSelectedTags(PersistentKey tagSetId, Set<PSTag> tags) {
        updateModified(Property.Field.TAGSET, tagSetId, getSelectedTags(tagSetId), tags);
        tagSets.put(tagSetId, tags);
    }

    // ///
    @Override
    public String toString() {
        return getName();
    }

    public void updateModified(Property prop, Object oldValue, Object newValue) {
        if (!Util.checkObjectsEquals(oldValue, newValue)) {
            setModified(prop);
        }
    }

    public void updateModified(Property.Field field, Object id, Object oldValue, Object newValue) {
        updateModified(new Property(field, id), oldValue, newValue);
    }

    void addToDependency(Dependency dep) {
        if (toDep == null) {
            toDep = new HashSet<Dependency>();
        }
        toDep.add(dep);
        dep.getFrom().addFromDependency(dep);
        setModified(Property.Dependency);
    }

    void setToDependency(Collection deps) {
        Iterator it = deps.iterator();
        while (it.hasNext()) {
            addToDependency((Dependency) it.next());
        }
    }

    private void addFromDependency(Dependency dep) {
        if (fromDep == null) {
            fromDep = new HashSet<Dependency>();
        }
        fromDep.add(dep);
    }
}

class TreeNode {

    private List<TreeNode> children;

    private TreeNode parent;

    public Iterator getChildrenIterator() {
        return !hasChildren() ? Collections.EMPTY_LIST.iterator() : children.iterator();
    }

    protected TreeNode getParent() {
        return parent;
    }

    protected boolean hasChildren() {
        return children != null;
    }

    TreeNode addChild(TreeNode node) {
        children = children == null ? new ArrayList<TreeNode>() : children;
        children.add(node);
        node.setParent(this);
        return node;
    }

    private void setParent(TreeNode parent) {
        this.parent = parent;
    }
}
