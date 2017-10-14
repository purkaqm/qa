package selenium.formholders;

import selenium.models.WorkTemplatesPage.DATE_CONSTRAINTS;

public class WorkTemplateRootGatedProjectHolder {
	private String workName;
	private String projectID;
	private Boolean useStatusReporting;
	private String statusReportingFrequency;
	private String priority;
	private String percentComplete;
	private String process;
	private String objectives;
	private Boolean inheritPermissions;
	private Boolean inheritControls;
	private Boolean webFolder;
	private Boolean controlCost;
	private Boolean manualSchedule;
	private DATE_CONSTRAINTS dateConstraint;
	private String dateConstraintStart;
	private String dateConstraintEnd;
	private String dateConstraintDuration;

	public WorkTemplateRootGatedProjectHolder(String workName, String objectives, String process){
		this.workName = workName;
		useStatusReporting = false;
		statusReportingFrequency = null;
		projectID = null;
		useStatusReporting = null;
		priority = null;
		percentComplete = null;
		this.process = process;
		this.objectives = objectives;
		inheritPermissions = null;
		inheritControls = null;
		webFolder = null;
		controlCost = null;
		manualSchedule = null;
		dateConstraint = DATE_CONSTRAINTS.NO_CHANGE;
		dateConstraintStart = null;
		dateConstraintEnd = null;
		dateConstraintDuration = null;
	}

	public String getWorkName() {
		return workName;
	}

	public void setWorkName(String workName) {
		this.workName = workName;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	public Boolean getUseStatusReporting() {
		return useStatusReporting;
	}

	public String getStatusReportingFrequency() {
		return statusReportingFrequency;
	}

	public void setStatusReporting(String statusReportingFrequency) {
		this.useStatusReporting = true;
		this.statusReportingFrequency = statusReportingFrequency;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getPercentComplete() {
		return percentComplete;
	}

	public void setPercentComplete(String percentComplete) {
		this.percentComplete = percentComplete;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getObjectives() {
		return objectives;
	}

	public void setObjectives(String objectives) {
		this.objectives = objectives;
	}

	public Boolean getInheritPermissions() {
		return inheritPermissions;
	}

	public void setInheritPermissions(Boolean inheritPermissions) {
		this.inheritPermissions = inheritPermissions;
	}

	public Boolean getInheritControls() {
		return inheritControls;
	}

	public void setInheritControls(Boolean inheritControls) {
		this.inheritControls = inheritControls;
	}

	public Boolean getWebFolder() {
		return webFolder;
	}

	public void setWebFolder(Boolean webFolder) {
		this.webFolder = webFolder;
	}

	public Boolean getControlCost() {
		return controlCost;
	}

	public void setControlCost(Boolean controlCost) {
		this.controlCost = controlCost;
	}

	public Boolean getManualSchedule() {
		return manualSchedule;
	}

	public void setManualSchedule(Boolean manualSchedule) {
		this.manualSchedule = manualSchedule;
	}

	public void setDateConstraint(DATE_CONSTRAINTS dateConstraint, String dateConstraintStart, String dateConstraintEnd, String dateConstraintsDuration){
		this.dateConstraint = dateConstraint;
		this.dateConstraintStart = dateConstraintStart;
		this.dateConstraintEnd = dateConstraintEnd;
		this.dateConstraintDuration = dateConstraintsDuration;
	}

	public DATE_CONSTRAINTS getDateConstraint() {
		return dateConstraint;
	}

	public String getDateConstraintStart() {
		return dateConstraintStart;
	}

	public String getDateConstraintsEnd() {
		return dateConstraintEnd;
	}

	public String getDateConstraintsDuration() {
		return dateConstraintDuration;
	}


}
