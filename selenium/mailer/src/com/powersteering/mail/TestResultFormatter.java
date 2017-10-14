package com.powersteering.mail;

import java.util.Date;


/**
 * Return formatted string for TestResultReporter object. You can get single line string
 * or string with several lines.
 *
 */
public class TestResultFormatter {

	public enum TestResultFormat{
		SIMPLE,
		SINGLE_STRING;
	}

	private TestResultFormatter(){
	}

	private static String simpleFormatMailBody(TestResultReporter reporter){
		StringBuilder testStatus = new StringBuilder();

		testStatus.append(reporter.getTestName());
		testStatus.append("\n");
		testStatus.append("Status: "+((reporter.getTestResultIsOk()) ? "PASSED" : "FAILED"));
		testStatus.append("\n");
		testStatus.append("Date: "+((reporter.getDate()!=0)?new Date(reporter.getDate()):"can't be defined"));
		testStatus.append("\n");
		testStatus.append("Environment: "+reporter.getEnvironment());
		testStatus.append("\n");
		testStatus.append("Comments: "+reporter.getMailSubtext());
		testStatus.append("\n");

		return testStatus.toString();
	}

	private static String singleStringFormatMailBodyIn(TestResultReporter reporter){
		StringBuilder testStatus = new StringBuilder();
		testStatus.append(reporter.getTestName());
		testStatus.append("; ");
		testStatus.append("Status: "+((reporter.getTestResultIsOk())?"PASSED":"FAILED"));
		testStatus.append("; ");
		testStatus.append("Date: "+((reporter.getDate()!=0)?new Date(reporter.getDate()):"can't be defined"));
		testStatus.append("; ");
		testStatus.append("Environment: "+reporter.getEnvironment());
		testStatus.append("; ");
		testStatus.append("Comments: "+reporter.getMailSubtext());

		return testStatus.toString();

	}


	/**
	 * Create formated string using test properties file
	 *
	 * @param reporter - object for formatting
	 * @param format - the format type
	 * @return formated string
	 */
	public static String formatMailBody(TestResultReporter reporter, TestResultFormat format ){

	switch (format) {
		case SIMPLE:
			return simpleFormatMailBody(reporter);
		case SINGLE_STRING:
			return singleStringFormatMailBodyIn(reporter);
		default:
			return simpleFormatMailBody(reporter);
		}

	}
}
