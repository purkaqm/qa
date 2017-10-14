package com.powersteeringsoftware.libs.objects.works;

import com.powersteeringsoftware.libs.objects.BuiltInRole;
import com.powersteeringsoftware.libs.objects.PSProcess;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests_data.Config;

import java.util.ArrayList;
import java.util.List;

public class GatedProject extends Work {

    public static final WorkType GATE = new WorkType() {
        @Override
        public String getName() {
            return "Gate";
        }
    };

    public static final IConstraint ESGP_CONSTRAINT = new IConstraint() {
        // its for 9.3 or later
        @Override
        public String getName() {
            return "ESG";
        }

        @Override
        public String getValue() {
            return "Enforce Sequential Gates";
        }

        public String toString() {
            return getName();
        }
    };
    private boolean skipDates;

    public GatedProject(String name, String term) {
        super(term, name);
    }

    public GatedProject(String name, WorkType term) {
        super(term, name);
        skipDates = true;
    }

    public GatedProject(String name, WorkType term, boolean esg) {
        super(term, name);
        set("enforce_sequential", esg);
        skipDates = true;
    }

    public GatedProject(Config c) {
        super(c);
    }

    protected void loadChildren() {
        children.clear();
        for (Config c : conf.getChsByName(NAME)) {
            Work w = new Gate(c);
            w.parent = this;
            children.add(w);
        }
    }

    public static GatedProject create(String name, String term) {
        return new GatedProject(name, term);
    }

    public void addChampion(User user) {
        addUser(BuiltInRole.CHAMPION, user);
    }

    public List<User> getChampionsList() {
        return getUsers(BuiltInRole.CHAMPION);
    }

    public PSProcess getProcess() {
        if (!conf.hasChild(PSProcess.NAME)) {
            if (getWorkType() != null && getWorkType() instanceof Template) {
                PSProcess pc = ((GatedProject) ((Template) getWorkType()).getStructure()).getProcess();
                conf.addChild(pc.getConfig());
                return pc;
            } else {
                return null;
            }
        }
        return new PSProcess(conf.getChByName(PSProcess.NAME));
    }

    public void setProcess(PSProcess pc) {
        if (conf.hasChild(PSProcess.NAME)) {
            conf.removeChild(PSProcess.NAME);
        }
        conf.addChild(pc.getConfig());
    }


    public boolean getManualScheduling() {
        return getBooleanFalse("manual_scheduling");
    }

    public boolean getEnforceSequential() {
        return getBooleanTrue("enforce_sequential");
    }

    public List<Work> getChildren() {
        if (children.size() == 0) {
            PSProcess proc = getProcess();
            if (proc == null) return null;
            for (PSProcess.Phase ps : proc.getPhasesList()) {
                Gate g = new Gate(ps);
                children.add(g);
                conf.addChild(g.getConfig());
            }
            setSGPDates();
        }

        List<Config> gates;
        while ((gates = conf.getChsByName("gate")).size() != 0) {
            Work _new = new Work(gates.get(0));
            for (Work w : children) {
                if (w.getName().equals(_new.getName())) {
                    w.merge(_new);
                    break;
                }
            }
            conf.removeChild("gate");
            setSGPDates();
        }
        return super.getChildren();
    }

    public Gate getLastGate() {
        List<Work> chs = getChildren();
        return (Gate) chs.get(chs.size() - 1);
    }

    public Gate getFirstGate() {
        return (Gate) getChildren().get(0);
    }

    public class Gate extends Work {
        private Boolean isFirst;

        private Gate(PSProcess.Phase phase) {
            super(GATE, phase.getName());
        }

        private Gate(Config c) {
            super(c);
        }

        @Override
        public GatedProject getParent() {
            if (parent == null)
                parent = GatedProject.this;
            return (GatedProject) parent;
        }

        @Override
        public boolean isGated() {
            return true;
        }

        public boolean isFirst() {
            if (isFirst == null) {
                isFirst = getParent().getChildren().get(0).equals(this);
            }
            return isFirst;
        }

