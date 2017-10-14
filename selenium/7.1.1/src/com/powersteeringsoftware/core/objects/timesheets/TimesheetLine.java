package com.powersteeringsoftware.core.objects.timesheets;

public class TimesheetLine {

	String workItem;
	TimesheetValues timesheetValues;
	String activityOptionLabel = "";
	String billingCategoryOptionLabel = "";

	public TimesheetLine(){
	}

	public TimesheetLine(String _workItem, String _activityOption){
		if((_workItem==null)||(_activityOption==null))
			throw new IllegalArgumentException("Neither work item nor activity can be null.");

		workItem = _workItem;
		activityOptionLabel = _activityOption;
		activityOptionLabel = "";
		billingCategoryOptionLabel="";

	}

	public String getWorkItem() {
		return workItem;
	}

	public void setWorkItem(String _workItem) {
		this.workItem = _workItem;
	}


	public TimesheetValues getTimesheetValues() {
		return timesheetValues;
	}


	public void setTimesheetValues(TimesheetValues timesheetValues) {
		this.timesheetValues = timesheetValues;
	}


	public String getActivityOptionLabel() {
		return activityOptionLabel;
	}


	public void setActivityOptionLabel(String _activityItem) {
		this.activityOptionLabel = _activityItem;
	}


	public String getBillingCategoryOptionLabel() {
		return billingCategoryOptionLabel;
	}


	public void setBillingCategoryOptionLabel(String billingCategoryItem) {
		this.billingCategoryOptionLabel = billingCategoryItem;
	}


	public int hashCode(){
		int result = 31;

		result = result*31+workItem.hashCode();
		result = result*31+timesheetValues.hashCode();
		result = result*31+activityOptionLabel.hashCode();
		result = result*31+billingCategoryOptionLabel.hashCode();

		return result ;
	}

	public boolean equals(Object obj){
		if (this == obj) {
		    return true;
		}
		if(obj==null){
			return false;
		}
		if(!(obj instanceof TimesheetLine)){
			return false;
		}
		if(hashCode()!=obj.hashCode()){
			return false;
		}

		TimesheetLine _timesheetLine = (TimesheetLine)obj;

		if(!workItem.equals(_timesheetLine.getWorkItem())){
			return false;
		}

		if(!timesheetValues.equals(_timesheetLine.getTimesheetValues())){
			return false;
		}

		if(!activityOptionLabel.equals(_timesheetLine.getActivityOptionLabel())){
			return false;
		}

		if(!billingCategoryOptionLabel.equals(_timesheetLine.getBillingCategoryOptionLabel())){
			return false;
		}

		return true;
	}

	public double getTotal(){
		return getTimesheetValues().getTotal();
	}
}
