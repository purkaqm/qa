package com.powersteeringsoftware.core.objects.measures.collection_method.scheduler;

public enum WeekDayEnum {
	SUNDAY("Sunday"),
	MONDAY("Monday"),
	TUESDAY("Tuesday"),
	WEDNESDAY("Wednesday"),
	THURSDAY("Thursday"),
	FRIDAY("Friday"),
	SATURDAY("Saturday"),
	DAY("Day"),
	WEEKDAY("Weekday"),
	WEEKEND_DAY("Weekend Day");

	String weekDay;

	WeekDayEnum(String _weekDay){
		weekDay = _weekDay;
	}
}
