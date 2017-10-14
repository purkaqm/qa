package com.powersteeringsoftware.tests.permissions.categories;

import com.powersteeringsoftware.libs.objects.PSPermissions;
import com.powersteeringsoftware.libs.tests.actions.PermissionsManager;
import org.testng.annotations.Test;

public class DeleteCategory extends NewCategoryDriver {

    @Test(groups = {MAIN_GROUP, DELETE_GROUP},
            dependsOnGroups = {
                    NEW_GROUP,
                    EDIT_GROUP},
            dataProvider = "customPolicies")
    public void deleteCategory(PermissionsData d) {
        PSPermissions.CustomCategory c = d.getCategory();
        PermissionsManager.deleteCategory(c, false);
        PermissionsManager.deleteCategory(c, true);
    }

    @Override
    public String getTestName() {
        return "Delete permissions category";
    }

}
