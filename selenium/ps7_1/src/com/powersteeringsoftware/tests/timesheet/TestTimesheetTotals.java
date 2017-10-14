package com.powersteeringsoftware.tests.timesheet;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.testng.Assert;

import com.powersteeringsoftware.core.adapters.timesheets.SubmittedTimesheetPageAdapter;
import com.powersteeringsoftware.core.managers.timesheet.SubmitNewTimesheetManager;
import com.powersteeringsoftware.core.objects.timesheets.TimeSheetTable;
import com.powersteeringsoftware.core.objects.timesheets.Timesheet;
import com.powersteeringsoftware.core.objects.timesheets.TimesheetLine;
import com.powersteeringsoftware.core.objects.timesheets.TimesheetValues;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;

public class TestTimesheetTotals extends PSTest {

	private SubmittedTimesheetPageAdapter sumbmittedAdapter;
	private Timesheet timesheet;

	private int lineNumber;

	public TestTimesheetTotals(){
		name = "Totals calculation on the page Timesheet";
		sumbmittedAdapter = new SubmittedTimesheetPageAdapter(SeleniumDriverSingleton.getDriver());
		timesheet = createTestTimesheetObject();
	}

	protected void run() {
		new SubmitNewTimesheetManager(timesheet, SeleniumDriverSingleton.getDriver()).manage();
		lineNumber = sumbmittedAdapter.getTimesheetLinesNumber();

		assertRowTotals();
		assertDayTotals();
		assertTotal();

	}

	private void assertDayTotals() {
		Assert.assertEquals(sumbmittedAdapter.getSundayTotalValue(),timesheet.getTimesheetTable().getSundayTotal());
		Assert.assertEquals(sumbmittedAdapter.getMondayTotalValue(),timesheet.getTimesheetTable().getMondayTotal());
		Assert.assertEquals(sumbmittedAdapter.getTuesdayTotalValue(),timesheet.getTimesheetTable().getTuesdayTotal());
		Assert.assertEquals(sumbmittedAdapter.getWednesdayTotalValue(),timesheet.getTimesheetTable().getWednesdayTotal());
		Assert.assertEquals(sumbmittedAdapter.getThursdayTotalValue(),timesheet.getTimesheetTable().getThursdayTotal());
		Assert.assertEquals(sumbmittedAdapter.getFridayTotaValuel(),timesheet.getTimesheetTable().getFridayTotal());
		Assert.assertEquals(sumbmittedAdapter.getSaturdayTotalValue(),timesheet.getTimesheetTable().getSaturdayTotal());
	}

	private void assertTotal() {
		Assert.assertEquals(sumbmittedAdapter.getTotalValue(),
		timesheet.getTimesheetTable().getTotal());
	}

	private void assertRowTotals() {
		for(int i=0;i<lineNumber;i++){
			Assert.assertEquals(sumbmittedAdapter.getTimesheetLine(i).getTotal(),
			timesheet.getTimesheetTable().getTimesheetLineByIndex(i).getTotal());			
		}
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
