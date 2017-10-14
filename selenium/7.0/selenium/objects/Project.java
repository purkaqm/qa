package selenium.objects;

import java.text.SimpleDateFormat;
import java.util.*;

public class Project implements IProject{

	private String name;
	private String term;

	private String objective;
	private Boolean runSchedulerOnCreation;
	private String parentString;

	private LinkedList<String> selectedOptions;

	private Project(String name, String term){
		this.name = name;
		this.term = term;

		this.objective = null;
		this.runSchedulerOnCreation = null;
		this.parentString = null;

		this.selectedOptions = null;
	}

	public static Project create(String name, String term){
		return new Project(name, term);
	}

	public static Project createWithNameTimeStamped(String name, String term){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss_z", Locale.US);
		formatter.setTimeZone(TimeZone.getTimeZone("EDT"));
		return new Project(name + "_" + formatter.format(new Date()), term);
	}

	public String getName() {
		return name;
	}

	public String getTerm() {
		return term;
	}

	public String getObjective() {
		return objective;
	}

	public Boolean getRunSchedulerOnCreation() {
		return runSchedulerOnCreation;
	}

	public String getParentString() {
		return parentString;
	}

	public LinkedList<String> getSelectedOptions() {
		return selectedOptions;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public void setRunSchedulerOnCreation(Boolean runSchedulerOnCreation) {
		this.runSchedulerOnCreation = runSchedulerOnCreation;
	}

	public void setParentString(String parentString) {
		this.parentString = parentString;
	}

	public void addSelectedOption(String selectedOptionString) {
		if (null == this.selectedOptions) this.selectedOptions = new LinkedList<String>();
		this.selectedOptions.add(selectedOptionString);
	}

}
