package com.powersteeringsoftware.tests.permissions.categories;

import com.powersteeringsoftware.libs.objects.PSPermissions;
import com.powersteeringsoftware.tests.permissions.TestDriver;
import org.testng.annotations.DataProvider;

import static com.powersteeringsoftware.tests.permissions.TestData.*;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 10.09.12
 * Time: 19:37
 * To change this template use File | Settings | File Templates.
 */
public abstract class NewCategoryDriver extends TestDriver {

    public static final String NEW_GROUP = "permissions.category.create";
    public static final String EDIT_GROUP = "permissions.newcategory.edit";
    public static final String DELETE_GROUP = "permissions.category.delete";

    @DataProvider(name = "customPolicies")
    public Object[][] customData() {
        return new Object[][]{
                new Object[]{new PermissionsData(NEW_CATEGORY_WI, NEW_CATEGORY_EDIT_1, NEW_CATEGORY_EDIT_2)},
                new Object[]{new PermissionsData(NEW_CATEGORY_GRP, NEW_CATEGORY_EDIT_1, NEW_CATEGORY_EDIT_2)},
                new Object[]{new PermissionsData(NEW_CATEGORY_MTB, NEW_CATEGORY_EDIT_5, NEW_CATEGORY_EDIT_6)},
                new Object[]{new PermissionsData(NEW_CATEGORY_MT, NEW_CATEGORY_EDIT_1, NEW_CATEGORY_EDIT_2)},
                new Object[]{new PermissionsData(NEW_CATEGORY_USR, NEW_CATEGORY_EDIT_1, NEW_CATEGORY_EDIT_2)},
                new Object[]{new PermissionsData(NEW_CATEGORY_CTX, NEW_CATEGORY_EDIT_3, NEW_CATEGORY_EDIT_4)}
        };
    }

    protected class PermissionsData {
        private PSPermissions edit1;
        private PSPermissions edit2;
        private PSPermissions.CustomCategory category;
        private PermissionsData(String id, String id1, String id2) {
            category = getTestData().getPermissionCategory(id);
            edit1 = getTestData().getPermissions(id1);
            edit2 = getTestData().getPermissions(id2);
        }
        public PSPermissions.CustomCategory getCategory() {
            return category;
        }
        public PSPermissions getEdit1() {
            return edit1;
        }
        public PSPermissions getEdit2() {
            return edit2;
        }
        public String toString() {
            return category.getObjectType().toString();
        }
    }
}
