package selenium.objects;

import java.util.LinkedList;

public class PSProcess {
	String name;
	String description;
	LinkedList<Phase> phasesList;

	public PSProcess(String name, String description){
		this.name = name;
		this.description = description;
		phasesList = new LinkedList<Phase>();
	}

	public void addPhase(String phaseName, String phasePercent){
		phasesList.add(new Phase(phaseName, phasePercent));
	}

	public LinkedList<Phase> getPhasesList(){
		return phasesList;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public class Phase {
		private String name;
		private String percent;

		protected Phase(String name, String percent){
			this.name = name;
			this.percent = percent;
		}

		public String getName() {
			return name;
		}

		public String getPercent() {
			return percent;
		}

	}

}
