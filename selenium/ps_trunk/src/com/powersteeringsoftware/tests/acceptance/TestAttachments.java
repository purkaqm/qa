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
 * Test issues can attach files</br>
 * Test can be executed only for 7.1 and higher
 *
 * @author selyaev_ag
 */
public class TestAttachments extends PSTest {

    private ThreadManager tM;
    private ThreadBlockManager tBM;
    private IssueManager iM;
    private Work wi;

    public TestAttachments(Work w) {
        name = "Issue Attachements";
        this.wi = w;
    }

    public void run() {
        if (getAppVersion().verLessThan(_7_1)) {
            PSSkipException.skip("Application version must be 7.1 or above");
        }


        ThreadBlock tI = new ThreadBlock("TestIssue" + CoreProperties.getTestTemplate(),
                "This is just a test issue created by an automated script.", wi);
        tI.addAttachment(FileAttachment.getFile("itest001.txt", "IssueTextFile001"));
        tI.addAttachment(FileAttachment.getFile("itest002.txt", "IssueTextFile002"));
        tI.setIssue(new Issue(3));

        ThreadBlock rB = new ThreadBlock("TestReply" + CoreProperties.getTestTemplate(),
                "This is just a test reply created by an automated script.", wi);
        rB.addAttachment(FileAttachment.getFile("itest003.txt", "IssueTextFile003"));
        rB.addAttachment(FileAttachment.getFile("itest004.txt", "IssueTextFile004"));
        rB.setIssue(new Issue(3));

        tM = new ThreadManager();
        tBM = new ThreadBlockManager();
        iM = new IssueManager();

        SummaryWorkPage summary = WorkManager.open(wi);

        IssueAddPage page = summary.openIssues().pushAddNew();
        DiscussionIssueViewPage issue = iM.createNewBlockWithIssue(page, tI);
        tM.testAttachments(issue, tI);
        DiscussionAddPage addDiscussion = tM.reply(issue, tI);
        DiscussionIssueViewPage discussion = tBM.createNewBlock(addDiscussion, rB);
        tM.testAttachments(discussion, rB);
        IssueAddPage addIssue = tM.escalate(discussion, rB);
        issue = iM.addIssueToBlock(addIssue, rB);
        tM.testAttachments(issue, rB);
        issue = tM.close(issue, rB);
        tM.testAttachments(issue, rB);
    }

}
