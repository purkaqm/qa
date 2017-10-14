package com.powersteeringsoftware.core.managers.timesheet;

import com.powersteeringsoftware.core.adapters.MainMenuAdapter;
import com.powersteeringsoftware.core.adapters.timesheets.EditedableTimesheetPageAdapter;
import com.powersteeringsoftware.core.managers.IManageable;
import com.powersteeringsoftware.core.objects.timesheets.Timesheet;
import com.thoughtworks.selenium.DefaultSelenium;

public class SubmitNewTimesheetManager implements IManageable {

	private Timesheet timesheet;
	private DefaultSelenium selenium;
	private EditedableTimesheetPageAdapter adapter;

	private String startDate;

	public SubmitNewTimesheetManager(Timesheet _timesheet, DefaultSelenium _selenium) {
		timesheet = _timesheet;
		selenium = _selenium;
		adapter = new EditedableTimesheetPageAdapter(selenium);
	}

	public void manage() {
		if (timesheet == null){
			throw new IllegalMonitorStateException("TimeSheet is null.");
		}
		MainMenuAdapter.clickBrowseTimesheet();
		adapter.assertIsEditablePage();
		startDate = adapter.getStartDate();
		fillTimesheet();
		adapter.pushSaveSubmit();
	}

	public void setTimesheet(Timesheet _timesheet) {
		timesheet = _timesheet;
	}

	private int getPushButtonAddCount() {
		return timesheet.getTimesheetTable().getLength() / 5 + timesheet.getTimesheetTable().getLength() % 5;
	}

	private void increaseLinesNumber() {
		if (EditedableTimesheetPageAdapter.LINES_NUMBER_FOR_EACH_ADD < timesheet.getTimesheetTable().getLength()) {
			adapter.addLines(getPushButtonAddCount());
		}
	}

	private void fillTimesheet() {
		increaseLinesNumber();
		for (int i = 0; i < timesheet.getTimesheetTable().getLength(); i++) {
			adapter.fillTimeSheetLine(timesheet.getTimesheetTable().getTimesheetLineByIndex(i), i);
			adapter.selectWorkItemInFavorites("link="+timesheet.getTimesheetTable().getTimesheetLineByIndex(i).getWorkItem(),i);
			adapter.selectActivityOption("label="+timesheet.getTimesheetTable().getTimesheetLineByIndex(i).getActivityOptionLabel(),i);
			adapter.selectBillingCategoryOption("label="+timesheet.getTimesheetTable().getTimesheetLineByIndex(i).getBillingCategoryOptionLabel(), i);
		}
	}

	public DefaultSelenium getSelenium() {
		return selenium;
	}

	public void setSelenium(DefaultSelenium selenium) {
		this.selenium = selenium;
	}

	public String getStartDate() {
		if (startDate == null) throw new IllegalMonitorStateException("You must call manage method before getting the start date");
		return startDate;
	}
}
