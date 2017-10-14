package com.powersteeringsoftware.libs.objects;

import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 22.06.2010
 * Time: 13:55:33
 */
public class PSTag extends ConfigObject implements Comparable, Cloneable {
    public static final String SEPARATOR = " -> ";
    public static final String NAME = "tag";
    private PSTag parent;

    public enum Associate {
        MESSAGES("Messages"),
        MESSAGES_DISCUSSIONS(MESSAGES, "Discussion Items"),
        MESSAGES_RISKS(MESSAGES, "Risks"),

        DOCUMENTS("Documents"),
        DOCUMENTS_DOCUMENTS(DOCUMENTS, "Documents"),

        PEOPLES("People"),
        PEOPLES_USERS(PEOPLES, "Users"),
        PEOPLES_GROUPS(PEOPLES, "Groups"),
        PEOPLES_RESOURCE_POOLS(PEOPLES, "Resource Pools"),
        PEOPLES_RESOURCE_QUALIFICATIONS(PEOPLES, "Resource Qualifications"),

        WORKS("Work Types"),
        WORKS_FOLDERS(WORKS, "Folders"),
        WORKS_GATED_PROJECTS_NSG(WORKS, "Gated Project None Sequetials"),
        WORKS_GATED_PROJECTS(WORKS, "Gated Projects"),
        WORKS_GATES(WORKS, "Gates"),
        WORKS_MILESTONE(WORKS, "Milestones"),
        WORKS_MSP_PROJECTS(WORKS, "MSP Projects"),
        WORKS_TEMPLATES(WORKS, "Templates"),
        WORKS_UNEXPANDED_WORKS(WORKS, "Unexpanded Work Items"),
        WORKS_WORKS(WORKS, "Work Items"),;

        Associate parent;
        String type;

        Associate(String s) {
            type = s;
        }

        Associate(Associate parent, String s) {
            this.parent = parent;
            this.type = s;
        }

        public List<Associate> getTypes() {
            if (parent != null) return Collections.emptyList();
            List<Associate> res = new ArrayList<Associate>();
            for (Associate t : values()) {
                if (!t.name().startsWith(name() + "_")) continue;
                res.add(t);
            }
            return res;
        }

        public Associate getParent() {
            return parent;
        }

        public boolean hasParent() {
            return parent != null;
        }

        public String toString() {
            return type;
        }

        public String getSingle() {
            return type.replaceAll("s$", "");
        }
    }

    public PSTag(Config conf) {
        super(conf);
        Config parent = conf.getParent();
        if (parent != null && parent.getName().equals(NAME)) {
            this.parent = new PSTag(parent);
        }
    }

    public PSTag(String name) {
        super(Config.createConfig(NAME));
        if (name.contains(SEPARATOR)) {
            String[] names = name.split(SEPARATOR);
            setName(names[names.length - 1]);
            this.parent = new PSTag(name.replaceAll(SEPARATOR + getName() + "$", ""));
        } else {
            setName(name);
        }
    }

    public List<Associate> getAssociations() {
        List<Associate> res = new ArrayList<Associate>();
        for (Config c : getRoot().getConfig().getChsByName("association")) {
            PowerSteeringVersions ver = PowerSteeringVersions.valueOf(c, "no");
            if (ver != null && getVersion() != null && getVersion().verGreaterOrEqual(ver)) continue;
            String txt = c.getText().toUpperCase().replace("-", "_");
            try {
                Associate type = Associate.valueOf(txt);
                if (type.hasParent())
                    res.add(type);
                else {
                    res.addAll(type.getTypes());
                    for (String st : c.getAttribute("exclude").split(";")) {
                        Associate ex = Associate.valueOf(st.trim().toUpperCase().replace("-", "_"));
                        res.remove(ex);
                    }
                }
            } catch (IllegalArgumentException iae) {
                // ignore
            }
        }
        return res;
    }

