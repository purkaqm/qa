/**
 * 
 */
package com.powersteeringsoftware.core.objects.measures;

public enum MeasureIndicatorTypeEnum{
	GOAL(0),
	VARIANCE(1),
	NONE(2);

	private int index;
	MeasureIndicatorTypeEnum(int indx){
		index = indx;
	}

	public int getIndex() {
		return index;
	}
}