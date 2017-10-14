package com.powersteeringsoftware.libs.objects;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.tests_data.Config;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 25.11.13
 * Time: 15:50
 * To change this template use File | Settings | File Templates.
 */
public class Cost extends SingleStartDateConfigObject implements Comparable {
    private boolean isManual = true;
    public static final String NAME = "cost";
    public static final boolean NF_GROUPING = true;
    public static final int NF_MIN_FRACTION_DIGITS = 2;
    public static final int NF_MAX_FRACTION_DIGITS = 2;

    private static final String AMOUNT_TMP = "{amount}";
    private static final String DATE_TMP = "{date}";
    private static final String DESC_TMP = "{desc}";
    private static final String TYPE_TMP = "{type}";
    private static final String ADD_TYPE_TMP = "{add-type}";
    private static final String ACTIVITY_TMP = "{activity}";
    private static final String RESOURCE_TMP = "{resource}";
    private static final String TEMPLATE_MAIN = TYPE_TMP + ",amount=" + AMOUNT_TMP + ",date=" + DATE_TMP + ",add-type=" + ADD_TYPE_TMP;
    private static final String TEMPLATE_ACTIVITY = ",activity=" + ACTIVITY_TMP;
    private static final String TEMPLATE_DESCRIPTION = ",description=" + DESC_TMP;
    private static final String TEMPLATE_RESOURCE = ",resource=" + RESOURCE_TMP;
    public static final String LABOR_DESC_TMPL_DATE = "{date}";
    public static final String LABOR_DESCRIPTION_TEMPLATE = "Labor cost estimate (" + LABOR_DESC_TMPL_DATE + ")";

    private Work work;

