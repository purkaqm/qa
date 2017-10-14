package com.powersteeringsoftware.core.objects.measures.collection_method.scheduler;

public class Daily extends AbstractSchedulerType {

	private int every = 1;
	private String timeOfDay = "12:00 AM";

	public Daily(){
		super(SchedulerTypesEnum.DAILY);
	}

	public int getEvery() {
		return every;
	}

	public void setEvery(int every) {
		this.every = every;
	}

	public String getTimeOfDay() {
		return timeOfDay;
	}

	public void setTimeOfDay(String timeOfDay) {
		this.timeOfDay = timeOfDay;
	}



}
