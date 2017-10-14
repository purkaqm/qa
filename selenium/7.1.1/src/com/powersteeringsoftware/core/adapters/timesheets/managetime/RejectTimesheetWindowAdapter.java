package com.powersteeringsoftware.core.adapters.timesheets.managetime;

import com.powersteeringsoftware.core.util.ILocatorable;
import com.powersteeringsoftware.core.util.waiters.ElementBecomeDisappearedWaiter;
import com.powersteeringsoftware.core.util.waiters.ElementBecomeVisibleWaiter;
import com.thoughtworks.selenium.DefaultSelenium;

public class RejectTimesheetWindowAdapter {

	enum RejectTimesheetWindowLocators implements ILocatorable{
		WINDOW_ID("RejectDialog"),
		TYPE_TEXTAREA_EXPLANATION("rejectText"),
		BUTTON_REJECT_RUN_SCRIPT("dojo.byId('rejectBtn').click();"),
		BUTTON_REJECT_RUN_LOCATOR("dojo.byId('rejectBtn');"),
		BUTTON_CANCEL_ID("RejectDialogHide");

		String locator;

		RejectTimesheetWindowLocators(String _locator){
			locator = _locator;
		}

		public String getLocator() {
			return locator;
		}
	}

	private DefaultSelenium selenium;

	RejectTimesheetWindowAdapter(DefaultSelenium _selenium){
		selenium = _selenium;
	}

	public void waitThisBecomeVisible(){
		ElementBecomeVisibleWaiter.waitElementBecomeVisible(RejectTimesheetWindowLocators.WINDOW_ID.getLocator());
	}

	public void pushReject(){
		waitThisBecomeVisible();
		selenium.runScript(RejectTimesheetWindowLocators.BUTTON_REJECT_RUN_SCRIPT.getLocator());
		ElementBecomeDisappearedWaiter.waitElementDissapeared(RejectTimesheetWindowLocators.BUTTON_REJECT_RUN_LOCATOR.getLocator());
	}

	public void pushCancel(){
		waitThisBecomeVisible();
		selenium.click(RejectTimesheetWindowLocators.BUTTON_CANCEL_ID.getLocator());
		waitThisBecomeVisible();
	}

	public void typeExplanation(String explanation){
		waitThisBecomeVisible();
		selenium.type(RejectTimesheetWindowLocators.TYPE_TEXTAREA_EXPLANATION.getLocator(), explanation);
	}
}
