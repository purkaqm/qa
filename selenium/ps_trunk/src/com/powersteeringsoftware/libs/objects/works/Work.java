package com.powersteeringsoftware.libs.objects.works;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.*;
import com.powersteeringsoftware.libs.objects.Currency;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.resources.RateTable;
import com.powersteeringsoftware.libs.objects.resources.ResourcePool;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.TimeStampName;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.util.*;

import static com.powersteeringsoftware.libs.util.session.TestSession.*;

/**
 * class for Sub Works in project central, includes Config (to load from test data config)
 */
public class Work extends ConfigObject implements TagsObject, PermissionsObject {

    public static final String RESOURCE_SPLIT_NAME = "Resource split";
    public static final String TREE_TAB = "\t";
    public static final String TREE_NEW_LINE = "\n";
    public static final String NAME = "work";
    public static final String WORK_TYPE = "type";
    protected static final String CONSTRAINT_START = START + "_" + DATE;
    protected static final String CONSTRAINT_END = END + "_" + DATE;
    protected WorkType workType;

    public enum Type implements WorkType {
        WORK("Work", true, false, null),
        MSP_PROJECT("MSP Project", true, false, null),
        // since qaauto1_12_test_140717.bak :
        NO_PROJECT("NoProject", false, false, PowerSteeringVersions._14),
        ORGANIZATION("Organization", false, true, PowerSteeringVersions._14),

        FOLDER("Folder", true, false, null),
        MILESTONE("Milestone", false, false, null),
        ACTION_ITEM("Action Item", false, false, null),;
        private String value;
        private boolean isProject;
        private boolean isOrganization;
        private PowerSteeringVersions version;

        private Type(String s, boolean _isProject, boolean _isOrganization, PowerSteeringVersions _v) {
            version = _v;
            value = s;
            isProject = _isProject;
            isOrganization = _isOrganization;
        }

        @Override
        public String getName() {
            return version == null || getVersion() == null || getVersion().verGreaterOrEqual(version) ? value : null;
        }

        public Work toWork() {
            return new Work(this, null);
        }

        public boolean isProject() {
            return isProject;
        }

        public boolean isOrganization() {
            return isOrganization;
        }
    }

    public static WorkType toWorkType(String txt) {
        for (Type t : Type.values()) {
            if (txt.equals(t.getName())) return t;
        }
        return new Template(txt);
    }

    public static List<WorkType> getBuiltInTypes() {
        List<WorkType> res = new ArrayList<WorkType>();
        for (Type t : Type.values()) {
            if (t.getName() != null)
                res.add(t);
        }
        return res;
    }

    public enum Status {
        EMPTY(""),
        PROPOSED("Proposed"),
        NOT_STARTED("Not Started"),
        ON_TRACK("On Track"),
        NEEDS_ATTENTION("Needs Attention"),
        DELAYED("Delayed"),
        OFF_TRACK("Off Track"),
        COMPLETED("Completed"),
        CANCELED("Canceled"),
        DEFERRED("Deferred"),;
        String value;

        Status(String value) {
            this.value = value;
        }

        public boolean isCompleted() {
            return equals(COMPLETED) || equals(CANCELED);
        }

        public boolean isNotActive() {
            return equals(PROPOSED) || equals(NOT_STARTED);
        }

        public boolean isActive() {
            return getActiveList().contains(this);
        }

        public String getValue() {
            return value;
        }

        public static List<Status> getList() {
            List<Status> res = new ArrayList<Status>();
            Status[] vals = values();
            for (int i = 1; i < vals.length; i++) {
                res.add(vals[i]);
            }
            return res;
        }

        public static List<Status> getActiveList() {
            return Arrays.asList(ON_TRACK, OFF_TRACK, NEEDS_ATTENTION, DELAYED);
        }

        public static List<String> getStringList() {
            List<String> res = new ArrayList<String>();
            for (Status st : getList()) {
                res.add(st.getValue());
            }
            return res;
        }

        public boolean equals(String s) {
            return value.equals(s);
        }

        public static Status get(String v) {
            for (Status s : values()) {
                if (s.getValue().equalsIgnoreCase(v)) return s;
            }
            return valueOf(v.replace(" ", "_").replace("-", "_").toUpperCase());
        }

    }

    public static final IConstraint EMPTY_CONSTRAINT = new IConstraint() {

        @Override
        public String getName() {
            return "EMPTY";
        }

        @Override
        public String getValue() {
            return "fake constraint";
        }

        public String toString() {
            return getName();
        }
    };

    public enum Constraint implements IConstraint {
        ALAP("As Late As Possible"),
        ASAP("As Soon As Possible"),
        FD("Fixed Dates"),
        MSO("Must Start On"),
        MFO("Must Finish On"),
        SNET("Start No Earlier Than"),
        SNLT("Start No Later Than"),
        FNLT("Finish No Later Than"),
        FNET("Finish No Earlier Than"),;
        String value;

        Constraint(String value) {
            this.value = value;
        }

        public String getName() {
            return toString();
        }

        public String getValue() {
            return value;
        }

        public boolean equals(String st) {
            return value.equals(st);
        }

        public static Constraint getByName(String name) {
            try {
                return valueOf(name.toUpperCase());
            } catch (Exception e) {
                return null;
            }
        }
    }

    public interface IConstraint {
        public String getName();

        public String getValue();
    }


    public enum StatusReportingFrequency {
        NO_FREQUENCY,
        WEEKLY,
        BIWEEKLY,
        MONTHLY,
        QUARTERLY
    }

    public enum Priority {
        NONE("none"),
        ONE("1 - Highest"),
        TWO("2 - High"),
        THREE("3 - Average"),
        FOUR("4 - Low"),
        FIVE("5 - Lowest");
        String value;

        Priority(String v) {
            value = v;
        }

        public String getValue() {
            return value;
        }

        public int getIndex() {
            return ordinal() + 1;
        }
    }

    public static final String NAME_SEPARATOR = ">";

    protected Set<Work> children = new LinkedHashSet<Work>();
    private List<Work> fakeList = new ArrayList<Work>();
    protected Work parent;

    public Work(Config conf) {
        super(conf);
        Config type = conf.getChByName(WORK_TYPE);
        if (type != null && type.hasXpathAttr()) { // load template if need.
            workType = new Template(type);
            merge(((Template) workType).getStructure(), false);
            setTemplateRoot(false);
        }
        loadChildren();
    }

