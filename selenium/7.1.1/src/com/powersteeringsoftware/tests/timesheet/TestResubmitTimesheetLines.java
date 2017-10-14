package com.powersteeringsoftware.tests.timesheet;

import com.powersteeringsoftware.core.managers.timesheet.SubmitNewTimesheetManager;
import com.powersteeringsoftware.core.managers.timesheet.TimesheetApprovingManager;
import com.powersteeringsoftware.core.managers.timesheet.TimesheetRejectingManager;
import com.powersteeringsoftware.core.objects.timesheets.Timesheet;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.waiters.TimerWaiter;

public class TestResubmitTimesheetLines extends PSTest {

	public TestResubmitTimesheetLines(){
		name = "Resubmit timesheet";
	}


    protected void run() {
        Timesheet timesheet = TestSubmitTimesheet.createTestTimesheetObject2();
        Timesheet timesheet2 = TestSubmitTimesheet.createTestTimeSheetObject();

        SubmitNewTimesheetManager newTimesheetAdapter = new SubmitNewTimesheetManager(timesheet, SeleniumDriverSingleton.getDriver());
        newTimesheetAdapter.manage();
        timesheet2.setStartDate(newTimesheetAdapter.getStartDate());
        timesheet.setStartDate(newTimesheetAdapter.getStartDate());

        new TimesheetRejectingManager(timesheet, SeleniumDriverSingleton.getDriver()).manage();
        new TimerWaiter(1000).waitTime();
        new SubmitNewTimesheetManager(timesheet2, SeleniumDriverSingleton.getDriver()).manage();
        new TimerWaiter(1000).waitTime();
        new TimesheetApprovingManager(timesheet2, SeleniumDriverSingleton.getDriver()).manage();
    }

}
