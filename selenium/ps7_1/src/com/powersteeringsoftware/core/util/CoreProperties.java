package com.powersteeringsoftware.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.thoughtworks.selenium.Wait;

/**
 * Class is for geting core properties such as:<br>
 * selenium.server - selenium server host (e.g localhost)<br>
 * selenium.server.port - selenium server port (e.g 4444)<br>
 * selenium.waitforpagetoload - wait for page to load time in mils (e.g. 30000)<br>
 * browser.default - browser by default (e.g. *iehta,*chrome)<br>
 * application.url.serverhost - (e.g https://qa-solid.cinteractive.com)<br>
 * application.context - context (e.g. /quicktest/Home.page)<br>
 * user.main - user name for login<br>
 * user.main.password - user password for login<br>
 * application.url.homepage - home page URL without server hostname<br>
 * application.url.mt.library - page "Measure Template:Library" url without
 * server hostname<br>
 * pages.properties - page properties file<br>
 * prefix.project - prefix for new object Project<br>
 * prefix.organization- prefix for new object Organization<br>
 * prefix.measure - prefix for new object Measure<br>
 * prefix.attachedmeasure - prefix for new object Attached Measure <br>
 * prefix.measuretemplate - prefix for new object Measure Template<br>
 * etc<br>
 *
 *
 * @author selyaev_ag
 * @since 2008 December 24
 */
public class CoreProperties{

	public final static String APPLICATION_URL_MT_ADD = "AddEditMeasure";
	public final static String APPLICATION_URL_MT_ADD_SP = "SCREATE_TEMPLATE";
	public final static String APPLICATION_URL_MT_LIBRARY = "/admin/measures/MeasureTemplates";
	public final static String APPLICATION_URL_MT_EDIT = "/AddEditMeasure";
	public final static String APPLICATION_URL_MT_EDIT_SP = "SEDIT";
	public final static String APPLICATION_URL_WORKITEM_SUMMARY = "/project/Summary1.epage";
	public final static String APPLICATION_URL_WORKITEM_MANAGE_MEASURES = "/measures/MeasureInstances";
	public final static String APPLICATION_URL_WORKITEM_MEASURE_INSTANCE = "measures/Instance.epage";
	public final static String APPLICATION_URL_ISSUE_VIEW ="DiscussionIssueView.epage";

	public static final String FAILED_TESTS_XML = "testng-failed.xml";
	public static final String LOG4J_PROP_FILE	= "./conf/log4j.properties";

	public final static String APPLICATION_PREFIX_PROJECT = "Project_";
	public final static String APPLICATION_PREFIX_ORGANIZATION = "Organization_";
	public final static String APPLICATION_PREFIX_MEASURE_INSTANCE = "MI_";
	public final static String APPLICATION_PREFIX_MEASURE_TEMPLATE = "MT_";
	public final static String APPLICATION_PREFIX_AUTO = "AUTO_";

	public static final String 	DEFAULT_CORE_PROPERTIES_FILE = "./conf/core.properties";
	public static final int 	DEFAULT_SELENIUM_PORT = 4747;
	public static final long 	DEFAULT_WAIT_FOR_PAGE_TO_LOAD = Wait.DEFAULT_TIMEOUT;

	public static final String DATE_FORMAT = "application.date.format";
	public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";

	private static Properties properties = new Properties();
	private static Logger log = Logger.getLogger(CoreProperties.class);
	private static String propertyFile = DEFAULT_CORE_PROPERTIES_FILE;

	private CoreProperties(){
	}

	/**
	 * Load properties from file core.properties
	 *
	 */
	public static void loadProperties() throws FileNotFoundException, IOException {
		loadProperties(propertyFile);
	}

	public static void loadProperties(String str) throws FileNotFoundException, IOException {
		propertyFile = str;
		File file = new File(str);

		if (!file.exists()) {
			log.error("Properties file for core doesn't not exist. Check the path:"
					  + DEFAULT_CORE_PROPERTIES_FILE);
		}


		properties.load(new FileInputStream(file));

	}

	/**
	 * Get core parameter by key
	 * @param key - the parameter name (or key)
	 * @return parameter as a string
	 * @throws NullPointerException - in case properties are null and there is no parameters
	 */
	public static String getProperty(String key) throws NullPointerException{
		if (null==properties){
			throw new NullPointerException(
					"Before using CoreProperties you should set it. Use loadProperties(...) methods.");
		}
		return properties.getProperty(key);
	}

	public static String getWaitForElementToLoadAsString(){
		return getProperty("selenium.waitforpagetoload");
	}

	public static long getWaitForElementToLoad(){
		try{
			return Long.parseLong(getProperty("selenium.waitforpagetoload"));
		} catch (NumberFormatException nfe){
			return DEFAULT_WAIT_FOR_PAGE_TO_LOAD;
		}
	}


	public static String getWaitForPageToStop(){
		return getProperty("selenium.waitforpagetostop");
	}

	public static String getURLHomePage(){
		return CoreProperties.getProperty("application.url.homepage");
	}

	public static String getURLContext(){
		return CoreProperties.getProperty("application.url.context");
	}

	public static String getURLHomePageWithContext(){
		return getURLContext()+CoreProperties.getURLHomePage();
	}

	public static String getURLServerHost(){
		return getProperty("application.url.serverhost");
	}

	public static String getURLServerWithContext(){
		return getURLServerHost()+getURLContext();
	}


	public static String getURLMTLibrary(){
		return APPLICATION_URL_MT_LIBRARY;
	}

