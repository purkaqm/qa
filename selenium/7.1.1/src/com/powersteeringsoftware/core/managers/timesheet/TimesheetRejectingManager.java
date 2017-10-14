package com.powersteeringsoftware.core.managers.timesheet;

import com.powersteeringsoftware.core.adapters.MainMenuAdapter;
import com.powersteeringsoftware.core.adapters.timesheets.managetime.AwaitingApprovalTimesheetsTabAdapter;
import com.powersteeringsoftware.core.managers.IManageable;
import com.powersteeringsoftware.core.objects.timesheets.Timesheet;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.thoughtworks.selenium.DefaultSelenium;

public class TimesheetRejectingManager implements IManageable {

	private Timesheet timesheet;
	private DefaultSelenium selenium;

	public TimesheetRejectingManager(Timesheet _timesheet, DefaultSelenium _selenium) {
		timesheet = _timesheet;
		selenium = _selenium;
	}

	public void manage(){
		MainMenuAdapter.clickBrowseManageTime();
		AwaitingApprovalTimesheetsTabAdapter manageTimesheetAdapter = new AwaitingApprovalTimesheetsTabAdapter(selenium);;
		manageTimesheetAdapter.navigateStartDate(timesheet.getStartDateAsDate(CoreProperties.getDateFormat()));
		manageTimesheetAdapter.expandTimesheet(0);
		manageTimesheetAdapter.checkAllStatuses(0);
		manageTimesheetAdapter.pushReject("Test rejection");
	}

}
