package com.powersteeringsoftware.core.objects.works;

import java.util.LinkedList;
import com.powersteeringsoftware.core.enums.PSObjectTypes;
import com.powersteeringsoftware.core.objects.PSObject;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.TimeStampName;

public class WorkItem extends PSObject implements Cloneable{

	protected String term;
	protected String parentString;
	protected String objective;
	protected boolean runSchedulerOnCreation;
	private LinkedList<String> selectedOptions;


	public WorkItem(String _name, String _term){

		super(_name,PSObjectTypes.WORK_ITEM);
		setTerm(_term);
		setParentString("");
		setObjective("");
		setRunSchedulerOnCreation(false);
		setSelectedOptions(null);
	}

	public Object clone(){
		WorkItem result = new WorkItem(getName(),getTerm());
		result.setParentString(parentString);
		result.setObjective(objective);
		result.setRunSchedulerOnCreation(runSchedulerOnCreation);
		LinkedList<String> clone = (LinkedList<String>)selectedOptions.clone();
		result.setSelectedOptions(clone);
		return result;
	}

	public String getTerm() {
		return term;
	}

	protected void setTerm(String term) {
		this.term = term;
	}

	public String getParentString() {
		return parentString;
	}

	public void setParentString(String parentString) {
		this.parentString = parentString;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public boolean getRunSchedulerOnCreation() {
		return runSchedulerOnCreation;
	}

	public void setRunSchedulerOnCreation(boolean runSchedulerOnCreation) {
		this.runSchedulerOnCreation = runSchedulerOnCreation;
	}

	public LinkedList<String> getSelectedOptions() {
		return selectedOptions;
	}

	public void setSelectedOptions(LinkedList<String> selectedOptions) {
		if(selectedOptions==null){
			this.selectedOptions = new LinkedList<String>();
		} else {
			this.selectedOptions = selectedOptions;
		}
	}

	public void addSelectedOption(String selectedOptionString) {
		if (null == this.selectedOptions) {
			this.selectedOptions = new LinkedList<String>();
		}
		this.selectedOptions.add(selectedOptionString);
	}

	public static WorkItem create(String name, String term){
		if (name == null){
			throw new IllegalArgumentException("Name can't be null");
		}
		if (term == null){
			throw new IllegalArgumentException("Term can't be null");
		}
		return new WorkItem(name, term);
	}

	public static WorkItem createTimeStamped(String name, String term){
		return new WorkItem(new TimeStampName(name).getTimeStampedName(), term);
	}

	public static WorkItem createWorkItemUsingCoreProperties(){
		WorkItem wi = new WorkItem("Default WorkItem",CoreProperties.getProjectTerm());
		wi.setUid(CoreProperties.getDefaultWorkItemIdWithPrefix());
		return wi;

	}

	public int hashCode(){
		int hashCode = 0;
		hashCode = hashCode*31 + this.name.hashCode();
		hashCode = hashCode*31 + this.term.hashCode();
		hashCode = hashCode*31 + this.objective.hashCode();
		hashCode = hashCode*31 + ((runSchedulerOnCreation)?1:0);
		hashCode = hashCode*31 + selectedOptions.hashCode();
		return hashCode;
	}

	public boolean equals(Object obj){
		if (this == obj) {
		    return true;
		}

		if(obj==null){
			return false;
		}

		if(!(obj instanceof WorkItem)){
			return false;
		}

		if(hashCode()!=obj.hashCode()){
			return false;
		}

		WorkItem _wi = (WorkItem) obj;

		if(!term.equals(_wi.getTerm())){
			return false;
		}
		if(!parentString.equals(_wi.getParentString())){
			return false;
		}
		if(!objective.equals(_wi.getObjective())){
			return false;
		}
		if(!runSchedulerOnCreation!=_wi.getRunSchedulerOnCreation()){
			return false;
		}
		if(!selectedOptions.equals(_wi.getSelectedOptions())){
			return false;
		}
		return true;
	}
}
