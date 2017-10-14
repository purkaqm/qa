package com.powersteeringsoftware.tests.permissions.categories;

import com.powersteeringsoftware.libs.objects.PSPermissions;
import com.powersteeringsoftware.libs.tests.actions.PermissionsManager;
import org.testng.annotations.Test;

public class CreateCategory extends NewCategoryDriver {

    /**
     * 'Create New Category' tests
     */
    @Test(groups = {MAIN_GROUP, NEW_GROUP},
            dataProvider = "customPolicies")
    public void createCategory(PermissionsData data) {
        PSPermissions.CustomCategory c = data.getCategory();
        PermissionsManager.createCategory(c, true, false);
        PermissionsManager.createCategory(c, false, true);
    }


    @Override
    public String getTestName() {
        return "Create permissions category";
    }

}
