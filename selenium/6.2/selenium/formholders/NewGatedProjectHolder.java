package selenium.formholders;

import java.util.*;

public class NewGatedProjectHolder {
	private String projectName;
	private String projectType;
	private String projectDescription;

	private LinkedList<NewGatedProjectDateField> dateFieldsList;
	private LinkedList<String> projectTeamMembers;
	private LinkedList<String> projectChampions;

	//TODO implement tags and custom fields handling

	public NewGatedProjectHolder(String projectName, String projectType){
		this.projectName = projectName;
		this.projectType = projectType;
		dateFieldsList = null;
		projectTeamMembers = null;
		projectChampions = null;
		projectDescription = null;
	}

	public void setProjectDescription(String projectDescription){
		this.projectDescription = projectDescription;
	}

	public void addProjectDateField(String dataFieldName, String dataFieldValue){
		if (null == dateFieldsList) dateFieldsList = new LinkedList<NewGatedProjectDateField>();
		dateFieldsList.add(new NewGatedProjectDateField(dataFieldName, dataFieldValue));
	}

	public void addProjectTeamMember(String projectTeamMember){
		if (null == projectTeamMembers) projectTeamMembers = new LinkedList<String>();
		projectTeamMembers.add(projectTeamMember);
	}

	public void addProjectChampion(String projectChampion){
		if (null == projectChampions) projectChampions = new LinkedList<String>();
		projectChampions.add(projectChampion);
	}

	public String getProjectName() {
		return projectName;
	}

	public String getProjectType() {
		return projectType;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public LinkedList<NewGatedProjectDateField> getDateFieldsList() {
		return dateFieldsList;
	}

	public LinkedList<String> getProjectTeamMembers() {
		return projectTeamMembers;
	}

	public LinkedList<String> getProjectChampions() {
		return projectChampions;
	}


}