    public List<String> getAssociations(Associate parent) {
        List<Associate> types = getAssociations();
        List<String> res = new ArrayList<String>();
        for (Associate type : types) {
            if (parent.equals(type.getParent())) {
                res.add(type.toString());
            }
        }
        return res;
    }

    public void addAssociation(Associate type) {
        Config c = getRoot().getConfig().addElement("association");
        c.setText(type.name());
    }

    public void setAssociation(Associate type) {
        if (getAssociations().contains(type)) return;
        addAssociation(type);
    }

    public boolean isInResources() {
        return getBooleanFalse("resources");
    }

    public void addValue(String value) {
        PSTag val = new PSTag(value);
        val.setParent(this);
    }

    public List<String> getValues() {
        List<String> res = new ArrayList<String>();
        if (!isMultiple() && !isHierarchical()) res.add(""); // empty for flat
        for (PSTag tg : getChilds()) {
            res.add(tg.getName());
        }
        return res;
    }

    public List<String> getLoadingValues() {
        List<String> res = new ArrayList<String>();
        for (PSTag tg : getChilds()) {
            if (tg.getConfig().hasAttribute("loading") && Boolean.parseBoolean(tg.getConfig().getAttribute("loading")))
                res.add(tg.getName());
        }
        return res;
    }

    public List<String> getTreeList() {
        PSTag root = getRoot();
        List<String> res = new ArrayList<String>();
        for (PSTag tg : getAllChildren()) {
            res.add(tg.getFullName().replace(root.getName() + SEPARATOR, ""));
        }
        return res;
    }

    public List<PSTag> getFirstLongestChain() {
        int max = 0;
        List<PSTag> res = null;
        for (PSTag ch : getAllChildren()) {
            List<PSTag> parents = getParents(ch);
            if (parents.size() > max) {
                max = parents.size();
                res = parents;
            }
        }
        if (res == null) return null;
        return res;
    }

    public static List<PSTag> getParents(PSTag child) {
        List<PSTag> res = new ArrayList<PSTag>();
        PSTag parent = child;
        while (parent != null) {
            res.add(0, parent);
            parent = parent.getParent();
        }
        return res;
    }

    public static List<String> getOpenedTreeList(PSTag tg) {
        PSTag root = tg.getRoot();
        List<PSTag> parents = getParents(tg);
        parents.remove(0); // remove root.
        List<PSTag> tmp = _getOpenedTreeList(root, parents, tg);
        List<String> res = new ArrayList<String>();
        for (PSTag ch : tmp) {
            res.add(ch.getFullName().replace(root.getName() + SEPARATOR, ""));
        }
        return res;
    }

    private static List<PSTag> _getOpenedTreeList(PSTag root, List<PSTag> parents, PSTag tg) {
        List<PSTag> res = new ArrayList<PSTag>();
        for (PSTag ch : root.getChilds()) {
            res.add(ch);
            if (parents.size() > 0 && parents.get(0).equals(ch)) {
                parents.remove(0);
                res.addAll(_getOpenedTreeList(ch, parents, tg));
            }
        }
        return res;
    }

    public PSTag getRoot() {
        if (!hasParent()) return this;
        return getParent().getRoot();
    }

    public void setParent(PSTag par) {
        if (parent != null) return;
        parent = par;
        parent.getConfig().addChild(conf);
    }

    public String getFullName() {
        if (hasParent()) return getParent().getFullName() + SEPARATOR + getName();
        return getName();
    }

    public PSTag getParent() {
        return parent;
    }

    private boolean getRootAttribute(String name) {
        return getRootAttribute(getRoot(), name);
    }

    private void setRootAttribute(String name, boolean x) {
        setRootAttribute(getRoot(), name, x);
    }

    private static boolean getRootAttribute(PSTag tag, String name) {
        return tag.getConfig().getAttribute(name).equalsIgnoreCase(String.valueOf(Boolean.TRUE));
    }

