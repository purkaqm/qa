package com.powersteeringsoftware.core.objects.measures.indicator_type;

public class VarianceIndicatorType implements IIndicatorType{

	public VarianceIndicatorType(){
	}

	private String redMessage1 = "";
	private String redMessage2 = "";
	private String yellowMessage1 = "";
	private String yellowMessage2 = "";
	private String redMessage = "";
	private double thresholdMessage1;
	private double thresholdMessage2;
	private double thresholdMessage3;
	private double thresholdMessage4;

	public String getRedMessage1() {
		return redMessage1;
	}
	public void setRedMessage1(String redMessage1) {
		this.redMessage1 = redMessage1;
	}
	public String getRedMessage2() {
		return redMessage2;
	}
	public void setRedMessage2(String redMessage2) {
		this.redMessage2 = redMessage2;
	}
	public String getYellowMessage1() {
		return yellowMessage1;
	}
	public void setYellowMessage1(String yellowMessage1) {
		this.yellowMessage1 = yellowMessage1;
	}
	public String getYellowMessage2() {
		return yellowMessage2;
	}
	public void setYellowMessage2(String yellowMessage2) {
		this.yellowMessage2 = yellowMessage2;
	}
	public String getRedMessage() {
		return redMessage;
	}
	public void setRedMessage(String redMessage) {
		this.redMessage = redMessage;
	}
	public Double getThresholdMessage1() {
		return thresholdMessage1;
	}
	public void setThresholdMessage1(Double thresholdMessage1) {
		this.thresholdMessage1 = thresholdMessage1;
	}
	public Double getThresholdMessage2() {
		return thresholdMessage2;
	}
	public void setThresholdMessage2(Double thresholdMessage2) {
		this.thresholdMessage2 = thresholdMessage2;
	}
	public Double getThresholdMessage3() {
		return thresholdMessage3;
	}
	public void setThresholdMessage3(Double thresholdMessage3) {
		this.thresholdMessage3 = thresholdMessage3;
	}
	public Double getThresholdMessage4() {
		return thresholdMessage4;
	}
	public void setThresholdMessage4(Double thresholdMessage4) {
		this.thresholdMessage4 = thresholdMessage4;
	}

}
