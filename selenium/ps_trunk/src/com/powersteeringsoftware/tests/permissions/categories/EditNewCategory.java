package com.powersteeringsoftware.tests.permissions.categories;

import com.powersteeringsoftware.libs.objects.PSPermissions;
import com.powersteeringsoftware.libs.tests.actions.PermissionsManager;
import org.testng.annotations.Test;

public class EditNewCategory extends NewCategoryDriver {

    @Test(groups = {MAIN_GROUP, EDIT_GROUP},
            dependsOnGroups = NEW_GROUP,
            dataProvider = "customPolicies")
    public void editNewCategory(PermissionsData data) {
        PSPermissions.CustomCategory category = data.getCategory();
        PSPermissions edit1 = data.getEdit1();
        PSPermissions edit2 = data.getEdit2();
        // category is created. set name and target to edit data
        edit1.setCategoryTarget(category.getObjectType());
        edit2.setCategoryTarget(category.getObjectType());
        edit1.getCategoryRoles().get(0).setName(category.getName());
        edit2.getCategoryRoles().get(0).setName(category.getName());

        // do test:
        PermissionsManager.setDefine(edit1);
        PermissionsManager.setDefine(edit2);

    }

    public String getTestName() {
        return "Edit New permissions category";
    }

}