    protected void loadChildren() {
        children.clear();
        for (Config c : conf.getChsByName(NAME)) {
            Work w = new Work(c);
            w.parent = this;
            children.add(w);
        }
    }

    public Work(String name) {
        this(Type.WORK, name);
    }

    protected Work(WorkType type, String name) {
        this(type.getName(), name);
        setType(type);
    }

    protected Work(String type, String name) {
        super();
        if (name != null)
            setName(name);
        if (type != null)
            setType(type);
    }

    public static Work create(String name, String term) {
        return new Work(term, name);
    }

    public static Work create(String name, Type term) {
        Work res = new Work(name);
        if (term != null)
            res.setType(term);
        return res;
    }

    public static Work create(String name, WorkType type, String id) {
        Work res;
        if (type instanceof Template && ((Template) type).getStructure().isGated()) {
            res = new GatedProject(name, type);
        } else {
            res = new Work(name);
            res.setType(type);
        }
        if (id != null) {
            res.setId(id);
        }
        return res;
    }

    public static Work create(String name) {
        return create(name, Type.WORK);
    }

    public static Work createOrg(String name) {
        Work w = create(name, (Type) null);
        w.setIsOrg(true);
        return w;
    }

    public static Work createOrg(String name, String id) {
        Work org = createOrg(name);
        org.setId(id);
        return org;
    }

    public static Work createEmpty(String name) {
        return create(name, (Type) null);
    }

    public static Work createTimeStamped(String name, String term) {
        return create(new TimeStampName(name).getTimeStampedName(), term);
    }

    public static Work createTimeStamped(String name) {
        return create(new TimeStampName(name).getTimeStampedName(), Type.WORK);
    }

    public String getFullName() {
        if (getParent() == null) return getName();
        return parent.getFullName() + NAME_SEPARATOR + getName();
    }

    public List<String> getPath() {
        List<String> res = new ArrayList<String>();
        if (hasParent()) {
            res.addAll(getParent().getPath());
        }
        res.add(getName());
        return res;
    }

    public int getDeep() {
        Work parent = getParent();
        if (parent == null) return 0;
        return parent.getDeep() + 1;
    }

    public int getIndent() {
        Work parent = getParent();
        if (parent == null) return 0;
        Work predecessor = getPredecessor();
        int deep = getDeep();
        int predecessorDeep = predecessor.getDeep();
        return deep - predecessorDeep;
    }

    public Integer getResourceDuration() {
        return conf.getText("duration") != null ? Integer.parseInt(conf.getText("duration")) : null;
    }

    public void setResourceDuration(int d) {
        conf.setText("duration", String.valueOf(d));
    }

    public Integer getConstraintDuration() {
        return conf.hasChild("constraint_duration") ?
                Integer.parseInt(conf.getText("constraint_duration")) : (isMilestone() ? 0 : 1);
    }

    public void setConstraintDuration(int d) {
        conf.setText("constraint_duration", String.valueOf(d));
    }

    public Float getAllocation() {
        return conf.getText("allocation") != null ? Float.parseFloat(conf.getText("allocation")) : null;
    }

    public void setAllocation(float a) {
        conf.setText("allocation", String.valueOf(a));
    }

    public Float getEffort() {
        return conf.getText("effort") != null ? Float.parseFloat(conf.getText("effort")) : null;
    }

    public boolean hasDAE() {
        return conf.hasChild("effort") || conf.hasChild("allocation") || conf.hasChild("duration");
    }

    public void setEffort(float e) {
        conf.setText("effort", String.valueOf(e));
    }

    public String getIndex() {
        return conf.getText("index");
    }

    public void setIndex(String index) {
        conf.setText(toUniqueNodes("index"), index);
    }

    private String getUrl(String type) {
        Config c = getUrlConfig(type);
        if (c == null) return null;
        String res = c.getText();
        if (!res.isEmpty())
            return res.replace(CoreProperties.getURLServerWithContext(), "");
        return null;
    }

    public void setId(String id) {
        super.setId(id);
    }

    private Config getUrlConfig(String type) {
        for (Config c : conf.getChsByName("url")) {
            if (c.getAttribute(WORK_TYPE).equals(type)) {
                return c;
            }
        }
        return null;
    }

    private void setUrl(String type, String url) {
        Config c = getUrlConfig(type);
        if (c == null) {
            c = conf.addElement(toUniqueNodes("url"));
            c.setAttributeValue(WORK_TYPE, type);
        }
        c.setText(url.replace(CoreProperties.getURLServerWithContext(), ""));
    }

    public void setWbsRRUrl(String url) {
        setUrl("wbs-rr-url", url);
    }

    public String getWbsRRUrl() {
        return getUrl("wbs-rr-url");
    }

    public void setWbsPCUrl(String url) {
        setUrl("wbs-pc-url", url);
    }

    public String getWbsPCUrl() {
        return getUrl("wbs-pc-url");
    }

    public String toString() {
        if (getName() == null)
            return "(" + getType() + ")";
        StringBuffer details = new StringBuffer();
        if (getType() != null) {
            details.append(getType());
        }
        /*if (getConstraint() != null || hasConstraintDates()) {
            details.append(getConstraint()).append(", ").append(getConstraintStartDate()).append(", ").append(getConstraintEndDate()).append(", ");
        }*/
        //details.append(", ").append(super.toString().replaceAll(".*@", ""));
        return getFullName() + (getId() != null ? "(" + getId() + ")" : "") + "[" + details.toString() + "]";
    }

    public String getType() {
        return getWorkType().getName();
    }

    public WorkType getWorkType() {
        if (workType != null) return workType;
        Config res = conf.getChByName(WORK_TYPE);
        if (res == null) { // by default work
            setType(Type.WORK);
            return workType;
        }
        String name = null;
        if (res.hasAttribute("name")) {
            name = res.getAttribute("name").toUpperCase();
        } else {
            name = res.getText();
        }
        setType(name);
        return workType;
    }

    private void setType(String name) {
        Config res = conf.getChByName(WORK_TYPE);
        if (res == null) {
            res = conf.addChild(Config.createConfig(WORK_TYPE));
        }
        try {
            workType = Type.valueOf(name.toUpperCase());
            if (!res.hasAttribute("name"))
                res.setAttributeValue("name", name);
        } catch (RuntimeException re) {
            // ignore
            workType = new Template(name);
        }
        res.setText(name);
    }

