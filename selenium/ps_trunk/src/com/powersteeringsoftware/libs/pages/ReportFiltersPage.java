package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.page_locators.ReportWizardPageLocators.PAGE_CANCEL_BUTTON;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 27.06.12
 * Time: 20:35
 * To change this template use File | Settings | File Templates.
 */
public class ReportFiltersPage extends PSPage {
    @Override
    public void open() {
        //todo
    }

    public ReportFolderPage clickCancel() {
        PSLogger.info("Do cancel from filters page");
        Button saveBtn = new Button(PAGE_CANCEL_BUTTON);
        saveBtn.click(true);
        return new ReportFolderPage();
    }
}
