
package com.powersteeringsoftware.tests.timesheet;

import org.testng.Assert;
import com.powersteeringsoftware.core.adapters.MainMenuAdapter;
import com.powersteeringsoftware.core.adapters.timesheets.SubmittedTimesheetPageAdapter;
import com.powersteeringsoftware.core.managers.timesheet.SubmitNewTimesheetManager;
import com.powersteeringsoftware.core.managers.timesheet.TimesheetApprovingManager;
import com.powersteeringsoftware.core.objects.timesheets.Timesheet;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;

/**
 * Test approving time sheet lines</br>
 *
 * Before test executing must be executed:
 * - TestSubmitTimesheet
 * @since 1b-12 (2009 June 15)
 *
 */
public class TestApproveTimesheetLines extends PSTest {

	private Timesheet timesheet;

	public TestApproveTimesheetLines(){
		this.name = "Approve timesheet line";
		timesheet = new Timesheet();
	}

	protected void run() {
		createTimesheet();
		approveTimesheet();
		doAssertion();
	}

	private void doAssertion() {
		MainMenuAdapter.clickBrowseTimesheet();
		SubmittedTimesheetPageAdapter submittedTSAdapter = new SubmittedTimesheetPageAdapter(SeleniumDriverSingleton.getDriver());
		submittedTSAdapter.navigateStartDate(timesheet.getStartDateAsDate(CoreProperties.getDateFormat()));
		Assert.assertEquals(timesheet.getTimesheetTable().getLength(), submittedTSAdapter.getApprovedTimesheetLineNumber());
	}

	private void approveTimesheet() {
		new TimesheetApprovingManager(timesheet, SeleniumDriverSingleton.getDriver()).manage();
	}

	private void createTimesheet(){
		timesheet = TestSubmitTimesheet.createTestTimeSheetObject();
		MainMenuAdapter.clickBrowseTimesheet();
		new SubmitNewTimesheetManager(timesheet,SeleniumDriverSingleton.getDriver()).manage();
		timesheet.setStartDate(new SubmittedTimesheetPageAdapter(SeleniumDriverSingleton.getDriver()).getStartDate());
	}
}
