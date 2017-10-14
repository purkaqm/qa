package com.powersteeringsoftware.libs.pages.rw;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.elements.SelectInput;
import com.powersteeringsoftware.libs.elements.TextArea;
import com.powersteeringsoftware.libs.enums.page_locators.ReportWizardPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.ReportFolderPage;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.ReportWizardPageLocators.PAGE_CANCEL_BUTTON;
import static com.powersteeringsoftware.libs.enums.page_locators.ReportWizardTabLocator.*;

public class SaveRWPage extends ReportWizardPage {

    private boolean isCopy;

    protected SaveRWPage() {
        super(ReportWizardPageLocators.RWTab.Save);
    }

    public void setReportName(String repName) {
        getReportNamenput().setValue(repName);
    }

    public void setReportDesription(String repDescription) {
        getReportDescriptionArea().setText(repDescription);
    }

    public void setReportLocation(String location) {
        if (!MY_REPORTS_TAB.getLocator().equals(location) && !PUBLIC_REPORTS_TAB.getLocator().equals(location)) {
            location = "- " + location;
        }
        getLocationSelector().select(location);
    }

    public void setReportLocation(boolean isMy) {
        getLocationSelector().select(isMy ? MY_REPORTS_TAB : PUBLIC_REPORTS_TAB);
    }

    public String getReportName() {
        return getReportNamenput().getValue();
    }

    private Input getReportNamenput() {
        return new Input(SAVE_TAB_REPORTNAME_INPUT);
    }

    private TextArea getReportDescriptionArea() {
        return new TextArea(SAVE_TAB_REPORTDESCRIPTION_AREA);
    }

    private SelectInput getLocationSelector() {
        return new SelectInput(SAVE_TAB_LOCATION_SELECTOR);
    }

    public static String getCopyName(String name) {
        return name + " - copy";
    }

    public static String getFirstEditedName(String name) {
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_3)) return name;
        return getCopyName(name);
    }

    public ReportFolderPage clickCancel() {
        if (!isCopy) {
            return null; // todo
        }
        PSLogger.info("Do cancel");
        Button saveBtn = new Button(PAGE_CANCEL_BUTTON);
        Assert.assertTrue(saveBtn.exists(), "Can't find cancel button");
        saveBtn.click(true);
        return new ReportFolderPage();
    }

    public void setIsCopy(boolean copy) {
        isCopy = copy;
    }
} // class SaveRWPage
