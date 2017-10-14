package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.page_locators.DiscussionIssueViewPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.DiscussionIssueViewPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 27.12.2010
 * Time: 19:02:06
 */
public class DiscussionIssueViewPage extends AbstractWorkPage {

    @Override
    public void open() {
        //ToDo
    }

    private String issue;
    private String redirectUrl;

    public void open(String issue) {
        super.open(redirectUrl = REDIRECT_URL.replace(this.issue = issue));
    }

    public String getTitle() {
        return getFirstTitle(false);
    }

    /**
     * this is for parent
     *
     * @return
     */
    protected String getFirstTitle(boolean full) {
        Element block = getElement(false, BLOCK); // first block
        if (block == null) {
            PSLogger.warn("Can't find any blocks on page");
            return null;
        }
        List<Element> titles = Element.searchElementsByXpath(block, TITLE);
        if (!full) return titles.get(titles.size() - 1).getDEText();
        StringBuilder sb = new StringBuilder();
        for (Element e : titles) { // for 9.2 and latter.
            String txt = e.getDEInnerText();
            sb.append(txt.replace("\n", " ").replaceAll("\\s+", " ").trim()).append(" ");
        }
        //return new Element(TITLE).getText();
        return sb.toString();
    }

    public boolean getOpen() {
        return new Element(OPEN).getText().contains(OPEN_YES.getLocator());
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public DocumentMenu callAttachMenu(String attachName) {
        PSLogger.debug("Call menu for attach " + attachName);
        DocumentMenu menu = new DocumentMenu(attachName);
        menu.open();
        return menu;
    }

    public String getAttachHref(String attachName) {
        DocumentMenu menu = callAttachMenu(attachName);
        Link link = menu.download();
        String attHref = link.getHref();
        menu.close();
        PSLogger.debug("Href is " + attHref);
        return attHref;
    }


    /**
     * escalate parent block
     *
     * @return
     */
    public IssueAddPage escalate() {
        Link escalate = Link.toLink(ESCALATE);
        escalate.clickAndWaitNextPage();
        return new IssueAddPage();
    }

    public DiscussionAddPage edit() {
        Link edit = Link.toLink(EDIT);
        edit.clickAndWaitNextPage();
        for (DiscussionAddPage res : new DiscussionAddPage[]{
                new DiscussionAddPage(),
                new IssueAddPage(),
        }) {
            if (res.checkUrl()) return res;
        }
        return null;
    }

    public CopyMoveDiscPage move() {
        new Link(MOVE_LINK).clickAndWaitNextPage();
        return new CopyMoveDiscPage();
    }

    public DiscussionBlock getBlock(String name) {
        for (Element issue : getIssuesTitleElements()) {
            if (issue.getDEText().equals(name)) {
                return new DiscussionBlock(issue.getParent().getParent(), name);
            }
        }
        return null;
    }

    private List<Element> getIssuesTitleElements() {
        List<Element> issues = getElements(TITLE);
        issues.addAll(getElements(false, TITLE2));
        return issues;
    }

    public List<String> getIssuesTitles() {
        List<String> res = new ArrayList<String>();
        for (Element issue : getIssuesTitleElements()) {
            Element issueId = Element.searchElementByXpath(issue, TITLE_LINK);
            if (issueId != null)
                continue; // skip ids for issues.
            res.add(issue.getDEText().trim());
        }
        return res;
    }

    public class DiscussionBlock extends Element {
        private String name;

        private DiscussionBlock(Element e, String name) {
            super(e);
            this.name = name;
        }

        public String getTitle() {
            return name;
        }

        public IssueAddPage escalate() {
            getLink(ESCALATE).clickAndWaitNextPage();
            return new IssueAddPage();
        }

        public DeEscalateDialog deescalate() {
            return (DeEscalateDialog) deleteDeescalate(false);
        }

        private DeleteDeEscalateDialog deleteDeescalate(boolean delete) {
            Link link = getLink(delete ? DELETE : DEESCALATE);
            PSLogger.debug("de-escalating/deleting link : " + link.asXML());
            String id = link.getDEAttribute("id").replace(DELETE_DEESCALATE_ID_SUFFIX.getLocator(), "");
            Assert.assertFalse(id.isEmpty(), "Can't find id for popup");
            DeleteDeEscalateDialog dialog = delete ? new DeleteDialog(id, name) : new DeEscalateDialog(id, name);
            link.click(false);
            dialog.waitForVisible();
            return dialog;
        }

        public DeleteDialog delete() {
            return (DeleteDialog) deleteDeescalate(true);
        }

        public DiscussionAddPage reply() {
            getLink(REPLY).clickAndWaitNextPage();
            DiscussionAddPage res = new DiscussionAddPage();
            res.setDefaultReplyName(name);
            return res;
        }

        public DiscussionIssueViewPage close() {
            getLink(CLOSE).clickAndWaitNextPage();
            DiscussionIssueViewPage res = new DiscussionIssueViewPage();
            Assert.assertTrue(res.checkUrl(), "Incorrect url after closing");
            PSLogger.debug("Thread block '" + name + "' issue was closed sucessfully");
            return res;
        }

        public DiscussionAddPage edit() {
            getLink(EDIT).clickAndWaitNextPage();
            for (DiscussionAddPage res : new DiscussionAddPage[]{
                    new DiscussionAddPage(),
                    new IssueAddPage(),
            }) {
                if (res.checkUrl()) return res;
            }
            return null;
        }

        private Link getLink(DiscussionIssueViewPageLocators linkName) {
            for (Element ch : Element.searchElementsByXpath(this, "//a")) {
                if (ch.getDEText().trim().equals(linkName.getLocator())) {
                    Link res = new Link(ch);
                    Assert.assertTrue(res.exists(), "Link '" + linkName.name().toLowerCase() + "' doesn't not exist in block " + name);
                    return res;
                }
            }
            Assert.fail("Can't find link '" + linkName.name().toLowerCase() + "' for block " + name);
            return null;
        }

    }

    public class DeEscalateDialog extends DeleteDeEscalateDialog {

        private DeEscalateDialog(String id, String name) {
            super(id, name);
        }

        public DiscussionIssueViewPage ok() {
            PSLogger.save("Before de-escalating");
            Button bt = new Button(getChildByXpath(DELETE_DEESCALATE_DIALOG_OK));
            bt.waitForVisible();
            bt.submit();
            PSLogger.save("After de-escalate");
            DiscussionIssueViewPage res = new DiscussionIssueViewPage();
            Assert.assertTrue(res.checkUrl(), "Incorrect url after de-escalating");
            PSLogger.debug("Thread block '" + name + "' was de-escalated sucessfully");
            return res;
        }
    }

    public class DeleteDialog extends DeleteDeEscalateDialog {

        private DeleteDialog(String id, String name) {
            super(id, name);
        }

        public DiscussionsPage ok() {
            PSLogger.save("Before deleting");
            Button bt = new Button(getChildByXpath(DELETE_DEESCALATE_DIALOG_OK));
            bt.waitForVisible();
            bt.submit();
            PSLogger.save("After deleting");
            DiscussionsPage res = new DiscussionsPage();
            Assert.assertTrue(res.checkUrl(), "Incorrect url after deleting");
            if (isCheckBlankPage() && isBlankPage()) {
                PSLogger.error("Blank page!");
                res.refresh();
            }
            PSLogger.debug("Thread block '" + name + "' was deleted sucessfully");
            return res;
        }
    }

    public abstract class DeleteDeEscalateDialog extends Dialog {
        protected String name;

        private DeleteDeEscalateDialog(String id, String name) {
            super(DELETE_DEESCALATE_DIALOG.replace(id));
            setPopup(getLocator());
            this.name = name;
        }

        public abstract AbstractWorkPage ok();

        public void cancel() {
            new Button(getChildByXpath(DELETE_DEESCALATE_DIALOG_CANCEL)).click(false);
            waitForUnvisible();
        }
    }

}