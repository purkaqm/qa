package com.powersteeringsoftware.core.objects.measures.collection_method.scheduler;

public class Weekly extends AbstractSchedulerType {

	private int every = 1;
	private boolean sunday;
	private boolean monday;
	private boolean tuesday;
	private boolean wednesday;
	private boolean thursday;
	private boolean friday;
	private boolean saturday;
	private String timeOfDay = "12:00 AM";

	Weekly(){
		super(SchedulerTypesEnum.WEEKLY);
	}

	public boolean isAll(){
		return sunday && monday && tuesday && wednesday && thursday && friday && saturday;
	}

	public void setAll(){
		setFriday(true);
		setMonday(true);
		setSaturday(true);
		setSunday(true);
		setThursday(true);
		setTuesday(true);
		setWednesday(true);
	}

	public int getEvery() {
		return every;
	}

	public void setEvery(int every) {
		this.every = every;
	}

	public boolean isSunday() {
		return sunday;
	}

	public void setSunday(boolean sunday) {
		this.sunday = sunday;
	}

	public boolean isMonday() {
		return monday;
	}

	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	public boolean isTuesday() {
		return tuesday;
	}

	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}

	public boolean isWednesday() {
		return wednesday;
	}

	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}

	public boolean isThursday() {
		return thursday;
	}

	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}

	public boolean isFriday() {
		return friday;
	}

	public void setFriday(boolean friday) {
		this.friday = friday;
	}

	public boolean isSaturday() {
		return saturday;
	}

	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}

	public String getTimeOfDay() {
		return timeOfDay;
	}

	public void setTimeOfDay(String timeOfDay) {
		this.timeOfDay = timeOfDay;
	}


}
