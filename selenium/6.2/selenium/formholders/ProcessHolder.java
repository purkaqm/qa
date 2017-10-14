package selenium.formholders;

import java.util.LinkedList;

public class ProcessHolder {
	String processName;
	String processDescription;
	LinkedList<ProcessPhase> phasesList;

	public ProcessHolder(String processName, String processDescription){
		this.processName = processName;
		this.processDescription = processDescription;
		phasesList = new LinkedList<ProcessPhase>();
	}

	public void addPhase(String phaseName, String phasePercent){
		phasesList.add(new ProcessPhase(phaseName, phasePercent));
	}

	public LinkedList<ProcessPhase> getList(){
		return phasesList;
	}

	public String getProcessName() {
		return processName;
	}

	public String getProcessDescription() {
		return processDescription;
	}
}
