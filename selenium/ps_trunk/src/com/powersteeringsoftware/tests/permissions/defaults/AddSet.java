package com.powersteeringsoftware.tests.permissions.defaults;

import com.powersteeringsoftware.libs.objects.PSPermissions;
import com.powersteeringsoftware.libs.tests.actions.PermissionsManager;
import com.powersteeringsoftware.tests.permissions.TestData;
import com.powersteeringsoftware.tests.permissions.TestDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


/**
 * @author vsumenkov
 */
public class AddSet extends TestDriver {
    public static final String DEFAULT_GROUP = "default.permissions";

    @Test(groups = {MAIN_GROUP, DEFAULT_GROUP}, description = "Create custom core set")
    public void createCoreSet() {
        /* --- Create new role --- */
        PSPermissions.CustomPSRole role = getTestData().getPermissionRole(TestData.ADD_SETS_CORE);
        PSPermissions.CustomPSRole edit = getTestData().getPermissionRole(TestData.ADD_SETS_CORE_EDIT);
        PermissionsManager.createRole(role);

        // Assure it contains valid data
        PermissionsManager.validateRole(role);

        /* --- Try to create role with the same data --- */
        String err = PermissionsManager.doCreateRole(role);
        Assert.assertNotNull(err, "Should be error");

        /* --- Update role --- */
        PermissionsManager.editRole(role, edit);
        role.merge(edit);

        //Ensure the field is modified
        PermissionsManager.validateRole(role);


        /* --- Delete role --- */
        PermissionsManager.deleteRole(role);
    }

    @Test(groups = {MAIN_GROUP, DEFAULT_GROUP},
            dataProvider = "customPolicies")
    public void createCustomSet(PermissionsData data) {
        PSPermissions p = data.get();
        PermissionsManager.setDefault(p);
        PermissionsManager.deleteUser(p.getCategoryTarget(), p.getUsers().get(0));
    }

    @DataProvider(name = "customPolicies")
    public Object[][] customData() {
        return new Object[][]{
                new Object[]{new PermissionsData(TestData.ADD_SETS_CUSTOM_CTX)},
                new Object[]{new PermissionsData(TestData.ADD_SETS_CUSTOM_GRP)},
                new Object[]{new PermissionsData(TestData.ADD_SETS_CUSTOM_MTB)},
                new Object[]{new PermissionsData(TestData.ADD_SETS_CUSTOM_MT)},
                new Object[]{new PermissionsData(TestData.ADD_SETS_CUSTOM_USR)},
                new Object[]{new PermissionsData(TestData.ADD_SETS_CUSTOM_WI)}
        };
    }

    private class PermissionsData {
        private PSPermissions p;

        private PermissionsData(String id) {
            p = getTestData().getPermissions(id);
        }

        public PSPermissions get() {
            return p;
        }

        public String toString() {
            return p.getCategoryTarget().toString();
        }
    }

    @Override
    public String getTestName() {
        return "Default Permissions";
    }

}
