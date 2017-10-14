package com.powersteeringsoftware.libs.objects.measures;

import com.powersteeringsoftware.libs.objects.ConfigObject;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 26.08.12
 * Time: 18:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class Measure extends ConfigObject implements Cloneable {
    public static final String NAME = "measure";
    private static final IndicatorType DEFAULT_INDICATOR_TYPE = IndicatorType.GOAL;

    public enum IndicatorType {
        VARIANCE,
        GOAL,
        NONE
    }

    enum DisplayFormatName {
        INTEGER,
        FLOAT,
        MONETARY,
        PERCENT,
    }

    public enum EffectiveDates {
        ABSOLUTE,
        ACTIVE_PROJECT,
        PROJECT_LIFETIME,
        BASELINE_TARGET_DATES,
        ALWAYS,
    }

    enum TimeOfDay {
        _4_00(240),
        _4_30(270),
        _10_00(600),
        _12_00(720),;
        int minute;

        TimeOfDay(int i) {
            minute = i;
        }

        public int getMinute() {
            return minute;
        }
    }

    public enum ScheduleName {
        NEVER,
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
    }

    public class DisplayFormat { // Scale, Precision

        private DisplayFormat(DisplayFormatName name) {
            this.name = name;
        }

        //TODO
        String scale;
        String precision;
        DisplayFormatName name;

        public int getIndex() {
            return name.ordinal();
        }
    }


    public Measure(Config conf) {
        super(conf);
    }

    public Measure(String name) {
        this(Config.createConfig(NAME));
        setName(name);
        conf.addElement("thresholds");
    }

    public abstract boolean isLibrary();

    public boolean isManual() {
        return getBooleanTrue("is_manual");
    }

    public boolean hasIndicatorType() {
        return conf.hasChild("indicator_type");
    }

    public IndicatorType getIndicatorType() {
        String type = getString("indicator_type");
        if (type == null) return DEFAULT_INDICATOR_TYPE;
        try {
            return IndicatorType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            // ignore
        }
        return DEFAULT_INDICATOR_TYPE;
    }

    public void setIndicationType(IndicatorType type) {
        set("indicator_type", type.name());
    }

    public boolean isGoal() {
        return getIndicatorType().equals(IndicatorType.GOAL);
    }

    public boolean hasThresholds() {
        return conf.hasChild("thresholds");
    }

    public String getUnitsMessage() {
        return getString("units_message");
    }

    public List<Threshold> getThresholds() {
        List<Threshold> res = new ArrayList<Threshold>();
        Config c1 = conf.getChByName("thresholds");
        if (c1 == null) return res;
        for (Config c : c1.getChilds()) {
            res.add(new Threshold(c));
        }
        Integer size = null;
        if (getIndicatorType().equals(IndicatorType.GOAL)) {
            size = 2;
        } else if (getIndicatorType().equals(IndicatorType.VARIANCE)) {
            size = 4;
        }
        if (size != null) {
            if (size != res.size()) throw new IllegalArgumentException("Incorrect config for measure " + getName() +
                    ": should be " + size + " thresholds specified");
        }
        return res;
    }

    public boolean hasDisplayFormat() {
        return conf.hasChild("display_format");
    }

    public DisplayFormat getDisplayFormat() {
        if (!conf.hasChild("display_format")) return new DisplayFormat(DisplayFormatName.INTEGER);
        DisplayFormat res;
        try {
            res = new DisplayFormat(DisplayFormatName.valueOf(conf.getText("display_format").toUpperCase()));
            res.scale = conf.getChByName("display_format").getAttributeValue("scale");
            res.precision = conf.getChByName("display_format").getAttributeValue("precision");
        } catch (Exception e) {
            res = new DisplayFormat(DisplayFormatName.INTEGER);
        }
        return res;
    }

    public boolean hasEffectiveDates() {
        return conf.hasChild("effective_dates");
    }

    public EffectiveDates getEffectiveDates() {
        if (!conf.hasChild("effective_dates")) return EffectiveDates.ALWAYS;
        EffectiveDates res;
        try {
            res = EffectiveDates.valueOf(conf.getText("effective_dates").toUpperCase());
        } catch (Exception e) {
            res = EffectiveDates.ALWAYS;
        }
        return res;
    }

    private String getEffectiveDate(boolean start) {
        if (!conf.hasChild("effective_dates")) return "";
        String res = conf.getChByName("effective_dates").getAttributeValue(start ? "start" : "end");
        return res == null ? "" : res;
    }

    public String getEffectiveStartDate() {
        return getEffectiveDate(true);
    }

    public String getEffectiveEndDate() {
        return getEffectiveDate(false);
    }

    private boolean hasSchedule(String name) {
        return conf.hasChild(name + "_schedule");
    }

    private Schedule getSchedule(String name) {
        if (!hasSchedule(name)) return new Schedule(name, ScheduleName.NEVER);
        Schedule res;
        try {
            Config c = conf.getChByName(name + "_schedule");
            ScheduleName _name = ScheduleName.valueOf(c.getText().toUpperCase());
            res = new Schedule(name, _name);
            String _every = c.getAttributeValue("every");
            String _time = c.getAttributeValue("time_of_day");
            if (_every != null) {
                res.setEvery(Integer.parseInt(_every));
            }
            if (_time != null) {
                int __time = Integer.parseInt(_time);
                for (TimeOfDay t : TimeOfDay.values()) {
                    if (__time == t.minute) {
                        res.setTime(t);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            // ignore
            res = new Schedule(name, ScheduleName.NEVER);
        }
        return res;
    }

    public boolean hasTestSchedule() {
        return hasSchedule("test");
    }

    public Schedule getTestSchedule() {
        return getSchedule("test");
    }

    public boolean hasHistorySchedule() {
        return hasSchedule("history");
    }

    public Schedule getHistorySchedule() {
        return getSchedule("history");
    }

    public boolean hasReminderSchedule() {
        return hasSchedule("reminder");
    }

    public Schedule getReminderSchedule() {
        return getSchedule("reminder");
    }

    public class Threshold {
        private Config c;

        private Threshold(Config c) {
            this.c = c;
        }

        public String getMessage() {
            return c.getText("message");
        }

        public Double getValue() {
            return Double.parseDouble(c.getText("value"));
        }
    }

    public String toString() {
        return getClass().getSimpleName() + "[" + getName() + "]";
    }

    public void setCreated() {
        super.setCreated();
        TestSession.putMeasure(this);
    }

    public void setDeleted() {
        super.setDeleted();
        TestSession.removeMeasure(this);
    }

    public static Measure getMeasure(Config c) {
        if (!c.hasChild("is_library") || Boolean.parseBoolean(c.getText("is_library")))
            return new MeasureTemplate(c);
        return new MeasureInstance(c);
    }


    public class Schedule {
        private ScheduleName name;
        private TimeOfDay time;
        private Integer every;
        private String type;

        private Schedule(String type, ScheduleName name) {
            this.name = name;
            this.type = type;
        }

        public int getIndex() {
            return name.ordinal();
        }

        public String getType() {
            return type;
        }

        public ScheduleName getName() {
            return name;
        }

        public Integer getTime() {
            return time == null ? null : time.minute;
        }

        public TimeOfDay getTimeName() {
            return time;
        }

        public void setTime(TimeOfDay time) {
            this.time = time;
        }

        public Integer getEvery() {
            return every;
        }

        public void setEvery(int every) {
            this.every = every;
        }

        public boolean isNever() {
            return name.equals(ScheduleName.NEVER);
        }

        public boolean isDaily() {
            return name.equals(ScheduleName.DAILY);
        }

        public boolean isWeekly() {
            return name.equals(ScheduleName.WEEKLY);
        }
    }

    public boolean equals(Object o) {
        return o != null && o instanceof Measure && getName().equals(((Measure) o).getName()) && isLibrary() == ((Measure) o).isLibrary();
    }
}
