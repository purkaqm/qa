package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.AddDocumentsDialog;
import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.DiscussionAddPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 07.06.2010
 * Time: 12:10:16
 */
public class DiscussionAddPage extends AbstractWorkPage {

    private String defaultReplyName;

    public void setSubject(String txt) {
        Input in = new Input(ISSUE_SUBJECT);
        in.waitForVisible();
        if (txt == null && defaultReplyName != null) {
            Assert.assertEquals(in.getValue(), defaultReplyName, "Incorrect default reply name");
            return;
        }
        PSLogger.info("Set subject '" + txt + "'");
        in.type(txt);
    }

    public void setDescription(String desc) {
        PSLogger.info("Set description '" + desc + "'");
        new Element(ISSUE_FRAME).waitForVisible();
        PSLogger.save("After loading frame");
        String script = ISSUE_DESCRIPTION.replace(desc);
        getDriver().runScript(script);
        new TimerWaiter(500).waitTime();
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._13)) {
            String content = getDriver().getEval(ISSUE_DESCRIPTION_CONTENT.getLocator());
            PSLogger.info("Content: >" + content + "<");
        }
        //clickBody();
        new Input(ISSUE_SUBJECT).focus();
        new Button(ISSUE_SUBMIT).focus();
        new TimerWaiter(2000).waitTime();
        PSLogger.save("After setting description");
    }


    public void addAttachment(String title, String path) {
        if (!path.contains(CoreProperties.getServerFolder())) path = CoreProperties.getServerFolder() + path;
        PSLogger.info("Adding attachment " + title + "," + path);
        AddDocumentsDialog dialog = new AddDocumentsDialog(ADD_DOCUMENT_DIALOG.replace(getUrlId()));
        dialog.open();

        dialog.setFile(path);
        dialog.setTitle(title);
        dialog.add();
        dialog.done();
        new TimerWaiter(500).waitTime();
        Element doc = new Element(ADDED_DOCUMENT_LINK.replace(title));
        Assert.assertTrue(doc.exists(), "Can't find document " + title + " after.");
    }

    public DiscussionIssueViewPage submit() {
        Button bt = new Button(ISSUE_SUBMIT);
        bt.waitForVisible();
        bt.setDefaultElement();
        //PSLogger.debug(bt.getParent().getParent().asXML());
        new TimerWaiter(500).waitTime();
        bt.focus();
        bt.click(true);
        String error = getErrorBoxMessage();
        Assert.assertNull(error, "Has error : " + error);
        DiscussionIssueViewPage res = new DiscussionIssueViewPage();
        Assert.assertTrue(res.checkUrl(), "Incorrect url after submitting new issue");
        if (isCheckBlankPage() && isBlankPage()) {
            PSLogger.error("Blank page!");
            res.refresh();
        }
        return res;
    }

    public void setDefaultReplyName(String parentName) {
        if (defaultReplyName != null) return;
        defaultReplyName = DEFAULT_ISSUE_SUBJECT_PATTERN.replace(parentName);
    }
}
