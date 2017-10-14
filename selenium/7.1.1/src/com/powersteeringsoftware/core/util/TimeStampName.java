package com.powersteeringsoftware.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeStampName {
	public static final String DEFAULT_TIME_PATTERN = "yyyy.MM.dd_HH:mm:ss_z";
	public static final Locale DEFAULT_LOCALE = Locale.US;
	public static final String DEFAULT_TIME_ZONE = "EDT";

	private String timePattern;
	private String timeZone;
	private Locale locale;
	private String name;

	public TimeStampName(String _name){
		this(_name, DEFAULT_TIME_PATTERN,DEFAULT_TIME_ZONE,DEFAULT_LOCALE);
	}

	public TimeStampName(String _name, String _timeTimePattern, String _timeZone, Locale _locale){
		name 		= _name;
		timePattern = _timeTimePattern;
		timeZone 	= _timeZone;
		locale 		= _locale;
	}

	public String getTimeStampedName(){
		SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_TIME_PATTERN, DEFAULT_LOCALE);
		formatter.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
		return	name + "_" + formatter.format(new Date());
	}

	protected String getTimePattern() {
		return timePattern;
	}

	protected void setTimePattern(String timeTimePattern) {
		this.timePattern = timeTimePattern;
	}

	protected String getTimeZone() {
		return timeZone;
	}

	protected void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	protected Locale getLocale() {
		return locale;
	}

	protected void setLocale(Locale locale) {
		this.locale = locale;
	}


}
