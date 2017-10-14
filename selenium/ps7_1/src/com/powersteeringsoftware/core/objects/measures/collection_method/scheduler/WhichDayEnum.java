package com.powersteeringsoftware.core.objects.measures.collection_method.scheduler;

public enum WhichDayEnum {

	FIRST("1st"),
	SECOND("2nd"),
	THIRD("3rd"),
	THOURTH("4th"),
	LAST("Last");

	String dayString;

	WhichDayEnum(String _dayString){
		dayString = _dayString;
	}
}
