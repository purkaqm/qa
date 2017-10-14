package com.powersteeringsoftware.libs.objects.works;

import com.powersteeringsoftware.libs.objects.ConfigObject;
import com.powersteeringsoftware.libs.objects.PSProcess;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.session.TestSession;

public class Template extends ConfigObject implements WorkType {

    public static final String NAME = "work-template";
    private Work root;

    //TODO custom fields
    //TODO team

    public Template(String name) {
        this(Config.createConfig(NAME));
        if (name != null)
            setName(name);
    }

    private Template(String name, String rootName, boolean isGated) {
        this(name);
        root = isGated ? new GatedProject(rootName, getName()) : new Work(rootName);
    }

    public Template(Config conf) {
        super(conf);
    }

    public String getObjective() {
        return getString("objective");
    }

    public void setObjective(String objective) {
        set("objective", objective);
    }

    public Boolean getIsShowSelectChildrenStep() {
        return getBoolean("is-show-selected-children-step");
    }

    public void setIsShowSelectChildrenStep(boolean isShowSelectChildrenStep) {
        set("is-show-selected-children-step", isShowSelectChildrenStep);
    }

    public Boolean getIsRequireGateEndDates() {
        return getBooleanFalse("is-required-gate-end-dates");
    }

    public void setIsRequireGateEndDates(boolean isRequireGateEndDates) {
        set("is-required-gate-end-dates", isRequireGateEndDates);
    }


    public Work getStructure() {
        if (root != null) return root;
        if (!conf.hasChild(Work.NAME)) return null;
        Config w = conf.getChByName(Work.NAME);

        if (w.hasChild(PSProcess.NAME)) {
            root = new GatedProject(w);
            if (w.hasChild(PSProcess.NAME)) {
                ((GatedProject) root).setProcess(new PSProcess(w.getChByName(PSProcess.NAME)));
            }
        } else {
            root = new Work(w);
        }

        if (!exist() && getVersion() != null && getVersion().verGreaterOrEqual(PowerSteeringVersions._9_4)) {
            root.setName(getName()); // workaround for new interface 9.4.
        }
        root.setType(this);
        root.setDeleted();
        root.setTemplateRoot(true);
        return root;
    }

    public boolean hasStructure() {
        return getStructure() != null;
    }

    public Boolean getManualSchedule() {
        Work w = getStructure();
        if (w == null) return null;
        return w.getManualScheduling();
    }

    public Boolean getControlCost() {
        Work w = getStructure();
        if (w == null) return null;
        return w.getControlCost();
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof Template && getName().equals(((Template) o).getName());
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void setCreated() {
        super.setCreated();
        getStructure().setCreated();
        TestSession.putWorkTemplate(this);
    }

    @Override
    public void setDeleted() {
        super.setDeleted();
        getStructure().setDeleted();
        TestSession.removeWorkTemplate(this);
    }

    public static Template createGatedTemplate(String name, String root) {
        return new Template(name, root, true);
    }

}
