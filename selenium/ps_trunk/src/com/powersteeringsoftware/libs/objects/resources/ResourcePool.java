package com.powersteeringsoftware.libs.objects.resources;

import com.powersteeringsoftware.libs.objects.ConfigObject;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created by admin on 22.04.2014.
 */
public class ResourcePool extends ConfigObject implements Comparable {
    public static final String NAME = "pool";

    public ResourcePool(Config c) {
        super(c);
        if (getConfigId() == null)
            throw new IllegalStateException("Config is incorrect. id is mandatory");
    }

    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(Object o) {
        return toString().compareTo(o.toString());
    }

    @Override
    public void setCreated() {
        super.setCreated();
        TestSession.putResourcePool(this);
    }

    @Override
    public void setDeleted() {
        super.setDeleted();
        TestSession.removeResourcePool(this);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ResourcePool && getConfigId().equals(((ResourcePool) o).getConfigId());
    }
}
