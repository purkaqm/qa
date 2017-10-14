package com.powersteeringsoftware.tests.timesheet;

import org.testng.annotations.Test;
import com.powersteeringsoftware.core.tc.Testable;
//ps_trunk

public class TestDriver{

	public TestDriver() {
	}

	@Test(groups = { "timesheet.SubmitTimesheet" },
			testName = "SubmitTimesheet")
	public void execute_TestSubmitTimesheet()  {
		Testable test = new TestSubmitTimesheet();
		test.execute();
	}

	@Test(groups = { "timesheet.CopyLastTimesheet" },
			testName = "CopyLastTimesheet")
	public void execute_TestCopyLastTimesheet()  {
		Testable test = new TestCopyLastTimesheet();
		test.execute();
	}

	@Test(groups = { "timesheet.ApproveTimesheet" },
			testName = "ApproveTimesheet")
	public void execute_TestApproveTimesheetLines()  {
		Testable test = new TestApproveTimesheetLines();
		test.execute();
	}


	@Test(groups = { "timesheet.RejectTimesheet" },
			testName = "RejectTimesheet")
	public void execute_TestRejectTimesheetLines()  {
		Testable test = new TestRejectTimesheetLines();
		test.execute();
	}

	@Test(groups = { "timesheet.ResubmitTimesheet" },
			testName = "ResubmitTimesheet")
	public void execute_TestResubmitTimesheetLines()  {
		Testable test = new TestResubmitTimesheetLines();
		test.execute();
	}

	@Test(groups = { "timesheet.CheckTimesheetPageTotals" },
			testName = "CheckTimesheetPageTotals")
	public void execute_TestTimesheetTotals()  {
		Testable test = new TestTimesheetTotals();
		test.execute();
	}

	@Test(groups = { "timesheet.TestManageTimeTotals" },
			testName = "TestManageTimeTotals")
	public void execute_TestManageTimeTotals()  {
		Testable test = new TestManageTimeTotals();
		test.execute();
	}

}
