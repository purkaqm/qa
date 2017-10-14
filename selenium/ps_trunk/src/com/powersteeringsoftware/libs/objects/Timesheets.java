package com.powersteeringsoftware.libs.objects;

import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.resources.RateTable;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.apache.commons.collections.map.HashedMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * example of xml:
 * <timesheets id="1">
 * <date>08/02/2009</date>
 * <line>
 * <work xpath="//work[@id='work0']" how_to_set='favorites'/>
 * <day day="sun">1</day>
 * <day day="mon">2</day>
 * <day day="tue">3</day>
 * <day day="wed">4</day>
 * <day day="thu">5</day>
 * <day day="fri">6</day>
 * <day day="sat">7</day>
 * </line>
 * <line>
 * <work xpath="//work[@id='work1']" how_to_set='browse'/>
 * <day day="mon">6</day>
 * <day day="tue">5</day>
 * <day day="wed">4</day>
 * <day day="thu">3</day>
 * <day day="sat">1</day>
 * </line>
 * <line>
 * <work xpath="//work[@id='work2']" how_to_set='search'/>
 * <day day="sun">1.1</day>
 * <day day="mon">2.2</day>
 * <day day="tue">3.3</day>
 * <day day="wed">4.4</day>
 * <day day="thu">5.5</day>
 * <day day="fri">6.6</day>
 * <day day="sat">7.7</day>
 * </line>
 * </timesheets>
 */
public class Timesheets extends ConfigObject implements Comparable {
    public static final String NAME = "timesheets";

    public Timesheets(Date date) {
        super();
        setDate(DATE, date.getTime());
    }

    public Timesheets(Config c) {
        super(c);
    }

    public Timesheets() {
        super();
    }

    public PSCalendar getPSDate() {
        return getPSDate(DATE);
    }

    public boolean hasDate() {
        return getConfig().hasChild(DATE);
    }

    public Date getDDate() {
        return getPSDate().getDate();
    }

    public String getDate() {
        return getDate(DATE);
    }

    public Long getLDate() {
        return getDateAsLong(DATE);
    }

    public void setSDate(String date) {
        set(DATE, date);
    }

