package com.powersteeringsoftware.libs.logger;

import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sergey.zuev
 * Date: 07.12.2009
 * Time: 11:11:12
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPSHTMLLayout extends Layout {
    private final int DEFAULT_SIZE = 256;
    private final int UPPER_LIMIT = 2048;

    protected StringBuffer buf = new StringBuffer(DEFAULT_SIZE);
    protected String baseDir;

    /**
     * No options to activate.
     */
    public void activateOptions() {
    }

    /**
     * The XMLLayout prints and does not ignore exceptions. Hence the
     * return value <code>false</code>.
     */
    public boolean ignoresThrowable() {
        return false;
    }

    protected abstract void appendCss();

    public String format(LoggingEvent event) {
        // Reset working buffer. If the buffer is too large, then we need a new
        // one in order to avoid the penalty of creating a large array.
        if (buf.capacity() > UPPER_LIMIT) {
            buf = new StringBuffer(DEFAULT_SIZE);
        } else {
            buf.setLength(0);
        }
        try {
            return _format(event);
        } catch (Exception e) {
            return "";
        }
    }


    protected abstract String _format(LoggingEvent event);

    public String getRelativeWebPath(String file) {
        if (file == null) return null;
        if (baseDir == null) {
            baseDir = PSLogger.getLogsDir().getAbsolutePath();
        }

        return getRelativeWebPath(baseDir, file);
    }

    protected static String getRelativeWebPath(String base, String file) {
        String[] aBase = getPathParts(base);
        String[] aFile = getPathParts(file);
        int j = -1;
        for (int i = 0; i < aBase.length; i++) {
            if (aBase[i].equals(aFile[i])) {
                j = i;
            } else if (j != -1) {
                break;
            }
        }
        if (j == -1) {
            return null;
        }
        j++;
        String res = "";
        String pref = "..";
        for (int i = 0; i < aBase.length - j; i++) {
            res += pref + "/";
        }
        for (int i = j; i < aFile.length; i++) {
            res += aFile[i] + "/";
        }
        res = "./" + res.replaceFirst("/$", "");
        return res;
    }

    protected static String[] getPathParts(String path) {
        try {
            if (path.matches("[A-Za-z]:.*"))
                return path.replaceAll("\\\\", "/").split("/+");
            else
                return path.split("/+");
        } catch (Exception e) {
            return new String[0];
        }
    }


    protected String putUrls(String mes) {
        int n = 50;
        int j = 0;
        List<String> urls = new ArrayList<String>();
        while (mes.matches(".*http[s]*:/.*")) {
            if (j++ > n) break;
            String url = mes.replaceAll(".*(http[s]*://\\S+[^\\s.,>)\\];'\\\"!?]).*", "$1");
            String link = "<a href='" + url + "'>" + url + "</a>";
            urls.add(link);
            mes = mes.replace(url, "_link_" + (urls.size() - 1) + "_");
        }
        for (int i = 0; i < urls.size(); i++) {
            mes = mes.replace("_link_" + i + "_", urls.get(i));
        }
        return mes;
    }

    enum HTMLColorLevel {
        BACKGROUND("silver"),
        GRAY("gray", "gr"),
        DARK_RED("darkred", "rr"),
        RED("red", "r"),
        YELLOW("yellow", "y"),
        BLUE("blue", "b"),
        LIGHT_LIGHT_GRAY("#D0D0D0", "llgr"),
        LIGHT_GRAY("blueviolet", "lgr"),
        PINK("pink", "p"),
        ORANGE("orange", "o"),
        DARK_GREEN("#002E00", "g2", 11),
        GREEN("green", "g", 11),;

        private HTMLColorLevel(String name) {
            this.name = name;
        }

        private HTMLColorLevel(String name, String clazz) {
            this(name);
            this.clazz = clazz;
        }

        private HTMLColorLevel(String name, String clazz, int size) {
            this(name, clazz);
            fontSize = size;
        }

        private String name;
        private String clazz;
        private Integer fontSize;

        public String getName() {
            return name;
        }

        public String toString() {
            return getName();
        }

        public String getClazz() {
            return clazz;
        }

        public Integer getFontSize() {
            return fontSize;
        }

        public static HTMLColorLevel getLogColor(Level level) {
            if (level.equals(PSLevel.FATAL)) return DARK_RED;
            if (level.equals(PSLevel.ERROR)) return RED;
            if (level.equals(PSLevel.KNIS)) return RED;
            if (level.equals(PSLevel.WARN)) return YELLOW;

            if (level.equals(PSLevel.TASK)) return BLUE;
            if (level.equals(PSLevel.INFO)) return LIGHT_GRAY;

            if (level.equals(PSLevel.SKIP)) return PINK;
            if (level.equals(PSLevel.SAVE)) return ORANGE;
            if (level.equals(PSLevel.DEBUG2)) return DARK_GREEN;
            return GREEN;
        }
    }


}
