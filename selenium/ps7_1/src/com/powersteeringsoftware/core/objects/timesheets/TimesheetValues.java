package com.powersteeringsoftware.core.objects.timesheets;

import java.util.HashMap;
import com.powersteeringsoftware.core.enums.DayOfWeek;

public class TimesheetValues implements Cloneable{
	private HashMap<DayOfWeek, Double> weekDayValues = new HashMap<DayOfWeek, Double>();

	public TimesheetValues(){
		setTimesheet(0,0,0,0,0,0,0);
	}

	public TimesheetValues(HashMap<DayOfWeek, Double> _weekDayValues){
		this();
		weekDayValues = _weekDayValues;
	}

	public TimesheetValues(double _sundaytime, double _mondayTime, double _tuesdayTime,
			double _wednesdayTime, double _thursdayTime, double _fridayTime,
			double _saturdayTime) {
		setTimesheet(_sundaytime,_mondayTime,_tuesdayTime,_wednesdayTime,_thursdayTime,_fridayTime,_saturdayTime);
	}

	public TimesheetValues(TimesheetValues _timesheet){
		weekDayValues.put(DayOfWeek.SUNDAY, 	_timesheet.getSundayTime());
		weekDayValues.put(DayOfWeek.MONDAY, 	_timesheet.getMondayTime());
		weekDayValues.put(DayOfWeek.TUESDAY, 	_timesheet.getTuesdayTime());
		weekDayValues.put(DayOfWeek.WEDNESDAY,	_timesheet.getWednesdayTime());
		weekDayValues.put(DayOfWeek.THURSDAY, 	_timesheet.getThursdayTime());
		weekDayValues.put(DayOfWeek.FRIDAY, 	_timesheet.getFridayTime());
		weekDayValues.put(DayOfWeek.SATURDAY, 	_timesheet.getSaturdayTime());
	}

	public Object clone(){
		TimesheetValues result =
			new TimesheetValues(getSundayTime(),getMondayTime(), getTuesdayTime(),
					getWednesdayTime(), getThursdayTime(), getFridayTime(), getSaturdayTime());
		return result;
	}

	private void setDayTime(DayOfWeek _dayOfWeek, double _time){
		if(_time<0 || _time>24) throw new IllegalArgumentException("Timesheet time can't be less then 0 and more then 24");
		weekDayValues.put(_dayOfWeek, _time);
	}

	public void setTimesheet(double _sundaytime, double _mondayTime, double _tuesdayTime,
			double _wednesdayTime, double _thursdayTime, double _fridayTime,
			double _saturdayTime) {
		weekDayValues.put(DayOfWeek.SUNDAY, 	_sundaytime);
		weekDayValues.put(DayOfWeek.MONDAY, 	_mondayTime);
		weekDayValues.put(DayOfWeek.TUESDAY, 	_tuesdayTime);
		weekDayValues.put(DayOfWeek.WEDNESDAY, 	_wednesdayTime);
		weekDayValues.put(DayOfWeek.THURSDAY, 	_thursdayTime);
		weekDayValues.put(DayOfWeek.FRIDAY, 	_fridayTime);
		weekDayValues.put(DayOfWeek.SATURDAY, 	_saturdayTime);
	}

	public void setFridayTime(double _time){
		setDayTime(DayOfWeek.FRIDAY, _time);
	}

	public double getFridayTime(){
		return weekDayValues.get(DayOfWeek.FRIDAY);
	}

	public void setMondayTime(double _time){
		setDayTime(DayOfWeek.MONDAY, _time);
	}

	public double getMondayTime(){
		return weekDayValues.get(DayOfWeek.MONDAY);
	}

	public void setSaturdayTime(double _time){
		setDayTime(DayOfWeek.SATURDAY, _time);
	}

	public double getSaturdayTime(){
		return weekDayValues.get(DayOfWeek.SATURDAY);
	}

	public void setSundayTime(double _time){
		setDayTime(DayOfWeek.SUNDAY , _time);
	}

	public double getSundayTime(){
		return weekDayValues.get(DayOfWeek.SUNDAY);
	}

	public void setThursdayTime(double _time){
		setDayTime(DayOfWeek.THURSDAY, _time);
	}

	public double getThursdayTime(){
		return weekDayValues.get(DayOfWeek.THURSDAY);
	}

	public void setTuesdayTime(double _time){
		setDayTime(DayOfWeek.TUESDAY, _time);
	}

	public double getTuesdayTime(){
		return weekDayValues.get(DayOfWeek.TUESDAY);
	}

	public void setWednesdayTime(double _time){
		setDayTime(DayOfWeek.WEDNESDAY, _time);
	}

	public double getWednesdayTime(){
		return weekDayValues.get(DayOfWeek.WEDNESDAY);
	}

	public int hashCode(){
		double result = 31;

		result = result*31 + getSundayTime();
		result = result*31 + getMondayTime();
		result = result*31 + getTuesdayTime();
		result = result*31 + getWednesdayTime();
		result = result*31 + getThursdayTime();
		result = result*31 + getFridayTime();
		result = result*31 + getSaturdayTime();

		return (int)result;
	}

	public boolean equals(Object obj){
		if (this == obj) {
		    return true;
		}

		if(obj==null){
			return false;
		}

		if(!(obj instanceof TimesheetValues)){
			return false;
		}

		if(hashCode()!=obj.hashCode()){
			return false;
		}

		TimesheetValues _timesheetValues = (TimesheetValues) obj;

		if(getSundayTime() != _timesheetValues.getSundayTime()){
			return false;
		}
		if(getMondayTime() != _timesheetValues.getMondayTime()){
			return false;
		}
		if(getTuesdayTime() != _timesheetValues.getTuesdayTime()){
			return false;
		}
		if(getWednesdayTime() != _timesheetValues.getWednesdayTime()){
			return false;
		}
		if(getThursdayTime() != _timesheetValues.getThursdayTime()){
			return false;
		}
		if (getFridayTime() != _timesheetValues.getFridayTime()){
			return false;
		}
		if(getSaturdayTime() != _timesheetValues.getSaturdayTime()){
			return false;
		}
		return true;
	}

	public String toString(){
		return weekDayValues.toString();
	}

	public double getTotal(){
		return getSundayTime() + getMondayTime() + getThursdayTime()
				+ getWednesdayTime() + getThursdayTime() + getFridayTime()
				+ getSaturdayTime();
	}

}