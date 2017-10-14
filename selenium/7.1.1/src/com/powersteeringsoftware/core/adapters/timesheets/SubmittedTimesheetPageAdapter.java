package com.powersteeringsoftware.core.adapters.timesheets;

import com.powersteeringsoftware.core.objects.timesheets.TimeSheetTable;
import com.powersteeringsoftware.core.objects.timesheets.Timesheet;
import com.powersteeringsoftware.core.objects.timesheets.TimesheetLine;
import com.powersteeringsoftware.core.objects.timesheets.TimesheetValues;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.ILocatorable;
import com.thoughtworks.selenium.DefaultSelenium;

public class SubmittedTimesheetPageAdapter extends AbstractTimesheetPageAdapter {

	public SubmittedTimesheetPageAdapter(DefaultSelenium _selenium){
		super(_selenium);
	}

	enum SubmittedTimesheetPageLocators implements ILocatorable{
		WORK_NAME_IDPREFIX("work"),

		WORK_NAME_GETEVAL_PREFIX("window.dojo.query('#work"),
		WORK_NAME_GETEVAL_POSTFIX(" a')[0].innerHTML");

		String locator;

		SubmittedTimesheetPageLocators(String _locator){
			locator = _locator;
		}

		public String getLocator(){
			return locator;
		}
	}

	/**
	 * Starts from 0 to n-1
	 */
	public String getBillingCategory(int lineNumber){
		return selenium.getEval(getLocatorForBillingCategory(lineNumber)+".innerHTML").trim();
	}

	public String getTimeSheetCellValue(int row, int column){
		return selenium.getEval(
				TimeSheetPageLocators.INPUT_VALUE_GETEVAL_PREFIX.getLocator()
						+ row+ "_"+ column
						+ TimeSheetPageLocators.INPUT_VALUE_GETEVAL_POSTFIX.getLocator());
	}

	/**
	 * Starts from 0 to n-1
	 * @param lineNumber
	 * @return
	 */
	public TimesheetValues getTimesheetValues(int lineNumber){
		double _sundaytime, _mondayTime, _tuesdayTime, _wednesdayTime, _thursdayTime, _fridayTime, _saturdayTime;
		_sundaytime = new Double(getTimeSheetCellValue(lineNumber,0));
		_mondayTime = new Double(getTimeSheetCellValue(lineNumber,1));
		_tuesdayTime = new Double(getTimeSheetCellValue(lineNumber,2));
		_wednesdayTime = new Double(getTimeSheetCellValue(lineNumber,3));
		_thursdayTime = new Double(getTimeSheetCellValue(lineNumber,4));
		_fridayTime = new Double(getTimeSheetCellValue(lineNumber,5));
		_saturdayTime = new Double(getTimeSheetCellValue(lineNumber,6));
		TimesheetValues result = new TimesheetValues(_sundaytime, _mondayTime, _tuesdayTime, _wednesdayTime, _thursdayTime, _fridayTime, _saturdayTime);
		return result;
	}

	/**
	 * Starts from 0 to n-1
	 * @param lineNumber
	 * @return
	 */
	public TimesheetLine getTimesheetLine(int lineNumber){
		TimesheetLine result = new TimesheetLine();
		result.setActivityOptionLabel(getActivity(lineNumber));
		result.setBillingCategoryOptionLabel(getBillingCategory(lineNumber));
		result.setTimesheetValues(getTimesheetValues(lineNumber));
		result.setWorkItem(getWorkName(lineNumber));
		return result;
	}

//	public int getTimesheetLineNumber(){
//		int lineNumber = 0;
//
//		try{
//			for(;;lineNumber++){
//				getActivity(lineNumber);
//			}
//		} catch(SeleniumException se){
//			return lineNumber;
//		}
//	}

	public TimeSheetTable getTimeSheetTable(){
		int lineNumber = getTimesheetLinesNumber();
		TimeSheetTable result = new TimeSheetTable();

		for(int i=0;i<lineNumber;i++){
			result.addTimesheetLine(getTimesheetLine(i));
		}
		return result;
	}

	public String getActivity(int lineNumber){
		return selenium.getEval(getLocatorForActivity(lineNumber)+".innerHTML").trim();
	}

	public String getWorkName(int lineNumber){
		return SeleniumDriverSingleton
				.getDriver()
				.getEval(SubmittedTimesheetPageLocators.WORK_NAME_GETEVAL_PREFIX.getLocator()
								+ lineNumber
								+ SubmittedTimesheetPageLocators.WORK_NAME_GETEVAL_POSTFIX.getLocator()).trim();
	}

	public Timesheet getTimesheet(){
		Timesheet timesheet = new Timesheet();
		timesheet.setTimesheetTable(getTimeSheetTable());
		timesheet.setStartDate(getStartDate());
		return timesheet;
	}

	public String getWorkTimeId(int lineNumber){
		return SubmittedTimesheetPageLocators.WORK_NAME_IDPREFIX.getLocator()+lineNumber;
	}

	public int getApprovedTimesheetLineNumber(){
		return Integer.parseInt(selenium.getEval("window.dojo.query('[tooltip*=Approved]').length"));
	}

	public int getRejectedTimesheetLineNumber(){
		return Integer.parseInt(selenium.getEval("window.dojo.query('[tooltip*=Rejected]').length"));
	}

}
