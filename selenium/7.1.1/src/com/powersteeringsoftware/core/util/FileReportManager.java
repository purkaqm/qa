package com.powersteeringsoftware.core.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class FileReportManager {

    public static final String PROP_NAME_RESULT_TESTNAME = "test.name";

    public static final String PROP_NAME_RESULT_ISOK = "test.result.isok";
    public static final String PROP_PASSED = "1";
    public static final String PROP_FAILED = "0";

    public static final String PROP_NAME_RESULT_MAILSUBTEXT = "mail.subtext";

    private String testName;
    private boolean testResultIsOk;
    private String mailSubtext;

    private String fileName;
    private String comments;

    public FileReportManager() {
        testName = new String();
        testResultIsOk = false;
        mailSubtext = new String();
        fileName = new String();
        comments = new String();
    }

    public FileReportManager(String aTestName, boolean aTestResultIsOk, String aMailSubtext ){
        testName = aTestName;
        testResultIsOk = aTestResultIsOk;
        mailSubtext = aMailSubtext;
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

    public void saveProperties(String _fileName, String _comments) throws IOException{

        Properties prop = new Properties();
        prop.put(PROP_NAME_RESULT_TESTNAME, testName);
        prop.put(PROP_NAME_RESULT_ISOK, testResultIsOk ? FileReportManager.PROP_PASSED : FileReportManager.PROP_FAILED);
        prop.put(PROP_NAME_RESULT_MAILSUBTEXT, mailSubtext);

        prop.store(new FileWriter(_fileName), _comments);
    }

    public void saveProperties() throws IOException{
    	saveProperties(fileName, comments);
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}



}