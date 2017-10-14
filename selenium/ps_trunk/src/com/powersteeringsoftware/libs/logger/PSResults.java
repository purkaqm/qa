package com.powersteeringsoftware.libs.logger;

import com.powersteeringsoftware.libs.util.bugtrackers.KnownIssue;

import static com.powersteeringsoftware.libs.logger.AbstractPSHTMLLayout.HTMLColorLevel.*;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 30.04.11
 * Time: 19:00
 * To change this template use File | Settings | File Templates.
 */
public enum PSResults {
    TITLE(LIGHT_LIGHT_GRAY),
    UNKNOWN(GRAY),
    OK(BLUE),
    WARN(YELLOW),
    ERROR(RED),
    FATAL(DARK_RED),
    SKIP(YELLOW),
    KNIS(RED),;

    private AbstractPSHTMLLayout.HTMLColorLevel color;
    private KnownIssue num;

    PSResults(AbstractPSHTMLLayout.HTMLColorLevel color) {
        this.color = color;
    }

    public String color() {
        return color.getName();
    }

    public String toString() {
        if (equals(KNIS) && num != null) {
            return "<a href='" + num.getUrl() + "'>" + num.getId() + "</a>";
        }
        return super.name();
    }

    public static PSResults getKnis(String s) {
        PSResults res = KNIS;
        if (s != null && !s.isEmpty()) {
            res.num = KnownIssue.addToList(s);
        }
        return res;
    }
}