    /**
     * @param t build-in WorkType
     */
    public void setType(Type t) {
        setType((WorkType) t); // attribute for build-in types:
        conf.getChByName(WORK_TYPE).setAttributeValue("name", t.name().toLowerCase());
    }

    /**
     * @param t Template WorkType
     */
    public void setType(WorkType t) {
        workType = t;
        set(WORK_TYPE, t.getName());
    }

    public boolean isMilestone() {
        return getType() != null && getType().equalsIgnoreCase(Type.MILESTONE.getName());
        //return conf.getChByName("is_milestone") != null && conf.getText("is_milestone").equals("true");
    }

    public boolean isFolder() {
        return getType() != null && getType().equalsIgnoreCase(Type.FOLDER.getName());
    }

    public boolean isActionItem() {
        return getType() != null && getType().equalsIgnoreCase(Type.ACTION_ITEM.getName());
    }

    public PSCalendar getCalendar() {
        return super.getCalendar();
    }

    public IConstraint getDummyConstraint() {
        return _getConstraint();
    }

    protected boolean hasConstraint() {
        return conf.hasChild("constraint");
    }

    public IConstraint getConstraint() {
        return _getConstraint();
    }

    protected IConstraint _getConstraint() {
        Config c = conf.getChByName("constraint");
        if (c == null) {
            IConstraint res;
            if (hasConstraintDates()) {
                res = Constraint.FD;
            } else if (hasConstraintEnd()) {
                res = Constraint.FNLT;
            } else if (hasConstraintStart()) {
                res = Constraint.SNET;
            } else {
                if (getVersion() != null && getVersion().verGreaterOrEqual(PowerSteeringVersions._10_0) && isFolder()) {
                    res = EMPTY_CONSTRAINT;
                } else {
                    res = Constraint.ASAP;
                }
            }
            setConstraint(res);
            return res;
        }
        String value = c.getAttributeValue("name");
        if (value == null) return null;
        return Constraint.getByName(value);
    }

    public boolean isConstraintEnd() {
        IConstraint c = getConstraint();
        return c != null && (c.equals(Constraint.FD) || c.equals(Constraint.MFO) || c.equals(Constraint.FNET) || c.equals(Constraint.FNLT));
    }

    public boolean isConstraintStart() {
        IConstraint c = getConstraint();
        return c != null && (c.equals(Constraint.MSO) || c.equals(Constraint.SNET) || c.equals(Constraint.SNLT) || c.equals(Constraint.FD));
    }

    public void setConstraint(IConstraint con) {
        Config c = conf.getChByName("constraint");
        if (c == null) c = conf.addElement("constraint");
        c.setAttributeValue("name", con.getName());
    }

    protected Long _getDateAsLong(String name) {
        return super._getDateAsLong(name, true); // workaround: do not set to weekends due to #83478
    }

    public String getConstraintStartDate() {
        if (!hasConstraintStart()) return null;
        return getDate(CONSTRAINT_START);
    }

    public Long getConstraintStartDateAsLong() {
        if (!hasConstraintStart()) return null;
        return getDateAsLong(CONSTRAINT_START);
    }

    public void setConstraintStartDate(Long time) {
        if (time == null) return;
        setDate(CONSTRAINT_START, time);
    }

    public void setConstraintStartDate(String time) {
        if (time == null) return;
        PSCalendar c = getCalendar().set(time);
        c.toStart();
        setConstraintStartDate(c);
    }

    public PSCalendar setConstraintStartDate(PSCalendar date) {
        if (date != null) {
            if (getVersion() != null && getVersion().verGreaterOrEqual(PowerSteeringVersions._9_4)) {
                date = date.nextWorkDate(true); // see 82059
            }
            setConstraintStartDate(date.getTime());
        }
        return date;
    }

    public boolean hasConstraintDates() {
        return hasConstraintStart() && hasConstraintEnd();
    }

    public boolean hasConstraintStart() {
        return _hasConstrainStart() && hasDate(CONSTRAINT_START);
    }

    private boolean _hasConstrainStart() {
        return __hasConstrainDates();
    }

    private boolean __hasConstrainDates() {
        return !(getVersion() != null && getVersion().verGreaterOrEqual(PowerSteeringVersions._10_0) && isFolder());
    }

    public boolean hasConstraintEnd() {
        return _hasConstrainEnd() && hasDate(CONSTRAINT_END);
    }

    private boolean _hasConstrainEnd() {
        return __hasConstrainDates();
    }

    public boolean hasAnyConstraints() {
        return hasConstraintStart() || hasConstraintEnd();
    }

    public String getConstraintEndDate() {
        if (!hasConstraintEnd()) return null;
        return getDate(CONSTRAINT_END);
    }

    public Long getConstraintEndDateAsLong() {
        if (!hasConstraintEnd()) return null;
        return getDateAsLong(CONSTRAINT_END);
    }

    public void setConstraintEndDate(Long time) {
        if (time == null) return;
        setDate(CONSTRAINT_END, time);
    }

    public PSCalendar setConstraintEndDate(PSCalendar date) {
        if (date != null) {
            if (getVersion() != null && getVersion().verGreaterOrEqual(PowerSteeringVersions._9_4)) {
                date = date.nextWorkDate(false); // see 82059
            }
            setConstraintEndDate(date.getTime());
        }
        return date;
    }

    public void setConstraintEndDate(String time) {
        if (time == null) return;
        PSCalendar c = getCalendar().set(time);
        c.toEnd();
        setConstraintEndDate(c);
    }

    public void resetDates() {
        for (Config c : conf.getChilds()) {
            if (c.getName().contains(DATE)) {
                conf.removeChild(c);
            }
        }
        for (Work w : children) {
            w.resetDates();
        }
    }


    private Long _getProjectStartDateAsLong() { // todo? // #86091
        Long projectStart = getConstraintStartDateAsLong();
        if (getVersion() != null && getVersion().verGreaterOrEqual(PowerSteeringVersions._10_0) && projectStart == null) { // asap
            Long cd = getCreationDate();
            if (cd != null) {
                PSCalendar c = getCalendar().set(cd).nextWorkDate(true);
                c.toStart();
                projectStart = c.getTime();
            }
        }
        return projectStart;
    }

