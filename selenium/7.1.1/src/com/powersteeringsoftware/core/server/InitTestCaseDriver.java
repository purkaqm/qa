package com.powersteeringsoftware.core.server;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.powersteeringsoftware.core.adapters.BasicCommons;
import com.powersteeringsoftware.core.util.CoreProperties;

/**
 * @deprecated
 * See SeleniumDriverSingleton.setDriverUsingCoreProperties();
 */

public class InitTestCaseDriver{
	private Logger log = Logger.getLogger(InitTestCaseDriver.class);

	public InitTestCaseDriver(){
	}

	@Test(groups = { "init.selenium" }, testName = "init.selenium")
	public void startSelenium() {
		log.info("Selenium driver initialization started.");

		String aServer=CoreProperties.getSeleniumServerHostname();
		int aPort=new Integer(CoreProperties.getSeleniumServerPortAsString());
		String aBrowser=CoreProperties.getDefaultBrowser();
		String aUrl=CoreProperties.getURLServerHost();
		String aContext=CoreProperties.getURLContext();

		SeleniumDriverSingleton.init(aServer, aPort, aBrowser,aUrl, aContext);
		log.info("Selenium driver is initialized.");
	}

	@Test(groups = { "init.login" }, testName = "init.login")
	public void loginPS() {
		log.info("Try to login.");
		BasicCommons.logIn();
		log.info("Has logined.");
	}


}
