package com.powersteeringsoftware.libs.settings;

import com.powersteeringsoftware.libs.tests_data.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Supported versions of PowerSteering</br>
 * Note: Order is important!
 */
public enum PowerSteeringVersions {

    _7_0("7.0"),
    _7_1("7.1"),
    _7_1_1("7.1.1"),
    _7_2("7.2"),
    _8_0("8.0"),
    _8_1("8.1"),
    _8_2("8.2"),
    _8_2_1("8.2.1"),
    _8_2_2("8.2.2"),
    _9_0("9.0"),
    _9_0_1("9.0.1"),
    _9_0_2("9.0.2"),
    _9_0_3("9.0.3"),
    _9_0_4("9.0.4"),
    _9_1("9.1"),
    _9_1_1("9.1.1"),
    _9_2("9.2"),
    _9_3("9.3"),
    _9_3_1("9.3.1"),
    _9_4("9.4"),
    _10_0("10.0"),
    TBD("TBD"),
    _10_1("10.1"),
    _10_5("10.5"),
    _11_0("11.0"),
    _11_SPRING("Spring_2014:_V11"),
    _V11_SPRING("Spring 2014: V11"),
    _11_5("11.5"),
    _12("Fall 2014"),
    _12_1("Winter 2015"), //v12 now
    _13("v13"),
    _14("v14"),
    _15("trunk");

    String value;

    PowerSteeringVersions(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    public boolean verLessThan(PowerSteeringVersions test) {
        return test != null && compareTo(test) < 0;
    }

    public boolean verGreaterThan(PowerSteeringVersions test) {
        return test != null && compareTo(test) > 0;
    }

    public boolean verGreaterOrEqual(PowerSteeringVersions test) {
        return test != null && compareTo(test) >= 0;
    }

    public boolean verLessOrEqual(PowerSteeringVersions test) {
        return test != null && compareTo(test) <= 0;
    }

    public boolean verEqualsTo(PowerSteeringVersions test) {
        return test != null && compareTo(test) == 0;
    }

    public boolean inRange(PowerSteeringVersions ver1, PowerSteeringVersions ver2) {
        return compareTo(ver1) >= 0 && compareTo(ver2) <= 0;
    }

    public static PowerSteeringVersions valueOf(Config conf) {
        return valueOf(conf, "ps");
    }

    public static PowerSteeringVersions valueOf(Config conf, String name) {
        String attr;
        if (conf != null && (attr = conf.getAttributeValue(name)) != null)
            return valueOf(attr);
        return null;
    }

    public static List<PowerSteeringVersions> versionsLessOrEqual(PowerSteeringVersions ver) {
        List<PowerSteeringVersions> vers = new ArrayList<PowerSteeringVersions>();
        for (PowerSteeringVersions v : PowerSteeringVersions.values()) {
            if (v.verLessOrEqual(ver)) {
                vers.add(v);
            }
        }
        Collections.reverse(vers);
        return vers;
    }
}
