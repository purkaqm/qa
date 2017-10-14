package com.powersteeringsoftware.core.objects.timesheets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timesheet {

	private TimeSheetTable timesheetTable;
	private String startDate;

	public Timesheet(){
		timesheetTable = new TimeSheetTable();
		setStartDate("");
	}

	public Timesheet(TimeSheetTable _table, String _startDate){
		setTimesheetTable(_table);
		setStartDate(_startDate);

	}

	public TimeSheetTable getTimesheetTable() {
		return timesheetTable;
	}

	public void setTimesheetTable(TimeSheetTable timesheetTable) {
		this.timesheetTable = (null==timesheetTable)?new TimeSheetTable():timesheetTable;
	}

	public String getStartDate() {
		return (null==startDate)?"":startDate;
	}

	public void setStartDate(String _startDate) {
		this.startDate = (null==_startDate)?"":_startDate;
	}


	public int hashCode(){
		int result= this.timesheetTable.hashCode();
		result = result*31 + startDate.hashCode();
		return result;
	}

	public boolean equals(Object obj){
		if(null==obj) return false;

		if(!(obj instanceof Timesheet)) return false;

		Timesheet timesheet = (Timesheet)obj;

		if(!(timesheet.getTimesheetTable().equals(this.getTimesheetTable()))) return false;

		if(!startDate.equals(timesheet.getStartDate())) return false;

		return true;
	}

	public Date getStartDateAsDate(String format){
		try{
			Date date = new SimpleDateFormat(format).parse(startDate);
			return date;
		} catch (ParseException parseException){
			throw new IllegalMonitorStateException("Wrong parametries for parsing startDate:"+startDate);
		}
	}

}

