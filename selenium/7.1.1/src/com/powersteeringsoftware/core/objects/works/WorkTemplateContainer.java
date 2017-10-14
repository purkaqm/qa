package com.powersteeringsoftware.core.objects.works;

public class WorkTemplateContainer {

	private String name;
	private String id;
	private String objective;
	private boolean isShowSelectChildrenStep;
	private boolean isRequireGateEndDates;

	//TODO custom fields
	//TODO team

	public WorkTemplateContainer(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public Boolean getIsShowSelectChildrenStep() {
		return isShowSelectChildrenStep;
	}

	public void setIsShowSelectChildrenStep(boolean isShowSelectChildrenStep) {
		this.isShowSelectChildrenStep = isShowSelectChildrenStep;
	}

	public Boolean getIsRequireGateEndDates() {
		return isRequireGateEndDates;
	}

	public void setIsRequireGateEndDates(boolean isRequireGateEndDates) {
		this.isRequireGateEndDates = isRequireGateEndDates;
	}

}
