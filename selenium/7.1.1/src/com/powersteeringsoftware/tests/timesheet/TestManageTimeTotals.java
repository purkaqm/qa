package com.powersteeringsoftware.tests.timesheet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.testng.Assert;

import com.powersteeringsoftware.core.adapters.MainMenuAdapter;
import com.powersteeringsoftware.core.adapters.timesheets.managetime.AwaitingApprovalTimesheetsTabAdapter;
import com.powersteeringsoftware.core.managers.timesheet.SubmitNewTimesheetManager;
import com.powersteeringsoftware.core.objects.timesheets.TimeSheetTable;
import com.powersteeringsoftware.core.objects.timesheets.Timesheet;
import com.powersteeringsoftware.core.objects.timesheets.TimesheetLine;
import com.powersteeringsoftware.core.objects.timesheets.TimesheetValues;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.tc.PSTestCaseException;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.thoughtworks.selenium.DefaultSelenium;

/**
 * Test total calculation on the page Browse>>Manage Time/ Tab: Awaiting Approvals
 *
 * @author selyaev_ag
 *
 * @since 1-b16 (2009 June 29)
 *
 */
public class TestManageTimeTotals extends PSTest {

	private Timesheet timesheet;
	private DefaultSelenium selenium;
	private SubmitNewTimesheetManager submitedTimesheetAdapter;
	private AwaitingApprovalTimesheetsTabAdapter awaitingAdapter;

	public TestManageTimeTotals(){
		name = "Calculating Totals on the page Manage Time:Awaiting approvals";
		timesheet = createTestTimesheetObject();
		selenium = SeleniumDriverSingleton.getDriver();
		submitedTimesheetAdapter = new SubmitNewTimesheetManager(timesheet,selenium);
		awaitingAdapter = new AwaitingApprovalTimesheetsTabAdapter(selenium);
	}

	protected void run() {
		submitedTimesheetAdapter.manage();

		Date date;
		try{
			date = new SimpleDateFormat(CoreProperties.DEFAULT_DATE_FORMAT).parse(submitedTimesheetAdapter.getStartDate());
		} catch(ParseException pe){
			throw new PSTestCaseException(pe);
		}

		int userIndex = 0;
		MainMenuAdapter.clickBrowseManageTime();
		awaitingAdapter.navigateStartDate(date);
		awaitingAdapter.expandTimesheet(userIndex);

		assertTotal(userIndex);
		assertDayTotals(userIndex);
		assertWorkTotals(userIndex);
	}

	private void assertWorkTotals(int userIndex) {
		int length = timesheet.getTimesheetTable().getLength();
		for(int i=0; i<length; i++){
			Assert.assertEquals((int)timesheet.getTimesheetTable().getTimesheetLineByIndex(i).getTotal(),
					(int)awaitingAdapter.getWorkTotalForUser(userIndex, i));
		}
	}

	private void assertDayTotals(int userIndex) {
		Assert.assertEquals(timesheet.getTimesheetTable().getSundayTotal(),awaitingAdapter.getSundayTotal(userIndex) );
		Assert.assertEquals(timesheet.getTimesheetTable().getMondayTotal(),awaitingAdapter.getMondayTotal(userIndex));
		Assert.assertEquals(timesheet.getTimesheetTable().getTuesdayTotal(),awaitingAdapter.getTuesdayTotal(userIndex));
		Assert.assertEquals(timesheet.getTimesheetTable().getWednesdayTotal(),awaitingAdapter.getWednesdayTotal(userIndex));
		Assert.assertEquals(timesheet.getTimesheetTable().getThursdayTotal(),awaitingAdapter.getThursdayTotal(userIndex));
		Assert.assertEquals(timesheet.getTimesheetTable().getFridayTotal(),awaitingAdapter.getFridayTotal(userIndex));
		Assert.assertEquals(timesheet.getTimesheetTable().getSaturdayTotal(),awaitingAdapter.getSaturdayTotal(userIndex));
	}

	private void assertTotal(int userIndex) {
		Assert.assertEquals(timesheet.getTimesheetTable().getTotal(), awaitingAdapter.getOverallTotal(userIndex));
	}


	public static Timesheet createTestTimesheetObject(){
		TimesheetLine timeLine1 = new TimesheetLine();
		timeLine1.setActivityOptionLabel("Coding");
		timeLine1.setBillingCategoryOptionLabel("1 - Category 1");
		timeLine1.setTimesheetValues(new TimesheetValues(8.00,8.00,8.00,8.00,8.00,8.00,8.00));
		timeLine1.setWorkItem("WORK_ITEM_ANALYSIS");

		TimesheetLine timeLine2 = new TimesheetLine();
		timeLine2.setActivityOptionLabel("Analysis");
		timeLine2.setBillingCategoryOptionLabel("2 - Category 2");
		timeLine2.setTimesheetValues(new TimesheetValues(8.10,8.10,8.10,8.10,8.10,8.10,8.10));
		timeLine2.setWorkItem("WORK_ITEM_CODING");

		TimesheetLine timeLine3 = new TimesheetLine();
		timeLine3.setActivityOptionLabel("Testing");
		timeLine3.setBillingCategoryOptionLabel("1 - Category 1");
		timeLine3.setTimesheetValues(new TimesheetValues(8.90,8.90,8.90,8.90,8.90,8.90,8.90));
		timeLine3.setWorkItem("WORK_ITEM_DESIGN");

		TimesheetLine timeLine4 = new TimesheetLine();
		timeLine4.setActivityOptionLabel("Design");
		timeLine4.setBillingCategoryOptionLabel("2 - Category 2");
		timeLine4.setTimesheetValues(new TimesheetValues(8.30,8.30,8.30,8.30,8.30,8.30,8.30));
		timeLine4.setWorkItem("WORK_ITEM_TESTING");

		TimeSheetTable timeSheetTableToSubmit = new TimeSheetTable();
		timeSheetTableToSubmit.addTimesheetLine(timeLine1);
		timeSheetTableToSubmit.addTimesheetLine(timeLine2);
		timeSheetTableToSubmit.addTimesheetLine(timeLine3);
		timeSheetTableToSubmit.addTimesheetLine(timeLine4);

		Timesheet timeSheet = new Timesheet();
		timeSheet.setTimesheetTable(timeSheetTableToSubmit);
		timeSheet.setStartDate(new SimpleDateFormat(CoreProperties.getDateFormat()).format(new Date()));

		return timeSheet;
	}
}
