package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import org.dom4j.Document;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.DocumentListingPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 04.09.11
 * Time: 18:34
 * To change this template use File | Settings | File Templates.
 */
public class DocumentListingPage extends AbstractWorkPage {
    @Override
    public void open() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public AddDocumentsDialog addDocument() {
        AddDocumentsDialog dialog = new AddDocumentsDialog(ADD_DOCUMENT_DIALOG_BUTTON.replace(getUrlId()));
        if (!dialog.exists()) {
            PSLogger.info("No documents on page");
            dialog = new AddDocumentsDialog(ADD_DOCUMENT_DIALOG_LINK.replace(getUrlId()));
        }
        dialog.open();
        return dialog;
    }

    public void addAttachment(String title, String path, String description) {
        if (!path.contains(CoreProperties.getServerFolder())) path = CoreProperties.getServerFolder() + path;
        PSLogger.info("Adding attachment " + title + "," + path);
        AddDocumentsDialog dialog = addDocument();
        dialog.setFile(path);
        dialog.setTitle(title);
        dialog.setDescription(description);
        dialog.add();
        dialog.done();
        waitForPageToLoad();
    }

    private List<Row> rows;

    public Document getDocument(boolean b) {
        if (b) rows = null;
        return super.getDocument(b);
    }

    public void waitForPageToLoad(boolean doCheckPage, long timeout) {
        super.waitForPageToLoad(doCheckPage, timeout);
        getDocument();
    }

    private List<Row> getRows() {
        if (rows != null) return rows;
        rows = new ArrayList<Row>();
        for (Element row : getElements(false, ROW)) {
            Element title = Element.searchElementByXpath(row, TITLE_COLUMN);
            if (title == null) continue;
            Link link = new Link(title.getChildByXpath(TITLE_COLUMN_LINK));
            Row r = new Row();
            r.link = link;
            r.title = link.getDEText();
            link.setName(r.title);
            r.ch = new CheckBox(row.getChildByXpath(CHECKBOX));
            r.ch.setName(r.title);
            rows.add(r);
        }
        return rows;
    }


    public boolean isDocumentPresent(String name) {
        return getDocumentTitle(name) != null;
    }


    public Link getDocumentTitle(String name) {
        Link link = new Link(MORE);
        for (; ; ) {
            Link res = _getDocumentTitle(name);
            if (res != null) return res;
            if (link.exists() && link.isVisible()) {
                link.clickAndWaitNextPage();
                getDocument();
                rows = null;
            } else {
                break;
            }
        }
        return null;
    }

    private Link _getDocumentTitle(String name) {
        for (Row link : getRows()) {
            if (name.equals(link.title)) return link.link;
        }
        return null;
    }

    public void select(String name) {
        for (Row link : getRows()) {
            if (name.equals(link.title)) {
                link.ch.select();
                return;
            }
        }
        Assert.fail("Can't find checkbox for document '" + name + "'");
    }

    public DeleteDialog delete() {
        Button bt = new Button(DELETE);
        Assert.assertTrue(bt.exists() && bt.isVisible(), "Can't find delete button");
        bt.click(false);
        DeleteDialog res = new DeleteDialog();
        res.waitForVisible();
        return res;
    }

    public DocumentDetailsPage.Upload update(String name) {
        Link link = getDocumentTitle(name);
        Assert.assertNotNull(link, "Can't find document " + name);
        DocumentMenu menu = new DocumentMenu(link);
        menu.open();
        menu.update().clickAndWaitNextPage();
        return new DocumentDetailsPage.Upload();
    }

    public DocumentDetailsPage seeDetails(String name) {
        Link link = getDocumentTitle(name);
        Assert.assertNotNull(link, "Can't find document " + name);
        DocumentMenu menu = new DocumentMenu(link);
        menu.open();
        Link unlock = menu.unlock();
        if (unlock != null) {
            unlock.clickAndWaitNextPage();
            return new DocumentDetailsPage.Upload();
        }
        menu.details().clickAndWaitNextPage();
        return new DocumentDetailsPage();
    }

    public String getHref(String name) {
        Link link = getDocumentTitle(name);
        if (link == null) return null;
        DocumentMenu menu = new DocumentMenu(link);
        menu.open();
        String res = menu.download().getHref();
        menu.close();
        return res;
    }

    public class DeleteDialog extends Dialog {

        private DeleteDialog() {
            super(DELETE);
            setPopup(DELETE_DIALOG);
        }

        public void yes() {
            Button bt = new Button(getPopup().getChildByXpath(DELETE_DIALOG_YES));
            bt.click(false);
            DocumentListingPage.this.waitForPageToLoad();
        }
    }

    private class Row {
        private Link link;
        private CheckBox ch;
        private String title;
    }
}
