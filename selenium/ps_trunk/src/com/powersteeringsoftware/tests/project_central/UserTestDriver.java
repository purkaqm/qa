package com.powersteeringsoftware.tests.project_central;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.core.PSTestListener;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.Map;

/**
 * Class for run project central tests under user with custom set.
 * Example of test-ng-suite:
 * <test name="Project Central tests (Resource Planning ON, Custom Permissions)">
 * <parameter name="resource_planning" value="on"/>
 * <parameter name="immediate_delegation" value="on"/>
 * <parameter name="set" value="guest1"
 * <class name="com.powersteeringsoftware.tests.project_central.UserTestDriver"/>
 * </classes>
 * </test>
 * where guest1 is a config id for permissions section
 * User: szuev
 * Date: 29.06.2010
 * Time: 17:33:54
 */
//@Test(groups = {RunTestNG.NOT_IE_RC_GROUP})
public class UserTestDriver extends TestDriver {
    private String setId;
    /*
    static {
        Test t = TestDriver.class.getAnnotation(Test.class);
        if (t != null)
            PSTestListener.setGroups(t.groups());
    }
    */


    @BeforeTest(alwaysRun = true, dependsOnMethods = "before", timeOut = CoreProperties.BEFORE_TEST_TIMEOUT)
    @Parameters({"set"})
    public void beforeUserTestDriver(String set) throws IOException {
        data = null;
        setId = set;
        Map<String, Class[]> map = getTestData().getExceptionsTestCases(setId);
        PSLogger.info("ExceptionsTestCases: " + map.keySet());
        PSTestListener.setExceptionsTestcases(map);
        PSTestListener.setEnabledTestCases(getTestData().getSkippedTestCases(setId));
        super.setPermsAndStartNewSession(setId);
    }

    //this is debug method.
    /*
    @BeforeMethod
    public void beforeMethod() {
        PSLogger.task("Debug, before method");
        SeleniumDriver.setDriverIndex(0);
        somePrepareActions.saveDefinePermissionsPage();
        SeleniumDriver.setDriverIndex();
    }
    */

    @AfterMethod(alwaysRun = true)
    public void afterTestMethod() {
        PSLogger.task("After test method");
        WBSPage page = WBSPage.getInstance();
        if (page.isResetAreaEnabled()) {
            PSLogger.info("'Reset' button is enabled, push it");
            page.resetArea();
        }
    }

    @Test(alwaysRun = true, description = "Rename works in grid",
            groups = {MAIN_WORK_GROUP}, expectedExceptions = AssertionError.class)
    public void renameInGrid() {
        ourGridActions.renameWorks(getTestData().getRootWork(), getTestData().getRootWork().getChild(1));
    }

    @AfterTest(alwaysRun = true)
    public void after() {
        closeNewSession();
    }

}
