package com.powersteeringsoftware.core.adapters.timesheets.managetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.ILocatorable;
import com.thoughtworks.selenium.DefaultSelenium;

public abstract class AbstractManageTimesheetPageAdapter {

	protected DefaultSelenium selenium;
	private String startDateFormat ="MM/dd/yyyy";


	public enum ManageTimesheetPageAdapter implements ILocatorable {
		IMG_PREVIOUS_WEEK_CLICK("//img[@alt='Next Week']"),
		IMG_NEXT_WEEK_CLICK("//img[@alt='Previous Week']"),

		BUTTON_SUBMIT_CLICK("dom=window.document.getElementById('Submit_0')"),

		BUTTON_REJECT_RUNSCRIPT("dijit.byId('RejectDialog').show();"),
		BUTTON_REFRESH_CLICK("refreshBtn"),

		INPUT_START_DATE_ID("start"),
		INPUT_START_DATE_GETEVAL("window.dojo.byId('start').value"),

		TIMESHEET_VISIBILITY_PREFIX("window.dojo.query('tbody #BRANCHCODE_"),
		TIMESHEET_VISIBILITY_POSTFIX("')[0].attributes[0].value"),

		TIMESHEET_EXPAND_OR_COLLAPSE_PREFIX("javascript:ps.Base.tableToggle('BRANCHCODE_"),
		TIMESHEET_EXPANDCOLLAPSE_POSTFIX_RUNSCRIPT("', 'Expand', 'Collapse')"),

		TIMESHEET_LINE_NUMBER_PREFIX_GETEVAL("window.dojo.query('#BRANCHCODE_"),
		TIMESHEET_LINE_NUMBER_POSTFIX_GETEVAL(" tr').length;"),

		TIMESHEET_DAYTOTAL_GETEVAL_PREFIX("var param=window.dojo.query('[href*=BRANCHCODE_"),
		TIMESHEET_DAYTOTAL_GETEVAL_MIDDLE("]')[0].parentNode.parentNode.parentNode; window.dojo.query('th',param)["),
		TIMESHEET_DAYTOTAL_GETEVAL_POSTFIX("].innerHTML;"),

		TIMESHEET_WORKTOTAL_GETEVAL_PREFIX("var param = window.dojo.query('#BRANCHCODE_"),
		TIMESHEET_WORKTOTAL_GETEVAL_MIDDLE(" tr')["),
		TIMESHEET_WORKTOTAL_GETEVAL_POSTFIX("]; window.dojo.query('td',param)[11].innerHTML;");


		String locator;

		ManageTimesheetPageAdapter(String _locator){
			locator = _locator;
		}

		public String getLocator() {
			return locator;
		}
	}

	public AbstractManageTimesheetPageAdapter(){
	}

