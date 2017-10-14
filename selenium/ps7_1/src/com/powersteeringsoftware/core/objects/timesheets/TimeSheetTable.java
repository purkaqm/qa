package com.powersteeringsoftware.core.objects.timesheets;

import java.util.ArrayList;

import com.powersteeringsoftware.core.adapters.timesheets.EditedableTimesheetPageAdapter;

/**
 * Set of time sheet lines on a page
 * @author selyaev_ag
 * @since 1-b11
 */
public class TimeSheetTable {
	ArrayList<TimesheetLine> timesheetLines = new ArrayList<TimesheetLine>(EditedableTimesheetPageAdapter.LINES_NUMBER_FOR_EACH_ADD);

	public TimeSheetTable(){
	}

	public void addTimesheetLine(TimesheetLine _timesheetLine){
		timesheetLines.add(_timesheetLine);
	}

	public TimesheetLine getTimesheetLineByIndex(int index){
		return timesheetLines.get(index);
	}

	public int getLength(){
		return timesheetLines.size();
	}

	public int hashCode(){
		return timesheetLines.hashCode();
	}

	public boolean equals(Object obj){
		if(null==obj) return false;

		if(!(obj instanceof TimeSheetTable)) return false;

		TimeSheetTable timesheet = (TimeSheetTable)obj;

		if(timesheet.getLength()!=this.getLength()) return false;

		for(int i=0;i<timesheet.getLength();i++){
			if(!this.getTimesheetLineByIndex(i).equals(timesheet.getTimesheetLineByIndex(i))) return false;
		}

		return true;
	}

	public String toString(){
		return timesheetLines.toString();
	}

	public double getTotal(){
		double result = 0;
		for(int i =0; i<this.getLength(); i++){
			result+=this.timesheetLines.get(i).getTotal();
		}
		return result;
	}

	public double getSundayTotal(){
		double result = 0;
		for(int i =0; i<this.getLength(); i++){
			result+=this.timesheetLines.get(i).getTimesheetValues().getSundayTime();
		}
		return result;
	}

	public double getMondayTotal(){
		double result = 0;
		for(int i =0; i<this.getLength(); i++){
			result+=this.timesheetLines.get(i).getTimesheetValues().getMondayTime();
		}
		return result;
	}

	public double getTuesdayTotal(){
		double result = 0;
		for(int i =0; i<this.getLength(); i++){
			result+=this.timesheetLines.get(i).getTimesheetValues().getTuesdayTime();
		}
		return result;
	}

	public double getWednesdayTotal(){
		double result = 0;
		for(int i =0; i<this.getLength(); i++){
			result+=this.timesheetLines.get(i).getTimesheetValues().getWednesdayTime();
		}
		return result;
	}

	public double getThursdayTotal(){
		double result = 0;
		for(int i =0; i<this.getLength(); i++){
			result+=this.timesheetLines.get(i).getTimesheetValues().getThursdayTime();
		}
		return result;
	}

	public double getFridayTotal(){
		double result = 0;
		for(int i =0; i<this.getLength(); i++){
			result+=this.timesheetLines.get(i).getTimesheetValues().getFridayTime();
		}
		return result;
	}

	public double getSaturdayTotal(){
		double result = 0;
		for(int i =0; i<this.getLength(); i++){
			result+=this.timesheetLines.get(i).getTimesheetValues().getSaturdayTime();
		}
		return result;
	}
}
