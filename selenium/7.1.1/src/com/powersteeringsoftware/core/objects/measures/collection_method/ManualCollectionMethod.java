package com.powersteeringsoftware.core.objects.measures.collection_method;

import com.powersteeringsoftware.core.objects.measures.collection_method.scheduler.AbstractSchedulerType;
import com.powersteeringsoftware.core.objects.measures.collection_method.scheduler.Never;

public class ManualCollectionMethod extends AbstractCollectionMethod{

	public ManualCollectionMethod(){
	}

	private AbstractSchedulerType reminderScheduler = new Never();

	public AbstractSchedulerType getReminderScheduler() {
		return reminderScheduler;
	}

	public void setReminderScheduler(AbstractSchedulerType reminderScheduler) {
		this.reminderScheduler = reminderScheduler;
	}
}