    private Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Cost)) return -1;
        Cost c = (Cost) o;
        if (getFormattedDate().equals(c.getFormattedDate())) {
            return getAmount() > c.getAmount() ? 1 : getAmount() < c.getAmount() ? -1 : 0;
        }
        return getDate() > c.getDate() ? 1 : getDate() < c.getDate() ? -1 : 0;
    }

    public enum Type {
        ESTIMATED,
        ACTUAL,
    }

    public enum AddType {
        MANUAL("Cost"),
        TIMESHEETS("Time"),
        LABOR("Labor cost");
        private String value;

        AddType(String t) {
            value = t;
        }

        public String getValue() {
            return value;
        }
    }

    protected Cost(Config config) {
        super(config);
    }

    public static Cost getCost(Config c, Work w) {
        Cost res = getCost(c);
        res.setWork(w);
        return res;
    }

    public static Cost getCost(Config c) {
        if (c.hasChild("hours")) {
            return new Cost.Time(c);
        } else {
            return new Cost(c);
        }
    }

    @Override
    public String getDescription() {
        if (!getConfig().hasChild("description") && getAddType().equals(AddType.LABOR) && hasCreationDate()) {
            setDescription(LABOR_DESCRIPTION_TEMPLATE.replace(LABOR_DESC_TMPL_DATE, getPSCreationDate().toString()));
        }
        return super.getDescription();
    }

    public Cost(Type type, Double amount, String desc, Long date) {
        super();
        setType(type);
        setAmount(amount);
        setDescription(desc);
        setDate(date);
    }

    public Cost(Double amount, String desc, long start, long end) { // estimated.
        this(Type.ESTIMATED, amount, desc, start, end);
    }

    public Cost(Double amount, String desc, long date, AddType type) { // actual.
        this(Type.ACTUAL, amount, desc, date);
        setAddType(type);
    }

    public Cost(Type type, Double amount, String desc, Long start, Long end) {
        this(type, amount, desc, start);
        setEndDate(end);
    }

    public void setType(Type type) {
        set("type", type.name().toLowerCase());
    }

    public Type getType() {
        String stype = getString("type");
        for (Type t : Type.values()) {
            if (t.name().equalsIgnoreCase(stype)) return t;
        }
        return Type.ESTIMATED;
    }

    public void setAddType(AddType type) {
        set("add-type", type.name().toLowerCase());
    }

    public void setAddType(String type) {
        AddType _t = null;
        for (AddType t : AddType.values()) {
            if (t.getValue().equals(type)) {
                _t = t;
                break;
            }
        }
        setAddType(_t);
    }

    public AddType getAddType() {
        String stype = getString("add-type");
        for (AddType t : AddType.values()) {
            if (t.name().equalsIgnoreCase(stype)) return t;
        }
        return AddType.MANUAL;
    }

    public Object getResource() {
        Config r = conf.getChByName("resource");
        if (r == null) return null;
        Config u = r.getChByName(User.NAME);
        if (u != null)
            return findUser(u);
        String txt = r.getText(PSPermissions.PSRole.NAME);
        if (txt != null && !txt.isEmpty()) {
            return BuiltInRole.getRoleByName(txt);
        }
        return null;
    }

    public boolean hasResource() {
        return conf.hasChild("resource");
    }

    public void setResource(User u) {
        addUser(getResourceConfig(), u);
    }

    public void setResource(Role role) {
        getResourceConfig().setText(PSPermissions.PSRole.NAME, role.getName());
    }

    private Config getResourceConfig() {
        Config r = conf.getChByName("resource");
        if (r == null) r = conf.addElement("resource");
        return r;
    }


    public String getActivity() {
        String a = getString("activity");
        return a == null ? "" : a;
    }

    public void setActivity(String a) {
        if (a != null && !a.isEmpty())
            set("activity", a);
    }

    public boolean isManual() {
        return isManual;
    }

    public void setAmount(Double d) {
        set("amount", d);
    }

    public Double getAmount() {
        try {
            String s = getString("amount");
            if (s == null || s.isEmpty()) return null;
            return Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public void setDate(long d) {
        super.setDate("date", d);
    }

    public Long getDate() {
        return getDateAsLong("date");
    }

    public String getSDate() {
        Long l = getDate();
        if (l != null) {
            return getCalendar().set(l).toString();
        }
        return null;
    }

    public PSCalendar getPSDate() {
        PSCalendar s = getCalendar().set(getDate());
        s.toStart();
        return s;
    }

    public PSCalendar getPSEndDate() {
        Long l = getEndDate();
        if (l == null) return null;
        PSCalendar e = getCalendar().set(l);
        e.toEnd();
        return e;
    }

    public String getSEndDate() {
        Long l = getEndDate();
        if (l != null) {
            return getCalendar().set(l).toString();
        }
        return null;
    }

    public void setEndDate(Long d) {
        if (d != null) {
            super.setDate("end-date", d);
        }
    }

    public Long getEndDate() {
        return getDateAsLong("end-date");
    }

    public boolean hasEndDate() {
        return getConfig().hasChild("end-date");
    }

    private static NumberFormat getNumberFormat(Currency currency, boolean group, int max, int min) {
        NumberFormat res = currency.getCurrencyFormat();
        res.setGroupingUsed(group);
        res.setMaximumFractionDigits(max);
        res.setMinimumFractionDigits(min);
        return res;
    }

    private static NumberFormat getNumberFormat(Currency c) {
        return getNumberFormat(c, NF_GROUPING, NF_MAX_FRACTION_DIGITS, NF_MIN_FRACTION_DIGITS);
    }

    @Override
    public Currency getCurrency() {
        if (work != null) return work.getCurrency();
        return Currency.getDefaultCurrency();
    }

    @Override
    public void setCurrency(Currency c) {
    }

    public String getFormattedAmount() {
        if (formattedAmount != null) return formattedAmount;
        Double amount = getAmount();
        if (amount == null) return "";
        String res = getNumberFormat(getCurrency()).format(Math.abs(amount));
        if (amount < 0) res = "(" + res + ")";
        return res;
    }

    private String formattedAmount;

    public void setFormattedAmount(String s) {
        formattedAmount = s;
    }

    public static Double parseAmount(Work w, String v) {
        boolean negative = v.contains("(");
        v = v.replace("(", "").replace(")", "");
        Double res;
        try {
            res = getNumberFormat(w.getCurrency()).parse(v).doubleValue();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return negative ? -res : res;
    }

    protected String getFormattedDate(PSCalendar c) {
        Long d = getDate();
        String res = c.set(d).toString();
        Long e = getEndDate();
        if (e == null) return res;
        return res + " - " + c.set(e).toString();
    }

    public String getFormattedDate() {
        return getFormattedDate(getCalendar());
    }

    public String toString() {
        return "[" + toString(getType().name(), getFormattedAmount().toString(), getFormattedDate(), getAddType().getValue(), getDescription(), getActivity(), hasResource() ? getResource().toString() : null) + "]";
    }

    public static String toString(String type, String amount, String date, String addType, String desc, String activity, String resource) {
        StringBuffer sb = new StringBuffer(TEMPLATE_MAIN.replace(TYPE_TMP, type).
                replace(AMOUNT_TMP, amount).
                replace(DATE_TMP, date).
                replace(ADD_TYPE_TMP, addType));
        if (desc != null && !desc.isEmpty()) {
            sb.append(TEMPLATE_DESCRIPTION.replace(DESC_TMP, desc));
        }
        if (activity != null && !activity.isEmpty()) {
            sb.append(TEMPLATE_ACTIVITY.replace(ACTIVITY_TMP, activity));
        }
        if (resource != null && !resource.isEmpty()) {
            sb.append(TEMPLATE_RESOURCE.replace(RESOURCE_TMP, resource));
        }
        return sb.toString();
    }

    public PSCalendar getCalendar() {
        if (work != null) return work.getCalendar();
        return super.getCalendar();
    }

    public Double getValue(Long start, Long end) {
        return getValue(getCalendar().set(start), getCalendar().set(end));
    }

    public Double getValue(String start, String end) {
        return getValue(getCalendar().set(start), getCalendar().set(end));
    }

    public Double getValue(PSCalendar periodStart, PSCalendar periodEnd) {
        periodStart.toStart();
        periodEnd.toEnd();
        PSCalendar costStart = getPSDate();
        PSCalendar costEnd = getPSEndDate();
        if (costEnd == null) {
            if (periodStart.lessOrEqual(costStart) && costStart.lessOrEqual(periodEnd)) {
                PSLogger.debug2(this + " => period=(" + periodStart + "," + periodEnd + "),res=(" + getAmount() + ")");
                return getAmount();
            } else {
                PSLogger.debug2(this + " => out of period=(" + periodStart + "," + periodEnd + ")[" + costStart.getTime() + "," + periodStart.getTime() + "," + periodEnd.getTime() + "]");
                return null;
            }
        }
        costEnd.toEnd();
        //costStart = costStart.nextWorkDate(true);
        //costEnd = costEnd.nextWorkDate(false);
        //periodStart = periodStart.nextWorkDate(true);
        //periodEnd = periodEnd.nextWorkDate(false);

        if (!periodStart.less(costEnd) || !costStart.less(periodEnd)) {
            PSLogger.debug2(this + " => out of period=(" + periodStart + "," + periodEnd + ")");
            return null;
        }

        double num = costEnd.diffInWorkDays(costStart);
        double perDay = getAmount() / num; // density

        PSCalendar start = periodStart.lessOrEqual(costStart) ? costStart : periodStart;
        PSCalendar end = costEnd.lessOrEqual(periodEnd) ? costEnd : periodEnd;
        double num2 = end.diffInWorkDays(start);
        if (num2 <= 0) {
            PSLogger.debug2(this + " => out of new_period=(" + start + "," + end + ")");
            return null;
        }

        Double res = perDay * num2;
        PSLogger.debug2(this + " => new_period=(" + start + "," + end + "),cost=(" + costStart + "," + costEnd + "),num=(" + num + "),num2=(" + num2 + "),res=(" + res + ")");
        return res;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Cost)) return false;
        Cost c = (Cost) o;
        return getFormattedAmount().equals(c.getFormattedAmount()) && getFormattedDate().equals(c.getFormattedDate()) &&
                equals(getDescription(), c.getDescription()) &&
                equals(getAddType(), c.getAddType()) && equals(getActivity(), c.getActivity()) && equals(getResource(), c.getResource());
    }

    static boolean equals(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    public static class Time extends Cost {

        public Time(Double hour, Double amount, long date) { // actual.
            super(Type.ACTUAL, amount, null, date);
            setAddType(AddType.TIMESHEETS);
            setHours(hour);
        }

        protected Time(Config c) {
            super(c);
        }

        public void setHours(Double d) {
            set("hours", d);
        }

        public Double getHours() {
            try {
                return Double.parseDouble(getString("hours"));
            } catch (NumberFormatException nfe) {
                return null;
            }
        }

        public boolean equals(Object o) {
            return super.equals(o) && equals(getHours(), ((Time) o).getHours());
        }

        public String toString() {
            return super.toString() + ",hours=" + getHours();
        }

    }
}