        protected void setDate(String name, Long ed) {
            _setDate(name, ed);
            if (!getEnforceSequential()) return;
            List<Work> chs = getParent().getChildren();
            if (chs.get(0).equals(this) && name.contains(START)) { // if start date for first gate
                GatedProject.super.setDate(name, ed);
            }
            if (chs.get(chs.size() - 1).equals(this) && name.contains(END)) { // if end date for last gate
                GatedProject.super.setDate(name, ed);
            }
            setSGPDates();
        }

        private void _setDate(String name, Long ed) {
            super.setDate(name, ed);
        }


        public IConstraint getConstraint() {
            if (isSGP()) {
                return ESGP_CONSTRAINT;
            }
            return super.getConstraint(); // asap even if tollgate has fd.
        }

        public void setConstraint(IConstraint c) {
            if (isSGP()) {
                throw new UnsupportedOperationException("Can't set constraint to gate");
            }
            super.setConstraint(c);
        }

        public boolean isConstraintEnd() {
            return isSGP() || super.isConstraintEnd();
        }

        public boolean isConstraintStart() {
            if (isSGP()) return isFirst();
            return super.isConstraintStart();
        }

        public boolean hasSummary() {
            return false;
        }

    }

    public boolean skipDates() {
        return skipDates;
    }

    private static PSCalendar _getPSDate(Work w, String name) {
        Long ed = w._getDateAsLong(name);
        if (ed == null) return null;
        return w.getCalendar().set(ed);
    }

    private void setSGPDates() {
        if (!getEnforceSequential()) return;
        PSCalendar start = _getPSDate(this, CONSTRAINT_START);
        PSCalendar end = _getPSDate(this, CONSTRAINT_END);
        if (start == null || end == null) return;
        List<Work> chs = new ArrayList<Work>(children);

        Gate first = (Gate) chs.get(0);
        Gate last = (Gate) chs.get(chs.size() - 1);
        PSCalendar s0 = start; //.nextWorkDate(true);
        PSCalendar e0 = start.nextWorkDate(true);
        e0.toEnd();
        first._setDate(CONSTRAINT_START, s0.getTime());
        if (!first.hasConstraintEnd() && (getVersion() == null || getVersion().verGreaterThan(PowerSteeringVersions._9_2))) {
            first._setDate(CONSTRAINT_END, e0.getTime());
        }

        PSCalendar s1 = end.nextWorkDate(false);
        PSCalendar e1 = end; //.nextWorkDate(false);
        s1.toStart();
        last._setDate(CONSTRAINT_END, e1.getTime());
        //if (!last.hasConstraintStart()) {
        //    last._setDate(CONSTRAINT_START, s1.getTime());
        //}
        if (getVersion() != null && getVersion().verLessOrEqual(PowerSteeringVersions._9_2)) {
            return; // do not set dates for gates
        }
        for (int i = 1; i < chs.size(); i++) {
            //for (int i = 1; i < chs.size() - 1; i++) {
            Work prev = chs.get(i - 1);
            Work next = i != chs.size() - 1 ? chs.get(i + 1) : null;
            Gate _this = (Gate) chs.get(i);
            PSCalendar si = _getPSDate(prev, CONSTRAINT_END).set(1).nextWorkDate(true);
            si.toStart();
            if (!_this.hasConstraintStart()) {
                _this._setDate(CONSTRAINT_START, si.getTime());
            }
            PSCalendar ei;
            if (next != null && next.hasConstraintStart()) {
                ei = _getPSDate(next, CONSTRAINT_START).set(-1).nextWorkDate(false);
            } else {
                ei = si;
            }
            ei.toEnd();
            if (!_this.hasConstraintEnd()) {
                _this._setDate(CONSTRAINT_END, ei.getTime());
            }
        }
    }

    protected void setDate(String name, Long ed) {
        if (!getEnforceSequential()) {
            super.setDate(name, ed);
        } else if (name.contains(START)) {
            getFirstGate().setDate(name, ed);
        } else if (name.contains(END)) {
            getLastGate().setDate(name, ed);
        }
    }

