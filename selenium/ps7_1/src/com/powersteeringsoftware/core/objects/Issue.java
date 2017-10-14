package com.powersteeringsoftware.core.objects;

import java.util.LinkedList;

public class Issue {
	public static final int DEFAULT_PRIORITY = 3;
	private int priority = DEFAULT_PRIORITY;
	private String recommendedAction;
	private LinkedList<Task> tasksList;

	public Issue(int priority){
		this.priority = priority;
		this.recommendedAction = null;
		this.tasksList = null;
	}

	public Issue(){
		this(DEFAULT_PRIORITY);
	}

	public int getPriority() {
		return this.priority;
	}

	public String getRecommendedAction() {
		return this.recommendedAction;
	}

	public LinkedList<Task> getTasksList() {
		return this.tasksList;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setRecommendedAction(String recommendedAction) {
		this.recommendedAction = recommendedAction;
	}

	public void addTask(Task task) {
		if (null == tasksList) tasksList = new LinkedList<Task>();
		tasksList.add(task);
	}

}
