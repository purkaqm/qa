package com.powersteeringsoftware.libs.objects.resources;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.ConfigObject;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests_data.Config;
import org.testng.Assert;

import javax.xml.parsers.ParserConfigurationException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 24.09.2010
 * Time: 15:38:16
 */
public class PSCalendar extends ConfigObject {

    private GregorianCalendar calendar;

    public static final long HOUR_IN_MS = 1000 * 60 * 60;
    public static final long DAY_IN_MS = HOUR_IN_MS * 24;

    public static final int DEFAULT_WORK_DAY_IN_H = 8;
    public static final int DEFAULT_HOLIDAY_IN_H = 0;
    public static final int DEFAULT_WORK_DAY_START_IN_H = 10;
    public static final int DEFAULT_WORK_DAY_END_IN_H = DEFAULT_WORK_DAY_IN_H + DEFAULT_WORK_DAY_START_IN_H;
    public static final int DEFAULT_HOLIDAY_START_IN_H = 10;
    public static final int DEFAULT_HOLIDAY_END_IN_H = DEFAULT_HOLIDAY_START_IN_H + DEFAULT_HOLIDAY_IN_H;
    public static final long DEFAULT_WORK_DAY_IN_MS = HOUR_IN_MS * DEFAULT_WORK_DAY_IN_H;
    public static final long DEFAULT_WORK_DAY_START_IN_MS = HOUR_IN_MS * DEFAULT_WORK_DAY_START_IN_H;
    public static final long DEFAULT_WORK_DAY_END_IN_MS = DEFAULT_WORK_DAY_START_IN_MS + DEFAULT_WORK_DAY_IN_MS;
    public static final String NAME = "calendar";

    private PSCalendar() {
        this(Config.createConfig(NAME));
        setName("empty");
        for (int i = 1; i < 8; i++) {
            Config c = conf.addElement("day");
            c.setText(String.valueOf(i));
            int start;
            int end;
            int duration;
            if (i == 1 || i == 7) {
                start = DEFAULT_HOLIDAY_START_IN_H;
                end = DEFAULT_HOLIDAY_END_IN_H;
                duration = DEFAULT_HOLIDAY_IN_H;
            } else {
                start = DEFAULT_WORK_DAY_START_IN_H;
                end = DEFAULT_WORK_DAY_END_IN_H;
                duration = DEFAULT_WORK_DAY_IN_H;
            }
            c.setAttributeValue("start", String.valueOf(start));
            c.setAttributeValue("end", String.valueOf(end));
            c.setAttributeValue("hours", String.valueOf(duration));
        }
    }

    public PSCalendar(Config c) {
        super(c);
        if (!conf.hasChild("time-zone"))
            conf.setText("time-zone", CoreProperties.getApplicationServerTimeZone().getID());
        if (!conf.hasChild("format")) {
            conf.setText("format", CoreProperties.getDateFormat());
        }
        calendar = new GregorianCalendar();
        calendar.setTimeZone(getTimeZone());
    }

    public static PSCalendar getEmptyCalendar() {
        return getEmptyCalendar(CoreProperties.getTestTemplate());
    }

    public static PSCalendar getCurrentEmptyCalendar() {
        return getEmptyCalendar(System.currentTimeMillis());
    }

    public static PSCalendar getEmptyCalendar(long date) {
        PSCalendar res = new PSCalendar();
        res.calendar.setTimeInMillis(date);
        return res;
    }

    public List<Integer> getWeekend() {
        List<Integer> res = new ArrayList<Integer>();
        Map<Integer, Integer> map = getHoursPerDay();
        for (int num : map.keySet()) {
            if (map.get(num) == 0)
                res.add(num);
        }
        return res;
    }

    public List<GregorianCalendar> getHolidays() {
        List<GregorianCalendar> calendars = new ArrayList<GregorianCalendar>();
        if (conf == null) return null;
        for (Config c : conf.getChsByName("holiday")) {
            calendars.add(toCalendar(calendar, c.getText(), getFormat()));
        }
        return calendars;
    }

    public Map<Integer, Integer> getHoursPerDay() {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (Config c : conf.getChsByName("day")) {
            String attr = c.getAttribute("hours");
            int hours = attr.isEmpty() ? 0 : Integer.parseInt(attr);
            int num = Integer.parseInt(c.getText());
            map.put(num, hours);
        }
        return map;
    }

    /**
     * @return number hours per week
     */
    public int getHoursPerWeek() {
        int res = 0;
        Map<Integer, Integer> map = getHoursPerDay();
        for (int day : map.keySet()) {
            res += map.get(day);
        }
        return res;
    }


    /**
     * @param to
     * @return
     */
    public int getPeriodHours(PSCalendar to) {
        int res = 0;
        Map<Integer, Integer> map = getHoursPerDay();
        GregorianCalendar tmp = (GregorianCalendar) calendar.clone();
        int step = calendar.compareTo(to.calendar);
        res += map.get(tmp.get(Calendar.DAY_OF_WEEK));
        do {
            tmp.set(Calendar.DAY_OF_YEAR, tmp.get(Calendar.DAY_OF_YEAR) - step);
            res += map.get(tmp.get(Calendar.DAY_OF_WEEK));
        } while (!tmp.equals(to.calendar));
        return res;
    }

