package com.powersteeringsoftware.libs.objects;

import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.session.TestSession;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 27.09.12
 * Time: 17:02
 * To change this template use File | Settings | File Templates.
 */
public class UixFeature extends ConfigObject {
    public static final String NAME = "uix-feature";

    public enum Code {
        RESOURCE_PLANING("resource-planning"),
        IMMEDIATE_DELEGATION("immediate.delegation"),
        RESOURCE_MANAGER("resource-manager"),;
        private String name;

        Code(String s) {
            name = s;
        }

        public String getName() {
            return name;
        }
    }

    public UixFeature(Config c) {
        super(c);
    }

    public UixFeature(String name) {
        super();
        if (name == null) throw new NullPointerException("UixFeature name should not be null");
        setName(name);
    }

    public Integer getValue() {
        return getInteger("value");
    }

    public String getStringValue() {
        Integer value = getValue();
        if (value == null) {
            return null;
        }
        if (value == 0) return "on";
        if (value == 1) return "off";
        if (value == 2) return "def on";
        if (value == 3) return "def off";
        return null;
    }

    public void setValue(Integer value) {
        set("value", value);
    }

    public boolean equals(Object o) {
        return o != null && o instanceof UixFeature && getName().equals(((UixFeature) o).getName());
    }

    public String toString() {
        return getName() + "[" + getStringValue() + "]";
    }

    public boolean exist() {
        return true;
    }

    public boolean doUseTimestamp() {
        return false;
    }

    public void setCreated() {
        TestSession.setUixFeature(this);
    }

    public Object clone() {
        try {
            return new UixFeature(conf.copy(true));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
