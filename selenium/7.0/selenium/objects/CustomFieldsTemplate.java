package selenium.objects;

import java.util.*;

public class CustomFieldsTemplate {

	private String name;
	private String description;

	private Boolean assocWithEvents = false;
	private Boolean assocWithMSPProjects = false;
	private Boolean assocWithMilestones = false;
	private Boolean assocWithTemplates = false;
	private Boolean assocWithFolders = false;
	private Boolean assocWithWorkItems = false;
	private Boolean assocWithUnexpWorkItems = false;
	private Boolean assocWithGatedProjects = false;
	private Boolean assocWithGates = false;
	private Boolean assocWithUsers = false;
	private Boolean assocWithGroups = false;
	private Boolean statusReport = false;

	private ArrayList<PSFieldYesNoButton> fieldsYesNoButtons = null;
	private ArrayList<PSFieldCheckboxes> fieldsCheckboxes = null;
	private ArrayList<PSFieldDate> fieldsDate = null;

	public CustomFieldsTemplate(String name){
		this.name = name;
		this.description = null;

		fieldsYesNoButtons = new ArrayList<PSFieldYesNoButton>();
		fieldsCheckboxes = new ArrayList<PSFieldCheckboxes>();
		fieldsDate = new ArrayList<PSFieldDate>();
	}

	public CustomFieldsTemplate(String name, String description){
		this.name = name;
		this.description = description;

		fieldsYesNoButtons = new ArrayList<PSFieldYesNoButton>();
		fieldsCheckboxes = new ArrayList<PSFieldCheckboxes>();
		fieldsDate = new ArrayList<PSFieldDate>();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getAssocWithEvents() {
		return assocWithEvents;
	}
	public void setAssocWithEvents(Boolean assocWithEvents) {
		this.assocWithEvents = assocWithEvents;
	}
	public Boolean getAssocWithMSPProjects() {
		return assocWithMSPProjects;
	}
	public void setAssocWithMSPProjects(Boolean assocWithMSPProjects) {
		this.assocWithMSPProjects = assocWithMSPProjects;
	}
	public Boolean getAssocWithMilestones() {
		return assocWithMilestones;
	}
	public void setAssocWithMilestones(Boolean assocWithMilestones) {
		this.assocWithMilestones = assocWithMilestones;
	}
	public Boolean getAssocWithTemplates() {
		return assocWithTemplates;
	}
	public void setAssocWithTemplates(Boolean assocWithTemplates) {
		this.assocWithTemplates = assocWithTemplates;
	}
	public Boolean getAssocWithFolders() {
		return assocWithFolders;
	}
	public void setAssocWithFolders(Boolean assocWithFolders) {
		this.assocWithFolders = assocWithFolders;
	}
	public Boolean getAssocWithWorkItems() {
		return assocWithWorkItems;
	}
	public void setAssocWithWorkItems(Boolean assocWithWorkItems) {
		this.assocWithWorkItems = assocWithWorkItems;
	}
	public Boolean getAssocWithUnexpWorkItems() {
		return assocWithUnexpWorkItems;
	}
	public void setAssocWithUnexpWorkItems(Boolean assocWithUnexpWorkItems) {
		this.assocWithUnexpWorkItems = assocWithUnexpWorkItems;
	}
	public Boolean getAssocWithGatedProjects() {
		return assocWithGatedProjects;
	}
	public void setAssocWithGatedProjects(Boolean assocWithGatedProjects) {
		this.assocWithGatedProjects = assocWithGatedProjects;
	}
	public Boolean getAssocWithGates() {
		return assocWithGates;
	}
	public void setAssocWithGates(Boolean assocWithGates) {
		this.assocWithGates = assocWithGates;
	}
	public Boolean getAssocWithUsers() {
		return assocWithUsers;
	}
	public void setAssocWithUsers(Boolean assocWithUsers) {
		this.assocWithUsers = assocWithUsers;
	}
	public Boolean getAssocWithGroups() {
		return assocWithGroups;
	}
	public void setAssocWithGroups(Boolean assocWithGroups) {
		this.assocWithGroups = assocWithGroups;
	}
	public Boolean getStatusReport() {
		return statusReport;
	}
	public void setStatusReport(Boolean statusReport) {
		this.statusReport = statusReport;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addYesNoButtonField(String name, String description){
		this.fieldsYesNoButtons.add(new PSFieldYesNoButton(name, description));
	}

	public ArrayList<PSFieldYesNoButton> getFieldsYesNoButtonsList(){
		return fieldsYesNoButtons;
	}

	public void addCheckboxesField(String name, String description, String[] checkboxesNames){
		this.fieldsCheckboxes.add(new PSFieldCheckboxes(name, description, checkboxesNames));
	}

	public ArrayList<PSFieldCheckboxes> getFieldsCheckboxesList(){
		return fieldsCheckboxes;
	}

	public void addDateField(String name, String description){
		this.fieldsDate.add(new PSFieldDate(name, description));
	}

	public ArrayList<PSFieldDate> getFieldsDateList(){
		return fieldsDate;
	}

//Beans
	private class PSField {
		String name;
		String description;

		private PSField(String name, String description){
			this.name = name;
			this.description = description;
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}
	}

	public class PSFieldYesNoButton extends PSField{
		private PSFieldYesNoButton(String name, String description){
			super(name, description);
		}
	}

	public class PSFieldCheckboxes extends PSField{
		String[] checkboxesNames;

		private PSFieldCheckboxes(String name, String description, String[] checkboxesNames){
			super(name, description);
			this.checkboxesNames = checkboxesNames;
		}

		public String[] getCheckboxesNames(){
			return this.checkboxesNames;
		}
	}

	public class PSFieldDate extends PSField{
		private PSFieldDate(String name, String description){
			super(name, description);
		}
	}
//!Beans
}