    public boolean isWeekend() {
        return calendar != null && getWeekend().contains(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public boolean isWorkDay() {
        for (GregorianCalendar c : getHolidays()) {
            if (calendar.equals(c)) return false;
        }
        return !isWeekend();
    }

    public String get(String newFormat) {
        return new SimpleDateFormat(newFormat).format(calendar.getTime());
    }

    public String get(ILocatorable newFormat) {
        return get(newFormat.getLocator());
    }

    public PSCalendar set(String date) {
        PSCalendar psCalendar = (PSCalendar) clone();
        psCalendar.calendar = toCalendar(psCalendar.calendar, date, getFormat());
        return psCalendar;
    }

    public PSCalendar set(Date date) {
        PSCalendar psCalendar = (PSCalendar) clone();
        psCalendar.calendar.setTime(date);
        return psCalendar;
    }

    public PSCalendar set(long time) {
        PSCalendar psCalendar = (PSCalendar) clone();
        psCalendar.calendar.setTimeInMillis(time);
        return psCalendar;
    }


    public PSCalendar set(int days) {
        PSCalendar psCalendar = (PSCalendar) clone();
        psCalendar.calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + days);
        return psCalendar;
    }


    public PSCalendar set(int days, int hour) {
        PSCalendar c = set(days);
        if (c != null) {
            c.calendar.set(Calendar.HOUR_OF_DAY, hour);
            setHourToStart();
        }
        return c;
    }

    public void toStart() {
        if (calendar == null) return;
        calendar.set(Calendar.HOUR_OF_DAY, getStart());
        setHourToStart();
    }

    public void toEnd() {
        if (calendar == null) return;
        calendar.set(Calendar.HOUR_OF_DAY, getEnd());
        setHourToStart();
    }

    private void setHourToStart() {
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }


    public PSCalendar setWorkDays(int days) {
        PSCalendar psCalendar = (PSCalendar) clone();
        while (days != 0) {
            psCalendar.calendar.set(Calendar.DAY_OF_YEAR,
                    psCalendar.calendar.get(Calendar.DAY_OF_YEAR) + (days > 0 ? 1 : -1));
            if (!psCalendar.isWorkDay()) continue;
            if (days > 0)
                days--;
            else days++;
        }
        return psCalendar;
    }

    public PSCalendar setWorkHours(int hours) {
        PSCalendar psCalendar = (PSCalendar) clone();
        Map<Integer, Integer> map = getHoursPerDay();
        int set = 0;
        while (set != hours) {
            int curDay = psCalendar.calendar.get(Calendar.DAY_OF_WEEK);
            int increment = hours > 0 ? 1 : -1;
            set += increment * map.get(curDay);
            if (set != hours) {
                psCalendar.calendar.set(Calendar.DAY_OF_YEAR,
                        psCalendar.calendar.get(Calendar.DAY_OF_YEAR) + increment);
            }
        }
        return psCalendar;
    }


    private static GregorianCalendar toCalendar(GregorianCalendar calendar, String date, DateFormat format) {
        calendar = calendar == null ? new GregorianCalendar() : (GregorianCalendar) calendar.clone();
        try {
            Date d = format.parse(date);
            calendar.setTime(d);
        } catch (ParseException e) {
            Assert.fail("Can't parse input", e);
        }
        return calendar;
    }

    private static GregorianCalendar toCalendar(GregorianCalendar calendar, String date) {
        return toCalendar(calendar, date, new SimpleDateFormat(CoreProperties.getDateFormat()));
    }

    public long days() {
        return days(null);
    }

    public PSCalendar nextWorkDate(Boolean doIncrease) {
        PSCalendar psCalendar = (PSCalendar) clone();
        int i = 0;
        while (!psCalendar.isWorkDay()) {
            psCalendar.calendar.set(Calendar.DAY_OF_YEAR,
                    psCalendar.calendar.get(Calendar.DAY_OF_YEAR) + (doIncrease ? 1 : -1));
            if (i++ > 365) throw new IllegalStateException("Can't find next work day");
        }
        return psCalendar;
    }

    /**
     * get first working day
     *
     * @return PSCalendar
     */
    public PSCalendar getFirstMonday() {
        PSCalendar monday = null;
        PSCalendar current = (PSCalendar) clone();
        // search first work day (monday)
        for (int i = 0; i < 7; i++) {
            if (!current.isWorkDay() && (monday = current.set(1)).isWorkDay()) {
                break;
            }
            current = current.set(1);
        }
        return monday;
    }

    private int getDaySetting(String name, int def) {
        Config c = getDayConfig();
        if (c == null) return -1;
        String attr = c.getAttribute(name);
        return attr.isEmpty() ? def : Integer.parseInt(attr);
    }

    public int getStart() {
        return getDaySetting("start", DEFAULT_WORK_DAY_START_IN_H);
    }

    public int getEnd() {
        return getDaySetting("end", DEFAULT_WORK_DAY_END_IN_H);
    }

    public int getDuration() {
        return getDaySetting("hours", DEFAULT_WORK_DAY_IN_H);
    }

    private Config getDayConfig() {
        if (calendar == null) return null;
        String d = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        for (Config c : conf.getChsByName("day")) {
            if (d.equals(c.getText())) return c;
        }
        return null;
    }

    public PSCalendar getStartWeekDay() {
        return getStartEndWeekDay(true);
    }

    public PSCalendar getEndWeekDay() {
        return getStartEndWeekDay(false);
    }

    private PSCalendar getStartEndWeekDay(boolean start) {
        int thisDay = getDayOfWeek();
        Integer[] week = getWeekDays();
        PSCalendar current = (PSCalendar) clone();
        int daysToSet = week[start ? 0 : week.length - 1] - thisDay;
        current.calendar.set(Calendar.DAY_OF_YEAR, current.calendar.get(Calendar.DAY_OF_YEAR) + daysToSet);
        return current;
    }

    public Integer[] getWeekDays() {
        Map<Integer, Integer> map = getHoursPerDay();
        Integer[] week = new Integer[map.keySet().size()];
        map.keySet().toArray(week);
        return week;
    }

    public int getDayOfWeek() {
        if (calendar == null) return -1;
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (getHoursPerDay().containsKey(day)) return day;
        return -1;
    }

    public long days(Boolean doIncrease) {
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        if (doIncrease != null) {
            PSCalendar psCalendar = (PSCalendar) clone();
            while (!psCalendar.isWorkDay()) {
                if (doIncrease) {
                    dayOfYear++;
                } else {
                    dayOfYear--;
                }
                psCalendar.calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
            }
        }
        long res = 365L * calendar.get(Calendar.YEAR) + dayOfYear;
        PSLogger.debug("number of" + (doIncrease != null ? " (work)" : "") +
                " days for " + this + " is " + res);
        return res;
    }

    public String toString() {
        if (calendar == null) return getName();
        return getFormat().format(getDate());
    }

    public long getTime() {
        if (calendar == null) return -1;
        return calendar.getTimeInMillis();
    }

    public Date getDate() {
        if (calendar == null) return null;
        return calendar.getTime();
    }

    public boolean lessOrEqual(PSCalendar toCompare) {
        return calendar.compareTo(toCompare.calendar) <= 0;
    }

    public boolean less(PSCalendar toCompare) {
        return calendar.compareTo(toCompare.calendar) < 0;
    }

    /**
     * @param date1 String
     * @param date2 String
     * @return 1 (date1 > date2), 0 (date1 == date2), -1 (date1 < date2)
     */
    public static int compare(String date1, String date2) {
        return toCalendar(null, date1).compareTo(toCalendar(null, date2));
    }

    public long diffInWorkDays(PSCalendar date2) {
        long diff = //(calendar.getTimeInMillis() - date2.calendar.getTimeInMillis()) / DAY_IN_MS;
                calendar.get(Calendar.DAY_OF_YEAR) - date2.calendar.get(Calendar.DAY_OF_YEAR);
        long res = 0;
        PSCalendar psCalendar = (PSCalendar) clone();
        for (int i = 0; i < Math.abs(diff) + 1; i++) {
            if (psCalendar.isWorkDay()) {
                res += diff >= 0 ? 1 : -1;
            }
            psCalendar.setDayOfYear(psCalendar.getDayOfYear() + (diff > 0 ? -1 : 1));
        }
        return res;
    }

    public void setDayOfYear(int day) {
        calendar.set(Calendar.DAY_OF_YEAR, day);
    }

    public int getDayOfYear() {
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public Object clone() {
        PSCalendar psCalendar = null;
        try {
            psCalendar = new PSCalendar(conf.copy(true));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        psCalendar.calendar = calendar == null ? new GregorianCalendar() : (GregorianCalendar) calendar.clone();
        return psCalendar;
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof PSCalendar)) return false;
        if (!getName().equals(((PSCalendar) o).getName())) return false;
        if (calendar == null) {
            if (((PSCalendar) o).calendar == null) return true;
        } else {
            return calendar.equals(((PSCalendar) o).calendar);
        }
        return false;
    }

    public void setConfig(Config c) {
        conf = c;
    }

    public void setTimeZone(TimeZone zone) {
        if (calendar != null) {
            calendar.setTimeZone(zone);
        }
        conf.setText("time-zone", zone.getID());
    }

    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone(conf.getText("time-zone"));
    }

    private SimpleDateFormat getFormat() {
        SimpleDateFormat res = new SimpleDateFormat(conf.getText("format"));
        res.setTimeZone(getTimeZone());
        return res;
    }


}
