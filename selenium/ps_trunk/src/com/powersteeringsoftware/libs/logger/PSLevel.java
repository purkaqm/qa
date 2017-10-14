package com.powersteeringsoftware.libs.logger;

import org.apache.log4j.Level;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 22.04.2010
 * Time: 12:47:35
 * To change this template use File | Settings | File Templates.
 */
public class PSLevel extends Level {
    public static final int SAVE_INT = Level.INFO_INT - 20;
    public static final int TASK_INT = Level.WARN_INT - 10;
    public static final int KNIS_INT = Level.WARN_INT - 5;
    public static final int SKIP_INT = Level.INFO_INT - 10;
    public static final int DEBUG2_INT = Level.DEBUG_INT + 10;
    public static final int UPPER_INFO_INT = Level.FATAL_INT + 10;

    public static final String SAVE_STR = "SAVE";
    public static final String TASK_STR = "TEST";
    private static final String KNIS_STR = "KNIS";
    public static final String SKIP_STR = "SKIP";
    public static final String DEBUG2_STR = "DEBUG";
    public static final String UPPER_INFO_STR = "TOSHORTLOG";

    public static final PSLevel SAVE = new PSLevel(SAVE_INT, SAVE_STR, 7);
    public static final PSLevel TASK = new PSLevel(TASK_INT, TASK_STR, 7);
    public static final PSLevel KNIS = new PSLevel(KNIS_INT, KNIS_STR, 7);
    public static final PSLevel SKIP = new PSLevel(SKIP_INT, SKIP_STR, 7);
    public static final PSLevel DEBUG2 = new PSLevel(DEBUG2_INT, DEBUG2_STR, 7);
    public static final PSLevel UPPER_INFO = new PSLevel(UPPER_INFO_INT, UPPER_INFO_STR, 7);

    private String altStr;

    protected PSLevel(int level, String levelStr, int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }

    public static Level toLevel(String sArg) {
        return toLevel(sArg, PSLevel.INFO);
    }

    public static Level toLevel(String sArg, Level defaultValue) {
        if (sArg == null) {
            return defaultValue;
        }
        String stringVal = sArg.toUpperCase();
        if (stringVal.equals(SAVE_STR)) {
            return PSLevel.SAVE;
        }
        if (stringVal.equals(TASK_STR)) {
            return PSLevel.TASK;
        }
        if (stringVal.equals(SKIP_STR)) {
            return PSLevel.SKIP;
        }
        if (stringVal.equals(UPPER_INFO_STR)) {
            return PSLevel.UPPER_INFO;
        }
        if (stringVal.equals(KNIS_STR)) {
            return PSLevel.KNIS;
        }
        if (stringVal.equals(DEBUG2_STR)) {
            return PSLevel.DEBUG2;
        }
        return Level.toLevel(sArg, defaultValue);
    }

    public static Level toLevel(int i) throws IllegalArgumentException {
        switch (i) {
            case SAVE_INT:
                return PSLevel.SAVE;
            case TASK_INT:
                return PSLevel.TASK;
            case SKIP_INT:
                return PSLevel.SKIP;
            case UPPER_INFO_INT:
                return PSLevel.UPPER_INFO;
            case KNIS_INT:
                return PSLevel.KNIS;
        }
        return Level.toLevel(i);
    }

}
