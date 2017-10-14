package com.powersteeringsoftware.tests.permissions.categories;

import com.powersteeringsoftware.libs.objects.PSPermissions;
import com.powersteeringsoftware.libs.tests.actions.PermissionsManager;
import com.powersteeringsoftware.tests.permissions.TestDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.powersteeringsoftware.tests.permissions.TestData.*;

//@Test (groups="permissions.test")
public class EditBasics extends TestDriver {

    @Test(groups = MAIN_GROUP,
            dataProvider = "customPolicies")
    public void editBasicCategories(PermissionsData data) {
        PSPermissions edit1 = data.getEdit1();
        PSPermissions edit2 = data.getEdit2();

        PermissionsManager.setDefine(edit1);
        PermissionsManager.setDefine(edit2);
    }

    @DataProvider(name = "customPolicies")
    public Object[][] customData() {
        return new Object[][]{
                new Object[]{new PermissionsData(BASIC_CTX_1, BASIC_CTX_2)},
                new Object[]{new PermissionsData(BASIC_GRP_1, BASIC_GRP_2)},
                new Object[]{new PermissionsData(BASIC_MT_1, BASIC_MT_2)},
                new Object[]{new PermissionsData(BASIC_MTB_1, BASIC_MTB_2)},
                new Object[]{new PermissionsData(BASIC_USR_1, BASIC_USR_2)},
                new Object[]{new PermissionsData(BASIC_WI_1, BASIC_WI_2)}
        };
    }

    protected class PermissionsData {
        private PSPermissions edit1;
        private PSPermissions edit2;
        private PermissionsData(String id1, String id2) {
            edit1 = getTestData().getPermissions(id1);
            edit2 = getTestData().getPermissions(id2);
        }

        public PSPermissions getEdit1() {
            return edit1;
        }
        public PSPermissions getEdit2() {
            return edit2;
        }
        public String toString() {
            return edit1.getCategoryTarget().toString();
        }
    }

    @Override
    public String getTestName() {
        return "Edit Basic permissions";
    }

}
