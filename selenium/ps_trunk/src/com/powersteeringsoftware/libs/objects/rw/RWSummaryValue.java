package com.powersteeringsoftware.libs.objects.rw;

public enum RWSummaryValue {
	COUNT("Count", 2),
	DISTCOUNT("Distinct Count", 3),
	FIRST("First", 4),
	MIN("Min", 5),
	MAX("Max", 6),
	SUM("Sum", 7),
	AVG("Avg", 8),
	STD_DEV("Standard Deviation", 9),
	VARIANCE("Variance", 10),
	NONE("None", 11)	;
	
	RWSummaryValue(String title, int pos){
		this.title = title;
		this.pos = pos;
	}
	
	public String getTitle(){
		return title;
	}
	
	public int getPos(){
		return pos;
	}
	
	public void setDisplayLabel(boolean disp){
		this.showDispLabel = disp;
	}
	
	public boolean isDisplayLabel(){
		return showDispLabel;
	}
	
	
	private String title; 
	private int pos;
	private boolean showDispLabel;
}// enum RWSummaryValue 
