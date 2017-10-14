package selenium.formholders;

public class ProcessPhase {
	private String phaseName;
	private String phasePercent;

	protected ProcessPhase(String phaseName, String phasePercent){
		this.phaseName = phaseName;
		this.phasePercent = phasePercent;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public String getPhasePercent() {
		return phasePercent;
	}

}
