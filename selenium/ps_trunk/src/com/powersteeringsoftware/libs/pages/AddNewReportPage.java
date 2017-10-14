package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Link;

import static com.powersteeringsoftware.libs.enums.page_locators.AddNewReportPageLocator.REPORT_WIZARD_START_LINK;

public class AddNewReportPage extends PSPage {

    public void open() {
        clickBrowseCreateReports();
    }

    public void startReportWizard() {
        Link startReportLink = new Link(REPORT_WIZARD_START_LINK);
        startReportLink.clickAndWaitNextPage();
    }

} // class AddNewReportPage 
