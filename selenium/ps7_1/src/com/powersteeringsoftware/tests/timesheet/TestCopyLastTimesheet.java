package com.powersteeringsoftware.tests.timesheet;

import com.powersteeringsoftware.core.adapters.MainMenuAdapter;
import com.powersteeringsoftware.core.adapters.timesheets.EditedableTimesheetPageAdapter;
import com.powersteeringsoftware.core.adapters.timesheets.SubmittedTimesheetPageAdapter;
import com.powersteeringsoftware.core.managers.timesheet.SubmitNewTimesheetManager;
import com.powersteeringsoftware.core.objects.timesheets.Timesheet;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;

/**
 * Test last time sheet can be copied.
 *
 * @since 1b-11
 *
 */
public class TestCopyLastTimesheet extends PSTest {

	Timesheet timesheet;
	EditedableTimesheetPageAdapter adapter;

	public TestCopyLastTimesheet() {
		name = "Copy Last Timesheet";
		adapter = new EditedableTimesheetPageAdapter(SeleniumDriverSingleton.getDriver());
	}

	protected void run() {
		createTimesheet();

		MainMenuAdapter.clickBrowseTimesheet();
		adapter.clickCopyLast();

		for (int i = 0; i < timesheet.getTimesheetTable().getLength(); i++) {
			assertWorks(i);
			assertActivities(i);
			assertBillingCategories(i);
			adapter.fillTimeSheetCell(i, 0, "8.0");
			adapter.fillTimeSheetCell(i, 1, "8.0");
			adapter.fillTimeSheetCell(i, 2, "8.0");
			adapter.fillTimeSheetCell(i, 3, "8.0");
			adapter.fillTimeSheetCell(i, 4, "8.0");
			adapter.fillTimeSheetCell(i, 5, "8.0");
			adapter.fillTimeSheetCell(i, 6, "8.0");
		}
		adapter.pushSaveSubmit();
	}

	private void assertBillingCategories(int lineNumber) {
		adapter.getBillingCategory(lineNumber).equals(
				timesheet.getTimesheetTable().getTimesheetLineByIndex(
						lineNumber).getBillingCategoryOptionLabel());
	}

	private void assertActivities(int lineNumber) {
		adapter.getActivity(lineNumber).equals(
				timesheet.getTimesheetTable().getTimesheetLineByIndex(
						lineNumber).getActivityOptionLabel());
	}

	private void assertWorks(int lineNumber) {
		adapter.getWorkName(lineNumber).equals(
				timesheet.getTimesheetTable().getTimesheetLineByIndex(
						lineNumber).getWorkItem());
	}

	private void createTimesheet(){
		timesheet = TestSubmitTimesheet.createTestTimeSheetObject();
		MainMenuAdapter.clickBrowseTimesheet();
		new SubmitNewTimesheetManager(timesheet,SeleniumDriverSingleton.getDriver()).manage();
		timesheet.setStartDate(new SubmittedTimesheetPageAdapter(SeleniumDriverSingleton.getDriver()).getStartDate());
	}
}
