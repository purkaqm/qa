package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.DatePicker;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.LocationPopup;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.Timesheets;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.Date;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.TimesheetsPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 09.06.2010
 * Time: 18:29:52
 */
public abstract class AbstractTimesheetsPage extends PSPage {
    public static final int LINES_NUMBER_FOR_EACH_ADD = 5;

    protected static final TimerWaiter WAITER = new TimerWaiter(5000);
    protected Element table;

    public void open() {
        clickBrowseMyProfileTimesheets();
        Assert.assertTrue(checkUrl(URL), "Expected Timesheets page");
        waitWhileLoad();
    }

    protected void waitWhileLoad() {
        getStartDatePicker().waitForVisible(WAITER); // is it need now?
        table = new Element(TIME_SHEET_TABLE);
        table.waitForVisible();
        table.setDefaultElement(getDocument());
    }

    public static boolean isEditableTimesheetPage() {
        return !new Element(ACTIVITY_SUBMITTED.replace(0)).exists();
    }

    public void assertIsEditablePage() {
        Assert.assertTrue(isEditableTimesheetPage(), "Timesheet page is not editable.");
    }

    public String getStartDate() {
        String res = getStartDatePicker().get();
        PSLogger.info("Timesheets date : " + res);
        return res;
    }

    public DatePicker getStartDatePicker() {
        DatePicker dp = new DatePicker(INPUT_START_DATE_PICKER);
        dp.useDatePopup(false);
        return dp;
    }

    public AbstractTimesheetsPage setStartDateAndGo(Date dateToFind) {
        PSLogger.info("Set timesheets date to " + dateToFind + " and Go");
        getStartDatePicker().set(dateToFind.getTime());
        pushGo(false);
        AbstractTimesheetsPage res;
        if (!isEditableTimesheetPage()) {
            res = new TimesheetsPageSubmitted();
        } else {
            res = new TimesheetsPage();
        }
        res.waitWhileLoad();
        return res;
    }

    public void pushGo() {
        pushGo(true);
    }

    private void pushGo(boolean doWait) {
        new Button(BUTTON_GO).click(true);
        if (doWait)
            waitWhileLoad();
    }

    /**
     * Get total value for column, columnNumber starts from 0 to n-1
     *
     * @param columnNumber - column
     * @return total value with index = columnNumber
     */
    public double getColumnTotalValue(int columnNumber) {
        Element cell = new Element(TIME_SHEER_TABLE_LAST_ROW_CELL.replace(columnNumber + 2));
        String result;
        if (getDriver().getType().isIE()) {
            result = cell.getText();
        } else {
            cell.setDefaultElement(document);
            result = cell.getDEText();
        }
        return Double.parseDouble(result);
    }

    public double getSundayTotalValue() {
        return getColumnTotalValue(0);
    }

    public double getMondayTotalValue() {
        return getColumnTotalValue(1);
    }

    public double getTuesdayTotalValue() {
        return getColumnTotalValue(2);
    }

    public double getWednesdayTotalValue() {
        return getColumnTotalValue(3);
    }

    public double getThursdayTotalValue() {
        return getColumnTotalValue(4);
    }

    public double getFridayTotaValuel() {
        return getColumnTotalValue(5);
    }

    public double getSaturdayTotalValue() {
        return getColumnTotalValue(6);
    }

    public double getTotalValue() {
        return getColumnTotalValue(7);
    }

    public Double getRowTotal(int i) {
        Element e = Element.searchElementByXpath(getRows().get(i), TIME_SHEER_TABLE_ROW_TOTAL);
        if (e == null || !e.isDEPresent()) return null;
        String txt = e.getDEText();
        if (!txt.matches("\\d+\\.\\d+")) return null;
        return Double.parseDouble(txt);
    }

    public List<Element> getRows() {
        return Element.searchElementsByXpath(table, TIME_SHEER_TABLE_WORK_ITEM_ROW);
    }

    /**
     * Get number of submitted rows
     *
     * @return number of submitted rows
     * @throws IllegalArgumentException if number is negative
     */
    public int getTimesheetLinesNumber() {
        return getRows().size();
    }

    public int getApprovedTimesheetLineNumber() {
        List<Element> list = getElements(APPROVED_ROWS);
        return list.size();
    }

    public int getRejectedTimesheetLineNumber() {
        List<Element> list = getElements(REJECTED_ROWS);
        return list.size();
    }

    public String getLocation(int i) {
        LocationPopup popup = new LocationPopup((isSubmitted() ? LOCATION_SUBMITTED : LOCATION_EDIT).replace(i), !isSubmitted());
        popup.open();
        TimerWaiter.waitTime(1500);
        List<String> res = popup.getLocation();
        PSLogger.debug("Location: " + res);
        popup.close();
        StringBuilder sb = new StringBuilder();
        for (String s : res) {
            sb.append(s).append(Work.NAME_SEPARATOR);
        }
        return sb.toString().replaceFirst(Work.NAME_SEPARATOR + "$", "");
    }

    public abstract boolean isSubmitted();

    /*
    * Justify Timesheets tests behaviour to comply with latest
    * changes in 8.0 (billing property removed)
    */
    public static boolean getBillingUsed() {
        return TestSession.getAppVersion().verLessThan(PowerSteeringVersions._8_0);
    }


    public Timesheets.Status getStatus(int row) {
        Element tolltip = Element.searchElementByXpath(getRows().get(row), STATUS_TOOLTIP);
        String attr = tolltip.getDEAttribute(STATUS_TOOLTIP_ATTR);
        if (attr.contains(STATUS_NOT_SUBMITTED.getLocator())) return Timesheets.Status.NOT_SUBMITTED;
        if (attr.contains(STATUS_SUBMITTED.getLocator())) return Timesheets.Status.SUBMITTED;
        if (attr.contains(STATUS_REJECTED.getLocator())) return Timesheets.Status.REJECTED;
        if (attr.contains(STATUS_APPROVED.getLocator())) return Timesheets.Status.APPROVED;
        Assert.fail("Can't find status for row " + row + ": " + attr);
        return null;
    }

    public abstract String getWorkItemName(int num);

    public abstract String getActivity(int num);

    public abstract String getBillingCategory(int num);

    public abstract String getTimeSheetCellValue(int row, int column);

}
