package com.powersteeringsoftware.tests.permissions.basic_permissions;

/**
 * Class Test Driver for Permission tests
 * <p/>
 * Date: 01/03/13
 *
 * @author karina & claus
 */

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSPermissions;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.DefinePermissionsPage;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.PermissionsManager;
import com.powersteeringsoftware.libs.tests.actions.UserManager;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.tests.permissions.TestData;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static com.powersteeringsoftware.tests.permissions.TestData.*;

public class TestDriver extends PSTestDriver {
    private TestData data;

    public TestData getTestData() {
        if (data == null) data = new TestData();
        return data;
    }

    /**
     * View Work permission test
     *
     * @param data
     */
    @Test(dataProvider = "testData", dependsOnMethods = "prepare")
    public void testViewPermission(Data data) {
        try {
            // login action
            SeleniumDriverFactory.initNewDriver();
            BasicCommons.logIn(data.getUser());
            //main action
            Work work = (Work) data.getObj();
            User user = data.getUser();
            List<PSPermissions.CategoryRole> roles = data.getPerm().getCategoryRoles();  //has only 1 role, the current
            PSPermissions.CategoryRole role = roles.get(0);
            if (TestSession.getPermissions().get(PSPermissions.BasicTarget.WI).hasPermission(role, PSPermissions.Verb.VIEW)) {
                Assert.assertTrue(ViewPermission.viewWorkTreeTest(data), "VIEW Permission ViewWorkTreeTest Positive case failure: '" + user.getFullName() + "' can not View work: " + work.getName());
                Assert.assertTrue(ViewPermission.viewSearchTest(data), "VIEW Permission SearchTest Positive case failure: '" + user.getFullName() + "' can not View work: " + work.getName());
            } else {
                Assert.assertFalse(ViewPermission.viewWorkTreeTest(data), "VIEW Permission ViewWorkTreeTest Negative case failure: '" + user.getFullName() + "' can View work: " + work.getName());
                Assert.assertFalse(ViewPermission.viewSearchTest(data), "VIEW Permission SearchTest Negative case failure: '" + user.getFullName() + "' can View work: " + work.getName());
            }
        } finally {
            if (SeleniumDriverFactory.getNumberOfDrivers() <= 1) return;
            BasicCommons.logOutWithoutThrow();
            SeleniumDriverFactory.stopLastSeleniumDriver();
        }
    }

    /**
     * Edit Work permission test
     *
     * @param data
     */
    @Test(dataProvider = "testData", dependsOnMethods = "prepare")
    public void testEditPermission(Data data) {
        try {
            // login action
            SeleniumDriverFactory.initNewDriver();
            BasicCommons.logIn(data.getUser());
            //main action
            Work work = (Work) data.getObj();
            User user = data.getUser();
            List<PSPermissions.CategoryRole> roles = data.getPerm().getCategoryRoles();  //has only 1 role, the current
            PSPermissions.CategoryRole role = roles.get(0);
            if (TestSession.getPermissions().get(PSPermissions.BasicTarget.WI).hasPermission(role, PSPermissions.Verb.VIEW)) {
                if (TestSession.getPermissions().get(PSPermissions.BasicTarget.WI).hasPermission(role, PSPermissions.Verb.EDIT)) {
                    Assert.assertTrue(EditPermission.editWorkTest(data), "EDIT Permission editWorkTest Positive case failure: '" + user.getFullName() + "' can not Edit work: '" + work.getName() + "'.");
                } else {
                    Assert.assertFalse(EditPermission.editWorkTest(data), "EDIT Permission editWorkTest Negative case failure: '" + user.getFullName() + "' can Edit work: '" + work.getName() + "'.");
                }
            } else {
                PSLogger.debug("User: " + user.getFullName() + "' has not VIEW PERMISSION, therefor he can not EDIT: '" + work.getName() + "'.");
                Assert.assertFalse(ViewPermission.viewWorkTreeTest(data), "EDIT Permission ViewWorkTreeTest Negative case failure: '" + user.getFullName() + "' can View work: " + work.getName());
                Assert.assertFalse(ViewPermission.viewSearchTest(data), "EDIT Permission SearchTest Negative case failure: '" + user.getFullName() + "' can View work: " + work.getName());
            }
        } finally {
            if (SeleniumDriverFactory.getNumberOfDrivers() <= 1) return;
            BasicCommons.logOutWithoutThrow();
            SeleniumDriverFactory.stopLastSeleniumDriver();
        }

    }


    /**
     * beforeMethod will execute only once before prepare
     *
     * @param data data object returned by dataProvider
     */
    @BeforeClass
    public void beforeClass() {
        // imho, it is not needed, the better is update general.xml to avoid excess operation. :
        DefinePermissionsPage definePage = PermissionsManager.openDefine();
        TestSession.getPermissions().mergePermissions(PermissionsManager.load(definePage));
    }


    /**
     * Prepare method will execute one time per data returned by dataProvider
     *
     * @param data data object returned by dataProvider
     */
    @Test(dataProvider = "testData")
    public void prepare(Data data) {
        UserManager.createUser(data.getUser());
        PermissionsManager.setPermissions(data.getObj(), data.getPerm());
    }

    /**
     * Data provider
     *
     * @return data collection
     */
    @DataProvider(name = "data")
    public Object[][] testData() {
        return new Object[][]{
                new Object[]{new Data(getTestData().getUser(NONE_USER), getTestData().getPermissions(NONE_PERM), getTestData().getDesignWork())},
                new Object[]{new Data(getTestData().getUser(VIEW_USER), getTestData().getPermissions(VIEW_PERM), getTestData().getDesignWork())},
                new Object[]{new Data(getTestData().getUser(EDIT_USER), getTestData().getPermissions(EDIT_PERM), getTestData().getDesignWork())}
        };
    }
}
