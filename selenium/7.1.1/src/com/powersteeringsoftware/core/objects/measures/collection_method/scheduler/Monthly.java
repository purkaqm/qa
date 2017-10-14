package com.powersteeringsoftware.core.objects.measures.collection_method.scheduler;

import org.testng.Assert;

public class Monthly extends AbstractSchedulerType {

	private boolean dayPart = true;
	private int day = 1;

	private int every = 1;

	private WhichDayEnum whichDay = WhichDayEnum.FIRST;
	private WeekDayEnum weekDay = WeekDayEnum.SUNDAY;
	private String timeOfDay = "12:00 AM";



	public Monthly(){
		super(SchedulerTypesEnum.MONTHLY);
	}

	public boolean isDayPart() {
		return dayPart;
	}

	public void setDayPart(boolean dayPart) {
		this.dayPart = dayPart;
	}

	public int getDay() {
		assertIsDayPart();
		return day;
	}

	public void setDay(int day) {
		assertIsDayPart();
		this.day = day;
	}

	public int getEvery() {
		assertIsNotDayPart();
		return every;
	}

	public void setEvery(int everyMonth) {
		assertIsNotDayPart();
		this.every = everyMonth;
	}

	public WhichDayEnum getWhichDay() {
		assertIsNotDayPart();
		return whichDay;
	}

	public void setWhichDay(WhichDayEnum whichDay) {
		assertIsNotDayPart();
		this.whichDay = whichDay;
	}

	public WeekDayEnum getWeekDay() {
		assertIsNotDayPart();
		return weekDay;
	}

	public void setWeekDay(WeekDayEnum weekDay) {
		assertIsNotDayPart();
		this.weekDay = weekDay;
	}

	public String getTimeOfDay() {
		assertIsNotDayPart();
		return timeOfDay;
	}

	public void setTimeOfDay(String timeOfDay) {
		assertIsNotDayPart();
		this.timeOfDay = timeOfDay;
	}

	public void assertIsDayPart(){
		Assert.assertTrue(this.dayPart,"Before invoking this method you must select 'Day...' part - use setDayPart()");
	}

	public void assertIsNotDayPart(){
		Assert.assertFalse(this.dayPart,"Before invoking this method you must select 'The..' part - use setDayPart()");
	}

}
