package com.powersteeringsoftware.libs.objects;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.works.WorkType;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests_data.Config;

import java.util.*;

/**
 * Example of xml:
 * <permissions_set id="guest1">
 * <permissions target="wi">
 * <category name="guest">
 * <user xpath="//user[@id='user1']"/>
 * <permission value="true">VIEW_PROJECT_CENTRAL</permission>
 * <permission value="false">DELETE</permission>
 * </category>
 * </permissions>
 * <permissions target="ctx">
 * <category name="guest">
 * <user xpath="//user[@id='user1']"/>
 * <permission value="false">CREATE_NON_TEMPLATE_WORK</permission>
 * </category>
 * <category name="none">
 * <role><name>Everybody</name></role>
 * </category>
 * </permissions>
 * </permissions_set>
 * <p/>
 * where text txt in xml "<permission>txt</permission>" is a enum name (see DefinePermissionsPageLocators.IRow)
 * and attribute txt target in xml "<permissions target="txt"/>" is a enum name  (see BasicTarget)
 */
public class PSPermissions extends ConfigObject {
    public static final String NAME = "permissions";
    private List<CategoryRole> coreSet;
    private List<CategoryRole> customSet;

    public static final String VIEW_PERMISSION_VERB = "view";

    public PSPermissions(Config conf) {
        super(conf);
    }

    public PSPermissions() {
        super();
    }

    public enum BasicTarget {
        USR("Users"),
        CTX("Contexts"),
        WI("Work Items"),
        GRP("Groups"),
        MT("Metric Templates"),
        MTB("Manage Time and Billing"),;
        private String name;

        private BasicTarget(String s) {
            this.name = s;
        }

        public String toString() {
            return name;
        }
    }


    public enum Verb {
        VIEW,
        EDIT;
    }

    public enum BasicCategory {
        NONE("none"),
        GUEST("guest"),
        VIEW("view"),
        EDIT("edit"),
        WORK("work"),
        FINANCE("finance"),
        ALL("all"),;
        private String name;

        BasicCategory(String s) {
            name = s;
        }
    }

    public BasicTarget getCategoryTarget() {
        String res = conf.getAttribute("target");
        try {
            return BasicTarget.valueOf(res.toUpperCase());
        } catch (Exception e) { // then object category
            return null;
        }
    }


    public void setCategoryTarget(BasicTarget c) {
        conf.setAttributeValue("target", c.name().toLowerCase());
    }

    public List<CategoryRole> getCategoryRoles() {
        List<CategoryRole> res = new ArrayList<CategoryRole>();
        for (Config c : getConfig().getChsByName("category")) {
            res.add(new CategoryRole(c, this));
        }
        return res;
    }


    public List<String> getCategoryRoleNames() {
        List<String> res = new ArrayList<String>();
        for (CategoryRole c : getCategoryRoles()) {
            res.add(c.getName());
        }
        return res;
    }

    public List<CategoryRole> getCoreSetCategories() {
        if (coreSet != null) return coreSet;
        coreSet = new ArrayList<CategoryRole>();
        for (CategoryRole c : getCategoryRoles()) {
            if (c.isCoreSet())
                coreSet.add(c);
        }
        return coreSet;
    }

    public List<CategoryRole> getCustomSetCategories() {
        if (customSet != null) return customSet;
        customSet = new ArrayList<CategoryRole>();
        for (CategoryRole c : getCategoryRoles()) {
            if (!c.isCoreSet())
                customSet.add(c);
        }
        return customSet;
    }

    public boolean hasCustomSet() {
        return getCustomSetCategories().size() != 0;
    }

    public boolean hasCoreSet() {
        return getCoreSetCategories().size() != 0;
    }

    private void addCategoryRole(String name) {
        CategoryRole r = getCategory(name);
        if (r != null) return;
        r = new CategoryRole(name);
        getConfig().addChild(r.getConfig());
    }

    public void removeCategory(String name) {
        CategoryRole r = getCategory(name);
        if (r == null) return;
        getConfig().removeChild(r.getConfig());
    }

    public CategoryRole getCategory(String name) {
        for (CategoryRole c : getCategoryRoles()) {
            if (c.getName() == null && name == null) return c;
            if (name != null && name.equalsIgnoreCase(c.getDisplayedName())) return c;
        }
        return null;
    }

