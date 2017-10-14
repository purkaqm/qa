package com.powersteeringsoftware.core.objects.works;

import org.testng.Assert;
import com.powersteeringsoftware.core.enums.SchedulerConstraintTypes;

public class WorkTemplateFramework {

	public final SchedulerConstraintTypes DEFAULT_SCHEDULE_CONSTRAINT_TYPE = SchedulerConstraintTypes.AS_SOON_AS_POSSIBLE;

	public enum StatusReportingFrequency {
		NO_FREQUENCY,
		WEEKLY,
		BIWEEKLY,
		MONTHLY,
		QUARTERLY
	}

	public enum Priority {
		ONE,
		TWO,
		THREE,
		FOUR,
		FIVE
	}

	private String name;
	private String processName;
	private String objective;

	private boolean isUseStatusReporting;
	private StatusReportingFrequency statusReportingFrequency;

	private boolean isSetPriority;
	private Priority priority;

	private boolean isInheritPermissions;
	private boolean isInheritControls;
  	private boolean isWebFolder;
  	private boolean isControlCost;
  	private boolean isManualScheduled;

  	private boolean isSetDateConstraint;
  	private SchedulerConstraintTypes dateConstraint;
  	private String dateConstraintStartValue;
  	private String dateConstraintEndValue;

	public WorkTemplateFramework(String name, String processName){
		this.name = (name==null)?"":name;
		this.processName = (processName==null)?"":processName;
		objective ="";

		this.isUseStatusReporting = false;
		this.isSetPriority = false;
		this.isSetDateConstraint = false;

		this.isInheritPermissions = false;;
		this.isInheritControls = false;
		this.isWebFolder = false;
		this.isControlCost = false;

		setDateConstraintASAP();

	}

	public String getName() {
		return name;
	}

	public String getProcessName() {
		return (null==processName)?"":processName;
	}

	public void setStatusReporting(StatusReportingFrequency statusReportingFrequency){
		this.isUseStatusReporting = true;
		this.statusReportingFrequency = statusReportingFrequency;
	}

	public boolean isUseStatusReporting() {
		return isUseStatusReporting;
	}

	public StatusReportingFrequency getStatusReportingFrequency() {
		return statusReportingFrequency;
	}

	public void setPriority(Priority priority){
		this.isSetPriority = true;
		this.priority = priority;
	}

	public boolean isSetPriority() {
		return isSetPriority;
	}

	public Priority getPriority() {
		return priority;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String _objective) {
		this.objective = (_objective==null)?"":_objective;
	}

	public boolean isInheritPermissions() {
		return isInheritPermissions;
	}

	public void setInheritPermissions(boolean isInheritPermissions) {
		this.isInheritPermissions = isInheritPermissions;
	}

	public boolean isInheritControls() {
		return isInheritControls;
	}

	public void setInheritControls(boolean isInheritControls) {
		this.isInheritControls = isInheritControls;
	}

	public boolean isWebFolder() {
		return isWebFolder;
	}

	public void setWebFolder(boolean isWebFolder) {
		this.isWebFolder = isWebFolder;
	}

	public boolean isControlCost() {
		return isControlCost;
	}

	public void setControlCost(boolean isControlCost) {
		this.isControlCost = isControlCost;
	}

	private void setDateConstraint(SchedulerConstraintTypes _dateConstraint, String _startValue, String _endValue){
		this.isSetDateConstraint = true;
		this.dateConstraint = _dateConstraint;
		this.dateConstraintStartValue = (null==_startValue)?"":_startValue;
		this.dateConstraintEndValue = (null==_endValue)?"":_endValue;
	}

	public void setDateConstraintASAP(){
		setDateConstraint(SchedulerConstraintTypes.AS_SOON_AS_POSSIBLE,"","");
	}

	public void setDateConstraintALAP(){
		setDateConstraint(SchedulerConstraintTypes.AS_LATE_AS_POSSIBLE,"","");
	}

	public void setDateConstraintFD(String _startDate, String _endDate){
		setDateConstraint(SchedulerConstraintTypes.FIXED_DATES, _startDate, _endDate);
		setManualScheduled(true);
		setInheritControls(false);
		setInheritPermissions(false);
		setInheritControls(false);
		setWebFolder(false);
	}

