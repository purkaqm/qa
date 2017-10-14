package selenium.objects;

import java.util.LinkedList;

public class Issue {

	private int priority;
	private String recommendedAction;
	private LinkedList<Task> tasksList;

	public Issue(int priority){
		this.priority = priority;
		this.recommendedAction = null;
		this.tasksList = null;
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
