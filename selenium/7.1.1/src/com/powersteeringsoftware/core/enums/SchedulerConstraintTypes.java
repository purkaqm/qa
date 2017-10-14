package com.powersteeringsoftware.core.enums;

/**
 * Schedule constraints for work items
 * @author selyaev_ag
 *
 */
public enum SchedulerConstraintTypes {
	AS_SOON_AS_POSSIBLE("As Soon As Possible","value=regexp:As\\sSoon\\sAs\\sPossible"),
	AS_LATE_AS_POSSIBLE("As Late As Possible","value=regexp:As\\sLate\\sAs\\sPossible"),
	MUST_START_ON("Must Start On","value=regexp:Must\\sStart\\sOn"),
	MUST_FINISH_ON("Must Finish On","value=regexp:Must\\sFinish\\sOn"),
	START_NO_EARLIER_THEN("Start No Earlier Than","value=regexp:Start\\sNo\\sEarlier\\sThan"),
	START_NO_LATER_THEN("Start No Later Than","value=regexp:Start\\sNo\\sLater\\sThan"),
	FINISH_NO_EARLIER_THEN("Finish No Earlier Than","value=regexp:Finish\\sNo\\sEarlier\\sThan"),
	FINISH_NO_LATER_THEN("Finish No Later Than","value=regexp:Finish\\sNo\\sLater\\sThan"),
	FIXED_DATES("Fixed Dates","value=regexp:Fixed\\sDates");

	private String htmlPattern;
	private String value;

	SchedulerConstraintTypes(String _value, String _htmlPattern) {
		htmlPattern = _htmlPattern;
		value = _value;
	}

	public String getPattern() {
		return htmlPattern;
	}

	public String getValue(){
		return value;
	}
}
