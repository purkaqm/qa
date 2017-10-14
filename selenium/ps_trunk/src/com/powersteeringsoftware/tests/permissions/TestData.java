package com.powersteeringsoftware.tests.permissions;

import com.powersteeringsoftware.libs.objects.PSPermissions;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import com.powersteeringsoftware.libs.util.TimeStampName;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 10.09.12
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */
public class TestData extends PSTestData {
    public static final String ADD_SETS_CORE = "core-set";
    public static final String ADD_SETS_CORE_EDIT = "core-set-edit";
    public static final String ADD_SETS_CUSTOM_WI = "custom-set-wi";
    public static final String ADD_SETS_CUSTOM_USR = "custom-set-usr";
    public static final String ADD_SETS_CUSTOM_GRP = "custom-set-grp";
    public static final String ADD_SETS_CUSTOM_MT = "custom-set-mt";
    public static final String ADD_SETS_CUSTOM_CTX = "custom-set-ctx";
    public static final String ADD_SETS_CUSTOM_MTB = "custom-set-mtb";

    public static final String NEW_CATEGORY_WI = "new-wi";
    public static final String NEW_CATEGORY_USR = "new-usr";
    public static final String NEW_CATEGORY_GRP = "new-grp";
    public static final String NEW_CATEGORY_MT = "new-mt";
    public static final String NEW_CATEGORY_CTX = "new-ctx";
    public static final String NEW_CATEGORY_MTB = "new-mtb";
    public static final String NEW_CATEGORY_EDIT_1 = "new-1";
    public static final String NEW_CATEGORY_EDIT_2 = "new-2";
    public static final String NEW_CATEGORY_EDIT_3 = "new-3";
    public static final String NEW_CATEGORY_EDIT_4 = "new-4";
    public static final String NEW_CATEGORY_EDIT_5 = "new-5";
    public static final String NEW_CATEGORY_EDIT_6 = "new-6";


    public static final String BASIC_WI_1 = "wi-1";
    public static final String BASIC_WI_2 = "wi-2";
    public static final String BASIC_USR_1 = "usr-1";
    public static final String BASIC_USR_2 = "usr-2";
    public static final String BASIC_GRP_1 = "grp-1";
    public static final String BASIC_GRP_2 = "grp-2";
    public static final String BASIC_MT_1 = "mt-1";
    public static final String BASIC_MT_2 = "mt-2";
    public static final String BASIC_CTX_1 = "ctx-1";
    public static final String BASIC_CTX_2 = "ctx-2";
    public static final String BASIC_MTB_1 = "mtb-1";
    public static final String BASIC_MTB_2 = "mtb-2";

    public static final String NONE_USER = "none-user";
    public static final String NONE_PERM = "none-perm";
    public static final String VIEW_USER = "view-user";
    public static final String VIEW_PERM = "view-perm";
    public static final String EDIT_USER = "edit-user";
    public static final String EDIT_PERM = "edit-perm";

    public PSPermissions.CustomCategory getPermissionCategory(String id) {
        PSPermissions.CustomCategory res = new PSPermissions.CustomCategory(conf.getElementByIdAndName(PSPermissions.CategoryRole.NAME, id));
        String name = res.getName();
        if (useTimestamp && res.doUseTimestamp()) { // use timestamp for newly created objects
            if (useTimestampFormat) {
                name = TimeStampName.getTimeStampedName(name);
            } else {
                name += "_" + CoreProperties.getTestTemplate();
            }
            res.setName(name);
            res.setUseTimestamp(false);
        }
        return res;
    }

    public PSPermissions.CustomPSRole getPermissionRole(String id) {
        PSPermissions.CustomPSRole res = new PSPermissions.CustomPSRole(conf.getElementByIdAndName(PSPermissions.CustomPSRole.NAME, id));
        String name1 = res.getName();
        String name2 = res.getPluralName();
        if (useTimestamp && res.doUseTimestamp()) { // use timestamp for newly created objects
            if (useTimestampFormat) {
                name1 = TimeStampName.getTimeStampedName(name1);
                name2 = TimeStampName.getTimeStampedName(name2);
            } else {
                name1 += "_" + CoreProperties.getTestTemplate();
                name2 += "_" + CoreProperties.getTestTemplate();
            }
            res.setName(name1);
            res.setPluralName(name2);
            res.setUseTimestamp(false);
        }
        return res;
    }

    public PSPermissions getPermissions(String id) {
        return new PSPermissions(conf.getElementByIdAndName(PSPermissions.NAME, id));
    }

    public PSPermissions.AllSet getPermissionsSet(String id) {
        return new PSPermissions.AllSet(conf.getElementByIdAndName(PSPermissions.AllSet.NAME, id));
    }
}
