package com.powersteeringsoftware.core.objects.works;

import java.util.LinkedList;
import com.powersteeringsoftware.core.util.TimeStampName;

public class GatedProject extends WorkItem{

	private LinkedList<String> champions;

	public GatedProject(String _name, String _term){
		super(_name,_term);
		this.champions = new LinkedList<String>();
	}

	public static GatedProject create(String name, String term){
			return new GatedProject(name, term);
	}

	public static GatedProject createTimeStamped(String name, String term){
		return new GatedProject(new TimeStampName(name).getTimeStampedName(), term);
	}

//	public Boolean getRunSchedulerOnCreation() {
//		return runSchedulerOnCreation;
//	}

//	public void setRunSchedulerOnCreation(Boolean runSchedulerOnCreation) {
//		this.runSchedulerOnCreation = runSchedulerOnCreation;
//	}

	public void addChampion(String championName) {
		this.champions.add(championName);
	}

	public LinkedList<String> getChampionsList() {
		return champions;
	}
}
