package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.core.PSApplicationException;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.thoughtworks.selenium.Wait;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.elements_locators.AddDocumentsDialogLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 03.09.11
 * Time: 20:01
 * To change this template use File | Settings | File Templates.
 */
public class AddDocumentsDialog extends Dialog {
    private String prefix1;
    private String prefix2;

    public AddDocumentsDialog(String prefix) {
        super(LINK.getLocator(prefix));
        setPopup(POPUP.getLocator(prefix));
        prefix1 = prefix;
        prefix2 = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4) ? prefix1 : prefix.replace(SUFFIX.getLocator(), "");
    }

    @Override
    public void open() {
        waitForVisible(5000);
        super.open();
        try {
            waitWhileLoading();
        } catch (Wait.WaitTimedOutException ww) {
            PSApplicationException cause = PSPage.getEmptyInstance().getProblemFromPage();
            if (cause != null) {
                PSLogger.error(ww);
                throw TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._14) ? new PSKnownIssueException("PS-4386", cause) : cause;
            }
            throw ww;
        }
    }

    protected void waitWhileLoading() {
        new Element(LOADING.getLocator(prefix1)).waitForDisapeared();
        getFileInput().waitForPresent();
    }

    protected Input getFileInput() {
        return new Input(FILE_INPUT.getLocator(prefix2));
    }

    protected Button getAddDocumentButton() {
        return new Button(ADD_DOCUMENT_BUTTON.getLocator(prefix1));
    }

    public void setFile(String path) {
        getFileInput().attachFile(path);
    }

    public void setTitle(String title) {
        new Input(FILE_TITLE.getLocator(prefix2)).type(title);
    }

    public void setDescription(String txt) {
        if (txt != null)
            new TextArea(FILE_DESCRIPTION.getLocator(prefix2)).setText(txt);
    }

    public void add() {
        Button add = getAddDocumentButton();
        add.waitForAttributeChanged(ADD_DOCUMENT_BUTTON_CLASS, ADD_DOCUMENT_BUTTON_CLASS_ATTR);
        add.click(false);
        add.waitForAttributeChanged(ADD_DOCUMENT_BUTTON_CLASS, ADD_DOCUMENT_BUTTON_CLASS_ATTR);
        waitWhileLoading();
        String alert = getDriver().isAlertPresent() ? getDriver().getAlert() : null;
        Assert.assertNull(alert, "There is an alert : '" + alert + "'");
    }

    public void done() {
        Button done = new Button(getPopup().getChildByXpath(DONE_BUTTON));
        done.waitForVisible(); // why?
        PSLogger.save("Before click 'Done'");
        new TimerWaiter(500).waitTime();
        done.mouseOver();
        done.click(false);
        popup.waitForUnvisible();
        new TimerWaiter(1500).waitTime();
        PSLogger.save("After attch");
    }
}

