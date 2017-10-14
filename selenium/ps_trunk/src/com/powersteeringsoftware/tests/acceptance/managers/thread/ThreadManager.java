package com.powersteeringsoftware.tests.acceptance.managers.thread;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.FileAttachment;
import com.powersteeringsoftware.libs.objects.ThreadBlock;
import com.powersteeringsoftware.libs.pages.DiscussionAddPage;
import com.powersteeringsoftware.libs.pages.DiscussionIssueViewPage;
import com.powersteeringsoftware.libs.pages.IssueAddPage;
import org.testng.Assert;

import java.util.Iterator;


/**
 * @author selyaev_ag
 */
public class ThreadManager {

    public DiscussionAddPage reply(DiscussionIssueViewPage page, ThreadBlock tB) {
        PSLogger.debug("Replying to thread block '" + tB.getSubject() + "'");
        DiscussionIssueViewPage.DiscussionBlock block = page.getBlock(tB.getSubject());
        return block.reply();
    }

    public DiscussionIssueViewPage close(DiscussionIssueViewPage page, ThreadBlock tB) {
        PSLogger.debug("Closing the issue for thread block '" + tB.getSubject() + "'");
        DiscussionIssueViewPage.DiscussionBlock block = page.getBlock(tB.getSubject());
        return block.close();
    }

    public IssueAddPage escalate(DiscussionIssueViewPage page, ThreadBlock tB) {
        PSLogger.debug("Escalating the thread block '" + tB.getSubject() + "'");
        DiscussionIssueViewPage.DiscussionBlock block = page.getBlock(tB.getSubject());
        return block.escalate();
    }

    public DiscussionIssueViewPage deEscalate(DiscussionIssueViewPage page, ThreadBlock tB) {
        PSLogger.debug("De-escalating the thread block '" + tB.getSubject() + "'");
        DiscussionIssueViewPage.DiscussionBlock block = page.getBlock(tB.getSubject());
        return block.deescalate().ok();
    }


    public void testAttachments(DiscussionIssueViewPage page, ThreadBlock tB) {
        PSLogger.debug("Testing the attachments for thread block '" + tB.getSubject() + "'");
        Iterator<FileAttachment> tBFAI = tB.getFileAttachmentsList().iterator();
        while (tBFAI.hasNext()) {
            FileAttachment fA = tBFAI.next();
            PSLogger.debug("Testing the attachment '" + fA.getTitle() + "'");

            String attHref = page.getAttachHref(fA.getTitle());

            Assert.assertTrue(FileAttachment.compareURLWithLocalFile(page.getCookie(), attHref, fA.getPath()),
                    "Original content and the local copy differ for '" + fA.getTitle() + "'");
        }
    }

}
