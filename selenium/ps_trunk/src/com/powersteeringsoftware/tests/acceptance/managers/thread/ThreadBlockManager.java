package com.powersteeringsoftware.tests.acceptance.managers.thread;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.FileAttachment;
import com.powersteeringsoftware.libs.objects.ThreadBlock;
import com.powersteeringsoftware.libs.pages.DiscussionAddPage;
import com.powersteeringsoftware.libs.pages.DiscussionIssueViewPage;

import java.util.Iterator;

/**
 * Class works only for 7.1
 *
 * @author selyaev_ag
 */
public class ThreadBlockManager {


    public DiscussionIssueViewPage createNewBlock(DiscussionAddPage page, ThreadBlock tB) {
        page.setSubject(tB.getSubject());
        page.setDescription(tB.getMessage());
        addAttachments(page, tB);
        return page.submit();
    }

    private void addAttachments(DiscussionAddPage page, ThreadBlock tB) {
        Iterator<FileAttachment> fAI = tB.getFileAttachmentsList().iterator();
        while (fAI.hasNext()) {
            PSLogger.debug("Add attachment to discussion");
            FileAttachment f = fAI.next();
            page.addAttachment(f.getTitle(), f.getPath());
        }
    }

}
