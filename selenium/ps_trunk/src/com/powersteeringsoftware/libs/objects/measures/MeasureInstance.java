package com.powersteeringsoftware.libs.objects.measures;

import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 26.08.12
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
public class MeasureInstance extends Measure {
    private Work work;

    public MeasureInstance(Config conf) {
        super(conf);
    }

    public boolean isLibrary() {
        return false;
    }

    public Work getWork() {
        if (work != null) return work;
        if (!getConfig().hasChild(Work.NAME)) return null;
        String id = getConfig().getChByName(Work.NAME).getAttributeValue("id");
        if (id != null) {
            for (Work w : TestSession.getWorkList()) {
                if (id.equals(w.getId())) return w;
            }
        }
        return work = new Work(getConfig().getChByName(Work.NAME));
        //return null;
    }

    public void setWork(Work w) {
        work = w;
        Config c = getConfig().getChByName(Work.NAME);
        if (c == null) c = getConfig().addElement(Work.NAME);
        c.setAttributeValue("id", w.getId());
        //getConfig().removeChild(Work.NAME);
        //getConfig().addConfig(w.getConfig());
    }

    public String toString() {
        return super.toString() + "[" + (getWork() != null ? getWork().getName() : null) + "]";
    }

    /**
     * see 81888.2.1.1.1.1
     * <p/>
     * The measure is not a manual measure (i.e., it is a formula).
     * The user has "Edit Work" permission for the project.
     * The project satisfies the measure definition's effective date setting.
     *
     * @return true if this is recalculated measure
     */
    public boolean canRecalculate() {
        if (isManual()) return false;
        if (getEffectiveEndDate().isEmpty()) return true;
        PSCalendar c = PSCalendar.getEmptyCalendar().set(getEffectiveEndDate());
        return c.getTime() > System.currentTimeMillis(); // todo
    }
}