	public void setDateConstraintSNET(String _startDate){
		setDateConstraint(SchedulerConstraintTypes.START_NO_EARLIER_THEN, _startDate, "");
	}
	public void setDateConstraintSNLT(String _endDate){
		setDateConstraint(SchedulerConstraintTypes.START_NO_LATER_THEN, "", _endDate);
	}

	public void setDateConstraintFNLT(String _endDate){
		setDateConstraint(SchedulerConstraintTypes.FINISH_NO_LATER_THEN, "", _endDate);
	}

	public void setDateConstraintFNET(String _startDate){
		setDateConstraint(SchedulerConstraintTypes.FINISH_NO_EARLIER_THEN, _startDate, "");
	}

	public void setDateConstraintMFO(String _endDate){
		setDateConstraint(SchedulerConstraintTypes.MUST_FINISH_ON, "", _endDate);
	}

	public void setDateConstraintMSO(String _startDate){
		setDateConstraint(SchedulerConstraintTypes.MUST_START_ON,_startDate,"");
	}

	public boolean getIsSetDateConstraint() {
		return isSetDateConstraint;
	}

	public SchedulerConstraintTypes getScheduleConstraint() {
		return dateConstraint;
	}

	public String getDateConstraintStartValue() {
		return (null==dateConstraintStartValue)?"":dateConstraintStartValue;
	}

	public String getDateConstraintEndValue() {
		return (null==dateConstraintEndValue)?"":dateConstraintEndValue;
	}

	public boolean isDateConstraintEndNeeded(){
		if(isFD())		return true;
		if(isMFO())		return true;
		if(isFNET())	return true;
		if(isFNLT())	return true;

		if(isASAP())	return false;
		if(isALAP())	return false;
		if(isMSO())		return false;
		if(isSNET())	return false;
		if(isSNLT())	return false;

		Assert.fail("Wrong Date Constraint type is used.");

		return false;
	}

	public boolean isDateConstraintStartNeeded(){
		if(isMSO())		return true;
		if(isSNET())	return true;
		if(isSNLT())	return true;
		if(isFD())		return true;

		if(isASAP())	return false;
		if(isALAP())	return false;
		if(isMFO())		return false;
		if(isFNET())	return false;
		if(isFNLT())	return false;

		Assert.fail("Wrong Date Constraint type is used.");
		return false;
	}

	public boolean isASAP(){
		if(dateConstraint == SchedulerConstraintTypes.AS_SOON_AS_POSSIBLE){
			return true;
		}
		return false;
	}

	public boolean isALAP(){
		if(dateConstraint == SchedulerConstraintTypes.AS_LATE_AS_POSSIBLE){
			return true;
		}
		return false;
	}

	public boolean isFD(){
		if(dateConstraint == SchedulerConstraintTypes.FIXED_DATES){
			return true;
		}
		return false;
	}

	public boolean isFNLT(){
		if(dateConstraint == SchedulerConstraintTypes.FINISH_NO_LATER_THEN){
			return true;
		}
		return false;
	}

	public boolean isFNET(){
		if(dateConstraint == SchedulerConstraintTypes.FINISH_NO_EARLIER_THEN){
			return true;
		}
		return false;
	}

	public boolean isSNLT(){
		if(dateConstraint == SchedulerConstraintTypes.START_NO_LATER_THEN){
			return true;
		}
		return false;
	}

	public boolean isSNET(){
		if(dateConstraint == SchedulerConstraintTypes.START_NO_EARLIER_THEN){
			return true;
		}
		return false;
	}

	public boolean isMSO(){
		if(dateConstraint == SchedulerConstraintTypes.MUST_START_ON){
			return true;
		}
		return false;
	}

	public boolean isMFO(){
		if(dateConstraint == SchedulerConstraintTypes.MUST_FINISH_ON){
			return true;
		}
		return false;
	}

	public boolean isManualScheduled() {
		return isManualScheduled;
	}

	public void setManualScheduled(boolean _isManualScheduled) {
		this.isManualScheduled = _isManualScheduled;
	}

}
