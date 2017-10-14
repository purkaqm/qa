package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 29.06.11
 * Time: 19:32
 * TODO: rewrite this
 */
public enum AddEditMeasurePageLocators implements ILocatorable {
    BLOCK("//div[@class='box']"),
    URL("/measures/AddEditMeasure.epage"),
    NAME("name"),
    DESCRIPTION("//textarea[@id='description']"),
    UNITS("units"),
    DATA_COLLECTION_MANUAL("manual"),
    DATA_COLLECTION_FORMULA("auto"),
    FORMULA_BODY("//tr[@id='tr_formula']"),
    SHOW_CLASS("show"),
    HIDE_CLASS("hide"),
    DISPLAY_FORMAT_SELECT("field_format"),
    DISPLAY_FORMAT_PRECISION_SELECT("PropertySelection_1"),
    DISPLAY_FORMAT_SCALE_SELECT("PropertySelection_0"),
    EFFECTIVE_DATES_SELECT("effectiveDates"),
    EFFECTIVE_DATES_ABSOLUTE_START("id=widget_startDate"),
    EFFECTIVE_DATES_ABSOLUTE_END("id=widget_endDate"),

    TEST_SCHEDULE("Test Schedule"),
    HISTORY_SCHEDULE("History Schedule"),
    REMINDER_SCHEDULE("Reminder Schedule"),

    SCHEDULES_TR("//tr[contains(@id, '_schedule') and @class='" + SHOW_CLASS.locator + "']"),
    SCHEDULE_TH("//th"),
    SCHEDULE_TH_DIV("//th/div[@class='" + SHOW_CLASS.locator + "']"),
    SCHEDULE_RADIO("//td/input[@type='radio']"),
    SCHEDULE_RADIO_VALUE("value"),
    SCHEDULE_RADIO_LABEL("//label/span"),
    SCHEDULE_BLOCK("//div[contains(@id, '" + LOCATOR_REPLACE_PATTERN + "')]"),
    SCHEDULE_DAILY_INPUT("//input[contains(@id, 'dayInterval')]"),
    SCHEDULE_COMBOBOX("//div[contains(@id, 'widget_PropertySelection')]"),
    SCHEDULE_COMBOBOX_FORMAT("hh:mm aa"),


    INDICATOR_TYPE_VARIANCE("variance"),
    INDICATOR_TYPE_GOAL("goal"),
    INDICATOR_TYPE_NONE("none"),

    THRESHOLD_VARIANCE_1("TextField_3", "varianceInd"),
    THRESHOLD_VARIANCE_2("TextField_5", "varianceInd_0"),
    THRESHOLD_VARIANCE_3("TextField_7", "varianceInd_1"),
    THRESHOLD_VARIANCE_4("TextField_9", "varianceInd_2"),
    THRESHOLD_GOAL_1("achInd", "goalInd"),
    THRESHOLD_GOAL_2("achInd_0", "goalInd_0");

    String locator;

    AddEditMeasurePageLocators(String _locator) {
        locator = _locator != null ? _locator : "";
    }

    AddEditMeasurePageLocators(String loc70, String loc) {
        locator = TestSession.getAppVersion().verLessOrEqual(PowerSteeringVersions._7_0) ? loc70 : loc;
    }

    public String getLocator() {
        return locator;
    }

    private static String replace(String init, String pat, Object rep) {
        String s;
        if (rep == null) {
            s = "";
        }
        if (rep instanceof ILocatorable) {
            s = ((ILocatorable) rep).getLocator();
        } else {
            s = String.valueOf(rep);
        }
        return init.replace(pat, s);
    }

    public String replace(Object o) {
        return replace(locator, LOCATOR_REPLACE_PATTERN, o);
    }

    public String replace(Object o1, Object o2) {
        return replace(replace(o1), LOCATOR_REPLACE_PATTERN_2, o2);
    }

    /**
     * Schedule items: Reminder, History, Test
     */
    public enum Schedule {
        NEVER(0, "nlayeruid"),
        DAILY(1, "dlayeruid"),
        WEEKLY(2, "wlayeruid"),
        MONTHLY(3, "mlayeruid"),
        QUARTERLY(4, "qlayeruid");

        private int index;
        private String id;

        Schedule(int aIndex, String id) {
            index = aIndex;
            this.id = id;
        }

        public int getIndex() {
            return index;
        }

        public String getBlockLoc() {
            return SCHEDULE_BLOCK.locator.replace(LOCATOR_REPLACE_PATTERN, id);
        }
    }

    public static String getTimeOfDay(int min) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(min * 60 * 1000);
        return getDateFormat().format(c.getTime());
    }

    public static int getMinOfDay(String time) {
        Calendar c = new GregorianCalendar();
        try {
            c.setTime(getDateFormat().parse(time));
        } catch (ParseException e) {
            return -1;
        }
        return (int) (c.getTimeInMillis() / 60 / 1000);
    }

    private static DateFormat getDateFormat() {
        SimpleDateFormat res = new SimpleDateFormat(SCHEDULE_COMBOBOX_FORMAT.locator);
        res.setTimeZone(TimeZone.getTimeZone("GMT"));
        return res;
    }
}