	public static String getURLMTEdit(){
		return APPLICATION_URL_MT_EDIT;
	}

	public static String getURLMTEditPS(){
		return APPLICATION_URL_MT_EDIT_SP;
	}

	public static String getURLMTAdd(){
		return APPLICATION_URL_MT_ADD;
	}

	public static String getURLMTAddPS(){
		return APPLICATION_URL_MT_ADD_SP;
	}

	public static String getDefaultUser(){
		return getProperty("login.username.default");
	}

	public static String getDefaultPassword(){
		return getProperty("login.password.default");
	}

	public static String getSeleniumServerPortAsString(){
		if (StringUtils.isEmpty(getProperty("selenium.server.port"))){
			log.warn("Selenium port was empty. Set as default "+DEFAULT_SELENIUM_PORT);
			return String.valueOf(DEFAULT_SELENIUM_PORT);
		}
		return getProperty("selenium.server.port");
	}

	public static int getSeleniumServerPort(){
		try{
			return new Integer(getProperty("selenium.server.port"));
		} catch(NumberFormatException nfe){
			log.warn("Was error while parsing Selenium port. Set as default "+DEFAULT_SELENIUM_PORT);
			return DEFAULT_SELENIUM_PORT;
		}
	}

	public static String getSeleniumServerHostname(){
		return getProperty("selenium.server.hostname");
	}

	public static String getDefaultBrowser(){
		return getProperty("browser.default");
	}

	public static String getPrefix_Project(){
		return APPLICATION_PREFIX_PROJECT;
	}

	public static String getPrefix_Organization(){
		return APPLICATION_PREFIX_ORGANIZATION;
	}

	public static String getPrefix_Measure(){
		return APPLICATION_PREFIX_MEASURE_INSTANCE;
	}

	public static String getPrefix_MeasureTemplate(){
		return APPLICATION_PREFIX_MEASURE_TEMPLATE;
	}

	public static String getPrefix_AutoCreated(){
		return APPLICATION_PREFIX_AUTO;
	}

	public static String getTestNGFile(){
		return getProperty("testng.properties");
	}

	public static boolean isMultipleLogs(){
		return new Boolean(getProperty("testng.logs.multiplefolders"));
	}

	public static String getLogMainPath(){
		return getProperty("testng.logs.mainpath");
	}

	public static String getDefaultUmbrellaIdWithPrefix(){
		return getProperty("application.object.default.umbrella.id");
	}

	public static String getDefaultUmbrellaIdWithoutPrefix(){
		return getProperty("application.object.default.umbrella.id").substring(3);
	}

	public static String getDefaultWorkItemIdWithPrefix(){
		return getProperty("application.object.default.workitem.id");
	}

	public static String getDefaultWorkItemIdWithoutPrefix(){
		return getProperty("application.object.default.workitem.id").substring(3);
	}

	public static String getURlWISummary(){
		return APPLICATION_URL_WORKITEM_SUMMARY;
	}

	public static String getURlWIManageMeasures(){
		return APPLICATION_URL_WORKITEM_MANAGE_MEASURES;
	}

	public static String getLogMailerPath(){
		return getProperty("testng.logs.mailer.path");	}

	public static String getURLWIMeasureInstance(){
		return APPLICATION_URL_WORKITEM_MEASURE_INSTANCE;
	}

	public static String getMailerFilename(){
		return getProperty("testng.logs.mailer.filename");
	}

	public static String getMailerTestName(){
		return getProperty("testng.logs.mailer.testname");
	}

	public static String getMultipleFolderFormatter(){
		return getProperty("testng.logs.multiplefolders.format");
	}

	public static int getRunFailedCount(){
		return Integer.parseInt(getProperty("testng.tests.runfailedcount"));
	}

	public static String getContextDBString(){
		return getProperty("context.dbcstring");
	}

	public static String getContextDBName(){
		return getProperty("context.dbname");
	}

	public static String getContextDBBupath(){
		return getProperty("context.dbbupath");
	}

	public static String getContextJMX(){
		return getProperty("context.jmx");
	}

	public static String getReportFileFolder(){
		return getProperty("report.file.folder");
	}

	public static boolean isRestoreDB(){
		return Boolean.parseBoolean(getProperty("application.restoredb"));
	}

	public static boolean isRestartResin(){
		return Boolean.parseBoolean(getProperty("application.restartresin"));
	}

	public static String getLocalFolder(){
		return getProperty("local.afolder");
	}

	public static String getServerFolder(){
		return getProperty("sserver.afolder");
	}

	public static String getProjectTerm(){
		return getProperty("project.term");
	}

	public static String getContextDBUser(){
		return getProperty("context.db.user");
	}

	public static String getContextDBPass(){
		return getProperty("context.db.pass");
	}


	public static String getPropertiesString(){
		StringBuffer propString = new StringBuffer("CoreProperties values:\n");
		for(Object key : CoreProperties.properties.keySet()){
			propString.append(key+":	");
			propString.append(CoreProperties.properties.getProperty((String)key));
			propString.append(";\n");
		}
		return propString.toString();
	}

	public static String getIssueTitle(){
		return getProperty("text.issues.title");
	}

	public static String getChampionName(){
		return getProperty("champion.name");
	}


	public static String getDateFormat(){
		if(StringUtils.isEmpty(getProperty(DATE_FORMAT))){
			return DEFAULT_DATE_FORMAT;
		}
		return getProperty(DATE_FORMAT);
	}

}
