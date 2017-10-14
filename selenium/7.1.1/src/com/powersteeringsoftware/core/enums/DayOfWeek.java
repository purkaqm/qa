package com.powersteeringsoftware.core.enums;

/**
 * First day of week is SUNDAY. Count starts from 1 (Sunday=1, Monday=2 etc)
 *
 * @author selyaev_ag
 * since 1-b11
 */
public enum DayOfWeek {

	SUNDAY(1,"Sunday"),
	MONDAY(2,"Monday"),
	TUESDAY(3,"Tuesday"),
	WEDNESDAY(4,"Wednesday"),
	THURSDAY(5,"Thursday"),
	FRIDAY(6,"Friday"),
	SATURDAY(7,"Saturday");

	int dayOfWeek=1;
	String description;
	DayOfWeek(int _dayOfWeek, String _description){
		dayOfWeek = _dayOfWeek;
		description = _description;
	}

}
