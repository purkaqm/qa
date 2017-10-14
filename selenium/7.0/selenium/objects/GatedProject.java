package selenium.objects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TimeZone;

public class GatedProject implements IProject{

	private String name;
	private String term;

	private String objective;
	private Boolean runSchedulerOnCreation;
	private String parentString;

	private LinkedList<String> selectedOptions;

	private LinkedList<String> champions;

	private GatedProject(String name, String term){
		this.name = name;
		this.term = term;

		this.objective = null;
		this.runSchedulerOnCreation = null;
		this.parentString = null;

		this.selectedOptions = null;

		this.selectedOptions = new LinkedList<String>();
		this.champions = new LinkedList<String>();
	}

	public static GatedProject create(String name, String term){
		return new GatedProject(name, term);
	}

	public static GatedProject createWithNameTimeStamped(String name, String term){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss_z", Locale.US);
		formatter.setTimeZone(TimeZone.getTimeZone("EDT"));
		return new GatedProject(name + "_" + formatter.format(new Date()), term);
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
		this.selectedOptions.add(selectedOptionString);
	}

	public LinkedList<String> getSelectedOptionsList() {
		return selectedOptions;
	}

	public void addChampion(String championName) {
		this.champions.add(championName);
	}

	public LinkedList<String> getChampionsList() {
		return champions;
	}
}
