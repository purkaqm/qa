package com.powersteeringsoftware.tests.timesheet;

import org.testng.Assert;
import com.powersteeringsoftware.core.adapters.MainMenuAdapter;
import com.powersteeringsoftware.core.adapters.timesheets.SubmittedTimesheetPageAdapter;
import com.powersteeringsoftware.core.managers.timesheet.SubmitNewTimesheetManager;
import com.powersteeringsoftware.core.managers.timesheet.TimesheetRejectingManager;
import com.powersteeringsoftware.core.objects.timesheets.Timesheet;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;

public class TestRejectTimesheetLines extends PSTest {

	private Timesheet timesheet;

	public TestRejectTimesheetLines(){
		this.name = "Reject timesheet line";
	}

	protected void run() {
		createTimesheet();
		rejectTimesheet();
		doAssertion();
	}

	private void doAssertion() {
		MainMenuAdapter.clickBrowseTimesheet();
		SubmittedTimesheetPageAdapter submittedTSAdapter = new SubmittedTimesheetPageAdapter(SeleniumDriverSingleton.getDriver());
		submittedTSAdapter.navigateStartDate(timesheet.getStartDateAsDate(CoreProperties.getDateFormat()));
		Assert.assertEquals(timesheet.getTimesheetTable().getLength(), submittedTSAdapter.getRejectedTimesheetLineNumber());
	}

	private void rejectTimesheet() {
		new TimesheetRejectingManager(timesheet, SeleniumDriverSingleton.getDriver()).manage();
	}

	private void createTimesheet(){
		timesheet = TestSubmitTimesheet.createTestTimeSheetObject();
		MainMenuAdapter.clickBrowseTimesheet();
		new SubmitNewTimesheetManager(timesheet,SeleniumDriverSingleton.getDriver()).manage();
		timesheet.setStartDate(new SubmittedTimesheetPageAdapter(SeleniumDriverSingleton.getDriver()).getStartDate());
	}
}
