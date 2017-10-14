package com.powersteeringsoftware.tests.acceptance;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.*;
import com.powersteeringsoftware.libs.tests.actions.TagManager;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.tests.core.PSTest;

import static com.powersteeringsoftware.libs.settings.PowerSteeringVersions._7_1;
import static com.powersteeringsoftware.libs.util.session.TestSession.getAppVersion;

/**
 * Summary: Test issue 59872: "Issues: Can't submit an issue".<br>
 * <p/>
 * Test Steps:<br>
 * 1. Create required tag for issues<br>
 * 2. Create unrequired tag for issues<br>
 * 3. Create issue. While creating check that checking required issues is
 * enough for saving (that means that unchecked unrequired tag has no effect o
 * saving an issue)<br>
 * 4. Test delete tags.
 * <p/>
 * Preconditions:<br>
 * - before executing test make sure CoreProperties has been initialized.<br>
 * References:
 * Used CoreProperties.getDefaultWorkItemIdWithoutPrefix() for navigating work
 * item summary page<br>
 * <p/>
 * Test can be executed only for PowerSteering 7.1 and above
 */

public class TestIssue59872 extends PSTest {

    private PSTag requiredTag;
    private PSTag unRequiredTag;
    private Work work;

    public TestIssue59872(Work w, PSTag t1, PSTag t2) {
        name = "Issue 59872:required\\unrequired tags in new issue";
        work = w;
        requiredTag = t1;
        unRequiredTag = t2;
    }

    public void run() {
        if (getAppVersion().verLessThan(_7_1)) {
            PSSkipException.skip("Application version must be 7.1 or above");
        }

        createRequiredTag();
        createUnrequiredTag();
        createIssue();
        setTagUnrequired();
    }

    private void createIssue() {
        SummaryWorkPage summary = WorkManager.open(work);

        IssuesPage issues = summary.openIssues();
        IssueAddPage add = issues.pushAddNew();
        add.setSubject("Tested Issue");
        add.setDescription("Issue message");
        add.selectTags(requiredTag.getName(), requiredTag.getValues().get(1));
        add.submit();
    }

    private void createUnrequiredTag() {
        PSLogger.info("Create unrequired tag");
        TagManager.addTag(unRequiredTag);
    }

    private void createRequiredTag() {
        PSLogger.info("Create required tag");
        TagManager.addTag(requiredTag);
    }

    private void setTagUnrequired() {
        PSLogger.info("Set tag '" + requiredTag.getName() + "' unrequired");
        TagsetPage tagPage = TagManager.openTag(requiredTag);
        TagSetsPage.TagSetDialog dialog = tagPage.edit();
        dialog.setRequired(false);
        dialog.submit();
        requiredTag.setRequired(false);
    }

}
