package com.powersteeringsoftware.libs.objects;

import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 09.04.14.
 */
public enum BuiltInRole implements Role {
    OWNER("Owner"),
    CONTRIBUTOR("Contributor"),
    CHAMPION("Champion"),
    FINANCIAL_REP("Financial Rep");
    private String name;

    BuiltInRole(String _name) {
        name = _name;
    }

    public String getName() {
        return name;
    }


    public static Role getRoleByName(String str) {
        for (Role r : TestSession.getRoleList()) {
            if (r.getName().equals(str)) {
                return r;
            }
        }
        Assert.fail("Can't find role '" + str + "'");
        return null;
    }

    public static List<Role> getList() {
        List<Role> res = new ArrayList<Role>();
        for (BuiltInRole r : values()) res.add(r);
        return res;
    }


}
