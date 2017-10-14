package com.powersteeringsoftware.core.objects.measures;

/**
 * Schedule items: Reminder, History, Test
 *
 */
public enum MeasureShedulesEnum{
	NEVER(0),
	DAILY(1),
	WEEKLY(2),
	MONTHLY(3),
	QUARTERLY(4);

	private int index;

	MeasureShedulesEnum(int aIndex){
		index = aIndex;
	}

	public int getIndex(){
		return index;
	}
}