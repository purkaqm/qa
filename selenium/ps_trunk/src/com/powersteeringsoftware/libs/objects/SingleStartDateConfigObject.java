package com.powersteeringsoftware.libs.objects;

import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.tests_data.Config;

/**
 * Created by admin on 09.04.14.
 */
public abstract class SingleStartDateConfigObject extends ConfigObject {

    protected SingleStartDateConfigObject(Config config) {
        super(config);
    }

    protected SingleStartDateConfigObject() {
        super();
    }

    public void setDate(long d) {
        super.setDate(DATE, d);
    }

    public void setDate(String s) {
        setDate(getCalendar().set(s));
    }

    public void setDate(PSCalendar c) {
        c.toStart();
        setDate(c.getTime());
    }

    public Long getDate() {
        return getDateAsLong(DATE);
    }

    public boolean hasDate() {
        return getConfig().hasChild(DATE);
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
}
