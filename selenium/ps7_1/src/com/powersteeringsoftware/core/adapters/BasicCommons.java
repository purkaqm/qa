package com.powersteeringsoftware.core.adapters;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.session.TestSession;
import com.powersteeringsoftware.core.util.session.TestSessionObjectNames;
import com.thoughtworks.selenium.DefaultSelenium;

public class BasicCommons {
	private static Logger log = Logger.getLogger(BasicCommons.class);

	private BasicCommons(){
	}

	public static boolean isLoadedHomePage(){
		DefaultSelenium seleniumDriver = SeleniumDriverSingleton.getDriver();
		String homePage = CoreProperties.getURLServerHost()
						  +CoreProperties.getURLHomePageWithContext();
		String currentLocation = seleniumDriver.getLocation();
		boolean result = currentLocation.startsWith(homePage);
		if (result!=true){
			log.debug("Locations are different for needed:"
					+ homePage
					+ " and current: "+seleniumDriver.getLocation());
		}
		return result;
	}

	public static boolean loadHomePage(){
		DefaultSelenium seleniumDriver = SeleniumDriverSingleton.getDriver();
		seleniumDriver.open(CoreProperties.getURLHomePageWithContext());
		seleniumDriver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
		return isLoadedHomePage();
	}

	public static boolean logIn(String user, String pass){
		log.debug("Logining has started.");
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();

		// add application description
		String appDescription = driver.getEval("window.dojo.query('#login h5')[0].innerHTML;");
		log.info("Application description is "+appDescription);
		log.debug("server version string is: "+TestSessionObjectNames.APPLICATION_VERSION.getObjectKey());
		TestSession.putObject(TestSessionObjectNames.APPLICATION_VERSION.getObjectKey(), StringUtils.isEmpty(appDescription)?"":appDescription);

		driver.type("pass",pass);
		driver.type("user",user);
		driver.click("Submit_0");
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
		log.debug("Login has passed.");
		return isLoadedHomePage();
	}

	public static boolean logIn(){
		return logIn(CoreProperties.getDefaultUser(),CoreProperties.getDefaultPassword());
	}

	/**
	 * Check if passed substring of URL is contained in the current loaded page.
	 * @param urlSubstr
	 * @return true - if page URL contains passed parameter urlSubstring
	 */
	public static boolean isPageURLContains(String urlSubstr){
		return SeleniumDriverSingleton.getDriver().getLocation().contains(urlSubstr);
	}


	public static void selectPageTopFrame(){
		SeleniumDriverSingleton.getDriver().selectFrame("relative=top");
	}

	public static void selectAdminPageFrame(){
		SeleniumDriverSingleton.getDriver().selectFrame("dom=window.frames['jspFrame']");
	}

}