    private Long _getProjectEndDateAsLong() {
        Long projectEnd = getConstraintEndDateAsLong();
        if (getVersion() != null && getVersion().verGreaterOrEqual(PowerSteeringVersions._10_0) && projectEnd == null) { // asap
            PSCalendar c = getCalendar().set(_getProjectStartDateAsLong()).nextWorkDate(false);
            c.toEnd();
            projectEnd = c.getTime();
        }
        return projectEnd;
    }

    /**
     * @return scheduled start date
     */
    public Long getProjectStartDateAsLong() {
        Long projectStart = _getProjectStartDateAsLong();
        for (Work ch : getAllChildren()) {
            if (getVersion() != null && getVersion().verGreaterOrEqual(PowerSteeringVersions._10_0) && !Constraint.FD.equals(ch.getConstraint())) {
                continue; // #86091
            }
            Long start = ch.getConstraintStartDateAsLong();
            if (projectStart == null) projectStart = start;
            if (start != null) projectStart = Math.min(start, projectStart);
        }
        return projectStart;
    }

    /**
     * @return scheduled end date
     */
    public Long getProjectEndDateAsLong() {
        Long projectEnd = _getProjectEndDateAsLong();
        for (Work ch : getAllChildren()) {
            if (getVersion() != null && getVersion().verGreaterOrEqual(PowerSteeringVersions._10_0) && !Constraint.FD.equals(ch.getConstraint())) {
                continue; // #86091
            }
            Long end = ch.getConstraintEndDateAsLong();
            if (projectEnd == null) projectEnd = end;
            if (end != null) projectEnd = Math.max(end, projectEnd);
        }
        return projectEnd;
    }

    private void setStatus(String st) {
        conf.setText("status", st);
    }

    public synchronized void setStatus(Status st) {
        if (st == null) {
            conf.removeChild("status");
            return;
        }
        setStatus(st.getValue());
    }

    public void setOwner(User own) {
        removeUsers(BuiltInRole.OWNER);
        addUser(BuiltInRole.OWNER, own);
    }

    public User getOwner() {
        List<User> users = getUsers(BuiltInRole.OWNER);
        if (users.size() != 1) {
            removeUsers(BuiltInRole.OWNER);
            return addUser(BuiltInRole.OWNER, CoreProperties.getDefaultUser());
        }
        return users.get(0);
    }

    public User addUser(Role role, User user) {
        if (role == null) throw new IllegalArgumentException("Role for user should be specified");
        Config c = addUser(conf, user);
        c.setAttributeValue(PSPermissions.PSRole.NAME, role.getName());
        return user;
    }

    public List<User> getUsers(Role role) {
        return getUsers().get(role);
    }

    public Map<Role, List<User>> getUsers() {
        Map<Role, List<User>> res = new HashMap<Role, List<User>>();
        for (Config c : conf.getChsByName(User.NAME)) {
            String sRole = c.getAttribute(PSPermissions.PSRole.NAME);
            if (sRole == null) continue;
            Role role = BuiltInRole.getRoleByName(sRole);
            if (!res.containsKey(role)) {
                res.put(role, new ArrayList<User>());
            }
            res.get(role).add(findUser(c));
        }
        if (!res.containsKey(BuiltInRole.OWNER)) {
            User user = addUser(BuiltInRole.OWNER, BasicCommons.getCurrentUser());
            List<User> list = new ArrayList<User>();
            list.add(user);
            res.put(BuiltInRole.OWNER, list);
        }
        return res;
    }

    public boolean hasUsersToAssign() {
        Map<Role, List<User>> res = getUsers();
        if (res.size() > 1) return true;
        // todo: vvv ???
        return !res.get(BuiltInRole.OWNER).get(0).equals(BasicCommons.getCurrentUser());
    }

    public synchronized boolean hasStatus() {
        return getConfig().hasChild("status");
    }

    public boolean hasPriority() {
        return !getPriority().equals(Priority.NONE);
    }

    public boolean hasSecondCreateWorkPage() {
        return hasStatus() || hasPriority() || hasAnyConstraints() || hasUsersToAssign();
    }

    public void removeUsers(Role role) {
        if (role == null) throw new IllegalArgumentException("Role should be specified");
        for (Config c : conf.getChsByName(User.NAME)) {
            if (c.getAttribute(PSPermissions.PSRole.NAME).equals(role.getName())) {
                conf.removeChild(c);
            }
        }
    }

    public void removeUser(Role role, User user) {
        for (Config c : conf.getChsByName(User.NAME)) {
            if (c.getAttribute(PSPermissions.PSRole.NAME).equals(role.getName()) && user.equals(findUser(c))) {
                conf.removeChild(c);
                return;
            }
        }
    }

    public void clearUsers() {
        conf.removeChildren(User.NAME);
    }

    private Need need;

    public Need getResource() {
        return getResource(false);
    }

    public Need getResource(boolean isPlanResources) {
        initResource();
        if (isPlanResources && hasSplitResources()) {
            return new Need(null);
        }
        if (isSplitResource() && getResourceRowIndex() == 1) {
            return parent.need;
        }
        return need;
    }

    public List<Need> getResources() {
        List<Need> res = new ArrayList<Need>();
        if (!hasSplitResources()) {
            res.add(getResource());
        } else {
            for (Work w : getSplitResources()) {
                res.add(w.getResource());
            }
        }
        return res;
    }

    private void initResource() {
        if (need == null) {
            Config _conf = getConfig().getChByName("need");
            if (_conf == null) _conf = getConfig().addElement("need");
            need = new Need(_conf);
        }
    }

    public enum CalculatedField {
        DURATION,
        ALLOCATION,
        EFFORT,
    }
    public class Need {
        private Config _res;
        private User person;
        private Cost cost;

        private Need(Config c) {
            _res = c;
        }

        public boolean isEmpty() {
            return _res.getChilds().isEmpty();
        }

        public User getPerson() {
            if (_res == null) return null;
            if (person != null) return person;
            return person = findUser(_res.getChByName(User.NAME));
        }

        public void setPerson(User user) {
            person = user;
            removePerson();
            addUser(_res, user);
            if (!hasRole()) {
                setRole(BuiltInRole.CONTRIBUTOR); //?
            }
            Cost cost = getCost();
            if (cost != null)
                cost.setResource(user);
        }

