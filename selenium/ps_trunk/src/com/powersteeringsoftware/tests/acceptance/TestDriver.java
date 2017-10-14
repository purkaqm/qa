package com.powersteeringsoftware.tests.acceptance;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.TagManager;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestDriver extends PSTestDriver {
    private static final String ISSUES_GROUP = "issues-group";

    private TestData data;
    private Work work;

    @Override
    public TestData getTestData() {
        if (data == null) data = new TestData();
        return data;
    }

    private Work getWork() {
        return work == null ? work = getTestData().getTestingWork() : work;
    }

    // Work Item "Work"
    public static String SESSION_KEY_WORK_ITEM_WORK = "app.workitem.work";

    @BeforeTest(alwaysRun = true, timeOut = CoreProperties.BEFORE_TEST_TIMEOUT)
    public void before() {
        deleteTags();
    }

    @AfterTest(alwaysRun = true, timeOut = CoreProperties.BEFORE_TEST_TIMEOUT)
    public void after() {
        deleteTags();
    }

    private void deleteTags() {
        try {
            TagManager.deleteTags(getTestData().getRequiredTag().getInitName(), getTestData().getUnRequiredTag().getInitName());
        } catch (Exception e) {
            PSLogger.fatal(e); // do not throw in before method
        }
    }


/*    @Test(groups = {TestSettings.ACCEPTANCE_GROUP},
            description = "Internal Acceptance: create work")
    public void createWorkItem() {
        new TestCreateWorkItem().execute();
    }*/

    /*@Test(groups = {"testPredessors"},
            dependsOnMethods = {"createWorkItem"},
            description = "Internal Acceptance: test predessors")
    // Not used  
    public void testPredessors() {
        //new TestPredessors().execute();

    }*/


    @Test(groups = {TestSettings.ACCEPTANCE_GROUP, TestSettings.NOT_IE_RC_GROUP, ISSUES_GROUP},
            description = "Internal Acceptance: create issue")
    public void createIssue() {
        // run only in FF
        new TestCreateIssue(getWork()).execute();
    }

    @Test(groups = {TestSettings.ACCEPTANCE_GROUP, TestSettings.NOT_IE_RC_GROUP, ISSUES_GROUP},
            dependsOnMethods = {"createIssue"},
            description = "Internal Acceptance: add attachment")
    public void addAttachments() {
        // run only in FF
        new TestAttachments(getWork()).execute();
    }

    @Test(groups = {TestSettings.ACCEPTANCE_GROUP, TestSettings.NOT_IE_RC_GROUP},
            dependsOnMethods = {"addAttachments"},
            description = "Internal Acceptance:update attachment")
    public void updateAttachment() {
        new TestAttachmentUpdates(getWork()).execute();
    }

    @Test(groups = {"debug", TestSettings.ACCEPTANCE_GROUP},
            dependsOnGroups = {ISSUES_GROUP}, // todo: temporary make this method last because of bug with required tags
            description = "Internal Acceptance:test issue 59872")
    public void testIssue59872() {
        new TestIssue59872(getWork(), getTestData().getRequiredTag(), getTestData().getUnRequiredTag()).execute();
    }

    @Test(groups = {TestSettings.ACCEPTANCE_GROUP, ISSUES_GROUP},
            description = "Create issues tree and move it to another project")
    public void testIssuesMoving() {
        new TestMoving(getTestData()).execute();
    }


}