    public void addPermission(String perm, String category, boolean check, boolean disabled) {
        addCategoryRole(category);
        getCategory(category).addPermission(perm, check, disabled);
    }

    public void addCoreSet(String role, String level, String category, boolean isChecked) {
        addCategoryRole(category);
        if (isChecked)
            getCategory(category).addRole(role, level);
    }

    public void addCustomSet(String user, String category, boolean isChecked) {
        addCategoryRole(category);
        if (!isChecked) return;
        // ToDo: 'user' is formatted name for User. need change this place
        User _u = User.getUserByFullFormattedName(user);
        if (_u == null) {
            PSLogger.warn("Can't find user " + user);
            _u = new User();
            _u.setFormatFullName(user);
            _u.setCreated();
        }
        //Assert.assertNotNull(_u, "Can't find user " + user);
        getCategory(category).addUser(_u);
    }

    private String _getCategoryTarget() {
        BasicTarget res = getCategoryTarget();
        if (res == null) return "";
        return res.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof PSPermissions)) return false;
        PSPermissions p = (PSPermissions) o;
        return _getCategoryTarget().equals(p._getCategoryTarget()) && getCategoryRoles().equals(p.getCategoryRoles());
    }

    @Override
    public String toString() {
        //todo: make more more readable output.
        //StringBuffer sb = new StringBuffer();
        //sb.append(getCategoryTarget()).append(" -> ");
        //for (CategoryRole c : getCategoryRoles()) {}
        return getCategoryTarget() != null ? getCategoryTarget().toString() : getConfig().toString();
    }

    public void mergePermissions(PSPermissions _add) {
        for (CategoryRole _category : _add.getCategoryRoles()) {
            int index = getCategoryRoleNames().indexOf(_category.getDisplayedName());
            if (index != -1) {
                CategoryRole _thisCategory = getCategoryRoles().get(index);
                _mergePermissions(_thisCategory, _category);
            } else {
                getConfig().addChild(_category.getConfig());
            }
        }
    }

    public void mergeSets(PSPermissions _add) {
        for (CategoryRole _category : _add.getCategoryRoles()) {
            int index = getCategoryRoleNames().indexOf(_category.getDisplayedName());
            if (index != -1) {
                CategoryRole _thisCategory = getCategoryRoles().get(index);
                _mergeSets(_thisCategory, _category);
            } else {
                getConfig().addChild(_category.getConfig());
            }
        }
    }

    private void _mergeSets(CategoryRole _thisCategory, CategoryRole _addCategory) {
        List<User> users = _addCategory.getUsers();
        List<PSRole> roles = _addCategory.getRoles();
        for (CategoryRole c : getCategoryRoles()) {
            c.removeRoles(roles);
            c.removeUsers(users);
        }
        _thisCategory.addUsers(users);
        _thisCategory.addRoles(roles);
    }

    private void _mergePermissions(CategoryRole _thisCategory, CategoryRole _addCategory) {
        List<String> names = _addCategory.getPermissionNames();

        for (CategoryRole.Permission _thisP : _thisCategory.getPermissions()) {
            if (names.contains(_thisP.getName())) {
                _thisCategory.getConfig().removeChild(_thisP.perm);
            }
        }
        for (CategoryRole.Permission _addP : _addCategory.getPermissions()) {
            _thisCategory.getConfig().addChild(_addP.perm);
        }
    }

    /**
     * after update some permissions can be changed...expect specified
     *
     * @param exclude
     */
    public void setAllOtherIgnored(PSPermissions exclude) {
        Set<String> names = new HashSet<String>();
        for (CategoryRole r : exclude.getCategoryRoles()) {
            for (CategoryRole.Permission p : r.getPermissions()) {
                names.add(p.getName());
            }
        }
        for (CategoryRole r : getCategoryRoles()) {
            for (CategoryRole.Permission p : r.getPermissions()) {
                if (names.contains(p.getName())) continue;
                p.setIgnored(true);
            }
        }
    }

    /**
     * Returns true when category has permission for a Verb
     *
     * @param category
     * @param verb
     * @return
     */
    public boolean hasPermission(PSPermissions.CategoryRole category, PSPermissions.Verb verb) {
        PSPermissions.CategoryRole role = getCategory(category.getDisplayedName());
        if (null != role) {
            PSPermissions.CategoryRole.Permission permission = role.getPermission(verb.name());
            if (null != permission) {
                PSLogger.debug("Permission '" + verb.name() + "=" + permission.getValue() + "' is present");
                return permission.getValue();
            }
            PSLogger.debug("Permission '" + verb.name() + "' not found");
            return false;
        }
        PSLogger.debug("Category '" + category.getName() + "' not found");
        return false;
    }

    public static class CustomCategory extends CategoryRole {
        public CustomCategory(Config c) {
            super(c, null);
        }

        public BasicTarget getObjectType() {
            return BasicTarget.valueOf(getString("object_type").toUpperCase());
        }

        public void setSequence(int i) {
            set("sequence", i);
        }

        public String getSequence() {
            return getString("sequence");
        }
    }

    public static class CategoryRole extends ConfigObject {
        public static final String NAME = "category";
        private PSPermissions parent;

        private List<User> users;

        private CategoryRole(Config c, PSPermissions p) {
            super(c);
            parent = p;
            if (isCustom() && doUseTimestamp()) { // todo:
                getConfig().removeChild("use-timestamp");
                set("name", getName() + CoreProperties.getTestTemplate());
            }
        }

        private CategoryRole(String name) {
            super();
            getConfig().setAttributeValue("name", name);
        }

        public BasicTarget getObjectType() {
            return parent.getCategoryTarget();
        }

        public String getName() {
            if (getConfig().hasAttribute("name")) {
                return getConfig().getAttribute("name");
            }
            return null; // can be null for custom data
        }

        public void setName(String name) {
            getConfig().setAttributeValue("name", name);
        }

        public boolean isCustom() {
            return getBooleanFalse("is_custom");
        }

        protected void setCustom() {
            set("is_custom", true);
        }

        public String getDisplayedName() {
            return isCustom() ? getString("name") : getName();
        }

        private String _getName() {
            String res = getDisplayedName();
            if (res == null) return "";
            return res;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof CategoryRole)) return false;
            CategoryRole c = (CategoryRole) o;
            return c._getName().equals(_getName()) && getUsers().equals(c.getUsers()) && getRoles().equals(c.getRoles()) && getPermissions().equals(c.getPermissions());
        }

        public String toString() {
            return getName();
        }

        public void setPermissions(CategoryRole role) {
            getConfig().removeChildren("permission");
            for (Config c : role.getConfig().getChsByName("permission")) {
                getConfig().addChild(c);
            }
        }

        /**
         * @return list of permissions for define category
         */
        public List<Permission> getPermissions() {
            List<Permission> res = new ArrayList<Permission>();
            for (Config c : getConfig().getChsByName("permission")) {
                res.add(new Permission(c));
            }
            Collections.sort(res);
            return res;
        }

        private List<String> getPermissionNames() {
            List<Permission> ps = getPermissions();
            List<String> res = new ArrayList<String>();
            for (Permission p : ps) {
                res.add(p.getName());
            }
            return res;
        }

        public Permission getPermission(String name) {
            for (Permission c : getPermissions()) {
                if (c.getName().equals(name)) return c;
            }
            return null;
        }

        public boolean isCoreSet() {
            return getConfig().hasChild("role");
        }

        /**
         * @return list of roles for core set
         */
        public List<PSRole> getRoles() {
            List<PSRole> res = new ArrayList<PSRole>();
            for (Config c : getConfig().getChsByName("role")) {
                res.add(new PSRole(c, this));
            }
            return res;
        }

        /**
         * @return list of users for custom set
         */
        public List<User> getUsers() {
            if (users != null) return users;
            users = new ArrayList<User>();
            for (Config c : getConfig().getChsByName(User.NAME)) {
                users.add(findUser(c));
            }
            return users;
        }

        public void addUser(User u) {
            if (getUsers().contains(u)) return;
            getUsers().add(u);
            addUser(getConfig(), u);
        }

        public void removeUsers(List<User> list) {
            for (User u : list) {
                int i = getUsers().indexOf(u);
                if (i == -1) continue;
                User _u = getUsers().get(i);
                removeUser(_u);
            }
        }

        public void removeUser(User u) {
            for (Config c : getConfig().getChsByName(User.NAME)) {
                if (u.equals(findUser(c))) {
                    getConfig().removeChild(c);
                    getUsers().remove(u);
                }
            }
        }

        public void addUsers(List<User> users) {
            for (User u : users) {
                addUser(u);
            }
        }

        public void removeRoles(List<PSRole> list) {
            List<PSRole> roles = getRoles();
            for (PSRole r : list) {
                int i = roles.indexOf(r);
                if (i == -1) continue;
                removeRole(roles.get(i));
            }
        }

        public void removeRole(PSRole r) {
            getConfig().removeChild(r.getConfig());
        }

        public void addRole(PSRole r) {
            getConfig().addChild(r.getConfig());
        }

        public void addRole(String name, String level) {
            getConfig().addChild(new PSRole(name, level, this).getConfig());
        }

        public void addRoles(List<PSRole> roles) {
            for (PSRole r : roles) {
                addRole(r);
            }
        }

        public void addPermission(String perm, boolean b, boolean d) {
            for (Permission p : getPermissions()) {
                if (p.getName().equals(perm)) {
                    p.setValue(b);
                    p.setDisabled(d);
                    return;
                }
            }
            Permission p = new Permission(perm);
            p.setValue(b);
            p.setDisabled(d);
            getConfig().addChild(p.perm);
        }

        public class Permission implements Comparable {
            private Config perm;

            private Permission(Config c) {
                perm = c;
            }

            private Permission(String n) {
                perm = Config.createConfig("permission");
                perm.setText(n);
            }

            public void setValue(Boolean b) {
                perm.setAttributeValue("value", b == null ? "null" : String.valueOf(b));
            }

            public Boolean getValue() {
                if (!perm.hasAttribute("value") || perm.getAttribute("value").equals("null")) return null;
                return Boolean.parseBoolean(perm.getAttribute("value"));
            }

            public boolean isIgnored() {
                return perm.hasAttribute("ignored") && Boolean.parseBoolean(perm.getAttribute("ignored"));
            }

            public boolean isDisabled() {
                return perm.hasAttribute("disabled") && Boolean.parseBoolean(perm.getAttribute("disabled"));
            }

            public void setIgnored(boolean b) {
                perm.setAttributeValue("ignored", Boolean.toString(b));
            }

            public void setDisabled(boolean b) {
                perm.setAttributeValue("disabled", Boolean.toString(b));
            }

            private String _getValue() {
                Boolean res = getValue();
                if (res == null) return "";
                return String.valueOf(res);
            }

            public String getName() {
                String r = perm.getText();
                return r == null ? null : r.toUpperCase();
            }


            @Override
            public boolean equals(Object o) {
                if (o == null) return false;
                if (!(o instanceof Permission)) return false;
                Permission p = (Permission) o;
                if (!getName().equals(p.getName())) return false;
                if (isDisabled() != p.isDisabled()) return false;
                if (isIgnored() || p.isIgnored()) return true; // do not check values
                return _getValue().equals(p._getValue());
            }

            public String toString() {
                return getName() + "(" + _getValue() + ")";
            }

            @Override
            public int compareTo(Object o) {
                if (o == null || !(o instanceof Permission)) return -1;
                return toString().compareTo(o.toString());
            }
        }

    }

    public static class AllSet extends ConfigObject {
        public static final String NAME = "permissions_set";

        public AllSet(Config c) {
            super(c);
        }

        public AllSet() {
            super();
        }

        public AllSet(PSPermissions... perms) {
            this();
            for (PSPermissions p : perms) {
                getConfig().addChild(p.getConfig());
            }
        }

        public List<PSPermissions> getPermissions() {
            List<PSPermissions> perms = new ArrayList<PSPermissions>();
            for (Config _c : getConfig().getChsByName(PSPermissions.NAME)) {
                perms.add(new PSPermissions(_c));
            }
            return perms;
        }

        public boolean hasPermissions() {
            return getPermissions().size() != 0;
        }

        public void put(BasicTarget target) {
            PSPermissions p = get(target);
            if (p != null) return;
            p = new PSPermissions();
            p.setCategoryTarget(target);
            getConfig().addChild(p.getConfig());
        }

        public PSPermissions get(BasicTarget target) {
            for (PSPermissions p : getPermissions()) {
                if (p.getCategoryTarget().toString().equalsIgnoreCase(target.toString())) return p;
            }
            return null;
        }

        public boolean equals(Object s) {
            return s != null && s instanceof AllSet && getPermissions().equals(((AllSet) s).getPermissions());
        }


        public void mergePermissions(PSPermissions add) {
            PSPermissions _was = get(add.getCategoryTarget());
            if (_was == null) {
                getConfig().addChild(add.getConfig());
            } else {
                _was.mergePermissions(add);
            }
            // remove ignored..
            for (PSPermissions p : getPermissions()) {
                for (CategoryRole c : p.getCategoryRoles()) {
                    for (CategoryRole.Permission _p : c.getPermissions()) {
                        _p.setIgnored(false);
                    }
                }
            }
        }

        public void mergeSets(PSPermissions add) {
            PSPermissions _was = get(add.getCategoryTarget());
            if (_was == null) {
                getConfig().addChild(add.getConfig());
            } else {
                _was.mergeSets(add);
            }
        }


        public String toString() {
            //StringBuffer sb = new StringBuffer();
            //todo:
            return getConfig().toString();
        }
    }

    public void addCustomRole(CustomPSRole role) {
        if (!getCategoryTarget().equals(BasicTarget.WI))
            throw new IllegalArgumentException("Adding role is unsupported for this perm");
        CategoryRole category = getCategory(BasicCategory.NONE.name);  // bu default to none immediately after creating
        category.addRole(role.getName(), CustomPSRole.Level.PROJECT.name);
        category.addRole(role.getName(), CustomPSRole.Level.HIGHER.name);
    }

    public List<PSRole> getRoles(CustomPSRole role) {
        List<PSRole> roles = new ArrayList<PSRole>();
        for (CategoryRole c : getCategoryRoles()) {
            for (PSRole r : c.getRoles()) {
                if (r.getName().equalsIgnoreCase(role.getName())) roles.add(r);
            }
        }
        return roles;
    }

    public void removeRole(CustomPSRole role) {
        for (PSRole r : getRoles(role)) {
            r.getCategory().removeRole(r);
        }
    }

    public void removeUser(User u) {
        for (CategoryRole c : getCategoryRoles()) {
            int i = c.getUsers().indexOf(u);
            if (i != -1)
                c.removeUser(c.getUsers().get(i));
        }
    }

    public List<User> getUsers() {
        HashSet<User> res = new HashSet<User>();
        for (CategoryRole c : getCategoryRoles()) {
            res.addAll(c.getUsers());
        }
        return new ArrayList<User>(res);
    }

    public static class PSRole extends ConfigObject implements Comparable {
        public static final String NAME = "role";
        private CategoryRole role;

        private PSRole(Config c, CategoryRole role) {
            super(c);
            this.role = role;
        }

        private PSRole(String n, String l, CategoryRole role) {
            super();
            this.role = role;
            setName(n);
            if (l != null) {
                setLevel(l);
            }
        }

        public String getName() {
            return getString("name");
        }

        public void setName(String name) {
            set("name", name);
        }

        public String getLevel() {
            return getString("level");
        }

        public void setLevel(String l) {
            set("level", l);
        }

        public String toString() {
            return getName() + (getLevel() == null ? "" : "," + getLevel());
        }

        public boolean equals(Object o) {
            return o != null && o instanceof PSRole && toString().equalsIgnoreCase(o.toString());
        }

        public CategoryRole getCategory() {
            return role;
        }

        @Override
        public int compareTo(Object o) {
            return toString().compareTo(o.toString());
        }

        public Role toRole() {
            for (BuiltInRole r : BuiltInRole.values()) {
                if (r.getName().equals(getName())) return r;
            }
            return null;
        }
    }

    public static class CustomPSRole extends PSRole {
        public enum Level {
            PROJECT("Project"),
            HIGHER("Higher"),;
            String name;

            Level(String s) {
                name = s;
            }

            public String getName() {
                return name;
            }
        }

        public String getPluralName() {
            return getString("plural_name");
        }

        public void setPluralName(String s) {
            set("plural_name", s);
        }

        public void setSequence(int i) {
            set("sequence", i);
        }

        public String getSequence() {
            return getString("sequence");
        }

        public CustomPSRole(Config c) {
            super(c, null);
        }

        private ConfigurableRole role;

        public ConfigurableRole toRole() {
            if (role != null) return role;
            return role = new ConfigurableRole(this);
        }

        public void setCreated() {
            toRole().setCreated();
        }

        @Override
        public void setDeleted() {
            toRole().setDeleted();
        }

        public void addWorkTypes(WorkType... types) {
            addWorkTypes(this, types);
        }

        public Set<WorkType> getWorkTypes() {
            return getWorkTypes(this);
        }

    }
}