        public void removePerson() {
            person = null;
            _res.removeChild(User.NAME);
        }

        public void setPool(ResourcePool pool) {
            _res.setText(ResourcePool.NAME, pool.getConfigId());
        }

        public ResourcePool getPool() {
            if (_res == null) return null;
            String id = _res.getText(ResourcePool.NAME);
            for (ResourcePool rp : TestSession.getResourcePoolList()) {
                if (rp.getConfigId().equals(id)) return rp;
            }
            return null;
        }

        public void removePool() {
            _res.removeChild("pool");
        }

        public boolean hasPool() {
            return _res.hasChild("pool");
        }

        public void setRole(Role role) {
            _res.setText(PSPermissions.PSRole.NAME, role.getName());
            Cost cost = getCost();
            if (cost != null) {
                cost.setResource(role);
                return;
            }
            cost = initCost(System.currentTimeMillis());
            if (cost == null) return;
            cost.setCreated();
            cost.setResource(role);
            _res.addChild(cost.getConfig());
            this.cost = cost;
        }

        public boolean hasRole() {
            return _res.hasChild(PSPermissions.PSRole.NAME);
        }

        public void removeRole() {
            _res.removeChild(PSPermissions.PSRole.NAME);
        }

        public Role getRole() {
            if (_res == null) return null;
            String res = _res.getText(PSPermissions.PSRole.NAME);
            if (res == null || res.isEmpty()) return null;
            return BuiltInRole.getRoleByName(res);
        }

        public String toString() {
            return "Need(" + getRole() + "," + getPerson() + "," + getPool() + ")";
        }

        public Cost getCost() {
            return cost != null ? cost : _res != null && _res.hasChild(Cost.NAME) ? cost = Cost.getCost(_res.getChByName(Cost.NAME), isSplitResource() ? getParent() : Work.this) : null;
        }

        private Cost initCost(long date) {
            if (_res == null) return null;
            if (getRole() == null) return null;

            PSCalendar calendar = getCalendar();
            PSCalendar psDate = calendar.set(date).nextWorkDate(true); // todo: only work days.
            int day = psDate.getDayOfWeek();
            int hours = calendar.getHoursPerDay().get(day);
            RateTable table = getRateTable();
            if (table == null || table.isFake()) return null;
            RateTable.Rate rate = null;
            RateTable.Rate def = null;
            for (RateTable.Rate r : table.getRates()) {
                if (r.isDefault()) def = r;
                if (!r.getPSDate().lessOrEqual(psDate)) continue;
                if (r.getRole().equals(getRole())) {
                    rate = r;
                    break;
                }
            }
            if (rate == null) {
                if (def == null)
                    return null;
                rate = def;
            }
            Double amount = rate.getCode().getAmount();
            amount *= hours / getCurrency().getExchangeRate(); //?

            Cost cost = new Cost(Cost.Type.ESTIMATED, amount, null, psDate.getTime());
            cost.setWork(isSplitResource() ? getParent() : Work.this);
            cost.setEndDate(psDate.getTime());
            cost.setAddType(Cost.AddType.LABOR);
            return cost;
        }
    }


    public boolean getCriticalPath() {
        //todo: this is true only for current project, not in past (should be CoreProperties.getTestTemplate() >= System.currentTimeMillis())
        //todo: do not use config, implement logic
        return getBooleanFalse("critical_path");
    }

    public void setCriticalPath(boolean path) {
        set("critical_path", path);
    }

    public void setManualScheduling(boolean flag) {
        conf.setText("manual_scheduling", String.valueOf(flag));
    }

