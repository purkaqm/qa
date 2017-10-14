package com.powersteeringsoftware.core.adapters.timesheets;

import com.powersteeringsoftware.core.managers.dojo.WorkItemComponentAdapter;
import com.powersteeringsoftware.core.objects.timesheets.TimesheetLine;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.ILocatorable;
import com.powersteeringsoftware.core.util.waiters.ElementBecomePresentWaiter;
import com.powersteeringsoftware.core.util.waiters.ElementBecomeVisibleWaiter;
import com.powersteeringsoftware.core.util.waiters.TimerWaiter;
import com.thoughtworks.selenium.DefaultSelenium;

public class EditedableTimesheetPageAdapter extends AbstractTimesheetPageAdapter{

	public EditedableTimesheetPageAdapter(DefaultSelenium _selenium){
		super(_selenium);
	}

	public static enum EditedableTimesheetPageLocators implements ILocatorable{
		BUTTON_ADD_LOCATOR("dom=window.dojo.query('[value=Add]')"),
		BUTTON_SAVE_SUBMIT_LOCATOR("dom=window.dojo.query('[value=Save & Submit]')[0]"),

		WORKITEM_LOCATOR_PREFIX("popup_"),
		WORKITEM_LOCATOR_POSTFIX("Show"),

		LINK_COPY_LAST_LOCATOR("LinkSubmit");

		String locator;

		EditedableTimesheetPageLocators(String _locator){
			locator = _locator;
		}

		public String getLocator() {
			return locator;
		}

		public String toString(){
			return locator;
		}
	}

	public void pushSaveSubmit(){
		selenium.click(EditedableTimesheetPageLocators.BUTTON_SAVE_SUBMIT_LOCATOR.getLocator());
		selenium.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
//		ElementBecomePresentWaiter.waitElementBecomePresent(new SubmittedTimesheetPageAdapter().getWorkTimeId(0));
//		new TimerWaiter((int)CoreProperties.getWaitForElementToLoad()).waitTime();
	}

	public void pushSaveChanges(){
		throw new IllegalMonitorStateException("TODO");
	}

	public void pushCancel(){
		throw new IllegalMonitorStateException("TODO");
	}

	public void pushAdd(){
		selenium.click(EditedableTimesheetPageLocators.BUTTON_ADD_LOCATOR.getLocator());
		selenium.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public void fillTimeSheetCell(int row, int column, String value){
		String prefix = AbstractTimesheetPageAdapter.TimeSheetPageLocators.INPUT_VALUE_IDPREFIX.getLocator();
		selenium.type(prefix+row+"_"+column, value);
	}

	public void fillTimeSheetLine(TimesheetLine timeSheetLine, int lineNumber){
		fillTimeSheetCell(lineNumber,0,String.valueOf(timeSheetLine.getTimesheetValues().getSundayTime()));
		fillTimeSheetCell(lineNumber,1,String.valueOf(timeSheetLine.getTimesheetValues().getMondayTime()));
		fillTimeSheetCell(lineNumber,2,String.valueOf(timeSheetLine.getTimesheetValues().getTuesdayTime()));
		fillTimeSheetCell(lineNumber,3,String.valueOf(timeSheetLine.getTimesheetValues().getWednesdayTime()));
		fillTimeSheetCell(lineNumber,4,String.valueOf(timeSheetLine.getTimesheetValues().getThursdayTime()));
		fillTimeSheetCell(lineNumber,5,String.valueOf(timeSheetLine.getTimesheetValues().getFridayTime()));
		fillTimeSheetCell(lineNumber,6,String.valueOf(timeSheetLine.getTimesheetValues().getSaturdayTime()));
	}

	public void addLines(int addLinesNumber) {
			for (int i = 0; i < addLinesNumber; i++) {
				pushAdd();
			}
	}
	/**
	 * Click select Work item on pointed timesheet line
	 *
	 * @param lineNumber - the number of line where work item is clicked (starts from 1)
	 * @throws IllegalArgumentException - if lineNumber less then 1
	 * @return new WorkItemComponentAdapter object
	 */
	public WorkItemComponentAdapter clickWorkItem(int lineNumber){
		SeleniumDriverSingleton
				.getDriver()
				.click(
						getLocatorForWorkItem(lineNumber));

		waitWorkItemComponentBecomeVisible();

		return new WorkItemComponentAdapter();
	}

	private String getLocatorForWorkItem(int lineNumber) {
		return EditedableTimesheetPageLocators.WORKITEM_LOCATOR_PREFIX.getLocator()
				+ lineNumber
				+ EditedableTimesheetPageLocators.WORKITEM_LOCATOR_POSTFIX.getLocator();
	}

	private void waitWorkItemComponentBecomeVisible() {
		ElementBecomeVisibleWaiter.waitElementBecomeVisible(WorkItemComponentAdapter.WorkItemComponentAdapterLocators.WORKITEM_DIV.getLocator());
	}

	public void selectWorkItemInFavorites(String workitemLocator,int lineNumber){
		WorkItemComponentAdapter componentAdapter = clickWorkItem(lineNumber);
		componentAdapter.clickFavoritesWorkItem(workitemLocator);
		new TimerWaiter(1500).waitTime();
	}


	/**
	 * Get select Activity options as an array for pointed line
	 * @param lineNumber
	 * @throws IllegalArgumentException - if lineNumber less then 1
	 * @return String array of options
	 */
	public String[] getActivityOptions(int lineNumber){
		String[] options =selenium.getSelectOptions(getLocatorForActivity(lineNumber));
		return options;
	}

	public void selectActivityOption(String optionLocator, int lineNumber){
		String locator = getLocatorForActivity(lineNumber);
		selenium.select(locator, optionLocator);
	}

	public void selectBillingCategoryOption(String optionLocator, int lineNumber){
		String locator =  getLocatorForBillingCategory(lineNumber);
		selenium.select(locator, optionLocator);
	}

	public String[] getBillingCategoryOptions(int lineNumber){
		String[] options =SeleniumDriverSingleton
		.getDriver()
		.getSelectOptions(
				getLocatorForBillingCategory(lineNumber));

		return options;
	}

	public void clickCopyLast(){
		ElementBecomePresentWaiter.waitElementBecomePresent(EditedableTimesheetPageLocators.LINK_COPY_LAST_LOCATOR.getLocator());
		selenium.click(EditedableTimesheetPageLocators.LINK_COPY_LAST_LOCATOR.getLocator());
		selenium.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public String getWorkName(int lineNumber){
		//return selenium.getEval("window.dojo.query('#popup_"+lineNumber+"Show')[0].innerHTML").trim();
		return selenium.getEval("window.document.getElementById('popup_"+lineNumber+"Show').innerHTML");
	}

	public String getActivity(int lineNumber){
		return selenium.getSelectedLabel((getLocatorForActivity(lineNumber))).trim();
	}

	public String getBillingCategory(int lineNumber){
		return selenium.getSelectedLabel(getLocatorForBillingCategory((lineNumber))).trim();
	}

}