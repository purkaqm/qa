package selenium.objects;

public class WorkTemplate {
	public enum DATE_CONSTRAINTS {
		AS_SOON_AS_POSSIBLE,
		AS_LATE_AS_POSSIBLE,
		MUST_START_ON,
		MUST_FINISH_ON,
		START_NO_EARLIER_THEN,
		START_NO_LATER_THEN,
		FINISH_NO_EARLIER_THEN,
		FINISH_NO_LATER_THEN,
		NO_CHANGE
	}

	private String name;
	private String id;
	private String objective;
	private Boolean isShowSelectChildrenStep;
	private Boolean isRequireGateEndDates;

	//TODO custom fields
	//TODO team

	public WorkTemplate(String name){
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

	public void setIsShowSelectChildrenStep(Boolean isShowSelectChildrenStep) {
		this.isShowSelectChildrenStep = isShowSelectChildrenStep;
	}

	public Boolean getIsRequireGateEndDates() {
		return isRequireGateEndDates;
	}

	public void setIsRequireGateEndDates(Boolean isRequireGateEndDates) {
		this.isRequireGateEndDates = isRequireGateEndDates;
	}

}
