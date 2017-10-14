package selenium.formholders;

public class WorkTemplateHolder {
	String workTemplateName;
	String workTemplateID;
	String workTemplateObjective;
	Boolean showSelectChildrenStep;
	Boolean requireGateEndDates;

	public WorkTemplateHolder(String workTemplateName, String workTemplateObjective){
		this.workTemplateName = workTemplateName;
		this.workTemplateObjective = workTemplateObjective;
		workTemplateID = null;
		showSelectChildrenStep = null;
		requireGateEndDates = null;
	}

	public String getWorkTemplateName() {
		return workTemplateName;
	}

	public void setWorkTemplateName(String workTemplateName) {
		this.workTemplateName = workTemplateName;
	}

	public String getWorkTemplateID() {
		return workTemplateID;
	}

	public void setWorkTemplateID(String workTemplateID) {
		this.workTemplateID = workTemplateID;
	}

	public String getWorkTemplateObjective() {
		return workTemplateObjective;
	}

	public void setWorkTemplateObjective(String workTemplateObjective) {
		this.workTemplateObjective = workTemplateObjective;
	}

	public Boolean getShowSelectChildrenStep() {
		return showSelectChildrenStep;
	}

	public void setShowSelectChildrenStep(Boolean showSelectChildrenStep) {
		this.showSelectChildrenStep = showSelectChildrenStep;
	}

	public Boolean getRequireGateEndDates() {
		return requireGateEndDates;
	}

	public void setRequireGateEndDates(Boolean requireGateEndDates) {
		this.requireGateEndDates = requireGateEndDates;
	}

}
