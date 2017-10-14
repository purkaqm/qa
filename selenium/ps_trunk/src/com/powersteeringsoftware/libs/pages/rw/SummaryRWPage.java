package com.powersteeringsoftware.libs.pages.rw;

import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.RadioButton;
import com.powersteeringsoftware.libs.enums.page_locators.ReportWizardPageLocators;
import com.powersteeringsoftware.libs.objects.rw.RWColumn;

import static com.powersteeringsoftware.libs.enums.page_locators.ReportWizardTabLocator.SUM_TAB_COLUMN_ROW_ELMENT;
import static com.powersteeringsoftware.libs.enums.page_locators.ReportWizardTabLocator.SUM_TAB_SUMMARY_PARAMETER_CONTROL;

public class SummaryRWPage extends ReportWizardPage {

    protected SummaryRWPage() {
        super(ReportWizardPageLocators.RWTab.Summary);
    }

    public void setColumnSummaryValue(RWColumn column) {
        getSummaryControl(column.getFullColumnName(), column.getSummaryValue().getPos()).check();
    }

    public boolean isSummaryTypeControlExist(RWColumn column) {
        return (getSummaryControl(column.getFullColumnName(), column.getSummaryValue().getPos()) != null);
    }

    private Element getTRElement(String columnName) {
        Element tr = searchElement(SUM_TAB_COLUMN_ROW_ELMENT.replace(columnName));
        return tr;
    }

    private RadioButton getSummaryControl(String columnName, int colPos) {
        Element tr = getTRElement(columnName);
        Element rdBtnElm = Element.searchElementByXpath(tr, SUM_TAB_SUMMARY_PARAMETER_CONTROL.replace(String.valueOf(colPos)));
        return rdBtnElm != null ? new RadioButton(rdBtnElm) : null;
    }


} //  class SummaryRWPage