	public void pushRefresh(){
		selenium.click(ManageTimesheetPageAdapter.BUTTON_REFRESH_CLICK.getLocator());
		selenium.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public String getStartDateAsString(){
		return selenium.getEval(ManageTimesheetPageAdapter.INPUT_START_DATE_GETEVAL.getLocator());
	}

	public Date getStartDateAsDate(){
		try {
			return new SimpleDateFormat(startDateFormat).parse(getStartDateAsString());
		} catch (ParseException e) {
			throw new IllegalMonitorStateException("Can't parse start date:"+getStartDateAsString());
		}
	}

	public void navigateStartDate(Date dateToFind){
		SimpleDateFormat sdf = new SimpleDateFormat(startDateFormat);
		selenium.type(ManageTimesheetPageAdapter.INPUT_START_DATE_ID.getLocator(),
				sdf.format(dateToFind));
		pushRefresh();
	}

	public void setStartDateFormat(String _startDateFormat){
		startDateFormat = _startDateFormat;
	}

	public void pushPreviousWeek(){
		selenium.click(ManageTimesheetPageAdapter.IMG_PREVIOUS_WEEK_CLICK.getLocator());
		selenium.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}


	public void pushNextWeek(){
		selenium.click(ManageTimesheetPageAdapter.IMG_NEXT_WEEK_CLICK.getLocator());
		selenium.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public void pushApprove(){
		selenium.click(ManageTimesheetPageAdapter.BUTTON_SUBMIT_CLICK.getLocator());
		selenium.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}


	private RejectTimesheetWindowAdapter pushReject(){
		selenium.runScript(ManageTimesheetPageAdapter.BUTTON_REJECT_RUNSCRIPT.locator);
		return new RejectTimesheetWindowAdapter(selenium);
	}


	public void pushReject(String explanation){
		RejectTimesheetWindowAdapter adapter = pushReject();
		adapter.typeExplanation(explanation);
		adapter.pushReject();
	}


	public boolean isShow(int branchcode){
		return getVisibilityForTimesheet(branchcode).contains("show");
	}

	public boolean isHide(int branchcode){
		return getVisibilityForTimesheet(branchcode).contains("hide");
	}

	private String getVisibilityForTimesheet(int branchcode){
		return  selenium.getEval(ManageTimesheetPageAdapter.TIMESHEET_VISIBILITY_PREFIX.getLocator()
				+ branchcode
				+ ManageTimesheetPageAdapter.TIMESHEET_VISIBILITY_POSTFIX.getLocator());
	}

	public void expandTimesheet(int branchcode){
		if(isHide(branchcode)) clickTimesheetOwnwer(branchcode);
	}

	private void clickTimesheetOwnwer(int branchcode){
		selenium.runScript(ManageTimesheetPageAdapter.TIMESHEET_EXPAND_OR_COLLAPSE_PREFIX.getLocator()
				+ branchcode
				+ ManageTimesheetPageAdapter.TIMESHEET_EXPANDCOLLAPSE_POSTFIX_RUNSCRIPT.getLocator());
	}

	public void collapseTimesheet(int branchcode){
		if(isShow(branchcode)) clickTimesheetOwnwer(branchcode);
	}

	/**
	 * Get Cell value for the timesheet table. Method is used for getting value for user totals.
	 *
	 * @param userIndex - starts from 0
	 * @param columnNumber - starts from 0
	 * @return evaluated value for found cell
	 */
	private String getDayTotalValueForUser(int userIndex, int columnNumber){
		String evalStr = ManageTimesheetPageAdapter.TIMESHEET_DAYTOTAL_GETEVAL_PREFIX.getLocator()
		+ userIndex
		+ ManageTimesheetPageAdapter.TIMESHEET_DAYTOTAL_GETEVAL_MIDDLE.getLocator()
		+ columnNumber
		+ ManageTimesheetPageAdapter.TIMESHEET_DAYTOTAL_GETEVAL_POSTFIX.getLocator();

		return selenium.getEval(evalStr.trim());
	}

	public double getSundayTotal(int userIndex){
		return Double.parseDouble(getDayTotalValueForUser(userIndex,4));
	}

	public double getMondayTotal(int userIndex){
		return Double.parseDouble(getDayTotalValueForUser(userIndex,5));
	}

	public double getTuesdayTotal(int userIndex){
		return Double.parseDouble(getDayTotalValueForUser(userIndex,6));
	}

	public double getWednesdayTotal(int userIndex){
		return Double.parseDouble(getDayTotalValueForUser(userIndex,7));
	}

	public double getThursdayTotal(int userIndex){
		return Double.parseDouble(getDayTotalValueForUser(userIndex,8));
	}

	public double getFridayTotal(int userIndex){
		return Double.parseDouble(getDayTotalValueForUser(userIndex,9));
	}

	public double getSaturdayTotal(int userIndex){
		return Double.parseDouble(getDayTotalValueForUser(userIndex,10));
	}

	public double getOverallTotal(int userIndex){
		return Double.parseDouble(getDayTotalValueForUser(userIndex,11));
	}

	public double getWorkTotalForUser(int userIndex, int workIndex){
		String evalStr = ManageTimesheetPageAdapter.TIMESHEET_WORKTOTAL_GETEVAL_PREFIX.getLocator()
		+ userIndex
		+ ManageTimesheetPageAdapter.TIMESHEET_WORKTOTAL_GETEVAL_MIDDLE.getLocator()
		+ workIndex
		+ ManageTimesheetPageAdapter.TIMESHEET_WORKTOTAL_GETEVAL_POSTFIX.getLocator();

		return Double.parseDouble(selenium.getEval(evalStr));
	}


}
