package com.powersteeringsoftware.libs.pages;


import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.elements.Link;

import static com.powersteeringsoftware.libs.enums.page_locators.TimesheetsPageLocators.*;

public class TimesheetsPageSubmitted extends AbstractTimesheetsPage {


    TimesheetsPageSubmitted() {
        // package access, not allow from any other places
    }

    /**
     * Starts from 0 to n-1
     */
    public String getBillingCategory(int lineNumber) {
        if (getBillingUsed()) {
            return new Element(BILLING_CATEGORY.replace(lineNumber)).getText().trim();
        } else {
            return "";
        }
    }

    public String getTimeSheetCellValue(int row, int column) {
        return new Input(TIME_SHEET_TABLE_CELL_SUBMITTED.replace(row, column)).getText();
    }


    public String getActivity(int lineNumber) {
        Element e = new Element(ACTIVITY_SUBMITTED.replace(lineNumber));
        if (!e.exists()) return null;
        Element ch;
        if ((ch = e.getChildByXpath(ACTIVITY_SUBMITTED_DIV)).exists()) {
            return ch.getText();
        }
        return "";
    }

    public String getWorkItemName(int lineNumber) {
        Link link = new Link(WORK_ITEM_SUBMITTED.replace(lineNumber));
        if (link.exists())
            return link.getText().trim();
        return null;
    }

    public boolean isSubmitted() {
        return true;
    }

}
