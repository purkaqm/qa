/**
 * 
 */
package com.powersteeringsoftware.core.objects.measures;

public enum  MeasureLockedFieldsEnum{
	NAME(0,"checkbox"),
	DESCRIPTION(1,"checkbox_0"),
	UNITS(2,"checkbox_1"),
	DATA_COLLECTION(3,"checkbox_2"),
	FORMULA(4,"checkbox_3"),
	DISPLAY_FORMAT(6,"checkbox_5"),
	EFFECTIVE_DATES(7,"checkbox_6"),
	TEST_SCHEDULE(8,"checkbox_7"),
	HISTORY_SCHEDULE(9,"checkbox_8"),
	REMINDER_SHEDULE(10,"checkbox_8"),
	START_DATE(11,"checkbox_9"),
	TARGET_DATE(12,"checkbox_10"),
	TARGET_VALUE(13,"checkbox_11"),
	GOAL_MESSAGES(14,"checkbox_12"),
	GOAL_THRESHOLDS(15,"checkbox_13"),
	VARIANCE_MESSAGES(16,"checkbox_14"),
	VARIANCE_THRESHOLDS(17,"checkbox_15");

	private int index;
	private String fieldId;

	MeasureLockedFieldsEnum(int indx, String id){
		fieldId = id;
		index = indx;
	}

	public int getIndex() {
		return index;
	}

	public String getFieldId() {
		return fieldId;
	}
}