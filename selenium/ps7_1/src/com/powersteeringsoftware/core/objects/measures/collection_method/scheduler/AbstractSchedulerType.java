package com.powersteeringsoftware.core.objects.measures.collection_method.scheduler;

public abstract class AbstractSchedulerType {

	private SchedulerTypesEnum schedulerValue;

	public AbstractSchedulerType(SchedulerTypesEnum _schedulerValue){
		schedulerValue = _schedulerValue;
	}

	public SchedulerTypesEnum getSchedulerValue() {
		return schedulerValue;
	}

	public void setSchedulerValue(SchedulerTypesEnum _schedulerValue) {
		this.schedulerValue = _schedulerValue;
	}

}