    @Override
    public boolean isGated() {
        return true;
    }

    /**
     * see method isUseAdvancedDatesComponent in
     * https://svn.cinteractive.com/cgi-bin/trac.cgi/browser/product/trunk/src/ps5/psapi/project/create/CreateWorkWizard.java
     *
     * @return true if there are selectors for each child, false if there are selectors for parent only
     */
    public boolean isUseAdvancedDatesComponent() {
        boolean hasFD = false; // at least one gate has FD
        for (Work c : getChildren())
            hasFD |= Constraint.FD == c.getConstraint();

        if (getVersion() != null && getVersion().verGreaterOrEqual(PowerSteeringVersions._9_3)) {
            return getManualScheduling() || hasFD || isPlannedEndRequired();
        }
        return hasFD || (getEnforceSequential() && isPlannedEndRequired());
    }


    public IConstraint getConstraint() {
        if (isSGP()) {
            return ESGP_CONSTRAINT;
        }
        return _getConstraint();
    }

    public IConstraint getDummyConstraint() {
        // todo: hotfix for Add Under/After
        PowerSteeringVersions ver = getVersion();
        if (ver == null) return _getConstraint();
        if (isSGP() && !hasConstraint()) {
            if (ver.verLessOrEqual(PowerSteeringVersions._9_2)) return Constraint.FD;
            if (ver.verGreaterOrEqual(PowerSteeringVersions._9_3)) return ESGP_CONSTRAINT;
            return Constraint.FNLT; // for 9.3
        } else if (ver.verGreaterOrEqual(PowerSteeringVersions._9_3)) {
            return getManualScheduling() ? EMPTY_CONSTRAINT : super.getDummyConstraint();
        }
        return _getConstraint();
    }

    private boolean isSGP() {
        return getEnforceSequential() && (getVersion() == null || getVersion().verGreaterOrEqual(PowerSteeringVersions._9_3));
    }

    public boolean isConstraintEnd() {
        if (isSGP()) return true;
        return super.isConstraintEnd();
    }

    public boolean isConstraintStart() {
        if (isSGP()) return true;
        return super.isConstraintStart();
    }

    public void setConstraint(IConstraint c) {
        if (isSGP()) {
            throw new UnsupportedOperationException("Can't set constraint to SGP project");
        }
        super.setConstraint(c);
    }

    public WorkType getWorkType() {
        if (workType == null) {
            Config parent = getConfig().getParent();
            if (parent.getName().equals(Template.NAME)) {
                workType = new Template(parent);
            }
        }
        return super.getWorkType();
    }

    public boolean isConstraintsDisabled() {
        PowerSteeringVersions ver = getVersion();
        if (ver != null && ver.verGreaterOrEqual(PowerSteeringVersions._9_3)) {
            return getEnforceSequential() || getManualScheduling();
        }
        return false;
    }

    public void setDatesFixed() {
        set("dates_is_fixed", true);
    }

    public boolean isDatesFixed() {
        return getBooleanFalse("dates_is_fixed");
    }

    /**
     * #85570
     *
     * @return true if broken
     */
    public boolean isBrokenDates() {
        PowerSteeringVersions ver = getVersion();
        if (ver == null || ver.verLessThan(PowerSteeringVersions._9_4)) {
            return false;
        }
        if (isDatesFixed()) return false;
        if (!isSGP()) {
            if (getConstraint().equals(Constraint.FNLT) &&
                    getConstraintEndDateAsLong() != null && getConstraintEndDateAsLong() < System.currentTimeMillis())
                return true; //#85570
            if (ver.verGreaterOrEqual(PowerSteeringVersions._10_0) && getConstraint().equals(Constraint.ASAP) && getManualScheduling())
                return true; // is it correct? #86463. for 10.0 and  DMAIC_AUTOTEST_NON_SEQUENCIAL_PROJECT
        }
        return false;
    }

    public boolean hasSecondCreateWorkPage() {
        return hasStatus() || hasPriority();
    }
}
