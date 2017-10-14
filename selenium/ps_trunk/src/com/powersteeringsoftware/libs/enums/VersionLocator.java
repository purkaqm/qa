package com.powersteeringsoftware.libs.enums;

import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by admin on 09.10.2014.
 */
public class VersionLocator implements Comparable<VersionLocator> {
    private PowerSteeringVersions ver;
    private Object loc;

    @Override
    public int compareTo(VersionLocator o) {
        return -ver.compareTo(o.ver);
    }

    private static VersionLocator get(PowerSteeringVersions v, Object loc) {
        VersionLocator res = new VersionLocator();
        res.ver = v;
        res.loc = loc;
        return res;
    }

    public static VersionLocator _8_0(final Object loc) {
        return VersionLocator.get(PowerSteeringVersions._8_0, loc);
    }

    public static VersionLocator _9_0(final Object loc) {
        return VersionLocator.get(PowerSteeringVersions._9_0, loc);
    }

    public static VersionLocator _9_1(final Object loc) {
        return VersionLocator.get(PowerSteeringVersions._9_1, loc);
    }

    public static VersionLocator _9_2(final Object loc) {
        return VersionLocator.get(PowerSteeringVersions._9_2, loc);
    }

    public static VersionLocator _9_3(final Object loc) {
        return VersionLocator.get(PowerSteeringVersions._9_3, loc);
    }

    public static VersionLocator _9_4(final Object loc) {
        return VersionLocator.get(PowerSteeringVersions._9_4, loc);
    }

    public static VersionLocator _10_0(final Object loc) {
        return VersionLocator.get(PowerSteeringVersions._10_0, loc);
    }

    public static VersionLocator _11_0(final Object loc) {
        return VersionLocator.get(PowerSteeringVersions._11_0, loc);
    }

    public static VersionLocator _12_0(final Object loc) {
        return VersionLocator.get(PowerSteeringVersions._12, loc);
    }

    public static VersionLocator _12_1(final Object loc) {
        return VersionLocator.get(PowerSteeringVersions._12_1, loc);
    }

    public static VersionLocator _13(final Object loc) {
        return VersionLocator.get(PowerSteeringVersions._13, loc);
    }

    public static VersionLocator _14(final Object loc) {
        return VersionLocator.get(PowerSteeringVersions._14, loc);
    }

    public static VersionLocator _15(final Object loc) {
        return VersionLocator.get(PowerSteeringVersions._15, loc);
    }

    public Object get() {
        return loc;
    }

    public String get(ILocatorable... replacements) {
        if (replacements != null) {
            for (ILocatorable rep : replacements) {
                String sRep = rep.getLocator();
                if (sRep == null) return null;
                loc = ((String) loc).replace(rep.name(), sRep);
            }
        }
        return String.valueOf(loc);
    }

    public void set(String l) {
        loc = l;
    }

    private static VersionLocator empty = new VersionLocator();

    public static VersionLocator chooseLocator(VersionLocator... locs) {
        if (locs == null) return empty;
        if (locs.length == 0) return empty;
        if (locs.length == 1) return locs[0];
        if (getCurrentVersion() == null) return empty;
        List<VersionLocator> list = Arrays.asList(locs);
        Collections.sort(list);
        for (VersionLocator loc : list) {
            if (getCurrentVersion().verGreaterOrEqual(loc.ver)) {
                return loc;
            }
        }
        return list.get(list.size() - 1);
    }

    public static PowerSteeringVersions getCurrentVersion() {
        if (TestSession.isVersionPresent())
            return TestSession.getAppVersion();
        return null;
    }
}
