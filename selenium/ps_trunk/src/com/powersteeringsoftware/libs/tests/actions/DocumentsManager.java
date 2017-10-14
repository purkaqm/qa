package com.powersteeringsoftware.libs.tests.actions;

import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.FileAttachment;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.AbstractWorkPage;
import com.powersteeringsoftware.libs.pages.DocumentDetailsPage;
import com.powersteeringsoftware.libs.pages.DocumentListingPage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import org.testng.Assert;


/**
 * only for FF (unsupported by selenium)
 */
public class DocumentsManager {

    public static DocumentDetailsPage update(FileAttachment fA, DocumentDetailsPage.Upload page) {
        String path = fA.getPath();
        PSLogger.info("Attach new file to existing document. path = " + path);
        page.setFile(path);
        page.setComments("New version.");
        DocumentDetailsPage page2 = page.submit();
        DocumentDetailsPage.Edit page3 = page2.edit();
        page3.setTitle(fA.getTitle());
        page3.setDescription(fA.getDescription());
        return page3.submit();
    }

    public static void checkVersion(int versionNumber, FileAttachment fA, DocumentDetailsPage page) {
        DocumentDetailsPage.Row row = page.getRow(versionNumber);
        Assert.assertNotNull(row, "Can't find attachment for version " + versionNumber);
        String verHref = row.getHref();
        Assert.assertTrue(FileAttachment.compareURLWithLocalFile(page.getCookie(), verHref, fA.getPath()),
                "Version #" + versionNumber + " content and the local copy " + fA.getPath() + " do not match");
    }

    public static DocumentListingPage addDocument(FileAttachment fA, SummaryWorkPage page) {
        DocumentListingPage documents = page.openDocuments();
        addAttachment(fA, documents);
        return documents;
    }

    private static void addAttachment(FileAttachment fA, DocumentListingPage page) {
        page.addAttachment(fA.getTitle(), fA.getPath(), fA.getDescription());
        Link title = page.getDocumentTitle(fA.getTitle());
        Assert.assertNotNull(title, "Can't find doc " + fA.getTitle() + " after attaching");
    }

    public static DocumentDetailsPage.Upload update(FileAttachment fA, DocumentListingPage page) {
        return page.update(fA.getTitle());
    }

    public static void validateChanges(FileAttachment f1, FileAttachment f2, AbstractWorkPage page) {
        DocumentListingPage page2 = page.openDocuments();
        String href1 = page2.getHref(f1.getTitle());
        String href2 = page2.getHref(f2.getTitle());
        Assert.assertNull(href1, "There is " + f1.getTitle() + " on page");
        Assert.assertTrue(FileAttachment.compareURLWithLocalFile(page.getCookie(), href2, f2.getPath()),
                "Incorrect document after upload new version");

    }

    public static void addDocument(Work work, FileAttachment file) {
        PSLogger.info("Add document " + file + " to work " + work.getName());
        SummaryWorkPage summary = WorkManager.open(work);
        DocumentListingPage page = addDocument(file, summary);
        Assert.assertTrue(page.isDocumentPresent(file.getTitle()), "Can't find document " + file + " after attaching to work " + work);
    }

    public static void removeDocument(Work work, FileAttachment file) {
        PSLogger.info("Delete document " + file + " from work " + work.getName());
        SummaryWorkPage summary = WorkManager.open(work);
        DocumentListingPage page = summary.openDocuments();
        page.select(file.getTitle());
        page.delete().yes();
        Assert.assertFalse(page.isDocumentPresent(file.getTitle()), "There is a document " + file + " after deleting from work " + work);
    }
}