    public String getName() {
        return getDate();
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Timesheets)) return 0;
        Timesheets t = (Timesheets) o;
        if (!hasDate() || !t.hasDate()) return 0;
        return getDDate().compareTo(t.getDDate());
    }

    public boolean equals(Object o) {
        if (!(o instanceof Timesheets)) return false;
        Timesheets t = (Timesheets) o;
        if (!hasDate() || !t.hasDate()) return false;
        return getDate().equals(t.getDate());
    }

    public enum DayOfWeek {
        SUN,
        MON,
        TUE,
        WED,
        THU,
        FRI,
        SAT,
    }


    public enum Status {
        NOT_SUBMITTED,
        SUBMITTED,
        APPROVED,
        REJECTED,
    }

    public enum HowToSet {
        FAVORITES,
        BROWSE,
        SEARCH,
    }

    public List<Line> getLines(Work w) {
        List<Line> res = new ArrayList<Line>();
        for (Config c : getConfig().getChsByName(Line.NAME)) {
            Line l = new Line(c);
            if (w == null || w.equals(l.getWork()))
                res.add(l);
        }
        return res;
    }

    public List<Line> getLines() {
        return getLines(null);
    }

    public Line getLine(String activity, Work w) {
        List<Line> lines = getLines(w);
        for (Line l : lines) {
            if (activity == null && l.getActivity() == null) return l;
            if (activity != null && activity.equals(l.getActivity())) return l;
        }
        return null;
    }

    public Map<Work, List<Cost>> getCosts() {
        Map res = new HashedMap();
        for (Line l : getLines()) {
            res.put(l.getWork(), l.getCosts());
        }
        return res;
    }

    public void addLine(Line line) {
        Config c = getConfig().addChild(line.getConfig());
        line.conf = c;
    }

    public Line addLine(Work w) {
        Line l = new Line(w);
        addLine(l);
        return l;
    }

    public String toString() {
        return getDate() + getLines();
    }

    public class Line extends ConfigObject implements Comparable {
        public static final String NAME = "line";
        private Work work;

        public Line(Work w) {
            super();
            setWork(w);
        }

        public Line(Config c) {
            super(c);
        }

        public Work getWork() {
            return work == null ? work = new Work(getConfig().getChByName(Work.NAME)) : work;
        }

        public void setWork(Work work) {
            if (getConfig().hasChild(Work.NAME)) getConfig().removeChild(Work.NAME);
            getConfig().addChild(work.getConfig());
        }

        public String toString() {
            return getName();
        }

        /**
         * @return 0, 1, 2(favorites, browse, search)
         */
        public HowToSet howToSetWork() {
            if (!getConfig().hasChild(Work.NAME)) return null;
            if (getConfig().getChByName(Work.NAME).hasAttribute("how_to_set")) {
                return HowToSet.valueOf(getConfig().getChByName(Work.NAME).getAttribute("how_to_set").toUpperCase());
            }
            return HowToSet.FAVORITES;
        }

        public String getName() {
            return getWork().getName();
        }

        public Cost getCost(DayOfWeek day) {
            if (!getStatus().equals(Status.APPROVED)) return null;
            Double val = getValue(day);
            if (val == null) return null;
            PSCalendar date = Timesheets.this.getPSDate();
            date = date.set(day.ordinal());
            //todo: hotfix
            double rate = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._14) ? 1d : 0d;
            RateTable rt = TestSession.getDefaultRateTable();
            Cost.Time res = new Cost.Time(val, rate * val, date.getTime());
            res.setActivity(getActivity());
            //getWork().addCost(res);
            return res;
        }

        public List<Cost> getCosts() {
            List<Cost> costs = new ArrayList<Cost>();
            for (DayOfWeek d : DayOfWeek.values()) {
                Cost c = getCost(d);
                if (c != null) costs.add(c);
            }
            return costs;
        }

        public Double getValue(DayOfWeek day) {
            for (Config c : getConfig().getChsByName("day")) {
                if (day.name().equalsIgnoreCase(c.getAttribute("day"))) {
                    return Double.parseDouble(c.getText());
                }
            }
            return null;
        }

        public void setValue(DayOfWeek day, Double v) {
            Config c = null;
            for (Config _c : getConfig().getChsByName("day")) {
                if (day.name().equalsIgnoreCase(_c.getAttribute("day"))) {
                    c = _c;
                    break;
                }
            }
            if (c == null) {
                c = getConfig().addElement("day");
                c.setAttributeValue("day", day.name().toLowerCase());
            }
            c.setText(String.valueOf(v));
        }

        public Status getStatus() {
            if (!getConfig().hasChild("status")) return Status.NOT_SUBMITTED;
            int i = Integer.parseInt(getConfig().getText("status"));
            return Status.values()[i];
        }

        public void setStatus(Status s) {
            if (!getConfig().hasChild("status")) getConfig().addElement("status");
            getConfig().setText("status", String.valueOf(s.ordinal()));
        }

        public String getActivity() {
            return getString("activity");
        }

        public void setActivity(String a) {
            set("activity", a);
        }

        public String getBillingCategory() {
            return getString("billing_category");
        }

        public void setBillingCategory(String a) {
            set("billing_category", a);
        }


        @Override
        public int compareTo(Object o) {
            if (!(o instanceof Line)) return 0;
            return getName().compareTo(((Line) o).getName());
        }

        public Double getTotal() {
            Double res = 0d;
            for (DayOfWeek day : DayOfWeek.values()) {
                Double val = getValue(day);
                if (val == null) continue;
                res += val;
            }
            return _round(res);
        }
    }

    public void setCreated() {
        super.setCreated();
        TestSession.putTimesheets(this);
    }

    public Double getTotal(DayOfWeek day) {
        Double res = 0d;
        for (Line l : getLines()) {
            Double v = l.getValue(day);
            if (v == null) continue;
            res += v;
        }
        return _round(res);
    }

    public Double getTotal() {
        Double res = 0d;
        for (Line l : getLines()) {
            Double v = l.getTotal();
            if (v == null) continue;
            res += v;
        }
        return _round(res);
    }

    private static Double _round(Double d) {
        if (d == null) return null;
        return (double) Math.round(d * 100) / 100d;
    }
}
