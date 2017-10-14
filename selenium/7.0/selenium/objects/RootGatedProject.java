package selenium.objects;

public class RootGatedProject {
	public enum STATUS_REPORTING_FREQUENCY {
		NO_FREQUENCY,
		WEEKLY,
		BIWEEKLY,
		MONTHLY,
		QUARTERLY
	}

	public enum PRIORITY {
		ONE,
		TWO,
		THREE,
		FOUR,
		FIVE
	}

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
	private String processName;
	private String objective;

	private Boolean isUseStatusReporting;
	private STATUS_REPORTING_FREQUENCY statusReportingFrequency;

	private Boolean isSetPriority;
	private PRIORITY priority;

	private Boolean isInheritPermissions;
	private Boolean isInheritControls;
  	private Boolean isWebFolder;
  	private Boolean isControlCost;

  	private Boolean isSetDateConstraint;
  	private DATE_CONSTRAINTS dateConstraint;
  	private String dateConstraintStartValue;
  	private String dateConstraintEndValue;

	public RootGatedProject(String name, String processName){
		this.name = name;
		this.processName = processName;

		this.isUseStatusReporting = false;
		this.isSetPriority = false;
		this.isSetDateConstraint = false;

		this.isInheritPermissions = null;;
		this.isInheritControls = null;
		this.isWebFolder = null;
		this.isControlCost = null;
	}

	public String getName() {
		return name;
	}

	public String getProcessName() {
		return processName;
	}

	public void setStatusReporting(STATUS_REPORTING_FREQUENCY statusReportingFrequency){
		this.isUseStatusReporting = true;
		this.statusReportingFrequency = statusReportingFrequency;
	}

	public Boolean isUseStatusReporting() {
		return isUseStatusReporting;
	}

	public STATUS_REPORTING_FREQUENCY getStatusReportingFrequency() {
		return statusReportingFrequency;
	}

	public void setPriority(PRIORITY priority){
		this.isSetPriority = true;
		this.priority = priority;
	}

	public Boolean isSetPriority() {
		return isSetPriority;
	}

	public PRIORITY getPriority() {
		return priority;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public Boolean isInheritPermissions() {
		return isInheritPermissions;
	}

	public void setInheritPermissions(Boolean isInheritPermissions) {
		this.isInheritPermissions = isInheritPermissions;
	}

	public Boolean isInheritControls() {
		return isInheritControls;
	}

	public void setInheritControls(Boolean isInheritControls) {
		this.isInheritControls = isInheritControls;
	}

	public Boolean isWebFolder() {
		return isWebFolder;
	}

	public void setWebFolder(Boolean isWebFolder) {
		this.isWebFolder = isWebFolder;
	}

	public Boolean isControlCost() {
		return isControlCost;
	}

	public void setControlCost(Boolean isControlCost) {
		this.isControlCost = isControlCost;
	}

	public void setDateConstraint(DATE_CONSTRAINTS dateConstraint, String startValue, String endValue){
		this.isSetDateConstraint = true;
		this.dateConstraint = dateConstraint;
		this.dateConstraintStartValue = startValue;
		this.dateConstraintEndValue = endValue;
	}

	public Boolean getIsSetDateConstraint() {
		return isSetDateConstraint;
	}

	public DATE_CONSTRAINTS getDateConstraint() {
		return dateConstraint;
	}

	public String getDateConstraintStartValue() {
		return dateConstraintStartValue;
	}

	public String getDateConstraintEndValue() {
		return dateConstraintEndValue;
	}

}
