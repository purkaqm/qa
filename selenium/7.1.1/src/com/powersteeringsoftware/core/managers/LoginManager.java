package com.powersteeringsoftware.core.managers;

import org.apache.log4j.Logger;

import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.session.TestSession;
import com.powersteeringsoftware.core.util.session.TestSessionObjectNames;
import com.thoughtworks.selenium.DefaultSelenium;

public class LoginManager{

	private Logger logger = Logger.getLogger(LoginManager.class);

	public LoginManager(){
	}

	public String login(String userName, String password) throws InterruptedException {
		String pageTitle = new String();
		String versionString = new String();

		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();

		pageTitle = driver.getTitle();
		if (!(pageTitle.contains("Login")))
			throw new IllegalStateException("Wrong page: Login page expected but found " + pageTitle);

		versionString = driver.getText("dom=window.dojo.query('div#login > h5')[0]");

		logger.info("Logging in as " + userName + "/" + password);
		driver.type("dom=window.dojo.byId('user')", userName);
		driver.type("dom=window.dojo.byId('pass')", password);
		driver.click("dom=window.dojo.byId('Submit_0')");
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

		pageTitle = driver.getTitle();
		if (!(pageTitle.contains("Home")))
			throw new IllegalStateException("Wrong page: Home page expected but found " + pageTitle);

		if(versionString.contains("7.0")){
			TestSession.putObject(TestSessionObjectNames.APPLICATION_VERSION.getObjectKey(), "7.0");
		} else if (versionString.contains("7.1")) {
			TestSession.putObject(TestSessionObjectNames.APPLICATION_VERSION.getObjectKey(), "7.1");
		} else{
			TestSession.putObject(TestSessionObjectNames.APPLICATION_VERSION.getObjectKey(), "undefined");
		}


		return versionString;
	}

}
