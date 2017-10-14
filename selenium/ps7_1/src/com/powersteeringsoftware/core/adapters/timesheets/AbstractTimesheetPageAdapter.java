package com.powersteeringsoftware.core.adapters.timesheets;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.testng.Assert;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.ILocatorable;
import com.powersteeringsoftware.core.util.waiters.ElementBecomePresentWaiter;
import com.thoughtworks.selenium.DefaultSelenium;


public abstract class AbstractTimesheetPageAdapter {

	public static final int LINES_NUMBER_FOR_EACH_ADD = 5;

	private String startDateFormat ="MM/dd/yyyy";
	protected DefaultSelenium selenium;

	public AbstractTimesheetPageAdapter(DefaultSelenium _selenium){
		selenium = _selenium;
	}

	public static enum TimeSheetPageLocators implements ILocatorable{
		NEXT_WEEK("dom=window.dojo.query('[alt=Next Week]')[0]"),
		PREVIOUS_WEEK("dom=window.dojo.query('[alt=Previous Week]')[0]"),

		PAGE_URL("Timesheets.epage"),

		INPUT_VALUE_IDPREFIX("value_"),
		INPUT_VALUE_GETEVAL_PREFIX("window.dojo.query('[id^=value_"),
		INPUT_VALUE_GETEVAL_POSTFIX("]')[0].innerHTML"),

		ACTIVITY_PREFIX("dom=window.dojo.query('[id^=act"),
		ACTIVITY_POSTFIX("]')[0]"),

		BILLING_CATEGORY_PREFIX("dom=window.dojo.query('[id^=bil"),
		BILLING_CATEGORY_POSTFIX("]')[0]"),

		//TEXT_TIMESHEET_DATES("window.dojo.query('#main div.toolbar-top .left')[0].childNodes[2]"),
		INPUT_START_DATE_GETEVAL("dom=window.dojo.query('#PSDatePicker')[0].value"),
		INPUT_START_DATE_LOCATOR("PSDatePicker"),

		BUTTON_GO_LOCATOR("dom=window.document.getElementById('Submit_0')"),

		ROW_TOTAL_GETDATE_PREFIX("var tr = window.dojo.query('#TimesheetsContainer table tr')["),
		ROW_TOTAL_GETDATE_POSTFIX("]window.dojo.query('td',tr)[11].innerHTML;");

		String locator;

		TimeSheetPageLocators(String _locator){
			locator = _locator;
		}

		public String getLocator() {
			return locator;
		}

		public String toString(){
			return locator;
		}
	}

	public boolean isTimesheetPage(){
		return selenium.getLocation().contains(TimeSheetPageLocators.PAGE_URL.getLocator());
	}

	public boolean isEditableTimesheetPage(){
		return !selenium.isElementPresent("dom=window.dojo.query('span #act0')[0];");
	}

	public boolean isSubmitedTimesheetPage(){
		return selenium.isElementPresent("dom=window.dojo.query('span #act0')[0];");
	}

	public void assertIsEditablePage(){
		Assert.assertEquals(true, isEditableTimesheetPage(),"Timesheet page is not editable.");
	}

	public void assertIsSubmittedPage(){
		Assert.assertEquals(true, isSubmitedTimesheetPage(),"Timesheet page is not submitted.");
	}

	public void clickNextWeek(){
		selenium.click(TimeSheetPageLocators.NEXT_WEEK.getLocator());
	}

	public void clickPreviousWeek(){
		selenium.click(TimeSheetPageLocators.PREVIOUS_WEEK.getLocator());
	}

	/**
	 *
	 * @param lineNumber line number, starts from 0 to n-1
	 * @return
	 */
	public String getLocatorForBillingCategory(int lineNumber) {
		return TimeSheetPageLocators.BILLING_CATEGORY_PREFIX.getLocator()
				+ lineNumber
				+ TimeSheetPageLocators.BILLING_CATEGORY_POSTFIX.getLocator();
	}

	/**
	 *
	 * @param lineNumber line number, starts from 0 to n-1
	 * @return
	 */
	protected String getLocatorForActivity(int lineNumber) {
		return TimeSheetPageLocators.ACTIVITY_PREFIX.getLocator()
				+ lineNumber
				+ TimeSheetPageLocators.ACTIVITY_POSTFIX.getLocator();
	}

	public String getStartDate(){
		ElementBecomePresentWaiter.waitElementBecomePresent(TimeSheetPageLocators.INPUT_START_DATE_GETEVAL.getLocator());
		return selenium.getEval(TimeSheetPageLocators.INPUT_START_DATE_GETEVAL.getLocator());
	}

	public void navigateStartDate(Date dateToFind){
		SimpleDateFormat sdf = new SimpleDateFormat(startDateFormat);
		selenium.type(TimeSheetPageLocators.INPUT_START_DATE_LOCATOR.getLocator(),
				sdf.format(dateToFind));
		pushGo();
	}

	public void pushGo(){
		selenium.click(TimeSheetPageLocators.BUTTON_GO_LOCATOR.getLocator());
		selenium.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public String getStartDateFormat() {
		return startDateFormat;
	}

	public void setStartDateFormat(String startDateFormat) {
		this.startDateFormat = startDateFormat;
	}

	/**
	 *
	 * @param lineNumber line number, starts from 0 to n-1.
	 * @return parsed double value
	 *
	 */
	public double getRowTotal(int lineNumber) {
		return Double.parseDouble(selenium.getEval(
						TimeSheetPageLocators.ROW_TOTAL_GETDATE_PREFIX.getLocator()
						+ (lineNumber+1)
						+ TimeSheetPageLocators.ROW_TOTAL_GETDATE_POSTFIX.getLocator()));
		}

	/**
	 * Get total value for column, columnNumber starts from 0 to n-1
	 * @param columnNumber - column
	 * @return total value with index = columnNumber
	 */
	private double getColumnTotalValue(int columnNumber){
		String eval=
		"var row = window.dojo.query('#TimesheetsContainer table tr')["
		+(getTimesheetLinesNumber()+1)
		+"]; window.dojo.query('th',row)["+(columnNumber+1)+"].innerHTML;";
		String result = selenium.getEval(eval);
		return Double.parseDouble(result);
	}

	public double getSundayTotalValue(){
		return getColumnTotalValue(0);
	}

	public double getMondayTotalValue(){
		return getColumnTotalValue(1);
	}

	public double getTuesdayTotalValue(){
		return getColumnTotalValue(2);
	}

	public double getWednesdayTotalValue(){
		return getColumnTotalValue(3);
	}

	public double getThursdayTotalValue(){
		return getColumnTotalValue(4);
	}

	public double getFridayTotaValuel(){
		return getColumnTotalValue(5);
	}

	public double getSaturdayTotalValue(){
		return getColumnTotalValue(6);
	}

	public double getTotalValue(){
		return getColumnTotalValue(7);
	}

	/**
	 * Get number of submitted rows
	 * @return number of submitted rows
	 * @throws IllegalArgumentException if number is negative
	 */
	public int getTimesheetLinesNumber(){
		int number = Integer.parseInt(selenium.getEval("window.dojo.query('#TimesheetsContainer table tr').length"));
		number =number-2;
		if (number<0) throw new IllegalArgumentException("Rows number can't be negative:"+number);
		return number;
	}

	abstract public String getWorkName(int lineNumber);

	abstract public String getBillingCategory(int lineNumber);

	abstract public String getActivity(int lineNumber);
}
