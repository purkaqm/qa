package com.powersteeringsoftware.libs.util;

import com.powersteeringsoftware.libs.settings.CoreProperties;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class TimeStampName {
    public static final String DEFAULT_TIME_PATTERN = "yyyy.MM.dd_HH:mm:ss_z";
    public static final Locale DEFAULT_LOCALE = Locale.US;
    public static final String DEFAULT_TIME_ZONE = "EDT";

    /**
     * Timestamp to be used in a current test suite run
     */
    public static final String TIMESTAMP = getTimeStamp();

    private String timePattern;
    private String timeZone;
    private Locale locale;
    private String name;

    public TimeStampName() {
        this("");
    }

    public TimeStampName(String name) {
        this(name, DEFAULT_TIME_PATTERN, DEFAULT_TIME_ZONE, DEFAULT_LOCALE);
    }

    public TimeStampName(String name, String timeTimePattern, String timeZone, Locale locale) {
        this.name = name;
        this.timePattern = timeTimePattern;
        this.timeZone = timeZone;
        this.locale = locale;
    }

    public String getTimeStampedName() {
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_TIME_PATTERN, DEFAULT_LOCALE);
        formatter.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(CoreProperties.getTestTemplate());
        return name + "_" + formatter.format(c.getTime());
    }

    public static String getTimeStampedName(String name) {
        return new TimeStampName(name).getTimeStampedName();
    }

    /**
     * @return Short time stamp (HHmmss)
     */
    private static String getTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss", DEFAULT_LOCALE);
        formatter.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
        return formatter.format(CoreProperties.getTestTemplate());
    }


}
