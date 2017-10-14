package com.powersteeringsoftware.core.adapters.timesheets.managetime;

import com.powersteeringsoftware.core.util.waiters.ElementBecomeVisibleWaiter;
import com.thoughtworks.selenium.DefaultSelenium;

public class AwaitingApprovalTimesheetsTabAdapter extends AbstractManageTimesheetPageAdapter{

	public AwaitingApprovalTimesheetsTabAdapter(DefaultSelenium _selenium){
		selenium = _selenium;
	}

	public void waitThisBecomeVisible(){
		ElementBecomeVisibleWaiter.waitElementBecomeVisible(ManageTimesheetPageAdapter.INPUT_START_DATE_ID.locator);
	}

	public int getTimesheetLineNumber(int branchcode){
		String locator = ManageTimesheetPageAdapter.TIMESHEET_LINE_NUMBER_PREFIX_GETEVAL.getLocator()
		+ branchcode
		+ ManageTimesheetPageAdapter.TIMESHEET_LINE_NUMBER_POSTFIX_GETEVAL.getLocator();
		String lineNumber = selenium.getEval(locator);
		return Integer.parseInt(lineNumber);
	}

	public void checkStatus(int branchcode, int linenumber){
		String locator =
		"dom=var param = window.dojo.query('#BRANCHCODE_"
				+ branchcode
				+ " tr')["
				+ linenumber
				+ "]; window.dojo.query('input',param)[0]";
		selenium.check(locator);
	}

	public void checkAllStatuses(int branchcode){
		int lineNumber = getTimesheetLineNumber(branchcode);
		for(int i=0;i<lineNumber;i++){
			checkStatus(branchcode,i);
		}
	}

}
