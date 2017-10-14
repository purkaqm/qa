package com.powersteeringsoftware.core.objects.measures.collection_method;

import com.powersteeringsoftware.core.objects.measures.collection_method.scheduler.AbstractSchedulerType;
import com.powersteeringsoftware.core.objects.measures.collection_method.scheduler.Never;

public class FormulaCollectionMethod extends AbstractCollectionMethod {

	private AbstractSchedulerType historyScheduler = new Never();
	private AbstractSchedulerType testScheduler = new Never();
	private String formula = "";

	public FormulaCollectionMethod(){
	}

	public AbstractSchedulerType getHistoryScheduler() {
		return historyScheduler;
	}

	public void setHistoryScheduler(AbstractSchedulerType historyScheduler) {
		this.historyScheduler = historyScheduler;
	}

	public AbstractSchedulerType getTestScheduler() {
		return testScheduler;
	}

	public void setTestScheduler(AbstractSchedulerType testScheduler) {
		this.testScheduler = testScheduler;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}



}