    private static void setRootAttribute(PSTag tag, String name, boolean flag) {
        tag.getConfig().setAttributeValue(name, String.valueOf(flag));
    }

    public boolean isMultiple() {
        return getRootAttribute("multiple");
    }

    public boolean isApplyUsersPermissions() {
        return getRootAttribute("apply-users-permissions");
    }

    public boolean isAlertsAndEventLogging() {
        return getRootAttribute("alert-and-event-logging");
    }

    public boolean isLocked() {
        return getRootAttribute(this, "locked");
    }

    public boolean isRequired() {
        return getRootAttribute("required");
    }

    public void setRequired(boolean x) {
        setRootAttribute("required", x);
    }

    public void setMultiple(boolean x) {
        setRootAttribute("multiple", x);
    }


    public boolean hasParent() {
        return parent != null;
    }

    public List<PSTag> getChilds() {
        List<PSTag> tags = new ArrayList<PSTag>();
        for (Config c : conf.getChsByName(NAME)) {
            PowerSteeringVersions ver = PowerSteeringVersions.valueOf(c);
            if (ver != null && ver.verGreaterOrEqual(getVersion())) continue;
            PSTag tag = new PSTag(c);
            tag.setParent(this);
            tags.add(tag);
        }
        return tags;
    }

    public List<PSTag> getAllChildren() {
        List<PSTag> res = new ArrayList<PSTag>();
        putToList(res, this);
        return res;
    }

    private static void putToList(List<PSTag> tags, PSTag tag) {
        for (PSTag ch : tag.getChilds()) {
            tags.add(ch);
            putToList(tags, ch);
        }
    }

    public String toString() {
        return getFullName() + (getId() != null ? "(" + getId() + ")" : "");
    }

    public boolean equals(Object o) {
        return o != null && o instanceof PSTag && getFullName().equals(((PSTag) o).getFullName());
    }

    public int hashCode() {
        return getFullName().hashCode();
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) return -1;
        if (!(o instanceof PSTag)) return -1;
        return toString().compareTo(o.toString());
    }

    public boolean isHierarchical() {
        for (PSTag t : getChilds()) {
            if (t.getChilds().size() != 0) return true;
        }
        return false;
    }

    @Override
    public void setCreated() {
        super.setCreated();
        TestSession.putTag(this);
    }

    @Override
    public void setDeleted() {
        super.setDeleted();
        TestSession.removeTag(this);
    }

    public void removeChild(PSTag tg) {
        PSTag parent = tg.getParent();
        parent.getConfig().removeChild(tg.getConfig());
    }

    public boolean hasChild(PSTag tg) {
        return getAllChildren().contains(tg);
    }

    public boolean hasChildren() {
        return getChilds().size() != 0;
    }

    public PSTag getChild(String s) {
        for (PSTag t : getChilds()) {
            if (t.getName().equals(s)) return t;
        }
        return null;
    }

    public List<PSTag> getChain(PSTag tg) {
        List<PSTag> res = new ArrayList<PSTag>();
        while (tg.hasParent()) {
            res.add(0, tg);
            tg = tg.getParent();
        }
        return res;
    }

    public static class Activity extends AbstractActivity {
        public Activity(Config c) {
            super(c, TestSession.Keys.ACTIVITY);
        }
    }

    public static class ActivityTypes extends AbstractActivity {
        public ActivityTypes(Config c) {
            super(c, TestSession.Keys.ACTIVITY_TYPES);
        }
    }

    private static abstract class AbstractActivity extends PSTag {
        private TestSession.Keys key;

        private AbstractActivity(Config c, TestSession.Keys k) {
            super(c);
            key = k;
        }

        public void setCreated() {
            if (TestSession.isObjectNull(key))
                TestSession.putObject(key, this);
        }

        public boolean exist() {
            return true;
        }

        public void setDeleted() {

        }
    }

}
