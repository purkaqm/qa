package com.powersteeringsoftware.tests.acceptance;

import com.powersteeringsoftware.libs.objects.FileAttachment;
import com.powersteeringsoftware.libs.objects.Issue;
import com.powersteeringsoftware.libs.objects.ThreadBlock;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.DiscussionAddPage;
import com.powersteeringsoftware.libs.pages.DiscussionIssueViewPage;
import com.powersteeringsoftware.libs.pages.IssueAddPage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.tests.core.PSTest;
import com.powersteeringsoftware.tests.acceptance.managers.issues.IssueManager;
import com.powersteeringsoftware.tests.acceptance.managers.thread.ThreadBlockManager;
import com.powersteeringsoftware.tests.acceptance.managers.thread.ThreadManager;

import static com.powersteeringsoftware.libs.settings.PowerSteeringVersions._7_1;
import static com.powersteeringsoftware.libs.util.session.TestSession.getAppVersion;

/**
 * Class works only for 7.1 or above
 *
 * @author selyaev_ag
 */
public class TestCreateIssue extends PSTest {

    private ThreadManager tM;
    private ThreadBlockManager tBM;
    private IssueManager iM;
    private ThreadBlock tD;
    private Work work;

    public TestCreateIssue(Work w) {
        name = "Create Issue";
        this.work = w;
    }

    public void run() {
        if (getAppVersion().verLessThan(_7_1)) {
            PSSkipException.skip("Application version must be 7.1 or above");
        }

        tD = new ThreadBlock("TestDiscussion" + CoreProperties.getTestTemplate(),
                "This is just a test discussion.", work);
        tD.addAttachment(FileAttachment.getFile("dtest001.txt",
                "DiscussionTextFile001"));
        tD.addAttachment(FileAttachment.getFile("dtest002.txt",
                "DiscussionTextFile002"));
        tD.setIssue(new Issue(3));

        tM = new ThreadManager();
        tBM = new ThreadBlockManager();
        iM = new IssueManager();

        SummaryWorkPage summary = WorkManager.open(work);
        DiscussionAddPage discussionsAddPage = summary.openDiscussionsTab().pushAddNew();
        DiscussionIssueViewPage discussion = tBM.createNewBlock(discussionsAddPage, tD);
        tM.testAttachments(discussion, tD);
        IssueAddPage issueAddPage = tM.escalate(discussion, tD);

        DiscussionIssueViewPage issue = iM.addIssueToBlock(issueAddPage, tD);
        tM.testAttachments(issue, tD);
        discussion = tM.deEscalate(issue, tD);
        tM.testAttachments(discussion, tD);
    }

}
