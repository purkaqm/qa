package com.powersteeringsoftware.tests.acceptance.managers.issues;

import java.util.Iterator;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.FileAttachment;
import com.powersteeringsoftware.libs.objects.ThreadBlock;
import com.powersteeringsoftware.libs.pages.DiscussionIssueViewPage;
import com.powersteeringsoftware.libs.pages.IssueAddPage;

public class IssueManager {


    public DiscussionIssueViewPage addIssueToBlock(IssueAddPage page, ThreadBlock tB) {
        PSLogger.debug("Adding issue to thread block '" + tB.getSubject() + "'");
        page.setPriority(tB.getIssue().getPriority());
        DiscussionIssueViewPage res = page.submit();
        PSLogger.debug("Thread block '" + tB.getSubject() + "' now has an issue");
        return res;
    }

    public DiscussionIssueViewPage createNewBlockWithIssue(IssueAddPage page, ThreadBlock tB) {
        PSLogger.debug("Creating a new thread block '" + tB.getSubject() + "' with issue");
        page.setSubject(tB.getSubject());
        page.setDescription(tB.getMessage());
        page.setPriority(tB.getIssue().getPriority() - 1);
        Iterator<FileAttachment> fAI = tB.getFileAttachmentsList().iterator();
        while (fAI.hasNext()) this.addAttachment(page, fAI.next());
        //TODO add recommended actions handling
        DiscussionIssueViewPage res = page.submit();
        PSLogger.debug("New thread block '" + tB.getSubject() + "' with issue was created successfully");
        return res;
    }

    private void addAttachment(IssueAddPage page, FileAttachment fA) {
        PSLogger.debug("Add attachment to issue");
        page.addAttachment(fA.getTitle(), fA.getPath());
    }

}