    /**
     * this is for 9.3 and greater
     */
    public boolean isConstraintsDisabled() {
        if (getVersion() == null) return false;
        if (getVersion().verGreaterOrEqual(PowerSteeringVersions._9_3)) {
            if ((getParent() != null && getParent().getManualSchedulingForDependencies())) {
                if (isMilestone()) {
                    return true;
                }
            }
            if (getVersion().verGreaterOrEqual(PowerSteeringVersions._10_0)) {
                // see #85854
                if (isFolder()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setPlanResources(boolean flag) {
        set("plan_resources", flag);
    }

    /**
     * @return by default true for 9.2 and false for 9.3.
     */
    public boolean getManualScheduling() {
        if (getVersion() != null && getVersion().verLessThan(PowerSteeringVersions._9_3)) {
            return getBooleanTrue("manual_scheduling");
        }
        return getBooleanFalse("manual_scheduling");
    }

    /**
     * Warning!
     * this is 'Manual schedule dependent works'. for 9.3 or latter.
     * by default true.
     *
     * @return
     */
    public boolean getManualSchedulingForDependencies() {
        return getBooleanTrue("manual_scheduling_dependencies");
    }

    public void setManualSchedulingForDependencies(boolean b) {
        set("manual_scheduling_dependencies", b);
    }

    public boolean getPlanResources() {
        return getBooleanFalse("plan_resources");
    }

    public Boolean getInheritPermissions() {
        return getBooleanFalse("inherit_permissions");
    }

    public void setInheritControls(boolean c) {
        set("inherit_controls", c);
    }

    public boolean getInheritControls() {
        if (!getConfig().hasChild("inherit_controls")) {
            setInheritControls(hasParent());
        }
        return getBoolean("inherit_controls");
    }

    public boolean getControlCost() {
        return getBooleanFalse("control_cost");
    }

    public void setControlCost(boolean c) {
        set("control_cost", c);
    }

    public Boolean getWebFolder() {
        return getBoolean("web_folder");
    }

    private String getStatusString() {
        return getString("status");
    }

    public int getPriorityInt() {
        Integer res = getInteger("priority");
        return res == null ? 0 : res;
    }

    public void setPriority(Priority p) {
        set("priority", p.getIndex());
    }

    public Status getStatus() {
        return getStatus(true);
    }

    public synchronized Status getStatus(boolean includeDef) {
        String st = getStatusString();
        if (st == null || st.isEmpty()) return includeDef ? getDefStatus() : null;
        return Status.get(st);
    }

    private Status getDefStatus() {
        return isFolder() ? Status.EMPTY : Status.PROPOSED;
    }

    public Priority getPriority() {
        int p = getPriorityInt();
        if (p == 0) return Priority.NONE;
        for (Priority pr : Priority.values()) {
            if (p == pr.ordinal()) return pr;
        }
        return Priority.NONE;
    }

    public void setChildren(List<String> sChilds) {
        for (String ch : sChilds) {
            Work chw = new Work((String) null, ch);
            chw.parent = this;
            children.add(chw);
        }
    }

    public void addChild(Work ch) {
        if (ch.isFake()) {
            fakeList.add(ch);
            return;
        }
        ch.parent = this;
        children.add(ch);
    }

    public void addChildren(List<Work> chs) {
        for (Work ch : chs) {
            addChild(ch);
        }
    }

    public void removeChild(Work work) {
        if (work.isFake()) {
            fakeList.remove(work);
            return;
        }
        work.setDeleted();
        children.remove(work);
        conf.removeChild(work.conf);
    }

    public static List<String> getListTree(Work root, boolean includeAll, boolean includeFake, boolean checkExist) {
        List<String> res = new ArrayList<String>();
        List<Work> works;
        if (includeAll) {
            works = root.getAllChildren(includeFake, checkExist);
        } else {
            works = root.getChildren(includeFake, checkExist);
        }
        res.add(root.getName());
        for (Work w : works) {
            res.add(w.getName());
        }
        return res;
    }

    public List<Work> getChildren() {
        return getChildren(false, false);
    }

    public List<Work> getExistChildren() {
        return getChildren(false, true);
    }

    public List<Work> getChildren(boolean includeFake, boolean checkExist) {
        List<Work> res = new ArrayList<Work>();
        if (includeFake) {
            for (Work work : fakeList) {
                res.add(work);
            }
        }
        for (Work work : children) {
            if (!work.hasParent()) {
                work.parent = this;
            }
            if (checkExist && !work.exist()) continue;
            res.add(work);
        }
        return res;
    }

    public List<Work> getMilestones() {
        List<Work> res = new ArrayList<Work>();
        for (Work w : getChildren()) {
            if (w.isMilestone()) res.add(w);
        }
        return res;
    }

    public boolean hasChildren() {
        return children.size() != 0;
    }

    public boolean hasDescendants() {
        for (Work w : children) {
            if (w.exist()) return true;
        }
        return false;
    }

    public Work getChild(int index) {
        if (children.size() <= index) return null;
        return getChildren().get(index);
    }

    public Work getChild(String name) {
        for (Work w : getAllChildren()) {
            if (w.getName().equals(name)) return w;
        }
        return null;
    }

    public List<Work> getAllChildren(boolean includeFake, boolean checkExist) {
        List<Work> res = new ArrayList<Work>();
        putToList(res, this, includeFake, checkExist);
        return res;
    }

    public List<Work> getAllChildren() {
        return getAllChildren(false, false);
    }

    public Work[] getAllChildrenArray() {
        return getAllChildren().toArray(new Work[]{});
    }

    public void replaceChildren(int i, int j) {
        if (children.size() <= i || children.size() <= j)
            throw new IllegalArgumentException("Incorrect indexes specified");
        List<Work> chs = getChildren();
        Work w1 = chs.get(i);
        Work w2 = chs.get(j);
        chs.set(i, w2);
        chs.set(j, w1);
        children.clear();
        children.addAll(chs);
    }

    public void insertAfter(Work w) {
        Work parent = getParent();
        if (parent == null) return;
        List<Work> chs = parent.getChildren();

        int j = chs.indexOf(w);
        if (j != -1) chs.remove(j);

        int i = chs.indexOf(this);
        chs.add(i + 1, w);

        parent.children.clear();
        parent.children.addAll(chs);
    }

    public void insertAfter(Work... list) {
        for (int i = list.length; i > 0; i--) {
            insertAfter(list[i - 1]);
        }
    }

    public Work getPredecessor() {
        List<Work> all = getParent().getAllChildren();
        int ind = all.indexOf(this);
        if (ind == 0) return parent;
        return all.get(ind - 1);
    }

    public Work getPredecessorSibling() {
        List<Work> all = getParent().getChildren();
        int ind = all.indexOf(this);
        if (ind == 0) return null;
        return all.get(ind - 1);
    }

    private static void putToList(List<Work> works, Work work, boolean includeFake, boolean checkExist) {
        for (Work ch : work.getChildren(includeFake, checkExist)) {
            works.add(ch);
            putToList(works, ch, includeFake, checkExist);
        }
    }

    private static String print(Work rootWork, String tab, String newLine, boolean includeFake, boolean checkExist) {
        StringBuffer sb = new StringBuffer();
        sb.append(rootWork.getName());
        sb.append(newLine);
        for (Work ch : rootWork.getChildren(includeFake, checkExist)) {
            sb.append(tab);
            sb.append(print(ch, tab + tab, newLine, includeFake, checkExist));
        }
        return sb.toString();
    }

    public String print(boolean includeFake, boolean checkExist) {
        return TREE_NEW_LINE + print(this, TREE_TAB, TREE_NEW_LINE, includeFake, checkExist).trim();
    }

    public String print() {
        return print(false, true);
    }

    public int getGeneralIndex() {
        String gi = conf.getText("general_index");
        if (gi != null)
            return Integer.valueOf(gi);
        return -1;
    }

    public int getRowIndex() {
        String gi = conf.getText("row_index");
        if (gi != null)
            return Integer.valueOf(gi);
        return -1;
    }

    public int getResourceRowIndex() {
        String gi = conf.getText("resource_row_index");
        if (gi != null)
            return Integer.valueOf(gi);
        return -1;
    }

    public void setGeneralIndex(int generalIndex) {
        conf.setText(toUniqueNodes("general_index"), String.valueOf(generalIndex));
    }

    public void setRowIndex(int rowIndex) {
        conf.setText(toUniqueNodes("row_index"), String.valueOf(rowIndex));
    }

    public void setResourceRowIndex(int rowIndex) {
        conf.setText(toUniqueNodes("resource_row_index"), String.valueOf(rowIndex));
    }

    /**
     * @return true if exist (by default)
     */
    public boolean exist() {
        return !hasExist() && hasParent() ? getRoot().exist() : super.exist();
    }

    public void setCreated() {
        super.setCreated();
        putWork(this);
    }

    public void setChildrenCreated() {
        for (Work w : getAllChildren()) {
            w.setCreated();
        }
    }

    public void setChildrenDeleted() {
        for (Work w : children) {
            w.setDeleted();
        }
    }

    public void setDeleted() {
        super.setDeleted();
        removeWork(this);
        for (Work c : children) {
            c.setDeleted();
        }
    }

    public boolean isGated() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Work)) return false;
        Work w = (Work) o;
        if (getId() != null && w.getId() != null) {
            return getId().equals(w.getId());
        }
        if (getFullName().equals(w.getFullName())) {
            return true;
        }
        /*// compare configs:
        List<String> txtChilds = new ArrayList<String>();
        for (Config c : w.getThisConfig().getChilds()) {
            txtChilds.add(c.getText());
        }
        List<Config> confChilds = getThisConfig().getChilds();
        if (confChilds.size() != txtChilds.size()) return false;
        for (Config c : confChilds) {
            if (!txtChilds.contains(c.getText())) {
                return false;
            }
        }*/
        return conf.equals(w.getConfig());
    }

    @Override
    public int hashCode() {
        if (getId() != null) return getId().hashCode();
        return super.hashCode();
    }

    public void setParent(Work parent) {
        this.parent = parent;
        this.parent.children.add(this);
    }

    public Work getParent() {
        if (parent == null && conf.getParent() != null && conf.getParent().getName().equals(NAME)) {
            parent = new Work(conf.getParent());
        }
        return parent;
    }

    public boolean hasParent() {
        return getParent() != null;
    }

    public String getParentName() {
        return getParent() == null ? "" : parent.getName();
    }

    public boolean hasSummary() {
        return true;
    }

    public Work getRoot() {
        if (getParent() == null) return this;
        return getParent().getRoot();
    }

    public Work copy() {
        Work res = (Work) super.copy();
        if (workType != null)
            res.setType(workType);
        res.resetDates();
        for (Work w : res.getAllChildren()) {
            w.removeUniqueNodes();
        }
        res.setDeleted();
        if (isGated()) {
            res = new GatedProject(res.conf);
        }
        if (!res.hasChildren()) {
            for (Work w : getChildren()) {
                res.addChild(w.copy());
            }
        }
        return res;
    }

    public List<Work> splitResources() {
        List<Work> before = getSplitResources();
        List<Work> res = new ArrayList<Work>();
        if (before.size() == 0) {
            res.add(initSplitResource(1));
            res.add(initSplitResource(2));
        } else {
            res.add(initSplitResource(before.size() + 1));
        }
        PSLogger.debug("Splitted Resources : " + res);
        return res;
    }

    public boolean isSplitResource() {
        return this instanceof SplitResource;
    }

    public boolean hasSplitResources() {
        for (Work w : fakeList) {
            if (w.isSplitResource()) return true;
        }
        return false;
    }

    public List<Work> getSplitResources() {
        List<Work> res = new ArrayList<Work>();
        for (Work w : fakeList) {
            if (w.isSplitResource()) res.add(w);
        }
        return res;
    }

    public List<Role> getResourceRoles() {
        List<Role> res = new ArrayList<Role>();
        for (Work s : getSplitResources()) {
            Role r = s.getResource().getRole();
            if (r != null)
                res.add(r);
        }
        return res;
    }

    public List<ResourcePool> getResourcePools() {
        List<ResourcePool> res = new ArrayList<ResourcePool>();
        for (Work s : getSplitResources()) {
            ResourcePool p = s.getResource().getPool();
            if (p != null)
                res.add(p);
        }
        return res;
    }

    public Work initSplitResource(int index) {
        Work res = new SplitResource(this, index);
        res.setRowIndex(getRowIndex());
        res.setResourceRowIndex(index);
        res.setConstraintEndDate(getConstraintEndDateAsLong());
        res.setConstraintStartDate(getConstraintStartDateAsLong());
        return res;
    }

    public class SplitResource extends Work {

        private SplitResource(Work parent, int index) {
            super(RESOURCE_SPLIT_NAME);
            setId("#" + index);
            this.parent = parent;
        }

        public boolean isFake() {
            return true;
        }

        public void setCreated() {
        }

        public void setDeleted() {
        }

        public boolean exist() {
            return false;
        }

        public Currency getCurrency() {
            return getParent().getCurrency();
        }

        public PSCalendar getCalendar() {
            return getParent().getCalendar();
        }

    }

    public boolean isFake() {
        return false;
    }

    public List<PSCalendar> getWeekPeriods() {
        return getWeekPeriods(getCalendar());
    }

    public List<Integer> getWeekDurations(PSCalendar calendar) {
        List<Integer> res = new ArrayList<Integer>();
        String startDate = getConstraintStartDate();
        String endDate = getConstraintEndDate();
        if (startDate == null || endDate == null) return res;
        PSCalendar start = calendar.set(startDate);
        PSCalendar end = calendar.set(endDate);
        List<PSCalendar> periods = getWeekPeriods();
        if (periods.size() == 1) {
            res.add(start.getPeriodHours(end));
        } else {
            res.add(start.getPeriodHours(periods.get(1).set(-1)));
            for (int i = 1; i < periods.size() - 1; i++) {
                periods.get(i).setConfig(calendar.getConfig());
                res.add(periods.get(i).getPeriodHours(periods.get(i + 1).set(-1)));
            }
            res.add(end.getPeriodHours(periods.get(periods.size() - 1)));
        }
        return res;
    }

    private List<PSCalendar> getWeekPeriods(PSCalendar calendar) {
        String startDate = getConstraintStartDate();
        String endDate = getConstraintEndDate();
        List<PSCalendar> periods = new ArrayList<PSCalendar>();
        if (startDate == null || endDate == null) return periods;
        Integer[] week = calendar.getWeekDays();
        PSCalendar start = calendar.set(startDate);
        PSCalendar end = calendar.set(endDate);
        PSCalendar periodStart = start.getStartWeekDay();
        periods.add(periodStart);
        while (periods.get(periods.size() - 1).getEndWeekDay().less(end)) {
            periods.add(periods.get(periods.size() - 1).set(week.length));
        }
        return periods;
    }

    public void setHoursInWeekPeriod(PSCalendar period, String hours) {
        Config week = conf.getElementById(period.toString());
        if (week == null) {
            week = conf.addElement("week");
            week.setAttributeValue("id", period.toString());
        }
        week.setText(hours);
    }

    public String getHoursInWeekPeriod(PSCalendar period) {
        Config week = conf.getElementById(period.toString());
        if (week == null) return null;
        return week.getText();
    }

    public static void sortByRowIndex(List<Work> works) {
        Collections.sort(works, new Comparator() {
            public int compare(Object o1, Object o2) {
                int index1 = ((Work) o1).getRowIndex();
                int index2 = ((Work) o2).getRowIndex();
                if (index1 == index2)
                    return 0;
                if (index1 > index2)
                    return 1;
                return -1;
            }
        });
    }


    public StatusReportingFrequency getStatusReporting() {
        if (!conf.hasChild("status-reporting")) return null;
        String s = conf.getText("status-reporting");
        return StatusReportingFrequency.valueOf(s.toUpperCase().replace("-", "_"));
    }


    /**
     * see method isUseAdvancedDatesComponent in
     * https://svn.cinteractive.com/cgi-bin/trac.cgi/browser/product/trunk/src/ps5/psapi/project/create/CreateWorkWizard.java
     *
     * @return true if there are selectors for each child, false if there are selectors for parent only
     */
    public boolean isUseAdvancedDatesComponent() {
        return false;
    }

    public boolean skipDates() {
        return false;
    }

    public boolean isPlannedEndRequired() {
        WorkType type = getWorkType();
        if (type instanceof Template) {
            return ((Template) type).getIsRequireGateEndDates();
        }
        return false;
    }

    public boolean isTemplateRoot() {
        return getBooleanFalse("is_template_root");
    }

    public boolean isInTemplate() {
        if (!hasParent()) return isTemplateRoot();
        return getParent().isInTemplate();
    }

    public void setTemplateRoot(boolean x) {
        set("is_template_root", x);
    }

    public List<PSTag> getTags() {
        return super.getTags();
    }

    public void setTags(PSTag... tags) {
        super.setTags(tags);
    }

    public void addTag(PSTag tg) {
        super.addTag(tg);
    }

    public void setTags(List<PSTag> tags) {
        super.setTags(tags);
    }

    public void setPermissions(PSPermissions perm) {
        super.setPSPermissions(perm);
    }

    public PSPermissions getPermissions() {
        return super.getPSPermissions();
    }

    @Override
    public boolean canView(User user) {
        // todo: maybe this method works incorrect in some case.
        return super.canView(user) ||
                hasPermission(TestSession.getPermissions().get(PSPermissions.BasicTarget.WI), user, PSPermissions.Verb.VIEW) ||
                hasPermission(getPSPermissions(), TestSession.getPermissions().get(PSPermissions.BasicTarget.WI), user, PSPermissions.Verb.VIEW) ||
                (hasParent() && getParent().canView(user));
    }

    private boolean hasPermission(PSPermissions perms, User user, PSPermissions.Verb verb) {
        return hasPermission(perms, null, user, verb);
    }

    private boolean hasPermission(PSPermissions perms, PSPermissions general, User user, PSPermissions.Verb verb) {
        if (perms == null) return false;
        for (PSPermissions.CategoryRole category : perms.getCategoryRoles()) {
            if (!category.isCoreSet()) {
                if (category.getUsers().contains(user)) { // this user is assigned in permissions for this object
                    return hasPermission(category, verb) || (general != null && hasPermission(general.getCategory(category.getName()), verb));
                }
            } else {
                //todo:
                for (PSPermissions.PSRole r : category.getRoles()) {
                    //if (PSPermissions.CustomRole.Level.HIGHER.getName().equals(r.getLevel()))
                    //    continue; // todo: what should we do with levels?
                    if (getUsers(r.toRole()).contains(user)) { // this user is assigned to object
                        return hasPermission(category, verb) || (general != null && hasPermission(general.getCategory(category.getName()), verb));
                    }
                }
            }
        }
        return false;
    }

    private boolean hasPermission(PSPermissions.CategoryRole category, PSPermissions.Verb verb) {
        for (PSPermissions.CategoryRole.Permission p : category.getPermissions()) {
            if (verb.name().equalsIgnoreCase(p.getName())) {
                return p.getValue();
            }
        }
        return false;
    }


    public boolean isBrokenDates() {
        return false;
    }

    public void setDatesFixed() {
        // empty
    }

    public boolean isDatesFixed() {
        return false;
    }

    public void addCost(Cost cost) {
        PSLogger.debug(getName() + ": add cost " + cost);
        Config costs = conf.getChByName("costs");
        if (costs == null) costs = conf.addElement("costs");
        costs.addChild(cost.getConfig());
        cost.setWork(this);
        cost.setCreated();
    }

    public void removeCost(Cost cost) {
        Config costs = conf.getChByName("costs");
        if (costs == null) return;
        for (Config c : costs.getChilds()) {
            if (Cost.getCost(c, this).equals(cost)) {
                costs.removeChild(c);
                return;
            }
        }
    }

    public List<Cost> getCosts() {
        return getCosts(null);
    }

    public List<Cost> getCosts(Cost.Type type) {
        if (!getControlCost()) return Collections.emptyList();
        Config costs = conf.getChByName("costs");
        if (costs == null) return Collections.emptyList();
        List<Cost> res = new ArrayList<Cost>();
        for (Config c : costs.getChilds()) {
            Cost _c = Cost.getCost(c, this);
            _c.setWork(this);
            if (type == null || _c.getType().equals(type))
                res.add(_c);
        }
        return res;
    }

    public Currency getCurrency() {
        String name = getConfig().getText(Currency.NAME);
        for (Currency c : getCurrencyList()) {
            if (name == null && c.isDefault()) return c;
            if (c.getName().equals(name)) return c;
        }
        throw new IllegalStateException("Config is incorrect. Can't find currency '" + name + "'");
    }

    public void setCurrency(Currency c) {
        super.setCurrency(c);
    }

    public RateTable getRateTable() {
        String name = getConfig().getText(RateTable.NAME);
        for (RateTable t : getRateTableList()) {
            if (name == null && t.isDefault()) return t;
            if (t.getName().equals(name)) return t;
        }
        throw new IllegalStateException("Config is incorrect. Can't find rate-table '" + name + "'");
    }

    public void setRateTable(RateTable tb) {
        getConfig().setText(RateTable.NAME, tb.getName());
    }

    public boolean isOrg() {
        return getBooleanFalse("is-org");
    }

    public void setIsOrg(boolean v) {
        set("is-org", v);
    }

    public boolean isWork() {
        return !isOrg();
    }
}
