package com.powersteeringsoftware.tests.acceptance;

import com.powersteeringsoftware.libs.objects.FileAttachment;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.DocumentDetailsPage;
import com.powersteeringsoftware.libs.pages.DocumentListingPage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.DocumentsManager;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.tests.core.PSTest;

import static com.powersteeringsoftware.libs.settings.PowerSteeringVersions._7_1;
import static com.powersteeringsoftware.libs.util.session.TestSession.getAppVersion;


/**
 * Test issues can update attached files</br>
 * Test can be executed only for 7.1 and higher
 *
 * @author selyaev_ag
 */
public class TestAttachmentUpdates extends PSTest {

    private Work work;

    public TestAttachmentUpdates(Work w) {
        name = "Update Attachements";
        this.work = w;
    }

    public void run() {
        if (getAppVersion().verLessThan(_7_1)) {
            PSSkipException.skip("Application version must be 7.1 or above");
        }

        SummaryWorkPage summary = WorkManager.open(work);

        FileAttachment fA1 = FileAttachment.getFile("utest001.txt", "UpdateTextFile001-" + CoreProperties.getTestTemplate());
        fA1.setDescription("init document");
        FileAttachment fA2 = FileAttachment.getFile("utest002.txt", "UpdateTextFile002-" + CoreProperties.getTestTemplate());
        fA2.setDescription("updated document");

        DocumentListingPage page = DocumentsManager.addDocument(fA1, summary);


        DocumentDetailsPage page2 = DocumentsManager.update(fA2, DocumentsManager.update(fA1, page));
        DocumentsManager.checkVersion(1, fA1, page2);
        DocumentsManager.checkVersion(2, fA2, page2);
        DocumentsManager.validateChanges(fA1, fA2, page2);
    }
}
