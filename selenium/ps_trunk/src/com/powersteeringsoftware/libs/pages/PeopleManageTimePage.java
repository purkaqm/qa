package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.page_locators.PeopleManageTimePageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.thoughtworks.selenium.Wait;

import java.util.Date;

import static com.powersteeringsoftware.libs.enums.page_locators.PeopleManageTimePageLocators.*;


/**
 * todo: rewrite this page
 */
public abstract class PeopleManageTimePage extends PSPage {

    public void open() {
        clickBrowseManageTime();
        getDocument();
    }


    public void navigateStartDate(Date dateToFind) {
        DatePicker dp = new DatePicker(INPUT_START_DATE_PICKER);
        dp.useDatePopup(true);
        dp.set(dateToFind.getTime());
        waitForPageToLoad();
        dp.waitForVisible();
    }


    public boolean isShow(int branchcode) {
        return getTbodyElement(branchcode).getElementClass().contains(TBODY_SHOW_CLASS.getLocator());
    }

    public boolean isHide(int branchcode) {
        return getTbodyElement(branchcode).getElementClass().contains(TBODY_HIDE_CLASS.getLocator());
    }

    protected Element getTbodyElement(int branchcode) {
        return new Element(TBODY.replace(branchcode));
    }

    public void expandTimesheet(int branchcode) {
        if (isHide(branchcode)) clickTimesheetOwnwer(branchcode);
        getTbodyElement(branchcode).waitForClass(TBODY_SHOW_CLASS);
        PSLogger.save("After expanding");
    }

    private void clickTimesheetOwnwer(int branchcode) {
        new Link(EXPAND_LINK.replace(branchcode)).click(false);
        new TimerWaiter(2000).waitTime();
    }

    public void collapseTimesheet(int branchcode) {
        if (isShow(branchcode)) clickTimesheetOwnwer(branchcode);
        getTbodyElement(branchcode).waitForClass(TBODY_HIDE_CLASS);
    }


    public int getTimesheetLineNumber(int branchcode) {
        String locator = TIMESHEET_LINE_NUMBER_GETEVAL.replace(branchcode);
        String lineNumber = SeleniumDriverFactory.getDriver().getEval(locator);
        return Integer.parseInt(lineNumber);
    }

    public void checkStatus(int branchcode, int linenumber) {
        Link link = new Link(TR_LINK.replace(branchcode, ++linenumber));
        CheckBox ch = new CheckBox(TR_CHECKBOX.replace(branchcode, linenumber));
        ch.setName(link.getText());
        ch.click();
    }

    public void checkAllStatuses(int branchcode) {
        int lineNumber = getTimesheetLineNumber(branchcode);
        for (int i = 0; i < lineNumber; i++) {
            checkStatus(branchcode, i);
        }
    }

    /**
     * Get Cell value for the timesheet table. Method is used for getting value for user totals.
     *
     * @param userIndex    - starts from 0
     * @param columnNumber - starts from 0
     * @return evaluated value for found cell
     */
    private String getDayTotalValueForUser(int userIndex, int columnNumber) {
        Element header = new Element(TITLE_TOTAL_VALUE.replace(userIndex, ++columnNumber));
        //header.setDefaultElement();
        String txt = header.getText().trim();
        PSLogger.debug("Total value for column [" + userIndex + "," + columnNumber + "] is " + txt);
        return txt;
    }

    public double getColumnTotal(int userIndex, Columns column) {
        return Double.parseDouble(getDayTotalValueForUser(userIndex, column.getValue()));
    }

    public double getColumnTotal(int userIndex, int v) {
        return getColumnTotal(userIndex, Columns.values()[v]);
    }

    public double getSundayTotal(int userIndex) {
        return getColumnTotal(userIndex, Columns.SUNDAY);
    }

    public double getMondayTotal(int userIndex) {
        return getColumnTotal(userIndex, Columns.MONDAY);
    }

    public double getTuesdayTotal(int userIndex) {
        return getColumnTotal(userIndex, Columns.TUESDAY);
    }

    public double getWednesdayTotal(int userIndex) {
        return getColumnTotal(userIndex, Columns.WEDNESDAY);
    }

    public double getThursdayTotal(int userIndex) {
        return getColumnTotal(userIndex, Columns.THURSDAY);
    }

    public double getFridayTotal(int userIndex) {
        return getColumnTotal(userIndex, Columns.FRIDAY);
    }

    public double getSaturdayTotal(int userIndex) {
        return getColumnTotal(userIndex, Columns.SATURDAY);
    }

    public double getOverallTotal(int userIndex) {
        return getColumnTotal(userIndex, Columns.OVERALL);
    }


    public double getWorkTotalForUser(int userIndex, int workIndex) {
        String txt = new Element(TR_TD_HOURS_TOTAL.replace(userIndex, ++workIndex)).getText();
        PSLogger.debug("Total hours for row [" + userIndex + "," + workIndex + "] is " + txt);
        return Double.parseDouble(txt);
    }

    protected void pushAction(PeopleManageTimePageLocators loc, boolean wait) {
        if (TestSession.getAppVersion().verLessOrEqual(PowerSteeringVersions._8_2_1)) {
            // don't know is it needed and correct
            new SelectInput(ACTION_SELECT).select(loc);
        }
        ComboBox c = new ComboBox(ACTION_COMBOBOX);
        c.select(loc);
        new Button(ACTION_APPLY).click(wait);
    }

    public static class Approved extends PeopleManageTimePage {
        public void open() {
            clickBrowseManageTime();
            new Link(APPROVED_TAB).clickAndWaitNextPage();
            getDocument();
        }

        public void pushUnApprove() {
            pushAction(ACTION_COMBOBOX_UNAPPROVE, true);
        }
    }


    public static class AwaitingApproval extends PeopleManageTimePage {

        public void pushApprove() {
            pushAction(ACTION_COMBOBOX_APPROVE, true);
        }

        private RejectDialog pushReject() {
            pushAction(ACTION_COMBOBOX_REJECT, false);
            RejectDialog dialog = new RejectDialog();
            dialog.waitForVisible();
            PSLogger.save("Reject dialog");
            return dialog;
        }

        public void pushReject(String explanation) {
            RejectDialog adapter = pushReject();
            adapter.typeExplanation(explanation);
            adapter.pushReject();
        }

        public class RejectDialog extends Dialog {
            public RejectDialog() {
                super(WINDOW_ID);
            }

            public void pushReject() {
                if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._8_0)) {
                    SeleniumDriverFactory.getDriver().runScript(BUTTON_REJECT_RUN_SCRIPT.getLocator());
                } else {
                    Button bt = new Button(BUTTON_REJECT);
                    bt.waitForVisible();
                    bt.click(false);
                }
                try {
                    waitForUnvisible(3000);
                } catch (Wait.WaitTimedOutException we) {
                    // ignore.
                }
                waitForPageToLoad();
            }

            public void pushCancel() {
                new Button(BUTTON_CANCEL_ID).click(false);
                waitForUnvisible();
            }

            public void typeExplanation(String explanation) {
                new TextArea(TYPE_TEXTAREA_EXPLANATION).setText(explanation);
            }

        }
    }
}
