package com.powersteeringsoftware.libs.objects;

import com.powersteeringsoftware.libs.objects.works.WorkType;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.session.TestSession;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Set;

/**
 * Created by admin on 09.04.14.
 */
public class ConfigurableRole extends ConfigObject implements Role, Comparable {
    public static final String NAME = "configurable-role";

    public ConfigurableRole(PSPermissions.CustomPSRole role) {
        super();
        for (Config c : role.getConfig().getChilds()) {
            try {
                getConfig().addChild(c.copy(false));
            } catch (ParserConfigurationException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public ConfigurableRole(Config c) {
        super(c);
    }

    public boolean equals(Object o) {
        return o instanceof ConfigurableRole && getName().equals(((ConfigurableRole) o).getName());
    }

    public String toString() {
        return getName();
    }

    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public void setCreated() {
        super.setCreated();
        TestSession.putRole(this);
    }

    @Override
    public void setDeleted() {
        super.setDeleted();
        TestSession.removeRole(this);
    }

    @Override
    public int compareTo(Object o) {
        //todo
        return toString().compareTo(o.toString());
    }

    public Set<WorkType> getWorkTypes() {
        return getWorkTypes(this);
    }
}
