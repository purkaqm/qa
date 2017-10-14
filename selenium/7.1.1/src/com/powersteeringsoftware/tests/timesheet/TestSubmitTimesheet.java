package com.powersteeringsoftware.tests.timesheet;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.testng.Assert;
import com.powersteeringsoftware.core.adapters.timesheets.EditedableTimesheetPageAdapter;
import com.powersteeringsoftware.core.adapters.timesheets.SubmittedTimesheetPageAdapter;
import com.powersteeringsoftware.core.managers.timesheet.SubmitNewTimesheetManager;
import com.powersteeringsoftware.core.objects.timesheets.TimeSheetTable;
import com.powersteeringsoftware.core.objects.timesheets.Timesheet;
import com.powersteeringsoftware.core.objects.timesheets.TimesheetLine;
import com.powersteeringsoftware.core.objects.timesheets.TimesheetValues;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;

/**
 *
 * @author selyaev_ag
 * @since 1-b11
 *
 * Before executing this test:
 * - must be created 4 work items
 * - must be created user with permissions for submiting timesheet (timesheet submiter)
 * - work item object must be setuped for using in timesheets:admin/object_types.jsp (automatic)
 * - tag Activity must be filled
 * - billing UIX must be on (User-selectable time sheet rates)
 */
public class TestSubmitTimesheet extends PSTest{
	Timesheet timeSheetToSubmit;
	Timesheet timeSheetSubmited;

	public TestSubmitTimesheet(){
		name = "Save and Submit Timesheet";
		timeSheetToSubmit = createTestTimeSheetObject();
		timeSheetSubmited = new Timesheet();
	}

	protected void run() {
		submitTimesheet();
		createSubmitedTimesheet();
		doAssertion();
	}

	private void createSubmitedTimesheet() {
		timeSheetSubmited = new SubmittedTimesheetPageAdapter(SeleniumDriverSingleton.getDriver()).getTimesheet();
	}

	private void doAssertion() {
		Assert.assertEquals(timeSheetSubmited.equals(timeSheetToSubmit), true, "Timesheets edited and sumbited");
	}

	private void submitTimesheet() {
		new SubmitNewTimesheetManager(timeSheetToSubmit, SeleniumDriverSingleton.getDriver()).manage();
		timeSheetToSubmit.setStartDate(new EditedableTimesheetPageAdapter(SeleniumDriverSingleton.getDriver()).getStartDate());
	}

	/**
	 * Create default timesheet object. Make sure all activities, billing rates
	 * are configured<br>
	 * Start Date is a new Date() with format "MM/dd/yyyy"
	 * @return created Timesheet object
	 */
	public static Timesheet createTestTimeSheetObject(){

		TimesheetLine timeLine1 = new TimesheetLine();
		timeLine1.setActivityOptionLabel("Analysis");
		timeLine1.setBillingCategoryOptionLabel("1 - Category 1");
		timeLine1.setTimesheetValues(new TimesheetValues(1,2,3,4,5,6,7));
		timeLine1.setWorkItem("WORK_ITEM_ANALYSIS");

		TimesheetLine timeLine2 = new TimesheetLine();
		timeLine2.setActivityOptionLabel("Coding");
		timeLine2.setBillingCategoryOptionLabel("2 - Category 2");
		timeLine2.setTimesheetValues(new TimesheetValues(1,2,3,4,5,6,7));
		timeLine2.setWorkItem("WORK_ITEM_CODING");

		TimesheetLine timeLine3 = new TimesheetLine();
		timeLine3.setActivityOptionLabel("Design");
		timeLine3.setBillingCategoryOptionLabel("1 - Category 1");
		timeLine3.setTimesheetValues(new TimesheetValues(1,2,3,4,5,6,7));
		timeLine3.setWorkItem("WORK_ITEM_DESIGN");

		TimesheetLine timeLine4 = new TimesheetLine();
		timeLine4.setActivityOptionLabel("Testing");
		timeLine4.setBillingCategoryOptionLabel("2 - Category 2");
		timeLine4.setTimesheetValues(new TimesheetValues(1,2,3,4,5,6,7));
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

	public static Timesheet createTestTimesheetObject2(){
		TimesheetLine timeLine1 = new TimesheetLine();
		timeLine1.setActivityOptionLabel("Coding");
		timeLine1.setBillingCategoryOptionLabel("1 - Category 1");
		timeLine1.setTimesheetValues(new TimesheetValues(1,2,3,4,5,6,7));
		timeLine1.setWorkItem("WORK_ITEM_ANALYSIS");

		TimesheetLine timeLine2 = new TimesheetLine();
		timeLine2.setActivityOptionLabel("Analysis");
		timeLine2.setBillingCategoryOptionLabel("2 - Category 2");
		timeLine2.setTimesheetValues(new TimesheetValues(1,2,3,4,5,6,7));
		timeLine2.setWorkItem("WORK_ITEM_CODING");

		TimesheetLine timeLine3 = new TimesheetLine();
		timeLine3.setActivityOptionLabel("Testing");
		timeLine3.setBillingCategoryOptionLabel("1 - Category 1");
		timeLine3.setTimesheetValues(new TimesheetValues(1,2,3,4,5,6,7));
		timeLine3.setWorkItem("WORK_ITEM_DESIGN");

		TimesheetLine timeLine4 = new TimesheetLine();
		timeLine4.setActivityOptionLabel("Design");
		timeLine4.setBillingCategoryOptionLabel("2 - Category 2");
		timeLine4.setTimesheetValues(new TimesheetValues(1,2,3,4,5,6,7));
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
