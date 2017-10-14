package com.powersteering.mail;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

/**
 * Class for working with files that contain report about test results. Test
 * result files must be properties files with properties as constants:<br>
 * - PROP_NAME_RESULT_TESTNAME,<br>
 * - PROP_NAME_RESULT_ISOK,<br>
 * - PROP_NAME_RESULT_MAILSUBTEXT,<br>
 * - PROP_NAME_RESULT_DATE,<br>
 * - PROP_NAME_RESULT_ENVIRONMENT<br>
 *
 */
public class TestResultReporter{

	/**
	 * This property describe test name
	 */
	public static final String PROP_NAME_RESULT_TESTNAME = "test.name";
	/**
	 * This property describe test result (can be 1 - PASSED or 0 - FAILED)
	 */
	public static final String PROP_NAME_RESULT_ISOK = "test.result.isok";
	/**
	 * This property describe description of the passed/failed test
	 */
	public static final String PROP_NAME_RESULT_MAILSUBTEXT = "mail.subtext";
	/**
	 * This property contains end date of the test (in milliseconds)
	 */
	public static final String PROP_NAME_RESULT_DATE = "mail.date";
	/**
	 * This property describe test environment: application name, build and db versions
	 */
	public static final String PROP_NAME_RESULT_ENVIRONMENT = "mail.environment";

	private String testName;
	private boolean testResultIsOk;
	private String mailSubtext;
	private long date;
	private String environment;

	public TestResultReporter() {
		testName = new String();
		testResultIsOk = false;
		mailSubtext = new String();
		date = 0;
		environment = new String();
	}

	public TestResultReporter(String aTestName, boolean aTestResultIsOk,
			String aMailSubtext, long aDate, String aEnvironment) {
		testName = aTestName;
		testResultIsOk = aTestResultIsOk;
		mailSubtext = aMailSubtext;
		date = aDate;
		environment = aEnvironment;
	}


	public long getDate() {
		return date;
	}

	public void setDate(long mils) {
		this.date = mils;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getTestName(){
		return testName;
	}

	public boolean getTestResultIsOk(){
		return testResultIsOk;
	}

	public String getMailSubtext(){
		return mailSubtext;
	}

	public void setTestName(String aTestName){
		testName = aTestName;
	}

	public void setTestResultIsOk(boolean newTestResult){
		testResultIsOk = newTestResult;
	}

	public void setMailSubtext(String newMailSubtext){
		mailSubtext = newMailSubtext;
	}


	/**
	 * Load all properties from the file
	 */
	public void loadProperties(File file) throws MailException, IOException{
		if(!file.exists()){
			throw new MailException("Properties file"+file+" is not exist. We can't load properties.");
		}

		Properties prop = new Properties();
		Reader reader;
		reader = new FileReader(file);
		prop.load(reader);
		reader.close();
		this.testName = prop.getProperty(PROP_NAME_RESULT_TESTNAME)!=null?prop.getProperty(PROP_NAME_RESULT_TESTNAME):"";
		this.testResultIsOk = prop.getProperty(PROP_NAME_RESULT_ISOK).equals("1");
		this.mailSubtext = prop.getProperty(PROP_NAME_RESULT_MAILSUBTEXT)!= null ? prop.getProperty(PROP_NAME_RESULT_MAILSUBTEXT):"";

		try {
			this.date = Long.parseLong(prop.getProperty(PROP_NAME_RESULT_DATE));
		} catch(NumberFormatException nfe){
			this.date = 0;
		}

		this.environment = prop.getProperty(PROP_NAME_RESULT_ENVIRONMENT)!=null?prop.getProperty(PROP_NAME_RESULT_ENVIRONMENT):"";
	}


	/**
	 * Save all data in the properties file with comments
	 */
	public void saveProperties(File file, String comments) throws MailException, IOException{
		if(!file.exists()){
			throw new MailException("File "+file+" is not exist. We can't store properties.");
		}
		Properties prop = new Properties();
		prop.put(PROP_NAME_RESULT_TESTNAME, testName);
		prop.put(PROP_NAME_RESULT_ISOK, (testResultIsOk)?1:0);
		prop.put(PROP_NAME_RESULT_MAILSUBTEXT, mailSubtext);
		prop.put(PROP_NAME_RESULT_DATE,date);
		prop.put(PROP_NAME_RESULT_ENVIRONMENT,environment);

		Writer writer = new FileWriter(file);
		prop.store(writer, comments);
	}

}
