package com.powersteeringsoftware.core.objects.measures.indicator_type;

import java.util.Date;

public class GoalIndicatorType implements IIndicatorType{


	public GoalIndicatorType(){
	}

	private Date startDate = new Date();
	private Date targetDate = new Date();
	private int  targetValue;
	private String redMessage = "";
	private String yellowMessage = "";
	private String greenMessage = "";
	private double threshold1;
	private double threshold2;
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}
	public int getTargetValue() {
		return targetValue;
	}
	public void setTargetValue(int targetValue) {
		this.targetValue = targetValue;
	}
	public String getRedMessage() {
		return redMessage;
	}
	public void setRedMessage(String redMessage) {
		this.redMessage = redMessage;
	}
	public String getYellowMessage() {
		return yellowMessage;
	}
	public void setYellowMessage(String yellowMessage) {
		this.yellowMessage = yellowMessage;
	}
	public String getGreenMessage() {
		return greenMessage;
	}
	public void setGreenMessage(String greenMessage) {
		this.greenMessage = greenMessage;
	}
	public double getThreshold1() {
		return threshold1;
	}
	public void setThreshold1(double threshold1) {
		this.threshold1 = threshold1;
	}
	public double getThreshold2() {
		return threshold2;
	}
	public void setThreshold2(double threshold2) {
		this.threshold2 = threshold2;
	}

}

